package it.lucavallerini.kenamobile;

import android.support.v4.app.Fragment;

/**
 * Interface that the host activity needs to implement in
 * order to receive a callback when the app needs to load
 * a new fragment.
 */
public interface OnLoadNewFragmentListener {
    void onLoadNewFragment(Fragment fragment, String tag);
}
