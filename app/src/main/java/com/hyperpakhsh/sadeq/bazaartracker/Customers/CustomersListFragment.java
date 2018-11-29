package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyperpakhsh.sadeq.bazaartracker.Dashboard.DashboardActivity;
import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderFragment;
import com.hyperpakhsh.sadeq.bazaartracker.Order.ProductItem;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.Utils;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CustomersListFragment extends Fragment {

    ArrayList<CustomerListItem> items;
    CustomerListAdapter adapter;
    RecyclerView customerList;
    public static int userId,saleMaliID;
    ApiInterface apiInterface;
    SharedPreferences preferences;
    Bundle arguments;
    DbHelper helper;
    private DecimalFormat formatter = new DecimalFormat("#,###,###");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customers_list,container,false);
        preferences = getContext().getSharedPreferences("LOGIN",MODE_PRIVATE);
        helper = new DbHelper(getContext());

        LinearLayout searchBar = (LinearLayout) v.findViewById(R.id.search_container);
        TextView searchTitle = (TextView) searchBar.findViewById(R.id.search_title);
        EditText search = (EditText) searchBar.findViewById(R.id.search_search);

        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");

        searchTitle.setTypeface(sansMedium);
        search.setTypeface(sansMedium);

        TextView row = (TextView) v.findViewById(R.id.item_customers_row);
        row.setTypeface(sansMedium);
        TextView product = (TextView) v.findViewById(R.id.item_customers_customer);
        product.setTypeface(sansMedium);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                items = helper.getCustoemrs(s.toString());
                customerList.setAdapter(new CustomerListAdapter(items, new CustomerListAdapter.OnCustomerSelected() {
                    @Override
                    public void CustomerSelectedListener(CustomerListItem customer) {

                    }
                }));
            }
        });


        arguments = getArguments();
        if(arguments != null)
        if(arguments.getInt("print",0)>0){
            String carListAsString = arguments.getString("products");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<ProductItem>>(){}.getType();
            ArrayList<ProductItem> productItems = gson.fromJson(carListAsString, type);
            PrintOrder(productItems);
        }

        userId = preferences.getInt("userId",0);
        saleMaliID = preferences.getInt("saleMaliID",0);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        customerList = (RecyclerView) v.findViewById(R.id.customers_list);
        customerList.setLayoutManager(new LinearLayoutManager(getContext()));

        items = helper.getCustoemrs("");
        if(items == null){
            setCustomers();
        }else{
            adapter = new CustomerListAdapter(items, new CustomerListAdapter.OnCustomerSelected() {
                @Override
                public void CustomerSelectedListener(CustomerListItem customer) {
                    Bundle args = new Bundle();
                    args.putInt("customerId",Integer.valueOf(customer.getSoftwareId()));
                    args.putString("customerName",customer.getName());
                    args.putInt("userId", CustomersListFragment.userId);
                    args.putInt("saleMaliID", CustomersListFragment.saleMaliID);

                    OrderFragment fragment = new OrderFragment();

                    ((DashboardActivity) getActivity()).replaceFragment(fragment,args,true);
                }
            });

            customerList.setAdapter(new CustomerListAdapter(items, new CustomerListAdapter.OnCustomerSelected() {
                @Override
                public void CustomerSelectedListener(CustomerListItem customer) {
                    Bundle args = new Bundle();
                    args.putInt("customerId",Integer.valueOf(customer.getSoftwareId()));
                    args.putString("customerName",customer.getName());
                    args.putInt("userId", CustomersListFragment.userId);
                    args.putInt("saleMaliID", CustomersListFragment.saleMaliID);

                    OrderFragment fragment = new OrderFragment();

                    ((DashboardActivity) getActivity()).replaceFragment(fragment,args,true);
                }
            }));
        }



        final FloatingActionButton addFab = (FloatingActionButton) v.findViewById(R.id.customers_add_fab);
        customerList.addOnScrollListener(new RecyclerView.OnScrollListener()
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
                DialogAddCustomer dialog = new DialogAddCustomer();
                dialog.setListener(new DialogAddCustomer.CustomerAddListener() {
                    @Override
                    public void onAddProduct(final CustomerListItem item) {
                        items.add(item);
                        adapter.notifyItemInserted(items.size() - 1);
                    }
                });

                dialog.show(getActivity().getSupportFragmentManager(),"ADD PRODUCT");
            }
        });


        return v;
    }

    void setCustomers(){

        apiInterface.getCustomers().enqueue(new Callback<ArrayList<CustomerListItem>>() {

            @Override
            public void onResponse(Call<ArrayList<CustomerListItem>> call, Response<ArrayList<CustomerListItem>> response) {
                helper.deleteTableDatas(DbHelper.TABLE_CUSTOMERS);
                helper.insertCustomers(response.body());
                items = response.body();
                adapter = new CustomerListAdapter(items, new CustomerListAdapter.OnCustomerSelected() {
                    @Override
                    public void CustomerSelectedListener(CustomerListItem customer) {
                        Bundle args = new Bundle();
                        args.putInt("customerId",Integer.valueOf(customer.getSoftwareId()));
                        args.putString("customerName",customer.getName());
                        args.putInt("userId", CustomersListFragment.userId);
                        args.putInt("saleMaliID", CustomersListFragment.saleMaliID);

                        OrderFragment fragment = new OrderFragment();

                        ((DashboardActivity) getActivity()).replaceFragment(fragment,args,true);
                    }
                });
                customerList.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<CustomerListItem>> call, Throwable t) {
                Log.e("CUSTOMERS",t.getLocalizedMessage());
            }
        });
    }


    void PrintOrder(ArrayList<ProductItem> items){
        String html = Utils.HtmlBase;
        html = html.replace("$customer",arguments.getString("customerName"));
        final WebView webView = new WebView(getActivity());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("STATUS","PAGE FINISHED");
                doPrintJob(webView);
            }
        });
        html = html.replace("$order",getOrdersInTrStyle(items));
        html = html.replace("$total","  "+ formatter.format(arguments.getInt("totalPrice",0)));
        html = html.replace("$shipping","  "+formatter.format(arguments.getInt("shipping",0)));
        html = html.replace("$discount","  "+formatter.format(arguments.getInt("discount",0)));
        html = html.replace("$final","  "+ formatter.format(arguments.getInt("finalTotal",0)));
        String desc = arguments.getString("desc");
        html = html.replace("$desc","  "+ (desc.equals("")?"فروش رسمی به "+arguments.getString("customerName"):desc));
        html = html.replace("$date",":  تاریخ "+arguments.getString("date"));
        webView.loadDataWithBaseURL("file:///android_asset/",html,"text/HTML","UTF-8",null);
    }

    void doPrintJob(WebView webView){
        PrintManager printManager = (PrintManager) getContext().getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printDocumentAdapter;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            printDocumentAdapter = webView.createPrintDocumentAdapter("PRINT SALE FACTOR");
        }
        else {
            printDocumentAdapter = webView.createPrintDocumentAdapter();
        }

        printManager.print("PRINT SALE FACTOR",printDocumentAdapter,new PrintAttributes.Builder().build());

    }

    private String getOrdersInTrStyle(ArrayList<ProductItem> items){
        StringBuilder stringBuilder = new StringBuilder();
        for(int i =0;i<items.size();i++){
            if(i!=0) {
                String template = Utils.orderTr;
                ProductItem item = items.get(i);
                template = template.replace("$row", (i) + "");
                template = template.replace("$product", item.getName());
                template = template.replace("$price",item.getPrice());
                template = template.replace("$quantity",item.getQuantity()+"");
                template = template.replace("$totalPrice",String.valueOf(item.getQuantity() * Integer.valueOf(item.getPrice())));
                stringBuilder.append(template);
            }
        }

        return stringBuilder.toString();
    }

}
