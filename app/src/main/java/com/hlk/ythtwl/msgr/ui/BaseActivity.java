package com.hlk.ythtwl.msgr.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.hlk.ythtwl.msgr.application.App;
import com.hlk.ythtwl.msgr.helper.LogHelper;
import com.hlk.ythtwl.msgr.helper.StringHelper;

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

    /**
     * 转到拨号界面，不直接拨打
     */
    public void dialPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 跳蛛拿到拨号界面并且直接拨打电话
     */
    @SuppressLint("MissingPermission")
    public void dialPhoneDirectly(String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    protected void log(String string) {
        LogHelper.log(getClass().getSimpleName(), string);
    }

    protected static boolean isEmpty(String string) {
        return StringHelper.isEmpty(string, true);
    }

    /**
     * 列表滚动到最后一条记录
     */
    protected void smoothScrollToBottom(final RecyclerView recyclerView, final int position) {
        if (position < 0) return;
        if (null != recyclerView) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(position);
                }
            }, 100);
        }

    }
}
