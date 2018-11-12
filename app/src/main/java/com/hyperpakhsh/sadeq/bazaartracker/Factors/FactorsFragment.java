package com.hyperpakhsh.sadeq.bazaartracker.Factors;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Dashboard.DashboardActivity;
import com.hyperpakhsh.sadeq.bazaartracker.Order.FactorItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.util.ArrayList;


public class FactorsFragment extends Fragment {

    RecyclerView factorsList;
    ApiInterface apiInterface;
    FactorAdapter adapter;
    ArrayList<FactorItem> items;
    DbHelper helper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_orders_list,container,false);

        helper = new DbHelper(getContext());
        factorsList = (RecyclerView) v.findViewById(R.id.orders_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        factorsList.setLayoutManager(linearLayoutManager);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        TextView row = (TextView) v.findViewById(R.id.item_factors_row);
        TextView date = (TextView) v.findViewById(R.id.item_factors_date);
        TextView time = (TextView) v.findViewById(R.id.item_factors_time);
        TextView customer = (TextView) v.findViewById(R.id.item_factors_customer);
        TextView total = (TextView) v.findViewById(R.id.item_factors_total);
        TextView status = (TextView) v.findViewById(R.id.item_factors_status);

        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");

        row.setTypeface(sansMedium);
        date.setTypeface(sansMedium);
        time.setTypeface(sansMedium);
        customer.setTypeface(sansMedium);
        total.setTypeface(sansMedium);
        status.setTypeface(sansMedium);

        LinearLayout searchBar = (LinearLayout) v.findViewById(R.id.search_container);
        ImageView refresh = (ImageView) searchBar.findViewById(R.id.search_refresh);
        TextView searchTitle = (TextView) searchBar.findViewById(R.id.search_title);
        EditText search = (EditText) searchBar.findViewById(R.id.search_search);

        searchTitle.setTypeface(sansMedium);
        search.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf"));

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                items = helper.getFactors(s.toString());
                factorsList.setAdapter(new FactorAdapter(items,getActivity().getSupportFragmentManager(),getContext()));
            }
        });

        items = helper.getFactors(null);
        adapter = new FactorAdapter(items,getActivity().getSupportFragmentManager(),getContext());
        adapter.setOnChangedListener(new FactorAdapter.OnChangedListener() {
            @Override
            public void OnDataChanged() {
                FactorsFragment factorsFragment = new FactorsFragment();
                ((DashboardActivity) getActivity()).replaceFragment(factorsFragment,null,false);
            }
        });
        factorsList.setAdapter(adapter);

        return v;
    }

}
