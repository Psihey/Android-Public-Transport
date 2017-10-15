package com.provectus.public_transport.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Psihey on 13.10.2017.
 */

public class FavouritesSectionAdapter extends StatelessSection {
    private String mTitle;
    private List<TransportEntity> mListTransportEntity;

    public FavouritesSectionAdapter(String title, List<TransportEntity> list) {
        super(R.layout.header, R.layout.item);
        this.mTitle = title;
        this.mListTransportEntity = list;
    }


    @Override
    public int getContentItemsTotal() {
        return mListTransportEntity.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new FavouritesItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
    FavouritesItemViewHolder favouritesViewHolder = (FavouritesItemViewHolder)holder;
        favouritesViewHolder.textView.setText(String.valueOf(mListTransportEntity.get(position).getNumber()));
        favouritesViewHolder.mImageViewDelete.setOnClickListener(v -> EventBus.getDefault().post(new BusEvents.DeleteFavourites(mListTransportEntity.get(position))));
        favouritesViewHolder.mItemFavourites.setOnClickListener(v ->{
            mListTransportEntity.get(position).setIsSelected(true);
            EventBus.getDefault().post(new BusEvents.SendChosenRoute(mListTransportEntity.get(position)));
        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new FavouritesHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        FavouritesHeaderViewHolder headerViewHolder = (FavouritesHeaderViewHolder)holder;
        headerViewHolder.textView.setText(mTitle);
    }

    class FavouritesItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rr)
        TextView textView;
        @BindView(R.id.iv_delete_favourites)
        ImageView mImageViewDelete;
        @BindView(R.id.container_favourites)
        RelativeLayout mItemFavourites;

        FavouritesItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FavouritesHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.eee)
        TextView textView;

        FavouritesHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
