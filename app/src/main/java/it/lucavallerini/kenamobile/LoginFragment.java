package it.lucavallerini.kenamobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    final static String LOGIN_FRAGMENT_TAG = "loginFragment";

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

    private ConnectionSingleton mConnection;
    private SharedPreferences mPreferences;

    private AlertDialog mAlertDialog;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Create a new connection
        mConnection = ConnectionSingleton.getInstance(getContext().getApplicationContext());
        Log.i(LOGIN_FRAGMENT_TAG, mConnection.getCookieManager().getCookieStore().getCookies().toString());

        mPreferences = getActivity().getSharedPreferences(Constants.PREF_FILE_LOGIN_NAME, Context.MODE_PRIVATE);

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
                mAlertDialog = showDialog(getContext(), "Login", "Accesso in corso...").show();
                login(mUsernameTextView.getText().toString(), mPasswordTextView.getText().toString());
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
     * Login into My Kena private area.
     */
    private void login(final String username, final String password) {
        StringRequest requestLogin = new StringRequest(Request.Method.POST,
                Constants.MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject loginResponse = new JSONObject(response);

                            if (loginResponse.getInt("result") == -1) {
                                // TODO Login not successfull
                                Log.i(LOGIN_FRAGMENT_TAG, "Wrong credentials!");
                            } else {
                                mPreferences.edit()
                                        .putString(Constants.PREF_LOGIN_USER_NAME, username)
                                        .commit();

                                mAlertDialog.dismiss();

                                mOnLoadNewFragmentListener
                                        .onLoadNewFragment(new OverviewFragment(),
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
                        mAlertDialog.dismiss();
                        Toast.makeText(getActivity().getApplicationContext(), "Login error!", Toast.LENGTH_LONG)
                                .show();
                        Log.e(LOGIN_FRAGMENT_TAG, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.MAYA_ACTION, Constants.MAYA_ACTION_LOGIN);
                params.put(Constants.USER, username);
                params.put(Constants.PASSWORD, password);
                params.put(Constants.ACTION, Constants.MAYA_INTERROGATE);

                return params;
            }
        };

        mConnection.addToRequestQueue(requestLogin.setTag(LOGIN_FRAGMENT_TAG));
    }

    private AlertDialog.Builder showDialog(Context context, String title, String message) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false);
    }
}
