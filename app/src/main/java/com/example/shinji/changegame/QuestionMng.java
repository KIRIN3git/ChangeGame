package com.example.shinji.changegame;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Timer;

/**
 * Created by etisu on 2017/09/23.
 */

public class QuestionMng {


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
    static int sPointNum = 0;

    static int sThinkingTime = 100;

    // 出題金額
    static int sPrice = 0;
    // お釣り
    static int sCharge = 0;

    // 正解コイン数
    static HashMap<String,Integer> answerCoinNum;


    public QuestionMng(Context context, LinearLayout layout,Handler handler) {
            sContext = context;
            sLayout = layout;

            // プログレスバーの設定
            progressBar = sLayout.findViewById(R.id.ProgressBarHorizontal);
            progressBar.setScaleY(30f); // 高さを指定

            // 前時間表示
            sTextView = sLayout.findViewById(R.id.textTime);
        }

    // プログレスバーの開始
    public void startProgressBar(int max){
        sMaxPB = max;
        sNowPB = 0;
        progressBar.setMax(sMaxPB); // 水平プログレスバーの最大値を設定
        progressBar.setProgress(0);
    }

    /*
    プログレスバーの延長
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

    public int NewQuestion(){

        sPrice = (int)(Math.random()*1700 + 300);
        startProgressBar(sThinkingTime);

        Log.w( "AAAAA", "aaa sPrice = " + sPrice);
        return sPrice;
    }

    /*
    ・問題に回答する
    true:正解
    false:不正解
     */
    public boolean answerQuestion(HashMap<String,Integer> allCoinNum,HashMap<String,Integer> trayCoinNum){

        //sPrice = 900;

        if( sPrice == 0 ){
            return false;
        }

        // 正解コイン数
        answerCoinNum = new HashMap<String,Integer>();

        int a = 0,b = 0,c = 0,d = 0; // a:一の位,b:十の位,c:百の位,d:千の位
        if( 1000 <= sPrice ) d = sPrice / 1000;
        if( 100 <= sPrice ) c = ( sPrice - (d * 1000) ) / 100;
        if( 10 <= sPrice ) b = ( sPrice -(d * 1000) - (c * 100) ) / 10;
        a = sPrice % 10;



        // 〇一の位の計算
        // 5を引いた金額
        int aa,bb,cc,dd;

		Log.w( "AAAAA aa", "answer c " + c );

        if(a >= 5) aa = a - 5;
        else aa = a;

        Log.w( "AAAAABBBBB", "a [" + a + "]");
        Log.w( "AAAAABBBBB", "b [" + b + "]");
        Log.w( "AAAAABBBBB", "c [" + c + "]");
        Log.w( "AAAAABBBBB", "d [" + d + "]");
        /*
        Log.w( "AAAAABBBBB", "aa [" + aa + "]");
        Log.w( "AAAAABBBBB", "bb [" + bb + "]");
        Log.w( "AAAAABBBBB", "cc [" + cc + "]");
        Log.w( "AAAAABBBBB", "dd [" + dd + "]");
*/

        // 全コインの各桁の値
        int allIchi = allCoinNum.get("1") + ( allCoinNum.get("5") * 5 );
        int allJyu = allCoinNum.get("10") + ( allCoinNum.get("50") * 5 );
        int allHyaku = allCoinNum.get("100") + ( allCoinNum.get("500") * 5 );
        int allSen = allCoinNum.get("1000") + ( allCoinNum.get("5000") * 5 );
        boolean fewIchiFlg = false,fewJyuFlg = false,fewHyakuFlg = false,fewSenFlg = false;


        Log.w( "AAAAABBBBB", "allIchi [" + allIchi + "]");
        Log.w( "AAAAABBBBB", "allJyu [" + allJyu + "]");
        Log.w( "AAAAABBBBB", "allHyaku [" + allHyaku + "]");
        Log.w( "AAAAABBBBB", "allSen [" + allSen + "]");

        // 一の位が足りない
        if( allIchi < a ) fewIchiFlg = true;

        // 一の位が足りていたら
        if( fewIchiFlg == false ){
            if( allCoinNum.get("1") >= aa ){
                answerCoinNum.put("1",aa); // 1円支払い
				if( a >= 5 ) answerCoinNum.put("5",1); //5円支払い
            }
            // 一円玉が足りないので5円支払い
            else answerCoinNum.put("5",1); // 5円支払い
        }
        // 一の位が足りていなかったら
        else {
            // 5を引いた一の位が払えるなら払っておく
            if (allCoinNum.get("1") >= aa) {
                answerCoinNum.put("1", aa);
            }
            // 5円以下で5円玉があるならなら5円玉は払っておく
            else if ( a < 5 && allCoinNum.get("5") >= 1 ){
                answerCoinNum.put("5", 1);
            }
        }

        // 一の位の負債を十の位に追加
        if( fewIchiFlg ) b++;

        if(b >= 5) bb = b - 5;
        else bb = b;

        // 十の位が足りない
        if( allJyu < b ) fewJyuFlg = true;

        // 十の位が足りていたら
        if( fewJyuFlg == false ){
			if( allCoinNum.get("10") >= bb ){
				answerCoinNum.put("10",bb); // 10円支払い
				if( b >= 5 ) answerCoinNum.put("50",1); //50円支払い
			}
			// 十円玉が足りないので50円支払い
			else answerCoinNum.put("50",1); // 50円支払い
        }
        // 十の位が足りていなかったら
        else {
            // 5を引いた十の位が払えるなら払っておく
            if (allCoinNum.get("10") >= bb) {
                answerCoinNum.put("10", bb);
            }
            // 50円以下で50円玉があるならなら50円玉は払っておく
            else if ( b < 5  && allCoinNum.get("50") >= 1){
                answerCoinNum.put("50", 1);
            }
        }

        // 十の位の負債を百の位に追加
        if( fewJyuFlg ) c++;

        if(c >= 5) cc = c - 5;
        else cc = c;
        // 百の位が足りない
        if( allHyaku < c ) fewHyakuFlg = true;

        // 百の位が足りていたら
		if( fewJyuFlg == false ){
			if( allCoinNum.get("100") >= cc ){
				answerCoinNum.put("100",cc); // 100円支払い
				Log.w( "AAAAA aa", "answer c2 " + c );
				if(c >= 5 ){
					Log.w( "AAAAA answer", "aaa1");
					answerCoinNum.put("500",1); //500円支払い
				}
			}
			// 百円玉が足りないので500円支払い
			else answerCoinNum.put("500",1); // 50円支払い
		}
        // 百の位が足りていなかったら
        else {
            // 5を引いた百の位が払えるなら払っておく
            if (allCoinNum.get("100") >= cc) {
                answerCoinNum.put("100", cc);
            }
            // 500円以下で500円玉があるなら500円玉は払っておく
            else if ( c < 5  && allCoinNum.get("500") >= 1 ){
                Log.w( "AAAAA", "BBBBBBBBBBBBBBB2");
                answerCoinNum.put("500", 1);
            }
        }

        // 百の位の負債を千の位に追加
        if( fewHyakuFlg ) d++;

        if(d >= 5) dd = d - 5;
        else dd = d;

		if( allCoinNum.get("1000") >= dd ){
			answerCoinNum.put("1000",dd); // 1000円支払い
			if( d >= 5 ) answerCoinNum.put("5000",1); //5000円支払い
		}
		// 千円札が足りないので5000円支払い
		else answerCoinNum.put("5000",1); // 5000円支払い

        if( answerCoinNum.get("1") == null ) answerCoinNum.put( "1",0 );
        if( answerCoinNum.get("5") == null ) answerCoinNum.put( "5",0 );
        if( answerCoinNum.get("10") == null ) answerCoinNum.put( "10",0 );
        if( answerCoinNum.get("50") == null ) answerCoinNum.put( "50",0 );
        if( answerCoinNum.get("100") == null ) answerCoinNum.put( "100",0 );
        if( answerCoinNum.get("500") == null ) answerCoinNum.put( "500",0 );
        if( answerCoinNum.get("1000") == null ) answerCoinNum.put( "1000",0 );
		if( answerCoinNum.get("5000") == null ) answerCoinNum.put( "5000",0 );

        Log.w( "answerCoinNum", "1[" + answerCoinNum.get("1") + "]");
        Log.w( "answerCoinNum", "5[" + answerCoinNum.get("5") + "]");
        Log.w( "answerCoinNum", "10[" + answerCoinNum.get("10") + "]");
        Log.w( "answerCoinNum", "50[" + answerCoinNum.get("50") + "]");
        Log.w( "answerCoinNum", "100[" + answerCoinNum.get("100") + "]");
        Log.w( "answerCoinNum", "500[" + answerCoinNum.get("500") + "]");
        Log.w( "answerCoinNum", "1000[" + answerCoinNum.get("1000") + "]");
		Log.w( "answerCoinNum", "5000[" + answerCoinNum.get("5000") + "]");

		Log.w( "trayCoinNum", "1[" + trayCoinNum.get("1") + "]");
		Log.w( "trayCoinNum", "5[" + trayCoinNum.get("5") + "]");
		Log.w( "trayCoinNum", "10[" + trayCoinNum.get("10") + "]");
		Log.w( "trayCoinNum", "50[" + trayCoinNum.get("50") + "]");
		Log.w( "trayCoinNum", "100[" + trayCoinNum.get("100") + "]");
		Log.w( "trayCoinNum", "500[" + trayCoinNum.get("500") + "]");
		Log.w( "trayCoinNum", "1000[" + trayCoinNum.get("1000") + "]");
		Log.w( "trayCoinNum", "5000[" + trayCoinNum.get("5000") + "]");

        if( answerCoinNum.get("1") == trayCoinNum.get("1") &&
                answerCoinNum.get("5") == trayCoinNum.get("5") &&
                answerCoinNum.get("10") == trayCoinNum.get("10") &&
                answerCoinNum.get("50") == trayCoinNum.get("50") &&
                answerCoinNum.get("100") == trayCoinNum.get("100") &&
                answerCoinNum.get("500") == trayCoinNum.get("500") &&
                answerCoinNum.get("1000") == trayCoinNum.get("1000") &&
				answerCoinNum.get("5000") == trayCoinNum.get("5000")){

            //お釣りをセット
            sCharge = ( ( trayCoinNum.get("5000") * 5000 ) + ( trayCoinNum.get("1000") * 1000 ) + ( trayCoinNum.get("500") * 500 ) + ( trayCoinNum.get("100") * 100 )
                    + ( trayCoinNum.get("50") * 50 ) + ( trayCoinNum.get("10") * 10 ) + ( trayCoinNum.get("5") * 5 ) + ( trayCoinNum.get("1") * 1 ) ) - sPrice;

            // CoinMng.AddCoin(changeAmount);

            return true;
        }

        return false;
    }
}

