package com.provectus.public_transport.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.model.TransportRoutes;

import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

import static com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter.TransportType.TRAM_TYPE;
import static com.provectus.public_transport.view.adapter.TramsAndTrolleyAdapter.TransportType.TROLLEYBUSES_TYPE;


/**
 * Created by Psihey on 11.08.2017.
 */

public class TramsAndTrolleyAdapter extends RecyclerView.Adapter<TramsAndTrolleyAdapter.TramsAndTrolleyViewHolder> {
    private List<TransportRoutes> mTransportRoutesData;

    enum TransportType {
        TRAM_TYPE("tram"),
        TROLLEYBUSES_TYPE("trolleybuses");

        private final String transportName;

        TransportType(String transportName) {
            this.transportName = transportName;
        }
    }


    public TramsAndTrolleyAdapter(List<TransportRoutes> data) {
        this.mTransportRoutesData = data;
    }

    @Override
    public TramsAndTrolleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bundle_tram_trolleybus, parent, false);

        return new TramsAndTrolleyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TramsAndTrolleyViewHolder holder, int position) {
        final TransportRoutes transportRoutes = mTransportRoutesData.get(position);

        if (transportRoutes.getType().equals(TRAM_TYPE.transportName)) {
            holder.ivLogoTransport.setImageResource(R.drawable.ic_train_public_gray);
        } else if (transportRoutes.getType().equals(TROLLEYBUSES_TYPE.transportName)) {
            holder.ivLogoTransport.setImageResource(R.drawable.ic_front_bus_gray);
        }

        holder.tvRoutesNumber.setText(String.valueOf(mTransportRoutesData.get(position).getNumber()));

    }

    @Override
    public int getItemCount() {
        return mTransportRoutesData.size();
    }

    class TramsAndTrolleyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_number_routes)
        TextView tvRoutesNumber;
        @BindView(R.id.image_view_logo_transport)
        ImageView ivLogoTransport;

        TramsAndTrolleyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
