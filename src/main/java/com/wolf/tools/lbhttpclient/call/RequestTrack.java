package com.wolf.tools.lbhttpclient.call;

import com.wolf.tools.lbhttpclient.Request;
import com.wolf.tools.lbhttpclient.Response;
import com.wolf.tools.lbhttpclient.util.NetworkUtils;

/**
 * Created by Roye on 2018/1/18.
 */

public class RequestTrack {

    Request request;
    Response response;
    public String responseResult;
    public Throwable throwable;
    public int httpStatusCode;

    public RequestTrack(Request request) {
        this.request = request;
        this.response = Response.create();
    }

    public Response getResponse() {
        return response;
    }

    public void feedback() {
        TaskCall.runOnUI(new Runnable() {
            @Override
            public void run() {
                try {
                    response.requestId = request.requestId;

                    if (HttpUtils.isGood(httpStatusCode)) {
                        doSuccess();
                    } else if (throwable != null) {
                        doError(throwable);
                    } else if (!NetworkUtils.isAvailable()) {
                        doNetBreak();
                    }
                } finally {
                    if (request.getResponseListener() != null) {
                        request.getResponseListener().onFinish();
                    }

                    TaskCall.runOnBack(new Runnable() {
                        @Override
                        public void run() {
                            RequestTrackLog.logResponse(response);
                            clear();
                        }
                    });
                }
            }
        });
    }

    void doStart() {
        TaskCall.runOnUI(new Runnable() {
            @Override
            public void run() {
                if (request.getResponseListener() != null) {
                    request.getResponseListener().onStart();
                }
                TaskCall.runOnBack(new Runnable() {
                    @Override
                    public void run() {
                        RequestTrackLog.logRequest(request);
                    }
                });
            }
        });
    }

    void doSuccess() {
        response.content = responseResult;
        if (request.getResponseListener() != null) {
            request.getResponseListener().onSuccessed(response);
        }
    }

    void doError(Throwable ex) {
        response.errorCode = Response.ERROR_SERVER_ERROR;
        response.errMsg = ex.toString();

        if (request.getResponseListener() != null) {
            request.getResponseListener().onFailed(response);
        }
    }

    void doNetBreak() {
        response.errorCode = Response.ERROR_NETWORK_BAD;
        response.errMsg = "request network is not available!";
        if (request.getResponseListener() != null) {
            request.getResponseListener().onNetBreak(response);
        }
    }

    public void clear() {
        request = null;
        responseResult = null;
        response = null;
        throwable = null;
    }
}
