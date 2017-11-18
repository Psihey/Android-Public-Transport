package com.provectus.public_transport.fragment.routestabfragment.impl;

import android.content.Intent;
import android.os.Bundle;
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
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.fragment.routestabfragment.TramFragmentPresenter;
import com.provectus.public_transport.fragment.routestabfragment.TransportFragment;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.service.TransportRoutesService;
import com.provectus.public_transport.utils.Const;
import com.provectus.public_transport.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TramFragmentImpl extends Fragment implements TransportFragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRoutesRecyclerView;
    @BindView(R.id.tab_fragment_progress_bar)
    ProgressBar mProgressBarNoItem;
    @BindView(R.id.bottom_sheet_btn_update)
    Button mBtnUpdate;
    @BindView(R.id.tv_wait_for_loading)
    TextView mBtnLoading;
    @BindView(R.id.tv_server_no_responding)
    TextView mTextViewNoServerResponding;

    private TramFragmentPresenter mTabFragmentPresenter;
    private Unbinder mUnbinder;
    private TramsAndTrolleyAdapter mTramAdapter;
    private MapsFragmentPresenter mMapsFragmentPresenter;
    private FavouritesFragmentPresenter mFavouritesFragmentPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        if (mTabFragmentPresenter == null) {
            mTabFragmentPresenter = new TramFragmentPresenterImpl();
        }
        mTabFragmentPresenter.bindView(this);
        mRoutesRecyclerView.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mTabFragmentPresenter.unregisteredEventBus();
        mTabFragmentPresenter.unbindView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initRecyclerView(List<TransportEntity> transportEntity) {
        mRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTramAdapter = new TramsAndTrolleyAdapter(getContext(), transportEntity, mMapsFragmentPresenter, mFavouritesFragmentPresenter);
        EventBus.getDefault().post(new BusEvents.SendTramsAndTrolleyAdapter(mTramAdapter, Const.TransportType.TRAMS_ADAPTER));
        mRoutesRecyclerView.setAdapter(mTramAdapter);
        mProgressBarNoItem.setVisibility(View.GONE);
        mTextViewNoServerResponding.setVisibility(View.INVISIBLE);
        setErrorVisible(View.GONE);
    }

    @Override
    public void checkMyServiceRunning() {
        if (!Utils.isMyServiceRunning(TransportRoutesService.class, getActivity())) {
            mProgressBarNoItem.setVisibility(View.GONE);
            setErrorVisible(View.VISIBLE);
            mBtnUpdate.setOnClickListener(view -> {
                startService();
            });
        }
    }

    @Override
    public void serviceEndWorked() {
        mBtnLoading.setVisibility(View.GONE);
        mRoutesRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateData(TransportEntity transportEntity) {
        if (mTramAdapter != null) {
            mTramAdapter.updateData(transportEntity);
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getMapsFragmentPresenter(BusEvents.SendMapsFragmentPresenter event) {
        mMapsFragmentPresenter = event.getMapsFragmentPresenter();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void getFavouritesFragmentPresenter(BusEvents.SendFavouriteFragmentPresenter event) {
        mFavouritesFragmentPresenter = event.getFavouritesFragmentPresenter();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void serverNotResponding(BusEvents.ServerNotResponding event) {
        if (mTramAdapter == null) {
            setErrorVisible(View.GONE);
            mTextViewNoServerResponding.setVisibility(View.VISIBLE);
        }
    }

    private void setErrorVisible(int visible) {
        mBtnUpdate.setVisibility(visible);
    }

    private void startService() {
        setErrorVisible(View.GONE);
        getActivity().startService(new Intent(getActivity(), TransportRoutesService.class));
        mProgressBarNoItem.setVisibility(View.VISIBLE);
    }
}
