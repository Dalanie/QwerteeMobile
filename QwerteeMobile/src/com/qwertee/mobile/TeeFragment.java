package com.qwertee.mobile;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.qwertee.tees.BuyableTee;
import com.qwertee.utilities.ImageSlidePagerAdapter;
import com.qwertee.utilities.ZoomOutPageTransformer;

public abstract class TeeFragment extends Fragment {
	private static final String TAG = "TeeFragment";

	protected ViewPager mPager;
	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	protected PagerAdapter mPagerAdapter;
	protected BuyableTee tee;
	protected MainActivity parentActivity;

	protected ImageButton menImageButton;
	protected ImageButton zoomImageButton;
	protected ImageButton womenImageButton;
	protected ImageButton maximumImageButton;
	protected TextView userNameView;
	protected TextView designNameView;
	protected TextView priceView;

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
		View rootView = inflater.inflate(R.layout.tee_fragment, container,
				false);
		mPager = (ViewPager) rootView.findViewById(R.id.teeViewPager);
		menImageButton = (ImageButton) rootView.findViewById(R.id.mens_tee);
		menImageButton.setOnClickListener(new ImageOnClickListener(0));
		zoomImageButton = (ImageButton) rootView.findViewById(R.id.zoom_tee);
		zoomImageButton.setOnClickListener(new ImageOnClickListener(1));
		womenImageButton = (ImageButton) rootView.findViewById(R.id.womens_tee);
		womenImageButton.setOnClickListener(new ImageOnClickListener(2));
		maximumImageButton = (ImageButton) rootView
				.findViewById(R.id.maximum_tee);
		maximumImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				parentActivity.onMaximizeClick(tee);
			}
		});
		designNameView = (TextView) rootView
				.findViewById(R.id.designNameTextView);
		userNameView = (TextView) rootView.findViewById(R.id.userNameTextView);

		priceView = (Button) rootView.findViewById(R.id.price_view);

		getTeeToBuy();
		return rootView;
	}

	protected abstract void getTeeToBuy();

	protected void initTee() {
		mPagerAdapter = new ImageSlidePagerAdapter(getChildFragmentManager(),
				tee);
		mPager.setAdapter(mPagerAdapter);
		mPager.setPageTransformer(true, new ZoomOutPageTransformer());
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int page) {
				changeStateButtons(page);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		designNameView.setText(tee.getDesignName());
		userNameView.setText(tee.getUserName());

		// priceView.setText(tee.getPriceByCountry("eu"));
		changeStateButtons(0);
	}

	protected class ImageOnClickListener implements ImageButton.OnClickListener {
		private int id = 0;

		public ImageOnClickListener(int id) {
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(id);
			changeStateButtons(id);
		}
	}

	protected void changeStateButtons(int id) {
		menImageButton.setSelected(false);
		womenImageButton.setSelected(false);
		zoomImageButton.setSelected(false);

		switch (id) {
		case 0:
			menImageButton.setSelected(true);
			break;
		case 1:
			zoomImageButton.setSelected(true);
			break;
		case 2:
			womenImageButton.setSelected(true);
			break;
		default:
			break;
		}
	}
}