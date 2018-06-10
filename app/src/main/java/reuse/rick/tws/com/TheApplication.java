package reuse.rick.tws.com;

import android.app.Application;
import android.content.Context;

import reuse.rick.tws.com.clipboard.HackClipboardManager;

public class TheApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            HackClipboardManager.installProxy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
