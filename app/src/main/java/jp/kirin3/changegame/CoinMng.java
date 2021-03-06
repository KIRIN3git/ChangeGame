package jp.kirin3.changegame;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by shinji on 2017/09/12.
 */
public class CoinMng implements OnTouchListener{

    static Context mContext;
    static FrameLayout mLayout;
    static View.OnTouchListener mVOL;


    static final int ALL_POSITION = 0;
    static final int WALLET_POSITION = 1;
    static final int TRAY_POSITION = 2;
    static final int OUTSIDE_POSITION = 3;

    // コイン種類
    static String coinType[] = {"1yen","5yen","10yen","50yen","100yen","500yen","1000yen"};
    // コインの枚数
    static int coinCount1 = 0;

    // コインサイズ
    static int ICHIYEN_SIZE_DP = 60;
    static int ICHIYEN_SIZE_PX;

    static int GOYEN_SIZE_DP = 60;
    static int GOYEN_SIZE_PX;

    static int JYUYEN_SIZE_DP = 60;
    static int JYUYEN_SIZE_PX;

    static int GOJYUYEN_SIZE_DP = 60;
    static int GOJYUYEN_SIZE_PX;

    static int HYAKUYEN_SIZE_DP = 60;
    static int HYAKUYEN_SIZE_PX;

    static int GOHYAKUYEN_SIZE_DP = 63;
    static int GOHYAKUYEN_SIZE_PX;

    static int SENYEN_SIZE_DP = 70;
    static int SENYEN_SIZE_PX;

    static int GOSENYEN_SIZE_DP = 70;
    static int GOSENYEN_SIZE_PX;

    // 財布の中でのX座標位置のコインズレ（パーセント）
    static int COIN_X_SHIFT = 2;


    // 財布とトレーのy座標差（パーセント）
    static final int wTotY = 34;

    // 財布の中でのY座標位置（パーセント）
    // 上の段
    static int COIN_Y_PS1 = 68;
    // 下の段
    static int COIN_Y_PS2 = 79;

    // 財布の中でのX座標位置（パーセント）
    static int COIN_X_PS1 = 4;
    static int COIN_X_PS2 = 31;
    static int COIN_X_PS3 = 53;
    static int COIN_X_PS4 = 80;

    static int coinsType[][] ={
            {1,ICHIYEN_SIZE_DP,ICHIYEN_SIZE_PX,COIN_X_PS1,COIN_Y_PS1},
            {5,GOYEN_SIZE_DP,GOYEN_SIZE_PX,COIN_X_PS2,COIN_Y_PS1},
            {10,JYUYEN_SIZE_DP,JYUYEN_SIZE_PX,COIN_X_PS3,COIN_Y_PS1},
            {50,GOJYUYEN_SIZE_DP,GOJYUYEN_SIZE_PX,COIN_X_PS4,COIN_Y_PS1},
            {100,HYAKUYEN_SIZE_DP,HYAKUYEN_SIZE_PX,COIN_X_PS1,COIN_Y_PS2},
            {500,GOHYAKUYEN_SIZE_DP,GOHYAKUYEN_SIZE_PX,COIN_X_PS2,COIN_Y_PS2},
            {1000,SENYEN_SIZE_DP,SENYEN_SIZE_PX,COIN_X_PS3,COIN_Y_PS2},
            {5000,SENYEN_SIZE_DP,SENYEN_SIZE_PX,COIN_X_PS4,COIN_Y_PS2},
    };


    // 財布に入っているコインの数
    static HashMap<String,Integer> walletCoinNum;

    // トレーに入っているコインの数
    static HashMap<String,Integer> trayCoinNum;

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
        mVOL = this;


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

		// コインをクリア
		DeleteCoins(0,ALL_POSITION);

