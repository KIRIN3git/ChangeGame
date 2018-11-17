package jp.kirin3.changegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static jp.kirin3.changegame.CommonMng.GetDateString;


/**
 * Created by shinji on 2017/09/12.
 */
public class DataMng{

	static Context mContext;

	final String USER_ID = "UserId";
	final String USER_NAME = "UserName";
	final String FB_START[] = { "","star1","star2","star3","star4","star5" };
	final String CLEAR_NAME[] = { "","ClearStar1","ClearStar2","ClearStar3","ClearStar4","ClearStar5" };
	final String CLEAR_GAME_TIME[] = { "","VestGameTime1","VestGameTime2","VestGameTime3","VestGameTime4","VestGameTime5" };

	final String TARMINAL = "tarminal";

	// ランキングの最大表示数
	final int OUTPUT_RANKING_NUM = 1000;
    // 標準のユーザー名
	final String DEFAULT_USER_NAME = "名無しさん";

	// リストデータ配列
	public static ArrayList<User> Users = new ArrayList<User>();

	private

	User user;
	private int rankNo;

	static SharedPreferences sSharedData;

	DatabaseReference sRef;

	static ListView sListView;

	public DataMng(Context context) {
		mContext = context;
		sSharedData = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);

		sRef = FirebaseDatabase.getInstance().getReference();

