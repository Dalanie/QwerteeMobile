package com.qwertee.mobile;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qwertee.tees.BuyableTee;

public class SingleBuyableTeeViewFragment extends TeeFragment {

	public SingleBuyableTeeViewFragment(BuyableTee tee) {
		this.tee = tee;
	}

	@Override
	protected void getTeeToBuy() {
		initTee();
	}

	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.single_buyable_tee_view,
				container, false);
		mPager = (ViewPager) rootView.findViewById(R.id.teeViewPager);
		menImageButton = (ImageButton) rootView.findViewById(R.id.mens_tee);
		menImageButton.setOnClickListener(new ImageOnClickListener(0));
		zoomImageButton = (ImageButton) rootView.findViewById(R.id.zoom_tee);
		zoomImageButton.setOnClickListener(new ImageOnClickListener(1));
		womenImageButton = (ImageButton) rootView.findViewById(R.id.womens_tee);
		womenImageButton.setOnClickListener(new ImageOnClickListener(2));

		designNameView = (TextView) rootView
				.findViewById(R.id.designNameTextView);
		userNameView = (TextView) rootView.findViewById(R.id.userNameTextView);

		// priceView = (Button) rootView.findViewById(R.id.price_view);

		getTeeToBuy();
		// ((MainActivity) getActivity()).mDrawerToggle
		// .setDrawerIndicatorEnabled(false);
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
