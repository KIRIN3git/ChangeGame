package com.example.shinji.changegame;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;

import android.view.View.OnTouchListener;


/**
 * Created by shinji on 2017/09/12.
 */

public class CoinMng implements OnTouchListener{

    static Context mContext;
    static FrameLayout mLayout;
    static View.OnTouchListener mVOL;

    // コイン種類
    static String coinType[] = {"1yen","5yen","10yen","50yen","100yen","500yen","1000yen"};
    // コインの枚数
    static int coinCount1 = 0;

    // コインサイズ
    static int ICHIYEN_SIZE_DP = 50;
    static int ICHIYEN_SIZE_PX;

    static int GOYEN_SIZE_DP = 50;
    static int GOYEN_SIZE_PX;

    static int JYUYEN_SIZE_DP = 50;
    static int JYUYEN_SIZE_PX;

    static int GOJYUYEN_SIZE_DP = 50;
    static int GOJYUYEN_SIZE_PX;

    static int HYAKUYEN_SIZE_DP = 50;
    static int HYAKUYEN_SIZE_PX;

    static int GOHYAKUYEN_SIZE_DP = 50;
    static int GOHYAKUYEN_SIZE_PX;

    static int SENYEN_SIZE_DP = 60;
    static int SENYEN_SIZE_PX;

    // 財布に入っているコインの数
    static HashMap<String,Integer> walletCoinNum;
    // トレーに入っているコインの数
    static HashMap<String,Integer> trayCoinNum;



    // 1円のID情報
    static ArrayList<Integer> coinIds1 = new ArrayList<Integer>();

    // 1円の位置情報
    static ArrayList<ArrayList<Float>> coinXY1 = new ArrayList<ArrayList<Float>>();

    public static ArrayList<CoinStatus> coinStatuses = new ArrayList<CoinStatus>();

    static int ichienImageId = R.drawable.ichien;
    static int goenImageId = R.drawable.goen;
    static int jyuenImageId = R.drawable.jyuen;
    static int gojyuenImageId = R.drawable.gojyuen;
    static int hyakuenImageId = R.drawable.hyakuen;
    static int gohyakuenImageId = R.drawable.gohyakuen;
    static int senenImageId = R.drawable.senen;

    public CoinMng(Context context,FrameLayout layout) {
        mContext = context;
        mLayout = layout;
        mVOL = (android.view.View.OnTouchListener)this;
    }

    public void CoinInit(){

        int i,x;
        CoinStatus coinMng;

        // dp→px変換
        float density = mContext.getResources().getDisplayMetrics().density;
        ICHIYEN_SIZE_PX = CommonMng.PxToDp2(ICHIYEN_SIZE_DP,density);
        GOYEN_SIZE_PX = CommonMng.PxToDp2(GOYEN_SIZE_DP,density);
        JYUYEN_SIZE_PX = CommonMng.PxToDp2(JYUYEN_SIZE_DP,density);
        GOJYUYEN_SIZE_PX = CommonMng.PxToDp2(GOJYUYEN_SIZE_DP,density);
        HYAKUYEN_SIZE_PX = CommonMng.PxToDp2(HYAKUYEN_SIZE_DP,density);
        GOHYAKUYEN_SIZE_PX = CommonMng.PxToDp2(GOHYAKUYEN_SIZE_DP,density);
        SENYEN_SIZE_PX = CommonMng.PxToDp2(SENYEN_SIZE_DP,density);


        // 1円の複数枚表示
        x = 5;
        for( i = 0; i < 4; i++ ) {
            coinMng = new CoinStatus(1, x, 75);
            coinStatuses.add(coinMng);
            x+=2;
        }

        // 5円の複数枚表示
        x = 29;
        coinMng = new CoinStatus(5, x, 75);
        coinStatuses.add(coinMng);

        // 10円の複数枚表示
        x = 55;
        for( i = 0; i < 4; i++ ) {
            coinMng = new CoinStatus(10, x, 75);
            coinStatuses.add(coinMng);
            x+=2;
        }

        // 50円の複数枚表示
        x = 79;
        coinMng = new CoinStatus(50, x, 75);
        coinStatuses.add(coinMng);

        // 100円の複数枚表示
        x = 5;
        for( i = 0; i < 4; i++ ) {
            coinMng = new CoinStatus(100, x, 87);
            coinStatuses.add(coinMng);
            x+=2;
        }

        // 500円の複数枚表示
        x = 29;
        coinMng = new CoinStatus(500, x, 87);
        coinStatuses.add(coinMng);


        // 1000円の複数枚表示
        x = 55;
        coinMng = new CoinStatus(1000, x, 87);
        coinStatuses.add(coinMng);

    }



