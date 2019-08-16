package com.wyattandsean.gameofhunters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GhostHuntButton {
    public int buttonX, buttonY;
    private Bitmap buttonImage;
    private Context context;

    public GhostHuntButton(Context context) {
        this.createButton(context);
    }

    public int getWidth(){return buttonImage.getWidth();}
    public int getHeight(){return buttonImage.getHeight();}
    public Bitmap getBitmap() {
        return buttonImage;
    }

    private void createButton(Context context) {
        buttonImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.red_x_exit_button);
    }

    public void onClick() {
        Intent intent = new Intent(this.context, MainActivity.class);
        this.context.startActivity(intent);
    }
}
