package jp.kirin3.changegame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    static final String INTENT_INPUT_URL = "INPUT_URL";
    static String url = "";
    static WebView sWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Bundle extras = getIntent().getExtras();
        url = extras.getString(INTENT_INPUT_URL);

        sWebView = (WebView) findViewById(R.id.webview);
        sWebView.setWebViewClient(new WebViewClient());
        sWebView.loadUrl(url);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 端末の戻るボタンでブラウザバック
        if(keyCode == KeyEvent.KEYCODE_BACK && sWebView.canGoBack()) {
            sWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode,  event);
    }
}