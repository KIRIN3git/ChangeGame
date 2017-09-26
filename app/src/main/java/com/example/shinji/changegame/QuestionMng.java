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
    static int sAmount = 0;



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

        sAmount = (int)(Math.random()*1700 + 300);
        startProgressBar(sThinkingTime);

        Log.w( "AAAAA", "aaa sAmount = " + sAmount);
        return sAmount;
    }

    /*
    ・問題に回答する
    true:正解
    false:不正解
     */
    public boolean anserQuestion(HashMap<String,Integer> allCoinNum,HashMap<String,Integer> trayCoinNum){

        //sAmount = 934;

        if( sAmount == 0 ){
            return false;
        }

        // 正解ハッシュマップ
        HashMap<String,Integer> anserCoinNum = new HashMap<String,Integer>();

        int a = 0,b = 0,c = 0,d = 0; // a:一の位,b:十の位,c:百の位,d:千の位
        if( 1000 <= sAmount ) d = sAmount / 1000;
        if( 100 <= sAmount ) c = ( sAmount - (d * 1000) ) / 100;
        if( 10 <= sAmount ) b = ( sAmount -(d * 1000) - (c * 100) ) / 10;
        a = sAmount % 10;

        // 〇一の位の計算
        // 5を引いた金額
        int aa,bb,cc,dd;
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
        int allSen = allCoinNum.get("1000");
        boolean fewIchiFlg = false,fewJyuFlg = false,fewHyakuFlg = false,fewSenFlg = false;


        Log.w( "AAAAABBBBB", "allIchi [" + allIchi + "]");
        Log.w( "AAAAABBBBB", "allJyu [" + allJyu + "]");
        Log.w( "AAAAABBBBB", "allHyaku [" + allHyaku + "]");
        Log.w( "AAAAABBBBB", "allSen [" + allSen + "]");

        // 一の位が足りない
        if( allIchi < a ) fewIchiFlg = true;

        // 一の位が足りていたら
        if( fewIchiFlg == false ){
            if( allCoinNum.get("1") >= aa ) anserCoinNum.put("1",aa);
            // 3円で7円持ちだったら5円払わなくてはならない
            else if( aa > 0 )  anserCoinNum.put("5",1);
            if( a >= 5 ) anserCoinNum.put("5",1);
        }
        // 一の位が足りていなかったら
        else {
            // 5を引いた一の位が払えるなら払っておく
            if (allCoinNum.get("1") >= aa) {
                anserCoinNum.put("1", aa);
            }
            // 5円以下で5円玉があるならなら5円玉は払っておく
            else if ( a < 5 && allCoinNum.get("5") >= 1 ){
                anserCoinNum.put("5", 1);
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
            if( allCoinNum.get("10") >= bb ) anserCoinNum.put("10",bb);
            // 30円で70円持ちだったら50円払わなくてはならない
            else if( bb > 0 )  anserCoinNum.put("50",1);
            if( b >= 5 ) anserCoinNum.put("50",1);
        }
        // 十の位が足りていなかったら
        else {
            // 5を引いた十の位が払えるなら払っておく
            if (allCoinNum.get("10") >= bb) {
                anserCoinNum.put("10", bb);
            }
            // 50円以下で50円玉があるならなら50円玉は払っておく
            else if ( b < 5  && allCoinNum.get("50") >= 1){
                anserCoinNum.put("50", 1);
            }
        }

        // 十の位の負債を百の位に追加
        if( fewJyuFlg ) c++;
        if(c >= 5) cc = c - 5;
        else cc = c;
Log.w( "AAAAA", "ccccccccccc " + c);
        // 百の位が足りない
        if( allHyaku < c ) fewHyakuFlg = true;

        // 百の位が足りていたら
        if( fewHyakuFlg == false ){
            Log.w( "AAAAA", "BBBBBBBBBBBBBBB1");
            if( allCoinNum.get("100") >= cc ) anserCoinNum.put("100",cc);
            // 300円で700円持ちだったら500円払わなくてはならない
            else if( cc > 0 )  anserCoinNum.put("500",1);

            if( c >= 5 ) anserCoinNum.put("500",1);
        }
        // 百の位が足りていなかったら
        else {
            // 5を引いた百の位が払えるなら払っておく
            if (allCoinNum.get("100") >= cc) {
                anserCoinNum.put("100", cc);
            }
            // 500円以下で500円玉があるなら500円玉は払っておく
            else if ( c < 5  && allCoinNum.get("500") >= 1 ){
                Log.w( "AAAAA", "BBBBBBBBBBBBBBB2");
                anserCoinNum.put("500", 1);
            }
        }

        // 百の位の負債を千の位に追加
        if( fewHyakuFlg ) d++;
        if(d >= 5) dd = d - 5;
        else dd = d;

        if( d > 0 ) anserCoinNum.put("1000", d);

        if( anserCoinNum.get("1") == null ) anserCoinNum.put( "1",0 );
        if( anserCoinNum.get("5") == null ) anserCoinNum.put( "5",0 );
        if( anserCoinNum.get("10") == null ) anserCoinNum.put( "10",0 );
        if( anserCoinNum.get("50") == null ) anserCoinNum.put( "50",0 );
        if( anserCoinNum.get("100") == null ) anserCoinNum.put( "100",0 );
        if( anserCoinNum.get("500") == null ) anserCoinNum.put( "500",0 );
        if( anserCoinNum.get("1000") == null ) anserCoinNum.put( "1000",0 );

        Log.w( "AAAAA", "1[" + anserCoinNum.get("1") + "]");
        Log.w( "AAAAA", "5[" + anserCoinNum.get("5") + "]");
        Log.w( "AAAAA", "10[" + anserCoinNum.get("10") + "]");
        Log.w( "AAAAA", "50[" + anserCoinNum.get("50") + "]");
        Log.w( "AAAAA", "100[" + anserCoinNum.get("100") + "]");
        Log.w( "AAAAA", "500[" + anserCoinNum.get("500") + "]");
        Log.w( "AAAAA", "1000[" + anserCoinNum.get("1000") + "]");

        if( anserCoinNum.get("1") == trayCoinNum.get("1") &&
                anserCoinNum.get("5") == trayCoinNum.get("5") &&
                anserCoinNum.get("10") == trayCoinNum.get("10") &&
                anserCoinNum.get("50") == trayCoinNum.get("50") &&
                anserCoinNum.get("100") == trayCoinNum.get("100") &&
                anserCoinNum.get("500") == trayCoinNum.get("500") &&
                anserCoinNum.get("1000") == trayCoinNum.get("1000")){
            return true;
        }


        return false;
    }
}

