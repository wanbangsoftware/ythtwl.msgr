package com.hlk.ythtwl.msgr.core;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 21:37 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 21:37 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Core {

    static {
        System.loadLibrary("ythtwl-core");
    }

    public native String accountDebug();

    public native String passwordDebug();

    public native String accountRelease();

    public native String passwordRelease();
}
