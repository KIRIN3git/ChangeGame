package com.example.shinji.changegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by etisu on 2017/11/12.
 */

public class ClearActivity extends AppCompatActivity {

    static int sStarNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clear);

        Bundle extras = getIntent().getExtras();
        sStarNum = extras.getInt("STAR");

        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        if( sStarNum == 1 ) {
            editor.putInt("ClearStar1", 1);
            editor.apply();
        }
        else if( sStarNum == 2 ) {
            editor.putInt("ClearStar2", 1);
            editor.apply();
        }
        else if( sStarNum == 3 ) {
            editor.putInt("ClearStar3", 1);
            editor.apply();
        }
        else if( sStarNum == 4 ) {
            editor.putInt("ClearStar4", 1);
            editor.apply();
        }
        else if( sStarNum == 5 ) {
            editor.putInt("ClearStar5", 1);
            editor.apply();
        }
    }
}
