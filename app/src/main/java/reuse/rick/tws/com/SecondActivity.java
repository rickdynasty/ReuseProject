package reuse.rick.tws.com;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import reuse.rick.tws.com.clipboard.HackClipboardManager;

public class SecondActivity extends Activity {
    private static final String TAG = SecondActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        // hook clipBoardManager
        final Button hookBtn1 = (Button) findViewById(R.id.hook_btn_1);

        hookBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HackClipboardManager.installProxy();
                    toastShow("Hook 1 success!");
                    hookBtn1.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShow("Hook 1 fail!");
                }
            }
        });
        final Button hookBtn2 = (Button) findViewById(R.id.hook_btn_2);
        hookBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    HackClipboardManager.installAnotherPos();
                    toastShow("Hook 2 success!");
                    hookBtn2.setEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    toastShow("Hook 2 fail!");
                }
            }
        });
    }

    private void toastShow(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
