package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;

public class Zombie {

    public int speed = 30;
    public boolean wasShot = true;
    int x = 0, y, width, height, zombiencounter = 1,health=2;
    Bitmap zombie1, zombie2, zombie3, zombie4,zombie5,zombie6,zombie7,zombie8,zombie9,zombie10;

    Zombie (Resources res) {

        zombie1 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk1);
        zombie2 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk2);
        zombie3 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk3);
        zombie4 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk4);
        zombie5 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk5);
        zombie6 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk6);
        zombie7 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk7);
        zombie8 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk8);
        zombie9 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk8);
        zombie10 = BitmapFactory.decodeResource(res, R.drawable.zombie_walk8);

        width = zombie1.getWidth();
        height = zombie1.getHeight();

        width /= 3;
        height /= 3;

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        zombie1 = Bitmap.createScaledBitmap(zombie1, width, height, false);
        zombie2 = Bitmap.createScaledBitmap(zombie2, width, height, false);
        zombie3 = Bitmap.createScaledBitmap(zombie3, width, height, false);
        zombie4 = Bitmap.createScaledBitmap(zombie4, width, height, false);
        zombie5 = Bitmap.createScaledBitmap(zombie5, width, height, false);
        zombie6 = Bitmap.createScaledBitmap(zombie6, width, height, false);
        zombie7 = Bitmap.createScaledBitmap(zombie7, width, height, false);
        zombie8 = Bitmap.createScaledBitmap(zombie8, width, height, false);
        zombie9 = Bitmap.createScaledBitmap(zombie9, width, height, false);
        zombie10 = Bitmap.createScaledBitmap(zombie10, width, height, false);

        y = -height;
    }

    Bitmap getZombie () {

        if (zombiencounter == 1) {
            zombiencounter++;
            return zombie1;
        }

        if (zombiencounter == 2) {
            zombiencounter++;
            return zombie2;
        }

        if (zombiencounter == 3) {
            zombiencounter++;
            return zombie3;
        }

        if (zombiencounter == 4) {
            zombiencounter++;
            return zombie4;
        }

        if (zombiencounter == 5) {
            zombiencounter++;
            return zombie5;
        }

        if (zombiencounter == 6) {
            zombiencounter++;
            return zombie6;
        }

        if (zombiencounter == 7) {
            zombiencounter++;
            return zombie7;
        }

        if (zombiencounter == 8) {
            zombiencounter++;
            return zombie8;
        }

        if (zombiencounter == 9) {
            zombiencounter++;
            return zombie9;
        }

        zombiencounter = 1;

        return zombie10;
    }

    Rect getCollisionShape () {
        return new Rect(x+width/4, y+height/4, x + width, y + height);
    }

}