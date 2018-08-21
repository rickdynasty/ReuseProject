package demo.net.citizen.pasc.com.componentdemo.clipboard;

import android.content.ClipData;

public class PAICClipboardManager {
    ClipData cache;

    public static PAICClipboardManager getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 静态内部类,只有在装载该内部类时才会去创建单例对象
     */
    private static class SingletonHolder {
        private static final PAICClipboardManager instance = new PAICClipboardManager();
    }

    public void setClipData(ClipData data){
        cache = new ClipData(data);
    }

    public ClipData getCache(){
        return cache;
    }
}
