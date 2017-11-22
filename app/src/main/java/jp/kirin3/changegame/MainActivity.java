package jp.kirin3.changegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;


import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;

    TextView textGameStart1;
    TextView textGameStart2;
    TextView textGameStart3;
    TextView textGameStart4;
    TextView textGameStart5;

    TextView textGameVestTime1;
    TextView textGameVestTime2;
    TextView textGameVestTime3;
    TextView textGameVestTime4;
    TextView textGameVestTime5;

    TextView textGameVestTimeSec1;
    TextView textGameVestTimeSec2;
    TextView textGameVestTimeSec3;
    TextView textGameVestTimeSec4;
    TextView textGameVestTimeSec5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CommonMng.real = getRealSize();

//        Log.w("DEBUG_DATA", "getRealSize" + CommonMng.real.x + " " + CommonMng.real.y);

        textGameStart1 = (TextView) findViewById(R.id.textGameStart1);
        textGameStart2 = (TextView) findViewById(R.id.textGameStart2);
        textGameStart3 = (TextView) findViewById(R.id.textGameStart3);
        textGameStart4 = (TextView) findViewById(R.id.textGameStart4);
        textGameStart5 = (TextView) findViewById(R.id.textGameStart5);

        textGameVestTime1 = (TextView) findViewById(R.id.textGameVestTime1);
        textGameVestTime2 = (TextView) findViewById(R.id.textGameVestTime2);
        textGameVestTime3 = (TextView) findViewById(R.id.textGameVestTime3);
        textGameVestTime4 = (TextView) findViewById(R.id.textGameVestTime4);
        textGameVestTime5 = (TextView) findViewById(R.id.textGameVestTime5);

        textGameVestTimeSec1 = (TextView) findViewById(R.id.textGameVestTimeSec1);
        textGameVestTimeSec2 = (TextView) findViewById(R.id.textGameVestTimeSec2);
        textGameVestTimeSec3 = (TextView) findViewById(R.id.textGameVestTimeSec3);
        textGameVestTimeSec4 = (TextView) findViewById(R.id.textGameVestTimeSec4);
        textGameVestTimeSec5 = (TextView) findViewById(R.id.textGameVestTimeSec5);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        MobileAds.initialize(this,getResources().getString(R.string.admob_app_id) );

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences data = getSharedPreferences("DataSave", Context.MODE_PRIVATE);

        int clearStart1 = data.getInt("ClearStar1",0 );
        if( clearStart1 == 1 ){
            textGameStart1.setTextColor(getResources().getColor(R.color.orenge));
            float vestGameTime1 = data.getFloat("VestGameTime1",0);

            textGameVestTime1.setText(String.valueOf(vestGameTime1));
            textGameVestTime1.setVisibility(View.VISIBLE);
            textGameVestTimeSec1.setVisibility(View.VISIBLE);
        }
        textGameStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("STAR", 1);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });

        int clearStart2 = data.getInt("ClearStar2",0 );
        if( clearStart2 == 1 ){
            textGameStart2.setTextColor(getResources().getColor(R.color.orenge));
            float vestGameTime2 = data.getFloat("VestGameTime2",0);
            textGameVestTime2.setText(String.valueOf(vestGameTime2));
            textGameVestTime2.setVisibility(View.VISIBLE);
            textGameVestTimeSec2.setVisibility(View.VISIBLE);
        }
        textGameStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("STAR", 2);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });

        int clearStart3 = data.getInt("ClearStar3",0 );
        if( clearStart3 == 1 ){
            textGameStart3.setTextColor(getResources().getColor(R.color.orenge));
            float vestGameTime3 = data.getFloat("VestGameTime3",0);
            textGameVestTime3.setText(String.valueOf(vestGameTime3));
            textGameVestTime3.setVisibility(View.VISIBLE);
            textGameVestTimeSec3.setVisibility(View.VISIBLE);
        }
        textGameStart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("STAR", 3);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });

        int clearStart4 = data.getInt("ClearStar4",0 );
        if( clearStart4 == 1 ){
            textGameStart4.setTextColor(getResources().getColor(R.color.orenge));
            float vestGameTime4 = data.getFloat("VestGameTime4",0);
            textGameVestTime4.setText(String.valueOf(vestGameTime4));
            textGameVestTime4.setVisibility(View.VISIBLE);
            textGameVestTimeSec4.setVisibility(View.VISIBLE);
        }
        textGameStart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("STAR", 4);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });

        int clearStart5 = data.getInt("ClearStar5",0 );
        if( clearStart5 == 1 ){
            textGameStart5.setTextColor(getResources().getColor(R.color.orenge));
            float vestGameTime5 = data.getFloat("VestGameTime5",0);
            textGameVestTime5.setText(String.valueOf(vestGameTime5));
            textGameVestTime5.setVisibility(View.VISIBLE);
            textGameVestTimeSec5.setVisibility(View.VISIBLE);
        }
        textGameStart5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("STAR", 5);
                // ゲーム画面の起動
                startActivity(intent);
            }
        });
    }
    // 端末のサイズを取得(Pointクラス px)
    private Point getRealSize() {

        Display display = getWindowManager().getDefaultDisplay();
        Point real = new Point(0, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Android 4.2以上
            display.getRealSize(real);
            return real;

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Android 3.2以上
            try {
                Method getRawWidth = Display.class.getMethod("getRawWidth");
                Method getRawHeight = Display.class.getMethod("getRawHeight");
                int width = (Integer) getRawWidth.invoke(display);
                int height = (Integer) getRawHeight.invoke(display);
                real.set(width, height);
                return real;

            } catch (Exception e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }
        }

        return real;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        // Viewのサイズを取得
        int x,y;

        RelativeLayout fl = (RelativeLayout) findViewById(R.id.activity_main);
        x = fl.getWidth();
        y = fl.getHeight();
        Point real = new Point(x, y);

        CommonMng.real = real;
    }

}
