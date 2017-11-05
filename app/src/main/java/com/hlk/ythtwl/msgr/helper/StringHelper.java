package com.hlk.ythtwl.msgr.helper;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hlk.ythtwl.msgr.application.App;

import java.util.Date;
import java.util.Locale;

/**
 * <b>功能：</b>常用的string相关方法集合<br />
 * <b>作者：</b>Hsiang Leekwok <br />
 * <b>时间：</b>2016/01/14 09:39 <br />
 * <b>邮箱：</b>xiang.l.g@gmail.com <br />
 */
public class StringHelper {

    public static String getString(int resId) {
        if (0 == resId)
            return null;
        try {
            return App.app().getResources().getString(resId);
        } catch (Exception ignore) {
            return null;
        }
    }

    public static String getString(int resId, Object... formatArgs) {
        return App.app().getResources().getString(resId, formatArgs);
    }

    public static String[] getStringArray(int resid) {
        return App.app().getResources().getStringArray(resid);
    }

    public static int getInteger(int resId) {
        return App.app().getResources().getInteger(resId);
    }

    public static String format(String fmt, Object... args) {
        // 参数列表为空时直接返回待格式化的字符串
        if (null == args || args.length < 1) return fmt;
        return String.format(Locale.getDefault(), fmt, args);
    }

    /**
     * 将json的“,”替换成“@”以便传输到intent里去
     *
     * @param string 字符串
     * @param toJson true=反转回json，false=替换掉json中的","为"@"
     */
    public static String replaceJson(String string, boolean toJson) {
        return string.replace((toJson ? "@" : ","), (toJson ? "," : "@"));
    }

    /**
     * 获取当前系统时间戳
     */
    public static long timestamp() {
        return new Date().getTime();
    }

    /**
     * 判断字符串是否为空，也即(null == value) or (value.length = 0)
     */
    public static boolean isEmpty(@Nullable String value) {
        return isEmpty(value, false);
    }

    /**
     * 判断字符串是否为空，字符串"null"也当作空，也即(null == value) or (value.length = 0) or (value == "null")
     */
    public static boolean isEmpty(@Nullable String value, boolean nullable) {
        return nullable ? (TextUtils.isEmpty(value) || value.toLowerCase().equals("null"))
                : TextUtils.isEmpty(value);
    }

    /**
     * 判断字符串是否为空的Json Array，如"[]"
     */
    public static boolean isEmptyJsonArray(String string) {
        return isEmpty(string) || string.equals("[]");
    }


    /**
     * 将文本中的空格、换行替换成html代码
     */
    public static String escapeToHtml(String text) {
        if (isEmpty(text)) return "";
        // 先替换空格，再替换换行
        return replaceAll(replaceAll(text, " ", "&nbsp;"), "\n", "<br/>");
    }

    /**
     * 将html中的空格、换行替换成文本
     */
    public static String escapeFromHtml(String html) {
        if (isEmpty(html)) return "";
        return replaceAll(replaceAll(html, "&nbsp;", " "), "<br/>", "\n");
    }

    /**
     * 将文本中的json字符串进行转义
     */
    public static String escapeJson(String text) {
        if (isEmpty(text)) return "";
        return replaceAll(replaceAll(text, "\\\\", "\\\\\\\\"), "\"", "\\\\\"");
    }

    /**
     * 进行文本替换
     *
     * @param text 文本内容
     * @param from 被替换的文本
     * @param to   替换成的文本
     */
    public static String replaceAll(String text, String from, String to) {
        if (isEmpty(text)) return "";
        return text.replaceAll(from, to);
    }
}
