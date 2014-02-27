package com.qwertee.utilities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qwertee.mobile.R;

public class NavDrawerAdapter extends BaseAdapter {

	private ArrayList<NavDrawItem> mData = new ArrayList<NavDrawItem>();
	private LayoutInflater mInflater;

	public NavDrawerAdapter(Context context) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int addItem(final NavDrawItem item) {
		int id = -1;
		if (mData.add(item)) {
			id = mData.size() - 1;
		}
		notifyDataSetChanged();
		return id;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public NavDrawItem getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		NavDrawItem item = mData.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			switch (item.type) {
			case CATEGORY:
				convertView = mInflater.inflate(R.layout.category, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textCategory);
				break;
			case ENTRY:
				convertView = mInflater.inflate(R.layout.element, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.textEntry);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView.setText(mData.get(position).text);
		return convertView;
	}

	public static class ViewHolder {
		TextView textView;
	}
}
