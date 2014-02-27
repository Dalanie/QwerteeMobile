package com.qwertee.mobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.qwertee.network.VolleySingleton;

public class SingleImageViewFragment extends Fragment {
	public static final String TAG = "SingleImageViewFragment";
	public static final String IMAGE_PATH = "imagePath";
	private String imagePath;
	private NetworkImageView networkImageView;
	private ImageLoader mImageLoader;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageLoader = VolleySingleton.getImageLoader();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// The last two arguments ensure LayoutParams are inflated
		// properly.
		View rootView = inflater.inflate(R.layout.single_image_view, container,
				false);
		Bundle args = getArguments();
		imagePath = args.getString(IMAGE_PATH);
		networkImageView = (NetworkImageView) rootView
				.findViewById(R.id.single_image_view);
		networkImageView.setImageUrl(imagePath, mImageLoader);
		networkImageView.setDefaultImageResId(R.drawable.placeholder_previous);
		networkImageView.setErrorImageResId(R.drawable.placeholder_previous);
		return rootView;
	}
}
