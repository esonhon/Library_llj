package com.common.library.llj.UniversalAdapter;

import com.llj.adapter.ViewHolder;
import com.llj.adapter.observable.ObservableListWrapper;
import com.tonicartos.superslim.LayoutManager;

import java.util.List;

/**
 * project:babyphoto_app
 * describe:
 * Created by llj on 2017/4/27.
 */

public abstract class ListBasedAdapterSticky<Item, Holder extends ViewHolder> extends ListBasedAdapterWrap<Item, Holder> {
    protected int     headerDisplay;//头部显示的模式
    protected boolean marginsFixed;//是否与显示item间隔距离

    public ListBasedAdapterSticky() {
    }

    public ListBasedAdapterSticky(List<Item> list) {
        super(list);
    }

    public ListBasedAdapterSticky(ObservableListWrapper<Item> list) {
        super(list);
    }

    //设置头部是否覆盖在item上,这样会重叠
    public void setHeadersOverlaid(boolean areHeadersOverlaid) {
        headerDisplay = areHeadersOverlaid ? headerDisplay | LayoutManager.LayoutParams.HEADER_OVERLAY : headerDisplay & ~LayoutManager.LayoutParams.HEADER_OVERLAY;
    }

    //是否设置头部悬停
    public void setHeadersSticky(boolean areHeadersSticky) {
        headerDisplay = areHeadersSticky ? headerDisplay | LayoutManager.LayoutParams.HEADER_STICKY : headerDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY;
    }

    //设置固定
    public void setMarginsFixed(boolean areMarginsFixed) {
        marginsFixed = areMarginsFixed;
    }

    //设置头部的位置inline,start,end
    public void setHeaderMode(int mode) {
        headerDisplay = mode | (headerDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (headerDisplay & LayoutManager.LayoutParams.HEADER_STICKY);
    }


}
