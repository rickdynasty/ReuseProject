package demo.net.citizen.pasc.com.componentdemo.clipboard;

import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AndroidAppClipboardManager implements InvocationHandler {

    // 系统端的ClipboardManager在进程空间本地端的代理
    private IBinder cmStub;

    public AndroidAppClipboardManager(IBinder cmStub) {
        this.cmStub = cmStub;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("queryLocalInterface")) {
            Log.i("rick_Print:AACM", "invoke:" + proxy + " method:" + method.getName());
            for (Object arg : args) {
                Log.i("rick_Print:AACM", " arg:" + arg);
            }
            // get Stub
            Class<?> IClipboardStubClazz = Class.forName("android.content.IClipboard$Stub");
            Log.i("rick_Print:AACM", " IClipboardStubClazz:" + IClipboardStubClazz);
            // 获取asInterface方法
            Method asInterfaceMethod = IClipboardStubClazz.getMethod("asInterface", IBinder.class);
            Log.i("rick_Print:AACM", " asInterfaceMethod:" + asInterfaceMethod);
            // asInterfaceMethod static方法，得到IClipboard对象
            Object clipboardManager = asInterfaceMethod.invoke(null, cmStub);
            Log.i("rick_Print:AACM", " clipboardManager:" + clipboardManager);

            // 返回代理的包装对象
            return Proxy.newProxyInstance(
                    clipboardManager.getClass().getClassLoader(),
                    clipboardManager.getClass().getInterfaces(),
                    new ClipboardManagerStubProxy(clipboardManager));
        }

        return method.invoke(cmStub, args);
    }
}
