package com.common.library.llj.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.library.llj.R;

/**
 * 通用自定义底部栏
 * 
 * @author llj 14/8/30
 * 
 */
public class CommonFootbar extends FrameLayout {
	private LinearLayout mItemLi1;
	private LinearLayout mItemLi2;
	private LinearLayout mItemLi3;
	private LinearLayout mItemLi4;
	private ImageView mItemIv1;
	private ImageView mItemIv2;
	private ImageView mItemIv3;
	private ImageView mItemIv4;
	private TextView mItemTv1;
	private TextView mItemTv2;
	private TextView mItemTv3;
	private TextView mItemTv4;

	public CommonFootbar(Context context) {
		super(context);
		initViews(context, null);
	}

	public CommonFootbar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViews(context, attrs);
	}

	public CommonFootbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViews(context, attrs);
	}

	private void initViews(Context context, AttributeSet attrs) {
		LayoutInflater.from(getContext()).inflate(R.layout.common_footbar_layout, this);
		mItemLi1 = (LinearLayout) findViewById(R.id.li_menu1);
		mItemLi2 = (LinearLayout) findViewById(R.id.li_menu2);
		mItemLi3 = (LinearLayout) findViewById(R.id.li_menu3);
		mItemLi4 = (LinearLayout) findViewById(R.id.li_menu4);

		mItemIv1 = (ImageView) findViewById(R.id.iv_menu_home);
		mItemIv2 = (ImageView) findViewById(R.id.iv_menu_community);
		mItemIv3 = (ImageView) findViewById(R.id.iv_menu_shopping_car);
		mItemIv4 = (ImageView) findViewById(R.id.iv_menu_mine);

		mItemTv1 = (TextView) findViewById(R.id.tv_menu_home);
		mItemTv2 = (TextView) findViewById(R.id.tv_menu_community);
		mItemTv3 = (TextView) findViewById(R.id.tv_menu_shopping_car);
		mItemTv4 = (TextView) findViewById(R.id.tv_menu_mine);
		initAttrs(context, attrs);
	}

	@SuppressWarnings("deprecation")
	private void initAttrs(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CommonFootbar);
			int gravity = typedArray.getInt(R.styleable.CommonFootbar_footbar_gravity, -1);
			if (gravity == -1) {
				// 没有gravity属性
				LinearLayout.LayoutParams mItemIvParams1 = (android.widget.LinearLayout.LayoutParams) mItemIv1.getLayoutParams();
				LinearLayout.LayoutParams mItemIvParams2 = (android.widget.LinearLayout.LayoutParams) mItemIv2.getLayoutParams();
				LinearLayout.LayoutParams mItemIvParams3 = (android.widget.LinearLayout.LayoutParams) mItemIv3.getLayoutParams();
				LinearLayout.LayoutParams mItemIvParams4 = (android.widget.LinearLayout.LayoutParams) mItemIv4.getLayoutParams();

				int topmargin = typedArray.getDimensionPixelSize(R.styleable.CommonFootbar_footbar_image_marginTop, 0);
				mItemIvParams1.topMargin = topmargin;
				mItemIvParams2.topMargin = topmargin;
				mItemIvParams3.topMargin = topmargin;
				mItemIvParams4.topMargin = topmargin;

				int bottommargin = typedArray.getDimensionPixelSize(R.styleable.CommonFootbar_footbar_image_marginBottom, 0);
				mItemIvParams1.bottomMargin = bottommargin;
				mItemIvParams2.bottomMargin = bottommargin;
				mItemIvParams3.bottomMargin = bottommargin;
				mItemIvParams4.bottomMargin = bottommargin;

				mItemIv1.setLayoutParams(mItemIvParams1);
				mItemIv2.setLayoutParams(mItemIvParams2);
				mItemIv3.setLayoutParams(mItemIvParams3);
				mItemIv4.setLayoutParams(mItemIvParams4);
			} else {
				// 有gravity属性
				mItemLi1.setGravity(gravity);
				mItemLi2.setGravity(gravity);
				mItemLi3.setGravity(gravity);
				mItemLi4.setGravity(gravity);
			}
			// 设置选中项背景
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background1) != null)
				mItemLi1.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background1));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background2) != null)
				mItemLi2.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background2));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background3) != null)
				mItemLi3.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background3));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background4) != null)
				mItemLi4.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_item_background4));
			// 设置图片背景
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background1) != null)
				mItemIv1.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background1));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background2) != null)
				mItemIv2.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background2));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background3) != null)
				mItemIv3.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background3));
			if (typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background4) != null)
				mItemIv4.setBackgroundDrawable(typedArray.getDrawable(R.styleable.CommonFootbar_footbar_image_background4));
			// 设置文本框背景
			mItemTv1.setText(typedArray.getString(R.styleable.CommonFootbar_footbar_text1));
			mItemTv2.setText(typedArray.getString(R.styleable.CommonFootbar_footbar_text2));
			mItemTv3.setText(typedArray.getString(R.styleable.CommonFootbar_footbar_text3));
			mItemTv4.setText(typedArray.getString(R.styleable.CommonFootbar_footbar_text4));
			// 设置文本框字体大小
			int textSize = (int) typedArray.getDimension(R.styleable.CommonFootbar_footbar_text_size, 24);
			// 默认传入的是sp单位，由于这里textSize已经的px单位，所以需要制定传入的单位为px
			mItemTv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			mItemTv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			mItemTv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			mItemTv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			// 设置文本框字体颜色
			ColorStateList colorStateList = typedArray.getColorStateList(R.styleable.CommonFootbar_footbar_text_color);
			if (colorStateList != null) {
				mItemTv1.setTextColor(colorStateList);
				mItemTv2.setTextColor(colorStateList);
				mItemTv3.setTextColor(colorStateList);
				mItemTv4.setTextColor(colorStateList);
			}

			typedArray.recycle();
		}

	}

	public void setOnlyItem1SelectedTrue() {
		mItemLi1.setSelected(true);
		mItemTv1.setSelected(true);
		mItemIv1.setSelected(true);
		mItemLi2.setSelected(false);
		mItemTv2.setSelected(false);
		mItemIv2.setSelected(false);
		mItemLi3.setSelected(false);
		mItemTv3.setSelected(false);
		mItemIv3.setSelected(false);
		mItemLi4.setSelected(false);
		mItemTv4.setSelected(false);
		mItemIv4.setSelected(false);
	}

	public void setOnlyItem2SelectedTrue() {
		mItemLi1.setSelected(false);
		mItemTv1.setSelected(false);
		mItemIv1.setSelected(false);
		mItemLi2.setSelected(true);
		mItemTv2.setSelected(true);
		mItemIv2.setSelected(true);
		mItemLi3.setSelected(false);
		mItemTv3.setSelected(false);
		mItemIv3.setSelected(false);
		mItemLi4.setSelected(false);
		mItemTv4.setSelected(false);
		mItemIv4.setSelected(false);
	}

	public void setOnlyItem3SelectedTrue() {
		mItemLi1.setSelected(false);
		mItemTv1.setSelected(false);
		mItemIv1.setSelected(false);
		mItemLi2.setSelected(false);
		mItemTv2.setSelected(false);
		mItemIv2.setSelected(false);
		mItemLi3.setSelected(true);
		mItemTv3.setSelected(true);
		mItemIv3.setSelected(true);
		mItemLi4.setSelected(false);
		mItemTv4.setSelected(false);
		mItemIv4.setSelected(false);
	}

	public void setOnlyItem4SelectedTrue() {
		mItemLi1.setSelected(false);
		mItemTv1.setSelected(false);
		mItemIv1.setSelected(false);
		mItemLi2.setSelected(false);
		mItemTv2.setSelected(false);
		mItemIv2.setSelected(false);
		mItemLi3.setSelected(false);
		mItemTv3.setSelected(false);
		mItemIv3.setSelected(false);
		mItemLi4.setSelected(true);
		mItemTv4.setSelected(true);
		mItemIv4.setSelected(true);
	}

	public LinearLayout getLinearLayout1() {
		return mItemLi1;
	}

	public LinearLayout getLinearLayout2() {
		return mItemLi1;
	}

	public LinearLayout getLinearLayout3() {
		return mItemLi1;
	}

	public LinearLayout getLinearLayout4() {
		return mItemLi1;
	}

	public void setItemSelectListener(OnClickListener onClickListener) {
		if (onClickListener != null) {
			mItemLi1.setOnClickListener(onClickListener);
			mItemLi2.setOnClickListener(onClickListener);
			mItemLi3.setOnClickListener(onClickListener);
			mItemLi4.setOnClickListener(onClickListener);
		}
	}

}
