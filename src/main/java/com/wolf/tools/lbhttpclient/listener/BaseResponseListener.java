package com.wolf.tools.lbhttpclient.listener;

import com.wolf.tools.lbhttpclient.Response;

public abstract class BaseResponseListener {

    public void onStart() {
    }

    public void onFinish() {
    }

    public abstract void onSuccessed(Response response);

    /**
     * @param response 网络错误，请求失败，或者服务端500，服务端拒绝400
     */
    public void onFailed(Response response) {
    }

    public void onCancel(Response response) {
    }

    public void onNetBreak(Response response) {
    }
}