package com.mcz.xhj.yz.gesture;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;

public class ActivityCollector {

	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);

	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		Log.d("allen", "activities" + activities.size());
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
