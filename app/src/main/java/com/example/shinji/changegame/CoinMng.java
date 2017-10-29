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

    // 財布とトレーのy座標差（％）
    static final int wTotY = 40;

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

    static int SENYEN_SIZE_DP = 50;
    static int SENYEN_SIZE_PX;

    static int GOSENYEN_SIZE_DP = 50;
    static int GOSENYEN_SIZE_PX;

    // 財布の中でのY座標位置（パーセント）
    // 上の段
    static int COIN_Y_PS1 = 75;
    // 下の段
    static int COIN_Y_PS2 = 87;

    // 財布の中でのX座標位置（パーセント）
    static int COIN_X_PS1 = 5;
    static int COIN_X_PS2 = 29;
    static int COIN_X_PS3 = 55;
    static int COIN_X_PS4 = 79;


    static int coinsType[][] ={
            {1,ICHIYEN_SIZE_DP,ICHIYEN_SIZE_PX,COIN_X_PS1,COIN_Y_PS1},
            {5,GOYEN_SIZE_DP,GOYEN_SIZE_PX,COIN_X_PS2,COIN_Y_PS1},
            {10,JYUYEN_SIZE_DP,JYUYEN_SIZE_PX,COIN_X_PS3,COIN_Y_PS1},
            {50,GOJYUYEN_SIZE_DP,GOJYUYEN_SIZE_PX,COIN_X_PS4,COIN_Y_PS1},
            {100,HYAKUYEN_SIZE_DP,HYAKUYEN_SIZE_PX,COIN_X_PS1,COIN_Y_PS2},
            {500,GOHYAKUYEN_SIZE_DP,GOHYAKUYEN_SIZE_PX,COIN_X_PS2,COIN_Y_PS2},
            {1000,SENYEN_SIZE_DP,SENYEN_SIZE_PX,COIN_X_PS3,COIN_Y_PS2},
    };

    // 財布の中でのX座標位置のコインズレ（パーセント）
    static int COIN_X_SHIFT = 2;


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
    static int gosenenImageId = R.drawable.gosenen;

    public CoinMng(Context context,FrameLayout layout) {
        mContext = context;
        mLayout = layout;
        mVOL = (android.view.View.OnTouchListener)this;
    }

    public void CoinInit(){

        // dp→px変換
        float density = mContext.getResources().getDisplayMetrics().density;
        ICHIYEN_SIZE_PX = CommonMng.PxToDp2(ICHIYEN_SIZE_DP,density);
        GOYEN_SIZE_PX = CommonMng.PxToDp2(GOYEN_SIZE_DP,density);
        JYUYEN_SIZE_PX = CommonMng.PxToDp2(JYUYEN_SIZE_DP,density);
        GOJYUYEN_SIZE_PX = CommonMng.PxToDp2(GOJYUYEN_SIZE_DP,density);
        HYAKUYEN_SIZE_PX = CommonMng.PxToDp2(HYAKUYEN_SIZE_DP,density);
        GOHYAKUYEN_SIZE_PX = CommonMng.PxToDp2(GOHYAKUYEN_SIZE_DP,density);
        SENYEN_SIZE_PX = CommonMng.PxToDp2(SENYEN_SIZE_DP,density);
        GOSENYEN_SIZE_PX = CommonMng.PxToDp2(GOSENYEN_SIZE_DP,density);

        // コインの初期作成
        CreateCoin(1,4,CoinStatus.WALLET_POSITION);
        CreateCoin(5,1,CoinStatus.WALLET_POSITION);
        CreateCoin(10,4,CoinStatus.WALLET_POSITION);
        CreateCoin(50,1,CoinStatus.WALLET_POSITION);
        CreateCoin(100,4,CoinStatus.WALLET_POSITION);
        CreateCoin(500,2,CoinStatus.WALLET_POSITION);
        CreateCoin(1000,1,CoinStatus.WALLET_POSITION);

        //CreateCoin(10,4,false);


        /*
        // 5000円の複数枚表示
        x = COIN_X_PS4;
        coinMng = new CoinStatus(5000, x, COIN_Y_PS2);
        coinStatuses.add(coinMng);
        */
    }

    public static void CheckCoinsNum(Integer amount){
        Integer countWallet = 0;
        Integer countTray = 0;

        Log.w( "DEBUG_DATA", "count coinStatuses.size() " + coinStatuses.size());
        for( int i = 0; i < coinStatuses.size(); i++ ) {
            Log.w( "DEBUG_DATA", "count coinStatuses.get(i).sAmount " + coinStatuses.get(i).sAmount + "i " + i);
            if( coinStatuses.get(i).sAmount == 10 ){
                if( coinStatuses.get(i).position == 0 ){
//                    Log.w( "DEBUG_DATA", "count DELLLLLLLLLL0 countWallet " + countWallet);
                    countWallet++;
                }
                else if( coinStatuses.get(i).position == 1 ){
//                    Log.w( "DEBUG_DATA", "count NOTTTTTTTTTT0 countTray " + countTray);
                    countTray++;
                }
            }
        }

        Log.w( "DEBUG_DATA", "countWallet " + countWallet );
        Log.w( "DEBUG_DATA", "countTray " + countTray );
    }
    /*
     指定した金額の整形を行う
     option
        1:財布
        2:トレー
    */
    public static void CleaningCoins( Integer amount,Integer position ){
        Log.w( "DEBUG_DATA", "CleaningCoins position" + position);

        Integer count = 0;
        count = DeleteCoins(amount,position);

        // 削除分作成
        CreateCoin( amount,count,position );
    }

    // amount:0 全削除
    public static int DeleteCoins( Integer amount,Integer position ){
        Integer count = 0;
        for( int i = coinStatuses.size() - 1; i >= 0; i-- ) {
            if( coinStatuses.get(i).sAmount == amount || amount == 0 ){
                if( coinStatuses.get(i).position == position ) {
                    count++;
                    coinStatuses.get(i).removeCoin(); // 画像削除
                    coinStatuses.remove(i); // 配列から削除
                }
            }
        }
        return  count;
    }



    public static HashMap<String,Integer> ChangeChargeToHashmap( Integer charge ){
        HashMap<String,Integer> coinNum = new HashMap<String,Integer>();


        return coinNum;
    }

    public static void CreateCoinChange( Integer charge,Integer position ){

        if( charge == 0 ) return;

        int num = 0;
        for(int i = coinsType.length - 1; i >= 0 ; i--){
            Log.w( "DEBUG_DATA", "coinsType[i][0]" + coinsType[i][0]);
            num = charge / coinsType[i][0];
            CreateCoin( coinsType[i][0],num,position );
        }
    }
    public static void CreateCoin( Integer amount,Integer num,Integer position ){

        int x,y,typeNum;
        CoinStatus coinStatus;

        typeNum = GetTypeNum( amount );

        x =  coinsType[typeNum][3];
        y =  coinsType[typeNum][4];
        if( position == 1 ){
            y -= wTotY;
        }
        Log.w( "EEEEEEEEEE", "aaaa y = " + y );
        for( int i = 0; i < num; i++ ) {
            coinStatus = new CoinStatus(amount,x,y,position);
            coinStatuses.add(coinStatus);
            x+=COIN_X_SHIFT;
        }
    }

    public static Integer GetTypeNum( Integer amount ){
        int typeNum = 0;

        for( int i = 0; i < coinsType.length; i++ ){
            if( coinsType[i][0] == amount ){
                typeNum = i;
                break;
            }
        }
        return typeNum;
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
                if( mode == 0 && coinStatuses.get(i).position == CoinStatus.WALLET_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).position == CoinStatus.TRAY_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
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
                if( mode == 0 && coinStatuses.get(i).position == CoinStatus.WALLET_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).position == CoinStatus.TRAY_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
            }
        }
        return null;
    }

    public CoinStatus GetCoinStatus( int viewId ){

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if( coinStatuses.get(i).sViewId == viewId ) return coinStatuses.get(i);
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
                Log.w("DEBUG_DATA1", "UP");
                // タッチしたコインを取得

                //CoinStatus touchCs = GetStatus(v.getId());
                CoinStatus touchCs = GetCoinStatus(v.getId());
                if( touchCs == null ) return false;
                        Log.w( "AAAAA", "aaa1");

                if( touchCs.moveFlg ) return false;
                Log.w( "AAAAA", "aaa2");
/////                if( touchCs.CheckMoveOk() == false ) return false;
                Log.w( "AAAAA", "aaa3");
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
                if (touchCs.position == CoinStatus.WALLET_POSITION ) {

   //                 if( touchCs.sY  500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetTopStatus(touchCs.sAmount, 0);
                    if( topCs == null) return false;
                    Log.w("DEBUG_DATAxx1", "topCs.moveFlg = " + topCs.moveFlg);
                    Log.w("DEBUG_DATAxx1", "topCs.sViewId = " + topCs.sViewId);
                    Log.w("DEBUG_DATA2", "topCs.imageId = " + topCs.sImageId);
                    Log.w("DEBUG_DATA2", "topCs.x = " + topCs.sX);
                    Log.w("DEBUG_DATA2", "topCs.y = " + topCs.sY);
                    Log.w("DEBUG_DATA2", "topCs.sAmount = " + topCs.sAmount);
                    topCs.MoveCoin(0,-1,0);
                }
                // トレーのコインをタッチしてたら
                else {
                    Log.w("DEBUG_DATA", "trayyyyyyyyyyyy ");


  //                  if( touchCs.sY > 500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetTopStatus(touchCs.sAmount, 1);
                    if( topCs == null) return false;


                    Log.w("DEBUG_DATAxx1", "topCs.sViewId = " + topCs.sViewId);
                    Log.w("DEBUG_DATA3", "topCs.imageId = " + topCs.sImageId);
                    Log.w("DEBUG_DATA3", "topCs.x = " + topCs.sX);
                    Log.w("DEBUG_DATA3", "topCs.y = " + topCs.sY);
                    Log.w("DEBUG_DATA3", "topCs.sAmount = " + topCs.sAmount);
                    topCs.MoveCoin(1,-1,0);
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

    // 表示されている全コイン数を取得
    public static HashMap<String,Integer> GetAllNum(){
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).sAmount == 1) ichien++;
            else if (coinStatuses.get(i).sAmount == 5) goen++;
            else if (coinStatuses.get(i).sAmount == 10) jyuen++;
            else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
            else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
            else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
            else if (coinStatuses.get(i).sAmount == 1000) senen++;
        }

        HashMap<String,Integer> coinNum = new HashMap<String,Integer>();
        coinNum.put("1",ichien);
        coinNum.put("5",goen);
        coinNum.put("10",jyuen);
        coinNum.put("50",gojyuen);
        coinNum.put("100",hyakuen);
        coinNum.put("500",gohyaku);
        coinNum.put("1000",senen);

        Log.w( "KEKKA", "ichien = [" + ichien + "]");
        Log.w( "KEKKA", "goen = [" + goen + "]");
        Log.w( "KEKKA", "jyuen = [" + jyuen + "]");
        Log.w( "KEKKA", "gojyuen = [" + gojyuen + "]");
        Log.w( "KEKKA", "hyakuen = [" + hyakuen + "]");
        Log.w( "KEKKA", "gohyaku = [" + gohyaku + "]");
        Log.w( "KEKKA", "senen = [" + senen + "]");

        return coinNum;
    }

    // 財布内のコイン数を取得
    public static HashMap<String,Integer> GetWalletNum(){
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).position == CoinStatus.WALLET_POSITION ) {
                if (coinStatuses.get(i).sAmount == 1) ichien++;
                else if (coinStatuses.get(i).sAmount == 5) goen++;
                else if (coinStatuses.get(i).sAmount == 10) jyuen++;
                else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
                else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
                else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
                else if (coinStatuses.get(i).sAmount == 1000) senen++;
            }
        }

        HashMap<String,Integer> coinNum = new HashMap<String,Integer>();
        coinNum.put("1",ichien);
        coinNum.put("5",goen);
        coinNum.put("10",jyuen);
        coinNum.put("50",gojyuen);
        coinNum.put("100",hyakuen);
        coinNum.put("500",gohyaku);
        coinNum.put("1000",senen);

        Log.w( "KEKKA", "ichien = [" + ichien + "]");
        Log.w( "KEKKA", "goen = [" + goen + "]");
        Log.w( "KEKKA", "jyuen = [" + jyuen + "]");
        Log.w( "KEKKA", "gojyuen = [" + gojyuen + "]");
        Log.w( "KEKKA", "hyakuen = [" + hyakuen + "]");
        Log.w( "KEKKA", "gohyaku = [" + gohyaku + "]");
        Log.w( "KEKKA", "senen = [" + senen + "]");

        return coinNum;
    }

    // トレー内のコイン数を取得
    public static HashMap<String,Integer> GetTrayNum(){
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).position == CoinStatus.TRAY_POSITION) {
                if (coinStatuses.get(i).sAmount == 1) ichien++;
                else if (coinStatuses.get(i).sAmount == 5) goen++;
                else if (coinStatuses.get(i).sAmount == 10) jyuen++;
                else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
                else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
                else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
                else if (coinStatuses.get(i).sAmount == 1000) senen++;
            }
        }

        HashMap<String,Integer> coinNum = new HashMap<String,Integer>();
        coinNum.put("1",ichien);
        coinNum.put("5",goen);
        coinNum.put("10",jyuen);
        coinNum.put("50",gojyuen);
        coinNum.put("100",hyakuen);
        coinNum.put("500",gohyaku);
        coinNum.put("1000",senen);

        Log.w( "KEKKA", "ichien = [" + ichien + "]");
        Log.w( "KEKKA", "goen = [" + goen + "]");
        Log.w( "KEKKA", "jyuen = [" + jyuen + "]");
        Log.w( "KEKKA", "gojyuen = [" + gojyuen + "]");
        Log.w( "KEKKA", "hyakuen = [" + hyakuen + "]");
        Log.w( "KEKKA", "gohyaku = [" + gohyaku + "]");
        Log.w( "KEKKA", "senen = [" + senen + "]");

        return coinNum;
    }

    // トレイに乗っているコインを全部削除
    public static void OutSideCoin(){
        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).position == CoinStatus.TRAY_POSITION) {
                // 画面外に移動
                coinStatuses.get(i).MoveCoin(2,i,0);
            }
        }
    }

    // 指定した金額分コイン追加
    // amount:追加したい金額
    public static void AddCoin(int amount){

        int num;
        int x;
        CoinStatus coinStatus;

        num = amount / 1000;
        x = COIN_X_PS3;
        Log.w( "XXXXX", "amount " + amount);
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(1000, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS2);
            Log.w( "XXXXX", "aaa1000");
        }
        amount -= num * 1000;

        num = amount / 500;
        x = COIN_X_PS2;
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(500, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS2);
            Log.w( "XXXXX", "aaa500");
        }
        amount -= num * 500;


        num = amount / 100;
        x = COIN_X_PS1;
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(100, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS2);
            Log.w( "XXXXX", "aaa100");
        }
        amount -= num * 100;

        num = amount / 50;
        x = COIN_X_PS4;
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(50, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS1);
            Log.w( "XXXXX", "aaa50");
        }
        amount -= num * 50;

        num = amount / 10;
        x = COIN_X_PS3;
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(10, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS1);
            Log.w( "XXXXX", "aaa10");
        }
        amount -= num * 10;

        num = amount / 5;
        x = COIN_X_PS2;
        for(int i = 0; i < num; i++ ){
//            coinMng = new CoinStatus(5, x, 0);
//            coinStatuses.add(coinMng);
            x+=COIN_X_SHIFT;
//            coinMng.MoveCoin(3,-1,COIN_Y_PS1);
            Log.w( "XXXXX", "aaa5");
        }
        amount -= num * 5;

        num = amount / 1;
        x = COIN_X_PS1;


        for(int i = 0; i < num; i++ ){
//            coinStatus = new CoinStatus(1, x, 0);
//            coinStatuses.add(coinStatus);
            x+=COIN_X_SHIFT;
//            coinStatus.MoveCoin(3,-1,COIN_Y_PS1);
            Log.w( "XXXXX", "aaa1");
        }
        amount -= num * 1;
    }
}
