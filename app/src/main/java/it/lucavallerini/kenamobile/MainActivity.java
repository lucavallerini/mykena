package it.lucavallerini.kenamobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnLoadNewFragmentListener {

    /**
     * Reference for all outgoing connections
     */
    ConnectionSingleton mConnectionSingleton;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;

    private Toolbar mToolbar;

    private MenuItem mCurrentItem;

    private boolean mUserLoggedIn = false; // TODO change this!

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        // Insert action bar
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        setupNavigationView();

        // Insert navigation_header in the navigation drawer:
        // the navigation header is based on the login status
        mNavigationView = findViewById(R.id.navigation_drawer);
        mHeaderView = getLayoutInflater()
                .inflate(R.layout.navigation_header, mNavigationView, false);
        TextView userName = mHeaderView.findViewById(R.id.header_text_first_row);
        mNavigationView.addHeaderView(mHeaderView);

        // Set on click listener to the navigation drawer
        mNavigationView.setNavigationItemSelectedListener(this);

        // Set the content of the main container and menu
        if (!mUserLoggedIn) {
            changeFragment(new LoginFragment(), LoginFragment.LOGIN_FRAGMENT_TAG);
            loggedMenu(false);
        } else {
            //changeFragment(new OverviewFragment(), OverviewFragment.OVERVIEW_FRAGMENT_TAG);
            loggedMenu(true);
        }

        mConnectionSingleton = ConnectionSingleton.getInstance(getApplicationContext());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (mCurrentItem != null) {
            mCurrentItem.setChecked(false);
        }

        mCurrentItem = item;

        switch (mCurrentItem.getItemId()) {
            case R.id.navigation_overview:
                changeFragment(new OverviewFragment(), OverviewFragment.OVERVIEW_FRAGMENT_TAG);
                break;
            case R.id.navigation_recharge_logged_out:
            case R.id.navigation_recharge_logged_in:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewFragment.URI_TO_LOAD_KEY, Constants.RECHARGE_URI);

                Fragment newFragment = new WebViewFragment();
                newFragment.setArguments(bundle);

                changeFragment(newFragment, WebViewFragment.WEBVIEW_FRAGMENT_TAG);
                setToolbarText(getString(R.string.navigation_item_recharge));

                loggedMenu(mCurrentItem.getItemId() == R.id.navigation_recharge_logged_in);
                break;
            case R.id.navigation_user:
                changeFragment(new UserDetailFragment(), UserDetailFragment.USER_DETAIL_FRAGMENT_TAG);
                break;
            case R.id.navigation_sim:
                changeFragment(new SimDetailFragment(), SimDetailFragment.SIM_DETAIL_FRAGMENT_TAG);
                break;
            case R.id.navigation_logout:
                String username = getSharedPreferences(Constants.PREF_FILE_LOGIN_NAME, MODE_PRIVATE)
                        .getString(Constants.PREF_LOGIN_USER_NAME, null);
                if (username != null) {
                    logout(username);
                    changeFragment(new LoginFragment(), LoginFragment.LOGIN_FRAGMENT_TAG);
                    loggedMenu(false);
                    getSharedPreferences(Constants.PREF_FILE_LOGIN_NAME, MODE_PRIVATE).edit()
                            .putString(Constants.PREF_LOGIN_USER_NAME, null)
                            .apply();
                } else {
                    loggedMenu(false);
                }
                break;
            case R.id.navigation_login:
                changeFragment(new LoginFragment(), LoginFragment.LOGIN_FRAGMENT_TAG);
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }

        // Select item
        mCurrentItem.setChecked(true);

        // Close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loggedMenu(boolean logged) {
        mNavigationView.getMenu().clear();
        if (logged) {
            mNavigationView.inflateMenu(R.menu.navigation_menu_loggedin);
        } else {
            mNavigationView.inflateMenu(R.menu.navigation_menu_loggedout);
        }
    }

    private void setupNavigationView() {
        ActionBarDrawerToggle actionBarDrawerToggle =
                new ActionBarDrawerToggle(this,
                        mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close) {

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        super.onDrawerOpened(drawerView);
                    }
                };

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));

        // Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Calling sync state is necessary or else hamburger icon won't show up
        actionBarDrawerToggle.syncState();
    }

    /**
     * Set the text that appears on the toolbar.
     *
     * @param text Text of the toolbar
     */
    private void setToolbarText(String text) {
        if (mToolbar != null) {
            mToolbar.setTitle(text);
        }
    }

    /**
     * Change {@link Fragment} within the specified container.
     *
     * @param container the container where to load the {@link Fragment}
     * @param fragment  the {@link Fragment} to load
     */
    protected void changeFragment(int container, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();

        if (!tag.equals(LoginFragment.LOGIN_FRAGMENT_TAG)) {
            loggedMenu(true);
        } else {
            loggedMenu(false);
        }

        if (tag.equals(OverviewFragment.OVERVIEW_FRAGMENT_TAG)) {
            setToolbarText(getString(R.string.navigation_item_overview));
            mCurrentItem = mNavigationView.getMenu().findItem(R.id.navigation_overview);
            mCurrentItem.setChecked(true);
        }

        if (tag.equals(UserDetailFragment.USER_DETAIL_FRAGMENT_TAG)) {
            setToolbarText(getString(R.string.navigation_item_user));
            mCurrentItem = mNavigationView.getMenu().findItem(R.id.navigation_user);
            mCurrentItem.setChecked(true);
        }

        if (tag.equals(SimDetailFragment.SIM_DETAIL_FRAGMENT_TAG)) {
            setToolbarText(getString(R.string.navigation_item_sim));
            mCurrentItem = mNavigationView.getMenu().findItem(R.id.navigation_sim);
            mCurrentItem.setChecked(true);
        }
    }

    /**
     * Change {@link Fragment} within the default container of the activity.
     *
     * @param fragment the {@link Fragment} to load
     */
    protected void changeFragment(Fragment fragment, String tag) {
        changeFragment(R.id.content_frame, fragment, tag);
    }

    @Override
    public void onLoadNewFragment(Fragment fragment, String tag) {
        changeFragment(fragment, tag);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment =
                getSupportFragmentManager().findFragmentByTag(WebViewFragment.WEBVIEW_FRAGMENT_TAG);
        if (fragment instanceof WebViewFragment) {
            WebViewFragment webView = (WebViewFragment) fragment;
            if (webView.canGoBack()) {
                webView.goBack();
                return;
            }
        }

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }

    /**
     * Logout from My Kena for the provided phone number.
     *
     * @param msisdn The phone number.
     */
    private void logout(final String msisdn) {
        StringRequest requestLogout = new StringRequest(Request.Method.POST,
                Constants.MYKENA_URI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("logout()", response);
                        mConnectionSingleton.removeCookies();
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
                params.put(Constants.MAYA_ACTION, Constants.MAYA_ACTION_LOGOUT);
                params.put(Constants.MSISDN, msisdn);
                params.put(Constants.ACTION, Constants.MAYA_INTERROGATE);

                return params;
            }
        };

        mConnectionSingleton.addToRequestQueue(requestLogout.setTag("REQUEST"));
    }
}
