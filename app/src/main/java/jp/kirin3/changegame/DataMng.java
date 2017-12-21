package jp.kirin3.changegame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
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
public class DataMng extends AppCompatActivity {

	static Context mContext;
	static float sGameTime;
	static float sVestGameTime;

	final String sClearStar[] = { "","ClearStar1","ClearStar2","ClearStar3","ClearStar4","ClearStar5" };
	final String sClearGameTime[] = { "","VestGameTime1","VestGameTime2","VestGameTime3","VestGameTime4","VestGameTime5" };

	static SharedPreferences sSharedData;

    public DataMng(Context context) {
		mContext = context;

		sSharedData = getSharedPreferences("DataSave", Context.MODE_PRIVATE);
    }


	public Float ReadVestTime( int starNum) {
		if (starNum < 1 || starNum > sClearStar.length ) return 0.0F;

		float vestTime;
		vestTime = sSharedData.getFloat(sClearGameTime[starNum], 0);

		return vestTime;
	}

	public Integer WriteVestTime( int starNum,Double vestTime ) {
		if (starNum < 1 || starNum > sClearStar.length ) return 1;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putFloat(sClearGameTime[starNum], sGameTime);
		editor.apply();

		return 0;
	}

	// 0:未クリア,1:クリア済み
	public Integer ReadStar( int starNum) {
		if (starNum < 1 || starNum > sClearStar.length ) return 0;

		int okFlg;
		okFlg = sSharedData.getInt(sClearGameTime[starNum], 0);

		return okFlg;
	}

	// 0:成功,1:失敗
	public Integer WriteStar( int starNum ) {
		if (starNum < 1 || starNum > sClearStar.length ) return 1;

		SharedPreferences.Editor editor = sSharedData.edit();
		editor.putInt(sClearStar[starNum], 1);
		editor.apply();

		return 0;
	}

	public Integer UpdateVestTime( int starNum,float time ){


    	return 0;
	}

}
