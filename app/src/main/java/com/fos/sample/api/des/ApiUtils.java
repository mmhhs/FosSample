package com.fos.sample.api.des;

import com.fos.fosmvp.common.http.Api;
import com.fos.fosmvp.common.http.EncryptListener;
import com.fos.fosmvp.common.utils.LogUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by wzw on 2019/3/22.
 */

public class ApiUtils {
    //数据加密进行传输
    public static String lockData(Map map) {
        String jsonParam = setHttpJsonString(map);
        LogUtils.e("jsonParam= "+jsonParam);
        String encryptStr = "";
        try {
            encryptStr = DES3.encode(jsonParam,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    //数据加密进行传输
    public static String lockImageData(String argString) {
        String jsonParam = setHttpJsonParams(argString);
        String encryptStr = "";
        try {
            encryptStr = DES3.encode(jsonParam,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    //数据加密
    public static String encodeData(String s) {
        String str = "";
        try {
            str = DES3.encode(s,0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //数据解密
    public static String decodeData(String s) {
        String str = "";
        try {
            str = DES3.formatResultString(DES3.decode(s,0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //map转json
    public static String setHttpJsonString(Map<String, Object> argsMap){
        String jsonParams = "";
        LimitEntity limitEntity = new LimitEntity();
        limitEntity.setUid("");
        limitEntity.setApplicationType("");
        String limitStr = new Gson().toJson(limitEntity);
        String paramsStr = new JSONObject(argsMap).toString();
        jsonParams = "{"+"\"limit\":"+limitStr+",\"param\":"+paramsStr+"}";
        return jsonParams;
    }

    //map转json
    public static String setHttpJsonParams(String argsString){
        String jsonParams = "";
        LimitEntity limitEntity = new LimitEntity();
        limitEntity.setApplicationType("");
        limitEntity.setUid("");
        String limitStr = new Gson().toJson(limitEntity);
        String paramsStr = argsString;
        jsonParams = "{"+"\"limit\":"+limitStr+",\"param\":"+paramsStr+"}";
        return jsonParams;
    }

    public static void setApi(){
        Api.setJsonKey("jsonParame");
        Api.setEncryptListener(new EncryptListener() {
            @Override
            public String onEncrypt(Map<String, Object> map) {
                return encodeData(""+map.get("jsonParame"));
            }

            @Override
            public String onDecrypt(String s) {
                return decodeData(s);
            }
        });
    }
}
