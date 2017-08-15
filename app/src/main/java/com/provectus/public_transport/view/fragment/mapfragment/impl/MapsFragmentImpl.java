package com.provectus.public_transport.view.fragment.mapfragment.impl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.provectus.public_transport.R;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;

import com.provectus.public_transport.view.fragment.mapfragment.MapsFragment;
import com.provectus.public_transport.view.fragment.mapfragment.MapsFragmentPresenter;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/10/2017.
 */

public class MapsFragmentImpl extends Fragment implements MapsFragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRecyclerViewRoutes;

    private MapsFragmentPresenter mMapsPresenter;
    private Unbinder mUnbinder;
    private ProgressDialog mProgressDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        mMapsPresenter = new MapsFragmentPresenterImpl();
        mMapsPresenter.bindView(this);
        initProgressDialog();
        BottomSheetBehavior mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
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

    @Override
    public void onStop() {
        super.onStop();
        mMapsPresenter.unbindView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void initRecyclerView(List<TransportRoutes> transportRoutes) {
        mProgressDialog.cancel();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerViewRoutes.setLayoutManager(linearLayoutManager);
        TramsAndTrolleyAdapter adapter = new TramsAndTrolleyAdapter(transportRoutes);
        mRecyclerViewRoutes.setAdapter(adapter);
    }

    @Override
    public void showDialogError() {
        mProgressDialog.cancel();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_error_internet_title);
        builder.setMessage(R.string.dialog_error_internet_message);
        builder.setIcon(R.drawable.common_google_signin_btn_icon_dark_focused);
        builder.setPositiveButton(R.string.dialog_error_internet_positive_button,(dialog, which) -> {
            mProgressDialog.show();
            mMapsPresenter.getRoutesFromServer();
        });
        builder.setNegativeButton(R.string.dialog_error_internet_negative_button, (dialog, which) -> {
            getActivity().onBackPressed();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    protected void onAttachToContext(Context context) {
        Context mContext = context;
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setTitle(getString(R.string.progrees_dialog_network_title));
        mProgressDialog.setMessage(getString(R.string.progrees_dialog_network_message));
        mProgressDialog.setIcon(R.drawable.common_google_signin_btn_icon_dark_focused);
        mProgressDialog.show();
    }

}
