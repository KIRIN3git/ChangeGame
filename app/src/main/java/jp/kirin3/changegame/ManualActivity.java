package jp.kirin3.changegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by etisu on 2017/11/12.
 */

public class ManualActivity extends AppCompatActivity {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    Button sButtonTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_manual);

        sButtonTop = (Button)findViewById(R.id.buttonTop);

        // ・ボタン設定
        sButtonTop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(ManualActivity.this, MainActivity.class);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });


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
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MANUAL");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
