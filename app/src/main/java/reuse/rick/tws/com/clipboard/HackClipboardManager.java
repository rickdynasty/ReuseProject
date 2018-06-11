package reuse.rick.tws.com.clipboard;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

import reuse.rick.tws.com.TheApplication;

/**
 * 剪贴板Hook帮助类
 */
public class HackClipboardManager {

    private final static String CLIPBOARD = "clipboard";
    private static final String TAG = HackClipboardManager.class.getSimpleName();

    private HackClipboardManager() {
    }

    /**
     * 姿势1，这种来得最直接：将本地端的IClipboard进行包装代理。
     *
     * @throws Exception
     */
    public static void installProxy() throws Exception {
        Log.i(TAG, "安装ClipboardManagerStubProxy");
        // 加载ClipboardManager类
        Class<?> cmClz = Class.forName("android.content.ClipboardManager");
        // 获取ClipboardManager的方法：getService，用于获取当前应用进程空间的ClipboardManager对象
        Method getServiceMethod = cmClz.getDeclaredMethod("getService");
        getServiceMethod.setAccessible(true);
        // 获取当前应用进程空间的ClipboardManager对象:IClipboard
        Object sService = getServiceMethod.invoke(null);
        // 获取 sService Feild
        Field sServiceFeild = cmClz.getDeclaredField("sService");
        sServiceFeild.setAccessible(true);
        // install - 替换sService为我们包装后的代理对象
        sServiceFeild.set(null,
                Proxy.newProxyInstance(sService.getClass().getClassLoader(),
                        sService.getClass().getInterfaces(),
                        new ClipboardManagerStubProxy(sService)));
    }

    /**
     * 姿势2：替换ServiceManager缓存sCache的缓存内容，即 - 从系统重新拿出“clipboard”的本地代理IBinder进行包装替换缓存。
     *
     * @throws Exception
     */
    public static void installAnotherPos() throws Exception {
        // 加载ServiceManager类
        Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
        // 获取getService方法
        Method getServiceMethod = serviceManagerClazz.getMethod("getService", String.class);
        // 获取真正的clipboardManager对象
        IBinder clipboardManagerIBinder = (IBinder) getServiceMethod.invoke(null, CLIPBOARD);
        // 获取sCache HashMap缓存
        Field sCacheField = serviceManagerClazz.getDeclaredField("sCache");
        // private变量
        sCacheField.setAccessible(true);
        // static变量
        HashMap<String, IBinder> sCache = (HashMap) sCacheField.get(null);
        // 把代理放入缓存
        sCache.put(CLIPBOARD, (IBinder) Proxy.newProxyInstance(
                clipboardManagerIBinder.getClass().getClassLoader(),
                clipboardManagerIBinder.getClass().getInterfaces(),
                new AndroidAppClipboardManager(clipboardManagerIBinder)));
    }
}
