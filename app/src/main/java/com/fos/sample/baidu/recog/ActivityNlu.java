package com.fos.sample.baidu.recog;


import com.fos.sample.R;

/**
 * 展示语义功能
 * 本类可以忽略
 */

public class ActivityNlu extends ActivityAbstractRecog {


    public ActivityNlu() {
        super(R.raw.offline_recog, true);
        // uiasr\src\main\res\raw\nlu_recog.txt 本Activity使用的说明文件
        // true 表示activity支持离线
    }


}
