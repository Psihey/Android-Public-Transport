package com.provectus.public_transport.view.fragment.mapfragment.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.provectus.public_transport.R;
import com.provectus.public_transport.view.fragment.ListRoutesFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.util.consts.TagFragmentConst;


import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment {


    private Context mContext;
    private MapsFragmentPresenterImpl mapsPresenter;
    private Unbinder mUnbinder;
    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mapsPresenter = new MapsFragmentPresenterImpl();
        mapsPresenter.bindView(this);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        return view;
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
        mContext = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        mapsPresenter.unbindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick(R.id.btn_all_routes)
    public void getAllRoutes() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ListRoutesFragment listRoutesFragment = new ListRoutesFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container_transport,listRoutesFragment,TagFragmentConst.TAG_LIST_TRANSPORT_FRAGMENT)
                .commit();
    }

}
