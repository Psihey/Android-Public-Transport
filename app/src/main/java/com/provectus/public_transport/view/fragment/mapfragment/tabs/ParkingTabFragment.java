package com.provectus.public_transport.view.fragment.mapfragment.tabs;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provectus.public_transport.R;
import com.provectus.public_transport.view.util.consts.TagFragmentConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class ParkingTabFragment extends Fragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRecyclerViewParkingRoutes;

    private Unbinder mUnbinder;
    private View view;

    public static ParkingTabFragment newInstance(FragmentManager fm) {
        ParkingTabFragment parkingTabFragment =
                (ParkingTabFragment) fm.findFragmentByTag(TagFragmentConst.TAG_PARKING_LIST_FRAGMENT);
        if (parkingTabFragment == null) {
            parkingTabFragment = new ParkingTabFragment();
        }
        return parkingTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    private void initRecyclerView() {
        mRecyclerViewParkingRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
        //TODO: Take the data from the Database and transfer it to the adapter
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= 23) {
            onAttachToContext(context);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < 23) {
            onAttachToContext(activity);
        }
    }

    protected void onAttachToContext(Context context) {
        Context mContext = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
