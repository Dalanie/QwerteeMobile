package com.qwertee.mobile;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends DialogFragment {
	private static final String QWERTEE_COLOR_STRING = "<b><font color=\"#ffffff\">Qwer</font><font color=\"#A6A8AB\">tee</font></b>";

	public static AboutFragment newInstance() {
		return new AboutFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.about_fragment, container,
				false);
		if (getChildFragmentManager().findFragmentByTag("added_tag") == null) {
			VideoFragment fragment = VideoFragment.newInstance();
			getChildFragmentManager()
					.beginTransaction()
					.add(R.id.youtube_fragment_container, fragment, "added_tag")
					.commit();
		}

		TextView titleTextView = (TextView) rootView
				.findViewById(R.id.titleTextView);
		titleTextView.setText(Html.fromHtml(String.format("What is %s?",
				QWERTEE_COLOR_STRING)));

		return rootView;
	}

}
