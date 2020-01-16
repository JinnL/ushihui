package com.mcz.xhj.yz.dr_util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PersonalToast {
	private static PersonalToast toasts = null;
	private static Toast toast;
	private Context context;

	public PersonalToast(Context context) {
		this.context = context;
	}

	public void toastShow(String text) {

		if (toasts == null) {
			synchronized (PersonalToast.class) {
				if (toasts == null) {
					toasts = new PersonalToast(context);
				}
			}
		}
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}
}
