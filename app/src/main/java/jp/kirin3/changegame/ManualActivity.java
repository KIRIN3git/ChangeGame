package jp.kirin3.changegame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by etisu on 2017/11/12.
 */

public class ManualActivity extends AppCompatActivity {


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
    }

}
