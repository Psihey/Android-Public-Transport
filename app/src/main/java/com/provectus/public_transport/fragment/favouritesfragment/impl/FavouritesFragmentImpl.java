package com.provectus.public_transport.fragment.favouritesfragment.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.adapter.FavouritesSectionAdapter;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragment;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.utils.Const;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by Psihey on 13.10.2017.
 */

public class FavouritesFragmentImpl extends Fragment implements FavouritesFragment {

    @BindView(R.id.recycler_view_favourites)
    RecyclerView mFavouritesRecyclerView;
    private Unbinder mUnbinder;
    private FavouritesFragmentPresenter mFavouritesFragmentPresenter;
    private FavouritesSectionAdapter mTramSection;
    private FavouritesSectionAdapter mTrolleybusSection;
    private SectionedRecyclerViewAdapter mSectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        Logger.d("Create favourite");
        if (mFavouritesFragmentPresenter == null) {
            mFavouritesFragmentPresenter = new FavouritesFragmentPresenterImpl();
        }
        mFavouritesFragmentPresenter.bindView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mFavouritesFragmentPresenter.unbindView();
    }

    @Override
    public void initRecyclerView(List<TransportEntity> transportEntity) {
        List<TransportEntity> tramSection = new ArrayList<>();
        List<TransportEntity> trolleybusSection = new ArrayList<>();
        mSectionAdapter = new SectionedRecyclerViewAdapter();

        for (TransportEntity currentTransport : transportEntity) {
            if (currentTransport.getType().equals(TransportType.TRAM_TYPE)) {
                tramSection.add(currentTransport);
            } else {
                trolleybusSection.add(currentTransport);
            }
        }

        mFavouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTramSection = new FavouritesSectionAdapter(getContext(), Const.TransportType.TRAMS, tramSection, mSectionAdapter);
        mTrolleybusSection = new FavouritesSectionAdapter(getContext(), Const.TransportType.TROLLEYBUSES, trolleybusSection, mSectionAdapter);

        mSectionAdapter.addSection(mTramSection);
        mSectionAdapter.addSection(mTrolleybusSection);
        mFavouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mFavouritesRecyclerView.setAdapter(mSectionAdapter);

    }

    @Override
    public void updateData(List<TransportEntity> mTransportData) {
        List<TransportEntity> tramSection = new ArrayList<>();
        List<TransportEntity> trolleybusSection = new ArrayList<>();
        for (TransportEntity currentTransport : mTransportData) {
            if (currentTransport.getType().equals(TransportType.TRAM_TYPE)) {
                tramSection.add(currentTransport);
            } else {
                trolleybusSection.add(currentTransport);
            }
        }
        mTramSection.updateData(tramSection);
        mTrolleybusSection.updateData(trolleybusSection);
    }
}
