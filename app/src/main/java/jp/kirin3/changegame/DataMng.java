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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by shinji on 2017/09/12.
 */
public class DataMng{

	static Context mContext;

	final String sUserName = "UserName";
	final String sClearStar[] = { "","ClearStar1","ClearStar2","ClearStar3","ClearStar4","ClearStar5" };
	final String sClearGameTime[] = { "","VestGameTime1","VestGameTime2","VestGameTime3","VestGameTime4","VestGameTime5" };

	static SharedPreferences sSharedData;

    public DataMng(Context context) {
		mContext = context;
		sSharedData = context.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
    }

	public String ReadUserName() {

		String userName = sSharedData.getString(sUserName, "");

		return userName;
	}

	public void WriteUserName( String userName ) {

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putString(userName, "");
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

		Log.w( "AAAAA", "starNum " + starNum);
		Log.w( "AAAAA", "time " + time);

		float vestGameTime = ReadVestTime(starNum);

		Log.w( "AAAAA", "vestGameTime " + vestGameTime);

		// 記録更新
		if( vestGameTime == 0.0 || time < vestGameTime ){
			WriteVestTime( starNum,time );
			return true;
		}
		return false;
	}

}