		rankNo = 0;
    }

	public DataMng(Context context,ListView listView) {
		mContext = context;
		sSharedData = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);

		sRef = FirebaseDatabase.getInstance().getReference();

		sListView = listView;

		rankNo = 0;
	}

	public String ReadUserId() {

		String userId = sSharedData.getString(USER_ID, "");

		return userId;
	}

	public void WriteUserId( String userId ) {

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putString(USER_ID, userId);
		editor.apply();
	}

	public String CreateUserId() {
		String id = UUID.randomUUID().toString();
		WriteUserId(id);
		return id;
	}

	public String ReadUserName() {

		String userName = sSharedData.getString(USER_NAME, "");

		return userName;
	}

	public void WriteUserName( String userName ) {

		SharedPreferences.Editor editor = sSharedData.edit();
		if( userName != null ) editor.putString(USER_NAME, userName);
		else editor.putString(USER_NAME, DEFAULT_USER_NAME);
		editor.apply();
	}

	public Float ReadVestTime( int starNum) {
		if (starNum < 1 || starNum > CLEAR_NAME.length ) return 0.0F;

		float vestTime;
		vestTime = sSharedData.getFloat(CLEAR_GAME_TIME[starNum], 0);

		return vestTime;
	}

	public void WriteVestTime( int starNum,float vestTime ) {
		if (starNum < 1 || starNum > CLEAR_NAME.length ) return;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putFloat(CLEAR_GAME_TIME[starNum], vestTime);
		editor.apply();
	}

	// 0:未クリア,1:クリア済み
	public Integer ReadStar( int starNum) {
		if (starNum < 1 || starNum > CLEAR_NAME.length ) return 0;

		int ok;
		ok = sSharedData.getInt(CLEAR_NAME[starNum], 0);

		return ok;
	}

	public void WriteStar( int starNum ) {
		if (starNum < 1 || starNum > CLEAR_NAME.length ) return;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putInt(CLEAR_NAME[starNum], 1);
		editor.apply();

		return;
	}

	/*
	　ベストタイムより早くクリアしたら、更新
	  0:更新ならず,1:更新
	 */
	public boolean UpdateVestTime( int starNum,float time ){
		if (starNum < 1 || starNum > CLEAR_NAME.length ) return false;

		float vestGameTime = ReadVestTime(starNum);

		// 記録更新
		if( vestGameTime == 0.0 || time < vestGameTime ){
			WriteVestTime( starNum,time );
			return true;
		}
		return false;
	}

	public static class User {
		public int rankingNo;
		public String name;
		public Double time; //float型だと誤差がでるので、DoubleでFBに保存
		public String date;
		public String userId;

		public User() {

		}
		public User(String _name, Float _time, String _date, String _userid) {
			name = _name;
			BigDecimal bd = new BigDecimal(_time);
			bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
			time = bd.doubleValue();

			if (_date == null) {
				date = GetDateString(1);
			}
			else date = _date;

			userId = _userid;
		}

		public void setRankingNo( int _rankingNo ){
			rankingNo = _rankingNo;
		}

		public Integer getRankingNo(){
			return rankingNo;
		}
		public String getName(){
			return name;
		}
		public Double getTime(){
			return (double)time;
		}
		public String getDate(){
			return date;
		}
		public String getUserId(){
			return userId;
		}
	}

	public void SaveFbStarRecode( int starNum,String userId,String userName,float time,String date ){
		User user = new User( userName,time,date,userId );
		sRef.child(FB_START[starNum]).child(userId).setValue(user);
	}


	/**
	 * Firebaseリアルタイムデータベースから値を取得
	 * ConstractにListViewを設定している場合は、表示も行う
     *
	 * @param starNum : 難易度☆の数
	 */
	public User GetFbStarRecode( int starNum ){
		final FirebaseDatabase database = FirebaseDatabase.getInstance();

		final DatabaseReference ref = database.getReference(FB_START[starNum]);
		ref.orderByChild("time").limitToFirst(OUTPUT_RANKING_NUM + 1).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				User user = dataSnapshot.getValue(User.class);
				rankNo++;
				user.setRankingNo(rankNo);
				/*
                Log.w( "DEBUG_DATA", "rankNo " + rankNo);
				Log.w( "DEBUG_DATA", "prevChildKey " + prevChildKey );
				Log.w( "DEBUG_DATA", "dataSnapshot.getKey " + dataSnapshot.getKey() );
				Log.w( "DEBUG_DATA", "user.name " + user.name);
				Log.w( "DEBUG_DATA", "user.time " + user.time);
				Log.w( "DEBUG_DATA", "user.date " + user.date);
                Log.w( "DEBUG_DATA", "user.userid " + user.userId);
				*/

                // 終端データを受け取ったら、アダプターに送信
				if( dataSnapshot.getKey().equals(TARMINAL) || rankNo == OUTPUT_RANKING_NUM + 1 ){
					UserAdapter adapter = new UserAdapter(mContext, 0, Users,ReadUserId());
					sListView.setAdapter(adapter);
//					Tab1Fragment.setAdapter();
				}
				else {
					Users.add(user);
				}
			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});

		return user;
	}

	/*
	 * FireBaseに保存してある全ての記録の名前を変更
	 * @param name : ユーザー名
	 */
	public void SaveFbAllUserName( String name ){
		String userId = ReadUserId();
		float vestTime;
		vestTime = ReadVestTime(1);
		if( vestTime > 0.1 ) SaveFbStarRecode( 1,userId,name,vestTime,null );
		vestTime = ReadVestTime(2);
		if( vestTime > 0.1 ) SaveFbStarRecode( 2,userId,name,vestTime,null );
		vestTime = ReadVestTime(3);
		if( vestTime > 0.1 ) SaveFbStarRecode( 3,userId,name,vestTime,null );
		vestTime = ReadVestTime(4);
		if( vestTime > 0.1 ) SaveFbStarRecode( 4,userId,name,vestTime,null );
		vestTime = ReadVestTime(5);
		if( vestTime > 0.1 ) SaveFbStarRecode( 5,userId,name,vestTime,null );

	}

	public void SaveAllTerminal(){
		String userId = "tarminal";
		String userName = "ターミナル";
		Float time = 99999.9F;
		String date = "1999-01-01 00:00:00";

		SaveFbStarRecode(1,userId,userName,time,date );
		SaveFbStarRecode(2,userId,userName,time,date );
		SaveFbStarRecode(3,userId,userName,time,date );
		SaveFbStarRecode(4,userId,userName,time,date );
		SaveFbStarRecode(5,userId,userName,time,date );
	}

	/**
	 * ユーザーリストアダプター
	 */
	public class UserAdapter extends ArrayAdapter<User> {

		private LayoutInflater layoutInflater;
		String sUserId;
		public UserAdapter(Context c, int id, ArrayList<User> users,String userid) {
			super(c, id, users);
			sUserId = userid;
			this.layoutInflater = (LayoutInflater) c.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE
			);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.user_list,
						parent,
						false
				);
			}

			TextView TextUserRank = (TextView) convertView.findViewById(R.id.userRank);
			ImageView ImageUserRank = (ImageView) convertView.findViewById(R.id.rankImg);

			User user = (User) getItem(position);
			// ランキング1,2,3は画像を表示
			if( user.getRankingNo() == 1 || user.getRankingNo() == 2 || user.getRankingNo() == 3 ){
				ImageUserRank.setVisibility(View.VISIBLE);
				TextUserRank.setVisibility(View.GONE);
				if( user.getRankingNo() == 1 ) ImageUserRank.setImageResource(R.drawable.rank1);
				if( user.getRankingNo() == 2 ) ImageUserRank.setImageResource(R.drawable.rank2);
				if( user.getRankingNo() == 3 ) ImageUserRank.setImageResource(R.drawable.rank3);
			}
			else{
				ImageUserRank.setVisibility(View.GONE);
				TextUserRank.setVisibility(View.VISIBLE);
				((TextView) convertView.findViewById(R.id.userRank)).setText(String.valueOf(user.getRankingNo()) + "位");
			}

			((TextView) convertView.findViewById(R.id.userName)).setText(user.getName());
			((TextView) convertView.findViewById(R.id.userTime)).setText(user.getTime().toString() + "s");

            int colorRed = mContext.getResources().getColor(R.color.pRed);
            int colorWhite = mContext.getResources().getColor(R.color.white);

			if( user.getUserId() != null && ReadUserId() != null && user.getUserId().equals(ReadUserId()) ){
                (convertView.findViewById(R.id.user_list_layout)).setBackgroundColor(colorRed);
            }
            else ( convertView.findViewById(R.id.user_list_layout)).setBackgroundColor(colorWhite);

            return convertView;
		}
	}
}
