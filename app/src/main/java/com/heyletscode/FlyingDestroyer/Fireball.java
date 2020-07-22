package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;
import static com.heyletscode.FlyingDestroyer.GameView.screenInches;

public class Fireball {

    int x, y, width, height,speed=40;
    Bitmap fireball;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    Fireball (Resources res) {

        fireball = BitmapFactory.decodeResource(res, R.drawable.fireball1);

        width = fireball.getWidth();
        height = fireball.getHeight();

        if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
            width /= 20;
            height /= 20;
        }
        else{
            width /= 10;
            height /= 10;
        }
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        fireball = Bitmap.createScaledBitmap(fireball, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x+width/3, y+height/3, x + width, y + height);
    }

}