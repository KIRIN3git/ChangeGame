package com.example.shinji.changegame;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by etisu on 2017/09/17.
 */

public class CoinStatus implements Animator.AnimatorListener {

    int sViewId;
    int sImageId;
    int sX,sY;
    int sAmount;
//    boolean walletFlg = true;
    static final int WALLET_POSITION = 0;
    static final int TRAY_POSITION = 1;
    static final int OUTSIDE_POSITION = 2;

    int position = 0; // 0:財布,1:トレー,2:外
    boolean moveFlg = false;
    int moveMode = 0;

    /* コインイメージの作成
    amount:金額
    ratioX:x座標画面表示割合（最大100）
    ratioY:y座標画面表示割合（最大100）
     */
    public CoinStatus(int amount, int ratioX, int ratioY,int _position ) {
        int image = 0;
        int newId = View.generateViewId();

        float xy[] = CommonMng.PsToPx(ratioX,ratioY);

        sViewId = newId;
        sX = (int)xy[0];
        sY = (int)xy[1];
        sAmount = amount;
        position = _position;
        moveFlg = false;

        image = CoinMng.GetCoinImage( amount );

        Log.w( "DEBUG", "aaa x[] " + xy[0]);
        Log.w( "DEBUG", "aaa y[] " + xy[1]);

        // ImageViewオブジェクトの作成
        ImageView imageView = new ImageView(CoinMng.mContext);

        // 画像の設定
        imageView.setImageResource(image);

        // タッチリスナーの設定
        imageView.setOnTouchListener(CoinMng.mVOL);

        // 動的に被らないIDを割り振り（API V17から）
        imageView.setId(newId);

        // 画像のサイズの設定
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(CoinMng.GetCoinSize(sAmount),CoinMng.GetCoinSize(sAmount));

        // 表示座標の設定
        lp.leftMargin = (int)xy[0];
        lp.topMargin = (int)xy[1];

        imageView.layout(sX + 50,sY + 20,sX + 200,sY + CoinMng.GetCoinSize(sAmount));

        // 画像の追加
        CoinMng.mLayout.addView(imageView,lp);
    }

    // 画像を削除
    void removeCoin( ){
        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);
        imageView.setImageDrawable(null);
    }

/*
mode:0 財布 → トレー
mode:1 トレー → 財布
mode:2 トレー → 外
    コインの削除処理も行う
mode:3 外 → 財布

i:CoinMngから削除するID
y:移動量(%)を指定
 */
    void MoveCoinOld( final int mode,final int i,int y ){
        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);
        final float xy[];
        final int moveY,x;

        if( y == 0) {
            xy = CommonMng.PsToPx(0, CoinMng.wTotY);
        }
        else{
            xy = CommonMng.PsToPx(0, y);
        }

        moveFlg = true;

        if( mode == 0 ){
            position = 1;
        }
        else if( mode == 1 || mode == 3 ){
            position = 0;
        }
        else if( mode == 2 ){
            position = 2;
        }
        else{
            return;
        }

        if( mode == 0){
            moveY = -(int)xy[1];
        }
        else if( mode == 1){
            moveY = (int)xy[1];
        }
        else if( mode == 2 ){
            moveY = - ( (int)xy[1] * 2 ) ;
        }
        else if( mode == 3 ){
            moveY = ( (int)xy[1] ) ;
        }
        else return;

        sY += moveY;

        int yyy = sY + moveY;
        Log.w( "DEBUG_DATAx", "mode " + mode );
        Log.w( "DEBUG_DATAx", "moveY " + moveY );
        Log.w( "DEBUG_DATAx", "sX " + sX );
        Log.w( "DEBUG_DATAx", "sY + moveY " + yyy );

        // layoutによる移動はTouchイベントが終わらないと描画されないといけないので
        // アニメーションに変更
        TranslateAnimation translate = new TranslateAnimation(0,0,0,0 + moveY);
        translate.setDuration(200);
        translate.setFillAfter(true);

