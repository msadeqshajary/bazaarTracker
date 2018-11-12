package com.hyperpakhsh.sadeq.bazaartracker.Products;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsFragment extends Fragment {

    ArrayList<ProductItem> items;
    ProductsAdapter adapter;
    RecyclerView list;
    DbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_products,container,false);

        dbHelper = new DbHelper(getContext());

        list = (RecyclerView) v.findViewById(R.id.products_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(linearLayoutManager);

        LinearLayout searchBar = (LinearLayout) v.findViewById(R.id.search_container);
        TextView searchTitle = (TextView) searchBar.findViewById(R.id.search_title);
        EditText search = (EditText) searchBar.findViewById(R.id.search_search);

        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");

        searchTitle.setTypeface(sansMedium);
        search.setTypeface(sansMedium);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                items = dbHelper.getProducts(s.toString());
                list.setAdapter(new ProductsAdapter(items));
            }
        });

        final FloatingActionButton addFab = (FloatingActionButton) v.findViewById(R.id.products_add_fab);
        list.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if ((dy>0||dy==0) && addFab.isShown())
                {
                    addFab.hide();
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    addFab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddProduct dialog = new DialogAddProduct();
                dialog.setListener(new DialogAddProduct.ProductSelectListener() {
                    @Override
                    public void onAddProduct(final ProductItem item) {
                        items.add(item);
                        adapter.notifyItemInserted(items.size() - 1);

                    }});

                dialog.show(getActivity().getSupportFragmentManager(),"ADD PRODUCT");
            }
        });

        items = dbHelper.getProducts(null);
        if(items==null){
            setProducts();
        }
        else list.setAdapter(new ProductsAdapter(items));

        TextView row = (TextView) v.findViewById(R.id.list_product_row);
        TextView product = (TextView) v.findViewById(R.id.list_product_product);
        TextView price = (TextView) v.findViewById(R.id.list_product_price);

        row.setTypeface(sansMedium);
        price.setTypeface(sansMedium);
        product.setTypeface(sansMedium);

        return v;
    }

    void setProducts(){
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getProducts().enqueue(new Callback<ArrayList<ProductItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductItem>> call, Response<ArrayList<ProductItem>> response) {
                items = response.body();
                adapter = new ProductsAdapter(items);
                list.setAdapter(adapter);
                dbHelper.insertProducts(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<ProductItem>> call, Throwable t) {

            }
        });
    }
}
