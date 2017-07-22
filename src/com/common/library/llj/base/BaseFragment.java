package com.common.library.llj.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.library.llj.lifecycle.LifecycleDispatcher;
import com.common.library.llj.utils.ActivityAnimUtil;
import com.common.library.llj.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

/**
 * 这里做一些共同的事情，比如check
 *
 * @author liulj
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 保存在onDestroyView中remove的view,这样onCreateView的时候不用重新inflater，复用之前的view
     * 这种情况只在attach和detach下，hide和show不用
     */
    private   View                 mPreView;
    private   boolean              mIsInit;
    private   boolean              mIsVisible;
    protected BaseFragmentActivity mBaseFragmentActivity;
    protected BaseApplication      mBaseApplication;

    private static final long CLICK_INTERVAL = 500;
    private              long mLastClickTime = 0;//最后点击的时间

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(String event) {
    }

    /**
     * 判断是否可以重新点击
     *
     * @return
     */
    public boolean canClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < CLICK_INTERVAL) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseFragmentActivity = (BaseFragmentActivity) context;
        mBaseApplication = (BaseApplication) mBaseFragmentActivity.getApplication();
        LifecycleDispatcher.get().onFragmentAttach(this, (Activity) context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LifecycleDispatcher.get().onFragmentCreated(this, savedInstanceState);
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        View layoutView = getLayoutView();
        if (layoutView != null) {
            view = layoutView;
        } else {
            view = inflater.inflate(getLayoutId(), null);
        }
        ButterKnife.bind(this, view);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        getArgumentsData();
        findViews(view, savedInstanceState);
        return view;
    }

    protected View getLayoutView() {
        return null;
    }

    protected abstract int getLayoutId();

    protected void getArgumentsData() {
    }

    /**
     * 进行相应的初始化工作
     *
     * @param view
     * @param savedInstanceState
     */
    protected void findViews(View view, Bundle savedInstanceState) {
    }


    @Override
    public void onStart() {
        super.onStart();
        LifecycleDispatcher.get().onFragmentStarted(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        LifecycleDispatcher.get().onFragmentResumed(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        LifecycleDispatcher.get().onFragmentPaused(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        LifecycleDispatcher.get().onFragmentStopped(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        LifecycleDispatcher.get().onFragmentDestroyed(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LifecycleDispatcher.get().onFragmentDetach(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LifecycleDispatcher.get().onFragmentSaveInstanceState(this, outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LifecycleDispatcher.get().onFragmentActivityCreated(this, savedInstanceState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //当fragment在viewPager中的时候需要实现懒加载的模式
        if (isVisibleToUser) {
            mIsVisible = true;
            //当使用viewPager进行预加载fragment的时候,先调用setUserVisibleHint,后调用onViewCreated
            //所以刚开始是mIsInit=true,mIsVisible为false
            if ((mIsInit) && (mIsVisible) && !isFinishLazyLoad())
                onLazyLoad();
        } else {
            mIsVisible = false;
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 已经完成初始化
        mIsInit = true;
        if (mIsVisible)
            onLazyLoad();
        //
        initViews(view, savedInstanceState);
    }


    protected boolean isFinishLazyLoad() {
        return false;
    }

    /**
     * 当页面已经初始化好,即 mIsInit = true,并且mIsVisible = true;的时候调用该方法
     */
    public void onLazyLoad() {
        LogUtil.LLJi("mIsInit:" + mIsInit + ",mIsVisible:" + mIsVisible);
    }

    /**
     * 初始化本地数据
     */
    protected void initViews(View view, Bundle savedInstanceState) {
    }

    public void onBackPressed() {
        ActivityAnimUtil.finishActivityPushOutRight(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // mPreView = getView();
    }
}
