package com.provectus.public_transport.fragment.mapfragment.impl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TramsAndTrolleyAdapter;
import com.provectus.public_transport.fragment.mapfragment.RoutesTabFragment;
import com.provectus.public_transport.model.TransportType;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class RoutesTabFragmentImpl extends Fragment implements RoutesTabFragment {

    private static final String TRANSPORT_TYPE = "transport_type";

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRoutesRecyclerView;
    @BindView(R.id.tv_tab_fragment_no_data)
    TextView textViewNoData;

    private RoutesTabFragmentPresenterImpl mTabFragmentPresenter;
    private TramsAndTrolleyAdapter mAdapter;
    private TransportType mType;

    private Unbinder mUnbinder;

    public static RoutesTabFragmentImpl newInstance(TransportType transportType) {
        RoutesTabFragmentImpl routesTabFragmentImpl = new RoutesTabFragmentImpl();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRANSPORT_TYPE, transportType);
        routesTabFragmentImpl.setArguments(bundle);
        return routesTabFragmentImpl;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mType = (TransportType) getArguments().get(TRANSPORT_TYPE);
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTabFragmentPresenter == null) {
            mTabFragmentPresenter = new RoutesTabFragmentPresenterImpl();
        }
        mTabFragmentPresenter.bindView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mTabFragmentPresenter.unbindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void initRecyclerView() {
        mRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TramsAndTrolleyAdapter(new ArrayList<>());
        mRoutesRecyclerView.setAdapter(mAdapter);
    }

}
