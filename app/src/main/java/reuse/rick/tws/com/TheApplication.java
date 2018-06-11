package reuse.rick.tws.com;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import reuse.rick.tws.com.clipboard.HackClipboardManager;

public class TheApplication extends Application {
    private static TheApplication mInstance = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;
        hackClipboardManager();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static TheApplication getInstance() {
        return mInstance;
    }

    private void hackClipboardManager() {
        boolean hackSuccess = false;
        if (Build.VERSION.SDK_INT <= 25) {
            try {
                HackClipboardManager.installProxy();
                hackSuccess = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!hackSuccess) {
            try {
                HackClipboardManager.installForHighVersion();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
