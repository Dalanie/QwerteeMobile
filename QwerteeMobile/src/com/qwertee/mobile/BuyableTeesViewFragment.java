package com.qwertee.mobile;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qwertee.utilities.CountdownChronometer;

public class BuyableTeesViewFragment extends Fragment {
	public static final String TAG = "BuyFragment";

	private TeeFragment todaysTeeFragment;
	private TeeFragment lastChanceTeeFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		lastChanceTeeFragment = new LastChanceTeeFragment();
		todaysTeeFragment = new TodaysTeeFragment();

		FragmentManager manager = getChildFragmentManager();
		manager.beginTransaction()
				.replace(R.id.contentToday, todaysTeeFragment).commit();
		manager.beginTransaction()
				.replace(R.id.contentLastChance, lastChanceTeeFragment)
				.commit();
	}

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.buy_fragment_layout, container,
				false);
		CountdownChronometer chronometer = (CountdownChronometer) rootView
				.findViewById(R.id.chronometer);
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+1"));
		Date now = new Date();
		c.setTime(now);
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		chronometer.setBase(c.getTimeInMillis());
		chronometer.start();

		return rootView;
	}

	// http://stackoverflow.com/questions/14900738/nested-fragments-disappear-during-transition-animation
	// https://gist.github.com/luksprog/4996190
	private Bitmap b = null;

	@Override
	public void onPause() {
		b = loadBitmapFromView(getView());
		super.onPause();
	}

	public static Bitmap loadBitmapFromView(View v) {
		Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, v.getWidth(), v.getHeight());
		v.draw(c);
		return b;
	}

	@Override
	public void onDestroyView() {
		BitmapDrawable bd = new BitmapDrawable(getResources(), b);
		rootView.setBackground(bd);
		b = null;
		super.onDestroyView();
	}
}
