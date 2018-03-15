package com.wolf.tools.lbhttpclient.call;

import com.wolf.tools.lbhttpclient.Request;
import com.wolf.tools.lbhttpclient.connection.ConnectionMgr;
import com.wolf.tools.lbhttpclient.util.NetworkUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Roye on 2018/1/18.
 */

public class HttpUtils {

    public static RequestTrack doRequest(Request request) {
        RequestTrack track = new RequestTrack(request);

        if (!NetworkUtils.isAvailable()) {
            return track;
        }
        track.doStart();

        HttpURLConnection connection = null;
        try {
            connection = ConnectionMgr.getHttpConnection(request);
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection
                    .getOutputStream());

            Iterator<Map.Entry<String, String>> it = request.params().entrySet().iterator();
            StringBuilder sb = new StringBuilder();
            boolean started = false;
            Map.Entry<String, String> p;
            while (it.hasNext()) {
                p = it.next();
                if (started) {
                    sb.append("&");
                } else {
                    started = true;
                }
                sb.append(p.getKey()).append("=").append(URLEncoder.encode(emptyStr(p.getValue()), "UTF-8"));
            }
            out.writeBytes(sb.toString());

            out.flush();
            out.close();

            int statusCode = connection.getResponseCode();
            track.httpStatusCode = statusCode;

            if (isGood(statusCode)) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder responseBuff = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    responseBuff.append(line);
                }

                track.responseResult = responseBuff.toString();

                reader.close();
            }
        } catch (Throwable e) {
            e.printStackTrace();

            track.throwable = e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return track;
    }

    public static boolean isGood(int statusCode) {
        return statusCode >= 200 && statusCode < 400;
    }

    public static String emptyStr(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

}
