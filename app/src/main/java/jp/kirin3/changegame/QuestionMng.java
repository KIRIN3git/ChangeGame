package jp.kirin3.changegame;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
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
    static ProgressBar sProgressBar;

    Timer sTimer = null;
    TextView sTextView;
    // プログレスバーのマックス秒数×１０
    static int sMaxPB;
    // プログレスバーの現在の秒数×１０
    static int sNowPB;
    static int sPointNum = 0;

	static int sStarNum = 0;
	static int sLevelNum = 0;

    // シンキングタイム（1/10秒）
    static int sThinkingTime = 200;

	// 最小お題金額
	static int sMinOdaiPrice;
	// ランダムお題金額
	static int sRandomOdaiPrice;

	// スタートレベル,クリア正解数,間違い可能数
	static int sStarInfo[][] = {
			{ 0,0,0 },

			{ 1,5,3 },  // スター1
			{ 3,7,3 },  // スター2
			{ 4,10,2 }, // スター3
			{ 8,15,2 }, // スター4
			{ 8,30,1 }, // スター5
	};

	// シンキングタイム,クリア正解数,最低金額、ランダム金額
	static int sLevelInfo[][] = {
			{ 0,0,0,0 },

            { 200,2,10  ,500  }, // レベル1
			{ 190,2,100 ,600  }, // レベル2
			{ 180,2,200 ,700  }, // レベル3
			{ 170,2,300 ,900  }, // レベル4
			{ 160,2,400 ,1000 }, // レベル5
			{ 150,2,500 ,1100 }, // レベル6
			{ 140,2,600 ,1200 }, // レベル7
			{ 130,2,70  ,1300 }, // レベル8
			{ 120,2,700 ,1400 }, // レベル9
			{ 110,2,800 ,1500 }, // レベル10
			{ 100,3,900 ,1600 }, // レベル11
			{  90,3,1000,1700 }, // レベル12
			{  80,2,1500,1800 }, // レベル13
			{  70,5,2000,1900 }, // レベル14
			{  60,4,2000,2000 }, // レベル15
			{  50,3,2000,2000 }, // レベル16
			{  40,2,2000,2000 }, // レベル17
			{  30,2,2000,2000 }, // レベル18
			{  20,2,2000,2000 }, // レベル19
			{  10,2,2000,2000 }, // レベル20
	};

	static int sLevelSeikaiNum = 0;

    static int sSeikaiNum = 0;
	static int sClearNum = 0;
	static int sHuSeikaiNum = 0;
	static int sNotClearNum = 0;

    // 出題金額
    static int sOdai = 0;
    // 支払い金額
    static int sShirarai = 0;
    // お釣り金額
    static int sOtsuri = 0;

	// 正解コイン数
	static HashMap<String,Integer> sAnswerCoinNum = null;

    public QuestionMng(Context context, LinearLayout layout,Handler handler,int starNum) {
        sContext = context;
        sLayout = layout;
		sStarNum = starNum;

        // プログレスバーの設定
        sProgressBar = sLayout.findViewById(R.id.ProgressBarHorizontal);
        sProgressBar.setScaleY(40f); // 高さを指定

		// レベル正解数を初期化
		sLevelSeikaiNum = 0;
        // 正解数を初期化
        sSeikaiNum = 0;
		// 不正解数を初期化
		sHuSeikaiNum = 0;
		// 初期レベルを初期化
		sLevelNum = sStarInfo[sStarNum][0];
		// クリア数を初期化
		sClearNum = sStarInfo[sStarNum][1];
		// ノットクリア数を初期化
		sNotClearNum = sStarInfo[sStarNum][2];
		// シンキングタイムを初期化
		sThinkingTime = sLevelInfo[sLevelNum][0];
		// 最小お題金額
		sMinOdaiPrice = sLevelInfo[sLevelNum][2];
		// ランダムお題金額
		sRandomOdaiPrice = sLevelInfo[sLevelNum][3];

		// 出題金額
		sOdai = 0;
		// 支払い金額
		sShirarai = 0;
		// お釣り金額
		sOtsuri = 0;

		// 正解コイン数
		sAnswerCoinNum = null;
     }

    // プログレスバーの開始
    public void startProgressBar(int max){
        sMaxPB = max;
        sNowPB = 0;
        sProgressBar.setMax(sMaxPB); // 水平プログレスバーの最大値を設定
        sProgressBar.setProgress(0);
    }

    /*
    プログレスバーの延長
    tenSec:経過秒数/10
     */
    public boolean extendProgressBar(int tenSec ){

        sNowPB += tenSec;

        sProgressBar.setProgress(sNowPB);

        ObjectAnimator animation = ObjectAnimator.ofInt(sProgressBar, "progress", (int)sNowPB);
        animation.setDuration(GameActivity.GAME_MILLI_SECOND); // 0.1 secondかけてアニメーション
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        return sMaxPB <= sNowPB;

    }

    public int NewQuestion(){

        sOdai = (int)(Math.random()*sRandomOdaiPrice + sMinOdaiPrice);
        startProgressBar(sThinkingTime);

        return sOdai;
    }

    /*
    ・問題に回答する
    true:正解
    false:不正解
     */
    public boolean answerQuestion(HashMap<String,Integer> allCoinNum,HashMap<String,Integer> trayCoinNum){

        //sOdai = 900;

        if( sOdai == 0 ){
            return false;
        }

		sAnswerCoinNum = new HashMap<String,Integer>();

        int a = 0,b = 0,c = 0,d = 0; // a:一の位,b:十の位,c:百の位,d:千の位
        if( 1000 <= sOdai) d = sOdai / 1000;
        if( 100 <= sOdai) c = ( sOdai - (d * 1000) ) / 100;
        if( 10 <= sOdai) b = ( sOdai -(d * 1000) - (c * 100) ) / 10;
        a = sOdai % 10;

        // 〇一の位の計算
        // 5を引いた金額
        int aa,bb,cc,dd;

        if(a >= 5) aa = a - 5;
        else aa = a;

        // 全コインの各桁の値
        int allIchi = allCoinNum.get("1") + ( allCoinNum.get("5") * 5 );
        int allJyu = allCoinNum.get("10") + ( allCoinNum.get("50") * 5 );
        int allHyaku = allCoinNum.get("100") + ( allCoinNum.get("500") * 5 );
        int allSen = allCoinNum.get("1000") + ( allCoinNum.get("5000") * 5 );
        boolean fewIchiFlg = false,fewJyuFlg = false,fewHyakuFlg = false,fewSenFlg = false;

        // 一の位が足りない
        if( allIchi < a ) fewIchiFlg = true;

        // 一の位が足りていたら
        if( fewIchiFlg == false ){
            if( allCoinNum.get("1") >= aa ){
                sAnswerCoinNum.put("1",aa); // 1円支払い
				if( a >= 5 ) sAnswerCoinNum.put("5",1); //5円支払い
            }
            // 一円玉が足りないので5円支払い
            else sAnswerCoinNum.put("5",1); // 5円支払い
        }
        // 一の位が足りていなかったら
        else {
            // 5を引いた一の位が払えるなら払っておく
            if (allCoinNum.get("1") >= aa) {
                sAnswerCoinNum.put("1", aa);
            }
            // 5円以下で5円玉があるならなら5円玉は払っておく
            else if ( a < 5 && allCoinNum.get("5") >= 1 ){
                sAnswerCoinNum.put("5", 1);
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
				sAnswerCoinNum.put("10",bb); // 10円支払い
				if( b >= 5 ) sAnswerCoinNum.put("50",1); //50円支払い
			}
			// 十円玉が足りないので50円支払い
			else sAnswerCoinNum.put("50",1); // 50円支払い
        }
        // 十の位が足りていなかったら
        else {
            // 5を引いた十の位が払えるなら払っておく
            if (allCoinNum.get("10") >= bb) {
                sAnswerCoinNum.put("10", bb);
            }
            // 50円以下で50円玉があるならなら50円玉は払っておく
            else if ( b < 5  && allCoinNum.get("50") >= 1){
                sAnswerCoinNum.put("50", 1);
            }
        }

        // 十の位の負債を百の位に追加
        if( fewJyuFlg ) c++;

        if(c >= 5) cc = c - 5;
        else cc = c;
        // 百の位が足りない
        if( allHyaku < c ) fewHyakuFlg = true;

        // 百の位が足りていたら
		if( fewHyakuFlg == false ){
			if( allCoinNum.get("100") >= cc ){
				sAnswerCoinNum.put("100",cc); // 100円支払い
				if(c >= 5 ){
					sAnswerCoinNum.put("500",1); //500円支払い
				}
			}
			// 百円玉が足りないので500円支払い
			else sAnswerCoinNum.put("500",1); // 50円支払い
		}
        // 百の位が足りていなかったら
        else {
            // 5を引いた百の位が払えるなら払っておく
            if (allCoinNum.get("100") >= cc) {
                sAnswerCoinNum.put("100", cc);
            }
            // 500円以下で500円玉があるなら500円玉は払っておく
            else if ( c < 5  && allCoinNum.get("500") >= 1 ){
                sAnswerCoinNum.put("500", 1);
            }
        }

        // 百の位の負債を千の位に追加
        if( fewHyakuFlg ) d++;

        if(d >= 5) dd = d - 5;
        else dd = d;

		if( allCoinNum.get("1000") >= dd ){
			sAnswerCoinNum.put("1000",dd); // 1000円支払い
			if( d >= 5 ) sAnswerCoinNum.put("5000",1); //5000円支払い
		}
		// 千円札が足りないので5000円支払い
		else sAnswerCoinNum.put("5000",1); // 5000円支払い

        if( sAnswerCoinNum.get("1") == null ) sAnswerCoinNum.put( "1",0 );
        if( sAnswerCoinNum.get("5") == null ) sAnswerCoinNum.put( "5",0 );
        if( sAnswerCoinNum.get("10") == null ) sAnswerCoinNum.put( "10",0 );
        if( sAnswerCoinNum.get("50") == null ) sAnswerCoinNum.put( "50",0 );
        if( sAnswerCoinNum.get("100") == null ) sAnswerCoinNum.put( "100",0 );
        if( sAnswerCoinNum.get("500") == null ) sAnswerCoinNum.put( "500",0 );
        if( sAnswerCoinNum.get("1000") == null ) sAnswerCoinNum.put( "1000",0 );
		if( sAnswerCoinNum.get("5000") == null ) sAnswerCoinNum.put( "5000",0 );

        //支払い金額をセット
        sShirarai = ( ( trayCoinNum.get("5000") * 5000 ) + ( trayCoinNum.get("1000") * 1000 ) + ( trayCoinNum.get("500") * 500 ) + ( trayCoinNum.get("100") * 100 )
                + ( trayCoinNum.get("50") * 50 ) + ( trayCoinNum.get("10") * 10 ) + ( trayCoinNum.get("5") * 5 ) + ( trayCoinNum.get("1") * 1 ) );

        //お釣りをセット
        sOtsuri = sShirarai - sOdai;

        return sAnswerCoinNum.get("1") == trayCoinNum.get("1") &&
                sAnswerCoinNum.get("5") == trayCoinNum.get("5") &&
                sAnswerCoinNum.get("10") == trayCoinNum.get("10") &&
                sAnswerCoinNum.get("50") == trayCoinNum.get("50") &&
                sAnswerCoinNum.get("100") == trayCoinNum.get("100") &&
                sAnswerCoinNum.get("500") == trayCoinNum.get("500") &&
                sAnswerCoinNum.get("1000") == trayCoinNum.get("1000") &&
                sAnswerCoinNum.get("5000") == trayCoinNum.get("5000");

    }

	/*
		レベルアップ
	 */
	public void UpdateLevel(){
		sLevelSeikaiNum++;
		if( sLevelSeikaiNum >= sLevelInfo[sLevelNum][1] ){
			sLevelNum++;
			sThinkingTime = sLevelInfo[sLevelNum][0];
			// 最小お題金額
			sMinOdaiPrice = sLevelInfo[sLevelNum][2];
			// ランダムお題金額
			sRandomOdaiPrice = sLevelInfo[sLevelNum][3];

			sLevelSeikaiNum = 0;
		}

    }
}

