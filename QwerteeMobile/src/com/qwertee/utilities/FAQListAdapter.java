package com.qwertee.utilities;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qwertee.mobile.R;

public class FAQListAdapter extends ArrayAdapter<FAQEntry> {

	private List<FAQEntry> entries;
	private Context context;

	public FAQListAdapter(Context context, int resource, List<FAQEntry> entries) {
		super(context, resource, entries);
		this.entries = entries;
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.faq_layout, parent, false);
		TextView question = (TextView) rootView.findViewById(R.id.question);
		TextView answer = (TextView) rootView.findViewById(R.id.answer);
		question.setText(entries.get(position).title);
		answer.setText(Html.fromHtml(entries.get(position).answer));
		return rootView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

}
