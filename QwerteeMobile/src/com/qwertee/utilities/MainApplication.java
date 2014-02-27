package com.qwertee.utilities;

import android.app.Application;

import com.qwertee.network.VolleySingleton;

//https://github.com/rdrobinson3/VolleyImageCacheExample/blob/master/CaptechBuzz/src/com/captechconsulting/captechbuzz/MainApplication.java
public class MainApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		VolleySingleton.init(this);
	}
}