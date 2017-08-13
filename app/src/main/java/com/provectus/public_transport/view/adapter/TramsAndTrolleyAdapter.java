package com.provectus.public_transport.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.provectus.public_transport.R;
import com.provectus.public_transport.model.ModelJson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Psihey on 11.08.2017.
 */

public class TramsAndTrolleyAdapter extends RecyclerView.Adapter<TramsAndTrolleyAdapter.TramsTrolleybusViewHolder> {
    //TODO : Change after implement correct JSON model
    private List<ModelJson> data;

    public TramsAndTrolleyAdapter(List<ModelJson> data) {
        this.data = data;
    }

    @Override
    public TramsTrolleybusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bundle_tram_trolleybus, parent, false);

        return new TramsTrolleybusViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(TramsTrolleybusViewHolder holder, int position) {
        if (data.get(position).getType().equals("tram")) {
            holder.ivLogoTransport.setImageResource(R.drawable.ic_train_public_gray);
        } else {
            holder.ivLogoTransport.setImageResource(R.drawable.ic_front_bus_gray);
        }
        holder.tvRoutesNumber.setText(String.valueOf(data.get(position).getNumber()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class TramsTrolleybusViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_view_number_routes)
        TextView tvRoutesNumber;
        @BindView(R.id.image_view_logo_transport)
        ImageView ivLogoTransport;

        TramsTrolleybusViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
