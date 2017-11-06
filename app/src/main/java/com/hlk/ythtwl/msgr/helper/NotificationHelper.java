package com.hlk.ythtwl.msgr.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.application.App;
import com.hlk.ythtwl.msgr.ui.MainActivity;

/**
 * <b>功能描述：</b>系统通知<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/05/25 17:59 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>Version: 1.0.0 <br />
 * <b>修改时间：</b>2017/05/25 17:59 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */

public class NotificationHelper {

    private static final int ID = 0x00FF00FF;

    public static NotificationHelper helper(Context context) {
        return new NotificationHelper(context);
    }

    private NotificationManager manager;
    private NotificationCompat.Builder builder;
    private Context context;

    private NotificationHelper(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(context, context.getString(R.string.app_name));
    }

    private static final long[] vibrate = new long[]{0L, 100L, 100L, 200L};
    private static final long[] mute = new long[]{0L};

    public void show(String title, String text, Intent extras) {
        PendingIntent pendingIntent = PendingIntent.getActivity(context, ID, MainActivity.getIntent(context, extras), PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setOngoing(false)
                .setLights(Color.RED, 0, 0)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(App.vibrateFlab ? vibrate : mute)
                .setSound(App.soundFlag ? Uri.parse(App.sound) : null);

        manager.notify(ID, builder.build());
    }
}
