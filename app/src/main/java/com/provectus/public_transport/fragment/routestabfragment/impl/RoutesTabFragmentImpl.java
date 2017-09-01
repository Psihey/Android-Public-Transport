package com.provectus.public_transport.fragment.routestabfragment.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.TramsAndTrolleyAdapter;
import com.provectus.public_transport.fragment.routestabfragment.RoutesTabFragment;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.TransportType;
import com.provectus.public_transport.service.TransportRoutesService;
import com.provectus.public_transport.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class RoutesTabFragmentImpl extends Fragment implements RoutesTabFragment {

    private static final String BUNDLE_TRANSPORT_TYPE = "transport_type";

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRoutesRecyclerView;
    @BindView(R.id.tab_fragment_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.tv_tab_fragment_no_data)
    TextView textViewNoData;
    @BindView(R.id.bottom_sheet_btn_update)
    Button btnUdate;

    private RoutesTabFragmentPresenterImpl mTabFragmentPresenter;
    private TramsAndTrolleyAdapter mAdapter;
    private TransportType mType;

    private Unbinder mUnbinder;

    public static RoutesTabFragmentImpl newInstance(TransportType transportType) {
        RoutesTabFragmentImpl routesTabFragmentImpl = new RoutesTabFragmentImpl();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TRANSPORT_TYPE, transportType);
        routesTabFragmentImpl.setArguments(bundle);
        return routesTabFragmentImpl;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = (TransportType) getArguments().get(BUNDLE_TRANSPORT_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTabFragmentPresenter == null) {
            mTabFragmentPresenter = new RoutesTabFragmentPresenterImpl();
        }
        mTabFragmentPresenter.bindView(this);
        mTabFragmentPresenter.setTransportType(mType);
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

    @Override
    public void initRecyclerView(List<TransportEntity> transportEntity) {
        mRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TramsAndTrolleyAdapter(transportEntity);
        mRoutesRecyclerView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
        setErrorVisible(View.GONE);
    }

    @Override
    public void checkMyServiceRunning() {
        if (!Utils.isMyServiceRunning(TransportRoutesService.class, getActivity())) {
            progressBar.setVisibility(View.GONE);
            setErrorVisible(View.VISIBLE);
            btnUdate.setOnClickListener(view -> {
                setErrorVisible(View.GONE);
                getActivity().startService(new Intent(getActivity(), TransportRoutesService.class));
                progressBar.setVisibility(View.VISIBLE);
            });

        }
    }

    private void setErrorVisible(int visible) {
        textViewNoData.setVisibility(visible);
        btnUdate.setVisibility(visible);
    }

}