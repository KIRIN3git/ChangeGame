package com.example.shinji.changegame;

import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CommonMng.real = getRealSize();

//        Log.w("DEBUG_DATA", "getRealSize" + CommonMng.real.x + " " + CommonMng.real.y);

        TextView text1 = (TextView) findViewById(R.id.game_start1);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // インテントのインスタンス生成
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
				intent.putExtra("STAR", 1);
				// ゲーム画面の起動
                startActivity(intent);
            }
        });

		TextView text2 = (TextView) findViewById(R.id.game_start2);
		text2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				intent.putExtra("STAR", 2);
				// ゲーム画面の起動
				startActivity(intent);
			}
		});

		TextView text3 = (TextView) findViewById(R.id.game_start3);
		text3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				intent.putExtra("STAR", 3);
				// ゲーム画面の起動
				startActivity(intent);
			}
		});

		TextView text4 = (TextView) findViewById(R.id.game_start4);
		text4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// インテントのインスタンス生成
				Intent intent = new Intent(MainActivity.this, GameActivity.class);
				intent.putExtra("STAR", 4);
				// ゲーム画面の起動
				startActivity(intent);
			}
		});

		TextView text5 = (TextView) findViewById(R.id.game_start5);
		text5.setOnClickListener(new View.OnClickListener() {
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
