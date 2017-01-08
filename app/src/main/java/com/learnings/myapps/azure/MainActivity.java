package com.learnings.myapps.azure;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.learnings.myapps.azure.Fragments.BanksFragment;
import com.learnings.myapps.azure.Fragments.DepositsFragment;
import com.learnings.myapps.azure.Fragments.OffersFragment;
import com.learnings.myapps.azure.Fragments.OpenFragment;
import com.learnings.myapps.azure.Fragments.QuickCalculationFragment;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import java.net.MalformedURLException;

import static com.learnings.myapps.azure.Fragments.DataContainer.mClient;
import static com.learnings.myapps.azure.Fragments.DataContainer.mEmail;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    public NavigationView getNavigationView() {
        return navigationView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mClient = new MobileServiceClient(
                    "https://mobileapp99.azurewebsites.net",
                    this
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Intent intent = getIntent();
        if (intent.hasExtra("email"))
            mEmail = intent.getStringExtra("email");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();
        if (id == R.id.nav_first) {
            DepositsFragment depositsFragment = new DepositsFragment();
            ft.replace(R.id.content_main, depositsFragment);
        } else if (id == R.id.nav_second) {
            OpenFragment openFragment = new OpenFragment();
            ft.replace(R.id.content_main, openFragment);
        } else if (id == R.id.nav_third) {
            BanksFragment banksFragment = new BanksFragment();
            ft.replace(R.id.content_main, banksFragment);
        } else if (id == R.id.nav_fourth) {
            OffersFragment offersFragment = new OffersFragment();
            ft.replace(R.id.content_main, offersFragment);
        } else if (id == R.id.nav_fifth) {
            QuickCalculationFragment quickCalculationFragment = new QuickCalculationFragment();
            ft.replace(R.id.content_main, quickCalculationFragment);
        }
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
