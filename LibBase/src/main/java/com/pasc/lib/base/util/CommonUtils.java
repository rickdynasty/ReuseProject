package com.pasc.lib.base.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.elvishew.xlog.XLog;
import com.pasc.lib.base.ApplicationProxy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具方法类
 * <p>
 * Created by duyuan797 on 2017/3/30.
 */
public class CommonUtils {

//    /**
//     * 完善图片url
//     *
//     * @param imageUrl 图片url，相对地址要加上host
//     * @return
//     */
    /*public static String completeUrl(String imageUrl) {
        return URLUtil.isNetworkUrl(imageUrl) ? imageUrl : String
                .format("%s%s", UrlManager.HOST, imageUrl);
    }*/

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


    public static boolean isChinaPhoneLegal(String str) {
        //String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,4,5-9])|(17[0-8])|(147))\\d{8}$";
        //String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(147))\\d{8}$";//稍微放宽检验范围
        String regExp = "^(1)\\d{10}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断密码各式是否正确
     *
     * @param password
     * @return
     */
    public static boolean isPasswordLegal(String password) {
        //String regExp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$";//稍微放宽检验范围
        //String regExp = "^(?![0-9]+$)(?![a-zA-Z]+$)[^ ]{6,32}$";//必须包含字母和数字不能有空格
        String regExp = "^(?=.*\\d)(?=.*[A-Za-z])[^ ]{6,32}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 判断验证码格式
     *
     * @param verifyCode
     * @return
     */
    public static boolean isVerifyCodeLegal(String verifyCode) {
        String regExp = "^[0-9]{6}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(verifyCode);
        return m.matches();
    }

    /**
     * 用户名格式
     *
     * @param username
     * @return
     */
    public static boolean isUsernameLegal(String username) {
        String regExp = "^[a-zA-Z\\u4E00-\\u9FA5·]{2,}+$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    /**
     * 提取短信验证码
     *
     * @param smsBody
     * @return
     */
    public static String getSmsVertifyCode(String smsBody) {
        if (TextUtils.isEmpty(smsBody)) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\d{6}");
        Matcher matcher = pattern.matcher(smsBody);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     * 判断身份证号码是否正确
     *
     * @param idNum 身份证号码
     * @return
     */
    public static boolean checkIdcardValid(String idNum) {
        int idLength = idNum.length();
        if (idLength != 15 && idLength != 18) {
            return false;
        }
        // 验证身份证号码出身月日
        String m = "";
        String d = "";
        if (idLength == 15) {
            m = idNum.substring(8, 10);
            d = idNum.substring(10, 12);
        } else {
            m = idNum.substring(10, 12);
            d = idNum.substring(12, 14);
        }
        int mInt = 0;
        int dInt = 0;
        // 如果字符串包含非数据字符，转换异常时返回false
        try {
            mInt = Integer.parseInt(m);
            dInt = Integer.parseInt(d);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
        if (mInt > 12 || dInt > 31) {
            return false;
        }
        // 如果是15位身份证号码，不进行下面算法的验证
        if (idLength == 15) {
            return true;
        }

        // 加权因子 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
        int w[] = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            String a = idNum.substring(i, i + 1);
            int aInt = Integer.parseInt(a);
            sum += aInt * w[i];
        }
        int mod = sum % 11;
        boolean isValid = false;
        String lastC = idNum.substring(17, 18);
        // mod: 0 1 2 3 4 5 6 7 8 9 10
        // 校验码: 1 0 X 9 8 7 6 5 4 3 2
        switch (mod) {
            case 0:
                isValid = (lastC.equals("1"));
                break;
            case 1:
                isValid = (lastC.equals("0"));
                break;
            case 2:
                isValid = (lastC.equals("X"));
                if (!isValid) {
                    isValid = (lastC.equals("x"));
                }
                break;
            case 3:
                isValid = (lastC.equals("9"));
                break;
            case 4:
                isValid = (lastC.equals("8"));
                break;
            case 5:
                isValid = (lastC.equals("7"));
                break;
            case 6:
                isValid = (lastC.equals("6"));
                break;
            case 7:
                isValid = (lastC.equals("5"));
                break;
            case 8:
                isValid = (lastC.equals("4"));
                break;
            case 9:
                isValid = (lastC.equals("3"));
                break;
            case 10:
                isValid = (lastC.equals("2"));
                break;
            default:
                break;
        }
        return isValid;
    }

    private static final String RESOURCE_BASE = "android.resource://";

    /**
     * @return True if the url is a resource file.
     */
    public static boolean isResourceUrl(String url) {
        return (null != url) && url.startsWith(RESOURCE_BASE);
    }

    /**
     * 根据资源id拼接资源url
     *
     * @param resId
     * @return
     */
    public static String getResourceUrl(Context context, int resId) {
        return String.format(Locale.getDefault(), "%s%s/%d", RESOURCE_BASE, context.getPackageName(), resId);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从字符串中截取连续6位数字
     * 用于从短信中获取动态密码
     *
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while (m.find()) {
            if (m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }

    /**
     * 判断网络是否可用
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) ApplicationProxy.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip, float leftPading, float rightPading) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = SizeUtils.dp2px(leftDip);
        int right = SizeUtils.dp2px(rightDip);

        int leftP = SizeUtils.dp2px(leftPading);
        int rightP = SizeUtils.dp2px(rightPading);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(leftP, 0, rightP, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        setIndicator(tabs, leftDip, rightDip, 0, 0);
    }

    public static String formatBankNum(String bankNum) {
        return convertFormattedBankCardNum(bankNum).replaceAll("(\\d{4})", "$1 ").trim();
    }


    public static String convertFormattedBankCardNum(String bankNum) {
        return bankNum.replaceAll("\\s*", "");
    }

    /**
     * 非空判断
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0 || str.toString().trim().length() == 0;
    }


    public static String formatPlayTime(int timeMillis) {
        int second = timeMillis / 1000;
        int min = second / 60;
        int h = min / 60;
        StringBuilder builder = new StringBuilder();
        if (h > 0) {
            builder.append(h);
            builder.append(":");
        }
        builder.append(min % 60);
        builder.append(":");
        builder.append(second % 60);
        return builder.toString();
    }


    /**
     * 显示软键盘
     */
    public static void openSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(et, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(EditText et) {
        InputMethodManager inputMethodManager = (InputMethodManager) et.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(et.getWindowToken(), InputMethodManager
                .HIDE_NOT_ALWAYS);
    }


    /**
     * 获取时间戳 平安付调用
     *
     * @return
     */
    public static Date getDateByTimeZone() {
        long time = new Date().getTime() - Calendar.getInstance().getTimeZone().getRawOffset() + TimeZone
                .getTimeZone("GMT+8").getRawOffset();
        return new Date(time);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param activity
     * @param phoneNum 电话号码
     */
    public static void diallPhone(Activity activity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }

    /**
     * 获取版本名
     */
    public static String getVersionName(Context ctx) {
        String versionName = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本code
     */
    public static long getVersionCode(Context context) {
        long versionCode = 0;
        try {
            PackageInfo packageInfo = context
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    public static String checkSex(String IdNum) {
        String sex = "0";
        try {
            if (Integer.parseInt(IdNum.substring(16, 17)) % 2 == 0) {// 判断性别
                sex = "2";
            } else {
                sex = "1";
            }
        } catch (Exception e) {
            XLog.e(e.getMessage());
        }
        return sex;
    }


    /**
     * 跳转应用市场
     *
     * @param activity
     */
    public static void goAppMarkets(Activity activity, String packageName) {
        try {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent intent = new Intent(new Intent(Intent.ACTION_VIEW, uri));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception ex) {
            ToastUtils.toastMsg("您的手机还没有安装任何安装安装应用市场");
        }
    }

    public static String getHostName(String urlString) {
        String head = "";
        int index = urlString.indexOf("://");
        if (index != -1) {
            head = urlString.substring(0, index + 3);
            urlString = urlString.substring(index + 3);
        }
        index = urlString.indexOf("/");
        if (index != -1) {
            urlString = urlString.substring(0, index + 1);
        }
        return head + urlString;
    }

    /**
     * 获得进程名字
     */
    public static String getPIDName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return "";
    }
}
