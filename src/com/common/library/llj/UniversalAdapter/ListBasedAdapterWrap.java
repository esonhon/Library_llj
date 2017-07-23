package com.common.library.llj.UniversalAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.common.library.llj.loadmore.LoadMoreContainer;
import com.common.library.llj.utils.ListUtil;
import com.llj.adapter.ListBasedAdapter;
import com.llj.adapter.UniversalAdapter;
import com.llj.adapter.ViewHolder;
import com.llj.adapter.listener.ItemClickedListener;
import com.llj.adapter.observable.ObservableListWrapper;

import java.util.Collection;
import java.util.List;

/**
 * Created by liulj on 16/6/1.
 */
public abstract class ListBasedAdapterWrap<Item, Holder extends ViewHolder> extends ListBasedAdapter<Item, Holder> {
    /**
     * 由于{@link com.raizlabs.universaladapter.converter.UniversalAdapter#createViewHolder(ViewGroup, int)}中的
     * 方法的限制,如果{@link #getItemViewTypeCount()}返回2,那么{@link #getItemViewType(int)}必须返回的两种类型必须是0和1;
     * 如果返回3,那么{@link #getItemViewType(int)}必须返回的两种类型必须是0,1,2;
     */
    public static final int VIEW_TYPE_LOADING = -1;//加载更多布局
    public static final int VIEW_TYPE_ITEM    = 1;//正常布局


    /**
     * Constructs an empty {@link ListBasedAdapter}.
     */
    public ListBasedAdapterWrap() {
    }

    /**
     * Constructs a {@link ListBasedAdapter} which contains the given list.
     *
     * @param list The list of items to use.
     */
    public ListBasedAdapterWrap(List<Item> list) {
        super(list);
    }

    /**
     * Constructs a {@link ListBasedAdapter} which contains the given list. Giving this adapter's underlying data
     * a hook into notifying this class of its changes automatically.
     *
     * @param list The list of items to use.
     */
    public ListBasedAdapterWrap(ObservableListWrapper<Item> list) {
        super(list);
    }

    public List<Item> getList() {
        return getItemsList();
    }

    public Item getLast() {
        if (!ListUtil.isEmpty(getItemsList())) {
            getItemsList().get(getItemsList().size() - 1);
        }
        return null;
    }

    @Override
    public void clear() {
        if (size() != 0)
            super.clear();
    }

    /**
     * 移除加载布局,添加新的数据
     *
     * @param collection 数据集合
     */
    public void tryToAddAllAndNotify(Collection<Item> collection) {
        if (!ListUtil.isEmpty(collection)) {
            //添加新的数据
            addAll(collection);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    public ItemClickedListener<Item, Holder> getItemClickedListener() {
        return new ItemClickedListener<Item, Holder>() {
            @Override
            public void onItemClicked(UniversalAdapter adapter, Item item, Holder holder, int position) {

            }
        };
    }

    /**
     * 置是否加载完毕
     *
     * @param offset -1加载完毕,否则没加载完
     */
    public void setBottomLoadMoreFinishFlag(Long offset, LoadMoreContainer loadMoreContainer) {
        if (offset == null || offset == -1) {
            loadMoreContainer.bottomLoadMoreFinish(false);
        } else {
            tryToAddBottomLoadMoreView();
            loadMoreContainer.bottomLoadMoreFinish(true);
        }
    }

    public void setBottomLoadMoreFinishFlag(boolean hasMore, LoadMoreContainer loadMoreContainer) {
        if (hasMore) {
            loadMoreContainer.bottomLoadMoreFinish(true);
            tryToAddBottomLoadMoreView();
        } else {
            loadMoreContainer.bottomLoadMoreFinish(false);
        }
    }

    public void setTopLoadMoreFinishFlag(boolean hasMore, LoadMoreContainer loadMoreContainer) {
        if (hasMore) {
            loadMoreContainer.topLoadMoreFinish(true);
            tryToAddTopLoadMoreView();
        } else {
            loadMoreContainer.topLoadMoreFinish(false);
        }
    }

    public void tryToRemoveBottomLoadMoreView() {
        if (size() > 0 && get(size() - 1) == null) {
            remove(size() - 1);
        }
    }

    public void tryToRemoveTopLoadMoreView() {
        if (size() > 0 && get(0) == null) {
            remove(0);
        }
    }

    public void tryToAddBottomLoadMoreView() {
        if (size() > 10 && get(size() - 1) != null) {
            add(null);
        }
    }

    public void tryToAddTopLoadMoreView() {
        if (size() > 0 && get(0) != null) {
            add(0, null);
        }
    }

    @Override
    public Item set(int location, Item object) {
        Item result = getItemsList().set(location, object);
        onItemRangeChanged(location, 1);
        return result;
    }

    /**
     * @param itemView
     */
    protected void setLoadingLayoutParams(View itemView) {
        itemView.getLayoutParams().height = -2;
        itemView.getLayoutParams().width = -1;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    ;

    public void onReadInstanceState(Bundle outState) {
    }

    ;
}
