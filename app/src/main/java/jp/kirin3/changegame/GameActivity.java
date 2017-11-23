package jp.kirin3.changegame;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by shinji on 2017/09/07.
 */

//https://akira-watson.com/android/property-animation.html

public class GameActivity extends AppCompatActivity {

    private AdView mAdView;

    static Context mContext;
    static Handler sHandler;

    static FrameLayout sCoinLayout;
    static LinearLayout sBackLayout;

    // 全体時間
    static float sLapTimeReal = 0.0f;
    // まるめ全体時間
    static float sLapTime = 0.0f;
    // ゲーム時間
    static float sGameTimeReal = 0.0f;
    // ゲーム時間まるめ
    static float sGameTime = 0.0f;
    // 記録時間
    static float sMemTime = 0.0f;

    CoinMng sCoinMng;

    Timer sTimer = null;
    TextView sTextSenter;
    TextView sTextTimer;
    TextView sTextOdai;
    TextView sTextShiharai;
    TextView sTextOtsuri;

	TextView sTextStar;
    TextView sTextLevelNum;
	TextView sTextSeikaiNum;
	TextView sTextClearNum;
	TextView sTextHuSeikaiNum;
	TextView sTextNotClearNum;
    TextView sTextGameTime;

	TextView sTextSeikaiIchien;
	TextView sTextSeikaiGoen;
	TextView sTextSeikaiJyuen;
	TextView sTextSeikaiGojyuen;
	TextView sTextSeikaiHyakuen;
	TextView sTextSeikaiGohyakuen;
	TextView sTextSeikaiSenen;
	TextView sTextSeikaiGosenen;

    LinearLayout sLLSeikai;

    ImageView sImageOk;
    ImageView sImageNg;

	static int sStarNum = 0;

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
    // ゲームオーバーフラグ
    static boolean sGameOverFlg;

    static int GAME_MILLI_SECOND = 100;
    static int sQuestionYen = 0;
    static int sCharge = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        FrameLayout r = (FrameLayout)findViewById(R.id.activity_game);
        sTextSenter = (TextView)findViewById(R.id.textCenter);

        sImageOk = (ImageView)findViewById(R.id.imageOk);
        sImageNg = (ImageView)findViewById(R.id.imageNg);

        mContext = this;

        // 表示したいレイアウト
        sCoinLayout = (FrameLayout)findViewById(R.id.coin_layout);
        sBackLayout = (LinearLayout)findViewById(R.id.back_layout);

		// 前時間表示
		//sTextTimer = (TextView)findViewById(R.id.textTime);
		sTextOdai = (TextView)findViewById(R.id.textAll);
		sTextShiharai = (TextView)findViewById(R.id.textPayment);
		sTextOtsuri = (TextView)findViewById(R.id.textChange);

		sTextStar = (TextView)findViewById(R.id.textStar);
        sTextLevelNum = (TextView)findViewById(R.id.textLevelNum);
		sTextSeikaiNum = (TextView)findViewById(R.id.textSeikaiNum);
		sTextClearNum = (TextView)findViewById(R.id.textClearNum);
		sTextHuSeikaiNum = (TextView)findViewById(R.id.textHuSeikaiNum);
		sTextNotClearNum = (TextView)findViewById(R.id.textNotClearNum);
        sTextGameTime = (TextView)findViewById(R.id.textGameTime);

		sTextSeikaiIchien = (TextView)findViewById(R.id.textSeikaiIchien);
		sTextSeikaiGoen = (TextView)findViewById(R.id.textSeikaiGoen);
		sTextSeikaiJyuen = (TextView)findViewById(R.id.textSeikaiJyuen);
		sTextSeikaiGojyuen = (TextView)findViewById(R.id.textSeikaiGojyuen);
		sTextSeikaiHyakuen = (TextView)findViewById(R.id.textSeikaiHyakuen);
		sTextSeikaiGohyakuen = (TextView)findViewById(R.id.textSeikaiGohyakuen);
		sTextSeikaiSenen = (TextView)findViewById(R.id.textSeikaiSenen);
		sTextSeikaiGosenen = (TextView)findViewById(R.id.textSeikaiGosenen);

