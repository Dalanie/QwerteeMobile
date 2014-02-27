package com.qwertee.utilities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
	private static float MIN_SCALE = 0.85f;
	private static float MIN_ALPHA = 0.5f;

	@SuppressLint("NewApi")
	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				// > 11 version
				view.setAlpha(0);
			} else {
				ViewHelper.setAlpha(view, 0);
			}

		} else if (position <= 1) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;

			float translationX = 0;
			if (position < 0) {
				translationX = horzMargin - vertMargin / 2;
			} else {
				translationX = -horzMargin + vertMargin / 2;
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				// > 11 version
				view.setTranslationX(translationX);
				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));
			} else {
				ViewHelper.setTranslationX(view, translationX);
				// Scale the page down (between MIN_SCALE and 1)
				ViewHelper.setScaleX(view, scaleFactor);
				ViewHelper.setScaleY(view, scaleFactor);
				// Fade the page relative to its size.
				ViewHelper.setAlpha(view, MIN_ALPHA + (scaleFactor - MIN_SCALE)
						/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));
			}

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				view.setAlpha(0);
			} else {
				ViewHelper.setAlpha(view, 0);
			}
		}
	}
}