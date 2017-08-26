package com.provectus.public_transport.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.provectus.public_transport.fragment.mapfragment.impl.MapsFragmentImpl;
import com.provectus.public_transport.R;
import com.provectus.public_transport.service.TransportRoutesService;

import static com.provectus.public_transport.utils.Const.TAG_MAP_FRAGMENT;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, TransportRoutesService.class));
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment_map,
                new MapsFragmentImpl(), TAG_MAP_FRAGMENT).commit();
    }

}
