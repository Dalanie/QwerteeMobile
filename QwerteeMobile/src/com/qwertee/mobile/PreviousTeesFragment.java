package com.qwertee.mobile;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.tees.PreviousTee;
import com.qwertee.utilities.PreviousTeeImageUrlAdapter;

public class PreviousTeesFragment extends Fragment implements
		OnItemClickListener {

	private MainActivity parentActivity;
	private static final String TAG = "PreviousTeesFragment";
	private PreviousTeeImageUrlAdapter imageUrlAdapter;

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
		View rootView = inflater.inflate(R.layout.previous_tee_fragment,
				container, false);
		GridView previousTeeGridView = (GridView) rootView
				.findViewById(R.id.previousTeeGridView);
		setupGridView(previousTeeGridView);

		NetworkCommunication.loadPreviousTees(
				new Listener<ArrayList<PreviousTee>>() {
					@Override
					public void onResponse(ArrayList<PreviousTee> tees) {
						for (PreviousTee previousTeeImageElement : tees) {
							imageUrlAdapter.add(previousTeeImageElement);
						}
					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub

					}
				}, parentActivity);
		return rootView;
	}

	private void setupGridView(GridView voteGridView) {
		imageUrlAdapter = new PreviousTeeImageUrlAdapter(parentActivity);
		voteGridView.setAdapter(imageUrlAdapter);
		voteGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
