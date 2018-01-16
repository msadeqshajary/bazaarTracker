package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.NameValueItem;

class RegionsSpinnerAdapter extends RecyclerView.Adapter<RegionsSpinnerAdapter.Holder>{

    private NameValueItem[] items;
    interface onRegionSelected{
        void setOnRegionSelectedListener(NameValueItem item);
    }

    private onRegionSelected listener;

    RegionsSpinnerAdapter(NameValueItem[] items,onRegionSelected listener) {
        this.items = items;
        this.listener = listener;
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView title;
        Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.item_regions_spinner_region);
            title.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sansmedium.ttf"));
        }
    }

    @Override
    public RegionsSpinnerAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_regions_spinner,parent,false));
    }

    @Override
    public void onBindViewHolder(RegionsSpinnerAdapter.Holder holder, int position) {
        final NameValueItem item = items[position];
        holder.title.setText(item.getName());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setOnRegionSelectedListener(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.length;
    }
}
