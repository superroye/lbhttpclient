package com.wolf.tools.lbhttpclient;

import com.wolf.tools.lbhttpclient.internal.Method;
import com.wolf.tools.lbhttpclient.listener.BaseResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roye on 2018/1/18.
 */

public class Request {

    private ConfigParams mConfig;
    public long requestId;

    private BaseResponseListener responseListener;

    private Request(long id) {
        requestId = id;
    }

    public String url() {
        return mConfig.url;
    }

    public Method method() {
        return mConfig.method;
    }

    public Map<String, String> params() {
        return mConfig.requestParams;
    }

    public int connectTimeout() {
        return mConfig.connectTimeout;
    }

    public int readTimeout() {
        return mConfig.readTimeout;
    }

    public void connectTimeout(int connectTimeout) {
        mConfig.connectTimeout = connectTimeout;
    }

    public void readTimeout(int readTimeout) {
        mConfig.readTimeout = readTimeout;
    }

    public void setResponseListener(BaseResponseListener listener) {
        responseListener = listener;
    }

    public BaseResponseListener getResponseListener() {
        return responseListener;
    }

    public void abort() {
        LBHttpClient.getInstance().cancel(requestId);
    }

    public static class Builder {

        ConfigParams mConfig;

        public Builder(String url) {
            mConfig = new ConfigParams(url);
        }

        public Builder method(Method method) {
            mConfig.method = method;
            return this;
        }

        public Builder addParam(String key, String value) {
            mConfig.requestParams.put(key, value);
            return this;
        }

        public Builder addParams(Map<String, String> params) {
            mConfig.requestParams.putAll(params);
            return this;
        }

        public Request build() {
            Request r = new Request(System.currentTimeMillis());
            r.mConfig = mConfig;
            return r;
        }
    }

    public static class ConfigParams {

        String url;
        Method method;
        Map<String, String> requestParams;

        int connectTimeout;
        int readTimeout;

        public ConfigParams(String url) {
            this.url = url;
            this.method = Method.GET;
            this.requestParams = new HashMap<>();
            connectTimeout = LBHttpClient.DEF_CONNECT_TIMEOUT;
            readTimeout = LBHttpClient.DEF_READ_TIMEOUT;
        }
    }
}
