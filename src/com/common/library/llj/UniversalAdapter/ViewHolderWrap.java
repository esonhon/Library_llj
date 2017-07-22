package com.common.library.llj.UniversalAdapter;

import android.view.View;

import com.llj.adapter.ViewHolder;

import butterknife.ButterKnife;

/**
 * Created by liulj on 16/6/1.
 */
public class ViewHolderWrap extends ViewHolder {

    public ViewHolderWrap(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
