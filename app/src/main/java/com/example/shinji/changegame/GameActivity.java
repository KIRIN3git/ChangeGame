package com.example.shinji.changegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by shinji on 2017/09/07.
 */

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        // 表示したいレイアウト
        LinearLayout layout = (LinearLayout)findViewById(R.id.over_layout);

        CoinMng.CoinInit(this,layout);



    }


}