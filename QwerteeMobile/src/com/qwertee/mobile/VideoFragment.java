package com.qwertee.mobile;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class VideoFragment extends YouTubePlayerSupportFragment implements
		OnInitializedListener {

	private YouTubePlayer player;
	private String videoId;
	private static final String DEVELOPER_KEY = "AIzaSyAAhLxNK44DAH6eU13kHno7EqALuA7A9CE";
	private static final String VIDEO_ID = "baLGNa1kckE";

	public static VideoFragment newInstance() {
		return new VideoFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(DEVELOPER_KEY, this);
	}

	@Override
	public void onDestroy() {
		if (player != null) {
			player.release();
		}
		super.onDestroy();
	}

	public void setVideoId(String videoId) {
		if (videoId != null && !videoId.equals(this.videoId)) {
			this.videoId = videoId;
			if (player != null) {
				player.cueVideo(videoId);
			}
		}
	}

	public void pause() {
		if (player != null) {
			player.pause();
		}
	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean restored) {
		this.player = player;
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);
		player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
		player.setShowFullscreenButton(true);
		
		// player.setOnFullscreenListener((VideoListDemoActivity)
		// getActivity());
		if (videoId == null) {
			videoId = VIDEO_ID;
		}
		if (!restored && videoId != null) {
			player.loadVideo(videoId);
		}

	}

	@Override
	public void onInitializationFailure(Provider provider,
			YouTubeInitializationResult result) {
		this.player = null;
	}

}
