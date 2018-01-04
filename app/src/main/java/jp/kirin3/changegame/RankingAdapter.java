package jp.kirin3.changegame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shinji on 2016/07/04.
 */
public class RankingAdapter extends ArrayAdapter<DataMng.User>{

	private ArrayList<DataMng.User> mUserItems;

	public RankingAdapter(Context context, int resource, ArrayList<DataMng.User> UserItems){
		super(context, resource, UserItems);

		this.mUserItems = UserItems;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent){

		TextView textView;

		// リストxml指定
		if( convertView == null ){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list, parent, false);
		}
		// 停留所名設定、停留所名色
		textView = (TextView) convertView.findViewById(R.id.stop_code);
		textView.setText(mUserItems.get(position).getName());

		return convertView;
	}
}
