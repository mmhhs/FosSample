package com.fos.sample.api;

import com.fos.fosmvp.start.FosMvpManager;
import com.fos.sample.api.des.ApiUtils;
import com.little.picture.PictureStartManager;

import java.io.File;

/**
 * 配置类
 * Created by xxjsb on 2019/3/5.
 */

public class AppConstants {
    private static AppConstants instance;
    /** 接口访问路径前缀 */
    public static String PREFIX_URL = "https://bfda-app.ifoton.com.cn/est/";

    /** 是否调试 */
    public static boolean DEBUGGING = true;

    //图片组件初始参数
    public static int SCALE_WIDTH = 1080;//缩放至的宽度
    public static int SCALE_HEIGHT = 1080;//缩放至的高度
    public static int QUALITY = 50;//图像质量
    public static String AUTHORITY = "com.fos.scan.fileprovider";//7.0以上调取相机相册需要 （福田e家：com.foton.almighty.fileprovider）
    public static String IMAGE_FOLDER = "";//存储图片的文件夹


    public AppConstants() {
        IMAGE_FOLDER = AppApplication.getAppContext().getExternalFilesDir("")+"/cache/image/";
        File imFolder = new File(IMAGE_FOLDER);
        if (!imFolder.exists()){
            imFolder.mkdirs();
        }
    }

    public static AppConstants getInstance() {
        if (instance == null) {
            synchronized (AppConstants.class) {
                if (instance == null) {
                    instance = new AppConstants();
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public static void init(){
        FosMvpManager.init(AppConstants.PREFIX_URL);
        PictureStartManager.getInstance().init(SCALE_WIDTH,SCALE_HEIGHT,QUALITY,AUTHORITY,IMAGE_FOLDER);
        FosMvpManager.setDEBUGGING(DEBUGGING);
        ApiUtils.setApi();
    }



    public static boolean isDEBUGGING() {
        return DEBUGGING;
    }


}
