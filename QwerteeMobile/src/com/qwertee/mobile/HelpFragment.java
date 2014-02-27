package com.qwertee.mobile;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.utilities.Category;

/**
 * http://stackoverflow.com/questions/6987334/separate-back-stack-for-each-tab-
 * in-android-using-fragments/12928498#12928498
 * 
 * @author Daniel
 * 
 */
public class HelpFragment extends DialogFragment {
	private MainActivity parentActivity;
	private static final String TAG = "HelpFragment";

	private List<Category> mCategories;
	private View mTabContent;
	private ActionBar actionBar;

	public static HelpFragment newInstance() {
		return new HelpFragment();
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Specify that tabs should be displayed in the action bar.

		actionBar = parentActivity.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		View rootView = inflater.inflate(R.layout.help_fragment, container,
				false);
		mTabContent = rootView.findViewById(android.R.id.tabcontent);
		NetworkCommunication.loadFAQ(new Listener<ArrayList<Category>>() {
			@Override
			public void onResponse(ArrayList<Category> categories) {
				mCategories = categories;
				initializeTabs();
			}

		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub

			}
		}, parentActivity);
		return rootView;
	}

	public void onDestroyView() {
		super.onDestroyView();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	};

	/*
	 * Create a tab listener that is called when the user changes tabs.
	 */
	ActionBar.TabListener tabListener = new ActionBar.TabListener() {
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			/*
			 * We have the target fragment in hand.. Just show it.. Show a
			 * standard navigation animation
			 */
			// ft.setCustomAnimations(R.anim.slide_in_left,
			// R.anim.slide_out_right);
			FAQFragment fragment = new FAQFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable(FAQFragment.CATEGORY_KEY,
					mCategories.get(tab.getPosition()).getEntries());
			fragment.setArguments(bundle);
			ft.replace(android.R.id.tabcontent, fragment);
		}

		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// hide the given tab
		}

		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// probably ignore this event
		}
	};

	public void initializeTabs() {
		for (int i = 0; i < mCategories.size(); i++) {
			Category category = mCategories.get(i);
			actionBar.addTab(actionBar.newTab().setText(category.getName())
					.setTabListener(tabListener));
		}
	}
}
