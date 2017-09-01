package com.provectus.public_transport.adapter;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.provectus.public_transport.R;
import com.provectus.public_transport.eventbus.BusEvents;
import com.provectus.public_transport.model.TransportEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Psihey on 11.08.2017.
 */

public class TramsAndTrolleyAdapter extends RecyclerView.Adapter<TramsAndTrolleyAdapter.TramsAndTrolleyViewHolder> {
    private List<TransportEntity> mTransportRoutesData;
    private boolean mIsSelectRoute;

    public TramsAndTrolleyAdapter(List<TransportEntity> data) {
        this.mTransportRoutesData = data;
    }

    @Override
    public TramsAndTrolleyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bundle_tram_trolleybus, parent, false);

        return new TramsAndTrolleyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TramsAndTrolleyViewHolder holder, int position) {
        final TransportEntity transportRoutes = mTransportRoutesData.get(position);

        switch (transportRoutes.getType()) {
            case TRAM_TYPE:
                holder.ivLogoTransport.setImageResource(R.drawable.ic_tram_public_gray);
                break;
            case TROLLEYBUSES_TYPE:
                holder.ivLogoTransport.setImageResource(R.drawable.ic_front_bus_gray);
                break;
            default:
                Logger.d("TransportEntity Type is Invalid");
                break;
        }

        holder.tvRoutesNumber.setText(String.valueOf(mTransportRoutesData.get(position).getNumber()));

        holder.checkBoxSelectRout.setOnCheckedChangeListener((buttonView, isChecked) -> mIsSelectRoute = isChecked);
        holder.checkBoxSelectRout.setOnClickListener(view -> EventBus.getDefault().post(new BusEvents.SendChosenRouter(mTransportRoutesData.get(position),mIsSelectRoute)));
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
        @BindView(R.id.checkbox_select_rout)
        AppCompatCheckBox checkBoxSelectRout;

        TramsAndTrolleyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}