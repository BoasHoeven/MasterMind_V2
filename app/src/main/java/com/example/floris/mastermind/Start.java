package com.example.floris.mastermind;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Floris on 29/03/2016.
 */
public class Start extends Activity {
    public int round = 1;
    public String[] uTable = {"","","",""};
    public String[] mTable = {"blue","yellow","purple","blue"};
    public int h_minute = 0;
    public int h_seconds = 0;

    public static boolean checkForDuplicates(String[] uTable,String[] mTable,String Color){
        boolean check = false;
        int u_number = 0;
        int m_number = 0;
        for (int i = 0; i < uTable.length; i++){
            if (uTable[i].equals(Color)){
                u_number++;
            }
        }
        if (u_number >1){
            check = true;
        }
        for (int i = 0; i < mTable.length; i++) {
            if (mTable[i].equals(Color)){
                m_number++;
            }
        }
        if (m_number == u_number){
            check = false;
        }
        return check;
    }

    public static int[] checkUserInput(String[] uTable, String[] mTable) {
        int z = 0;
        int w = 0;
        for (int i = 0; i < mTable.length; i++) {
            if(mTable[i].equals(uTable[i])) {
                //System.out.println("Position " + i + "> user guessed position / color right (" +uTable[i]+ ")");
                z++;
            }
            else{
                //System.out.println("Position " + i + "> user guessed color position wrong (" +uTable[i]+ ")");
                //System.out.println("Checking whether it's white pin");
                boolean found = false;
                for (int g = 0; g < mTable.length; g++) {
                    if(mTable[g].equals(uTable[i])) {
                        if (checkForDuplicates(uTable, mTable, uTable[i])) {
                            uTable[i] = "placeholder";
                            //System.out.println("It's a duplicated pin");
                            found = true;
                            break;
                        }
                        else{
                            //System.out.println("It's a white pin");
                            found = true;
                            w++;
                            break;
                        }
                    }
                }
                //if (!found){
                    //System.out.println("It's not white pin");
               // }
            }
        }
        return new int[] {z, w};
    }

