package it.lucavallerini.kenamobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public ConnectivityReceiver(ConnectivityReceiverListener connectivityReceiverListener) {
        ConnectivityReceiver.connectivityReceiverListener = connectivityReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        try {
            activeNetwork = connectivityManager.getActiveNetworkInfo();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }

    interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}
