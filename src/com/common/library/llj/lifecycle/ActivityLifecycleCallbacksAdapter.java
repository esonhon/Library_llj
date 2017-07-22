package com.common.library.llj.lifecycle;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * @author liulj
 * 
 */
public class ActivityLifecycleCallbacksAdapter implements ActivityLifecycleCallbacksCompat {
	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
	}

	@Override
	public void onActivityStarted(Activity activity) {
	}

	@Override
	public void onActivityResumed(Activity activity) {
	}

	@Override
	public void onActivityPaused(Activity activity) {
	}

	@Override
	public void onActivityStopped(Activity activity) {
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
	}
}
