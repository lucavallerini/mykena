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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnLoadNewFragmentListener {

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
        mHeaderView = getLayoutInflater().inflate(R.layout.navigation_header, mNavigationView, false);
        TextView userName = mHeaderView.findViewById(R.id.header_text_first_row);
        TextView phoneNumber = mHeaderView.findViewById(R.id.header_text_second_row);
        if (!mUserLoggedIn) {
            userName.setText("");
            ((ViewGroup) mHeaderView).removeView(phoneNumber);
        } else {
            userName.setText("");
            phoneNumber.setText("");
        }
        mNavigationView.addHeaderView(mHeaderView);

        // Inflate menu: the menu is also based on the login status of the user
        if (!mUserLoggedIn) {
            mNavigationView.inflateMenu(R.menu.navigation_menu_loggedout);
        } else {
            mNavigationView.inflateMenu(R.menu.navigation_menu_loggedin);
        }

        // Set on click listener to the navigation drawer
        mNavigationView.setNavigationItemSelectedListener(this);

        // Set the content of the main container
        if (!mUserLoggedIn) {
            changeFragment(new LoginFragment(), LoginFragment.LOGIN_FRAGMENT_TAG);
        } else {
            changeFragment(new OverviewFragment(), OverviewFragment.OVERVIEW_FRAGMENT_TAG);
        }
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
                setToolbarText(getString(R.string.navigation_item_overview));
                break;
            case R.id.navigation_recharge:
                Bundle bundle = new Bundle();
                bundle.putString(WebViewFragment.URI_TO_LOAD_KEY, ConnectionRequests.RECHARGE_URI);

                Fragment newFragment = new WebViewFragment();
                newFragment.setArguments(bundle);

                changeFragment(newFragment, WebViewFragment.WEBVIEW_FRAGMENT_TAG);
                setToolbarText(getString(R.string.navigation_item_recharge));
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
    // TODO set appropriate tag for transaction and back stack
    protected void changeFragment(int container, Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
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
        Fragment webViewFragment =
                getSupportFragmentManager().findFragmentByTag(WebViewFragment.WEBVIEW_FRAGMENT_TAG);
        if (webViewFragment instanceof WebViewFragment) {
            WebViewFragment webView = (WebViewFragment) webViewFragment;
            if (webView.canGoBack()) {
                webView.goBack();
                return;
            }
        }

        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
}
