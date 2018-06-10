package reuse.rick.tws.com.clipboard;

import android.content.ClipData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 剪贴板代理处理类
 */
public class ClipboardManagerStubProxy implements InvocationHandler {

    // 真正的clipboardManager
    private Object cm;

    public ClipboardManagerStubProxy(Object cm) {
        this.cm = cm;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String methodName = method.getName();
        switch (methodName) {
            case "hasPrimaryClip": // 剪贴板是否有粘贴内容[走系统流程]
                break;
            case "getPrimaryClip": // 获取粘贴内容[走系统流程]
                break;
            case "setPrimaryClip": // 向剪切板设置粘贴内容[注意：替换或者清空，但要保留真时的内容进行管理]
                // 替换或者清空，但要保留真时的内容进行管理
                // 替换或者清空，但要保留真时的内容进行管理
                final int argsCount = null == args ? 0 : args.length;
                for (int index = 0; index < argsCount; index++) {
                    if (args[index] instanceof ClipData) {
                        args[index] = ClipData.newPlainText(null, "政务通秘密不外漏");
                    }
                }
                break;
        }

        return method.invoke(cm, args);
    }

}
