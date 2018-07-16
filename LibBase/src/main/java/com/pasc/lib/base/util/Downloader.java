package com.pasc.lib.base.util;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.elvishew.xlog.XLog;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Turbo on 2018/06/01.
 */

public class Downloader {
    private static final String TAG = Downloader.class.getSimpleName();
    private volatile static Downloader INSTANCE;
    private static volatile boolean isInit = false;

    public static DownloadManager downloadManager;
    private static Context mContext;
    private ScheduledExecutorService scheduledExecutorService;
    private BroadcastReceiver downLoadBroadcast;
    public static final int HANDLE_DOWNLOAD = 0x001;
    public static final float COPLETED = 2.0F;
    private DownloadChangeObserver downloadObserver;
    private OnDownLoadListener onDownLoadListener;
    private long downloadId;
    Timer timer = new Timer();

    public Handler downLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (onDownLoadListener != null && HANDLE_DOWNLOAD == msg.what) {
                if (msg.arg1 >= 0 && msg.arg2 > 0) {
                    onDownLoadListener.onProgress(msg.arg1 / (float) msg.arg2);
                }
            }
        }
    };

    private Runnable progressRunnable = () -> updateProgress();

    private Downloader() {
    }

    public static void init(Context context) {
        if (!isInit) {
            mContext = context;
            downloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
            isInit = true;
        }
    }

    public static Downloader getInstance() {
        if (!isInit) {
            throw new InitException("Downloader::Init::Invoke init(context) first!");
        } else if (null == INSTANCE) {
            synchronized (Downloader.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Downloader();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 不显示标题栏下载进度，文件保存在InExternalFilesDir。
     *
     * @param url
     * @param fileName
     * @param onProgressListener
     * @return
     */
    public String[] download(String url, String fileName, OnDownLoadListener onProgressListener) {
        return download(false, "", "", url, true, "/", Environment.DIRECTORY_DOWNLOADS, fileName, onProgressListener);
    }

    /**
     * @param isShowTittle
     * @param tittle
     * @param description
     * @param url
     * @param isInExternalFilesDir
     * @param subPath
     * @param dirType
     * @param fileName
     * @param onProgressListener
     * @return
     */
    public String[] download(boolean isShowTittle, String tittle, String description, String url, boolean isInExternalFilesDir, String subPath, String dirType, String fileName, OnDownLoadListener onProgressListener) {

        String[] strings = new String[2]; //下标0为id,下标1为保存路径。

//        注册下载监听
        downloadObserver = new DownloadChangeObserver();
        registerContentObserver();
        registerBroadcast();

        this.onDownLoadListener = onProgressListener;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

//        是否显示在公共路径
        if (isInExternalFilesDir) {
            request.setDestinationInExternalFilesDir(mContext, dirType, subPath + fileName);
            strings[1] = mContext.getExternalFilesDir(dirType).getAbsolutePath();
        } else {
            request.setDestinationInExternalPublicDir(dirType, subPath + fileName);
            strings[1] = Environment.getExternalStoragePublicDirectory(dirType).getAbsolutePath();
        }

//        是否显示标题栏
        if (isShowTittle) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setTitle(tittle);
            request.setDescription(description);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }

//        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        downloadId = downloadManager.enqueue(request);
        strings[0] = String.valueOf(downloadId);

        return strings;
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
//        注册service 广播 1.任务完成时 2.进行中的任务被点击
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
        mContext.registerReceiver(downLoadBroadcast = new DownLoadBroadcast(), intentFilter);
    }

    /**
     * 注销广播
     */
    private void unregisterBroadcast() {
        if (downLoadBroadcast != null) {
            mContext.unregisterReceiver(downLoadBroadcast);
            downLoadBroadcast = null;
        }
    }

    /**
     * 注册ContentObserver
     */
    private void registerContentObserver() {
        if (downloadObserver != null) {
            mContext.getContentResolver().registerContentObserver(Uri.parse("content://downloads/my_downloads"), false, downloadObserver);
        }
    }

    /**
     * 注销ContentObserver
     */
    private void unregisterContentObserver() {
        if (downloadObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(downloadObserver);
        }
    }

    /**
     * 关闭定时器，线程等操作
     */
    private void close() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }

        if (downLoadHandler != null) {
            downLoadHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 发送Handler消息更新进度和状态
     */
    private void updateProgress() {
        int[] bytesAndStatus = getBytesAndStatus(downloadId);
        downLoadHandler.sendMessage(downLoadHandler.obtainMessage(HANDLE_DOWNLOAD, bytesAndStatus[0], bytesAndStatus[1], bytesAndStatus[2]));
    }

    /**
     * 通过query查询下载状态，包括已下载数据大小，总大小，下载状态
     *
     * @param downloadId
     * @return
     */
    private int[] getBytesAndStatus(long downloadId) {
        int[] bytesAndStatus = new int[]{
                -1, -1, 0
        };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = downloadManager.query(query);
            if (cursor != null && cursor.moveToFirst()) {
                //已经下载文件大小
                bytesAndStatus[0] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //下载文件的总大小
                bytesAndStatus[1] = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                //下载状态
                bytesAndStatus[2] = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                int reasonIdx = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                switch (bytesAndStatus[2]) {
                    //下载暂停
                    case DownloadManager.STATUS_PAUSED:
                        onDownLoadListener.onEvent(DownloadManager.STATUS_PAUSED, String.valueOf(cursor.getInt(reasonIdx)));
                        break;

                    //下载延迟
                    case DownloadManager.STATUS_PENDING:
//                        onDownLoadListener.onEvent(DownloadManager.STATUS_PAUSED,String.valueOf(cursor.getInt(reasonIdx)));
                        onDownLoadListener.onEvent(DownloadManager.STATUS_PENDING, "STATUS_PENDING");
                        break;

                    //正在下载
                    case DownloadManager.STATUS_RUNNING:
                        break;

                    //下载完成
                    case DownloadManager.STATUS_SUCCESSFUL:
                        break;

                    //下载失败
                    case DownloadManager.STATUS_FAILED:
                        onDownLoadListener.onEvent(DownloadManager.STATUS_FAILED, String.valueOf(cursor.getInt(reasonIdx)));
                        break;
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return bytesAndStatus;
    }

    //检查是否正在下载
    public boolean isDownloading(long id) {
        boolean result = false;
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    result = true;
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    result = true;
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    result = true;
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    break;

                //下载失败
                case DownloadManager.STATUS_FAILED:
                    break;
            }
        }
        c.close();
        return result;
    }

    public interface OnDownLoadListener {
        /**
         * 下载进度
         *
         * @param fraction 已下载/总大小
         */
        void onProgress(float fraction);

        void onEvent(int code, String msg);
    }

    /**
     * @param onProgressListener
     */
    public void setOnProgressListener(OnDownLoadListener onProgressListener) {
        this.onDownLoadListener = onProgressListener;
    }

    /**
     * 监听下载进度
     */
    private class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(downLoadHandler);
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        }

        /**
         * 当所监听的Uri发生改变时，就会回调此方法
         *
         * @param selfChange
         */
        @Override
        public void onChange(boolean selfChange) {
            XLog.i("onChange");

            if (!scheduledExecutorService.isTerminated()) {
                scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 100, TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * 接受下载完成广播
     */
    public class DownLoadBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            switch (intent.getAction()) {
                case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                    if (downloadId == downId && downId != -1 && downloadManager != null) {
                        close();
//                        下载完成
                        if (onDownLoadListener != null) {
                            onDownLoadListener.onProgress(COPLETED);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 消毁下载监听
     */
    public void destroy() {
        unregisterBroadcast();
        unregisterContentObserver();
    }

}
