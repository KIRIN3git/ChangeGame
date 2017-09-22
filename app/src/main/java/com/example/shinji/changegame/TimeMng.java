package com.example.shinji.changegame;

import android.content.Context;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by shinji on 2017/09/20.
 */

public class TimeMng implements Runnable {
    static Context mContext;
    static LinearLayout mLayout;

    static ProgressBar progressBar;
    Thread thread;
    long StartTimeMillis;

    public TimeMng(Context context, LinearLayout layout) {
        mContext = context;
        mLayout = layout;
    }
    public void TimeMngInit(){
//http://techbooster.org/android/application/934/
        mLayout.findViewById(R.id.ProgressBarHorizontal);

        progressBar = mLayout.findViewById(R.id.ProgressBarHorizontal);
        progressBar.setScaleY(30f); // 高さを指定

        // 起動時間
        StartTimeMillis = System.currentTimeMillis();
    }

    public void CountTime(){
        long CurrentTimeMillis = System.currentTimeMillis();
        int second = (int)(CurrentTimeMillis - StartTimeMillis);

        Log.w( "AAAAA", "second = " + second);
    }




    public void StartProgressBar(int max){

        mLayout.findViewById(R.id.ProgressBarHorizontal);


        progressBar.setMax(max); // 水平プログレスバーの最大値を設定

//        progressBar.setProgress(i);


        thread = new Thread(this);
        thread.start();
        /*
        for(int i = 0; i < max; i++ ) {
            try {
                Thread.sleep(1000); //ミリ秒
            } catch (InterruptedException e) {
            }
        }
        */
    }
    @Override
    public void run() {
        try {
            thread.sleep(5000);
        } catch (InterruptedException e) { }
    }
}

