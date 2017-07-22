package com.common.library.llj.recyclerview.drag;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

/**
 * Created by llj on 2016/12/26.
 */

public interface ItemTouchHelperAdapter {
    int filterDragPosition(int position);

    boolean onMove(int fromPosition, int toPosition);

    void onSwiped(int position);

    void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive);

    void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder);

    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState);


}
