package com.provectus.public_transport.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.model.StopDetailEntity;
import com.provectus.public_transport.utils.Const;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class StopDetailSectionAdapter extends StatelessSection {

    private List<StopDetailEntity> mStopDetailList;
    private String mTitle;
    private Context mContext;

    public StopDetailSectionAdapter(Context context, List<StopDetailEntity> stopDetailEntityList, String title) {
        super(new SectionParameters.Builder(R.layout.item_item_stop_detail)
                .headerResourceId(R.layout.item_header_detail_stop)
                .build());
        this.mContext = context;
        this.mStopDetailList = stopDetailEntityList;
        this.mTitle = title;
    }

    @Override
    public int getContentItemsTotal() {
        return mStopDetailList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new StopDetailItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        StopDetailItemViewHolder stopDetailItemViewHolder = (StopDetailItemViewHolder) holder;

        StopDetailEntity stopDetailEntity = mStopDetailList.get(position);

        stopDetailItemViewHolder.mTextViewTransportNumber.setText(mContext.getResources().getString(R.string.text_view_item_tram_trooley_transport_number, String.valueOf(mStopDetailList.get(position).getNumber())));
        stopDetailItemViewHolder.mTextViewFirstStop.setText(stopDetailEntity.getFirstStopping());
        stopDetailItemViewHolder.mTextViewLastStop.setText(stopDetailEntity.getLastStopping());
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new StopDetailHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        StopDetailHeaderViewHolder stopDetailHeaderViewHolder = (StopDetailHeaderViewHolder) holder;
        stopDetailHeaderViewHolder.mTextViewTypeTransport.setText(mTitle);
        if (mTitle.equals(Const.TransportType.TRAMS)) {
            stopDetailHeaderViewHolder.mTransportIcon.setImageResource(R.drawable.ic_tram_gray_24_dp);
        } else {
            stopDetailHeaderViewHolder.mTransportIcon.setImageResource(R.drawable.ic_trolley_gray_24_dp);
        }
    }

    class StopDetailItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_stop_detail_transport_number)
        TextView mTextViewTransportNumber;
        @BindView(R.id.tv_first_stop_stop_detail)
        TextView mTextViewFirstStop;
        @BindView(R.id.tv_last_stop_stop_detail)
        TextView mTextViewLastStop;

        StopDetailItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class StopDetailHeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_header_detail_stop)
        TextView mTextViewTypeTransport;
        @BindView(R.id.iv_header_detail_stop)
        ImageView mTransportIcon;

        StopDetailHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
