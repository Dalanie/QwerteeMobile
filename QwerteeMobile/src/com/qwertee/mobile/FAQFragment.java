package com.qwertee.mobile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.qwertee.utilities.FAQEntry;
import com.qwertee.utilities.FAQListAdapter;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

public class FAQFragment extends DialogFragment {
	private MainActivity parentActivity;
	private static final String TAG = "FAQFragment";
	public static final String CATEGORY_KEY = "category";

	private ListView faqView;
	private List<FAQEntry> faqEntries;
	private FAQListAdapter faqAdapter;

	public static FAQFragment newInstance() {
		return new FAQFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Activity activity = getActivity();
		if (activity instanceof MainActivity) {
			parentActivity = (MainActivity) activity;
		} else {
			Log.e(TAG, "ParentActivity is not MainActivity. Big Error!");
			throw new ClassCastException();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.faq_fragment, container,
				false);
		faqView = (ListView) rootView.findViewById(R.id.faq);
		faqEntries = (ArrayList<FAQEntry>) getArguments().getSerializable(
				CATEGORY_KEY);
		faqAdapter = new FAQListAdapter(parentActivity, R.layout.faq_layout,
				faqEntries);
		SlideExpandableListAdapter adapter = new SlideExpandableListAdapter(
				faqAdapter, R.id.expandable_toogle_layout, R.id.expandable);
		faqView.setAdapter(adapter);
		return rootView;
	}
}
