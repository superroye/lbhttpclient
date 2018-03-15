package com.wolf.tools.lbhttpclient;

import android.app.Application;

import com.wolf.tools.lbhttpclient.call.CallBoxes;
import com.wolf.tools.lbhttpclient.connection.HTTPSTrustManager;

/**
 * Created by Roye on 2018/1/18.
 * <p>
 * LBHttpClient lb,light bird
 */

public class LBHttpClient {

    public static String TAG = "LBHttp";

    static LBHttpClient mInstance;
    public static final int DEF_CONNECT_TIMEOUT = 10_000;
    public static final int DEF_READ_TIMEOUT = 10_000;

    int connectTimeout;
    int readTimeout;

    Application app;

    private LBHttpClient() {

    }

    public static LBHttpClient getInstance() {
        if (mInstance == null) {
            new Throwable("LBHttpClient is not init!");
        }
        return mInstance;
    }

    public Application getApp() {
        return app;
    }

    public void setTag(String tag) {
        TAG = tag;
    }

    //同步执行
    public Response execute(Request request) {
        return CallBoxes.execute(request);
    }

    //异步执行
    public void call(Request request) {
        request.readTimeout(readTimeout);
        request.connectTimeout(connectTimeout);

        CallBoxes.call(request);
    }

    public void cancel(long requestId) {
        CallBoxes.cancel(requestId);
    }

    public static final class Builder {
        int connectTimeout;
        int readTimeout;
        Application app;

        public Builder() {
            connectTimeout = DEF_CONNECT_TIMEOUT;
            readTimeout = DEF_READ_TIMEOUT;
        }

        public void connectTimeout(int connectTimeout) {
            this.readTimeout = connectTimeout;
        }

        public void readTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
        }

        public void setApp(Application app) {
            this.app = app;
        }

        public LBHttpClient build() {
            LBHttpClient l = new LBHttpClient();
            l.connectTimeout = connectTimeout;
            l.readTimeout = readTimeout;
            l.app = app;

            HTTPSTrustManager.allowAllSSL();

            if (mInstance == null) {
                mInstance = l;
            }
            return l;
        }
    }

}
