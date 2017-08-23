package com.provectus.public_transport.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.provectus.public_transport.fragment.mapfragment.impl.MapsFragmentImpl;
import com.provectus.public_transport.R;

public class MainActivity extends AppCompatActivity {

    public final static String MAP_FRAGMENT = "f_map";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpToolbar();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new MapsFragmentImpl(), MAP_FRAGMENT).commit();
    }

    // TODO: 23.08.17 Use styles
    private void setUpToolbar() {
        if (getSupportActionBar()!= null && getSupportActionBar().isShowing()) getSupportActionBar().hide();
    }

}
