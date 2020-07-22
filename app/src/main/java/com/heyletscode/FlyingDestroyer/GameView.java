package com.heyletscode.FlyingDestroyer;

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
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    public int update_count=0,num_birds,num_zombies,num_greybirds,num_dinos,num_rockets;
    private boolean isPlaying, isGameOver = false;
    private int screenX, screenY, score = 0;
    public static float screenRatioX, screenRatioY;
    public static double screenInches;
    public int[] change;
    private Paint paint;
    private Bird[] birds;
    private Dino[] dinos;
    private GreyBird[] greybirds;
    private Zombie[] zombies;
    private Rocket[] rockets;
    private SharedPreferences prefs;
    private Random random;
    private SoundPool soundPool;
    private List<Bullet> bullets;
    private int sound;
    private Flight flight;
    private GameActivity activity;
    private Background bg_ground1,bg_ground2,bg_treerocks1,bg_treerocks2,bg_hillscastle1,bg_hillscastle2,bg_clouds1,bg_clouds2,bg_hills,bg_rocks,bg_sky;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    public GameView(GameActivity activity, int screenX, int screenY, double screenInches) {
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
        this.screenInches=screenInches;

        Log.d("debug","vivo screenInches:"+this.screenInches);
        change=new int[9];
        for(int i=0;i<9;i++){
            change[i]=0;
        }

        screenRatioX = 1440f / screenX;
        screenRatioY = 720f / screenY;
//        background_back=new Background(screenX, screenY, getResources(),R.drawable.country_platform_back);
//        background_mid1=new Background(screenX, screenY, getResources(),R.drawable.country_platform_forest);
//       // background_mid2=new Background(screenX, screenY, getResources(),R.drawable.country_platform_forest);
//        background_front1 = new Background(screenX, screenY, getResources(),R.drawable.country_platform_tiles_example);
//        background_front2 = new Background(screenX, screenY, getResources(),R.drawable.country_platform_tiles_example);
        bg_ground1=new Background(screenX, screenY, getResources(),R.drawable.layer01_ground);
        bg_ground2=new Background(screenX, screenY, getResources(),R.drawable.layer01_ground);

        bg_treerocks1=new Background(screenX, screenY, getResources(),R.drawable.layer02_trees_rocks);
        bg_treerocks2=new Background(screenX, screenY, getResources(),R.drawable.layer02_trees_rocks);

        bg_hillscastle1=new Background(screenX, screenY, getResources(),R.drawable.layer03_hills_castle);
        bg_hillscastle2=new Background(screenX, screenY, getResources(),R.drawable.layer03_hills_castle);

        bg_clouds1=new Background(screenX, screenY, getResources(),R.drawable.layer04_clouds);
        bg_clouds2=new Background(screenX, screenY, getResources(),R.drawable.layer04_clouds);

        bg_hills=new Background(screenX, screenY, getResources(),R.drawable.layer05_hills);
        bg_rocks=new Background(screenX, screenY, getResources(),R.drawable.layer06_rocks);
        bg_sky=new Background(screenX, screenY, getResources(),R.drawable.layer07_sky);

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

//        background_front2.x = screenX;
        bg_ground2.x=bg_ground1.background.getWidth();
        bg_treerocks2.x=bg_treerocks1.background.getWidth();
        bg_hillscastle2.x=bg_hillscastle1.background.getWidth();
        bg_clouds2.x=bg_clouds1.background.getWidth();

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

         num_birds=4;
         num_dinos=1;
         num_zombies=1;
         num_greybirds=3;
         num_rockets=num_zombies;

        birds = new Bird[num_birds];
        dinos= new Dino[num_dinos];
        greybirds=new GreyBird[num_greybirds];
        zombies=new Zombie[num_zombies];
        rockets= new Rocket[num_rockets];
        for (int i = 0;i < num_birds;i++) {

            Bird bird = new Bird(getResources());
            birds[i] = bird;

        }

        for (int i = 0;i < num_dinos;i++) {

            Dino dino = new Dino(getResources());
            dinos[i] = dino;

        }

        for (int i = 0;i < num_zombies;i++) {

            Zombie zombie = new Zombie(getResources());
            zombies[i] = zombie;

        }

        for (int i = 0;i < num_greybirds;i++) {

            GreyBird greybird = new GreyBird(getResources());
            greybirds[i] = greybird;
        }

        for (int i = 0;i < num_rockets;i++) {

            Rocket rocket = new Rocket(getResources());
            rocket.x=-500;
            rockets[i] = rocket;
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
          int i,j,k;
        bg_ground1.x-=10*screenRatioX;
        bg_ground2.x-=10*screenRatioX;
        int score_limit_for_background_change=50;
        if (bg_ground1.x + bg_ground1.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[0]==0){
                change[0]++;
                bg_ground1=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_01_ground);
            }
            bg_ground1.x = bg_ground1.background.getWidth();
        }

        if (bg_ground2.x + bg_ground2.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[1]==0){
                change[1]++;
                bg_ground2=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_01_ground);
            }
            bg_ground2.x = bg_ground2.background.getWidth();
        }


        bg_treerocks1.x-=10*screenRatioX;
        bg_treerocks2.x-=10*screenRatioX;
        if (bg_treerocks1.x + bg_treerocks1.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[2]==0){
                change[2]++;
                bg_treerocks1=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_02_trees);
            }
            bg_treerocks1.x = bg_treerocks1.background.getWidth();
        }
        if (bg_treerocks2.x + bg_treerocks2.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[3]==0){
                change[3]++;
                bg_treerocks2=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_02_trees);
            }
            bg_treerocks2.x = bg_treerocks2.background.getWidth();
        }


        bg_hillscastle1.x-=5*screenRatioX;
        bg_hillscastle2.x-=5*screenRatioX;
        if (bg_hillscastle1.x + bg_hillscastle1.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[4]==0){
                change[4]++;
                bg_hillscastle1=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_03_cake);
            }
            bg_hillscastle1.x = bg_hillscastle1.background.getWidth();
        }
        if (bg_hillscastle2.x + bg_hillscastle2.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[5]==0){
                change[5]++;
                bg_hillscastle2=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_03_cake);
            }
            bg_hillscastle2.x = bg_hillscastle2.background.getWidth();
        }


        bg_clouds1.x-=5*screenRatioX;
        bg_clouds2.x-=5*screenRatioX;
        if (bg_clouds1.x + bg_clouds1.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[6]==0){
                change[6]++;
                bg_clouds1=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_04_clouds);
            }
            bg_clouds1.x = bg_clouds1.background.getWidth();
        }
        if (bg_clouds2.x + bg_clouds2.background.getWidth() < 0) {
            if(score>score_limit_for_background_change && change[7]==0){
                change[7]++;
                bg_clouds2=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_04_clouds);
            }
            bg_clouds2.x = bg_clouds2.background.getWidth();
        }

        if(score>score_limit_for_background_change && change[8]==0){
            change[8]++;
            bg_hills=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_05_rocks);
            bg_rocks=new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_06_sky);
            bg_sky= new Background(screenX, screenY, getResources(),R.drawable.layer_icecream_06_sky);
        }

        int flightupspeed;
        if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
            flightupspeed=15;
        }
        else{
            flightupspeed=30;
        }

        if (flight.isGoingUp)
            flight.y -= flightupspeed * screenRatioY;
        else
            flight.y += flightupspeed * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y >= screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {

            if (bullet.x > screenX)
                trash.add(bullet);

            int top_speed;
            if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                top_speed=45;
            }
            else{
                top_speed=50;
            }
            bullet.x += top_speed * screenRatioX;

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

            for (Zombie zombie : zombies) {

                if (Rect.intersects(zombie.getCollisionShape(), bullet.getCollisionShape()) ) {
                    if(zombie.health==2){
                        trash.add(bullet);
                        zombie.health--;
                    }
                    else {
                        score++;
                        zombie.health=2;
                        zombie.x = -500;
                        bullet.x = screenX + 500;
                        zombie.wasShot = true;
                    }
                }
            }

            for (GreyBird greybird : greybirds) {

                if (Rect.intersects(greybird.getCollisionShape(), bullet.getCollisionShape()) ) {
                    if(greybird.health==2){
                        trash.add(bullet);
                        greybird.health--;
                    }
                    else {
                        score++;
                        greybird.health=2;
                        greybird.x = -500;
                        bullet.x = screenX + 500;
                        greybird.wasShot = true;
                    }
                }
            }

        }

        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for (i=0;i<num_birds;i++) {

            birds[i].x -= birds[i].speed;

            if (birds[i].x + birds[i].width < 0) {

//                if (!bird.wasShot) {
//                    isGameOver = true;
//                    return;
//                }
                int top_speed, min_speed;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    top_speed=25;
                    min_speed=15;
                }
                else{
                    top_speed=40;
                    min_speed=20;
                }

                int bound = (int) (top_speed * screenRatioX);
                birds[i].speed = random.nextInt(bound);

                if (birds[i].speed < min_speed* screenRatioX)
                    birds[i].speed = (int) (min_speed * screenRatioX);

                int lower_bound=i*(screenY - 3*birds[i].height)/num_birds;
                int upper_bound=(i+1)*(screenY - 3*birds[i].height)/num_birds;
                birds[i].x = screenX;
                birds[i].y = random.nextInt(upper_bound-lower_bound+1)+lower_bound;

                birds[i].wasShot = false;
            }

            if (Rect.intersects(birds[i].getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

        for (i=0;i<num_dinos;i++) {

            dinos[i].x -= dinos[i].speed;

            if (dinos[i].x + dinos[i].width < 0) {

//                if (!dino.wasShot) {
//                    isGameOver = true;
//                    return;
//                }
                if(score>score_limit_for_background_change+25 && dinos[i].activejack==false){
                    dinos[i].TransformtoJack(getResources());
                }
                int top_speed, min_speed;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    top_speed=30;
                    min_speed=10;
                }
                else{
                    top_speed=40;
                    min_speed=20;
                }
                int bound = (int) (top_speed* screenRatioX);
                dinos[i].speed = random.nextInt(bound);

                if (dinos[i].speed < min_speed * screenRatioX)
                    dinos[i].speed = (int) (min_speed * screenRatioX);

                dinos[i].x = screenX;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    dinos[i].y = screenY-dinos[i].height-80;
                }
                else{
                    dinos[i].y = screenY-dinos[i].height-140;
                }

                dinos[i].wasShot = false;
            }
            if (Rect.intersects(dinos[i].getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }
        }

        for (i=0;i<num_greybirds;i++) {

            greybirds[i].x -= greybirds[i].speed;

            if (greybirds[i].x + greybirds[i].width < 0) {

//                if (!bird.wasShot) {
//                    isGameOver = true;
//                    return;
//                }
                if(score>score_limit_for_background_change+25 && greybirds[i].activedragon==false){
                    greybirds[i].Transformtodragon(getResources());
                }
                int top_speed, min_speed;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    top_speed=20;
                    min_speed=12;
                }
                else{
                    top_speed=45;
                    min_speed=30;
                }


                int bound = (int) (top_speed * screenRatioX);
                greybirds[i].speed = random.nextInt(bound);

                if (greybirds[i].speed < min_speed* screenRatioX)
                    greybirds[i].speed = (int) (min_speed * screenRatioX);

                int lower_bound=i*(screenY - 3*greybirds[i].height)/num_birds;
                int upper_bound=(i+1)*(screenY - 3*greybirds[i].height)/num_birds;
                greybirds[i].x = screenX;
                greybirds[i].y = random.nextInt(upper_bound-lower_bound+1)+lower_bound;

                greybirds[i].wasShot = false;
            }

            if (Rect.intersects(greybirds[i].getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }

        }

        for (i=0;i<num_zombies;i++) {

            zombies[i].x -= zombies[i].speed;
            rockets[i].x -= rockets[i].speed;

            if (zombies[i].x + zombies[i].width < 0) {

//                if (!zombie.wasShot) {
//                    isGameOver = true;
//                    return;
//                }
                int top_speed, min_speed;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    top_speed=25;
                    min_speed=15;
                }
                else{
                    top_speed=40;
                    min_speed=30;
                }
                int bound = (int) (top_speed * screenRatioX);
                zombies[i].speed = random.nextInt(bound);

                if (zombies[i].speed < min_speed * screenRatioX)
                    zombies[i].speed = (int) (min_speed * screenRatioX);

                zombies[i].x = screenX;
                if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
                    zombies[i].y = screenY-zombies[i].height-100;
                }
                else{
                    zombies[i].y = screenY-zombies[i].height-150;
                }

                zombies[i].wasShot = false;
            }

            if(rockets[i].x + rockets[i].width<0 && zombies[i].wasShot==false){
                int bound=(int)(zombies[i].speed+10*screenRatioX);
                rockets[i].speed=bound;
                rockets[i].x=zombies[i].x;
                rockets[i].y=zombies[i].y+zombies[i].height/2;
            }

            if (Rect.intersects(zombies[i].getCollisionShape(), flight.getCollisionShape())) {

                isGameOver = true;
                return;
            }
        }

        for(i=0;i<num_rockets;i++){
            if (Rect.intersects(rockets[i].getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true;
                return;
            }
        }
    }

    private void draw () {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();
//            canvas.drawBitmap(background_back.background, background_back.x, background_back.y, paint);
//            canvas.drawBitmap(background_mid1.background, background_mid1.x, background_mid1.y, paint);
//            //canvas.drawBitmap(background_mid2.background, background_mid2.x, background_mid2.y, paint);
//            canvas.drawBitmap(background_front1.background, background_front1.x, background_front1.y, paint);
//            canvas.drawBitmap(background_front2.background, background_front2.x, background_front2.y, paint);

            canvas.drawBitmap(bg_sky.background, bg_sky.x, bg_sky.y, paint);
            canvas.drawBitmap(bg_rocks.background, bg_rocks.x, bg_rocks.y, paint);
            canvas.drawBitmap(bg_hills.background, bg_hills.x, bg_hills.y, paint);
            canvas.drawBitmap(bg_clouds1.background, bg_clouds1.x, bg_clouds1.y, paint);
            canvas.drawBitmap(bg_clouds2.background, bg_clouds2.x, bg_clouds2.y, paint);
            canvas.drawBitmap(bg_hillscastle1.background, bg_hillscastle1.x, bg_hillscastle1.y, paint);
            canvas.drawBitmap(bg_hillscastle2.background, bg_hillscastle2.x, bg_hillscastle2.y, paint);
            canvas.drawBitmap(bg_treerocks1.background, bg_treerocks1.x, bg_treerocks1.y, paint);
            canvas.drawBitmap(bg_treerocks2.background, bg_treerocks2.x, bg_treerocks2.y, paint);
            canvas.drawBitmap(bg_ground1.background, bg_ground1.x, bg_ground1.y, paint);
            canvas.drawBitmap(bg_ground2.background, bg_ground2.x, bg_ground2.y, paint);

            for (Bird bird : birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);
            for (Dino dino : dinos)
                canvas.drawBitmap(dino.getDino(), dino.x, dino.y, paint);
            for (GreyBird greybird : greybirds)
                canvas.drawBitmap(greybird.getGreyBird(), greybird.x, greybird.y, paint);
            for (Zombie zombie : zombies)
                canvas.drawBitmap(zombie.getZombie(), zombie.x, zombie.y, paint);
            for(int i=0;i<num_rockets;i++){
                if(rockets[i].x+rockets[i].width>0){
                    canvas.drawBitmap(rockets[i].rocket, rockets[i].x, rockets[i].y, paint);
                }
            }
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
        int t;
        if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
            t=10;
        }
        else{
            t=2;
        }
        try {
            Thread.sleep(t);
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

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (event.getX() < screenX / 2) {
//                    flight.isGoingUp = true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                flight.isGoingUp = false;
//                if (event.getX() > screenX / 2)
//                    flight.toShoot++;
//                break;
//        }
        int i,j,k;
        int lefthandid=-1;
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN){
            if(event.getX(event.getActionIndex())<screenX/2){
                flight.isGoingUp=true;
            }
        }
        else if(event.getActionMasked()==MotionEvent.ACTION_UP){
            flight.isGoingUp=false;
            if(event.getX(event.getActionIndex())>screenX/2){
                flight.toShoot++;
            }
        }
        else if(event.getActionMasked()==MotionEvent.ACTION_POINTER_DOWN){
            if(event.getX(event.getActionIndex())<screenX/2){
                flight.isGoingUp=true;
            }
        }
        else if(event.getActionMasked()==MotionEvent.ACTION_POINTER_UP){
            if(event.getX(event.getActionIndex())<screenX/2){
                flight.isGoingUp=false;
            }
            if(event.getX(event.getActionIndex())>screenX/2){
                flight.toShoot++;
            }
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
