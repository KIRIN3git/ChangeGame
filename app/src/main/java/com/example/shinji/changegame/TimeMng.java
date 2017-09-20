package com.example.shinji.changegame;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by shinji on 2017/09/20.
 */

public class TimeMng {
    static Context mContext;
    static LinearLayout mLayout;

    public TimeMng(Context context, LinearLayout layout) {
        mContext = context;
        mLayout = layout;
    }
    public void TImeMngInit(){

        mLayout.findViewById(R.id.ProgressBarHorizontal);

        ProgressBar progressBar1 = mLayout.findViewById(R.id.ProgressBarHorizontal);
        progressBar1.setMax(100); // 水平プログレスバーの最大値を設定
        progressBar1.setProgress(20); // 水平プログレスバーの値を設定
        progressBar1.setSecondaryProgress(60); // 水平プログレスバーのセカンダリ値を設定
        progressBar1.setScaleY(30f); // 高さを指定
    }
/*
    publicd static CountTime(){

    }
*/
}
