package com.hyperpakhsh.sadeq.bazaartracker.Order;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    private ArrayList<ProductItem> items;
    private DecimalFormat formatter = new DecimalFormat("#,###,###");
    private Typeface sansmedium;
    private Activity context;
    private DbHelper helper;


    OrderAdapter(ArrayList<ProductItem> items, Activity context) {
        this.items = items;
        sansmedium = Typeface.createFromAsset(context.getApplicationContext().getAssets(),"fonts/sansmedium.ttf");
        this.context = context;
        helper = new DbHelper(context.getApplicationContext());
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView row,price,product,total,count;
        Typeface sansMedium;
        LinearLayout container;
        Holder(View itemView) {
            super(itemView);

            sansMedium = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf");
            row = (TextView) itemView.findViewById(R.id.item_order_row);
            price = (TextView) itemView.findViewById(R.id.item_order_price);
            product = (TextView) itemView.findViewById(R.id.item_order_product);
            total = (TextView) itemView.findViewById(R.id.item_order_total);
            count = (TextView) itemView.findViewById(R.id.item_order_count);

            row.setTypeface(sansMedium);
            price.setTypeface(sansMedium);
            product.setTypeface(sansMedium);
            total.setTypeface(sansMedium);
            count.setTypeface(sansMedium);

            container = (LinearLayout) itemView.findViewById(R.id.item_order_container);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_list,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        final ProductItem item = items.get(position);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                items.remove(holder.getAdapterPosition() );
                                notifyItemRemoved(holder.getAdapterPosition());
                                helper.deleteItem(item.getName());
                                dialog.dismiss();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("حذف کالا؟").setPositiveButton("بله", dialogClickListener)
                        .setNegativeButton("خیر", dialogClickListener).show();
            }
        });

        //First row...
        if(position==0){
            holder.row.setText("ردیف");
            holder.count.setText("تعداد");
            holder.total.setText("مجموع");
            holder.price.setText("قیمت واحد");
            holder.product.setText("محصول");

            holder.row.setTypeface(sansmedium);
            holder.count.setTypeface(sansmedium);
            holder.total.setTypeface(sansmedium);
            holder.price.setTypeface(sansmedium);
            holder.product.setTypeface(sansmedium);
        }
        else{
            holder.row.setText(position + "");

            int total = 0;
            if(item.getPrice() != null) {
                total = item.getQuantity() * (Integer.valueOf(item.getPrice()));
            }
            holder.total.setText(formatter.format(total));
            holder.price.setText(formatter.format(Integer.valueOf(item.getPrice())));
            holder.product.setText(item.getName());
            holder.count.setText(formatter.format(item.getQuantity()));

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