		// コインの初期作成
		CreateCoin(1,4,WALLET_POSITION);
		CreateCoin(5,1,WALLET_POSITION);
		CreateCoin(10,4,WALLET_POSITION);
		CreateCoin(50,1,WALLET_POSITION);
		CreateCoin(100,4,WALLET_POSITION);
		CreateCoin(500,1,WALLET_POSITION);
		CreateCoin(1000,4,WALLET_POSITION);
		CreateCoin(5000,1,WALLET_POSITION);

    }

    // お金の表示枚数を
    public static void CreateText( Integer amount,Integer position ){

		int ratioX,ratioY,x,y,typeNum;

		typeNum = GetTypeNum( amount );

		ratioX =  coinsType[typeNum][3];
		ratioY =  coinsType[typeNum][4];

		if( position == TRAY_POSITION ){
			ratioY -= wTotY;
		}

		float xy[] = CommonMng.PsToPx(ratioX,ratioY);

		x = (int)xy[0];
		y = (int)xy[1];

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(80,30);

		// 表示座標の設定
		lp.leftMargin = 150;
		lp.topMargin = 200;

		TextView textView = new TextView(CoinMng.mContext);
		textView.setText("8");
		textView.setTextSize(50);
		mLayout.addView(textView);
	}

	private static LinearLayout.LayoutParams createParam(int w, int h){
		return new LinearLayout.LayoutParams(w, h);
	}


    public static void CheckCoinsNum(Integer amount){
        Integer countWallet = 0;
        Integer countTray = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if( coinStatuses.get(i).sAmount == 10 ){
                if( coinStatuses.get(i).position == 0 ){
                    countWallet++;
                }
                else if( coinStatuses.get(i).position == 1 ){
                    countTray++;
                }
            }
        }
    }
    /*
     指定した金額の整形を行う
     option
        1:財布
        2:トレー
    */
    public static void CleaningCoins( Integer amount,Integer position ){

        Integer count = 0;
        count = DeleteCoins(amount,position);

        // 削除分作成
        CreateCoin( amount,count,position );
    }

    // amount 0:全削除 position 0:全削除
    public static int DeleteCoins( Integer amount,Integer position ){
        Integer count = 0;
        for( int i = coinStatuses.size() - 1; i >= 0; i-- ) {
            if( coinStatuses.get(i).sAmount == amount || amount == 0 ){
                if( coinStatuses.get(i).position == position || position == 0 ) {
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
            num = charge / coinsType[i][0];
			charge -= coinsType[i][0] * num;
            CreateCoin( coinsType[i][0],num,position );
        }
    }

    public static void CreateCoin( Integer amount,Integer num,Integer position ){

        int x,y,typeNum;
        CoinStatus coinStatus;

        typeNum = GetTypeNum( amount );

        x =  coinsType[typeNum][3];
        y =  coinsType[typeNum][4];
        if( position == TRAY_POSITION ){
            y -= wTotY;
        }
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
        for( int i = coinStatuses.size() - 1; i >= 0; i-- ){
            if( coinStatuses.get(i).sAmount== amount ){
                if( mode == 0 && coinStatuses.get(i).position == WALLET_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).position == TRAY_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
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
        for( int i = 0; i < coinStatuses.size(); i++ ){
            if( coinStatuses.get(i).sAmount== amount ){
                if( mode == 0 && coinStatuses.get(i).position == WALLET_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
                else if( mode == 1 && coinStatuses.get(i).position == TRAY_POSITION && coinStatuses.get(i).moveFlg == false ) return coinStatuses.get(i);
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

        // イベントの状態を調べる
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_UP:
                // タッチしたコインを取得

                //CoinStatus touchCs = GetStatus(v.getId());
                CoinStatus touchCs = GetCoinStatus(v.getId());
                if( touchCs == null ) return false;

                if( touchCs.moveFlg ) return false;

                // 財布のコインをタッチしてたら
                if (touchCs.position == WALLET_POSITION ) {

   //                 if( touchCs.sY  500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetTopStatus(touchCs.sAmount, 0);
                    if( topCs == null) return false;
                    topCs.MoveCoin(0,-1,0);
                }
                // トレーのコインをタッチしてたら
                else {
  //                  if( touchCs.sY > 500 ) return false;

                    // タッチしたコインの中の一番上のコインを取得
                    CoinStatus topCs = GetTopStatus(touchCs.sAmount, 1);
                    if( topCs == null) return false;

                    topCs.MoveCoin(1,-1,0);
                }

                break;
        }

        return true;
    }

    public boolean onTouchEvent(MotionEvent event)
    {
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
		else if( amount == 5000 ) return CoinMng.gosenenImageId;

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
		else if( amount == 5000 ) return GOSENYEN_SIZE_PX;

        return 0;
    }

    /*
     表示されているコイン数を取得
     option 0:全部,1:財布,2:トレー
    */

    public static HashMap<String,Integer> GetCoinNum( int position ){
       // WALLET_POSITION = 3;
        int ichien = 0,goen = 0,jyuen = 0,gojyuen = 0,hyakuen = 0,gohyaku = 0,senen = 0,gosenen = 0;

        for( int i = 0; i < coinStatuses.size(); i++ ) {
            if (coinStatuses.get(i).position == position || position == ALL_POSITION ) {
                if (coinStatuses.get(i).sAmount == 1) ichien++;
                else if (coinStatuses.get(i).sAmount == 5) goen++;
                else if (coinStatuses.get(i).sAmount == 10) jyuen++;
                else if (coinStatuses.get(i).sAmount == 50) gojyuen++;
                else if (coinStatuses.get(i).sAmount == 100) hyakuen++;
                else if (coinStatuses.get(i).sAmount == 500) gohyaku++;
                else if (coinStatuses.get(i).sAmount == 1000) senen++;
                else if (coinStatuses.get(i).sAmount == 5000) gosenen++;
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
        coinNum.put("5000",gosenen);

        return coinNum;
    }


    // トレイに乗っているコインを全部削除
	public static void OutSideCoin(){
		for( int i = 0; i < coinStatuses.size(); i++ ) {
			if (coinStatuses.get(i).position == TRAY_POSITION) {
				// 画面外に移動
				coinStatuses.get(i).MoveCoin(2,i,0);
			}
		}
	}

	// トレイに乗っているコインを全部財布に移動
	public static void MoveAllCoin( int position, int moveMode ){
		for( int i = 0; i < coinStatuses.size(); i++ ) {
			if (coinStatuses.get(i).position == position) {
				// 画面外に移動
				coinStatuses.get(i).MoveCoin(moveMode,i,0);
			}
		}
	}


    // 指定した金額分コイン追加
    // amount:追加したい金額
    public static void AddCoin(int amount){

        int num;
        int x;

		num = amount / 5000;
		x = COIN_X_PS3;
		for(int i = 0; i < num; i++ ){
			x+=COIN_X_SHIFT;
		}
		amount -= num * 5000;

        num = amount / 1000;
        x = COIN_X_PS3;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 1000;

        num = amount / 500;
        x = COIN_X_PS2;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 500;


        num = amount / 100;
        x = COIN_X_PS1;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 100;

        num = amount / 50;
        x = COIN_X_PS4;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 50;

        num = amount / 10;
        x = COIN_X_PS3;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 10;

        num = amount / 5;
        x = COIN_X_PS2;
        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 5;

        num = amount / 1;
        x = COIN_X_PS1;


        for(int i = 0; i < num; i++ ){
            x+=COIN_X_SHIFT;
        }
        amount -= num * 1;
    }
}
