package reuse.rick.tws.com.clipboard;

import android.os.IBinder;

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
            // get Stub
            Class<?> IClipboardStubClazz = Class.forName("android.content.IClipboard$Stub");
            // 获取asInterface方法
            Method asInterfaceMethod = IClipboardStubClazz.getMethod("asInterface", IBinder.class);
            // asInterfaceMethod static方法，得到IClipboard对象
            Object clipboardManager = asInterfaceMethod.invoke(null, cmStub);

            // 返回代理的包装对象
            return Proxy.newProxyInstance(
                    clipboardManager.getClass().getClassLoader(),
                    clipboardManager.getClass().getInterfaces(),
                    new ClipboardManagerStubProxy(clipboardManager));
        }

        return method.invoke(cmStub, args);
    }
}
