package com.hlk.ythtwl.msgr.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.hlk.ythtwl.msgr.application.App;
import com.hlk.ythtwl.msgr.helper.LogHelper;

/**
 * <b>功能描述：</b>Activity 基类<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/06 16:55 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/06 16:55 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.app().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        App.app().removeActivity(Integer.toHexString(this.hashCode()));
        log(String.format("Activity '%s' is now destroy", getClass().getSimpleName()));
        super.onDestroy();
    }

    protected void log(String string) {
        LogHelper.log(getClass().getSimpleName(), string);
    }

}
