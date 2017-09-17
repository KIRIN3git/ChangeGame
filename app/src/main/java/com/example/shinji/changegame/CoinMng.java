package com.example.shinji.changegame;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.ArrayList;
import android.view.View.OnTouchListener;


/**
 * Created by shinji on 2017/09/12.
 */

public class CoinMng implements OnTouchListener{

    private static Context mContext;
    private static ViewGroup mViewGroup;
    private static LinearLayout mLayout;
    private static View.OnTouchListener mVOL;

    // コイン種類
    static String coinType[] = {"1yen","5yen","10yen","50yen","100yen","500yen","1000yen"};
    // コインの枚数
    static int coinCount[] = {4,1,4,1,4,1,1};
    // コインの初期位置

    static int ichienImageId = R.drawable.ichien;

    public CoinMng(Context context,LinearLayout layout) {
        mContext = context;
        mLayout = layout;
   //     mVOL = vol;
    }

    public void CoinInit(){

        float xy[] = CommonMng.PsToPx(90,90);
        Log.w( "DEBUG", "aaa x[] " + xy[0]);
        Log.w( "DEBUG", "aaa y[] " + xy[1]);

        // ImageViewオブジェクトの作成
        ImageView imageView = new ImageView(mContext);

        // 画像の設定
        imageView.setImageResource(ichienImageId);

        // タッチリスナーの設定
        imageView.setOnTouchListener(this);

        // 画像のサイズの設定
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(100,100);

        // 表示座標の設定
        lp.leftMargin = (int)xy[0];
        lp.topMargin = (int)xy[1];

        // 画像の追加
        mLayout.addView(imageView,lp);


    }
    public boolean onTouch(View v, MotionEvent event) {
        String currAction = "";
        Log.w( "DEBUG_DATA", "v.toString() " + v.toString());
        Log.w( "DEBUG_DATA", "v.getId() " + v.getId());
        Log.w( "DEBUG_DATA", "v.getResources() " + v.getResources());

        // イベントの状態を調べる
        switch (event.getAction() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN:
                Log.w( "DEBUG_DATA", "DOWN2");
                currAction = "DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                Log.w( "DEBUG_DATA", "MOVE2");
                currAction = "MOVE";
                break;
            case MotionEvent.ACTION_UP:
                Log.w( "DEBUG_DATA", "UP2");
                currAction = "UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.w( "DEBUG_DATA", "CANCEL2");
                currAction = "CANCEL";
                break;
        }

        return true;
    }

}
