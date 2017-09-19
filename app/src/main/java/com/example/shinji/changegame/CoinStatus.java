package com.example.shinji.changegame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by etisu on 2017/09/17.
 */

public class CoinStatus {

    int sViewId;
    int sImageId;
    int sX,sY;
    int sAmount;
    boolean walletFlg = true;
    boolean moveFlg = false;


    /* コインイメージの作成
    amount:金額
    ratioX:x座標画面表示割合（最大100）
    ratioY:y座標画面表示割合（最大100）
     */
    public CoinStatus(int amount, int ratioX, int ratioY ) {
        int image = 0;
        int newId = View.generateViewId();

        float xy[] = CommonMng.PsToPx(ratioX,ratioY);

        sViewId = newId;
        sX = (int)xy[0];
        sY = (int)xy[1];
        sAmount = amount;

        if( amount == 1 ){
            image = CoinMng.ichienImageId;

        }
        else if( amount == 5 ) image = CoinMng.goenImageId;
        else if( amount == 10 ) image = CoinMng.jyuenImageId;
        else if( amount == 50 ) image = CoinMng.gojyuenImageId;
        else if( amount == 100 ) image = CoinMng.hyakuenImageId;
        else if( amount == 500 ) image = CoinMng.gohyakuenImageId;
        else if( amount == 1000 ) image = CoinMng.senenImageId;
        else return;

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
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(100,100);

        // 表示座標の設定
        lp.leftMargin = (int)xy[0];
        lp.topMargin = (int)xy[1];

        imageView.layout(sX + 50,sY + 20,sX + 200,sY + 100);

        // 画像の追加
        CoinMng.mLayout.addView(imageView,lp);


    }

    /*
    mode:0 財布→トレー
    mode:1 トレー→財布
     */
    void MoveCoin( final int mode ){
        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);
        final float xy[] = CommonMng.PsToPx( 0,40 );
        final int moveY,x;

        moveFlg = true;

        if( mode == 0){
            walletFlg = false;
        }
        else{
            walletFlg = true;
        }
        if( mode == 0){
            moveY = -(int)xy[1];
        }
        else{
            moveY = (int)xy[1];
        }
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
                imageView.layout(sX, sY, sX + 100, sY + 100);
                imageView.setAnimation(null); //これをしないとアニメーション完了後にチラつく
                Log.w( "DEBUG_DATAxx1", "onAnimationEnddddddddddddddddddddd1");

                moveFlg = false;

                Log.w( "DEBUG_DATAxx1", "onAnimationEnddddddddddddddddddddd2");
            }
        });
        imageView.startAnimation(translate);
    }


    boolean CheckMoveOk(){
        final ImageView imageView = (ImageView)CoinMng.mLayout.findViewById(sViewId);
        int y = imageView.getTop();

        Log.w( "DEBUG_DATAxx2", "y " + y);

        if( walletFlg == true && 600 < y ) return true;
        else if( walletFlg == false && 600 > y) return true;

    return false;
    }


}