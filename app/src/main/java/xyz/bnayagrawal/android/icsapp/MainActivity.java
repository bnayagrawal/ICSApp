package xyz.bnayagrawal.android.icsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import xyz.bnayagrawal.android.icsapp.dashboard.DashboardFragment;
import xyz.bnayagrawal.android.icsapp.event.EventFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Bundle savedInstanceState;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        //check if the user has logged in or not
        if(!checkLogin()) {
            //user has not logged in
            Toast.makeText(getApplicationContext(),"Please login",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        this.savedInstanceState = savedInstanceState;
        setFragment();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);
        navigationView.setItemIconTintList(null);
        setNavDrawerUserInfo();
    }

    //check if the user has logged in or not
    protected boolean checkLogin() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE);
        String jwtToken = sharedPref.getString("USER_JWT_TOKEN",null);

        return (jwtToken != null);
    }

    //set nav drawer user name and email
    public void setNavDrawerUserInfo() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE);
        String user_full_name = sharedPref.getString("USER_FIRST_NAME","user") + " " + sharedPref.getString("USER_LAST_NAME","name");
        String user_email = sharedPref.getString("USER_EMAIL","username@email.xyz");

        //https://stackoverflow.com/questions/33194594/navigationview-get-find-header-layout
        View header = navigationView.getHeaderView(0);

        //sets
        ImageView user_image = ((ImageView) header.findViewById(R.id.nav_header_user_image));
        user_image.getLayoutParams().height = (int) getResources().getDimension(R.dimen.nav_header_icon_height);
        user_image.getLayoutParams().width = (int) getResources().getDimension(R.dimen.nav_header_icon_width);
        //int height = header.getWidth() * 9/16;
        //user_image.setMinimumHeight(height);

        ((TextView) header.findViewById(R.id.nav_header_user_email)).setText(user_email);
        ((TextView) header.findViewById(R.id.nav_header_user_full_name)).setText(user_full_name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.event_filter_show_all:
                return false;
            case R.id.event_filter_interested:
                return false;
            case R.id.event_filter_going:
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if(count == 1)
                Toast.makeText(this, "Press back button once again to exit!", Toast.LENGTH_SHORT).show();
            if(count == 0)
                super.onBackPressed();
            else
                getSupportFragmentManager().popBackStack();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        switch (id) {
            case R.id.nav_dashboard : {
                if (findViewById(R.id.fragment_container) != null) {
                    //check if it the fragment is already being display
                    DashboardFragment tf =  (DashboardFragment)getSupportFragmentManager().findFragmentByTag("DASHBOARD_FRAGMENT");
                    if(tf != null && tf.isVisible())
                        break;//means this fragment is currently being displayed

                    if (savedInstanceState == null) {
                        //clear the back stack
                        int count =  getSupportFragmentManager().getBackStackEntryCount();
                        for(int i = 0; i < count; ++i) {
                            getSupportFragmentManager().popBackStackImmediate();
                        }
                        Toast.makeText(this, "Press back button once again to exit!", Toast.LENGTH_SHORT).show();

                        //create the fragment
                        drawer.closeDrawer(GravityCompat.START);
                        DashboardFragment fragment = new DashboardFragment();
                        fragment.setArguments(getIntent().getExtras());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragment_container, fragment,"DASHBOARD_FRAGMENT").commit();
                    }
                }
                break;
            }
            case R.id.nav_anouncements:
                break;
            case R.id.nav_assignments:
                break;
            case R.id.nav_notice:
                break;
            case R.id.nav_events: {
                if (findViewById(R.id.fragment_container) != null) {
                    //check if it the fragment is already being display
                    EventFragment tf =  (EventFragment)getSupportFragmentManager().findFragmentByTag("EVENTS_FRAGMENT");
                    if(tf != null && tf.isVisible())
                        break; //means this fragment is currently being displayed
                    if (savedInstanceState == null) {
                        drawer.closeDrawer(GravityCompat.START);
                        EventFragment fragment = new EventFragment();
                        fragment.setArguments(getIntent().getExtras());
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.replace(R.id.fragment_container, fragment,"EVENTS_FRAGMENT").addToBackStack("EVENTS_FRAGMENT").commit();
                    }
                }
                break;
            }
            case R.id.nav_news:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                break;
            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbar() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        } else {
            drawer.setDrawerListener(null);
        }
    }

    protected void setFragment() {
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            //clear backstack
            getSupportFragmentManager().popBackStackImmediate();

            // Create a new Fragment to be placed in the activity layout
            DashboardFragment fragment = new DashboardFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            fragment.setArguments(getIntent().getExtras());

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // Add the fragment to the 'fragment_container' FrameLayout
            ft.replace(R.id.fragment_container, fragment, "DASHBOARD_FRAGMENT").commit();

            getSupportFragmentManager().addOnBackStackChangedListener(
                    new FragmentManager.OnBackStackChangedListener() {
                        @Override
                        public void onBackStackChanged() {
                            DashboardFragment fd =  (DashboardFragment)getSupportFragmentManager().findFragmentByTag("DASHBOARD_FRAGMENT");
                            if(fd != null && fd.isVisible())
                                navigationView.setCheckedItem(R.id.nav_dashboard);
                            EventFragment fe = (EventFragment) getSupportFragmentManager().findFragmentByTag("EVENTS_FRAGMENT");
                            if(fe != null && fe.isVisible())
                                navigationView.setCheckedItem(R.id.nav_events);

                            //TODO: For each fragment do the same
                        }
                    });
        }
    }
}
