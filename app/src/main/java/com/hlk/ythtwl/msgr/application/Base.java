package com.hlk.ythtwl.msgr.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.hlk.ythtwl.msgr.BuildConfig;
import com.hlk.ythtwl.msgr.helper.LogHelper;
import com.hlk.ythtwl.msgr.helper.StringHelper;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * <b>功能描述：</b>Application基类方法集合<br />
 * <b>创建作者：</b>Hsiang Leekwok <br />
 * <b>创建时间：</b>2017/11/05 13:57 <br />
 * <b>作者邮箱：</b>xiang.l.g@gmail.com <br />
 * <b>最新版本：</b>version: 1.0.0 <br />
 * <b>修改时间：</b>2017/11/05 13:57 <br />
 * <b>修改人员：</b><br />
 * <b>修改备注：</b><br />
 */
public class Base extends Application {

    private static final String TAG = "App";

    protected static void log(String text) {
        LogHelper.log(TAG, text);
    }

    /**
     * App是否转入后台运行
     */
    protected boolean isAppStayInBackground = false;

    /**
     * 设置app是否转入后台运行
     */
    public void setAppStayInBackground(boolean background) {
        isAppStayInBackground = background;
    }

    /**
     * 查看app是否在后台运行
     */
    public boolean isAppStayInBackground() {
        return isAppStayInBackground;
    }

    public static final boolean isAppOnForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> list = manager.getRunningAppProcesses();
        if (list == null) {
            return false;
        }
        boolean ret = false;
        Iterator<ActivityManager.RunningAppProcessInfo> it = list.iterator();
        while (it.hasNext()) {
            ActivityManager.RunningAppProcessInfo appInfo = it.next();
            if (appInfo.processName.equals(packageName) && appInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    protected static String sha1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean shouldInit() {
        boolean ret = false;
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        if (null != am) {
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            String mainProcessName = getPackageName();
            int myPid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 是否为release版本
     */
    public static boolean releasable() {
        return BuildConfig.BUILD_TYPE.equals("release");
    }

    /**
     * Uri.fromFile
     */
    public static Uri getUriFromFile(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= 24) {
            File tempFile = new File(filePath);
            return FileProvider.getUriForFile(context, StringHelper.format("%s.fileProvider", BuildConfig.APPLICATION_ID), tempFile);
        } else {
            return Uri.fromFile(new File(filePath));
        }
    }

    /**
     * 获取application中配置的meta-data值
     */
    public String getMetadata(String name) {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前apk的版本号
     */
    public String version() {
        String version = "";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 字符串是否为空
     */
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || value.toLowerCase().equals("null");
    }

    public static final String ROOT_DIR = "ythtwl";
    /**
     * 本地数据库缓存目录
     */
    public static final String DB_DIR = "database";

    /**
     * 获取外挂 SD 卡目录下的 data 目录，末尾不包含/<br>
     * 如果外置SD卡不可读则转为获取内置缓存目录
     */
    public String gotExternalCacheDir() {
        String path = null;
        try {
            // 获取外置SD卡中的缓存目录
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory().getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(path)) {
            // 如果外置SD卡缓存目录获取失败则使用内置私有目录
            path = gotCacheDir();
        }
        return path;
    }

    /**
     * 获取内置SD卡中app私有存储空间
     */
    public String gotCacheDir() {
        return getCacheDir().toString();
    }

    /**
     * 在外置缓存中获取指定的目录路径，末尾包含/
     */
    public String getCachePath(String dir) {
        StringBuilder sb = new StringBuilder();
        if (releasable() && dir.equals(DB_DIR)) {
            // release时，db文件放在内置私有目录下
            sb.append(gotCacheDir());
        } else {
            //if (dir.equals(DB_DIR)) {
            // 缓存数据库和图像都存在内置app私有空间里
            //    sb.add(gotCacheDir());
            //} else {
            sb.append(gotExternalCacheDir());
            //}
        }
        sb.append("/").append(ROOT_DIR).append("/").append(dir).append("/");
        createDirs(sb.toString());
        return sb.toString();
    }

    /**
     * 创建指定的文件目录
     */
    private synchronized void createDirs(String dirs) {
        File file = new File(dirs);
        // 查看文件目录是否存在，不存在则创建
        if (!file.exists()) {
            file.mkdirs();
        }
//        if (dirs.endsWith(IMAGE_DIR + "/") || dirs.endsWith(HTML_DIR + "/") || dirs.endsWith(CROPPED_DIR + "/") || dirs.endsWith(CAMERA_DIR + "/")) {
//            // 本地图片文件夹增加 .nomedia
//            String path = dirs + NOMEDIA;
//            File f = new File(path);
//            if (!f.exists()) {
//                try {
//                    f.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

}
