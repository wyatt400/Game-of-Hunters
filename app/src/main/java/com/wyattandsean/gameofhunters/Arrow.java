package com.wyattandsean.gameofhunters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Direction Arrows for Hunter
 */
public class Arrow
{
    private int height, width;
    private Bitmap arrow;

    public Arrow(Context context, boolean isRightArrow)
    {
        this.setArrow(context, isRightArrow);
    }

    private void setArrow(Context context, boolean isRightArrow)
    {
        if (isRightArrow == true)
        {
            this.arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.right_button);
        }
        else
        {
            this.arrow = BitmapFactory.decodeResource(context.getResources(), R.drawable.left_button);
        }

        this.height = this.arrow.getHeight();
        this.width = this.arrow.getWidth();
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public Bitmap getBitmap()
    {
        return arrow;
    }
}
