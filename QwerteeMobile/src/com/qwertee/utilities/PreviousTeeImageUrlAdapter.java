package com.qwertee.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.qwertee.mobile.R;
import com.qwertee.network.NetworkCommunication;
import com.qwertee.network.VolleySingleton;
import com.qwertee.tees.PreviousTee;

public class PreviousTeeImageUrlAdapter extends ArrayAdapter<PreviousTee> {

	private ImageLoader imageLoader;
	private LayoutInflater mInflater;

	public PreviousTeeImageUrlAdapter(Context context) {
		super(context, 0);
		mInflater = LayoutInflater.from(context);
		imageLoader = VolleySingleton.getImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.previous_tee_fragment_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.networkImageView = (AnimatedNetworkImageView) convertView
					.findViewById(R.id.vote_image);
			viewHolder.imageDescription = (TextView) convertView
					.findViewById(R.id.imageDescription);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PreviousTee element = getItem(position);
		viewHolder.networkImageView.setAnimation(AnimationUtils.loadAnimation(
				getContext(), android.R.anim.fade_in));
		viewHolder.networkImageView.setImageUrl(NetworkCommunication.qwerteeURL
				+ element.getImagePath(), imageLoader);
		viewHolder.networkImageView
				.setDefaultImageResId(R.drawable.placeholder_previous);
		viewHolder.networkImageView.setErrorImageResId(R.drawable.placeholder_previous);
		String description = element.getDesignName() + "\n"
				+ element.getUserName();
		viewHolder.imageDescription.setText(description);
		return convertView;
	}

	static class ViewHolder {
		AnimatedNetworkImageView networkImageView;
		TextView imageDescription;
	}
}