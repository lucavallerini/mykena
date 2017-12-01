package it.lucavallerini.kenamobile;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class MyCookieStore implements CookieStore {

    private ArrayList<Cookie> mCookieStore;

    MyCookieStore() {
        mCookieStore = new ArrayList<>();
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException();
        }

        if (uri == null) {
            mCookieStore.add(new Cookie(null, cookie));
        } else {
            Cookie tmpCookie = new Cookie(uri, cookie);
            int index = mCookieStore.indexOf(tmpCookie);

            if (index != -1) {
                mCookieStore.remove(index);
            }

            mCookieStore.add(tmpCookie);
        }
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        if (uri == null) {
            throw new NullPointerException();
        }

        List<HttpCookie> listCookies = new ArrayList<>();

        for (int i = 0; i <= mCookieStore.size(); i++) {
            Cookie cookie = mCookieStore.get(i);
            if (cookie.getUri().equals(uri)) {
                listCookies.add(cookie.getCookie());
            }
        }

        return listCookies;
    }

    @Override
    public List<HttpCookie> getCookies() {
        List<HttpCookie> listCookies = new ArrayList<>();

        for (int i = 0; i <= mCookieStore.size(); i++) {
            HttpCookie cookie = mCookieStore.get(i).getCookie();

            if (!cookie.hasExpired()) {
                listCookies.add(cookie);
            } else { // cookie is expired
                remove(mCookieStore.get(i).getUri(), cookie);
            }
        }

        return listCookies;
    }

    @Override
    public List<URI> getURIs() {
        List<URI> listURIs = new ArrayList<>();

        for (int i = 0; i <= mCookieStore.size(); i++) {
            listURIs.add(mCookieStore.get(i).getUri());
        }

        return listURIs;
    }

    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookie == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i <= mCookieStore.size(); i++) {
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
    }
}
