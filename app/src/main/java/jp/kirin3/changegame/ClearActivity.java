package jp.kirin3.changegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by etisu on 2017/11/12.
 */

public class ClearActivity extends AppCompatActivity {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    static int sStarNum;
    static float sGameTime;
    static float sVestGameTime;
    static boolean sVestGameTimeFlg;
    static DataMng sDataMng;

    TextView sTextStar;
    TextView sTextVestGameTimeMsg;
    TextView sTextVestGameTime;
    TextView sTextGameTime;
    Button sButtonTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clear);

		sDataMng = new DataMng(this );

        sTextStar = (TextView)findViewById(R.id.textStar);
        sTextVestGameTimeMsg = (TextView)findViewById(R.id.textVestGameTimeMsg);
        sTextVestGameTime = (TextView)findViewById(R.id.textVestGameTime);
        sTextGameTime = (TextView)findViewById(R.id.textGameTime);
        sButtonTop = (Button)findViewById(R.id.buttonTop);

        Bundle extras = getIntent().getExtras();
        sStarNum = extras.getInt("STAR");
        sGameTime = extras.getFloat("GAME_TIME");

		// クリア難易度を保存
		sDataMng.WriteStar(sStarNum);
		// ベストタイムの更新
		sVestGameTimeFlg = sDataMng.UpdateVestTime(sStarNum,sGameTime);
		// ベストタイムの取得
		sVestGameTime = sDataMng.ReadVestTime(sStarNum);

		// ・テキスト設定
        //難易度
        String strStar = "";
        for( int i = 0; i < sStarNum; i++ ){
            strStar += "★";
        }
        sTextStar.setText(strStar);

        sTextVestGameTime.setText(String.valueOf(sVestGameTime));
        sTextGameTime.setText(String.valueOf(sGameTime));

        // ベストタイム更新時
        if( sVestGameTimeFlg ){
            // ベストタイムメッセージ表示
        	sTextVestGameTimeMsg.setVisibility(View.VISIBLE);

            // ----------- ベストタイムをFBに記録 -----------
			DataMng sDataMng = new DataMng(this );

			// ユーザID取得
			String userId = sDataMng.ReadUserId();
			// 無い場合は作成
			if( userId == null ) userId = sDataMng.CreateUserId();

			// ユーザー名
			String userName = sDataMng.ReadUserName();
			if( userName == null ) userName = userId; // 名前がない場合はUserId
			float time = sDataMng.ReadVestTime(sStarNum);
			String date = null;

			sDataMng.SaveFbStarRecode(sStarNum,userId,userName,time,date );
			// -----------------------------------------------
        }

        // ・ボタン設定
        sButtonTop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(ClearActivity.this, MainActivity.class);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });

        // Firebase
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setAnalytics();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setAnalytics(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "CLEAR");
        bundle.putString("clear_star", String.valueOf(sStarNum));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
