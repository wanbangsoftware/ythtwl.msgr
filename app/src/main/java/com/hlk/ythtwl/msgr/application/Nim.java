package com.hlk.ythtwl.msgr.application;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Parcelable;

import com.google.gson.reflect.TypeToken;
import com.hlk.hlklib.etc.Cryptography;
import com.hlk.ythtwl.msgr.R;
import com.hlk.ythtwl.msgr.helper.LogHelper;
import com.hlk.ythtwl.msgr.helper.NotificationHelper;
import com.hlk.ythtwl.msgr.helper.PreferenceHelper;
import com.hlk.ythtwl.msgr.helper.StringHelper;
import com.hlk.ythtwl.msgr.helper.ToastHelper;
import com.hlk.ythtwl.msgr.listener.OnMsgrEventListener;
import com.hlk.ythtwl.msgr.model.Json;
import com.hlk.ythtwl.msgr.model.Point;
import com.hlk.ythtwl.msgr.notification.Msgr;
import com.hlk.ythtwl.msgr.ui.MainActivity;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <b>功能描述：</b>网易云信application<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 13:24 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 13:24 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Nim extends BAM {

    protected void initializeNim() {
        readNimMessageNotify();
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), options());
        if (shouldInit()) {
            handleUserOnlineStatus();
            // 初始化个性化数据库
            initializeLiteOrm(account());
            // 注册自定义推送通知接收程序
            registerCustomNotification();
        }
    }

    /**
     * 声音、震动提醒
     */
    public static boolean soundFlag = false, vibrateFlab = false;

    /**
     * 读取本地设置的消息通知方式
     */
    public static void readNimMessageNotify() {
        String sound = PreferenceHelper.get(R.string.preference_sound_flag, "");
        String vibrate = PreferenceHelper.get(R.string.preference_vibrate_flag, "");
        soundFlag = isEmpty(sound) || sound.equals("1");
        vibrateFlab = isEmpty(vibrate) || vibrate.equals("1");
    }

    /**
     * 重置 Nim 消息通知方式
     */
    public static void resetNimMessageNotify(boolean sound, boolean vibrate) {
        soundFlag = sound;
        vibrateFlab = vibrate;
        PreferenceHelper.save(R.string.preference_sound_flag, (sound ? "1" : "0"));
        PreferenceHelper.save(R.string.preference_vibrate_flag, (vibrate ? "1" : "0"));
        NIMClient.updateStatusBarNotificationConfig(getNotificationConfig());
    }

    public static final String sound = "android.resource://com.hlk.ythtwl.msgr/raw/msg";

    private static StatusBarNotificationConfig getNotificationConfig() {
        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = MainActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 是否需要震动
        config.vibrate = vibrateFlab;
        // 是否需要响铃
        config.ring = soundFlag;
        // 通知铃声的uri字符串
        config.notificationSound = sound;
        return config;
    }

    private SDKOptions options() {
        SDKOptions options = new SDKOptions();
        options.statusBarNotificationConfig = getNotificationConfig();

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" + ROOT_DIR + "/nim";

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = getImageMaxEdge();

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId, SessionTypeEnum sessionType) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(SessionTypeEnum sessionType, String sessionId) {
                return null;
            }
        };
        return options;
    }

    private int getImageMaxEdge() {
        return getResources().getDisplayMetrics().widthPixels / 2;
    }

    protected static int kickTimes = 0;

    private void handleUserOnlineStatus() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            public void onEvent(StatusCode status) {
                LogHelper.log("NimApp", "User status changed to: " + status);
                if (status.wontAutoLogin()) {
                    StatusCode sc = StatusCode.typeOfValue(status.getValue());
                    if (sc == StatusCode.PWD_ERROR) {
                        ToastHelper.make(Nim.this).showMsg(R.string.ui_text_nim_pwd_error);
                    } else if (sc == StatusCode.FORBIDDEN) {
                        ToastHelper.make(Nim.this).showMsg(R.string.ui_text_nim_forbidden);
                    } else {
                        // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                        ToastHelper.make(Nim.this).showMsg(R.string.ui_text_nim_kick_out);
                        if (kickTimes <= 5) {
                            // 超过5次之后不再重新登录
                            kickTimes++;
                            doLogin();
                        }
                    }
                } else if (status.shouldReLogin()) {
                    if (StatusCode.typeOfValue(status.getValue()) == StatusCode.NET_BROKEN) {
                        ToastHelper.make(Nim.this).showMsg(R.string.ui_text_nim_net_broken);
                    }
                }
            }
        }, true);
    }

    @SuppressWarnings("unchecked")
    private void doLogin() {
        NIMClient.getService(AuthService.class).login(loginInfo()).setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                ToastHelper.make().showMsg(R.string.ui_text_sing_nim_success);
            }

            @Override
            public void onFailed(int i) {
                ToastHelper.make().showMsg(StringHelper.getString(R.string.ui_text_sign_nim_failed, i));
            }

            @Override
            public void onException(Throwable throwable) {
                ToastHelper.make().showMsg(StringHelper.getString(R.string.ui_text_sing_nim_exception, throwable.getMessage()));
            }
        });
    }

    public String account() {
        return releasable() ? getString(R.string.app_release_user_id) : "development";
    }

    private String token() {
        return Cryptography.md5(account()).toLowerCase(Locale.getDefault());
        //releasable() ? "7edb18ad781a0dfa7ea1bcc1da5ad802" : "759b74ce43947f5f4c91aeddc3e5bad3";
    }

    private LoginInfo loginInfo() {
        // boss_user: 7edb18ad781a0dfa7ea1bcc1da5ad802
        // development: 759b74ce43947f5f4c91aeddc3e5bad3
        // service: aaabf0d39951f3e6c3e8a7911df524c2
        return new LoginInfo(account(), token().substring(0, 20));
    }

    private void registerCustomNotification() {
        // 如果有自定义通知是作用于全局的，不依赖某个特定的 Activity，那么这段代码应该在 Application 的 onCreate 中就调用
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
            @Override
            public void onEvent(CustomNotification message) {
                // log("notification: " + message.getContent() + " from :" + message.getSessionId() + "/" + message.getSessionType());
                // 在这里处理自定义通知。
                String json = message.getContent();
                if (!isEmpty(json)) {
                    log(json);
                    // {"id":1511182986,"license":"鲁F64942","latitude":37.561668,"longitude":121.18732,"status":2,"begin":"2017-11-20 21:05:00","times":756,"name1":"王撕葱","phone1":"13999999999","name2":"王建玲","phone2":"13999999998"}
                    Msgr msg = Msgr.fromJson(json);
                    if (null != msg) {
                        if (msg.getPoints().size() > 0) {
                            for (Point point : msg.getPoints()) {
                                // id
                                point.setId(System.currentTimeMillis() + point.getStop());
                            }
                        }
                        Msgr.save(msg);
                        if (isAppStayInBackground || !isAppOnForeground(Nim.this)) {
                            // 如果app已经隐藏到后台，则需要打开通过系统通知来提醒用户
                            Intent extra = new Intent().putExtra(MainActivity.EXTRA_NOTIFICATION, (Parcelable) msg);
                            NotificationHelper.helper(Nim.this).show(getString(R.string.app_name), getString(R.string.ui_notification_content), extra);
                        }
                        //dispatchCallbacks();
                        dispatchEvents(msg);
                    }
                }
            }
        }, true);
    }

    private static ArrayList<OnMsgrEventListener> msgrEventListeners = new ArrayList<>();

    /**
     * 注册、添加云信消息处理回调
     */
    public static void addMsgrEventListener(OnMsgrEventListener l) {
        if (!msgrEventListeners.contains(l)) {
            msgrEventListeners.add(l);
        }
    }

    /**
     * 移除云信消息处理回调
     */
    public static void removeMsgrEventListener(OnMsgrEventListener l) {
        msgrEventListeners.remove(l);
    }

    /**
     * 按照列表挨个通知回调处理消息到达事件
     */
    private static void dispatchEvents(Msgr message) {
        for (OnMsgrEventListener event : msgrEventListeners) {
            event.onEvent(message);
        }
    }

}
