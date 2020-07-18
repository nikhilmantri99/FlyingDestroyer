package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;
import static com.heyletscode.FlyingDestroyer.GameView.screenInches;

public class Dino {

    public int speed = 30;
    public boolean wasShot = true;
    int x = 0, y, width, height, dinocounter = 1;
    Bitmap dino1, dino2, dino3, dino4,dino5,dino6,dino7,dino8;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    Dino (Resources res) {

        dino1 = BitmapFactory.decodeResource(res, R.drawable.dino_run_1);
        dino2 = BitmapFactory.decodeResource(res, R.drawable.dino_run_2);
        dino3 = BitmapFactory.decodeResource(res, R.drawable.dino_run_3);
        dino4 = BitmapFactory.decodeResource(res, R.drawable.dino_run_4);
        dino5 = BitmapFactory.decodeResource(res, R.drawable.dino_run_5);
        dino6 = BitmapFactory.decodeResource(res, R.drawable.dino_run_6);
        dino7 = BitmapFactory.decodeResource(res, R.drawable.dino_run_7);
        dino8 = BitmapFactory.decodeResource(res, R.drawable.dino_run_8);

        width = dino1.getWidth();
        height = dino1.getHeight();

        if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
            width /= 6;
            height /= 6;
        }
        else{
            width /= 3;
            height /= 3;
        }

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        dino1 = Bitmap.createScaledBitmap(dino1, width, height, false);
        dino2 = Bitmap.createScaledBitmap(dino2, width, height, false);
        dino3 = Bitmap.createScaledBitmap(dino3, width, height, false);
        dino4 = Bitmap.createScaledBitmap(dino4, width, height, false);
        dino5 = Bitmap.createScaledBitmap(dino5, width, height, false);
        dino6 = Bitmap.createScaledBitmap(dino6, width, height, false);
        dino7 = Bitmap.createScaledBitmap(dino7, width, height, false);
        dino8 = Bitmap.createScaledBitmap(dino8, width, height, false);
        //dino1 = Bitmap.createScaledBitmap(dino1, width, height, false);

        y = -height;
    }

    Bitmap getDino () {

        if (dinocounter == 1 || dinocounter == 2) {
            dinocounter++;
            return dino1;
        }

        if (dinocounter == 3 || dinocounter == 4) {
            dinocounter++;
            return dino2;
        }

        if (dinocounter == 5 || dinocounter == 6) {
            dinocounter++;
            return dino3;
        }

        if (dinocounter == 7 || dinocounter == 8) {
            dinocounter++;
            return dino4;
        }

        if (dinocounter == 9 ||dinocounter == 10) {
            dinocounter++;
            return dino5;
        }

        if (dinocounter == 11 || dinocounter == 12) {
            dinocounter++;
            return dino6;
        }

        if (dinocounter == 13 || dinocounter == 14) {
            dinocounter++;
            return dino7;
        }

        if(dinocounter == 15){
            dinocounter++;
            return dino8;
        }

        dinocounter = 1;

        return dino8;
    }

    Rect getCollisionShape () {
        return new Rect(x+width/2, y+height/4, x + width, y + height);
    }

}
