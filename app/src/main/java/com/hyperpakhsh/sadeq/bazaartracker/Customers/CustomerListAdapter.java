package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.R;

import java.util.ArrayList;

class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.Holder> {

    private ArrayList<CustomerListItem> items;
    private OnCustomerSelected listener;


    interface OnCustomerSelected{
        void CustomerSelectedListener(CustomerListItem customer);
    }

    CustomerListAdapter(ArrayList<CustomerListItem> items,OnCustomerSelected listener) {
        this.items = items;
        this.listener = listener;
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView row,customer;
        ImageView order;
        LinearLayout container;
        ImageView windows;

        Holder(View itemView) {
            super(itemView);

            row = (TextView) itemView.findViewById(R.id.item_customers_row);
            customer = (TextView) itemView.findViewById(R.id.item_customers_customer);
            order = (ImageView)  itemView.findViewById(R.id.item_customers_add_order);

            row.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf"));
            customer.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf"));

            windows = (ImageView) itemView.findViewById(R.id.item_customers_windows);

            container = (LinearLayout) itemView.findViewById(R.id.item_customers_container);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customers,parent,false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        holder.container.setBackgroundColor((position % 2 == 0)? Color.parseColor("#D4D4D4"): Color.parseColor("#EFEFEF"));
        final CustomerListItem item = items.get(position);
        holder.customer.setText(item.getName());
        holder.row.setText((position + 1)+"");

        if(item.getWindows() == 0){
            holder.windows.setVisibility(View.VISIBLE);
            holder.order.setVisibility(View.GONE);
        }

        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.CustomerSelectedListener(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (items!=null)?items.size():0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
