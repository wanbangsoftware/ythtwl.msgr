package com.hlk.ythtwl.msgr.helper;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.HashMap;

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

    private Snackbar snackbar;
    private CharSequence text;
    private HashMap<CharSequence, View.OnClickListener> events = new HashMap<>();

    private SnackbarHelper(View view) {
        softReference = new SoftReference<>(view);
    }

    public static SnackbarHelper make(View view) {
        return new SnackbarHelper(view);
    }

    public SnackbarHelper setText(int res) {
        text = StringHelper.getString(res);
        return this;
    }

    public SnackbarHelper setText(CharSequence text) {
        this.text = text;
        return this;
    }

    public SnackbarHelper setAction(CharSequence action, View.OnClickListener listener) {
        if (!StringHelper.isEmpty(action.toString()) && null != listener) {
            if (events.size() >= 1) {
                throw new IllegalArgumentException("Only can set one action button.");
            }
            if (!events.containsKey(action)) {
                events.put(action, listener);
            }
        }
        return this;
    }

    public void show() {
        Snackbar snackbar = Snackbar.make(softReference.get(), text, Snackbar.LENGTH_LONG);
        if (!events.isEmpty()) {
            CharSequence[] key = events.keySet().toArray(new CharSequence[events.size()]);
            for (CharSequence cs : key) {
                snackbar.setAction(cs, events.get(cs));
            }
        }
        snackbar.show();
    }
}
