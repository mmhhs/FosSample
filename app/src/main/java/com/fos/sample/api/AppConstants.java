package com.fos.sample.api;

import com.fos.fosmvp.start.FosMvpManager;

import java.util.List;

/**
 * 配置类
 * Created by xxjsb on 2019/3/5.
 */

public class AppConstants {
    /** 接口访问路径前缀 */
    public static String PREFIX_URL = "https://bfda-app.ifoton.com.cn/est/";

    /** 是否调试 */
    public static boolean DEBUGGING = true;

    /** 应用类型 */
    public String appType = "lt_ftej";

    /** 用户ID */
    public String userId = "";
    /** 昵称 */
    public String userNick = "";
    /** 头像 */
    public String userAvatar = "";

    /** 版块集合 */
    public List blockList;


    /**
     * 初始化
     */
    public static void init(){
        FosMvpManager.init(AppConstants.PREFIX_URL);
        FosMvpManager.setDEBUGGING(DEBUGGING);
    }

    /**
     * 配置基本信息
     * @param appType
     * @param userId
     * @param userNick
     * @param userAvatar
     */
    public void setAppInfo(String appType,String userId,String userNick,String userAvatar) {
        this.appType = appType;
        this.userId = userId;
        this.userNick = userNick;
        this.userAvatar = userAvatar;
    }

    public static boolean isDEBUGGING() {
        return DEBUGGING;
    }

    public String getAppType() {
        return appType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserNick() {
        return userNick;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public List getBlockList() {
        return blockList;
    }

    public static String getPrefixUrl() {
        return PREFIX_URL;
    }

    public static void setPrefixUrl(String prefixUrl) {
        PREFIX_URL = prefixUrl;
    }

    public void setBlockList(List blockList) {
        this.blockList = blockList;
    }
}
