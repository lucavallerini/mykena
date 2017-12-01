package it.lucavallerini.kenamobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OverviewFragment extends Fragment {

    final static String OVERVIEW_FRAGMENT_TAG = "overviewFragment";
    static final String MSISDN = "msisdn";
    /**
     * Site URIs.
     */
    private static final String MYKENA_URI = "https://www.kenamobile.it/wp-admin/admin-ajax.php";
    /**
     * Connections parameters keys.
     */
    private static final String ACTION = "action";
    private static final String MAYA_ACTION = "maya_action";
    /**
     * Connections parameters values.
     */
    private static final String MAYA_INTERROGATE = "maya_interrogate";
    private static final String MAYA_ACTION_CREDIT = "getCreditInfo";
    private static final String MAYA_ACTION_PROMO = "getUserPromoBags";

    private View mOverviewFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Object> mAdapterList;

    private ConnectionRequests mConnection;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        mOverviewFragment = layoutInflater.inflate(R.layout.overview_fragment, container, false);

        mRecyclerView = mOverviewFragment.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapterList = new ArrayList<>(2);
        mAdapter = new OverviewAdapter(mAdapterList);
        mRecyclerView.setAdapter(mAdapter);

        mConnection = new ConnectionRequests(getContext());

        return mOverviewFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String msisdn = getArguments().getString(MSISDN);
        Log.d("username", msisdn);
        getCreditInfo(msisdn);
//        getPromoInfo(msisdn);
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
                MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(OVERVIEW_FRAGMENT_TAG, response);
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

                            mAdapterList.add(0, creditInfo);
                            mAdapter.notifyDataSetChanged();
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

        mConnection.addToRequestQueue(requestData.setTag("REQUEST"));
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
                MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(OVERVIEW_FRAGMENT_TAG, response);
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

                            mAdapterList.add(1, promoInfo);
                            mAdapter.notifyDataSetChanged();
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

        mConnection.addToRequestQueue(requestData);
    }
}
