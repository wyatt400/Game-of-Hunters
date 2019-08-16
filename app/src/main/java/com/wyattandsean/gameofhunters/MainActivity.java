package com.wyattandsean.gameofhunters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * Launching emulator issues:
 * https://stackoverflow.com/questions/42816127/waiting-for-target-device-to-come-online
 **/

public class MainActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void startButtonOnClick(View view)
    {
        Log.i("ImageButton", "Start Button Clicked");

        // system should deliver intent
        // this is a way to move from one Activity to another Activity
        // All activities must be registered in AndroidManifest.xml
        Intent intent = new Intent(this, StartGame.class);

        startActivity(intent);

        // when the StartGame Activity begins, we want to shutdown MainActivity
        // we do this here
        finish();
    }
}
