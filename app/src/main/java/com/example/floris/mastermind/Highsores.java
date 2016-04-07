package com.example.floris.mastermind;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Floris on 4-4-2016.
 */
public class Highsores extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final TextView score1 = (TextView)findViewById(R.id.h1);
        final TextView score2 = (TextView)findViewById(R.id.h2);
        final TextView score3 = (TextView)findViewById(R.id.h3);
        final TextView score4 = (TextView)findViewById(R.id.h4);
        final TextView score5 = (TextView)findViewById(R.id.h5);
        final TextView score6 = (TextView)findViewById(R.id.h6);
        final TextView score7 = (TextView)findViewById(R.id.h7);
        final TextView score8 = (TextView)findViewById(R.id.h8);
        final TextView score9 = (TextView)findViewById(R.id.h9);
        final TextView score10 = (TextView)findViewById(R.id.h10);
        final TextView[] scoreTable = {score1,score2,score3,score4,score5,score6,score7,score8,score9,score10};
        for (int i = 0; i < scoreTable.length; i++) {
            String key = "highscore" + i;
            int data = sharedPreferences.getInt(key, 0);
            if (data > 0) {
                String data_s = i + 1 + ". Floris " + Integer.toString(data);
                scoreTable[i].setText(data_s);
            }
            //Integer data = sharedPreferences.getInt("highscore", 0) ;
            //Toast.makeText(this, i + ". " + data, Toast.LENGTH_LONG).show();
        }
    }
}
