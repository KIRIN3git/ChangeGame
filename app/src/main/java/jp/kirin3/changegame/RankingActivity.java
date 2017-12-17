package jp.kirin3.changegame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

/**
 * Created by etisu on 2017/11/12.
 */

public class RankingActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    private AdView mAdView;
    private FirebaseAnalytics mFirebaseAnalytics;
    Button sButtonTop;

    // TabHost
    private TabHost mTabHost;
    // Last selected tabId
    private String mLastTabId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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


        setDatabase();
        getDatabase();

        //////////////// Firebase ////////////////
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setAnalytics();
    }


    public static class Post {

        public String author;
        public String title;

        public Post() {

        }
        public Post(String _author, String _title) {
            author = _author;
            title = _title;
        }
    }

    public void setDatabase(){
		final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String id = UUID.randomUUID().toString();

//        ref.child("star1").child("user_id").setValue(id);
//        ref.child("star1").child(id).child("name").setValue("name1");
//        ref.child("star1").child(id).child("time").setValue(3.4);

        Post post = new Post( "author2","title2" );

        ref.child("posts").child("post").setValue(2);
        ref.child("posts").child("2").setValue(post);
//        ref.child("posts").child(id).child("author").setValue("name1");
//        ref.child("posts").child(id).child("title").setValue("name2");
    }

    public void getDatabase(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("posts/1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot){
                Post post = snapshot.getValue(Post.class);
                Log.w( "DEBUG_DATA", "ccc" + post.author);
                Log.w( "DEBUG_DATA", "ccc" + post.title);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w( "DEBUG_DATA", "aaa");
            }
        });
/*
        DatabaseReference ref2 = database.getReference("posts");

        ref2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Post newPost = dataSnapshot.getValue(Post.class);
                Log.w( "DEBUG_DATA", "Author: " + newPost.author);
                Log.w( "DEBUG_DATA", "Title: " + newPost.title);
                Log.w( "DEBUG_DATA", "Previous Post ID: " + prevChildKey);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
*/
    }

    /*
	 * タブの選択が変わったときに呼び出される
	 * @Override
	 */
    public void onTabChanged(String tabId) {
        Log.d("TAB_FRAGMENT_LOG","tabId:" + tabId);
        int starNum = 1;

        if(mLastTabId != tabId){

			Tab1Fragment fragment = new Tab1Fragment();
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
