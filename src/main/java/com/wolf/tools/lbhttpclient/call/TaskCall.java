package com.wolf.tools.lbhttpclient.call;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Roye on 2018/1/22.
 */

public class TaskCall {

    static Handler mHandler;

    private TaskCall() {
    }

    public static void runOnUI(Runnable runnable) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(runnable);
    }

    public static void runOnBack(Runnable runnable) {
        new AsyncTask<Runnable, Void, String>() {
            @Override
            protected String doInBackground(Runnable... runnables) {
                runnables[0].run();
                return null;
            }
        }.execute(runnable);
    }

}
