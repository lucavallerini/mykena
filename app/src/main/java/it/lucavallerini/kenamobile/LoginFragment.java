package it.lucavallerini.kenamobile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    final static String LOGIN_FRAGMENT_TAG = "loginFragment";

    /**
     * Cookie name, domain, path and version parameters.
     */
    private static final String COOKIE_SESSION_NAME = "_wp_session";
    private static final String COOKIE_SESSION_DOMAIN = "";
    private static final String COOKIE_SESSION_PATH = "/";
    private static final int COOKIE_SESSION_VERSION = 0;

    /**
     * Site URIs.
     */
    private static final String COOKIE_URI = "https://www.kenamobile.it/mykena/";
    private static final String MYKENA_URI = "https://www.kenamobile.it/wp-admin/admin-ajax.php";

    /**
     * Connections parameters keys.
     */
    private static final String USER = "user";
    private static final String PASSWORD = "userPassword";
    private static final String ACTION = "action";
    private static final String MAYA_ACTION = "maya_action";

    /**
     * Connections parameters values.
     */
    private static final String MAYA_INTERROGATE = "maya_interrogate";
    private static final String MAYA_ACTION_LOGIN = "ldapLogin";

    /**
     * Listener that helps to load a new fragment from the activity
     * when it is necessary.
     */
    private OnLoadNewFragmentListener mOnLoadNewFragmentListener;

    private Button mRechargeButton;
    private Button mLoginButton;
    private Button mClearEntries;
    private TextView mUsernameTextView;
    private TextView mPasswordTextView;
    private CheckBox mRememberMeCheckBox;

    private ConnectionRequests mConnection;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a new connection
        mConnection = new ConnectionRequests(getContext());

        // Inflate the layout
        return layoutInflater.inflate(R.layout.login_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUsernameTextView = view.findViewById(R.id.usernameEditText);
        mPasswordTextView = view.findViewById(R.id.passwordEditText);
        mRememberMeCheckBox = view.findViewById(R.id.rememberMe);

        mRechargeButton = view.findViewById(R.id.rechargeButton);
        mRechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnLoadNewFragmentListener
                        .onLoadNewFragment(new WebViewFragment(),
                                WebViewFragment.WEBVIEW_FRAGMENT_TAG);
            }
        });

        mClearEntries = view.findViewById(R.id.clearButton);
        mClearEntries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearEntries();
            }
        });

        mLoginButton = view.findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO check for proper credentials
                getCookie();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnLoadNewFragmentListener = (OnLoadNewFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRechargeButtonClickListener");
        }
    }

    /**
     * Clear all login form entries.
     */
    private void clearEntries() {
        if (mUsernameTextView != null) {
            mUsernameTextView.setText("");
            mUsernameTextView.requestFocus();
        }
        
        if (mPasswordTextView != null) {
            mPasswordTextView.setText("");
        }

        if (mRememberMeCheckBox != null) {
            mRememberMeCheckBox.setChecked(false);
        }
    }

    /**
     * Retrieve the cookie that stores the session ID
     * that needs to be used for further requests.
     */
    private void getCookie() {
        StringRequest requestCookie = new StringRequest(Request.Method.GET,
                COOKIE_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(LOGIN_FRAGMENT_TAG, response);
                        HttpCookie cookie = new HttpCookie(COOKIE_SESSION_NAME, response);
                        cookie.setDomain(COOKIE_SESSION_DOMAIN);
                        cookie.setPath(COOKIE_SESSION_PATH);
                        cookie.setVersion(COOKIE_SESSION_VERSION);
                        mConnection.setCookie(cookie);
                        login();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOGIN_FRAGMENT_TAG, error.toString());
                    }
                }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String header_response = String.valueOf(response.headers.values());
                int indexStart = header_response.indexOf("_wp_session=");
                int indexEnd = header_response.indexOf("; expires=");
                return Response.success(header_response.substring(indexStart + 12, indexEnd),
                        HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        mConnection.addToRequestQueue(requestCookie.setTag(LOGIN_FRAGMENT_TAG));
    }

    /**
     * Login into My Kena private area.
     */
    private void login() {
        StringRequest requestLogin = new StringRequest(Request.Method.POST,
                MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(LOGIN_FRAGMENT_TAG, response);
                        try {
                            JSONObject loginResponse = new JSONObject(response);

                            if (loginResponse.getInt("result") == -1) {
                                // TODO Login not successfull
                                Log.i(LOGIN_FRAGMENT_TAG, "Wrong credentials!");
                            } else {
                                Log.i(LOGIN_FRAGMENT_TAG, response);

                                Bundle bundle = new Bundle();
                                bundle.putString(OverviewFragment.MSISDN,
                                        mUsernameTextView.getText().toString());

                                Fragment newFragment = new OverviewFragment();
                                newFragment.setArguments(bundle);

                                mOnLoadNewFragmentListener
                                        .onLoadNewFragment(newFragment,
                                                OverviewFragment.OVERVIEW_FRAGMENT_TAG);
                            }
                        } catch (JSONException e) {
                            Log.e(LOGIN_FRAGMENT_TAG, "JSON parsing error");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOGIN_FRAGMENT_TAG, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MAYA_ACTION, MAYA_ACTION_LOGIN);
                params.put(USER, mUsernameTextView.getText().toString());
                params.put(PASSWORD, mPasswordTextView.getText().toString());
                params.put(ACTION, MAYA_INTERROGATE);

                return params;
            }
        };

        mConnection.addToRequestQueue(requestLogin.setTag(LOGIN_FRAGMENT_TAG));
    }
}
