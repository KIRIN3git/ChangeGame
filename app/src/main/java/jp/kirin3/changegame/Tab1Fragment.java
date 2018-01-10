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
import android.widget.ListView;

public class Tab1Fragment extends Fragment {


	static ListView userListView;
	static DataMng sDataMng;

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

		userListView = (ListView) v.findViewById(R.id.userListView);
		sDataMng = new DataMng(getContext(),userListView );

		return v;
	}

	static public void setAdapter(){

	//	DataMng.UserAdapter adapter = new DataMng.UserAdapter(this, 0, users);

	//	userListView.setAdapter(adapter);

		return;
	}
}