package com.example.shinji.changegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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



        float xy[] = CommonMng.PsToPx(10,90);
        Log.w( "DEBUG", "aaa x[] " + xy[0]);
        Log.w( "DEBUG", "aaa y[] " + xy[1]);

        // 表示したいレイアウト
        LinearLayout layout = (LinearLayout)findViewById(R.id.over_layout);

        // ImageViewオブジェクトの作成
        ImageView imageView = new ImageView(this);

        // 画像の設定
        imageView.setImageResource(R.drawable.ichien);

        // 画像のサイズの設定
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,150);

        // 表示座標の設定
        //lp.leftMargin = (int)xy[0];
        //lp.topMargin = (int)xy[1];

        // 画像の追加
        layout.addView(imageView,lp);

    }
}