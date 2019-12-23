package com.e.fueleddemo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.e.fueleddemo.restaurant_model.Venue;
import com.e.fueleddemo.viewmodel.MyViewModel;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {

    private ArrayList<Venue> alVenues;
    private Context ctx;
    private MyViewModel viewModel;

    private static final int MODE_USUAL_LIST = 0;
    private static final int MODE_HIDE_LIST = 1;

    private int btMode = MODE_HIDE_LIST;

    public RestaurantAdapter(ArrayList<Venue> alVenues, Context ctx, MyViewModel viewModel) {
        this.alVenues = alVenues;
        this.ctx = ctx;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, final int position) {
        Venue venue = alVenues.get(position);
        holder.tvName.setText(venue.getName());
        holder.tvAddress.setText(viewModel.getAddress(venue, ctx));
        Glide.with(ctx).load(viewModel.getIconUrl(venue)).into(holder.ivLogo);
        setListener(holder.itemView, position);
    }

    private void setListener(View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do nothing when the current list shows the hidden restaurants
                if (btMode == MODE_USUAL_LIST) {
                    return;
                }

                // don't show the dialog if there is no internet connection
                if (!viewModel.isNetworkAvailable(ctx)) {
                    Toast.makeText(ctx, ctx.getText(R.string.no_network), Toast.LENGTH_SHORT).show();
                    return;
                }
                Venue venue = alVenues.get(position);
                viewModel.getGoDialog(venue, ctx).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return alVenues.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivLogo;
        private TextView tvName;
        private TextView tvAddress;

        public RestaurantViewHolder(@NonNull View view) {
            super(view);
            this.ivLogo = view.findViewById(R.id.ivLogo);
            this.tvName = view.findViewById(R.id.tvName);
            this.tvAddress = view.findViewById(R.id.tvAddress);
        }
    }

    public void setAlVenues(ArrayList<Venue> alVenues, int mode) {
        this.alVenues = alVenues;
        this.btMode = mode;
        notifyDataSetChanged();
    }
}
