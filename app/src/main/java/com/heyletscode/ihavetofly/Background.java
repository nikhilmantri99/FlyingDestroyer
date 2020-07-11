package com.heyletscode.ihavetofly;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class Background {

    int x = 0, y = 0;
    Bitmap background;

    Background (int screenX, int screenY, Resources res, int temp) {

        background = BitmapFactory.decodeResource(res, temp);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }

}
