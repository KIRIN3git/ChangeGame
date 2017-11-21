package jp.kirin3.changegame;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;

import java.lang.reflect.Method;

/**
 * Created by shinji on 2017/06/15.
 */

public class CommonMng{

	static String printText = "";
	static float printX,printY;
	static float mirrorX,mirrorY;

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

		Log.w( "DEBUG_DATA", "real.x " + real.x);
		Log.w( "DEBUG_DATA", "real.y " + real.y);
		Log.w( "DEBUG_DATA", "psx " + psx);
		Log.w( "DEBUG_DATA", "psy " + psy);

        ret[0] = real.x * (psx / 100.0f);
        ret[1] = real.y * (psy / 100.0f);

		return ret;
	}

}