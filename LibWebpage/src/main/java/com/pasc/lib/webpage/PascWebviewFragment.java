package com.pasc.lib.webpage;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;

import static android.app.Activity.RESULT_OK;

public class PascWebviewFragment extends BaseFragment implements WebChromeClientCallback {
    private final String TAG = PascWebviewFragment.class.getSimpleName();

    public final static int FILECHOOSER_RESULTCODE = 1;
    public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 2;

    public BridgeWebView mWebView = null;

    ValueCallback<Uri> mUploadMessage;
    ValueCallback<Uri[]> mUploadMessageForAndroid5;

    ViewGroup mRootView;
    ValueAnimator mValueAnimator = null;
    private ProgressBar mProgressbar;
    private int mCurrentProgress;
    /**
     * 加载地址
     */
    private String mUrl = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == mRootView) {
            mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_webview, container, false);
            mWebView = mRootView.findViewById(R.id.pase_webview);
            mProgressbar = (ProgressBar) mRootView.findViewById(R.id.mprogressBar);
            mProgressbar.setVisibility(View.VISIBLE);
            sureHandler();
            initWebView();
        }

        return mRootView;
    }


    private void initWebView() {
        if (null == mWebView) {
            throw new RuntimeException("WebView can not be null!");
        }

        if (null != mWebView.getBridgeWebChromeClient()) {
            mWebView.getBridgeWebChromeClient().setWebChromeClientCallback(this);
        }

        if (mActivity != null) {
            mWebView.setDownloadListener(new MyWebViewDownLoadListener());

            // 加载url
//            mWebView.loadUrl("file:///android_asset/test.html");
            mWebView.loadUrl("http://news.baidu.com/");
        }
    }

    @Override
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        Log.i(TAG, "openFileChooserImpl uploadMsg:" + uploadMsg);
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    @Override
    public void showFileChooser(ValueCallback<Uri[]> filePathCallback) {
        Log.i(TAG, "onenFileChooseImpleForAndroid filePathCallback:" + filePathCallback);
        mUploadMessageForAndroid5 = filePathCallback;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "onActivityResult intent:" + intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    private void startProgressAnim() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
            mValueAnimator.setDuration(400);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    int progress = (int) (value * mCurrentProgress);
                    if (progress > mProgressbar.getProgress()) {
                        mProgressbar.setProgress(progress);
                    }
                }
            });
        }
        if (mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
        Log.i("hiyi", "mValueAnimator.start() ------");
        mValueAnimator.start();
    }

    @Override
    public void onProgressChanged(int newProgress) {
        if (newProgress >= 100) {
            mProgressbar.setProgress(100);
            if (null != mHandler) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressbar.setProgress(0);
                        mProgressbar.setVisibility(View.GONE);
                    }
                }, 500);
            }

        } else if (newProgress - mCurrentProgress > 0) {
            Log.w("hiyi", "newProgress: " + newProgress + ", mCurrentProgress: " + mCurrentProgress);
            mCurrentProgress = newProgress;
            startProgressAnim();
        }
    }

    /**
     * 下载监听
     *
     * @param
     * @return
     * @date 2018/4/23
     */
    private class MyWebViewDownLoadListener implements DownloadListener {

        @SuppressLint("NewApi")
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (mActivity != null) {
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        if (mWebView.isLoadFinish() && mWebView.canGoBack()) {/*&& !isDirectlyFinish*/
//            //获取历史列表
//            WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
//            //判断当前历史列表是否最顶端,其实canGoBack已经判断过
//            if (mWebBackForwardList.getCurrentIndex() > 0) {
            mWebView.goBack();
            return true;
//            }
        } else {
            if (mActivity != null) {
                mActivity.finish();
            }
            return true;
        }
    }
}
