package com.hyperpakhsh.sadeq.bazaartracker.Order;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectProductDialogFragment extends DialogFragment implements ProductsAdapter.OnProductSelect {

    RecyclerView list;
    ProductsAdapter adapter;
    ArrayList<ProductItem> items;
    ProductsSubmitListener listener;
    DbHelper helper;


    interface ProductsSubmitListener{
        void onSubmitProducts();
    }

    public void setListener(ProductsSubmitListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_select_product,container,false);
        TextView title = (TextView) v.findViewById(R.id.dialog_order_title);
        helper = new DbHelper(getContext());
        title.setText("انتخاب محصول");
        title.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sans.ttf"));

        final EditText search = (EditText) v.findViewById(R.id.dialog_order_search);
        search.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                    items = helper.getProducts(s.toString());
                    list.setAdapter(new ProductsAdapter(items,SelectProductDialogFragment.this));

            }
        });

        /**
         * List titr
         */

        TextView product = (TextView) v.findViewById(R.id.product_product);
        TextView count = (TextView) v.findViewById(R.id.product_count);
        TextView pricex = (TextView) v.findViewById(R.id.product_price);

        pricex.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        count.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));
        product.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));


        list = (RecyclerView) v.findViewById(R.id.dialog_add_product_list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        items = helper.getProducts(null);
        if(items==null)setProducts();
        else list.setAdapter(new ProductsAdapter(items,SelectProductDialogFragment.this));

        Button submit = (Button) v.findViewById(R.id.dialog_add_product_submit);
        submit.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf"));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            listener.onSubmitProducts();
            dismiss();
            }
        });
        return v;
    }

    void setProducts(){
        helper.deleteTableDatas(DbHelper.TABLE_PRODUCTS);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getProducts().enqueue(new Callback<ArrayList<ProductItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductItem>> call, Response<ArrayList<ProductItem>> response) {
                helper.insertProducts(response.body());

                items = helper.getProducts(null);
                adapter = new ProductsAdapter(items,SelectProductDialogFragment.this);
                list.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<ProductItem>> call, Throwable t) {
                Log.e("FAILURE",t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void setOnProductSelectListener(ProductItem item) {
        helper.updateTempOrderItem(item);
    }
}
