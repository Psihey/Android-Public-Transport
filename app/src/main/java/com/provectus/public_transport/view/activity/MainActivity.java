package com.provectus.public_transport.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.provectus.public_transport.view.fragment.mapfragment.impl.MapsFragmentImpl;
import com.provectus.public_transport.R;
import com.provectus.public_transport.view.util.consts.TagFragmentConst;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setUpToolbar();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,
                new MapsFragmentImpl(), TagFragmentConst.MAP_FRAGMENT).commit();
    }

    private void setUpToolbar() {
        if (getSupportActionBar()!= null && getSupportActionBar().isShowing()) getSupportActionBar().hide();
    }

}
