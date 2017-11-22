package com.provectus.public_transport.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.fragment.favouritesfragment.FavouritesFragmentPresenter;
import com.provectus.public_transport.fragment.mapfragment.MapsFragmentPresenter;
import com.provectus.public_transport.model.TransportEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TramsAndTrolleyAdapter extends RecyclerView.Adapter<TramsAndTrolleyAdapter.TramsAndTrolleyViewHolder> implements Filterable {
    private static final float mItemViewTransparentAvailable = 1;
    private static final float mItemViewTransparentNotAvailable = 0.4f;
    private List<TransportEntity> mTransportRoutesData;
    private List<TransportEntity> mFilteredList;
    private Context mContext;
    private MapsFragmentPresenter mMapsFragmentPresenter;
    private FavouritesFragmentPresenter mFavouritesFragmentPresenter;

    public TramsAndTrolleyAdapter(Context context, List<TransportEntity> data, MapsFragmentPresenter mapsFragmentPresenter, FavouritesFragmentPresenter favouritesFragmentPresenter) {
        this.mContext = context;
        this.mTransportRoutesData = data;
        this.mMapsFragmentPresenter = mapsFragmentPresenter;
        this.mFavouritesFragmentPresenter = favouritesFragmentPresenter;
        this.mFilteredList = data;
    }

    @Override
    public TramsAndTrolleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bundle_tram_trolleybus, parent, false);
        return new TramsAndTrolleyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TramsAndTrolleyViewHolder holder, int position) {
        final TransportEntity transportRoutes = mFilteredList.get(position);
        holder.mTvRoutesNumber.setText(mContext.getResources().getString(R.string.text_view_item_tram_trooley_transport_number, String.valueOf(transportRoutes.getNumber())));
        holder.itemView.setOnFocusChangeListener((v, hasFocus) -> transportRoutes.setIsSelected(hasFocus));
        holder.mTvFirstStop.setText(transportRoutes.getFirstStop());
        holder.mTvLastStop.setText(transportRoutes.getLastStop());
        setItemViewCondition(holder.itemView, transportRoutes);

        if (transportRoutes.isSelected()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetSelectedBackground));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetBackground));
        }

        holder.itemView.setOnClickListener(v -> {
            if (transportRoutes.isSelected()) {
                transportRoutes.setIsSelected(false);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetBackground));
            } else {
                transportRoutes.setIsSelected(true);
                holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorBottomSheetSelectedBackground));
            }

            mMapsFragmentPresenter.onSelectCurrentRoute(transportRoutes);
            mFavouritesFragmentPresenter.updateRecyclerView(mFilteredList);
        });

        holder.mImageButtonRouteInfo.setOnClickListener(v -> mMapsFragmentPresenter.getRouteInformation(transportRoutes));
    }

    @Override
    public int getItemCount() {
        if (mFilteredList != null) {
            return mFilteredList.size();
        } else return 0;
    }

    private void setItemViewCondition(View itemView, TransportEntity transportEntity) {
        if (!transportEntity.isAvailable()) {
            itemView.setEnabled(false);
            itemView.setAlpha(mItemViewTransparentNotAvailable);
        } else {
            itemView.setEnabled(true);
            itemView.setAlpha(mItemViewTransparentAvailable);
        }
    }

    public void updateData(TransportEntity transportRoute) {
        for (TransportEntity currentEntity : mTransportRoutesData) {
            if (currentEntity.getServerId() == transportRoute.getServerId()) {
                currentEntity.setIsSelected(transportRoute.isSelected());
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mTransportRoutesData;
                } else {
                    List<TransportEntity> filteredList = new ArrayList<>();
                    for (TransportEntity transportEntity : mTransportRoutesData) {
                        String firstStop = transportEntity.getFirstStop().toLowerCase();
                        String lastStop = transportEntity.getLastStop().toLowerCase();
                        String searchStringForStops = charString.toLowerCase();

                        if (String.valueOf(transportEntity.getNumber()).contains(charString)
                                || firstStop.contains(searchStringForStops)
                                || lastStop.contains(searchStringForStops)) {
                            filteredList.add(transportEntity);
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (List<TransportEntity>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class TramsAndTrolleyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_item_tram_trooley_transport_number)
        TextView mTvRoutesNumber;
        @BindView(R.id.image_button_route_info)
        ImageView mImageButtonRouteInfo;
        @BindView(R.id.tv_first_stop)
        TextView mTvFirstStop;
        @BindView(R.id.tv_last_stop)
        TextView mTvLastStop;

        TramsAndTrolleyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
