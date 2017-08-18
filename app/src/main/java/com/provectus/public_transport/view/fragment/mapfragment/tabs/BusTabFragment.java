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
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;
import com.provectus.public_transport.view.util.consts.TagFragmentConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class BusTabFragment extends Fragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRecyclerViewBusRoutes;

    private View view;
    private TramsAndTrolleyAdapter adapter;

    private Unbinder mUnbinder;

    public static BusTabFragment newInstance(FragmentManager fm) {
        BusTabFragment busTabFragment =
                (BusTabFragment) fm.findFragmentByTag(TagFragmentConst.TAG_BUSES_LIST_FRAGMENT);
        if (busTabFragment == null) {
            busTabFragment = new BusTabFragment();
        }
        return busTabFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initRecyclerView();
        return view;
    }


    private void initRecyclerView() {
        mRecyclerViewBusRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
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