//        imageView.layout(sX, sY, sX + 100, sY + 100);


        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.layout(sX, sY, sX + CoinMng.GetCoinSize(sAmount), sY + CoinMng.GetCoinSize(sAmount));
                imageView.setAnimation(null); //これをしないとアニメーション完了後にチラつく
                Log.w( "DEBUG_DATAxx1", "onAnimationEnddddddddddddddddddddd1");

                moveFlg = false;
                if(mode == 2){
                    //☆CoinMng.coinStatuses.remove(i);
                }
                Log.w( "DEBUG_DATAxx1", "onAnimationEnddddddddddddddddddddd2");
            }
        });
        imageView.startAnimation(translate);
    }

    /*
    mode:0 財布 → トレー
    mode:1 トレー → 財布
    mode:2 トレー → 外
        コインの削除処理も行う
    mode:3 外 → 財布

    i:CoinMngから削除するID
    y:移動量(%)を指定
     */
    void MoveCoin( final int mode,final int i,int y ){

        final float xy[];
        final int moveY,x;
        moveMode = mode;

        //CoinMng.CheckCoinsNum(10);
        //Log.w( "DEBUG_DATA", "count check1111111111");
        if( moveFlg ) return;

        if( y == 0) {
            xy = CommonMng.PsToPx(0, CoinMng.wTotY);
        }
        else{
            xy = CommonMng.PsToPx(0, y);
        }

        moveFlg = true;

        if( mode == 0 ){
            position = 1;
        }
        else if( mode == 1 || mode == 3 ){
            position = 0;
        }
        else if( mode == 2 ){
            position = 2;
        }
        else{
            return;
        }

        if( mode == 0){
            moveY = -(int)xy[1];
        }
        else if( mode == 1){
            moveY = (int)xy[1];
        }
        else if( mode == 2 ){
            moveY = - ( (int)xy[1] * 2 ) ;
        }
        else if( mode == 3 ){
            moveY = ( (int)xy[1] ) ;
        }
        else return;

        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);

        ObjectAnimator objectAnimator;

        // PropertyValuesHolderを使ってＸ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhX = PropertyValuesHolder.ofFloat( "translationX", 0.0f, 0.0f );
        // PropertyValuesHolderを使ってＹ軸方向移動範囲のpropertyを保持
        PropertyValuesHolder vhY = PropertyValuesHolder.ofFloat( "translationY", 0.0f, moveY );
        // PropertyValuesHolderを使って回転範囲のpropertyを保持
        PropertyValuesHolder vhRotaion = PropertyValuesHolder.ofFloat( "rotation", 0.0f, 0.0f );

        // ObjectAnimatorにセットする
        objectAnimator = ObjectAnimator.ofPropertyValuesHolder(imageView, vhX  ,vhY , vhRotaion );

        // 再生時間を設定 200msec
        objectAnimator.setDuration(200);

        // リスナーの追加
        objectAnimator.addListener(this);

        // アニメーションを開始する
        objectAnimator.start();
    }

    // アニメーション開始で呼ばれる
    @Override
    public void onAnimationStart(Animator animation) {
        Log.d("debug","onAnimationStart()");
    }

    // アニメーションがキャンセルされると呼ばれる
    @Override
    public void onAnimationCancel(Animator animation) {
        Log.d("debug","onAnimationCancel()");
    }

    // アニメーション終了時
    @Override
    public void onAnimationEnd(Animator animation) {
        Log.d("debug","onAnimationEnd()");
        moveFlg = false;
        //CoinMng.CheckCoinsNum(10);
        if( moveMode == 0) CoinMng.CleaningCoins(sAmount,TRAY_POSITION);
        else if ( moveMode == 1) CoinMng.CleaningCoins(sAmount,WALLET_POSITION);
        else if ( moveMode == 2){
            CoinMng.DeleteCoins(sAmount,OUTSIDE_POSITION);

        }
    }

    // 繰り返しでコールバックされる
    @Override
    public void onAnimationRepeat(Animator animation) {
        Log.d("debug","onAnimationRepeat()");
    }

/*
    boolean CheckMoveOk(){
        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);
        int y = imageView.getTop();

        final float xy[] = CommonMng.PsToPx( 0,60 );

        Log.w( "DEBUG_DATAxx2aa", "xy[1] " + xy[1]);

        if( walletFlg == true && xy[1] < y ) return true;
        else if( walletFlg == false && xy[1] > y) return true;

    return false;
    }
*/
}
