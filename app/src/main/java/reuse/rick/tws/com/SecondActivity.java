package reuse.rick.tws.com;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.URISyntaxException;

public class SecondActivity extends Activity {
    private static final String TAG = SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        try {
            debug();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void debug() throws URISyntaxException {
        String jumpUri2 = "#Intent;action=com.example.MY_ACTION;i.some_int=100;end";
        String jumpUri3 = "#Intent;action=com.example.MY_ACTION;i.some_int=100;end";
        String jumpUriPage = "#Intent;action=com.example.myapp.SecondActivity;package=com.example.myapp;category=android.intent.category.DEFAULT;S.some=systemFrom;end";
        String jumpUriPage2 = "#Intent;action=com.example.myaction;package=com.example.myapp;category=android.intent.category.DEFAULT;S.some=innerFrom;end";

        Uri uri1, uri2, uri3, uri4;
        uri1 = Uri.parse(jumpUri2);
        uri2 = Uri.parse(jumpUri3);
        uri3 = Uri.parse(jumpUriPage);
        uri4 = Uri.parse(jumpUriPage2);
        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
        Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
        Intent intent4 = new Intent(Intent.ACTION_VIEW, uri4);
        Log.i(TAG, "" + intent1 + " " + intent2 + " " + intent3 + " " + intent4);

        Intent intentj2, intentj3, intentp, intentp2;
        intentj2 = Intent.parseUri(jumpUri2, Intent.URI_ANDROID_APP_SCHEME);
        intentj3 = Intent.parseUri(jumpUri3, Intent.URI_ANDROID_APP_SCHEME);
        intentp = Intent.parseUri(jumpUriPage, Intent.URI_ANDROID_APP_SCHEME);
        intentp2 = Intent.parseUri(jumpUriPage2, Intent.URI_ANDROID_APP_SCHEME);

        Log.i(TAG, "" + intentj2 + " " + intentj3 + " " + intentp + " " + intentp2);
    }
}
