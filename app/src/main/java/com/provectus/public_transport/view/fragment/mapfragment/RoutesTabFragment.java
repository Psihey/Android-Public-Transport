package com.provectus.public_transport.view.fragment.mapfragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class RoutesTabFragment extends Fragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRoutesRecyclerView;
    @BindView(R.id.tv_tab_fragment_no_data)
    TextView tvNoData;

    private View view;
    private TramsAndTrolleyAdapter adapter;
    private int mPosition;

    private Unbinder mUnbinder;

    public static final String POSITION_PAR = "pos_par";

    public static RoutesTabFragment newInstance(int position) {
        RoutesTabFragment routesTabFragment = new RoutesTabFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(POSITION_PAR, position);
        routesTabFragment.setArguments(bundle);

        return routesTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        mPosition = getArguments().getInt(POSITION_PAR);
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent) {
        //TODO: After caching delete the method and load data from the database
        if (mPosition == routesEvent.getTransportType()) {
            initRecyclerView(routesEvent.getTransportRoutes());
        }

    }

    private void initRecyclerView(List<TransportRoutes> routes) {
        mRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TramsAndTrolleyAdapter(routes);
        mRoutesRecyclerView.setAdapter(adapter);
        tvNoData.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        EventBus.getDefault().unregister(this);
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
