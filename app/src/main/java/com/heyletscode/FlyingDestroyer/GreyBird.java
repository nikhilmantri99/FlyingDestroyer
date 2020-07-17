package com.heyletscode.FlyingDestroyer;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.heyletscode.FlyingDestroyer.GameView.screenRatioX;
import static com.heyletscode.FlyingDestroyer.GameView.screenRatioY;
import static com.heyletscode.FlyingDestroyer.GameView.screenInches;

public class GreyBird {

    public int speed = 50;
    public boolean wasShot = true;
    public int x = 0, y, width, height, birdCounter = 1,health=2;
    Bitmap bird1, bird2, bird3, bird4;

    double absolute(double t){
        if(t<0){
            return -t;
        }
        else{
            return t;
        }
    }

    GreyBird (Resources res) {

        bird1 = BitmapFactory.decodeResource(res, R.drawable.grey_bird_1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.grey_bird_2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.grey_bird_3);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.grey_bird_4);

        width = bird1.getWidth();
        height = bird1.getHeight();

        if(absolute(screenInches-5.50)<absolute(screenInches-6.55)){
            width /= 10;
            height /= 10;
        }
        else{
            width /= 8;
            height /= 8;
        }

        width = (int) (width * screenRatioX);
        height = (int) (height * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);

        y = -height;
        health=2;
    }

    Bitmap getGreyBird () {

        if (birdCounter == 1) {
            birdCounter++;
            return bird1;
        }

        if (birdCounter == 2) {
            birdCounter++;
            return bird2;
        }

        if (birdCounter == 3) {
            birdCounter++;
            return bird3;
        }

        birdCounter = 1;

        return bird4;
    }

    Rect getCollisionShape () {
        return new Rect(x+width/3, y+height/3, x + (4*width)/5, y + (4*height)/5);
    }

}
