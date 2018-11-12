package com.hyperpakhsh.sadeq.bazaartracker.Factors;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;

public class OrdersDialog extends DialogFragment {



    interface onDismiss {
        void onDismissListener();
    }

    onDismiss listener;

    public void setListener(onDismiss listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_orders,container,false);

        TextView title = (TextView) v.findViewById(R.id.dialog_orders_title);
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sans.ttf"));

        title.setText("مشاهده فاکتور -> "+getArguments().getString("customerName"));

        TextView discount = (TextView) v.findViewById(R.id.dialog_orders_discount);
        discount.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf"));

        TextView shipping = (TextView) v.findViewById(R.id.dialog_orders_shipping);
        shipping.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf"));

        TextView row = (TextView) v.findViewById(R.id.item_order_row);
        TextView product = (TextView) v.findViewById(R.id.item_order_product);
        TextView quantity = (TextView) v.findViewById(R.id.item_order_count);
        TextView price = (TextView) v.findViewById(R.id.item_order_price);
        TextView total = (TextView) v.findViewById(R.id.item_order_total);

        row.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        product.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        quantity.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        price.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        total.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));


        discount.setText("تخفیف : "+getArguments().getInt("discount")+" تومان ");
        shipping.setText(" هزینه حمل: "+getArguments().getInt("shipping")+" تومان ");

        final RecyclerView ordersList = (RecyclerView) v.findViewById(R.id.dialog_orders_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ordersList.setLayoutManager(linearLayoutManager);

        int factorId = getArguments().getInt("factorId");
        boolean isSent = getArguments().getBoolean("isSent");
        DbHelper dbHelper = new DbHelper(getContext());
        ArrayList<OrderItem> items = dbHelper.getOrdersByFactorId(factorId);
        OrderAdapter orderAdapter = new OrderAdapter(items,getContext(),isSent);

        ordersList.setAdapter(orderAdapter);

        Button back = (Button) v.findViewById(R.id.dialog_orders_back);
        back.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDismissListener();
                dismiss();
            }
        });

        return v;
    }
}
