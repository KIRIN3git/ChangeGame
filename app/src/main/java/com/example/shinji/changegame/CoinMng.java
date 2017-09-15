package com.example.shinji.changegame;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by shinji on 2017/09/12.
 */

public class CoinMng extends GameActivity{

    // コイン種類
    static String coinType[] = {"1yen","5yen","10yen","50yen","100yen","500yen","1000yen"};
    // コインの枚数
    static int coinCount[] = {4,1,4,1,4,1,1};
    // コインの初期位置

    static int ichienImageId = R.drawable.ichien;

    public static void CoinInit(Context context,LinearLayout layout){

        float xy[] = CommonMng.PsToPx(10,80);
        Log.w( "DEBUG", "aaa x[] " + xy[0]);
        Log.w( "DEBUG", "aaa y[] " + xy[1]);

        // ImageViewオブジェクトの作成
        ImageView imageView = new ImageView(context);

        // 画像の設定
        imageView.setImageResource(ichienImageId);

        // タッチリスナーの設定
     //   imageView.setOnTouchListener();

        // 画像のサイズの設定
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150,150);

        // 表示座標の設定
        lp.leftMargin = (int)xy[0];
        lp.topMargin = (int)xy[1];


        float xy2[] = CommonMng.PsToPx(15,80);

        // 画像の追加
        layout.addView(imageView,lp);

        // ImageViewオブジェクトの作成
        ImageView imageView2 = new ImageView(context);

        // 画像の設定
        imageView2.setImageResource(R.drawable.ichien);

        // タッチリスナーの設定
//        imageView.setOnTouchListener(imageView.setOnTouchListener);

        // 画像のサイズの設定
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(150,150);

        // 表示座標の設定
        lp.leftMargin = (int)xy2[0];
        lp.topMargin = (int)xy2[1];

        // 画像の追加
        layout.addView(imageView2,lp2);
    }


}
