package com.youngheart.engine;

import android.app.Application;

import com.infrastructure.cache.CacheManager;

public class YoungHeartApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate(); 
		
		CacheManager.getInstance().initCacheDir();
	}
}
