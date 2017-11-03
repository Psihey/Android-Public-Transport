package com.provectus.public_transport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private static final String[] mEmailForFeedback = {"publictransportcustomercare@gmail.com"};
    private static final String mEmailTitleForFeedback = "Feedback on the operation of the PublicTransport application";
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
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, mEmailForFeedback);
            intent.putExtra(Intent.EXTRA_SUBJECT, mEmailTitleForFeedback);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
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

}
