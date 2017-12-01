package it.lucavallerini.kenamobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment {

    final static String OVERVIEW_FRAGMENT_TAG = "overviewFragment";

    private View mOverviewFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<Object> mAdapterList;

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

        mAdapterList.add(0, new DashboardInfo());
        mAdapterList.add(1, new PromoInfo());
        mAdapter.notifyDataSetChanged();

        return mOverviewFragment;
    }
}
