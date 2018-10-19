package com.fos.sample.webview.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OptionUtil {


    public static String tag = "OptionUtil";

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
        int code = 1;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            code = info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 获取版本名称
     *
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        String name = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    //获取IP地址
    public static String getNetIp() {
        //TODO 获取ip地址异常
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
//            infoUrl = new URL("http://ip168.com/");
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strber.append(line + "\n");
                }
                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
                httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ipLine;
    }

    /**
     * 隐藏键盘
     *
     * @param activity
     */
    public static void closeKeyBoard(Activity activity) {

        try {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            ((InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus()
                                    .getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开键盘
     *
     * @param activity
     */
    public static void openKeyBoard(Handler handler, final Activity activity, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, delay);
    }

    /**
     * 打开应用设置
     *
     * @param context
     */
    public static void setting(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拨打电话
     *
     * @param context
     */
    public static void call(Context context, final String tel) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送短信
     *
     * @param context
     */
    public static void sendMsg(Context context, final String msg) {
        try {
            Intent intentFinalMessage = new Intent(Intent.ACTION_VIEW);
            intentFinalMessage.setType("vnd.android-dir/mms-sms");
            intentFinalMessage.putExtra("sms_body", msg);
            context.startActivity(intentFinalMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getBrandName(String brandId) {
        String brand = "";
        switch (brandId) {
            case "-1":
                brand = "全品牌";
                break;
            case "1000":
                brand = "欧曼";
                break;
            case "1007":
                brand = "拓陆者";
                break;
            case "1009":
                brand = "奥铃";
                break;
            case "1011":
                brand = "蒙派克";
                break;
            case "1026":
                brand = "欧马可";
                break;
            case "1029":
                brand = "伽途";
                break;
            case "1030":
                brand = "萨瓦纳";
                break;
            case "1035":
                brand = "时代";
                break;
            case "1036":
                brand = "瑞沃";
                break;
            case "1068":
                brand = "图雅诺";
                break;
            case "1069":
                brand = "萨普";
                break;
            case "1074":
                brand = "迷迪";
                break;
            case "1075":
                brand = "雷萨";
                break;
            case "1076":
                brand = "欧辉";
                break;
            case "1079":
                brand = "风景";
                break;
        }
        return brand;
    }

    /**
     * 获取屏幕宽度px
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        int screenWidth = 720;
        try {
            screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    /**
     * 获取屏幕高度px
     *
     * @param activity
     * @return
     */

    public static int getScreenHeight(Activity activity) {
        int screenHeight = 0;
        try {
            screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }


    public static int getStatusBarHeight(Activity activity) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 38;//默认为38，貌似大部分是这样的

        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = activity.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }




    public static void setBaseWebSetting(WebView webView) {
        WebSettings webSettings = webView.getSettings();
        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点
        webView.requestFocusFromTouch();
        webView.requestFocus();
        //打开页面时， 自适应屏幕
        webSettings.setUseWideViewPort(true);//关键点 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
//		webSettings.setBlockNetworkImage(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本op
        //定位
        webSettings.setGeolocationEnabled(true);
        //其他
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//支持内容重新布局
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);  //webview中缓存
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.supportMultipleWindows();  //多窗口
        // 开启 DOM storage API 功能?
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能?
        webSettings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setDefaultTextEncodingName("UTF-8");// 设置默认编码
//		webSettings.setUserAgentString(Application.getUserAgent());
//		String cacheDirPath = BaseConstant.IMAGETAMPPATH;
        //设置? Application Caches 缓存目录?
//		webSettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能?
        webSettings.setAppCacheEnabled(true);

        //设置浏览器标识
//        webSettings.setUserAgentString("foton_almighty");


//        webSettings.setAllowFileAccessFromFileURLs(true);
//        webSettings.setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        } else {
            try {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webSettings, true);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置webview 不支持缩放
     *
     * @param webView
     */
    public static void setWebSetting(WebView webView) {
        setBaseWebSetting(webView);
        WebSettings webSettings = webView.getSettings();
        //页面支持缩放
        webSettings.setBuiltInZoomControls(false); // 设置显示缩放按钮
        webSettings.setSupportZoom(false); // 支持缩放

    }

    /**
     * 设置webview 支持缩放
     *
     * @param webView
     */
    public static void setWebSettingScale(final WebView webView) {
        setBaseWebSetting(webView);
        WebSettings webSettings = webView.getSettings();
        //页面支持缩放
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setDisplayZoomControls(false);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                webView.getContext().startActivity(intent);
            }
        });
    }


}