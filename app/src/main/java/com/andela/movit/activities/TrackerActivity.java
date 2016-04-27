package com.andela.movit.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.andela.movit.R;
import com.andela.movit.utilities.Utility;

public class TrackerActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    private NavigationView navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        prepareToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                toggleDrawer();
                return true;
            default:
                break;
        }
        return true;
    }

    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(navDrawer)) {
            drawerLayout.closeDrawer(navDrawer);
        } else {
            drawerLayout.openDrawer(navDrawer);
        }
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_36dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        prepareNavDrawer();
    }

    private void prepareNavDrawer() {
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navDrawer = (NavigationView)findViewById(R.id.nav_drawer);
        View drawerHeader = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        navDrawer.addHeaderView(drawerHeader);
        navDrawer.setNavigationItemSelectedListener(getDrawerSelectionListener());
    }

    private NavigationView.OnNavigationItemSelectedListener getDrawerSelectionListener() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                toggleDrawer();
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.my_movements:
                        Utility.startActivity(TrackerActivity.this, MovementActivity.class);
                        break;
                    case R.id.my_locations:
                        Utility.startActivity(TrackerActivity.this, VisitActivity.class);
                    default:
                        break;
                }
                return false;
            }
        };
    }
}
