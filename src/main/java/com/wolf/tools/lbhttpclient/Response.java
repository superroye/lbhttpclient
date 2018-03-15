package com.wolf.tools.lbhttpclient;

import com.wolf.tools.lbhttpclient.call.HttpUtils;

/**
 * Created by Roye on 2018/1/18.
 */

public class Response {

    public static final int ERROR_TIME_OUT = -101;
    public static final int ERROR_NETWORK_BAD = -102;
    public static final int ERROR_SERVER_ERROR = -103;
    public static final int ERROR_EMPTY_RESULT = -104;
    public static final int ERROR_OTHER_ERROR = -999;

    //错误类型
    public int errorCode;
    //http 状态码
    public int statusCode;
    public String errMsg;
    public String content;
    public long requestId;

    public static Response create() {
        Response r = new Response();
        r.errorCode = 0;
        r.statusCode = 200;
        return r;
    }

    public boolean isOK() {
        return HttpUtils.isGood(statusCode);
    }
}
