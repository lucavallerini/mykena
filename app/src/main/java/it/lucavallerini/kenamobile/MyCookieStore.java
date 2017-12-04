package it.lucavallerini.kenamobile;

import android.util.Log;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class MyCookieStore implements CookieStore {

    private final static String LOG_TAG = "MyCookieStore";

    private ArrayList<Cookie> mCookieStore;

    MyCookieStore() {
        mCookieStore = new ArrayList<>();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException();
        }

        Cookie tmpCookie = new Cookie(uri, cookie);
        int index = mCookieStore.indexOf(tmpCookie);

        if (index != -1) {
            mCookieStore.remove(index);
            Log.d("REMOVE WHEN ADDING", tmpCookie.toString());
        }

        Log.d("ADDING", tmpCookie.toString());
        mCookieStore.add(tmpCookie);
        Log.d("AFTER ADDED", mCookieStore.toString());
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException();
        }

        List<HttpCookie> listCookies = new ArrayList<>();

        for (int i = 0; i < mCookieStore.size(); i++) {
            Cookie cookie = mCookieStore.get(i);
            if (cookie.getUri().equals(uri)) {
                listCookies.add(cookie.getCookie());
            }
        }

        Log.d("GET LIST FROM URI", listCookies.toString());
        return listCookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> listCookies = new ArrayList<>();

        for (int i = 0; i < mCookieStore.size(); i++) {
            HttpCookie cookie = mCookieStore.get(i).getCookie();

            if (!cookie.hasExpired()) {
                listCookies.add(cookie);
            } else { // cookie is expired
                remove(mCookieStore.get(i).getUri(), cookie);
            }
        }

        Log.d("GET LIST OF COOKIES", listCookies.toString());
        return listCookies;
    }

    @Override
    public List<URI> getURIs() {
        List<URI> listURIs = new ArrayList<>();

        for (int i = 0; i < mCookieStore.size(); i++) {
            listURIs.add(mCookieStore.get(i).getUri());
        }

        Log.d("GET LIST OF URI", listURIs.toString());
        return listURIs;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < mCookieStore.size(); i++) {
            Cookie tmpCookie = mCookieStore.get(i);

            if (uri != null) {
                if (tmpCookie.getUri().equals(uri)) {
                    mCookieStore.remove(i);
                    return true;
                }
            } else {
                if (tmpCookie.getCookie().equals(cookie)) {
                    mCookieStore.remove(i);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean removeAll() {
        mCookieStore.clear();
        return mCookieStore.isEmpty();
    }

    private class Cookie {
        private URI mURI;
        private HttpCookie mCookie;

        Cookie(URI uri, HttpCookie cookie) {
            mURI = uri;
            mCookie = cookie;
        }

        private URI getUri() {
            return mURI;
        }

        private HttpCookie getCookie() {
            return mCookie;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }

            if (obj instanceof Cookie) {
                Cookie tmp = (Cookie) obj;

                if (tmp.getUri() == null) {
                    return tmp.getCookie().equals(this.getCookie());
                } else {
                    return tmp.getUri().equals(this.getUri())
                            && tmp.getCookie().equals(this.getCookie());
                }
            }

            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUri(), getCookie());
        }

        @Override
        public String toString() {
            if (getUri() != null) {
                return getUri().toString() + ":::" + getCookie().toString();
            } else {
                return getCookie().toString();
            }
        }
    }
}
