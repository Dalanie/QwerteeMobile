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
import com.qwertee.tees.VoteTee;
import com.qwertee.utilities.VoteTeeImageUrlAdapter;

public class VoteFragment extends Fragment implements OnItemClickListener {

	private MainActivity parentActivity;
	private static final String TAG = "VoteFragment";
	private VoteTeeImageUrlAdapter imageUrlAdapter;

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
		View rootView = inflater.inflate(R.layout.vote_fragment, container,
				false);
		GridView voteGridView = (GridView) rootView
				.findViewById(R.id.voteGridView);
		setupGridView(voteGridView);

		NetworkCommunication.loadVotes(new Listener<ArrayList<VoteTee>>() {
			@Override
			public void onResponse(ArrayList<VoteTee> votes) {
				for (VoteTee voteImageElement : votes) {
					imageUrlAdapter.add(voteImageElement);
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
		imageUrlAdapter = new VoteTeeImageUrlAdapter(parentActivity);
		voteGridView.setAdapter(imageUrlAdapter);
		voteGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
