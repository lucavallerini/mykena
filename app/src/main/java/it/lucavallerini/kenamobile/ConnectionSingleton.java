package it.lucavallerini.kenamobile;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


class ConnectionSingleton {
    private static ConnectionSingleton mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private ConnectionSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    static synchronized ConnectionSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConnectionSingleton(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
