package com.heyletscode.ihavetofly;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    private Paint paint;
    private Bird[] birds;
    private Dino[] dinos;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Flight flight;
    private GameActivity activity;
    private Background background_back,background_mid1,background_mid2,background_front1, background_front2;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;
        background_back=new Background(screenX, screenY, getResources(),R.drawable.country_platform_back);
        background_mid1=new Background(screenX, screenY, getResources(),R.drawable.country_platform_forest);
        background_mid2=new Background(screenX, screenY, getResources(),R.drawable.country_platform_forest);
        background_front1 = new Background(screenX, screenY, getResources(),R.drawable.country_platform_tiles_example);
        background_front2 = new Background(screenX, screenY, getResources(),R.drawable.country_platform_tiles_example);

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        background_front2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        birds = new Bird[4];
        dinos= new Dino[2];

        for (int i = 0;i < 4;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        for (int i = 0;i < 2;i++) {

            Dino dino = new Dino(getResources());
            dinos[i] = dino;

        }

        random = new Random();

    }

    @Override
    public void run() {

        while (isPlaying) {

            update ();
            draw ();
            sleep ();

        }

    }

    private void update () {
//        background_mid1.x -= 5 * screenRatioX;
//        background_mid2.x -= 5 * screenRatioX;

        background_front1.x -= 10 * screenRatioX;
        background_front2.x -= 10 * screenRatioX;

//        if (background_mid1.x + background_mid1.background.getWidth() < 0) {
//            background_mid1.x = screenX;
//        }
//
//        if (background_mid2.x + background_mid2.background.getWidth() < 0) {
//            background_mid2.x = screenX;
//        }

        if (background_front1.x + background_front1.background.getWidth() < 0) {
            background_front1.x = screenX;
        }

        if (background_front2.x + background_front2.background.getWidth() < 0) {
            background_front2.x = screenX;
        }

        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 50 * screenRatioX;

            for (Bird bird : birds) {

                if (Rect.intersects(bird.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;

                }

            }

            for (Dino dino : dinos) {

                if (Rect.intersects(dino.getCollisionShape(),
                        bullet.getCollisionShape())) {

                    score++;
                    dino.x = -500;
                    bullet.x = screenX + 500;
                    dino.wasShot = true;

                }

            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (Bird bird : birds) {

            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

//                if (!bird.wasShot) {
//                    isGameOver = true;
//                    return;
//                }

                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt(bound);

                if (bird.speed < 20* screenRatioX)
                    bird.speed = (int) (20 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);

                bird.wasShot = false;
            }

            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

        for (Dino dino : dinos) {

            dino.x -= dino.speed;

            if (dino.x + dino.width < 0) {

//                if (!dino.wasShot) {
//                    isGameOver = true;
//                    return;
//                }

                int bound = (int) (30 * screenRatioX);
                dino.speed = random.nextInt(bound);

                if (dino.speed < 10 * screenRatioX)
                    dino.speed = (int) (10 * screenRatioX);

                dino.x = screenX;
                dino.y = screenY-dino.height-100;

                dino.wasShot = false;
            }

            if (Rect.intersects(dino.getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

    }

    private void draw () {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background_back.background, background_back.x, background_back.y, paint);
            canvas.drawBitmap(background_mid1.background, background_mid1.x, background_mid1.y, paint);
            canvas.drawBitmap(background_mid2.background, background_mid2.x, background_mid2.y, paint);
            canvas.drawBitmap(background_front1.background, background_front1.x, background_front1.y, paint);
            canvas.drawBitmap(background_front2.background, background_front2.x, background_front2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
            for (Dino dino : dinos)
                canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting ();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets)
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

            getHolder().unlockCanvasAndPost(canvas);

        }

    }

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void saveIfHighScore() {

        if (prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {

        isPlaying = true;
        thread = new Thread(this);
        thread.start();

    }

    public void pause () {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;
        }

        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false))
            soundPool.play(sound, 1, 1, 0, 0, 1);

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);
        bullets.add(bullet);

    }
}
