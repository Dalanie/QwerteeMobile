package com.qwertee.utilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qwertee.mobile.SingleImageViewFragment;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.tees.Tee;

public class ImageSlidePagerAdapter extends FragmentStatePagerAdapter {
	private Tee tee;

	public ImageSlidePagerAdapter(FragmentManager fm, Tee tee) {
		super(fm);
		this.tee = tee;
	}

	private static final String mensDesign = "images/designs/mens/";
	private static final String zoomDesign = "images/designs/zoom/";
	private static final String womensDesign = "images/designs/womens/";
	private final String[] designs = { mensDesign, zoomDesign, womensDesign };

	@Override
	public Fragment getItem(int i) {
		Fragment fragment = new SingleImageViewFragment();
		Bundle args = new Bundle();
		args.putString(SingleImageViewFragment.IMAGE_PATH,
				NetworkCommunication.qwerteeURL + designs[i] + tee.getTeeId()
						+ Tee.getImageending());
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount() { 
		return designs.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			return "Men's design";
		case 1:
			return "Zoom";
		case 2:
			return "Women's design";
		default:
			return "Today's Tee!";
		}
	}
}