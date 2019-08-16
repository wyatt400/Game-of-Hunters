package com.wyattandsean.gameofhunters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Reference: http://gamecodeschool.com/android/building-a-simple-android-2d-scrolling-shooter/
 */
public class Fireball
{
    public Bitmap fireballImage;
    public float fireballX, fireballY, fireballVector;

    public Fireball(Context context, int gunBarrelX)
    {
        fireballImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.missile);

//        move fireball to hunter position
        this.fireballX = gunBarrelX;
        this.fireballY = GameView.displayHeight - Hunter.hunterHeight - this.getFireballHeight()/2;

        this.fireballVector = 50;
    }

    public int getFireballWidth()
    {
        return this.fireballImage.getWidth();
    }

    public int getFireballHeight()
    {
        return this.fireballImage.getHeight();
    }
}
