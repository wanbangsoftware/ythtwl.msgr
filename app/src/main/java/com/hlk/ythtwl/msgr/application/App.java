package com.hlk.ythtwl.msgr.application;

import com.hlk.ythtwl.msgr.crash.AppCrashHandler;

/**
 * <b>功能描述：</b>基类<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 13:24 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 13:24 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class App extends Nim {

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppCrashHandler.getInstance(this);
        initializeNim();
    }

    private static App instance;

    /**
     * 获取全局Application实例
     */
    public static App app() {
        return instance;
    }

}
