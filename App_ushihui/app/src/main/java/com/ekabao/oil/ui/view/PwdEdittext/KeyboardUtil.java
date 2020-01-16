package com.ekabao.oil.ui.view.PwdEdittext;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;


import com.ekabao.oil.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *  随机数字键盘
 * @author AHF
 *
 */
public class KeyboardUtil {
	private View mActivity;
	@SuppressWarnings("unused")
	private Context mContext;
	@SuppressWarnings("unused")
	private KeyboardView keyboardView;
	private Keyboard keyDig;// 数字键盘
	private SecurityPasswordEditText securityPasswordEditText;
	private EditText ed;

	public KeyboardUtil(View mActivity,Context mContext, SecurityPasswordEditText edit) {
		this.mActivity=mActivity;
		this.mContext = mContext;
		securityPasswordEditText = edit;
		this.ed = edit.getSecurityEdit();
		keyDig = new Keyboard(mContext, R.xml.symbols);
		keyboardView = (KeyboardView) mActivity.findViewById(R.id.keyboard_view);
		randomdigkey();
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
	}

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = ed.getText();
			int start = ed.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				securityPasswordEditText.clearSecurityEdit() ;
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				securityPasswordEditText.delTextValue();
				if (!TextUtils.isEmpty(editable)) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
		}
	};

	/**
	 * 键盘大小写切换
	 */
	public void showKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.GONE || visibility == View.INVISIBLE) {
			keyboardView.setVisibility(View.VISIBLE);
		}
	}

	public void hideKeyboard() {
		int visibility = keyboardView.getVisibility();
		if (visibility == View.VISIBLE) {
			keyboardView.setVisibility(View.INVISIBLE);
		}
	}

	private boolean isNumber(String str) {
		String wordstr = "0123456789";
		if (wordstr.indexOf(str) > -1) {
			return true;
		}
		return false;
	}

	private void randomdigkey(){
		List<Key> keyList = keyDig.getKeys();
		// 查找出0-9的数字键
		List<Key> newkeyList = new ArrayList<Key>();
		for (int i = 0; i < keyList.size(); i++) {
			if (keyList.get(i).label != null
					&& isNumber(keyList.get(i).label.toString())) {
				newkeyList.add(keyList.get(i));
			}
		}
		// 数组长度
		int count = newkeyList.size();
		// 结果集
		List<KeyModel> resultList = new ArrayList<KeyModel>();
		// 用一个LinkedList作为中介
		LinkedList<KeyModel> temp = new LinkedList<KeyModel>();
		// 初始化temp
		for (int i = 0; i < count; i++) {
			temp.add(new KeyModel(48 + i, i + ""));
		}
		// 取数
		Random rand = new Random();
		for (int i = 0; i < count; i++) {
			int num = rand.nextInt(count - i);
			resultList.add(new KeyModel(temp.get(num).getCode(),
					temp.get(num).getLable()));
			temp.remove(num);
		}
		for (int i = 0; i < newkeyList.size(); i++) {
			newkeyList.get(i).label = resultList.get(i).getLable();
			newkeyList.get(i).codes[0] = resultList.get(i)
					.getCode();
		}
		keyboardView.setKeyboard(keyDig);
	}
}
