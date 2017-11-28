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
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private View mHeaderView;

    private Toolbar mToolbar;

    private boolean mUserLoggedIn = true; // TODO change this!

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
            userName.setText("Utente non loggato");
            ((ViewGroup) mHeaderView).removeView(phoneNumber);
        } else {
            userName.setText("Luca Vallerini");
            phoneNumber.setText("3476806462");
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
            changeFragment(new LoginFragment());
        } else {
            changeFragment(new OverviewFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_overview:
                changeFragment(new OverviewFragment());
                break;
            default:
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }

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

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        // Calling sync state is necessary or else hamburger icon won't show up
        actionBarDrawerToggle.syncState();
    }

    /**
     * Change {@link Fragment} within the specified container.
     *
     * @param container the container where to load the {@link Fragment}
     * @param fragment  the {@link Fragment} to load
     */
    private void changeFragment(int container, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Change {@link Fragment} within the default container of the activity.
     *
     * @param fragment the {@link Fragment} to load
     */
    private void changeFragment(Fragment fragment) {
        changeFragment(R.id.content_frame, fragment);
    }
}
