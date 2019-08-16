package com.wyattandsean.gameofhunters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Reference: http://gamecodeschool.com/android/building-a-simple-android-2d-scrolling-shooter/
 */
public class Guano
{
    public Bitmap guanoImage;
    public float fireballX, fireballY, fireballVector;

    public Guano(Context context, int hunterX)
    {
        guanoImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.missile);

//        move fireball to hunter position
        this.fireballX = hunterX;
        this.fireballY = GameView.displayHeight - Hunter.hunterHeight - this.getFireballHeight()/2;

        this.fireballVector = 50;
    }

    public int getFireballWidth()
    {
        return this.guanoImage.getWidth();
    }

    public int getFireballHeight()
    {
        return this.guanoImage.getHeight();
    }
}
