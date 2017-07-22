package com.common.library.llj.listener;

import android.support.v4.view.ViewPager;

/**
 * project:babyphoto_app
 * describe:
 * Created by llj on 2017/5/10.
 */

public class OnMyPageChangeListener implements ViewPager.OnPageChangeListener {
    private boolean flag;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        switch(state) {
//            case ViewPager.SCROLL_STATE_DRAGGING:
//                //拖的时候才进入下一页
//                flag = false;
//                break;
//            case ViewPager.SCROLL_STATE_SETTLING:
//                flag = true;
//                break;
//            case ViewPager.SCROLL_STATE_IDLE:
//                if(mViewPager.getCurrentItem() == mWelcomeViewpagerAdapter.getCount() - 1 && !flag) {
//                    Intent localIntent = new Intent();
//                    localIntent.setClass(WelcomeActivity.this, MainActivity.class);
//                    startActivity(localIntent);
//                    // overridePendingTransition(0, 0);
//                    finish();
//                }
//                flag = true;
//                break;
//        }
    }
}
