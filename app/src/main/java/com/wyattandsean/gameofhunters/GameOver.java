package com.wyattandsean.gameofhunters;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

public class GameOver extends Activity
{
    TextView tvScore, tvPersonalBest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.game_over);

        int score = getIntent().getExtras().getInt("score");

        // allows us to save data, even after app is closed
        // 0 - private mode
        SharedPreferences preferences = getSharedPreferences("MyPrefs", 0);

        // get the high score from storage
        int highScore = preferences.getInt("highScore", 0);

        SharedPreferences.Editor editor = preferences.edit();

       if (score > highScore)
        {
            highScore = score;

            editor.putInt("highScore", highScore);

            editor.commit();
        }

        tvScore = (TextView) findViewById(R.id.tvScore);
        tvScore.setText("" + score);
        tvPersonalBest = (TextView) findViewById(R.id.tvPersonalBest);
        tvPersonalBest.setText("" + highScore);

    }

    /**
     * Take user to Main Screen
     * @param view
     */
    public void restartButtonOnClick(View view)
    {
        Intent intent = new Intent(GameOver.this, MainActivity.class);

        startActivity(intent);

        finish();
    }

    public void exitButtonOnClick(View view)
    {
        this.finishAffinity();
    }
}

// TODO: add the background changes after x seconds