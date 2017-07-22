package com.common.library.llj.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.common.library.llj.listener.OnMyClickListener;
import com.common.library.llj.views.ProgressWebView;

import java.io.File;

/**
 * Created by liulj on 2016/11/22.
 * WEBVIEW utils
 *
 */

public class WebViewUtil {
    /**
     * @param webView
     */
    public static void initWebViewSetting(final WebView webView) {
        WebSettings webSettings = webView.getSettings();
//        try {
//            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //允许js交互,允许中文编码
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        //Android webview 从Lollipop(5.0)开始webview默认不允许混合模式，https当中不能加载http资源，需要设置开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setHapticFeedbackEnabled(false);
        webView.setHapticFeedbackEnabled(false);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                IntentUtil.goToUpdate(webView.getContext(), url);
            }
        });
    }


    public static final String ALIPAY_SCHEME = "alipays";

    public static boolean shouldOverrideIntentUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        if (uri != null
                && uri.getScheme() != null
                && uri.getScheme().equals(ALIPAY_SCHEME)) {
            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setComponent(null);
                intent.setSelector(null);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                if (e instanceof ActivityNotFoundException) {
                    ToastUtil.show("未检测到支付宝客户端");
                }
                return true;
            }
            return true;
        }
        return false;
    }


    //处理返回键
    public static void handleWithBack(final WebView webView) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            ActivityAnimUtil.finishActivityPushOutRight(webView.getContext());
        }
    }


    /**
     * @param progressWebView
     * @param view
     */
    public static void setOnBackClick(final ProgressWebView progressWebView, View view) {
        view.setOnClickListener(new OnMyClickListener() {
            @Override
            public void onCanClick(View v) {
                handleWithBack(progressWebView);
            }
        });
    }
}
