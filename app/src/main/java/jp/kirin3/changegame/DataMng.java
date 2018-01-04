package jp.kirin3.changegame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import static jp.kirin3.changegame.CommonMng.GetDateString;


/**
 * Created by shinji on 2017/09/12.
 */
public class DataMng{

	static Context mContext;

	final String sUserId = "UserId";
	final String sUserName = "UserName";
	final String sFbStar[] = { "","star1","star2","star3","star4","star5" };
	final String sClearStar[] = { "","ClearStar1","ClearStar2","ClearStar3","ClearStar4","ClearStar5" };
	final String sClearGameTime[] = { "","VestGameTime1","VestGameTime2","VestGameTime3","VestGameTime4","VestGameTime5" };

	final String sTarminal = "tarminal";

	// リストデータ配列
	public static ArrayList<User> Users = new ArrayList<User>();

	private

	User user;

	static SharedPreferences sSharedData;

	DatabaseReference sRef;

    public DataMng(Context context) {
		mContext = context;
		sSharedData = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);

		sRef = FirebaseDatabase.getInstance().getReference();
    }

	public String ReadUserId() {

		String userId = sSharedData.getString(sUserId, "");

		return userId;
	}

	public void WriteUserId( String userId ) {

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putString(sUserId, userId);
		editor.apply();
	}

	public String CreateUserId() {
		String id = UUID.randomUUID().toString();
		WriteUserId(id);
		return id;
	}

	public String ReadUserName() {

		String userName = sSharedData.getString(sUserName, "");

		return userName;
	}

	public void WriteUserName( String userName ) {

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putString(sUserName, userName);
		editor.apply();
	}

	public Float ReadVestTime( int starNum) {
		if (starNum < 1 || starNum > sClearStar.length ) return 0.0F;

		float vestTime;
		vestTime = sSharedData.getFloat(sClearGameTime[starNum], 0);

		return vestTime;
	}

	public void WriteVestTime( int starNum,float vestTime ) {
		if (starNum < 1 || starNum > sClearStar.length ) return;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putFloat(sClearGameTime[starNum], vestTime);
		editor.apply();
	}

	// 0:未クリア,1:クリア済み
	public Integer ReadStar( int starNum) {
		if (starNum < 1 || starNum > sClearStar.length ) return 0;

		int ok;
		ok = sSharedData.getInt(sClearStar[starNum], 0);

		return ok;
	}

	public void WriteStar( int starNum ) {
		if (starNum < 1 || starNum > sClearStar.length ) return;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putInt(sClearStar[starNum], 1);
		editor.apply();

		return;
	}

	/*
	　ベストタイムより早くクリアしたら、更新
	  0:更新ならず,1:更新
	 */
	public boolean UpdateVestTime( int starNum,float time ){
		if (starNum < 1 || starNum > sClearStar.length ) return false;

		float vestGameTime = ReadVestTime(starNum);

		// 記録更新
		if( vestGameTime == 0.0 || time < vestGameTime ){
			WriteVestTime( starNum,time );
			return true;
		}
		return false;
	}

	public static class User {
		public String name;
		public Double time;
		public String date;


		public User() {

		}
		public User(String _name, Float _time, String _date) {
			name = _name;
			BigDecimal bd = new BigDecimal(_time);
			bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
			time = bd.doubleValue();

			if (_date == null) {
				date = GetDateString(1);
			}
			else date = _date;

		}

		public String getName(){
			return name;
		}
	}

	public void SaveFbStarRecode( int starNum,String userId,String userName,float time,String date ){
		User user = new User( userName,time,date );
		sRef.child(sFbStar[starNum]).child(userId).setValue(user);
	}

	public User GetFbStarRecode( int starNum ){
		final FirebaseDatabase database = FirebaseDatabase.getInstance();

		DatabaseReference ref = database.getReference(sFbStar[starNum]);
		ref.orderByChild("user_id").limitToFirst(10).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				User user = dataSnapshot.getValue(User.class);

				Log.w( "DEBUG_DATA", "aaaaaaaaaaaaaaaaaaaaaaaaaaaa DataSnapshot " );

				Log.w( "DEBUG_DATA", "prevChildKey " + prevChildKey );
				Log.w( "DEBUG_DATA", "dataSnapshot.getKey " + dataSnapshot.getKey() );
				Log.w( "DEBUG_DATA", "user.name " + user.name);
				Log.w( "DEBUG_DATA", "user.time " + user.time);
				Log.w( "DEBUG_DATA", "user.date " + user.date);

				Users.add( user );

				if( user.name.equals(sTarminal)){

				}
//				DataMng.this.user = user;
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Log.w( "DEBUG_DATA", "aaa onChildChanged " );
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Log.w( "DEBUG_DATA", "aaa onChildRemoved " );
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Log.w( "DEBUG_DATA", "aaa onChildMoved " );
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.w( "DEBUG_DATA", "aaa onCancelled " );
			}
		});

		return user;
	}


	public User GetFbStarRecode2( int starNum ){
		final FirebaseDatabase database = FirebaseDatabase.getInstance();

		DatabaseReference ref = database.getReference(sFbStar[starNum]);
		ref.orderByChild("user_id").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {

				Log.w( "DEBUG_DATA", "bbbbbbbbb onDataChange " );

				// Get user value
				User user = dataSnapshot.getValue(User.class);
				Log.w( "DEBUG_DATA", "user.name " + user.name);
				Log.w( "DEBUG_DATA", "user.time " + user.time);
				Log.w( "DEBUG_DATA", "user.date " + user.date);
				// ...
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.w("DEBUG_DATA", "bbb getUser:onCancelled", databaseError.toException());
			}
		});

		return user;
	}

}
