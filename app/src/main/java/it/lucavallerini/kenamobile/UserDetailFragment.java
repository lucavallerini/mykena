package it.lucavallerini.kenamobile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class UserDetailFragment extends Fragment {

    static final String USER_DETAIL_FRAGMENT_TAG = "USER_DETAIL_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        return layoutInflater.inflate(R.layout.user_detail_fragment, container, false);
    }
}
