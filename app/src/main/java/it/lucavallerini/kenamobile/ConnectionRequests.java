package it.lucavallerini.kenamobile;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class ConnectionRequests {

    /**
     * Site URIs.
     */
    private static final String COOKIE_URI = "https://www.kenamobile.it";
    private static final String MYKENA_URI = "https://www.kenamobile.it/wp-admin/admin-ajax.php";

    /**
     * Connections parameters keys.
     */
    private static final String ACTION = "action";
    private static final String MAYA_ACTION = "maya_action";
    private static final String MSISDN = "msisdn";
    private static final String TRAFFIC_DATE_FROM = "dateFrom";
    private static final String TRAFFIC_DATE_TO = "dateTo";
    private static final String TRAFFIC_VOICE = "service_VOCE";
    private static final String TRAFFIC_SMS = "service_SMS";
    private static final String TRAFFIC_MMS = "service_MMS";
    private static final String TRAFFIC_DATA = "service_DATI";

    /**
     * Connections parameters values.
     */
    private static final String MAYA_INTERROGATE = "maya_interrogate";
    private static final String MAYA_ACTION_SERVICES = "getServices";
    private static final String MAYA_ACTION_CUSTOMER = "getCustomerByMsisdn";
    private static final String MAYA_ACTION_INFO_SIM = "getInfoSimByMSISDN";
    private static final String MAYA_ACTION_CHECK_LOGIN = "getLogin";
    private static final String MAYA_ACTION_LOGOUT = "logout";
    private static final String MAYA_ACTION_TRAFFIC = "getTraffic";

    private ConnectionSingleton mConnectionSingleton;

    /**
     * Logout from My Kena for the provided phone number.
     *
     * @param msisdn The phone number.
     */
    private void logout(final String msisdn) {
        StringRequest requestLogout = new StringRequest(Request.Method.POST,
                MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.i(LOG_TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("logout()", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MAYA_ACTION, MAYA_ACTION_LOGOUT);
                params.put(MSISDN, msisdn);
                params.put(ACTION, MAYA_INTERROGATE);

                return params;
            }
        };

        mConnectionSingleton.addToRequestQueue(requestLogout.setTag("REQUEST"));
    }
}
