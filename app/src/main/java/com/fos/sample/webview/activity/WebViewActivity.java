package com.fos.sample.webview.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.baidu.speech.utils.LogUtil;
import com.fos.fosmvp.common.base.BaseActivity;
import com.fos.sample.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import static android.view.View.GONE;


public class WebViewActivity extends BaseActivity {

    WebView webView;
    ProgressBar progressBar;//进度条
    private boolean isExit = false;
    private String path = "https://wms.foton.com.cn/rf";

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView(Bundle savedInstanceState) {




        initWebView();//初始化webview


    }

    private void bindView(){
        webView = (WebView)findViewById(R.id.web_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    /**
     * 初始化webviewF
     */
    private void initWebView() {
        OptionUtil.setWebSettingScale(webView);
        //网页调用APP方法
        webView.addJavascriptInterface(this, "nativeMethod");
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);


        readHtml();
    }


    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            isExit = true;
            //解决第二次进入无法跳转问题
            if (webView != null) {
                webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
                webView.clearHistory();
                webView.getSettings().setJavaScriptEnabled(false);
                webView.setVisibility(GONE);
                webView.removeAllViews();
                webView.destroy();
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeView(webView);
                }
                webView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void readHtml() {
        try {
            webView.clearHistory();
            webView.loadUrl(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    WebChromeClient webChromeClient = new WebChromeClient() {
        boolean selectOpt = false;//是否选择

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            //super.onGeolocationPermissionsShowPrompt(origin, callback);
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (!isExit) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() == View.GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }




    };


    WebViewClient webViewClient = new WebViewClient(){
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (error.getPrimaryError() == SslError.SSL_INVALID||error.getPrimaryError() == SslError.SSL_NOTYETVALID
                    ||error.getPrimaryError() == SslError.SSL_EXPIRED
                    ||error.getPrimaryError() == SslError.SSL_IDMISMATCH
                    ||error.getPrimaryError() == SslError.SSL_UNTRUSTED
                    ||error.getPrimaryError() == SslError.SSL_DATE_INVALID
                    ) {
                handler.proceed();
            } else {
                handler.cancel();
            }
        }
        //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.e("current url is " + url);
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            }
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            webView.loadUrl(url);
            return true;
        }
        //在页面加载开始时调用
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            viewTool.addLoadView(ActWebActivity.this, getString(R.string.task2), containLayout,
//                    loadLayout);
//            containLayout.setVisibility(View.VISIBLE);
            if (!isExit) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                super.onPageStarted(view, url, favicon);
            }

        }

        //在页面加载结束时调用
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if (containLayout != null && loadLayout != null) {
//                viewTool.removeLoadView(containLayout, loadLayout);
//            } else {
//                loadLayout.setVisibility(GONE);
//            }
        }
    };




    //网页调用APP方法
    @JavascriptInterface
    public void jumpClick(String androidClassName) {
        try {
            LogUtil.e("androidClassName= "+androidClassName);
            Intent intent = new Intent(this,Class.forName(androidClassName));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //网页调用APP方法 shareStyle:0:普通，1：二维码
    @JavascriptInterface
    public void qrClick() {
        try {
            Intent intent = new Intent(WebViewActivity.this, CaptureActivity.class);
            startActivityForResult(intent, 11);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jsFun(String v){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            webView.loadUrl("javascript:callJS("+v+")");
        } else {
            webView.evaluateJavascript("javascript:callJS("+v+")", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果

                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 11:
                    //处理扫描结果（在界面上显示）
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            jsFun(result);
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {

                        }
                    }
                    break;
            }

        }
    }


}