    public CoinStatus GetStatus(int viewId ){
        for( int i = 0; i < coinStatuses.size(); i++ ){
            if( coinStatuses.get(i).sViewId == viewId ) return coinStatuses.get(i);
        }
        return null;
    }

    /*
    CoinStatusからamountでtrayStatus==falseで、配列から一番上のものを取得
    mode:0 財布の一番上
    mode:1 トレーの一番上
     */
    public CoinStatus GetTopStatus(int amount,int mode ){
        Log.w( "DEBUG_DATA", "amount " + amount);
        Log.w( "DEBUG_DATA", "coinStatuses.size()  " + coinStatuses.size() );
        for( int i = coinStatuses.size() - 1; i >= 0; i-- ){
            Log.w( "DEBUG_DATA", "i " + i);
            Log.w( "DEBUG_DATA", "coinStatuses.get(i).sAmount " + coinStatuses.get(i).sAmount);
            if( coinStatuses.get(i).sAmount== amount ){
                if( mode == 0 && coinStatuses.get(i).walletFlg == true && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).walletFlg == false && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
            }
        }
        return null;
    }


    /*
CoinStatusからamountでtrayStatus==falseで、配列から一番上のものを取得
mode:0 財布の一番上
mode:1 トレーの一番上
 */
    public CoinStatus GetBottomStatus(int amount, int mode ){
        Log.w( "DEBUG_DATA", "amount " + amount);
        Log.w( "DEBUG_DATA", "coinStatuses.size()  " + coinStatuses.size() );
        for( int i = 0; i < coinStatuses.size(); i++ ){
            Log.w( "DEBUG_DATA", "i " + i);
            Log.w( "DEBUG_DATA", "coinStatuses.get(i).sAmount " + coinStatuses.get(i).sAmount);
            if( coinStatuses.get(i).sAmount== amount ){
                if( mode == 0 && coinStatuses.get(i).walletFlg == true && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).walletFlg == false && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
            }
        }
        return null;
    }


    public boolean onTouch(View v, MotionEvent event) {
        String currAction = "";
        Log.w( "DEBUG_DATA", "v.toString() " + v.toString());
        Log.w( "DEBUG_DATA", "v.getId() " + v.getId());
        Log.w( "DEBUG_DATA", "v.getResources() " + v.getResources());
        Log.w( "DEBUG_DATAxx2", "y00 " + v.getTop());

        // イベントの状態を調べる
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                Log.w("DEBUG_DATA1", "DOWN2");
                // タッチしたコインを取得
                CoinStatus touchCs = GetStatus(v.getId());

                if( touchCs.moveFlg ) return false;
                if( touchCs.CheckMoveOk() == false ) return false;

/*
                if( touchCs.touchOkFlg == false ){
                    Log.w("DEBUG_DATAxx0", "NOOOOOOOOOOO = " + touchCs.touchOkFlg);
                    return false;
                }
                else{
                    Log.w("DEBUG_DATAxx0", "OKKKKKKKKKKKKKKKKKKKKKKKK = " + touchCs.touchOkFlg);

                }
*/

                Log.w("DEBUG_DATAxx0", "touchCs.sViewId = " + touchCs.sViewId);
                Log.w("DEBUG_DATA1", "touchCs.imageId = " + touchCs.sImageId);
                Log.w("DEBUG_DATA1", "touchCs.x = " + touchCs.sX);
                Log.w("DEBUG_DATA1", "touchCs.y = " + touchCs.sY);
                Log.w("DEBUG_DATA1", "touchCs.sAmount = " + touchCs.sAmount);
                Log.w("DEBUG_DATAxx1", "touchCs.moveFlg = " + touchCs.moveFlg);

                // 財布のコインをタッチしてたら
                if (touchCs.walletFlg == true) {

   //                 if( touchCs.sY  500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetBottomStatus(touchCs.sAmount, 0);
                    if( topCs == null) return false;
                    Log.w("DEBUG_DATAxx1", "topCs.moveFlg = " + topCs.moveFlg);
                    Log.w("DEBUG_DATAxx1", "topCs.sViewId = " + topCs.sViewId);
                    Log.w("DEBUG_DATA2", "topCs.imageId = " + topCs.sImageId);
                    Log.w("DEBUG_DATA2", "topCs.x = " + topCs.sX);
                    Log.w("DEBUG_DATA2", "topCs.y = " + topCs.sY);
                    Log.w("DEBUG_DATA2", "topCs.sAmount = " + topCs.sAmount);
                    topCs.MoveCoin(0);
                }
                // トレーのコインをタッチしてたら
                else {

  //                  if( touchCs.sY > 500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetBottomStatus(touchCs.sAmount, 1);
                    if( topCs == null) return false;


                    Log.w("DEBUG_DATAxx1", "topCs.sViewId = " + topCs.sViewId);
                    Log.w("DEBUG_DATA3", "topCs.imageId = " + topCs.sImageId);
                    Log.w("DEBUG_DATA3", "topCs.x = " + topCs.sX);
                    Log.w("DEBUG_DATA3", "topCs.y = " + topCs.sY);
                    Log.w("DEBUG_DATA3", "topCs.sAmount = " + topCs.sAmount);
                    topCs.MoveCoin(1);
                }

                break;
        }

