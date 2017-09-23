package com.example.shinji.changegame;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;

/**
 * Created by shinji on 2017/09/20.
 */

public class TimeMng {
    static Context sContext;
    static LinearLayout sLayout;
    static ProgressBar progressBar;
    static float sLaptime = 0.0f;

    Timer sTimer = null;
    TextView sTextView;
    // プログレスバーのマックス秒数×１０
    static int sMaxPB;
    // プログレスバーの現在の秒数×１０
    static int sNowPB;


    public TimeMng(Context context, LinearLayout layout,Handler handler) {
        sContext = context;
        sLayout = layout;

        // プログレスバーの設定
        progressBar = sLayout.findViewById(R.id.ProgressBarHorizontal);
        progressBar.setScaleY(30f); // 高さを指定

        // 前時間表示
        sTextView = sLayout.findViewById(R.id.textTime);

    }

    /*
    tenSec:経過秒数×10
     */
    public boolean extendProgressBar(int tenSec ){

        sNowPB += tenSec;

        //progressBar.setProgress(sNowPB);


        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", (int)sNowPB);
        animation.setDuration(GameActivity.GAME_MILLI_SECOND); // 0.1 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();


        if( sMaxPB <= sNowPB ){
            return true;
        }

        return false;
    }




    public void startProgressBar(int max){
        sMaxPB = max;
        sNowPB = 0;
        progressBar.setMax(sMaxPB); // 水平プログレスバーの最大値を設定
        progressBar.setProgress(0);
    }

}

