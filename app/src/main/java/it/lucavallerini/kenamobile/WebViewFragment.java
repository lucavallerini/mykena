package it.lucavallerini.kenamobile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class WebViewFragment extends Fragment {

    final static String URI_TO_LOAD_KEY = "URI_TO_LOAD";
    final static String WEBVIEW_FRAGMENT_TAG = "webviewFragment";

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return layoutInflater.inflate(R.layout.webview_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mWebView = view.findViewById(R.id.webview);
        mProgressBar = view.findViewById(R.id.progress_bar);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Set the progress in the progress bar
                mProgressBar.setProgress(newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Make the progress bar visible
                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Make the progress bar gone
                mProgressBar.setVisibility(ProgressBar.GONE);
                super.onPageFinished(view, url);
            }
        });

        mWebView.loadUrl(getArguments().getString(URI_TO_LOAD_KEY));
    }

    /**
     * Check if the web client has a history stack.
     *
     * @return true if the history stack is not empty, false otherwise.
     */
    boolean canGoBack() {
        return mWebView.canGoBack();
    }

    /**
     * Traverse the history stack of the web client
     * one entry at a time.
     */
    void goBack() {
        mWebView.goBack();
    }
}
