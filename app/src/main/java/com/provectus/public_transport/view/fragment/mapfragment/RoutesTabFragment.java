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

import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportRoutes;
import com.provectus.public_transport.model.TransportType;
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class RoutesTabFragment extends Fragment {

    // TODO: 23.08.17 Rename
    public static final String POSITION_PAR = "pos_par";

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRoutesRecyclerView;
    @BindView(R.id.tv_tab_fragment_no_data)
    TextView tvNoData;

    // TODO: 23.08.17 Move to local
    private View view;
    private TramsAndTrolleyAdapter adapter;
    private TransportType mType;

    private Unbinder mUnbinder;

    // TODO: 23.08.17 Use adapter
    private List<TransportRoutes> myRoutes;

    public static RoutesTabFragment newInstance(TransportType transportType) {
        RoutesTabFragment routesTabFragment = new RoutesTabFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(POSITION_PAR, transportType);
        routesTabFragment.setArguments(bundle);
        return routesTabFragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        myRoutes = new ArrayList<>();
        initRecyclerView();
        mType = (TransportType) getArguments().get(POSITION_PAR);
        return view;
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
    public void onDestroy() {
        super.onDestroy();
    }

    private void hideTvNoData() {
        if (!myRoutes.isEmpty()) {
            tvNoData.setVisibility(View.GONE);
        }
    }


    protected void onAttachToContext(Context context) {
        Context mContext = context;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllRoutes(BusEvents.SendRoutesEvent routesEvent) {
        //TODO: After caching delete the method and load data from the database
        if (mType == routesEvent.getTransportType()) {
            myRoutes = routesEvent.getTransportRoutes();
            initAdapter();
            hideTvNoData();
        }

    }

    private void initRecyclerView() {
        mRoutesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
        hideTvNoData();
    }

    private void initAdapter() {
        if (!myRoutes.isEmpty()) {
            adapter = new TramsAndTrolleyAdapter(myRoutes);
            mRoutesRecyclerView.setAdapter(adapter);
        }
    }

}
