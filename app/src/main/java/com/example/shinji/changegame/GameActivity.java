package com.example.shinji.changegame;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shinji on 2017/09/07.
 */

//https://akira-watson.com/android/property-animation.html

public class GameActivity extends AppCompatActivity {

    static Context mContext;
    static Handler sHandler;

    static FrameLayout sOverLayout;
    static LinearLayout sBackLayout;

    static float sLapTimeReal = 0.0f;
    static float sLapTime= 0.0f;
    static float sMemTime = 0.0f;

    CoinMng sCoinMng;

    Timer sTimer = null;
    TextView sTextStart;
    TextView sTextTimer;
    TextView sTextAmount;
    TextView sTextPayment;
    TextView sTextChange;

    ImageView sImageOk;
    ImageView sImageNg;



    static QuestionMng sQuestionMng;

    // スタート表示中
    static boolean sOpeningFlg;
    // 新しい問題を出題
    static boolean sNewQuestionFlg;
    // 考え中中フラグ
    static boolean sNowThinkingFlg;
    // 解答処理中フラグ
    static boolean sNowAnserFlg;
    // 正解フラグ
    static boolean sQuestionOkFlg;
    // お釣り返却中フラグ
    static boolean sNowAmountFlg;

    static int GAME_MILLI_SECOND = 100;
    static int sQuestionYen = 0;
    static int sAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        FrameLayout r = (FrameLayout)findViewById(R.id.activity_game);
        sTextStart = (TextView)findViewById(R.id.textStart);

        sImageOk = (ImageView)findViewById(R.id.imageOk);
        sImageNg = (ImageView)findViewById(R.id.imageNg);


        mContext = this;

        // 表示したいレイアウト
        sOverLayout = (FrameLayout)findViewById(R.id.over_layout);
        sBackLayout = (LinearLayout)findViewById(R.id.back_layout);

    }

    // 再度起動時
    @Override
    protected void onResume() {
        super.onResume();

        //CoinMng
        sCoinMng = new CoinMng(this,sOverLayout);
        sCoinMng.CoinInit();

        //QuestionMng
        sHandler = new Handler();
        sQuestionMng = new QuestionMng(this,sBackLayout,sHandler);

        // 支払ボタンをクリック
        answer();

        mainGame();

    }

    private void answer(){

        Button btn = (Button)findViewById(R.id.buttonAnswer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( sNowAnserFlg == true ) return;

                HashMap<String,Integer> allCoinNum = CoinMng.GetAllNum();
                HashMap<String,Integer> walletCoinNum = CoinMng.GetWalletNum();
                HashMap<String,Integer> trayCoinNum = CoinMng.GetTrayNum();

                sQuestionOkFlg = sQuestionMng.anserQuestion(allCoinNum,trayCoinNum);
                if(sQuestionOkFlg){
                    sCoinMng.DeleteCoin();
                }

                sNowAnserFlg = true;
                sMemTime = sLapTime;



                Log.w( "AAAAA", "okkkkkkkkkkkkkkkkkkk " + sQuestionOkFlg);
            }
        });


    }

    private void mainGame(){

        // 前時間表示
        sTextTimer = (TextView)findViewById(R.id.textTime);
        sTextAmount = (TextView)findViewById(R.id.textAll);
        sTextPayment = (TextView)findViewById(R.id.textPayment);
        sTextChange = (TextView)findViewById(R.id.textChange);

        sOpeningFlg = true;
        sNewQuestionFlg = false;
        sNowThinkingFlg = false;
        sNowAnserFlg = false;
        sQuestionOkFlg = false;
        sNowAmountFlg = false;


        sTimer = new Timer(true);
        sTimer.schedule(new TimerTask(){
            @Override
            public void run() {

                // sHandlerを通じてUI Threadへ処理をキューイング
                sHandler.post( new Runnable() {

                    public void run() {

                        sTextStart.setVisibility(View.GONE);
                        sImageOk.setVisibility(View.GONE);
                        sImageNg.setVisibility(View.GONE);

                        //実行間隔分を加算処理
                        sLapTimeReal +=  0.1;

                        //計算にゆらぎがあるので小数点第1位で丸める
                        BigDecimal bi = new BigDecimal(sLapTimeReal);
                        sLapTime = bi.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

                        //現在のLapTime
                        sTextTimer.setText(Float.toString(sLapTime));

                        //出題金額
                        sTextAmount.setText(Integer.toString(sQuestionMng.sAmount));

                        // カウントダウン終了
                        if( sOpeningFlg == true && sLapTime > 2.0 ){
                            sOpeningFlg = false;
                            sNewQuestionFlg = true; // とりあえず第一問
                        }
                        // 正解表示時間終了
                        if( sNowAnserFlg == true ) {

                            // 1秒立ったら
                            if (((int)( (sLapTime * 10 ) - (sMemTime * 10 ) ) ) == 10 ) {
                                sNowAmountFlg = true;
                            }
                            if ((sLapTime - sMemTime) > 2.0) {
                                sNowAnserFlg = false;
                                sNewQuestionFlg = true;

                            }
                        }

                        // カウントダウン表示
                        if( sOpeningFlg == true ){
                            sTextStart.setVisibility(View.VISIBLE);
                        }
                        // 解答結果待ち
                        else if( sNowAnserFlg == true ){
                            if(sQuestionOkFlg){
                                sImageOk.setVisibility(View.VISIBLE);
                            }
                            else{
                                sImageNg.setVisibility(View.VISIBLE);
                            }
                            if( sNowAmountFlg ){
                                Log.w( "DEBUG_DATA", "gggggggggggggggggggggggggggggggggggg2" + sAmount);

                                CoinStatus coinStatus = new CoinStatus(1, 10, 0);
//                                CoinMng.AddCoin(sAmount);
                                sNowAmountFlg = false;
                            }

                        }

                        // ゲーム開始
                        else{
                            // 新しい問題
                            if( sNewQuestionFlg ){

                                Log.w( "DEBUG_DATA", "aaaaaaaaaaaaaaaa");
                                sQuestionMng.startProgressBar(100); // 初期値
                                sNewQuestionFlg = false;
                                sNowThinkingFlg = true;

                                // 出題
                                // 値段を取得 プログレスバーが起動
                                sQuestionYen = sQuestionMng.NewQuestion();

                            }

                            if(sNowThinkingFlg){
                                boolean timeOverFlg = sQuestionMng.extendProgressBar(GAME_MILLI_SECOND/100);
                                if( timeOverFlg == true ){
                                    sNewQuestionFlg = true;
                                }
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