package com.example.shinji.changegame;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;

import static java.security.AccessController.getContext;

/**
 * Created by shinji on 2017/09/07.
 */

public class GameActivity extends AppCompatActivity {
    static int ichienImageId = R.drawable.ichien;
    static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        mContext = this;
        // 表示したいレイアウト
        LinearLayout layout = (LinearLayout)findViewById(R.id.over_layout);
        //CoinMng.CoinInit(this,layout);

        CoinMng coinMng = new CoinMng(this,layout);
        coinMng.CoinInit();

    }



}