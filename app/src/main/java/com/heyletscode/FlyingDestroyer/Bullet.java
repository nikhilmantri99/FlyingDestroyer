package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;
import static com.heyletscode.FlyingDestroyer.GameView.screenInches;

public class Bullet {

    int x, y, width, height;
    Bitmap bullet;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    Bullet (Resources res) {

        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);

        width = bullet.getWidth();
        height = bullet.getHeight();

        if(absolute(screenInches-5.00)<absolute(screenInches-6.55)){
            width /= 6;
            height /= 6;
        }
        else{
            width /= 4;
            height /= 4;
        }

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);

    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