        return true;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        Log.w( "DEBUG_DATAxx2", "y00x " + event.getY());
        return true;
    }

    public static int GetCoinImage( int amount ){
        if( amount == 1 ) return CoinMng.ichienImageId;
        else if( amount == 5 ) return CoinMng.goenImageId;
        else if( amount == 10 ) return CoinMng.jyuenImageId;
        else if( amount == 50 ) return CoinMng.gojyuenImageId;
        else if( amount == 100 ) return CoinMng.hyakuenImageId;
        else if( amount == 500 ) return CoinMng.gohyakuenImageId;
        else if( amount == 1000 ) return CoinMng.senenImageId;

        return 0;
    }

    public static int GetCoinSize( int amount ){
        if( amount == 1 ) return ICHIYEN_SIZE_PX;
        else if( amount == 5 ) return GOYEN_SIZE_PX;
        else if( amount == 10 ) return JYUYEN_SIZE_PX;
        else if( amount == 50 ) return GOJYUYEN_SIZE_PX;
        else if( amount == 100 ) return HYAKUYEN_SIZE_PX;
        else if( amount == 500 ) return GOHYAKUYEN_SIZE_PX;
        else if( amount == 1000 ) return SENYEN_SIZE_PX;

        return 0;
    }

    // 財布内のコイン数を取得
    public static HashMap<String,Integer> GetWalletNum(){
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).walletFlg) {
                if (coinStatuses.get(i).sAmount == 1) ichien++;
                else if (coinStatuses.get(i).sAmount == 5) goen++;
                else if (coinStatuses.get(i).sAmount == 10) jyuen++;
                else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
                else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
                else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
                else if (coinStatuses.get(i).sAmount == 1000) senen++;
            }
        }

        HashMap<String,Integer> num = new HashMap<String,Integer>();
        num.put("1",ichien);
        num.put("5",goen);
        num.put("10",jyuen);
        num.put("50",gojyuen);
        num.put("100",hyakuen);
        num.put("500",gohyaku);
        num.put("1000",senen);

        Log.w( "KEKKA", "ichien = [" + ichien + "]");
        Log.w( "KEKKA", "goen = [" + goen + "]");
        Log.w( "KEKKA", "jyuen = [" + jyuen + "]");
        Log.w( "KEKKA", "gojyuen = [" + gojyuen + "]");
        Log.w( "KEKKA", "hyakuen = [" + hyakuen + "]");
        Log.w( "KEKKA", "gohyaku = [" + gohyaku + "]");
        Log.w( "KEKKA", "senen = [" + senen + "]");

        return num;
    }

    // 財布内のコイン数を取得
    public static HashMap<String,Integer> GetTrayNum(){
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).walletFlg == false) {
                if (coinStatuses.get(i).sAmount == 1) ichien++;
                else if (coinStatuses.get(i).sAmount == 5) goen++;
                else if (coinStatuses.get(i).sAmount == 10) jyuen++;
                else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
                else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
                else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
                else if (coinStatuses.get(i).sAmount == 1000) senen++;
            }
        }

        HashMap<String,Integer> num = new HashMap<String,Integer>();
        num.put("1",ichien);
        num.put("5",goen);
        num.put("10",jyuen);
        num.put("50",gojyuen);
        num.put("100",hyakuen);
        num.put("500",gohyaku);
        num.put("1000",senen);

        Log.w( "KEKKA", "ichien = [" + ichien + "]");
        Log.w( "KEKKA", "goen = [" + goen + "]");
        Log.w( "KEKKA", "jyuen = [" + jyuen + "]");
        Log.w( "KEKKA", "gojyuen = [" + gojyuen + "]");
        Log.w( "KEKKA", "hyakuen = [" + hyakuen + "]");
        Log.w( "KEKKA", "gohyaku = [" + gohyaku + "]");
        Log.w( "KEKKA", "senen = [" + senen + "]");

        return num;
    }
}
