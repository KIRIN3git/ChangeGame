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
import com.example.shinji.changegame.QuestionMng;
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

    // 全体時間
    static float sLapTimeReal = 0.0f;
    // ゲーム時間
    static float sLapTime= 0.0f;
    // 記録時間
    static float sMemTime = 0.0f;

    CoinMng sCoinMng;

    Timer sTimer = null;
    TextView sTextStart;
    TextView sTextTimer;
    TextView sTextOdai;
    TextView sTextShiharai;
    TextView sTextOtsuri;
    TextView sTextSeikaiNum;

    ImageView sImageOk;
    ImageView sImageNg;



    static QuestionMng sQuestionMng;

    // スタート表示中
    static boolean sOpeningFlg;
    // 新しい問題を出題
    static boolean sNewQuestionFlg;
    // 考え中フラグ
    static boolean sNowThinkingFlg;
    // 解答処理中フラグ
    static boolean sNowAnserFlg;
    // 正解フラグ
    static boolean sQuestionOkFlg;
    // お釣り返却中フラグ
    static boolean sNowAmountFlg;

    static int GAME_MILLI_SECOND = 100;
    static int sQuestionYen = 0;
    static int sCharge = 0;

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

                sNowThinkingFlg = false;
                sNowAnserFlg = true;
                sMemTime = sLapTime;

                HashMap<String,Integer> allCoinNum = CoinMng.GetCoinNum(CoinMng.ALL_POSITION);
                HashMap<String,Integer> walletCoinNum = CoinMng.GetCoinNum(CoinMng.WALLET_POSITION);
                HashMap<String,Integer> trayCoinNum = CoinMng.GetCoinNum(CoinMng.TRAY_POSITION);

                sQuestionOkFlg = sQuestionMng.answerQuestion(allCoinNum,trayCoinNum);
                if(sQuestionOkFlg){
//                    sCoinMng.OutSideCoin();
                    // コイン全削除
                    sCoinMng.DeleteCoins(0,CoinMng.TRAY_POSITION);
                    // トレイにお釣り表示
                    sCoinMng.CreateCoinChange(QuestionMng.sOtsuri,CoinMng.TRAY_POSITION);
                    // シンキングタイム1秒マイナス
                    sQuestionMng.sThinkingTime -= 10;
                    // 正解数プラス
                    sQuestionMng.sSeikaiNum++;

                    // 五千円札がなければ補充
                    if( walletCoinNum.get("5000") == 0 ){
                        CoinMng.CreateCoin( 5000,1,CoinMng.WALLET_POSITION );
                    }
				}
                Log.w( "AAAAA", "okkkkkkkkkkkkkkkkkkk " + sQuestionOkFlg);
            }
        });
    }

    private void mainGame(){

        // 前時間表示
        sTextTimer = (TextView)findViewById(R.id.textTime);
        sTextOdai = (TextView)findViewById(R.id.textAll);
        sTextShiharai = (TextView)findViewById(R.id.textPayment);
        sTextOtsuri = (TextView)findViewById(R.id.textChange);
        sTextSeikaiNum = (TextView)findViewById(R.id.SeikaiNum);


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
                        sTextOdai.setText(Integer.toString(sQuestionMng.sOdai));

                        // カウントダウン終了
                        if( sOpeningFlg == true && sLapTime > 2.0 ){
                            sOpeningFlg = false;
                            sNewQuestionFlg = true; // とりあえず第一問
                        }

                        // 正解表示時間終了
                        if( sNowAnserFlg == true ) {
                            //支払金額
                            sTextShiharai.setText(Integer.toString(sQuestionMng.sShirarai));
                            //お釣り金額
                            sTextOtsuri.setText(Integer.toString(sQuestionMng.sOtsuri));
                            //正解数
                            sTextSeikaiNum.setText(Integer.toString(sQuestionMng.sSeikaiNum));

                            if ((sLapTime - sMemTime) > 2.0) {

                                //支払金額
                                sTextShiharai.setText("");
                                //お釣り金額
                                sTextOtsuri.setText("");

                                // トレイに乗っているコインを全部財布に移動
                                CoinMng.MoveAllCoin(CoinMng.TRAY_POSITION,1);

                                sNowAnserFlg = false;
                                sNewQuestionFlg = true;
                            }
                        }

                        // カウントダウン表示
                        if( sOpeningFlg == true ){
                            sTextStart.setVisibility(View.VISIBLE);
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
                            // 解答結果待ち
                            else if( sNowAnserFlg == true ){
                                if(sQuestionOkFlg){
                                    sImageOk.setVisibility(View.VISIBLE);
                                }
                                else{
                                    sImageNg.setVisibility(View.VISIBLE);
                                }
                                if( sNowAmountFlg ){

//                                CoinStatus coinStatus = new CoinStatus(1, 10, 0,true);
//                                CoinMng.AddCoin(sAmount);
                                    sNowAmountFlg = false;
                                }
                            }
                            if(sNowThinkingFlg){
                                boolean timeOverFlg = sQuestionMng.extendProgressBar(1); // 0.1秒づつバーを伸ばす
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