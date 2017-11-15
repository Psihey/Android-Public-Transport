package com.provectus.public_transport.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.provectus.public_transport.R;
import com.provectus.public_transport.fragment.AboutProgramFragment;
import com.provectus.public_transport.fragment.mapfragment.impl.MapsFragmentImpl;
import com.provectus.public_transport.service.TransportRoutesService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String mGMailPackage = "com.google.android.gm";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private FragmentManager mFragmentManager;
    private MapsFragmentImpl mMapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(new Intent(this, TransportRoutesService.class));
        mFragmentManager = getSupportFragmentManager();
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
        mMapsFragment = new MapsFragmentImpl();
        mFragmentManager
                .beginTransaction()
                .replace(R.id.container_fragment_map, mMapsFragment, MapsFragmentImpl.TAG_MAP_FRAGMENT).commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_send_feedback) {
            if (getAllPackageForSendAction().contains(mGMailPackage)) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.setPackage(mGMailPackage);
                intent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.main_activity_email_address_array));
                intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.main_activity_email_title));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            } else {
                Snackbar snackbar = Snackbar.make(mDrawerLayout, R.string.main_activity_snack_bar_no_gmail_agent, Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        } else if (id == R.id.nav_about_program) {
            mFragmentManager
                    .beginTransaction()
                    .hide(mMapsFragment)
                    .add(R.id.container_fragment_map, new AboutProgramFragment(), AboutProgramFragment.TAG_ABOUT_PROGRAM_FRAGMENT)
                    .addToBackStack(null).commit();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private List<String> getAllPackageForSendAction() {
        List<String> packageNameList = new ArrayList<>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = this.getPackageManager();
        List<ResolveInfo> resInfoList = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfoList.isEmpty()) {
            for (ResolveInfo resolveInfo : resInfoList) {
                packageNameList.add(resolveInfo.activityInfo.packageName);
            }
        }
        return packageNameList;
    }

}
