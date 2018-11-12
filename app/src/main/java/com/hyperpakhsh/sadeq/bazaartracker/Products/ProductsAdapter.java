package com.hyperpakhsh.sadeq.bazaartracker.Products;

import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;

import java.util.ArrayList;

class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.Holder> {
    private ArrayList<ProductItem> items;

    ProductsAdapter(ArrayList<ProductItem> items) {
        this.items = items;
    }

    class Holder extends RecyclerView.ViewHolder{
        Typeface sansMedium;

        TextView title,price,row;
        ImageView edit;
        ImageView delete;

        LinearLayout container;
        Holder(View itemView) {
            super(itemView);
            sansMedium = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf");

            title = (TextView) itemView.findViewById(R.id.list_product_product);
            price = (TextView) itemView.findViewById(R.id.list_product_price);
            row = (TextView) itemView.findViewById(R.id.list_product_row);

            title.setTypeface(sansMedium);
            price.setTypeface(sansMedium);
            row.setTypeface(sansMedium);

            edit = (ImageView) itemView.findViewById(R.id.list_product_edit);
            delete = (ImageView) itemView.findViewById(R.id.list_product_delete);

            container = (LinearLayout) itemView.findViewById(R.id.list_product_container);
        }
    }

    @Override
    public ProductsAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false));
    }

    @Override
    public void onBindViewHolder(ProductsAdapter.Holder holder, int position) {
        ProductItem item = items.get(position);
        holder.row.setText((position+1)+"");
        holder.title.setText(item.getName());
        holder.price.setText(item.getPrice());
        if(item.getWindows() == 1){
            holder.delete.setVisibility(View.INVISIBLE);
            holder.edit.setVisibility(View.INVISIBLE);
        }

        holder.container.setBackgroundColor((position%2==0)? Color.parseColor("#D4D4D4"): Color.parseColor("#EFEFEF"));
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
