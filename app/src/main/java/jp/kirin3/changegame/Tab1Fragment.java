package jp.kirin3.changegame;

/**
 * Created by shinji on 2017/12/12.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Tab1Fragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		int si = getArguments().getInt("STAR_NUM");
		Log.i("MyFragment : ", "渡された値 = " + Integer.toString(si));
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab1_fragment, container, false);
		return v;
	}



}