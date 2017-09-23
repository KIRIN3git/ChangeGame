package com.example.shinji.changegame;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import static java.security.AccessController.getContext;

/**
 * Created by shinji on 2017/09/07.
 */

public class GameActivity extends AppCompatActivity {

    static Context mContext;
    static Handler sHandler;

    static FrameLayout sOverLayout;
    static LinearLayout sBackLayout;

    static float sLaptime = 0.0f;

    Timer sTimer = null;
    TextView sTextStart;
    TextView sTextView;
    static TimeMng sTimeMng;

    static boolean sOpeningFlg;
    static boolean sNewQuestionFlg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FrameLayout r = (FrameLayout)findViewById(R.id.activity_game);
        sTextStart = (TextView)findViewById(R.id.textStart);

        mContext = this;

        // 表示したいレイアウト
        sOverLayout = (FrameLayout)findViewById(R.id.over_layout);
        sBackLayout = (LinearLayout)findViewById(R.id.back_layout);


    }

    // 再度起動時
    @Override
    protected void onResume() {
        super.onResume();

        CoinMng coinMng = new CoinMng(this,sOverLayout);
        coinMng.CoinInit();

        //TImeMng
        sHandler = new Handler();
        sTimeMng = new TimeMng(this,sBackLayout,sHandler);

        mainGame();

    }

    private void mainGame(){

        // 前時間表示
        sTextView = (TextView)findViewById(R.id.textView6);

        sOpeningFlg = true;
        sNewQuestionFlg = true;



        sTimer = new Timer(true);
        sTimer.schedule(new TimerTask(){
            @Override
            public void run() {





                // sHandlerを通じてUI Threadへ処理をキューイング
                sHandler.post( new Runnable() {

                    public void run() {

                        //実行間隔分を加算処理
                        sLaptime +=  0.1;

                        //計算にゆらぎがあるので小数点第1位で丸める
                        BigDecimal bi = new BigDecimal(sLaptime);
                        float outputValue = bi.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                        //現在のLapTime
                        sTextView.setText(Float.toString(outputValue));


                        if( sLaptime > 2.0 ){
                            sOpeningFlg = false;
                        }


                        // ゲーム開始カウントダウン表示
                        if( sOpeningFlg == true ){
                            sTextStart.setVisibility(View.VISIBLE);
                        }
                        else{
                            sTextStart.setVisibility(View.GONE);

                            if( sNewQuestionFlg ){

                                sTimeMng.startProgressBar(100);
                                sNewQuestionFlg = false;
                            }

                            boolean overFlg = sTimeMng.extendProgressBar(1);
                            if( overFlg == true ){
                                sNewQuestionFlg = true;
                            }

                        }






                        //progressBar.setProgress((int)mLaptime);
/*
                        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", (int)sLaptime);
                        animation.setDuration(100); // 0.1 second
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();
*/
                    }
                });
            }
        }, 100, 100); // 0.1秒間隔

    }

    // 端末のサイズを取得(Pointクラス px)
    private Point geViewSize() {
        int x,y;

        LinearLayout fl = (LinearLayout)findViewById(R.id.back_layout);
        x = fl.getWidth();
        y = fl.getHeight();
        Point real = new Point(x, y);

        Log.w( "DEBUG_DATA", "reaxl.x = " + x);
        Log.w( "DEBUG_DATA", "reaxl.y = " + y);
        return real;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

    // Viewのサイズを取得
        int x,y;

        LinearLayout fl = (LinearLayout)findViewById(R.id.back_layout);
        x = fl.getWidth();
        y = fl.getHeight();
        Point real = new Point(x, y);

        Log.w( "DEBUG_DATA", "reaxl.x = " + x);
        Log.w( "DEBUG_DATA", "reaxl.y = " + y);

 //       CommonMng.real = real;
    }



}