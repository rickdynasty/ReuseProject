package demo.net.citizen.pasc.com.componentdemo.clipboard;

import android.content.ClipData;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import demo.net.citizen.pasc.com.componentdemo.TheApplication;

/**
 * 剪贴板代理处理类
 */
public class ClipboardManagerStubProxy implements InvocationHandler {
    private final static String DEFAULT_DES_MESSAGE = "政务通的内容不能拷贝到外面去哦~";
    // 真正的clipboardManager
    private Object cm;

    public ClipboardManagerStubProxy(Object cm) {
        this.cm = cm;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final int argsCount = null == args ? 0 : args.length;

        final String methodName = method.getName();
        switch (methodName) {
            case "hasPrimaryClip": // 剪贴板是否有粘贴内容[走系统流程]
                break;
            case "getPrimaryClip": // 获取粘贴内容[走系统流程]
            {
                final ClipData paCache = PAICClipboardManager.getInstance().getCache();
                if (null == paCache) {
                    break;
                }

                final ClipData sysClipData = (ClipData) method.invoke(cm, args);
                if (null == sysClipData || sysClipData.getItemCount() < 1) {
                    return paCache;
                }

                final CharSequence clipDataText = sysClipData.getItemAt(0).getText();
                if (DEFAULT_DES_MESSAGE.equals(clipDataText)) {
                    return paCache;
                }
            }
            case "setPrimaryClip": // 向剪切板设置粘贴内容[注意：替换或者清空，但要保留真时的内容进行管理]
                // 替换或者清空，但要保留真时的内容进行管理
                // 替换或者清空，但要保留真时的内容进行管理
                boolean hasGetData = false;
                Log.i("rick_Print:CMSP", "setPrimaryClip");
                for (int index = 0; index < argsCount; index++) {
                    if (args[index] instanceof ClipData) {
                        if (hasGetData) {
                            Toast.makeText(TheApplication.getInstance(), "怎么会存在两个及以上的拷贝内容？？？？", Toast.LENGTH_LONG).show();
                        }

                        PAICClipboardManager.getInstance().setClipData((ClipData) args[index]);

                        args[index] = ClipData.newPlainText(null, "政务通的内容不能拷贝到外面去哦~");
                    }
                }
                break;
        }

        return method.invoke(cm, args);
    }

}
