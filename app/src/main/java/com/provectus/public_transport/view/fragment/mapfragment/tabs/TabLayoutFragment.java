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
import com.provectus.public_transport.view.util.consts.Constants;
import com.provectus.public_transport.view.util.consts.TagFragmentConst;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Evgeniy on 8/17/2017.
 */

public class TabLayoutFragment extends Fragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRecyclerViewRoutes;

    private View view;
    private TramsAndTrolleyAdapter adapter;
    private int mPosition;

    private Unbinder mUnbinder;

    public static TabLayoutFragment newInstance(FragmentManager fm, int position) {
        TabLayoutFragment tabLayoutFragment =
                (TabLayoutFragment) fm.findFragmentByTag(TagFragmentConst.TAG_BUSES_LIST_FRAGMENT);
        if (tabLayoutFragment == null) {
            tabLayoutFragment = new TabLayoutFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putInt(TabLayoutFragment.class.getCanonicalName(), position);

        return tabLayoutFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mPosition = getArguments().getInt(TabLayoutFragment.class.getCanonicalName());
        initRecyclerView();
        return view;
    }


    private void initRecyclerView() {
        switch (mPosition) {
            case Constants.TabPosition.BUS:
                break;
            case Constants.TabPosition.TRAM:
                break;
            case Constants.TabPosition.PARKING:
                break;
        }
        mRecyclerViewRoutes.setLayoutManager(new LinearLayoutManager(getContext()));
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
