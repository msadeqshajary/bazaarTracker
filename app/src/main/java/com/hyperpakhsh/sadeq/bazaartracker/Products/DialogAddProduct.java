package com.hyperpakhsh.sadeq.bazaartracker.Products;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogAddProduct extends DialogFragment {

    ProductSelectListener listener;
    DbHelper helper;
    ApiInterface apiInterface;

    interface ProductSelectListener{
        void onAddProduct(ProductItem item);
    }

    public void setListener(ProductSelectListener listener) {
        this.listener = listener;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_product,container,false);


        helper = new DbHelper(getContext());
        TextView title = (TextView) v.findViewById(R.id.dialog_add_product_title);
        TextView nameTv = (TextView) v.findViewById(R.id.dialog_add_product_name_tv);
        final TextView priceTv = (TextView) v.findViewById(R.id.dialog_add_product_price_tv);

        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");
        title.setTypeface(sansMedium);
        nameTv.setTypeface(sansMedium);
        priceTv.setTypeface(sansMedium);

        final EditText name = (EditText) v.findViewById(R.id.dialog_add_product_name);
        final EditText price = (EditText) v.findViewById(R.id.dialog_add_product_price);
        name.setTypeface(sansMedium);
        price.setTypeface(sansMedium);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final ProgressBar pb = (ProgressBar) v.findViewById(R.id.dialog_add_product_progress);



        Button submit = (Button) v.findViewById(R.id.dialog_add_product_submit);
        submit.setTypeface(sansMedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pricex;
                if(price.getText().toString().equals("")) pricex = 0;
                else pricex  = Integer.valueOf(price.getText().toString());
                pb.setVisibility(View.VISIBLE);
                final ProductItem item = new ProductItem();
                item.setName(name.getText().toString());
                item.setPrice(String.valueOf(pricex));

                apiInterface.addProduct(item.getName(), Integer.parseInt(item.getPrice())).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().contains("true")) {
                            Toast.makeText(getContext(), "محصول جدید با موفقیت افزوده شد", Toast.LENGTH_LONG).show();
                            if(listener!=null)
                            listener.onAddProduct(item);
                            pb.setVisibility(View.INVISIBLE);
                            recycleProducts(getContext().getApplicationContext());
                            dismiss();
                        }
                        else{
                            Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        return v;
    }

    void recycleProducts(final Context context){
        apiInterface.getProducts().enqueue(new Callback<ArrayList<ProductItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductItem>> call, Response<ArrayList<ProductItem>> response) {
                helper.deleteTableDatas(DbHelper.TABLE_PRODUCTS);
                helper.insertProducts(response.body());
                Toast.makeText(context,"محصولات با موفقیت به روز شدند",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ArrayList<ProductItem>> call, Throwable t) {

            }
        });
    }
}
