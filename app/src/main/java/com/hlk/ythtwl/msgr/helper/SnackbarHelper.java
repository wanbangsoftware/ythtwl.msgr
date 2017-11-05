package com.hlk.ythtwl.msgr.helper;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.lang.ref.SoftReference;

/**
 * <b>功能描述：</b><br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 20:27 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 20:27 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class SnackbarHelper {

    private SoftReference<View> softReference;

    private SnackbarHelper(View view) {
        softReference = new SoftReference<>(view);
    }

    public static SnackbarHelper make(View view) {
        return new SnackbarHelper(view);
    }

    public void show(int res) {
        show(StringHelper.getString(res));
    }

    public void show(String text) {
        Snackbar.make(softReference.get(), text, Snackbar.LENGTH_LONG).show();
    }
}
