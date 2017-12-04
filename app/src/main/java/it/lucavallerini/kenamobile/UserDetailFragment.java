package it.lucavallerini.kenamobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class UserDetailFragment extends Fragment {

    static final String USER_DETAIL_FRAGMENT_TAG = "USER_DETAIL_FRAGMENT";

    private ConnectionSingleton mConnection;
    private SharedPreferences mPreferences;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return layoutInflater.inflate(R.layout.user_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConnection = ConnectionSingleton.getInstance(getContext().getApplicationContext());
        mPreferences = getActivity().getSharedPreferences(Constants.PREF_FILE_LOGIN_NAME, Context.MODE_PRIVATE);

        String msisdn = mPreferences.getString(Constants.PREF_LOGIN_USER_NAME, null);
        if (msisdn != null) {
            getUserDetail(msisdn);
        }
    }

    /**
     * Obtain customer info by its msisdn.
     *
     * @param msisdn Customer's phone number
     */
    private void getUserDetail(final String msisdn) {
        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(USER_DETAIL_FRAGMENT_TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(USER_DETAIL_FRAGMENT_TAG, error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.MAYA_ACTION, Constants.MAYA_ACTION_CUSTOMER);
                params.put(Constants.MSISDN, msisdn);
                params.put(Constants.ACTION, Constants.MAYA_INTERROGATE);
                return params;
            }
        };

        mConnection.addToRequestQueue(request.setTag("GET_USER_DETAIL_REQUEST"));
    }
}
