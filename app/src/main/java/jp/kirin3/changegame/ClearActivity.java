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
    static SharedPreferences sSharedData;
    final String sClearStar[] = { "","ClearStar1","ClearStar2","ClearStar3","ClearStar4","ClearStar5" };
    final String sClearGameTime[] = { "","VestGameTime1","VestGameTime2","VestGameTime3","VestGameTime4","VestGameTime5" };

    TextView sTextStar;
    TextView sTextVestGameTimeMsg;
    TextView sTextVestGameTime;
    TextView sTextGameTime;
    Button sButtonTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_clear);

		sVestGameTimeFlg = false;

        sTextStar = (TextView)findViewById(R.id.textStar);
        sTextVestGameTimeMsg = (TextView)findViewById(R.id.textVestGameTimeMsg);
        sTextVestGameTime = (TextView)findViewById(R.id.textVestGameTime);
        sTextGameTime = (TextView)findViewById(R.id.textGameTime);
        sButtonTop = (Button)findViewById(R.id.buttonTop);


        sSharedData = getSharedPreferences("DataSave", Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        sStarNum = extras.getInt("STAR");
        sGameTime = extras.getFloat("GAME_TIME");

        // ベストタイムの取得と保存
        SaveVestTime();

        // クリア難易度を保存
        SaveStar();

        // ・テキスト設定
        //難易度
        String strStar = "";
        for( int i = 0; i < sStarNum; i++ ){
            strStar += "★";
        }
        sTextStar.setText(strStar);

        sTextVestGameTime.setText(String.valueOf(sVestGameTime));
        sTextGameTime.setText(String.valueOf(sGameTime));
        if( sVestGameTimeFlg ){
            sTextVestGameTimeMsg.setVisibility(View.VISIBLE);
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


        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setAnalytics();
    }

    private void setAnalytics(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "CLEAR");
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, String.valueOf(sStarNum));
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    void SaveVestTime(){
        if( sStarNum < 1 ) return;

        sVestGameTime = sSharedData.getFloat(sClearGameTime[sStarNum],0 );

        // 新記録
        if( sVestGameTime < 1 || sVestGameTime > sGameTime ){
            sVestGameTimeFlg = true;
            SharedPreferences.Editor editor = sSharedData.edit();
            editor.putFloat(sClearGameTime[sStarNum], sGameTime);
            editor.apply();
            sVestGameTime = sGameTime;
        }
    }

    void SaveStar(){
        if( sStarNum < 1 ) return;

        SharedPreferences.Editor editor = sSharedData.edit();
        editor.putInt(sClearStar[sStarNum], 1);
        editor.apply();
    }
}
