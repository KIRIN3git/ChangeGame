package jp.kirin3.changegame;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by etisu on 2017/11/12.
 */
public class RankingActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;

    // TabHost
    private TabHost mTabHost;
    private String mLastTabId;

	static DataMng sDataMng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		sDataMng = new DataMng(this );

        //sDataMng.SaveAllTerminal();

        setContentView(R.layout.activity_ranking);

        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        /* Tab1 設定 */
        TabHost.TabSpec tab1 = mTabHost.newTabSpec("tab1");
        tab1.setIndicator("★１");
        tab1.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab1);

        // Tab2 設定
        TabHost.TabSpec tab2 = mTabHost.newTabSpec("tab2");
        tab2.setIndicator("★２");
        tab2.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab2);

        // Tab3 設定
        TabHost.TabSpec tab3 = mTabHost.newTabSpec("tab3");
        tab3.setIndicator("★３");
        tab3.setContent(new DummyTabFactory(this));
        mTabHost.addTab(tab3);

		// Tab4 設定
		TabHost.TabSpec tab4 = mTabHost.newTabSpec("tab4");
		tab4.setIndicator("★４");
		tab4.setContent(new DummyTabFactory(this));
		mTabHost.addTab(tab4);

		// Tab5 設定
		TabHost.TabSpec tab5 = mTabHost.newTabSpec("tab5");
		tab5.setIndicator("★５");
		tab5.setContent(new DummyTabFactory(this));
		mTabHost.addTab(tab5);

        // タブ変更時イベントハンドラ
        mTabHost.setOnTabChangedListener(this);

        // 初期タブ選択
        onTabChanged("tab1");

        //////////////// Firebase ////////////////
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setAnalytics();
    }

    /*
	 * タブの選択が変わったときに呼び出される
	 * @Override
	 */
    public void onTabChanged(String tabId) {

        if(mLastTabId != tabId){

			TabFragment fragment = new TabFragment();
			Bundle bundle = new Bundle();

            FragmentTransaction fragmentTransaction
                    = getSupportFragmentManager().beginTransaction();
            if("tab1" == tabId){
				bundle.putInt("STAR_NUM", 1);
				fragment.setArguments(bundle);
                fragmentTransaction
                        .replace(R.id.realtabcontent, fragment);
            }else if("tab2" == tabId){
				bundle.putInt("STAR_NUM", 2);
				fragment.setArguments(bundle);
                fragmentTransaction
                        .replace(R.id.realtabcontent, fragment);
            }else if("tab3" == tabId){
				bundle.putInt("STAR_NUM", 3);
				fragment.setArguments(bundle);
                fragmentTransaction
                        .replace(R.id.realtabcontent, fragment);
			}else if("tab4" == tabId){
				bundle.putInt("STAR_NUM", 4);
				fragment.setArguments(bundle);
				fragmentTransaction
						.replace(R.id.realtabcontent, fragment);
			}else if("tab5" == tabId){
				bundle.putInt("STAR_NUM", 5);
				fragment.setArguments(bundle);
				fragmentTransaction
						.replace(R.id.realtabcontent, fragment);
			}
            mLastTabId = tabId;

            fragmentTransaction.commit();
        }
    }

    /*
	 * android:id/tabcontent のダミーコンテンツ
	 */
    private static class DummyTabFactory implements TabHost.TabContentFactory {

        /* Context */
        private final Context mContext;

        DummyTabFactory(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            return v;
        }
    }

    private void setAnalytics(){
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "5");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "RANKING");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}
