package com.hyperpakhsh.sadeq.bazaartracker.Factors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;


class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {

    private ArrayList<OrderItem> items;
    private boolean isSent;
    private DbHelper helper;



    OrderAdapter(ArrayList<OrderItem> items, Context context, boolean isSent) {
        this.items = items;
        this.isSent = isSent;
        helper = new DbHelper(context);
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView row,product,count,price,total;
        LinearLayout container;

        Holder(View itemView) {
            super(itemView);

            Typeface sansLight = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf");
            row = (TextView) itemView.findViewById(R.id.item_order_row);
            product = (TextView) itemView.findViewById(R.id.item_order_product);
            count = (TextView) itemView.findViewById(R.id.item_order_count);
            price = (TextView) itemView.findViewById(R.id.item_order_price);
            total = (TextView) itemView.findViewById(R.id.item_order_total);
            container = (LinearLayout) itemView.findViewById(R.id.item_order_container);

            row.setTypeface(sansLight);
            price.setTypeface(sansLight);
            product.setTypeface(sansLight);
            count.setTypeface(sansLight);
            total.setTypeface(sansLight);
        }
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final OrderItem item = items.get(position);

        holder.total.setText(item.getTotal()+"");
        holder.count.setText(item.getQuantity()+"");
        holder.product.setText(item.getProduct()+"");
        holder.row.setText((position+1)+"");
        holder.price.setText(item.getQntPrice()+"");

        holder.container.setBackgroundColor((position%2==0)? Color.parseColor("#D4D4D4"): Color.parseColor("#A0A39D"));

        if(!isSent){
            holder.count.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")) {
                        item.setQuantity(Integer.valueOf(s.toString()));
                        helper.updateOrder(item.getId(), item.getQntPrice(),Integer.valueOf(s.toString()),item.getFactorId());
                        item.setTotal(Integer.valueOf(s.toString()) * item.getQntPrice());
                        holder.total.setText(Integer.valueOf(s.toString()) * item.getQntPrice()+"");
                    }
                }
            });

            holder.price.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().equals("")) {
                        item.setQntPrice(Integer.valueOf(s.toString()));
                        helper.updateOrder(item.getId(), Integer.valueOf(s.toString()), item.getQuantity(),item.getFactorId());
                        item.setTotal(Integer.valueOf(s.toString()) * item.getQntPrice());
                        holder.total.setText(Integer.valueOf(s.toString()) * item.getQuantity()+"");

                    }
                }
            });
        }else{
            holder.count.setEnabled(false);
            holder.price.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
