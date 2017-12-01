package it.lucavallerini.kenamobile;

import android.webkit.WebView;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyCookieManager extends CookieManager {

    private android.webkit.CookieManager mCookieManager;

    MyCookieManager() {
        super(new MyCookieStore(), null);
        mCookieManager = android.webkit.CookieManager.getInstance();
    }

    MyCookieManager(CookieStore cookieStore, WebView view) {
        super(cookieStore, null);
        mCookieManager = android.webkit.CookieManager.getInstance();
        mCookieManager.setAcceptThirdPartyCookies(view, true);
    }

    @Override
    public CookieStore getCookieStore() {
        return super.getCookieStore();
    }

    @Override
    public Map<String, List<String>> get(URI uri, Map<String, List<String>> requestHeaders) throws IOException {
        // Check arguments validity
        if ((uri == null) || (requestHeaders == null)) {
            throw new IllegalArgumentException();
        }

        String url = uri.toString();
        Map<String, List<String>> response = new HashMap<>();

        String cookie = mCookieManager.getCookie(url);

        if (cookie != null) {
            response.put("Cookie", Collections.singletonList(cookie));
        }

        return response;
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException {
        // Check arguments validity
        if ((uri == null) || (responseHeaders == null)) {
            return;
        }

        String url = uri.toString();

        for (String headerKey : responseHeaders.keySet()) {
            if ((headerKey == null)
                    || !(headerKey.equalsIgnoreCase("Set-Cookie2"))
                    || headerKey.equalsIgnoreCase("Set-Cookie")) {
                continue;
            }

            for (String headerValue : responseHeaders.get(headerKey)) {
                mCookieManager.setCookie(url, headerValue);
            }
        }

        super.put(uri, responseHeaders);
    }
}
