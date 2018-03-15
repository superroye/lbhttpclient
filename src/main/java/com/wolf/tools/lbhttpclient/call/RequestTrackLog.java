package com.wolf.tools.lbhttpclient.call;

import com.wolf.tools.lbhttpclient.LBHttpClient;
import com.wolf.tools.lbhttpclient.Request;
import com.wolf.tools.lbhttpclient.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Roye on 2018/1/22.
 */

public class RequestTrackLog {

    public static void logRequest(Request request) {
        StringBuilder format = new StringBuilder();
        format.append("request url: %s\n")
                .append("request id : %s\n")
                .append("method     : %s\n")
                .append("params     : %s\n");

        Iterator<Map.Entry<String, String>> it = request.params().entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        boolean started = false;
        Map.Entry<String, String> p;
        try {
            while (it.hasNext()) {
                p = it.next();
                if (started) {
                    sb.append("&");
                } else {
                    started = true;
                }
                sb.append(p.getKey()).append("=").append(URLEncoder.encode(HttpUtils.emptyStr(p.getValue()), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        android.util.Log.d(LBHttpClient.TAG, String.format(format.toString(), request.url(), String.valueOf(request.requestId), request.method(), sb.toString()));
    }

    public static void logResponse(Response response) {
        StringBuilder format = new StringBuilder();
        format.append("request id : %s\n")
                .append("status code: %s\n")
                .append("err msg    : %s\n")
                .append("result     : %s\n");

        android.util.Log.d(LBHttpClient.TAG, String.format(format.toString(), String.valueOf(response.requestId), response.statusCode, response.errMsg, response.content));
    }
}
