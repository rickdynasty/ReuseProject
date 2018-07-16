package com.pasc.lib.base.util;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ex-lingchun001 on 2018/2/10.
 */

public class DownloadFileUtils {

    public interface LoadFileListener {
        void loadFinish(File file);

        void loadFail();
    }

    public static void downloadFileByUrl(final String url, final String fileName, final LoadFileListener listener) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                url2file(url, fileName, listener);
            }
        }

        ).start();
    }

    public static void url2file(String url, String fileName, final LoadFileListener listener) {
        InputStream in = null;
        OutputStream os = null;
        try {
            File appDir = new File(Environment.getExternalStorageDirectory(), "nantong");
            if (!appDir.exists()) appDir.mkdir();
            final File file = new File(appDir, fileName);
            if (file.exists()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.loadFinish(file);
                    }
                });
                return;
            }

            HttpURLConnection http = null;
            if (isHttps(url)) {
                http = initHttps(url);
            } else {
                http = initHttp(url);
            }

            in = http.getInputStream();

            // 获得文件字符流
            os = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.loadFinish(file);
                }
            });
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    listener.loadFail();
                }
            });
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化http请求参数
     */
    public static HttpURLConnection initHttp(String url) throws IOException {
        URL _url = new URL(url);
        HttpURLConnection http = (HttpURLConnection) _url.openConnection();
        // 连接超时
        http.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        http.setReadTimeout(25000);
        http.setRequestMethod("GET");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        return http;
    }

    /**
     * 初始化http请求参数
     */
    public static HttpsURLConnection initHttps(String url)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
            KeyManagementException {
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL _url = new URL(url);
        HttpsURLConnection http = (HttpsURLConnection) _url.openConnection();
        // 设置域名校验
        http.setHostnameVerifier(new TrustAnyHostnameVerifier());
        http.setSSLSocketFactory(ssf);
        // 连接超时
        http.setConnectTimeout(25000);
        // 读取超时 --服务器响应比较慢，增大时间
        http.setReadTimeout(25000);
        http.setRequestMethod("GET");
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        return http;
    }

    /**
     * 检测是否https
     */
    public static boolean isHttps(String url) {
        return url.startsWith("https");
    }

    /**
     * 信任所有主机 对于任何证书都不做SSL检测
     * 安全验证机制，而Android采用的是X509验证
     */
    public static class MyX509TrustManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }

    /**
     * 不进行主机名确认
     */
    public static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
