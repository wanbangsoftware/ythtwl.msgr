package com.hlk.ythtwl.msgr.etc;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hlk.ythtwl.msgr.helper.StringHelper;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些常用的方法集合
 * <p/>
 * Created by Hsiang Leekwok on 2015/07/13.
 */
public class Utils {

    public static final String SUFFIX_PNG = "png";
    public static final String SUFFIX_JPG = "jpg";
    public static final String SUFFIX_JPEG = "jpeg";
    /**
     * 缩略图的最大尺寸
     */
    public static final int MAX_THUMBNAIL_SIZE = 300;

    /**
     * 头像按照屏幕宽度缩放比例
     */
    public static final float HEADER_ZOOM_MULTIPLES = 6.0f;
    public static final float CHATING_HEADER_ZOOM_SIZE = 7.5f;

    public static final String FMT_HHMM = "yyyy-MM-dd HH:mm";
    public static final String FMT_MDHM2 = "MM-dd HH:mm";
    public static final String FMT_MDHM = "MM月dd日 HH:mm";
    public static final String FMT_YMD = "yyyy/MM/dd";
    public static final String FMT_YMD2 = "yyyy-MM-dd";
    public static final String FMT_YMD3 = "yyyy年MM月dd日";
    public static final String FMT_YMDHM = "yyyy年MM月dd日HH:mm";
    public static final String FMT_HHMMSS = "yyyy/MM/dd HH:mm:ss";
    public static final String FMT_MMDD = "MM月dd日";
    public static final String FMT_HHMM1 = "HH:mm";
    private static final String FMT_MMDDHHMM = "MM月dd日HH:mm";
    public static final String FMT_YYYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String FMT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static String formatDateTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FMT_YYYYMMDDHHMMSS, Locale.getDefault());
        return sdf.format(date);
    }

    public static String formatDateOfNow(String fmt) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 将字符串格式的时间转换成需要的时间
     *
     * @param text       字符串时间内容
     * @param textFormat 字符串时间格式
     * @param toFormat   要转换成的时间format格式
     */
    public static String format(String text, String textFormat, String toFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(textFormat, Locale.getDefault());
        try {
            return format(toFormat, sdf.parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "格式化错误";
    }

    public static String format(String fmt, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.getDefault());
        return sdf.format(null == date ? new Date() : date);
    }

    public static Date parseDate(String fmt, String source) {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.getDefault());
        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String format(String fmt, long time) {
        return format(fmt, new Date(time));
    }

    public static String format(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long h = time / HOUR;
        long m = time % HOUR / MINUTE;
        long s = time % MINUTE / SECOND;
        long ms = time % SECOND;
        return String.format(Locale.getDefault(), "%02d:%02d:%02d.%03d", h, m, s, ms);
    }

    // 2004-06-14T19:GMT20:30Z
    // 2004-06-20T06:GMT22:01Z

    // http://www.cl.cam.ac.uk/~mgk25/iso-time.html
    //
    // http://www.intertwingly.net/wiki/pie/DateTime
    //
    // http://www.w3.org/TR/NOTE-datetime
    //
    // Different standards may need different levels of granularity in the date and
    // time, so this profile defines six levels. Standards that reference this
    // profile should specify one or more of these granularities. If a given
    // standard allows more than one granularity, it should specify the meaning of
    // the dates and times with reduced precision, for example, the result of
    // comparing two dates with different precisions.

    // The formats are as follows. Exactly the components shown here must be
    // present, with exactly this punctuation. Note that the "T" appears literally
    // in the string, to indicate the beginning of the time element, as specified in
    // ISO 8601.

    //    Year:
    //       YYYY (eg 1997)
    //    Year and month:
    //       YYYY-MM (eg 1997-07)
    //    Complete date:
    //       YYYY-MM-DD (eg 1997-07-16)
    //    Complete date plus hours and minutes:
    //       YYYY-MM-DDThh:mmTZD (eg 1997-07-16T19:20+01:00)
    //    Complete date plus hours, minutes and seconds:
    //       YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00)
    //    Complete date plus hours, minutes, seconds and a decimal fraction of a
    // second
    //       YYYY-MM-DDThh:mm:ss.sTZD (eg 1997-07-16T19:20:30.45+01:00)

    // where:

    //      YYYY = four-digit year
    //      MM   = two-digit month (01=January, etc.)
    //      DD   = two-digit day of month (01 through 31)
    //      hh   = two digits of hour (00 through 23) (am/pm NOT allowed)
    //      mm   = two digits of minute (00 through 59)
    //      ss   = two digits of second (00 through 59)
    //      s    = one or more digits representing a decimal fraction of a second
    //      TZD  = time zone designator (Z or +hh:mm or -hh:mm)
    public static Date parseJson(String json) throws ParseException {
        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());

        //this is zero time so we need to add that TZ indicator for
        if (json.endsWith("Z")) {
            json = json.substring(0, json.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = json.substring(0, json.length() - inset);
            String s1 = json.substring(json.length() - inset, json.length());

            json = s0 + "GMT" + s1;
        }

        return df.parse(json);
    }

    /**
     * 将Date对象转换成json字符串
     */
    public static String DateToJson(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());

        TimeZone tz = TimeZone.getTimeZone("UTC");
        df.setTimeZone(tz);

        String output = df.format(date);

        int inset0 = 9;
        int inset1 = 6;

        String s0 = output.substring(0, output.length() - inset0);
        String s1 = output.substring(output.length() - inset1, output.length());

        String result = s0 + s1;

        result = result.replaceAll("UTC", "+00:00");

        return result;
    }

    /**
     * 获取指定日期的开始时间戳
     *
     * @param date  日期
     * @param begin true=指定日期的开始时间，false=指定日期的结束时间
     */
    public static long getDayBeginOrEndInMillis(Date date, boolean begin) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, begin ? 0 : 23);
        calendar.set(Calendar.MINUTE, begin ? 0 : 59);
        calendar.set(Calendar.SECOND, begin ? 0 : 59);
        calendar.set(Calendar.MILLISECOND, begin ? 1 : 990);
        return calendar.getTimeInMillis();
    }

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    public static final long DAY = HOUR * 24;
    private static final String JUST_NOW = "刚刚";
    private static final String MINUTES = "%d分钟前";
    private static final String HALF_HOUR = "半小时前";
    private static final String TODAY = "今天%s";
    private static final String YESTERDAY = "昨天%s";
    private static final String BEFORE_YESTERDAY = "前天%s";

    public static String formatTimeAgo(String fmt, String time) {
        return formatTimeAgo(parseDate(fmt, time));
    }

    public static String formatTimeAgo(Date date) {
        return formatTimeAgo(date.getTime());
    }

    public static String formatTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }
        long now = System.currentTimeMillis();
        long todayBegin = getDayBeginOrEndInMillis(new Date(), true);
        long yesterdayStart = todayBegin - DAY;
        long beforeYesterdayStart = yesterdayStart - DAY;
        if (time > now || time <= 0) {
            return JUST_NOW;
        }
        if (time > todayBegin) {
            // 今天
            final long diff = now - time;
            if (diff < MINUTE) {
                return JUST_NOW;
            } else if (diff < 30 * MINUTE) {
                return StringHelper.format(MINUTES, diff / MINUTE);
            } else if (diff < 59 * MINUTE) {
                return HALF_HOUR;
            } else if (diff < DAY) {
                return StringHelper.format(TODAY, format(FMT_HHMM1, time));
            }
        } else if (time > yesterdayStart) {
            // 昨天
            return StringHelper.format(YESTERDAY, format(FMT_HHMM1, time));
        } else if (time > beforeYesterdayStart) {
            // 前天
            return StringHelper.format(BEFORE_YESTERDAY, format(FMT_HHMM1, time));
        }
        Calendar year = Calendar.getInstance();
        year.set(Calendar.MONTH, Calendar.JANUARY);
        year.set(Calendar.DAY_OF_YEAR, 1);
        year.set(Calendar.HOUR_OF_DAY, 0);
        year.set(Calendar.MINUTE, 0);
        year.set(Calendar.SECOND, 0);
        year.set(Calendar.MILLISECOND, 1);
        long yearBeginning = year.getTimeInMillis();
        if (time > yearBeginning) {
            // 今年的话，则格式化成 xx月xx日
            return format(FMT_MMDDHHMM, time);
        }
        // 去年以及以前的都格式化成 xxxx年xx月xx日
        return format(FMT_YMDHM, time);
    }

    private static final String[] digitalUnits = new String[]{"B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB", "DB", "NB"};

    /**
     * 格式化显示文件大小
     */
    public static String formatSize(long size) {
        if (size <= 0) return "0B";
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + digitalUnits[digitGroups];
    }

    public static String formatDistance(double distance) {
        if (distance < 999) {
            return StringHelper.format("%.2f米", distance);
        } else {
            return StringHelper.format("%.2f公里", distance / 1000.0);
        }
    }

    /**
     * 获取当前时间戳
     */
    public static long timestamp() {
        return new Date().getTime();
    }

    private static final String REGEX_HTTP = "^((http[s]{0,1})|ftp)://";

    /**
     * 判断字符串是否是url，http/https/ftp开头的
     */
    public static boolean isUrl(String url) {
        Pattern pattern = Pattern.compile(REGEX_HTTP);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_img = "<img[^>]*?(\\/>|><\\/img>|>)"; // 定义img的正则表达式
    private static final String regEx_video = "<video[^>]*?>[\\s\\S]*?<\\/video>"; // 定义img的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    /**
     * 清除 html 标签
     */
    public static String clearHtml(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * HTML字符串里是否包含img标签
     */
    public static boolean hasImage(String html) {
        return !StringHelper.isEmpty(html, true) && Pattern.compile(regEx_img).matcher(html).find();
    }

    /**
     * HTML字符串里是否包含video标签
     */
    public static boolean hasVideo(String html) {
        return !StringHelper.isEmpty(html, true) && Pattern.compile(regEx_video).matcher(html).find();
    }

    /**
     * 判断字符串是否全为数字
     */
    public static boolean isNumber(String text) {
        return text.matches("[0-9]+");
    }

    public static boolean isItMobilePhone(String phone) {
        return Pattern.compile("^((\\+86)|(86))?[1][\\d]{10}$").matcher(phone).matches();
    }

    /**
     * 查看指定的包是否已安装
     */
    public static PackageInfo isInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo;
    }

    public static void hidingInputBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hidingInputBoard(View view) {
        hidingInputBoard(view.getContext(), view);
    }


    public static void showInputBoard(View view) {
        showInputBoard(view.getContext(), view);
    }


    public static void showInputBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    /**
     * 通过string获取相应资源的id<br />
     * getResId("icon", context, Drawable.class);
     */
    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String listToString(List<String> list) {
        String string = "";
        for (String s : list) {
            string += (StringHelper.isEmpty(string) ? "" : ",") + s;
        }
        return string;
    }
}
