package com.provectus.public_transport.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.fragment.routestabfragment.impl.TrolleybusFragmentPresenterImpl;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.fragment.routestabfragment.TramFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;
import com.provectus.public_transport.model.converter.TransportType;
import com.provectus.public_transport.utils.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class FavouritesSectionAdapter extends StatelessSection {
    private String mTitle;
    private List<TransportEntity> mListTransportEntity;
    private SectionedRecyclerViewAdapter mSectionAdapter;
    private Context mContext;
    private MapsFragmentPresenter mMapsFragmentPresenter;
    private FavouritesFragmentPresenter mFavouritesFragmentPresenter;
    private TramFragmentPresenter mTramFragmentPresenter;
    private TrolleybusFragmentPresenterImpl mTrolleybusFragmentPresenter;

    public FavouritesSectionAdapter(Context context,
                                    String title,
                                    List<TransportEntity> list,
                                    SectionedRecyclerViewAdapter sectionAdapter,
                                    MapsFragmentPresenter mapsFragmentPresenter,
                                    FavouritesFragmentPresenter favouritesFragmentPresenter,
                                    TramFragmentPresenter routesTabFragmentPresenter,
                                    TrolleybusFragmentPresenterImpl trolleybusFragmentPresenter) {
        super(new SectionParameters.Builder(R.layout.item_item_bundle_favourites_tram_trolleybus)
                .headerResourceId(R.layout.item_header_bundle_favourites_tram_trolleybus)
                .build());
        this.mContext = context;
        this.mTitle = title;
        this.mListTransportEntity = list;
        this.mSectionAdapter = sectionAdapter;
        this.mMapsFragmentPresenter = mapsFragmentPresenter;
        this.mFavouritesFragmentPresenter = favouritesFragmentPresenter;
        this.mTramFragmentPresenter = routesTabFragmentPresenter;
        this.mTrolleybusFragmentPresenter = trolleybusFragmentPresenter;
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
        TransportEntity currentRoute = mListTransportEntity.get(position);

        FavouritesItemViewHolder itemViewHolder = (FavouritesItemViewHolder) holder;
        itemViewHolder.mTextViewTransportNumber.setText(mContext.getResources().getString(R.string.text_view_item_tram_trooley_transport_number, String.valueOf(mListTransportEntity.get(position).getNumber())));
        itemViewHolder.mTextViewFavouritesFirstStop.setText(currentRoute.getFirstStop());
        itemViewHolder.mTextViewFavouritesLastStop.setText(currentRoute.getLastStop());
        if (currentRoute.isSelected()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetSelectedBackground));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetBackground));
        }

        itemViewHolder.mImageViewDeleteFavourites.setOnClickListener(v -> mFavouritesFragmentPresenter.deleteFavourites(currentRoute));


        itemViewHolder.itemView.setOnClickListener(v -> {
            if (currentRoute.isSelected()) {
                currentRoute.setIsSelected(false);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetBackground));
            } else {
                currentRoute.setIsSelected(true);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetSelectedBackground));
            }

            if (currentRoute.getType() == TransportType.TROLLEYBUSES_TYPE) {
                mTrolleybusFragmentPresenter.getDataForUpdateRecyclerView(currentRoute);
            } else {
                mTramFragmentPresenter.getDataForUpdateRecyclerView(currentRoute);
            }
            mMapsFragmentPresenter.onSelectCurrentRoute(currentRoute);

        });
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new FavouritesHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        FavouritesHeaderViewHolder headerViewHolder = (FavouritesHeaderViewHolder) holder;

        headerViewHolder.mTextViewTypeTransport.setText(mTitle);
        if (mTitle.equals(Const.TransportType.TRAMS)) {
            headerViewHolder.mTransportIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
        } else {
            headerViewHolder.mTransportIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
        }

    }

    public void updateData(List<TransportEntity> transportEntities) {
        for (TransportEntity currentEntity : mListTransportEntity) {
            for (TransportEntity newEntity : transportEntities) {
                if (currentEntity.getServerId() == newEntity.getServerId()) {
                    currentEntity.setIsSelected(newEntity.isSelected());
                }
            }
        }
        mSectionAdapter.notifyDataSetChanged();
    }

    class FavouritesItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_number_transport_favourites)
        TextView mTextViewTransportNumber;
        @BindView(R.id.iv_delete_favourites)
        ImageView mImageViewDeleteFavourites;
        @BindView(R.id.tv_favourites_first_stop)
        TextView mTextViewFavouritesFirstStop;
        @BindView(R.id.tv_favourites_last_stop)
        TextView mTextViewFavouritesLastStop;

        FavouritesItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class FavouritesHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header_favourites)
        TextView mTextViewTypeTransport;
        @BindView(R.id.iv_header_favourites)
        ImageView mTransportIcon;

        FavouritesHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
