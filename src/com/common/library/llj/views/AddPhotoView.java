package com.common.library.llj.views;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.common.library.llj.R;

/**
 * 添加照片的布局
 * 
 * @author liulj
 * 
 */
public class AddPhotoView extends LinearLayout {
	private int mViewCount;
	private Context mContext;
	private int screenWidth;
	private int mCountInLine = 3;
	private int mHorizontalSpace = 20;
	private int mVerticalSpace = 20;
	private List<AddViewItem> mAddViewItems = new ArrayList<AddViewItem>();
	private int mLine = 0;

	public AddPhotoView(Context context) {
		super(context);
		mContext = context;
		initDisplay();
	}

	public AddPhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initDisplay();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public AddPhotoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initDisplay();
	}

	private void initDisplay() {
		mAddViewItems.clear();
		screenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 设置每行显示几个
	 * 
	 * @param countInLine
	 */
	public void setCountInLine(int countInLine) {
		mCountInLine = countInLine;
	}

	/**
	 * 设置行间距
	 * 
	 * @param horizontalSpace
	 */
	public void setHorizontalSpace(int horizontalSpace) {
		mHorizontalSpace = horizontalSpace;
	}

	/**
	 * 设置列间距
	 * 
	 * @param verticalSpace
	 */
	public void setVerticalSpace(int verticalSpace) {
		this.mVerticalSpace = verticalSpace;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	public void setLinWidth(int width) {
		screenWidth = width;
	}

	/**
	 * 
	 * @param bitmap
	 * @param listener
	 */
	public void updateAddViewOnce(Bitmap bitmap, final OnAddClickListener listener) {
		mViewCount++;
		// 每行的第一个都先加载行布局
		if (mViewCount % mCountInLine == 1) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenWidth, -2);
			if (mViewCount != 1) {
				lp.topMargin = mVerticalSpace;
			}
			addView(new LinearLayout(mContext), lp);
		}
		// 初始化item
		View child = View.inflate(mContext, R.layout.addview_item, null);
		final ImageView display = (ImageView) child.findViewById(R.id.iv_image1);
		final ImageView delete = (ImageView) child.findViewById(R.id.iv_delete1);
		display.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 0可以添加
				if ("0".equals(v.getTag())) {
					if (listener != null) {
						listener.onAddClick();
					}
				}
			}
		});
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 1可以删除
				if ("1".equals(display.getTag())) {
					delete();
				}
			}
		});
		// 计算是第几行
		if (mViewCount % mCountInLine == 0) {
			mLine = mViewCount / mCountInLine - 1;
		} else {
			mLine = mViewCount / mCountInLine;
		}
		// 将child存入某行
		if (getChildCount() > 0 && getChildAt(mLine) instanceof LinearLayout) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2, -2);
			lp.leftMargin = mHorizontalSpace;
			lp.width = (screenWidth - mCountInLine * mHorizontalSpace) / mCountInLine;
			lp.height = lp.width;
			((LinearLayout) getChildAt(mLine)).addView(child, lp);
			// 将对象存进集合
			mAddViewItems.add(new AddViewItem(display, delete));
		}
		//
		if (bitmap != null) {
			if (mViewCount - 2 >= 0) {
				AddViewItem addViewItem = mAddViewItems.get(mViewCount - 2);
				addViewItem.getmDisplay().setImageBitmap(bitmap);
				addViewItem.getmDisplay().setTag("1");
				addViewItem.getmDelete().setVisibility(View.VISIBLE);
			}
			if (mViewCount - 3 >= 0) {
				AddViewItem addViewItem = mAddViewItems.get(mViewCount - 3);
				addViewItem.getmDisplay().setTag("2");
				addViewItem.getmDelete().setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
 * 
 */
	private void delete() {
		// 移除最后一个view
		mAddViewItems.remove(mViewCount - 1);
		int childCount = ((LinearLayout) getChildAt(mLine)).getChildCount();
		if (childCount != 0) {
			((LinearLayout) getChildAt(mLine)).removeViewAt(childCount - 1);
		}
		// 设置自己为加号
		if (mAddViewItems.size() > mViewCount - 2) {
			AddViewItem addViewItem = mAddViewItems.get(mViewCount - 2);
			addViewItem.getmDisplay().setImageResource(R.drawable.bid_add_icon);
			addViewItem.getmDisplay().setTag("0");
			addViewItem.getmDelete().setVisibility(View.INVISIBLE);
		}
		// 设置前一项可以delete
		if (mAddViewItems.size() > mViewCount - 3 && mViewCount - 3 >= 0) {
			AddViewItem addViewItem = mAddViewItems.get(mViewCount - 3);
			addViewItem.getmDelete().setVisibility(View.VISIBLE);
			addViewItem.getmDisplay().setTag("1");
		}
		mViewCount--;
		//
		if (mViewCount % mCountInLine == 0) {
			mLine = mViewCount / mCountInLine - 1;
		} else {
			mLine = mViewCount / mCountInLine;
		}
	}

	public interface OnAddClickListener {
		void onAddClick();
	}

	class AddViewItem {
		private ImageView mDisplay;
		private ImageView mDelete;

		public AddViewItem(ImageView display, ImageView delete) {
			this.mDisplay = display;
			this.mDelete = delete;
		}

		public ImageView getmDisplay() {
			return mDisplay;
		}

		public void setmDisplay(ImageView mDisplay) {
			this.mDisplay = mDisplay;
		}

		public ImageView getmDelete() {
			return mDelete;
		}

		public void setmDelete(ImageView mDelete) {
			this.mDelete = mDelete;
		}

	}
}
