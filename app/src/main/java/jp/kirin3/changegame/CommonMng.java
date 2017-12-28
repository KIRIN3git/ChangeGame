package jp.kirin3.changegame;

import android.graphics.Point;

import java.util.Calendar;


/**
 * Created by shinji on 2017/06/15.
 */

public class CommonMng{


    public static Point real = null;


	public static float PxToDp(float px,float density){
		float dp = px * density + 0.5f;

		return dp;
	}

    public static int PxToDp2(int px,float density){
        int dp = (int)(px * density + 0.5f);

        return dp;
    }

	// XY座標のパーセントをピクセルで返す
	public static float[] PsToPx(float psx,float psy){

		float ret[] = {0.0f,0.0f};
        if( real == null ) return ret;

        ret[0] = real.x * (psx / 100.0f);
        ret[1] = real.y * (psy / 100.0f);

		return ret;
	}

	public static String GetDateString( int option ){

		String date = null;

		Calendar calendar = Calendar.getInstance();

		final int year = calendar.get(Calendar.YEAR);
		final int month = calendar.get(Calendar.MONTH);
		final int day = calendar.get(Calendar.DAY_OF_MONTH);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int second = calendar.get(Calendar.SECOND);
		if( option == 1 ) date = String.format( "%04d-%02d-%02d %02d:%02d:%02d",year,month,day,hour,minute,second );

		return date;
	}

}
