package com.provectus.public_transport.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provectus.public_transport.R;
import com.provectus.public_transport.model.ModelJson;
import com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Psihey on 13.08.2017.
 */

public class ListRoutesFragment extends Fragment {

    @BindView(R.id.recycler_view_routes)
    RecyclerView mRecViewRoutes;

    private Unbinder mUnbinder;
    //TODO : DELETE and move (implement set data) to the presenter
    private List<ModelJson> mData = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lisr_routes, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        //TODO : Delete after implement connect with server
        hardcode();
        initRecyclerView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecViewRoutes.setLayoutManager(linearLayoutManager);
        TramsAndTrolleyAdapter adapter = new TramsAndTrolleyAdapter(mData);
        mRecViewRoutes.setAdapter(adapter);
    }

    private void hardcode() {
        mData.add(new ModelJson(1, "tram"));
        mData.add(new ModelJson(2, "tram"));
        mData.add(new ModelJson(3, "tram"));
        mData.add(new ModelJson(4, "bus"));
        mData.add(new ModelJson(5, "bus"));
        mData.add(new ModelJson(6, "bus"));
    }

}
