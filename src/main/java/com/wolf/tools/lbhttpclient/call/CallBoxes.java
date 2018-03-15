package com.wolf.tools.lbhttpclient.call;

import android.os.AsyncTask;
import com.wolf.tools.lbhttpclient.Request;
import com.wolf.tools.lbhttpclient.Response;
import com.wolf.tools.lbhttpclient.util.NetworkUtils;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Roye on 2018/1/18.
 */

public class CallBoxes {

    static LinkedList<CallTask> linkedList = new LinkedList<>();

    public static void call(Request request) {
        AsyncTask<Object, Void, RequestTrack> task = new RequestTask();

        CallTask task1 = new CallTask(request.requestId, task);
        task1.execute(request);

        linkedList.add(task1);
    }

    //同步执行
    public static Response execute(Request request) {
        RequestTrack track = HttpUtils.doRequest(request);
        track.feedback();
        return track.getResponse();
    }

    public static void cancel(long requestId) {
        Iterator<CallTask> it = linkedList.iterator();
        CallTask task;
        while (it.hasNext()) {
            task = it.next();
            if (task.requestId == requestId) {
                it.remove();
                task.cancel();
            }
        }
    }

    public static void requestFinish(long requestId) {
        Iterator<CallTask> it = linkedList.iterator();
        CallTask task;
        while (it.hasNext()) {
            task = it.next();
            if (task.requestId == requestId) {
                it.remove();
            }
        }
    }

    static class RequestTask extends AsyncTask<Object, Void, RequestTrack> {

        public RequestTask() {

        }

        @Override
        protected RequestTrack doInBackground(Object... param) {
            Request request = (Request)param[0];
            if (!NetworkUtils.isAvailable()) {
                return new RequestTrack(request);
            }

            return HttpUtils.doRequest(request);
        }

        @Override
        protected void onPostExecute(RequestTrack result) {
            CallBoxes.requestFinish(result.request.requestId);
            result.feedback();
        }
    }

    static class CallTask {

        public long requestId;
        public AsyncTask task;

        CallTask(long requestId, AsyncTask task) {
            this.task = task;
            this.requestId = requestId;
        }

        public void execute(Request request) {
            task.execute(request);
        }

        public void cancel() {
            task.cancel(false);
        }
    }

}
