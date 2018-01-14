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

public class TabFragment extends Fragment {

	static ListView userListView;
	static DataMng sDataMng;
	static int sStarNum;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		int sn = getArguments().getInt("STAR_NUM");
		Log.i("MyFragment : ", "渡された値 = " + Integer.toString(sn));
		sStarNum = sn;
	}

	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tab1_fragment, container, false);

		userListView = (ListView) v.findViewById(R.id.userListView);
		sDataMng = new DataMng(getContext(),userListView );

		sDataMng.Users.clear();
		sDataMng.GetFbStarRecode(sStarNum );

		return v;
	}
}