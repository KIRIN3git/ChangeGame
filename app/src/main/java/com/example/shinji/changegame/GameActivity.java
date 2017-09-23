package com.example.shinji.changegame;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

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

    static int GAME_MILLI_SECOND = 100;

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
        sTextView = (TextView)findViewById(R.id.textTime);

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
                                Log.w( "DEBUG_DATA", "aaaaaaaaaaaaaaaa");
                                sTimeMng.startProgressBar(100); // 初期値
                                sNewQuestionFlg = false;

                                // sTimeMng.extendProgressBar(GAME_MILLI_SECOND/100);
                            }


                            boolean timeOverFlg = sTimeMng.extendProgressBar(GAME_MILLI_SECOND/100);
                            if( timeOverFlg == true ){
                                sNewQuestionFlg = true;
                            }

                        }
                    }
                });
            }
        }, GAME_MILLI_SECOND, GAME_MILLI_SECOND); // 0.1秒間隔

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