    TextView timerTextView;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            h_seconds = seconds;
            seconds = seconds % 60;
            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 500);
        }
    };
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
        Intent sd=new Intent(this,Start.class);
        startActivity(sd);
    }
    private void SavePreferences_int(String key, int value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
        Intent sd=new Intent(this,Start.class);
        startActivity(sd);
    }

    private void finishGame(){
        timerHandler.removeCallbacks(timerRunnable);
        CharSequence timeToSave = timerTextView.getText();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SavePreferences_int("highscore0",h_minute);
        for (int i = 0; i < 10; i++) {
            String key = "highscore" + i;
            int data = sharedPreferences.getInt(key, 0);
            if (data < h_seconds){
                int backupdata = data;
                SavePreferences_int(key,h_seconds);
                int chk = i+1;
                while (chk < 10){
                    //Toast.makeText(Start.this, "chk: " + Integer.toString(chk) , Toast.LENGTH_LONG).show();
                    String newkey = "highscore" + chk;
                    int nextHighscore = sharedPreferences.getInt(newkey, 0);
                    if (backupdata > nextHighscore){
                        SavePreferences_int(newkey,backupdata);
                        backupdata = nextHighscore;
                    }
                    chk++;
                }
                String data_s = Integer.toString(data);
                Toast.makeText(Start.this, "Saving to key <" + key + "> To localStorage - Highscore: " + h_seconds , Toast.LENGTH_LONG).show();
                break;
            }
        }
        //
        Toast.makeText(Start.this, "Winner! - " + timeToSave , Toast.LENGTH_LONG).show();
        Intent h = new Intent(Start.this, MainActivity.class);
        startActivity(h);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        final ImageView ok1 = (ImageView)findViewById(R.id.ok_1);
        final ImageView ok2 = (ImageView)findViewById(R.id.ok_2);
        final ImageView ok3 = (ImageView)findViewById(R.id.ok_3);
        final ImageView ok4 = (ImageView)findViewById(R.id.ok_4);
        final ImageView ok5 = (ImageView)findViewById(R.id.ok_5);
        final ImageView ok6 = (ImageView)findViewById(R.id.ok_6);
        final ImageView ok7 = (ImageView)findViewById(R.id.ok_7);
        final ImageView pin1 = (ImageView)findViewById(R.id.pin1_1);
        final ImageView pin2 = (ImageView)findViewById(R.id.pin1_2);
        final ImageView pin3 = (ImageView)findViewById(R.id.pin1_3);
        final ImageView pin4 = (ImageView)findViewById(R.id.pin1_4);
        final ImageView[] pins1 = {pin1,pin2,pin3,pin4};
        final ImageView pin5 = (ImageView)findViewById(R.id.pin2_1);
        final ImageView pin6 = (ImageView)findViewById(R.id.pin2_2);
        final ImageView pin7 = (ImageView)findViewById(R.id.pin2_3);
        final ImageView pin8 = (ImageView)findViewById(R.id.pin2_4);
        final ImageView[] pins2 = {pin5,pin6,pin7,pin8};
        final ImageView pin9 = (ImageView)findViewById(R.id.pin3_1);
        final ImageView pin10 = (ImageView)findViewById(R.id.pin3_2);
        final ImageView pin11= (ImageView)findViewById(R.id.pin3_3);
        final ImageView pin12 = (ImageView)findViewById(R.id.pin3_4);
        final ImageView[] pins3 = {pin9,pin10,pin11,pin12};
        final ImageView pin13 = (ImageView)findViewById(R.id.pin4_1);
        final ImageView pin14 = (ImageView)findViewById(R.id.pin4_2);
        final ImageView pin15= (ImageView)findViewById(R.id.pin4_3);
        final ImageView pin16 = (ImageView)findViewById(R.id.pin4_4);
        final ImageView[] pins4 = {pin13,pin14,pin15,pin16};
        final ImageView pin17 = (ImageView)findViewById(R.id.pin5_1);
        final ImageView pin18 = (ImageView)findViewById(R.id.pin5_2);
        final ImageView pin19= (ImageView)findViewById(R.id.pin5_3);
        final ImageView pin20 = (ImageView)findViewById(R.id.pin5_4);
        final ImageView[] pins5 = {pin17,pin18,pin19,pin20};
        final ImageView pin21 = (ImageView)findViewById(R.id.pin6_1);
        final ImageView pin22 = (ImageView)findViewById(R.id.pin6_2);
        final ImageView pin23= (ImageView)findViewById(R.id.pin6_3);
        final ImageView pin24 = (ImageView)findViewById(R.id.pin6_4);
        final ImageView[] pins6 = {pin21,pin22,pin23,pin24};
        final ImageView pin25 = (ImageView)findViewById(R.id.pin7_1);
        final ImageView pin26 = (ImageView)findViewById(R.id.pin7_2);
        final ImageView pin27= (ImageView)findViewById(R.id.pin7_3);
        final ImageView pin28 = (ImageView)findViewById(R.id.pin7_4);
        final ImageView[] pins7 = {pin25,pin26,pin27,pin28};
        final ImageView ball1 = (ImageView)findViewById(R.id.B1_1);
        final ImageView ball2 = (ImageView)findViewById(R.id.B1_2);
        final ImageView ball3 = (ImageView)findViewById(R.id.B1_3);
        final ImageView ball4 = (ImageView)findViewById(R.id.B1_4);
        final ImageView[] balls1 = {ball1,ball2,ball3,ball4};
        final ImageView ball5 = (ImageView)findViewById(R.id.B2_1);
        final ImageView ball6 = (ImageView)findViewById(R.id.B2_2);
        final ImageView ball7 = (ImageView)findViewById(R.id.B2_3);
        final ImageView ball8 = (ImageView)findViewById(R.id.B2_4);
        final ImageView[] balls2 = {ball5,ball6,ball7,ball8};
        final ImageView ball9 = (ImageView)findViewById(R.id.B3_1);
        final ImageView ball10 = (ImageView)findViewById(R.id.B3_2);
        final ImageView ball11 = (ImageView)findViewById(R.id.B3_3);
        final ImageView ball12 = (ImageView)findViewById(R.id.B3_4);
        final ImageView[] balls3 = {ball9,ball10,ball11,ball12};
        final ImageView ball13 = (ImageView)findViewById(R.id.B4_1);
        final ImageView ball14 = (ImageView)findViewById(R.id.B4_2);
        final ImageView ball15 = (ImageView)findViewById(R.id.B4_3);
        final ImageView ball16 = (ImageView)findViewById(R.id.B4_4);
        final ImageView[] balls4 = {ball13,ball14,ball15,ball16};
        final ImageView ball17 = (ImageView)findViewById(R.id.B5_1);
        final ImageView ball18 = (ImageView)findViewById(R.id.B5_2);
        final ImageView ball19 = (ImageView)findViewById(R.id.B5_3);
        final ImageView ball20 = (ImageView)findViewById(R.id.B5_4);
        final ImageView[] balls5 = {ball17,ball18,ball19,ball20};
        final ImageView ball21 = (ImageView)findViewById(R.id.B6_1);
        final ImageView ball22 = (ImageView)findViewById(R.id.B6_2);
        final ImageView ball23 = (ImageView)findViewById(R.id.B6_3);
        final ImageView ball24 = (ImageView)findViewById(R.id.B6_4);
        final ImageView[] balls6 = {ball21,ball22,ball23,ball24};
        final ImageView ball25 = (ImageView)findViewById(R.id.B7_1);
        final ImageView ball26 = (ImageView)findViewById(R.id.B7_2);
        final ImageView ball27 = (ImageView)findViewById(R.id.B7_3);
        final ImageView ball28 = (ImageView)findViewById(R.id.B7_4);
        final ImageView[] balls7 = {ball25,ball26,ball27,ball28};
        final ImageView btn = (ImageView) findViewById(R.id.Click1);
        final ImageView btn2 = (ImageView) findViewById(R.id.Click2);
        final ImageView btn3 = (ImageView) findViewById(R.id.Click3);
        final ImageView btn4 = (ImageView) findViewById(R.id.Click4);
        ok1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 1 && ok1.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable,mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins1.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4){
                           finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins1[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        }
                        else if (white > 0) {
                            white = white - 1;
                            pins1[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok1.setContentDescription(null);
                    ok1.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 2 && ok2.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable,mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins2.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4){
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins2[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        }
                        else if (white > 0) {
                            white = white - 1;
                            pins2[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok2.setContentDescription(null);
                    ok2.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 3 && ok3.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable,mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins3.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4){
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins3[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        }
                        else if (white > 0) {
                            white = white - 1;
                            pins3[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok3.setContentDescription(null);
                    ok3.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 4 && ok4.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable,mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins4.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4){
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins4[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        }
                        else if (white > 0) {
                            white = white - 1;
                            pins4[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok4.setContentDescription(null);
                    ok4.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 5 && ok5.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable, mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins5.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4) {
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins5[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        } else if (white > 0) {
                            white = white - 1;
                            pins5[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok5.setContentDescription(null);
                    ok5.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 6 && ok6.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable, mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins6.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4) {
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins6[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        } else if (white > 0) {
                            white = white - 1;
                            pins6[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    //Toast.makeText(Start.this, "NEXT ROUND: " +  round, Toast.LENGTH_SHORT).show();
                    ok6.setContentDescription(null);
                    ok6.setImageResource(android.R.color.transparent);
                }
            }
        });

        ok7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7 && ok7.getContentDescription() != null) {
                    round++;
                    int[] pins = checkUserInput(uTable, mTable);
                    //Toast.makeText(Start.this, "Pins - Black:" +  pins[0] +" White: "+ pins[1] + "  -  ROUND: " +  round, Toast.LENGTH_LONG).show();
                    int black = pins[0];
                    int white = pins[1];
                    for (int x = 0; x < pins7.length; x++) {
                        //Toast.makeText(Start.this, "Now in loop #" + x, Toast.LENGTH_SHORT).show();
                        if (black == 4) {
                            finishGame();
                        }
                        if (black > 0) {
                            black = black - 1;
                            pins7[x].setImageResource(R.drawable.darkpin);
                            //Toast.makeText(Start.this, "placing a black pin - " + black, Toast.LENGTH_LONG).show();
                        } else if (white > 0) {
                            white = white - 1;
                            pins7[x].setImageResource(R.drawable.whitepin);
                            //Toast.makeText(Start.this, "placing a white pin - " + white, Toast.LENGTH_LONG).show();
                        }
                    }
                    timerHandler.removeCallbacks(timerRunnable);
                    CharSequence timeToSave = timerTextView.getText();
                    Toast.makeText(Start.this, "Je hebt verloren. - " + timeToSave, Toast.LENGTH_LONG).show();
                    Intent h = new Intent(Start.this, MainActivity.class);
                    startActivity(h);
                    ok7.setContentDescription(null);
                    ok7.setImageResource(android.R.color.transparent);
                }
            }
        });

        ball1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (round == 1) {
                    //System.out.println("clicked!");
                    ok1.setContentDescription(null);
                    ok1.setImageResource(android.R.color.transparent);
                    //System.out.println("ok1!");
                    uTable[0] = "";
                    //System.out.println("settomg content!");
                    ball1.setContentDescription(null);
                   // System.out.println("settomg recource!");
                    ball1.setImageResource(android.R.color.transparent);
                   // System.out.println("resource set!");
                   // Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 1) {
                    ok1.setContentDescription(null);
                    ok1.setImageResource(android.R.color.transparent);
                    uTable[1] = "";
                    ball2.setContentDescription(null);
                    ball2.setImageResource(android.R.color.transparent);
               //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 1) {
                    ok1.setContentDescription(null);
                    ok1.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball3.setContentDescription(null);
                    ball3.setImageResource(android.R.color.transparent);
                 //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 1) {
                    ok1.setContentDescription(null);
                    ok1.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball4.setContentDescription(null);
                    ball4.setImageResource(android.R.color.transparent);
              //      Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 2) {
                    uTable[0] = "";
                    ball5.setContentDescription(null);
                    ball5.setImageResource(android.R.color.transparent);
                    Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (round == 2) {
                ok2.setContentDescription(null);
                ok2.setImageResource(android.R.color.transparent);
                uTable[1] = "";
                ball6.setContentDescription(null);
                ball6.setImageResource(android.R.color.transparent);
           //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
            }
            }
        });

        ball7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (round == 2) {
                ok2.setContentDescription(null);
                ok2.setImageResource(android.R.color.transparent);
                uTable[2] = "";
                ball7.setContentDescription(null);
                ball7.setImageResource(android.R.color.transparent);
           //    Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
            }
            }
        });

        ball8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 2) {
                    ok2.setContentDescription(null);
                    ok2.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball8.setContentDescription(null);
                    ball8.setImageResource(android.R.color.transparent);
              //      Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 3) {
                    ok3.setContentDescription(null);
                    ok3.setImageResource(android.R.color.transparent);
                    uTable[0] = "";
                    ball9.setContentDescription(null);
                    ball9.setImageResource(android.R.color.transparent);
               //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (round == 3) {
                ok3.setContentDescription(null);
                ok3.setImageResource(android.R.color.transparent);
                uTable[1] = "";
                ball10.setContentDescription(null);
                ball10.setImageResource(android.R.color.transparent);
             //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
            }
            }
        });

        ball11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 3) {
                    ok3.setContentDescription(null);
                    ok3.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball11.setContentDescription(null);
                    ball11.setImageResource(android.R.color.transparent);
                //    Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });


        ball12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 3) {
                    ok3.setContentDescription(null);
                    ok3.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball12.setContentDescription(null);
                    ball12.setImageResource(android.R.color.transparent);
              //      Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 4) {
                    ok4.setContentDescription(null);
                    ok4.setImageResource(android.R.color.transparent);
                    uTable[0] = "";
                    ball13.setContentDescription(null);
                    ball13.setImageResource(android.R.color.transparent);
             //       Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 4) {
                    ok4.setContentDescription(null);
                    ok4.setImageResource(android.R.color.transparent);
                    uTable[1] = "";
                    ball14.setContentDescription(null);
                    ball14.setImageResource(android.R.color.transparent);
                //    Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 4) {
                    ok4.setContentDescription(null);
                    ok4.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball15.setContentDescription(null);
                    ball15.setImageResource(android.R.color.transparent);
                 //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 4) {
                    ok4.setContentDescription(null);
                    ok4.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball15.setContentDescription(null);
                    ball15.setImageResource(android.R.color.transparent);
                 //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 5) {
                    ok5.setContentDescription(null);
                    ok5.setImageResource(android.R.color.transparent);
                    uTable[0] = "";
                    ball17.setContentDescription(null);
                    ball17.setImageResource(android.R.color.transparent);
                 //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 5) {
                    ok5.setContentDescription(null);
                    ok5.setImageResource(android.R.color.transparent);
                    uTable[1] = "";
                    ball18.setContentDescription(null);
                    ball18.setImageResource(android.R.color.transparent);
               //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 5) {
                    ok5.setContentDescription(null);
                    ok5.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball19.setContentDescription(null);
                    ball19.setImageResource(android.R.color.transparent);
                 //   Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 5) {
                    ok5.setContentDescription(null);
                    ok5.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball20.setContentDescription(null);
                    ball20.setImageResource(android.R.color.transparent);
                //    Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 6) {
                    ok6.setContentDescription(null);
                    ok6.setImageResource(android.R.color.transparent);
                    uTable[0] = "";
                    ball21.setContentDescription(null);
                    ball21.setImageResource(android.R.color.transparent);
               //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 6) {
                    ok6.setContentDescription(null);
                    ok6.setImageResource(android.R.color.transparent);
                    uTable[1] = "";
                    ball22.setContentDescription(null);
                    ball22.setImageResource(android.R.color.transparent);
                  //  Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 6) {
                    ok6.setContentDescription(null);
                    ok6.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball23.setContentDescription(null);
                    ball23.setImageResource(android.R.color.transparent);
            //        Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });


        ball24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 6) {
                    ok6.setContentDescription(null);
                    ok6.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball24.setContentDescription(null);
                    ball24.setImageResource(android.R.color.transparent);
               //     Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7) {
                    ok7.setContentDescription(null);
                    ok7.setImageResource(android.R.color.transparent);
                    uTable[0] = "";
                    ball25.setContentDescription(null);
                    ball25.setImageResource(android.R.color.transparent);
          //          Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7) {
                    ok7.setContentDescription(null);
                    ok7.setImageResource(android.R.color.transparent);
                    uTable[1] = "";
                    ball26.setContentDescription(null);
                    ball26.setImageResource(android.R.color.transparent);
         //           Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball27.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7) {
                    ok7.setContentDescription(null);
                    ok7.setImageResource(android.R.color.transparent);
                    uTable[2] = "";
                    ball27.setContentDescription(null);
                    ball27.setImageResource(android.R.color.transparent);
            //        Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        ball28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7) {
                    ok7.setContentDescription(null);
                    ok7.setImageResource(android.R.color.transparent);
                    uTable[3] = "";
                    ball28.setContentDescription(null);
                    ball28.setImageResource(android.R.color.transparent);
              //      Toast.makeText(Start.this, "clicked: " + round, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Button1clicked!");
                if (round == 7) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls7.length; i++) {
                        if (balls7[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls7[i].setContentDescription("blue");
                            balls7[i].setImageResource(R.drawable.blue);
                        }
                        if(balls7[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok7.setImageResource(R.drawable.ok);
                        ok7.setContentDescription("ok");
                    }
                }
                if (round == 6) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls6.length; i++) {
                        if (balls6[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls6[i].setContentDescription("blue");
                            balls6[i].setImageResource(R.drawable.blue);
                        }
                        if(balls6[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok6.setImageResource(R.drawable.ok);
                        ok6.setContentDescription("ok");
                    }
                }
                if (round == 5) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls5.length; i++) {
                        if (balls5[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls5[i].setContentDescription("blue");
                            balls5[i].setImageResource(R.drawable.blue);
                        }
                        if(balls5[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok5.setImageResource(R.drawable.ok);
                        ok5.setContentDescription("ok");
                    }
                }
                if (round == 4) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls4.length; i++) {
                        if (balls4[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls4[i].setContentDescription("blue");
                            balls4[i].setImageResource(R.drawable.blue);
                        }
                        if(balls4[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok4.setImageResource(R.drawable.ok);
                        ok4.setContentDescription("ok");
                    }
                }
                if (round == 3) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls3.length; i++) {
                        if (balls3[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls3[i].setContentDescription("blue");
                            balls3[i].setImageResource(R.drawable.blue);
                        }
                        if(balls3[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok3.setImageResource(R.drawable.ok);
                        ok3.setContentDescription("ok");
                    }
                }
                if (round == 2) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls2.length; i++) {
                        if (balls2[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            balls2[i].setContentDescription("blue");
                            balls2[i].setImageResource(R.drawable.blue);
                        }
                        if(balls2[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok2.setImageResource(R.drawable.ok);
                        ok2.setContentDescription("ok");
                    }
                }
                if (round == 1) {
                    System.out.println("round found!");
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls1.length; i++) {
                        if (balls1[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "blue";
                            System.out.println("Setting image");
                            balls1[i].setContentDescription("blue");
                            balls1[i].setImageResource(R.drawable.blue);
                            System.out.println("image set");
                        }
                        if(balls1[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok1.setImageResource(R.drawable.ok);
                        ok1.setContentDescription("ok");
                    }
                }
                System.out.println("Click ended");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls7.length; i++) {
                        if (balls7[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls7[i].setContentDescription("yellow");
                            balls7[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls7[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok7.setImageResource(R.drawable.ok);
                        ok7.setContentDescription("ok");
                    }
                }
                if (round == 6){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls6.length; i++) {
                        if (balls6[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls6[i].setContentDescription("yellow");
                            balls6[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls6[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok6.setImageResource(R.drawable.ok);
                        ok6.setContentDescription("ok");
                    }
                }
                if (round == 5){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls5.length; i++) {
                        if (balls5[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls5[i].setContentDescription("yellow");
                            balls5[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls5[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok5.setImageResource(R.drawable.ok);
                        ok5.setContentDescription("ok");
                    }
                }
                if (round == 4){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls4.length; i++) {
                        if (balls4[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls4[i].setContentDescription("yellow");
                            balls4[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls4[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok4.setImageResource(R.drawable.ok);
                        ok4.setContentDescription("ok");
                    }
                }
                if (round == 3){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls3.length; i++) {
                        if (balls3[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls3[i].setContentDescription("yellow");
                            balls3[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls3[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok3.setImageResource(R.drawable.ok);
                        ok3.setContentDescription("ok");
                    }
                }
                if (round == 2){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls2.length; i++) {
                        if (balls2[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls2[i].setContentDescription("yellow");
                            balls2[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls2[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok2.setImageResource(R.drawable.ok);
                        ok2.setContentDescription("ok");
                    }
                }
                if (round == 1) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls1.length; i++) {
                        if (balls1[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "yellow";
                            balls1[i].setContentDescription("yellow");
                            balls1[i].setImageResource(R.drawable.yellow);
                        }
                        if(balls1[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok1.setImageResource(R.drawable.ok);
                        ok1.setContentDescription("ok");
                    }
                }
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls7.length; i++) {
                        if (balls7[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls7[i].setContentDescription("green");
                            balls7[i].setImageResource(R.drawable.green);
                        }
                        if(balls7[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok7.setImageResource(R.drawable.ok);
                        ok7.setContentDescription("ok");
                    }
                }
                if (round == 6){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls6.length; i++) {
                        if (balls6[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls6[i].setContentDescription("green");
                            balls6[i].setImageResource(R.drawable.green);
                        }
                        if(balls6[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok6.setImageResource(R.drawable.ok);
                        ok6.setContentDescription("ok");
                    }
                }
                if (round == 5){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls5.length; i++) {
                        if (balls5[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls5[i].setContentDescription("green");
                            balls5[i].setImageResource(R.drawable.green);
                        }
                        if(balls5[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok5.setImageResource(R.drawable.ok);
                        ok5.setContentDescription("ok");
                    }
                }
                if (round == 4){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls4.length; i++) {
                        if (balls4[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls4[i].setContentDescription("green");
                            balls4[i].setImageResource(R.drawable.green);
                        }
                        if (balls4[i].getContentDescription() == null) {
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok4.setImageResource(R.drawable.ok);
                        ok4.setContentDescription("ok");
                    }
                }
                if (round == 3){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls3.length; i++) {
                        if (balls3[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls3[i].setContentDescription("green");
                            balls3[i].setImageResource(R.drawable.green);
                        }
                        if(balls3[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok3.setImageResource(R.drawable.ok);
                        ok3.setContentDescription("ok");
                    }
                }
                if (round == 2){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls2.length; i++) {
                        if (balls2[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls2[i].setContentDescription("green");
                            balls2[i].setImageResource(R.drawable.green);
                        }
                        if(balls2[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok2.setImageResource(R.drawable.ok);
                        ok2.setContentDescription("ok");
                    }
                }

                if (round == 1) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls1.length; i++) {
                        if (balls1[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "green";
                            balls1[i].setContentDescription("green");
                            balls1[i].setImageResource(R.drawable.green);
                        }
                        if(balls1[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok1.setImageResource(R.drawable.ok);
                        ok1.setContentDescription("ok");
                    }
                }

            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (round == 7){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls7.length; i++) {
                        if (balls7[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls7[i].setContentDescription("purple");
                            balls7[i].setImageResource(R.drawable.violet);
                        }
                        if(balls7[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok7.setImageResource(R.drawable.ok);
                        ok7.setContentDescription("ok");
                    }
                }
                if (round == 6){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls6.length; i++) {
                        if (balls6[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls6[i].setContentDescription("purple");
                            balls6[i].setImageResource(R.drawable.violet);
                        }
                        if(balls6[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok6.setImageResource(R.drawable.ok);
                        ok6.setContentDescription("ok");
                    }
                }


                if (round == 5){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls5.length; i++) {
                        if (balls5[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls5[i].setContentDescription("purple");
                            balls5[i].setImageResource(R.drawable.violet);
                        }
                        if(balls5[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok5.setImageResource(R.drawable.ok);
                        ok5.setContentDescription("ok");
                    }
                }
                if (round == 4){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls4.length; i++) {
                        if (balls4[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls4[i].setContentDescription("purple");
                            balls4[i].setImageResource(R.drawable.violet);
                        }
                        if(balls4[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok4.setImageResource(R.drawable.ok);
                        ok4.setContentDescription("ok");
                    }
                }
                if (round == 3){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls3.length; i++) {
                        if (balls3[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls3[i].setContentDescription("purple");
                            balls3[i].setImageResource(R.drawable.violet);
                        }
                        if(balls3[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok3.setImageResource(R.drawable.ok);
                        ok3.setContentDescription("ok");
                    }
                }
                if (round == 2){
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls2.length; i++) {
                        if (balls2[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls2[i].setContentDescription("purple");
                            balls2[i].setImageResource(R.drawable.violet);
                        }
                        if(balls2[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok2.setImageResource(R.drawable.ok);
                        ok2.setContentDescription("ok");
                    }
                }
                if (round == 1) {
                    boolean notDone = false;
                    int pompert = 0;
                    for (int i = 0; i < balls1.length; i++) {
                        if (balls1[i].getContentDescription() == null && !notDone) {
                            notDone = true;
                            uTable[i] = "purple";
                            balls1[i].setContentDescription("purple");
                            balls1[i].setImageResource(R.drawable.violet);
                        }
                        if(balls1[i].getContentDescription() == null){
                            pompert++;
                        }
                    }
                    if (pompert == 0) {
                        ok1.setImageResource(R.drawable.ok);
                        ok1.setContentDescription("ok");
                    }
                }
            }
        });
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

    }

}
