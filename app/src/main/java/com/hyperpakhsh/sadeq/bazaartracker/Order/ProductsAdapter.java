package com.hyperpakhsh.sadeq.bazaartracker.Order;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.R;

import java.util.ArrayList;

class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.Holder> {
    private ArrayList<ProductItem> items;
    private OnProductSelect listener;

    interface OnProductSelect{
        void setOnProductSelectListener(ProductItem item);
    }



    ProductsAdapter(ArrayList<ProductItem> items,OnProductSelect listener) {
        this.items = items;
        this.listener = listener;
       // setHasStableIds(true);
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView title;
        EditText count;
        EditText price;
        LinearLayout container;

        Holder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.list_product_product);
            count = (EditText) itemView.findViewById(R.id.list_product_count);
            price = (EditText) itemView.findViewById(R.id.list_product_price);

            title.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf"));
            count.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf"));
            price.setTypeface(Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf"));
            container = (LinearLayout) itemView.findViewById(R.id.list_product_container);
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_products,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final ProductItem item = items.get(position);
        if(item.getWindows()== 0){
            holder.title.setTextColor(Color.GRAY);
            holder.price.setTextColor(Color.GRAY);
        }
        holder.title.setText(item.getName());
        holder.price.setText(item.getPrice()+"");

        System.out.println("SOFTWARE ID: "+item.getSoftwareID());

        holder.container.setBackgroundColor((position % 2 == 0)? Color.parseColor("#D4D4D4"): Color.parseColor("#A0A39D"));

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
                    if(Integer.valueOf(item.getPrice()) != 0 && item.getQuantity() != 0)
                    listener.setOnProductSelectListener(item);
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

                if(!s.toString().equals("")){
                item.setPrice(s.toString());
                if(Integer.valueOf(item.getPrice()) != 0 && item.getQuantity() != 0)
                listener.setOnProductSelectListener(item);
                }
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
