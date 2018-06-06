package reuse.rick.tws.com;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import java.net.URISyntaxException;

public class PageJump {

    static final String TAG = "PageJump";

    static final String SCHEME_INTENT = "page";
    static final String SCHEME_ANDROIDAPP = "android-app:";
    static final String SCHEME_HTTP = "http";
    static final String SCHEME_HTTPS = "https";

    //动态解析实现对页面行为控制
    public static void jumpPageUri(Context context, String strUri) {
        //android-app://com.example.app/<br />#Intent;action=com.example.MY_ACTION;
        if (TextUtils.isEmpty(strUri)) {
            throw new NullPointerException("parser uri content is empty");
        }

        String uriData = strUri.trim();

        if (uriData.startsWith(SCHEME_ANDROIDAPP)) {
            Intent intent = null;
            try {
                intent = Intent.parseUri(uriData, Intent.URI_INTENT_SCHEME);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            if(TextUtils.isEmpty(intent.getPackage())){
                intent.setPackage(context.getPackageName());
            }

            context.startActivity(intent);

        } else if (uriData.startsWith(SCHEME_HTTP) || uriData.startsWith(SCHEME_HTTPS)) {
//              WebViewActivity.launch(context, uri);
        } else {
            Uri uri = Uri.parse(strUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        }
    }
}
