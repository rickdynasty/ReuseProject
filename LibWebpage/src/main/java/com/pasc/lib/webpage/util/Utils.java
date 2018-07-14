package com.pasc.lib.webpage.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

//import com.paic.lib.base.util.Tools;
//import com.pingan.imui.commonbase.BaseActivity;

/**
 * 适配屏幕获取工具
 * 
 * @author 陈大龙
 * @date 2013-9-19
 * @time 上午10:32:49
 * 
 */
public class Utils {

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	public static float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception ex) {

		}
		return 1.0f;
	}
	
	public static int calcOfProportionWidth(Context context, int realTotalWidth, int realWidth){
	    return realWidth * getScreenWidth(context) / realTotalWidth;
	}

	public static int calcOfProportionHeight(Context context, int realTotalHeight, int realHeight){
        return realHeight * getScreenHeight(context) / realTotalHeight;
    }
	
	/**
     * 获取当前应用版本名 [一句话功能简述]<BR>
     * [功能详细描述]
     * @return
     */
    public static String getLocalVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pinfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = "V"+pinfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }
    
    /**
    * 获取屏幕的高度
    * 
    * @return 屏幕高度
    */
   public static int getDisplayHeight(Activity activity) {
       return activity.getWindowManager().getDefaultDisplay().getHeight();
   }

   /**
    * 获取顶部状态栏高度
    * 
    * @return 状态栏高度
    */
   public static int getStatusHeigth(Activity activity) {
       Rect rectgle = new Rect();
       activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
       int statusBarHeight = rectgle.top;
       return statusBarHeight;
   }

   
}