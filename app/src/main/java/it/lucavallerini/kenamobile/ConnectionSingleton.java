package it.lucavallerini.kenamobile;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;


class ConnectionSingleton {

    /**
     * Site URIs.
     */
    private static final String COOKIE_URI = "https://www.kenamobile.it";

    private static ConnectionSingleton mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;
    private MyCookieManager mCookieManger;

    private ConnectionSingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
        mCookieManger = new MyCookieManager();
        CookieHandler.setDefault(mCookieManger);
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

    /**
     * Stores the cookie in the {@link CookieManager}.
     *
     * @param cookie {@link HttpCookie} cookie to store
     */
    void setCookie(HttpCookie cookie) {
        try {
            mCookieManger.getCookieStore()
                    .add(new URI(COOKIE_URI), cookie);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove the cookie stored in {@link CookieManager}.
     *
     * @param cookie {@link HttpCookie} cookie to remove
     */
    void removeCookie(HttpCookie cookie) {
        try {
            mCookieManger.getCookieStore()
                    .remove(new URI(COOKIE_URI), cookie);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    MyCookieManager getCookieManager() {
        return mCookieManger;
    }
}
