package reuse.rick.tws.com;

import android.app.Application;
import android.content.Context;

import reuse.rick.tws.com.clipboard.HackClipboardManager;

public class TheApplication extends Application {
    private static TheApplication mInstance = null;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mInstance = this;

        boolean hackSuccess = false;
        try {
            HackClipboardManager.installProxy();
            hackSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!hackSuccess) {
            try {
                HackClipboardManager.installAnotherPos();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static TheApplication getInstance() {
        return mInstance;
    }
}