        sLLSeikai = (LinearLayout)findViewById(R.id.SeikaiInfo);

		Bundle extras = getIntent().getExtras();
		sStarNum = extras.getInt("STAR");
	}

	// 停止時
	@Override
	protected void onStop() {
		super.onStop();
		sTimer.cancel();
		sHandler.removeCallbacksAndMessages(null);

	}

    // 再度起動時
    @Override
    protected void onResume() {
        super.onResume();

        // CoinMng
        sCoinMng = new CoinMng(this, sCoinLayout);
//      sCoinMng.CoinInit();

        // QuestionMng
        sHandler = new Handler();
        sQuestionMng = new QuestionMng(this,sBackLayout,sHandler,sStarNum);

		// 全体時間
		sLapTimeReal = 0.0f;
		// まるめ全体時間
		sLapTime= 0.0f;
        // ゲーム時間
        sGameTimeReal = 0.0f;
        // まるめゲーム時間
        sGameTime = 0.0f;

		// 記録時間
		sMemTime = 0.0f;

		//難易度
		String strStar = "";
		for( int i = 0; i < sStarNum; i++ ){
			strStar += "★";
		}
		sTextStar.setText(strStar);

		// テキストデータの更新
		UpdateText();

        // 支払ボタンをクリック
        answer();

        mainGame();
    }


    public void UpdateText() {
		//支払金額
		sTextShiharai.setText(Integer.toString(sQuestionMng.sShirarai));
		//お釣り金額
		sTextOtsuri.setText(Integer.toString(sQuestionMng.sOtsuri));

		//レベル数
		sTextLevelNum.setText(Integer.toString(sQuestionMng.sLevelNum));
		//正解数
		sTextSeikaiNum.setText(Integer.toString(sQuestionMng.sSeikaiNum));
		//クリア数
		sTextClearNum.setText(Integer.toString(sQuestionMng.sClearNum));
		//不正解数
		sTextHuSeikaiNum.setText(Integer.toString(sQuestionMng.sHuSeikaiNum));
		//ノットクリア数
		sTextNotClearNum.setText(Integer.toString(sQuestionMng.sNotClearNum));


		//if( sQuestionMng.sAnswerCoinNum.get("1") != null ) sTextSeikaiIchien.setText(Integer.toString(sQuestionMng.sSeikaiIchien));
		if (sQuestionMng.sAnswerCoinNum != null) {

            sLLSeikai.setVisibility(View.VISIBLE);

			sTextSeikaiIchien.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("1")));
			if( sQuestionMng.sAnswerCoinNum.get("1") > 0 ) sTextSeikaiIchien.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiIchien.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGoen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("5")));
			if( sQuestionMng.sAnswerCoinNum.get("5") > 0 ) sTextSeikaiGoen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiGoen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiJyuen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("10")));
			if( sQuestionMng.sAnswerCoinNum.get("10") > 0 ) sTextSeikaiJyuen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiJyuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGojyuen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("50")));
			if( sQuestionMng.sAnswerCoinNum.get("50") > 0 ) sTextSeikaiGojyuen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiGojyuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiHyakuen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("100")));
			if( sQuestionMng.sAnswerCoinNum.get("100") > 0 ) sTextSeikaiHyakuen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiHyakuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGohyakuen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("500")));
			if( sQuestionMng.sAnswerCoinNum.get("500") > 0 ) sTextSeikaiGohyakuen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiGohyakuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiSenen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("1000")));
			if( sQuestionMng.sAnswerCoinNum.get("1000") > 0 ) sTextSeikaiSenen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiSenen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGosenen.setText(Integer.toString(sQuestionMng.sAnswerCoinNum.get("5000")));
			if( sQuestionMng.sAnswerCoinNum.get("5000") > 0 ) sTextSeikaiGosenen.setTextColor(getResources().getColor(R.color.orenge));
			else  sTextSeikaiGosenen.setTextColor(getResources().getColor(R.color.pGreen));
		}
		else{
			Log.w( "AAAAA", "aaaawwwwwwwwwwwwwwwwwwww000 ");

            sLLSeikai.setVisibility(View.GONE);

			sTextSeikaiIchien.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGoen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiJyuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGojyuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiHyakuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGohyakuen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiSenen.setTextColor(getResources().getColor(R.color.pGreen));
			sTextSeikaiGohyakuen.setTextColor(getResources().getColor(R.color.pGreen));
		}
	}

    private void answer(){

        Button btn = (Button)findViewById(R.id.buttonAnswer);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( sNowAnserFlg == true || sGameOverFlg == true ) return;

                sNowThinkingFlg = false;
                sNowAnserFlg = true;


                HashMap<String,Integer> allCoinNum = CoinMng.GetCoinNum(CoinMng.ALL_POSITION);
                HashMap<String,Integer> walletCoinNum = CoinMng.GetCoinNum(CoinMng.WALLET_POSITION);
                HashMap<String,Integer> trayCoinNum = CoinMng.GetCoinNum(CoinMng.TRAY_POSITION);

                sQuestionOkFlg = sQuestionMng.answerQuestion(allCoinNum,trayCoinNum);
                if(sQuestionOkFlg){ // 正解
                    sMemTime = sLapTime;
//                    sCoinMng.OutSideCoin();
                    // コイン全削除
                    sCoinMng.DeleteCoins(0,CoinMng.TRAY_POSITION);
                    // トレイにお釣り表示
                    sCoinMng.CreateCoinChange(QuestionMng.sOtsuri,CoinMng.TRAY_POSITION);
                    // シンキングタイム1秒マイナス
                    //sQuestionMng.sThinkingTime -= 10;
                    // 正解数プラス
                    sQuestionMng.sSeikaiNum++;
					sQuestionMng.UpdateLevel();

                    Log.w( "DEBUG_DATA", "sQuestionMng.sSeikaiNum " + sQuestionMng.sSeikaiNum);
                    Log.w( "DEBUG_DATA", "sQuestionMng.sClearNum " + sQuestionMng.sClearNum);



                    // 五千円札がなければ補充
                    if( walletCoinNum.get("5000") == 0 ){
                        CoinMng.CreateCoin( 5000,1,CoinMng.WALLET_POSITION );
                    }
				}
				else{ // 不正解
                    sMemTime = sLapTime + 2;
					// 正解数プラス
					sQuestionMng.sHuSeikaiNum++;
                    //if( sQuestionMng.sHuSeikaiNum >= sQuestionMng.sNotClearNum ) gameClear(1);

				}
				// 正解コインを表示

                Log.w( "AAAAA", "okkkkkkkkkkkkkkkkkkk " + sQuestionOkFlg);
            }
        });
    }

    private void mainGame(){

        sOpeningFlg = true;
        sNewQuestionFlg = false;
        sNowThinkingFlg = false;
        sNowAnserFlg = false;
        sQuestionOkFlg = false;
        sNowAmountFlg = false;
        sGameOverFlg = false;

        sTimer = new Timer(true);
        sTimer.schedule(new TimerTask(){
            @Override
            public void run() {

                // sHandlerを通じてUI Threadへ処理をキューイング
                sHandler.post( new Runnable() {
                    public void run() {
                        roop();
                    }
                });
            }
        }, GAME_MILLI_SECOND, GAME_MILLI_SECOND); // 0.1秒間隔

    }

    void roop(){
        if( sGameOverFlg ) return;

        sImageOk.setVisibility(View.GONE);
        sImageNg.setVisibility(View.GONE);

        //実行間隔分を加算処理
        sLapTimeReal +=  0.1;

        //計算にゆらぎがあるので小数点第1位で丸める
        BigDecimal bi = new BigDecimal(sLapTimeReal);
        sLapTime = bi.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();


        //出題金額
        sTextOdai.setText(Integer.toString(sQuestionMng.sOdai));

        // カウントダウン終了
        if( sOpeningFlg == true && sLapTime > 2.0 ){
            sTextSenter.setVisibility(View.GONE);
            sOpeningFlg = false;
            sNewQuestionFlg = true; // とりあえず第一問
        }

        // 正解表示時間終了
        if( sNowAnserFlg == true ) {
							/*
                            //支払金額
                            sTextShiharai.setText(Integer.toString(sQuestionMng.sShirarai));
                            //お釣り金額
                            sTextOtsuri.setText(Integer.toString(sQuestionMng.sOtsuri));
                            //正解数
                            sTextSeikaiNum.setText(Integer.toString(sQuestionMng.sSeikaiNum));
							*/
            UpdateText();

            if ((sLapTime - sMemTime) > 2.0) {

                // クリア
                if (sQuestionMng.sSeikaiNum >= sQuestionMng.sClearNum) {
                    Intent intent = new Intent(GameActivity.this, ClearActivity.class);
                    intent.putExtra("STAR", sStarNum);
                    intent.putExtra("GAME_TIME", sGameTime);
                    // クリア画面の起動
                    startActivity(intent);
                    sGameOverFlg = true;
                }
                // ゲームオーバー
                else if (sQuestionMng.sHuSeikaiNum >= sQuestionMng.sNotClearNum) gameClear(1);
                else {
                    //支払金額
                    sTextShiharai.setText("0");
                    //お釣り金額
                    sTextOtsuri.setText("0");

                    // トレイに乗っているコインを全部財布に移動
                    CoinMng.MoveAllCoin(CoinMng.TRAY_POSITION, 1);

                    sNowAnserFlg = false;
                    sNewQuestionFlg = true;

                }
            }
        }

        // カウントダウン表示
        if( sOpeningFlg == true ){
            sTextSenter.setText("START");
            sTextSenter.setVisibility(View.VISIBLE);
        }
        // ゲーム開始
        else {
            // 新しい問題
            if (sNewQuestionFlg) {

                sQuestionMng.startProgressBar(100); // 初期値
                sNewQuestionFlg = false;
                sNowThinkingFlg = true;

                // 出題
                // 値段を取得 プログレスバーが起動
                sQuestionYen = sQuestionMng.NewQuestion();

            }
            // 解答結果待ち
            else if (sNowAnserFlg == true) {
                if (sQuestionOkFlg) {
                    sImageOk.setVisibility(View.VISIBLE);
                } else {
                    sImageNg.setVisibility(View.VISIBLE);
                }
                if (sNowAmountFlg) {

//                  CoinStatus coinStatus = new CoinStatus(1, 10, 0,true);
//                  CoinMng.AddCoin(sAmount);
                    sNowAmountFlg = false;
                }
            }
            if (sNowThinkingFlg) {

                // ゲーム時間の追加と表示
                addGameTime();

                boolean timeOverFlg = sQuestionMng.extendProgressBar(1); // 0.1秒づつバーを伸ばす
                // 時間切れ
                if (timeOverFlg == true) {
					// 解答のために正解を出しておく
					HashMap<String,Integer> allCoinNum = CoinMng.GetCoinNum(CoinMng.ALL_POSITION);
					HashMap<String,Integer> trayCoinNum = CoinMng.GetCoinNum(CoinMng.TRAY_POSITION);
					sQuestionMng.answerQuestion(allCoinNum,trayCoinNum);

					sLLSeikai.setVisibility(View.VISIBLE);
                    //sNewQuestionFlg = true;

                    sQuestionOkFlg = false;
                    sNowThinkingFlg = false;
                    sNowAnserFlg = true;
                    sMemTime = sLapTime + 2;
                    sQuestionMng.sHuSeikaiNum++; //
                    //if (sQuestionMng.sHuSeikaiNum >= sQuestionMng.sNotClearNum) gameClear(1);
                }
            }
        }
    }

    void addGameTime(){
        //実行間隔分を加算処理
        sGameTimeReal +=  0.1;

        //計算にゆらぎがあるので小数点第1位で丸める
        BigDecimal bi = new BigDecimal(sGameTimeReal);
        sGameTime = bi.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

        //現在のLapTime
        sTextGameTime.setText(Float.toString(sGameTime));
    }


    // 0:clear 1:not clear
    void gameClear( int mode ){
        if( mode == 1 ){
            sTextSenter.setText("GAME OVER");
            sTextSenter.setVisibility(View.VISIBLE);
            sGameOverFlg = true;
            sQuestionOkFlg = false;
            sNowThinkingFlg = false;
            sNowAnserFlg = false;
            UpdateText();

        }
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