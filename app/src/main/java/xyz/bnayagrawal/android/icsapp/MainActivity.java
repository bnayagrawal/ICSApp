package xyz.bnayagrawal.android.icsapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import xyz.bnayagrawal.android.icsapp.dashboard.DashboardFragment;
import xyz.bnayagrawal.android.icsapp.event.EventFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int accentColor = -1;
    private int primaryColor = -1;
    private int primaryColorDark = -1;
    private int menu_selected_item_id;

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //updateUserThemePreferences();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        //check if the user has logged in or not
        if (!checkLogin()) {
            //user has not logged in
            Toast.makeText(getApplicationContext(), "Please login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        /*If activity has been recreated due to screen rotation*/
        if(savedInstanceState == null)
            setFragment();

        setNavigationView();
        setNavDrawerUserInfo();
    }

    /*No use so far*/
    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /*No use so far*/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*No use so far*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //check if the user has logged in or not
    protected boolean checkLogin() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE);
        String jwtToken = sharedPref.getString("USER_JWT_TOKEN", null);

        return (jwtToken != null);
    }

    //set nav drawer user name and email
    public void setNavDrawerUserInfo() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.SP_USER_FILE), Context.MODE_PRIVATE);
        String user_full_name = sharedPref.getString("USER_FIRST_NAME", "user") + " " + sharedPref.getString("USER_LAST_NAME", "name");
        String user_email = sharedPref.getString("USER_EMAIL", "username@email.xyz");

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
            if (count == 1)
                Toast.makeText(this, "Press back button once again to exit!", Toast.LENGTH_SHORT).show();
            if (count == 0)
                super.onBackPressed();
            else
                getSupportFragmentManager().popBackStack();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        menu_selected_item_id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (menu_selected_item_id) {
                    case R.id.nav_dashboard: {
                        //check if it the fragment is already being display
                        DashboardFragment tf = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("DASHBOARD_FRAGMENT");
                        if (tf != null && tf.isVisible())
                            break;
                        else {
                            //setting this fragment will clear backstack.
                            int count = getSupportFragmentManager().getBackStackEntryCount();
                            for (int i = 0; i < count; ++i) {
                                getSupportFragmentManager().popBackStackImmediate();
                            }
                            Toast.makeText(getApplicationContext(), "Press back button once again to exit!", Toast.LENGTH_SHORT).show();

                            //create the fragment
                            DashboardFragment fragment = new DashboardFragment();
                            fragment.setArguments(getIntent().getExtras());
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.replace(R.id.fragment_container, fragment, "DASHBOARD_FRAGMENT").commit();
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
                        //check if the fragment is already being display
                        EventFragment tf = (EventFragment) getSupportFragmentManager().findFragmentByTag("EVENTS_FRAGMENT");
                        if (tf != null && tf.isVisible())
                            break; //means this fragment is currently being displayed
                        else {
                            EventFragment fragment = new EventFragment();
                            fragment.setArguments(getIntent().getExtras());
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            ft.replace(R.id.fragment_container, fragment, "EVENTS_FRAGMENT").addToBackStack("EVENTS_FRAGMENT").commit();
                        }
                        break;
                    }
                    case R.id.nav_news:
                        break;
                    case R.id.nav_settings:
                        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                    default:
                        break;
                }
            }
        },300); //Perform operation after the drawer has closed properly

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Logout */
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(R.string.action_logout_logout, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getApplicationContext().getSharedPreferences(getString(R.string.SP_USER_FILE),Context.MODE_PRIVATE).edit().clear().apply();
                Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.action_logout_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setTitle(getString(R.string.action_logout_title));
        builder.setMessage(getString(R.string.action_logout_content));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_dashboard);
        navigationView.setItemIconTintList(null);

        /* SET NAV_HEADER_BACKGROUND_COLOR */
        if(primaryColor != -1)
            ((LinearLayout)navigationView.getHeaderView(0).findViewById(R.id.nav_header_content)).setBackgroundColor(primaryColor);
    }

    public void setToolbar() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    // To perform an action only after the drawer is closed
                }
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // No use so far
                }
            };
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        } else {
            drawer.addDrawerListener(null);
        }
    }

    private void updateUserThemePreferences() {
        /* TODO: SET USER THEME */
        setTheme(R.style.AppThemeDarkGray);

        /* Fetch currently applied theme colors */
        TypedValue typedValue = new TypedValue();
        TypedArray array = this.obtainStyledAttributes(
                typedValue.data, new int[] {
                        R.attr.colorAccent,
                        R.attr.colorPrimary,
                        R.attr.colorPrimaryDark
                });

        /* -1 ON FAILURE */
        accentColor = array.getColor(0, -1);
        primaryColor = array.getColor(1,-1);
        primaryColorDark = array.getColor(2,-1);
        array.recycle();
    }

    /* call to below method will set the dashboard fragment*/
    protected void setFragment() {
        getSupportFragmentManager().popBackStackImmediate();
        DashboardFragment fragment = new DashboardFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.replace(R.id.fragment_container, fragment, "DASHBOARD_FRAGMENT").commit();

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        DashboardFragment fd = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("DASHBOARD_FRAGMENT");
                        if (fd != null && fd.isVisible())
                            navigationView.setCheckedItem(R.id.nav_dashboard);
                        EventFragment fe = (EventFragment) getSupportFragmentManager().findFragmentByTag("EVENTS_FRAGMENT");
                        if (fe != null && fe.isVisible())
                            navigationView.setCheckedItem(R.id.nav_events);

                        //TODO: For each fragment do the same
                    }
                });
    }
}
