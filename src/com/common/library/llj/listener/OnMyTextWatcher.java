package com.common.library.llj.listener;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * 过滤不用的方法
 * 
 * @author liulj
 * 
 */
public abstract class OnMyTextWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
