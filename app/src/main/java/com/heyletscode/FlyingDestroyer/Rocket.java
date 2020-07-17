package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;
import static com.heyletscode.FlyingDestroyer.GameView.screenInches;

public class Rocket {

    int x, y, width, height,speed=40;
    Bitmap rocket;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    Rocket (Resources res) {

        rocket = BitmapFactory.decodeResource(res, R.drawable.rocket_weapon);

        width = rocket.getWidth();
        height = rocket.getHeight();

        if(absolute(screenInches-5.50)<absolute(screenInches-6.55)){
            width /= 3;
            height /= 3;
        }
        else{
            width /= 2;
            height /= 2;
        }
        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        rocket = Bitmap.createScaledBitmap(rocket, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x+width/3, y+height/3, x + width, y + height);
    }

}