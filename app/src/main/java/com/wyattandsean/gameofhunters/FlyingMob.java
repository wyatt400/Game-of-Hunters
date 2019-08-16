package com.wyattandsean.gameofhunters;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Random;

abstract class FlyingMob
{
    Random random;

    public abstract Bitmap getBitmap();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void setFlyingMonster(Context context);

    public abstract void resetPosition();
}
