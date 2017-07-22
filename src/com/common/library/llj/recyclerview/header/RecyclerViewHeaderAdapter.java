package com.common.library.llj.recyclerview.header;

import android.support.v7.widget.RecyclerView;

import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 存在header的RecyclerView的适配器
 * Created by liulj on 16/4/25.
 */
public abstract class RecyclerViewHeaderAdapter<Holder extends RecyclerView.ViewHolder, Item extends RecyclerViewHeaderAdapter.RecyclerViewHeaderItem> extends RecyclerView.Adapter<Holder> {
    protected final int VIEW_TYPE_HEADER = 0x01;
    protected final int VIEW_TYPE_CONTENT = 0x00;
    protected String HEAD = "head";
    protected String ITEM = "item";
    protected int headerDisplay;//头部显示的模式
    protected boolean marginsFixed;//是否与显示item间隔距离

    private List<Item> recyclerViewHeaderItemList;

    public RecyclerViewHeaderAdapter(List<Item> recyclerViewHeaderItemList) {
        if (recyclerViewHeaderItemList == null)
            this.recyclerViewHeaderItemList = new ArrayList<>();
        else
            this.recyclerViewHeaderItemList = recyclerViewHeaderItemList;

        setMarginsFixed(false);
        setHeadersSticky(false);
        setHeadersOverlaid(false);
    }

    public void add(Item recyclerViewHeaderItem) {
        recyclerViewHeaderItemList.add(recyclerViewHeaderItem);
    }

    public Item get(int position) {
        return recyclerViewHeaderItemList.get(position);
    }

    public void clear() {
        recyclerViewHeaderItemList.clear();
    }

    public int size() {
        return recyclerViewHeaderItemList.size();
    }


    //设置头部是否覆盖在item上,这样会重叠
    public void setHeadersOverlaid(boolean areHeadersOverlaid) {
        headerDisplay = areHeadersOverlaid ? headerDisplay | LayoutManager.LayoutParams.HEADER_OVERLAY : headerDisplay & ~LayoutManager.LayoutParams.HEADER_OVERLAY;
        notifyHeaderChanges();
    }

    //是否设置头部悬停
    public void setHeadersSticky(boolean areHeadersSticky) {
        headerDisplay = areHeadersSticky ? headerDisplay | LayoutManager.LayoutParams.HEADER_STICKY : headerDisplay & ~LayoutManager.LayoutParams.HEADER_STICKY;
        notifyHeaderChanges();
    }

    //设置固定
    public void setMarginsFixed(boolean areMarginsFixed) {
        marginsFixed = areMarginsFixed;
        notifyHeaderChanges();
    }

    //设置头部的位置inline,start,end
    public void setHeaderMode(int mode) {
        headerDisplay = mode | (headerDisplay & LayoutManager.LayoutParams.HEADER_OVERLAY) | (headerDisplay & LayoutManager.LayoutParams.HEADER_STICKY);
        notifyHeaderChanges();
    }

    /**
     * 更新header
     */
    public void notifyHeaderChanges() {
        if (recyclerViewHeaderItemList != null && recyclerViewHeaderItemList.size() != 0)
            for (int i = 0; i < recyclerViewHeaderItemList.size(); i++) {
                RecyclerViewHeaderItem item = recyclerViewHeaderItemList.get(i);
                if (item.isHeader) {
                    notifyItemChanged(i);
                }
            }
    }

    /**
     * 基类
     */
    public static class RecyclerViewHeaderItem {
        public boolean isHeader; //是否是header
        public int sectionFirstPosition;//照片所在section的位置
        public int position;//照片集合中的位置,不包括header的,可以获取对应的对象

        public RecyclerViewHeaderItem(boolean isHeader, int sectionFirstPosition, int position) {
            this.isHeader = isHeader;
            this.sectionFirstPosition = sectionFirstPosition;
            this.position = position;
        }
    }
}