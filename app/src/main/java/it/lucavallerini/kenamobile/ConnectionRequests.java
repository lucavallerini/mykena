package it.lucavallerini.kenamobile;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ConnectionRequests {

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
    private static final String COOKIE_URI = "https://www.kenamobile.it";
    private static final String DASHBOARD_URI = "https://www.kenamobile.it/mykena/#/dashboard";
    private static final String MYKENA_URI = "https://www.kenamobile.it/wp-admin/admin-ajax.php";
    private static final String RECHARGE_URI = "https://www.kenamobile.it/ricarica";

    /**
     * Connections parameters keys.
     */
    private static final String USER = "user";
    private static final String PASSWORD = "userPassword";
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
    private static final String MAYA_ACTION_LOGIN = "ldapLogin";
    private static final String MAYA_ACTION_SERVICES = "getServices";
    private static final String MAYA_ACTION_CUSTOMER = "getCustomerByMsisdn";
    private static final String MAYA_ACTION_INFO_SIM = "getInfoSimByMSISDN";
    private static final String MAYA_ACTION_CHECK_LOGIN = "getLogin";
    private static final String MAYA_ACTION_CREDIT = "getCreditInfo";
    private static final String MAYA_ACTION_PROMO = "getUserPromoBags";
    private static final String MAYA_ACTION_LOGOUT = "logout";
    private static final String MAYA_ACTION_TRAFFIC = "getTraffic";

    private ConnectionSingleton mConnectionSingleton;
    private CookieManager mCookieManger;
    private HttpCookie mCookie; // TODO store it somewhere else?

    ConnectionRequests(Context context) {
        mConnectionSingleton = ConnectionSingleton.getInstance(context);
        mCookieManger = new CookieManager();
        CookieHandler.setDefault(mCookieManger);
    }

    /**
     * Retrieve the cookie that stores the session ID
     * that need to be used for further requests.
     */
    private void getCookie() {
        StringRequest requestCookie = new StringRequest(Request.Method.GET,
                COOKIE_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HttpCookie cookie = new HttpCookie(COOKIE_SESSION_NAME, response);
                        cookie.setDomain(COOKIE_SESSION_DOMAIN);
                        cookie.setPath(COOKIE_SESSION_PATH);
                        cookie.setVersion(COOKIE_SESSION_VERSION);
                        try {
                            mCookieManger
                                    .getCookieStore()
                                    .add(new URI(COOKIE_URI), cookie);
                            mCookie = cookie;
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                            Log.e("setCookie()", "Error on setting cookie");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getCookie()", error.toString());
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

        mConnectionSingleton.addToRequestQueue(requestCookie.setTag("REQUEST"));
    }

    /**
     * Login into MyKena private area.
     *
     * @param username the username to access MyKena
     * @param password the password to access MyKena
     */
    private void login(final String username, final String password) {
        StringRequest requestLogin = new StringRequest(Request.Method.POST,
                MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("login()", response);
                        try {
                            JSONObject loginResponse = new JSONObject(response);

                            if (loginResponse.getInt("result") == -1) {
                                // TODO Login not successfull
                            }
                        } catch (JSONException e) {
                            Log.e("login()", "JSON parsing error");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("login()", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MAYA_ACTION, MAYA_ACTION_LOGIN);
                params.put(USER, username);
                params.put(PASSWORD, password);
                params.put(ACTION, MAYA_INTERROGATE);

                return params;
            }
        };

        mConnectionSingleton.addToRequestQueue(requestLogin.setTag("REQUEST"));
    }

    /**
     * Retrieve base information on the provided phone number.
     * <p>
     * With this request it is possible to retrieve:
     * <ul>
     * <li>credit left on the SIM;</li>
     * <li>the current tariff plan;</li>
     * <li>the activation date of the SIM;</li>
     * <li>the termination date of the SIM.</li>
     * </ul>
     *
     * @param msisdn The phone number.
     */
    private void getCreditInfo(final String msisdn) {
        StringRequest requestData = new StringRequest(Request.Method.POST,
                DASHBOARD_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("getCreditInfo()", response);
                        DashboardInfo creditInfo = new DashboardInfo();
                        try {
                            JSONObject jCreditInfo = new JSONObject(response);

                            creditInfo.setCreditLeft(jCreditInfo.getDouble("credit"));
                            creditInfo.setTariffPlan(jCreditInfo.getString("tariffPlan"));

                            String activeFrom = jCreditInfo.getString("activeFrom");
                            String activeTo = jCreditInfo.getString("activeTo");

                            SimpleDateFormat dateFormatSIM = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
                            try {
                                creditInfo.setSimActivationDate(dateFormatSIM.parse(activeFrom));
                                creditInfo.setSimTerminationDate(dateFormatSIM.parse(activeTo));
                            } catch (ParseException e) {
                                Log.e("getSimInfo()", "Date parsing error");
                                e.printStackTrace();
                            }

                            // TODO
//                            mDashboardInfoList.add(0, creditInfo);
//                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("getCreditInfo()", "JSON error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getCreditInfo()", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MAYA_ACTION, MAYA_ACTION_CREDIT);
                params.put(MSISDN, msisdn);
                params.put(ACTION, MAYA_INTERROGATE);

                return params;
            }
        };

        mConnectionSingleton.addToRequestQueue(requestData.setTag("REQUEST"));
    }

    /**
     * Retrieve information on the promotion(s) currently active
     * for the provided phone number.
     * <p>
     * <p>
     * With this request it is possible to retrieve:
     * <ul>
     * <li>the name of the promotion(s);</li>
     * <li>the cost of the promotion(s);</li>
     * <li>the activation date of the promotion(s);</li>
     * <li>the renewal (or termination) date of the promotion(s);</li>
     * <li>the description of the promotion(s);</li>
     * <li>the counter(s) of the promotion(s).</li>
     * </ul>
     * </p>
     *
     * @param msisdn The phone number.
     */
    private void getPromoInfo(final String msisdn) {
        StringRequest requestData = new StringRequest(Request.Method.POST,
                DASHBOARD_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PromoInfo promoInfo = new PromoInfo();

                        try {
                            JSONObject jPromoInfo = new JSONArray(response).getJSONObject(0);
                            JSONObject jPromoDescription = jPromoInfo.getJSONObject("translated_fields");
                            JSONObject jPromoData = jPromoInfo.getJSONArray("auxBag").getJSONObject(0);
                            JSONObject jPromoCalls = jPromoInfo.getJSONArray("auxBag").getJSONObject(1);
                            JSONObject jPromoDataEu = jPromoInfo.getJSONArray("auxBag").getJSONObject(2);

                            SimpleDateFormat dateFormatPromo = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);

                            String startPromo = jPromoInfo.getString("startDate");
                            String endPromo = jPromoInfo.getString("endDate");
                            try {
                                promoInfo.setPromoActivationDate(dateFormatPromo.parse(startPromo));
                                promoInfo.setPromoTerminationDate(dateFormatPromo.parse(endPromo));
                            } catch (ParseException e) {
                                Log.e("getPromoInfo()", "Date parsing error");
                                e.printStackTrace();
                            }

                            promoInfo.setPromoName(jPromoInfo.getString("promoName"));
                            promoInfo.setShortDescription(jPromoDescription.getString("description"));
                            promoInfo.setLongDescription(jPromoDescription.getString("description_tooltip"));

                            promoInfo.setPromoPrice(jPromoDescription.getString("price"));

                            promoInfo.setDataHuman(jPromoData.getDouble("value"));
                            promoInfo.setDataUnit(jPromoData.getString("unit"));
                            promoInfo.setDataByteUsed(jPromoData.getDouble("base_value"));
                            double dataByteTotal = jPromoData.getDouble("bagInitValue");
                            promoInfo.setDataByteTotal(dataByteTotal);
                            promoInfo.setDataGByteTotal(dataByteTotal / Math.pow(1024, 3));

                            promoInfo.setCallsHuman(jPromoCalls.getDouble("value"));
                            promoInfo.setCallsUnit(jPromoCalls.getString("unit"));
                            promoInfo.setCallsSecsUsed(jPromoCalls.getDouble("base_value"));
                            double callsSecsTotal = jPromoCalls.getDouble("bagInitValue");
                            promoInfo.setCallsSecsTotal(callsSecsTotal);
                            promoInfo.setCallsMinTotal(callsSecsTotal / 60);

                            promoInfo.setDataEuHuman(jPromoDataEu.getDouble("value"));
                            promoInfo.setDataEuUnit(jPromoDataEu.getString("unit"));
                            promoInfo.setDataEuByteUsed(jPromoDataEu.getDouble("base_value"));
                            double dataByteEuTotal = jPromoDataEu.getDouble("bagInitValue");
                            promoInfo.setDataEuByteTotal(dataByteEuTotal);
                            promoInfo.setDataEuGByteTotal(dataByteEuTotal / Math.pow(1024, 3));

                            // TODO
//                            mDashboardInfoList.add(1, promoInfo);
//                            mAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("getPromoInfo()", "JSON parsing error");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("getPromo()", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MAYA_ACTION, MAYA_ACTION_PROMO);
                params.put(MSISDN, msisdn);
                params.put(ACTION, MAYA_INTERROGATE);

                return params;
            }
        };

        mConnectionSingleton.addToRequestQueue(requestData);
    }

    /**
     * Logout from MyKena for the provided phone number.
     *
     * @param msisdn The phone number.
     */
    private void logout(final String msisdn) {
        StringRequest requestLogout = new StringRequest(Request.Method.POST,
                DASHBOARD_URI,
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
