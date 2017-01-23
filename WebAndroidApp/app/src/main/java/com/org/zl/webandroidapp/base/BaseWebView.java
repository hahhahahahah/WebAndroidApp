package com.org.zl.webandroidapp.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.org.zl.webandroidapp.R;


/**
 * 作者：朱亮 on 2017/1/20 19:54
 * 邮箱：171422696@qq.com
 * ${通用BaseVebView，html初始自适应大小，可根据情况设置放大缩小}(这里用一句话描述这个方法的作用)
 */

public class BaseWebView extends RelativeLayout {
    private Context mContext;
    private WebView webView;
    private ProgressBar progressBar;//加载进度

    public BaseWebView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }
    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }
    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_webview, this);
        webView = (WebView) view.findViewById(R.id.view_webView);
        progressBar = (ProgressBar) view.findViewById(R.id.view_webview_progress);
        initWebViewSet();
    }
    /**
     * 初始化WebView设置(里面是适配的关键配置)
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSet() {
        // 设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        // User settings

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);//关键点
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setJavaScriptEnabled(true); // 设置支持javascript脚本
        webView.getSettings().setAllowFileAccess(true); // 允许访问文件
        webView.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮

        webView.getSettings().setLoadWithOverviewMode(true);
        DisplayMetrics metrics = new DisplayMetrics();
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        // 重新WebView加载URL的方法
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });
    }

    /**
     * 获取WebView实例
     * @return
     */
    public WebView getWebView(){
        return webView;
    }
    /**
     *   这个自定义web是否支持缩放（暂时测试只有图片可以缩放，网页自适应）
     */
    public void setZoom(boolean flag){
        webView.getSettings().setSupportZoom(flag);
    }

    //web 清空，避免内存泄露
    public  void webDestory(){
        if (webView != null) {
            Log.e("LOGSe","web 清空");
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
