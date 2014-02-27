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
import com.qwertee.tees.VoteTee;

public class VoteTeeImageUrlAdapter extends ArrayAdapter<VoteTee> {

	private ImageLoader imageLoader;
	private LayoutInflater mInflater;

	public VoteTeeImageUrlAdapter(Context context) {
		super(context, 0);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = VolleySingleton.getImageLoader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.vote_fragment_item,
					parent, false);
			viewHolder = new ViewHolder();
			viewHolder.networkImageView = (AnimatedNetworkImageView) convertView
					.findViewById(R.id.vote_image);
			viewHolder.author = (TextView) convertView
					.findViewById(R.id.authorName);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.imageName);
			viewHolder.votes = (TextView) convertView
					.findViewById(R.id.vote_text);
			viewHolder.comments = (TextView) convertView
					.findViewById(R.id.comment_text);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		VoteTee element = getItem(position);
		viewHolder.networkImageView.setAnimation(AnimationUtils.loadAnimation(
				getContext(), android.R.anim.fade_in));
		viewHolder.networkImageView.setImageUrl(NetworkCommunication.qwerteeURL
				+ element.getImagePath(), imageLoader);
		viewHolder.networkImageView
				.setDefaultImageResId(R.drawable.placeholder);
		viewHolder.networkImageView.setErrorImageResId(R.drawable.placeholder);
		viewHolder.author.setText(element.getUserName());
		viewHolder.title.setText(element.getDesignName());
		viewHolder.votes.setText(element.getNumberOfVotes() + "");
		viewHolder.comments.setText(element.getNumberOfComments() + "");

		return convertView;
	}

	static class ViewHolder {
		AnimatedNetworkImageView networkImageView;
		TextView author;
		TextView title;
		TextView votes;
		TextView comments;
	}
}