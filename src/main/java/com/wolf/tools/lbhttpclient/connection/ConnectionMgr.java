package com.wolf.tools.lbhttpclient.connection;

import com.wolf.tools.lbhttpclient.Request;
import com.wolf.tools.lbhttpclient.internal.Method;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Roye on 2018/1/18.
 */

public class ConnectionMgr {

    public static HttpURLConnection getHttpConnection(Request request) throws IOException {
        URL url = new URL(request.url());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod(request.method().name());
        conn.setReadTimeout(request.readTimeout());
        conn.setConnectTimeout(request.connectTimeout());

        conn.setDoOutput(true);
        conn.setDoInput(true);

        if (Method.POST == request.method()) {
            conn.setUseCaches(false);
        }
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; chartset=UTF-8");
        conn.setRequestProperty("User-Agent", "android");
        conn.setRequestProperty("Connection", "Keep-Alive");

        return conn;
    }


}
