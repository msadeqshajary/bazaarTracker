package com.hyperpakhsh.sadeq.bazaartracker.Order;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyperpakhsh.sadeq.bazaartracker.Customers.CustomersListFragment;
import com.hyperpakhsh.sadeq.bazaartracker.Dashboard.DashboardActivity;
import com.hyperpakhsh.sadeq.bazaartracker.Products.DialogAddProduct;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class OrderFragment extends Fragment {

    OrderAdapter adapter;
    private DecimalFormat formatter = new DecimalFormat("#,###,###");
    int /*total price of order no attention to discount*/totalPrice;
    ApiInterface apiInterface;
    public static int ORDERID;
    int customerId,userId,saleMaliID;
    ProgressBar pb;
    ArrayList<ProductItem> productItems;
    String date,desc;
    DbHelper helper;
    int discount,shipping,finalTotal;
    Bundle arguments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order,container,false);
        /**
         * Init values
         */
        arguments = getArguments();
        helper = new DbHelper(getContext());
        helper.deleteTableDatas(DbHelper.TABLE_TEMPORDER);
        productItems = new ArrayList<>();
        ORDERID  = (int) (System.currentTimeMillis() * (Math.random()));
        userId = arguments.getInt("userId",0);
        customerId = arguments.getInt("customerId",0);
        saleMaliID = arguments.getInt("saleMaliID",0);

        pb = (ProgressBar) v.findViewById(R.id.order_progress);
        pb.setVisibility(View.INVISIBLE);

        Typeface sans = Typeface.createFromAsset(getContext().getAssets(),"fonts/sans.ttf");
        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getTimeInstance();
        Calendar calendar = Calendar.getInstance();

        PersianDate pd = new PersianDate(calendar.getTimeInMillis());
        date = pd.getShYear()+"/"+pd.getShMonth()+"/"+pd.getShDay();
        final String time = simpleDateFormat.format(pd.getTime());

        /**
         * Description
         */

        TextView descTitle = (TextView) v.findViewById(R.id.order_desc_title);
        descTitle.setTypeface(sansMedium);
        final EditText descEt = (EditText) v.findViewById(R.id.order_desc);
        descEt.setTypeface(Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf"));

        /**
         * Order final
         */

        final TextView totalPriceTitle,totalPriceTv,shippingTitle,discountTitle,payTitle,totalPay;
        final EditText shippingEt,discountEt;

        totalPriceTv = (TextView) v.findViewById(R.id.order_final_total_price);
        totalPriceTitle = (TextView) v.findViewById(R.id.order_final_total_price_title);
        discountEt = (EditText) v.findViewById(R.id.order_final_total_discount);
        discountTitle = (TextView) v.findViewById(R.id.order_final_total_discount_title);
        shippingTitle = (TextView) v.findViewById(R.id.order_final_total_shipping_title);
        shippingEt = (EditText) v.findViewById(R.id.order_final_shipping);
        payTitle = (TextView) v.findViewById(R.id.order_final_total_title);
        totalPay = (TextView) v.findViewById(R.id.order_final_total);

        Typeface sansLight = Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf");
        totalPriceTv.setTypeface(sansLight);
        totalPriceTitle.setTypeface(sansLight);
        discountEt.setTypeface(sansLight);
        discountTitle.setTypeface(sansLight);
        shippingTitle.setTypeface(sansLight);
        shippingEt.setTypeface(sansLight);
        payTitle.setTypeface(sansMedium);
        totalPay.setTypeface(sansMedium);

        shippingEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals(""))
                    shipping = Integer.valueOf(s.toString());
                else shipping=0;
                finalTotal = totalPrice + shipping - discount;
                totalPay.setText(formatter.format(finalTotal));

            }
        });

        discountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().equals("")) {
                    discount = Integer.valueOf(s.toString());
                }else discount = 0;
                finalTotal = totalPrice + shipping - discount;
                totalPay.setText(formatter.format(finalTotal));

            }
        });

        finalTotal = totalPrice + shipping - discount;
        totalPay.setText(formatter.format(finalTotal));

        final CardView orderCard = (CardView) v.findViewById(R.id.order_card);

        /**
         * Init List of products in orders
         */

        final RecyclerView orderList = (RecyclerView) v.findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductItem titleOrder = new ProductItem();
        productItems.add(titleOrder);

/**
 * Add icon
 */

        v.findViewById(R.id.orders_add_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectProductDialogFragment dialogFragment = new SelectProductDialogFragment();
                dialogFragment.setListener(new SelectProductDialogFragment.ProductsSubmitListener() {
                    @Override
                    public void onSubmitProducts() {

                        ArrayList<ProductItem> tempItems = helper.getTempProductItems();
                        productItems.clear();
                        productItems.add(new ProductItem());
                        productItems.addAll(tempItems);
                        adapter = new OrderAdapter(productItems,getActivity());
                        orderList.setAdapter(adapter);

                        totalPrice = getTotalPrice(productItems,0,false);
                        finalTotal = totalPrice + shipping - discount;
                        totalPay.setText(formatter.format(finalTotal));

                        totalPriceTv.setText(formatter.format(getTotalPrice(productItems,0,false))+"");
                        orderCard.setVisibility(View.VISIBLE);
                    }
                });

                dialogFragment.show(getActivity().getSupportFragmentManager(),"TAG");
            }
        });

        /**
         * connect to printer
         */

//        final PrinterUtils printerUtils = new PrinterUtils(OrderFragment.this);
//        try {
//            printerUtils.findPairedDevices();
//            printerUtils.setBxlConfigLoader(getApplicationContext());
//            printerUtils.connect(getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**
         * Submit
         */
        Button submit = (Button) v.findViewById(R.id.order_submit);
        submit.setTypeface(sansMedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                //printerUtils.PrintOrders(orderItems,"فروشگاه رضوران",15000,formatter.format(getTotalPrice(orderItems,0,true)));
                OrderItem[] items = new OrderItem[productItems.size() - 1];
                for(int i =1;i<productItems.size();i++){
                    ProductItem productItem = productItems.get(i);
                    OrderItem orderItem = new OrderItem();
                    orderItem.setRowNo(i);
                    orderItem.setProductLocalId(Integer.parseInt(productItem.getLocalId()));
                    orderItem.setQuantity(productItem.getQuantity());
                    orderItem.setQntPrice(Integer.parseInt(productItem.getPrice()));
                    orderItem.setTotal((Integer.parseInt(productItem.getPrice()) * productItem.getQuantity()));
                    orderItem.setDes("فروش "+productItem.getName());
                    orderItem.setStoreStuffRelationByLevelID(Integer.parseInt(productItem.getSoftwareID()));
                    orderItem.setMysqlId(0);
                    orderItem.setProductId(productItem.getSqliteId());
                    items[i-1] = orderItem;
                }

                desc = descEt.getText().toString();
                FactorItem item = new FactorItem();
                item.setCustomerId(customerId);
                item.setDate(date);
                item.setRegTime(time);
                item.setDes((desc!=null || !desc.equals(""))?desc:"فروش رسمی به "+arguments.getString("customerName"));
                if(desc.length()<3)item.setDes("فروش رسمی به "+arguments.getString("customerName"));
                item.setSaleMaliId(saleMaliID);
                item.setDiscountAmount(discount);
                item.setTotal(totalPrice);
                item.setUserId(userId);
                item.setCharge(shipping);

                if(isNetworkAvailable()){
                    addFactor(item,items);
                }else{
                    //Insert factor to sqlite and get last inserted id
                    item.setMysqlId(0);
                    int lastId = helper.insertFactor(item,0);
                    helper.insertOrders(items,lastId);
                    Bundle args = new Bundle();

                    Gson gson = new Gson();
                    String jsonProducts = gson.toJson(productItems);
                    args.putString("products",jsonProducts);
                    args.putInt("totalPrice",totalPrice);
                    args.putInt("shipping",shipping);
                    args.putInt("discount",discount);
                    args.putInt("finalTotal",finalTotal);
                    args.putString("date",date);
                    args.putInt("print",1);
                    args.putString("desc",(desc!=null)?desc:"فروش رسمی به "+arguments.getString("customerName"));
                    args.putString("customerName",arguments.getString("customerName"));

                    CustomersListFragment fragment = new CustomersListFragment();
                    ((DashboardActivity) getActivity()).replaceFragment(fragment,args,false);
                }


            }
        });
        Button submitOffline = (Button) v.findViewById(R.id.order_submit_offline);
        submitOffline.setTypeface(sansMedium);
        submitOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                //printerUtils.PrintOrders(orderItems,"فروشگاه رضوران",15000,formatter.format(getTotalPrice(orderItems,0,true)));
                OrderItem[] items = new OrderItem[productItems.size() - 1];
                for(int i =1;i<productItems.size();i++){
                    ProductItem productItem = productItems.get(i);
                    OrderItem orderItem = new OrderItem();
                    orderItem.setRowNo(i);
                    orderItem.setProductLocalId(Integer.parseInt(productItem.getLocalId()));
                    orderItem.setQuantity(productItem.getQuantity());
                    orderItem.setQntPrice(Integer.parseInt(productItem.getPrice()));
                    orderItem.setTotal((Integer.parseInt(productItem.getPrice()) * productItem.getQuantity()));
                    orderItem.setDes("فروش "+productItem.getName());
                    orderItem.setStoreStuffRelationByLevelID(Integer.parseInt(productItem.getSoftwareID()));
                    orderItem.setMysqlId(0);
                    orderItem.setProductId(productItem.getSqliteId());
                    items[i-1] = orderItem;
                }

                desc = descEt.getText().toString();
                FactorItem item = new FactorItem();
                item.setCustomerId(customerId);
                item.setDate(date);
                item.setRegTime(time);
                item.setDes((desc!=null || !desc.equals(""))?desc:"فروش رسمی به "+arguments.getString("customerName"));
                if(desc.length()<3)item.setDes("فروش رسمی به "+arguments.getString("customerName"));
                item.setSaleMaliId(saleMaliID);
                item.setDiscountAmount(discount);
                item.setTotal(totalPrice);
                item.setUserId(userId);
                item.setCharge(shipping);

                    //Insert factor to sqlite and get last inserted id
                    item.setMysqlId(0);
                    int lastId = helper.insertFactor(item,0);
                    helper.insertOrders(items,lastId);
                    Bundle args = new Bundle();

                    Gson gson = new Gson();
                    String jsonProducts = gson.toJson(productItems);
                    args.putString("products",jsonProducts);
                    args.putInt("totalPrice",totalPrice);
                    args.putInt("shipping",shipping);
                    args.putInt("discount",discount);
                    args.putInt("finalTotal",finalTotal);
                    args.putString("date",date);
                    args.putInt("print",1);
                    args.putString("desc",(desc!=null)?desc:"فروش رسمی به "+arguments.getString("customerName"));
                    args.putString("customerName",arguments.getString("customerName"));

                    CustomersListFragment fragment = new CustomersListFragment();
                    ((DashboardActivity) getActivity()).replaceFragment(fragment,args,false);

            }
        });

        v.findViewById(R.id.orders_add_product_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddProduct dialogAddProduct = new DialogAddProduct();
                dialogAddProduct.show(getActivity().getSupportFragmentManager(),"TAG");
            }
        });



        return v;
    }


    int getTotalPrice(ArrayList<ProductItem> items, int dis, boolean percentDis){
        float i = 0;
        for(ProductItem item : items){
            if(item != null && item.getQuantity() != 0)
            i+= (Integer.valueOf(item.getPrice()) * item.getQuantity());
        }

        if(percentDis){
            float disx = dis / 100.0f;
            float priceDis = i * disx;
            i = i - priceDis;
        }
        else{
            i = i - dis;
        }
        totalPrice = (int) i;
        return (int) i;
    }

    private void addFactor(final FactorItem item, final OrderItem[] items){
        String json = new Gson().toJson(item);
        System.out.println(json);
        apiInterface.addFactor(json).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("RESPONSE",response.body());
                if(!response.body().contains("false")){
                    String res = response.body().replace(" ","");
                    int factorId = Integer.parseInt(res);
                    item.setMysqlId(factorId);
                    int last = helper.insertFactor(item,1);
                    sendOrders(items,factorId,last);
                }else Toast.makeText(getContext(),"Error occurred",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR",t.getLocalizedMessage());
            }
        });
    }

    private void sendOrders(final OrderItem[] items, int factorId, final int last){

        OrderItem[] newItems = new OrderItem[items.length];
        for(int i =0;i<items.length;i++){
            OrderItem x = items[i];
            OrderItem item = new OrderItem();
            item.setFactorId(factorId);
            item.setQuantity(x.getQuantity());
            item.setDes(x.getDes());
            item.setQntPrice(x.getQntPrice());
            item.setRowNo(x.getRowNo());
            item.setStoreStuffRelationByLevelID(x.getStoreStuffRelationByLevelID());
            item.setTotal(x.getTotal());
            item.setProductLocalId(x.getProductLocalId());
            item.setMysqlId(factorId);
            newItems[i] = item;
        }
        String jsonArray = new Gson().toJson(newItems);
        System.out.println(jsonArray);
        apiInterface.addOrder(jsonArray).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().contains("true")){
                    Log.e("STATUS","EVERY THING IS OKAY");
                    pb.setVisibility(View.GONE);
                    helper.insertOrders(items,last);
                    Toast.makeText(getContext(),"فاکتور با موفقیت ثبت شد",Toast.LENGTH_LONG).show();
                    Bundle args = new Bundle();

                    Gson gson = new Gson();
                    String jsonProducts = gson.toJson(productItems);
                    args.putString("products",jsonProducts);
                    args.putInt("totalPrice",totalPrice);
                    args.putInt("shipping",shipping);
                    args.putInt("discount",discount);
                    args.putInt("finalTotal",finalTotal);
                    args.putString("date",date);
                    args.putInt("print",1);
                    args.putString("desc",(desc!=null)?desc:"فروش رسمی به "+arguments.getString("customerName"));
                    args.putString("customerName",arguments.getString("customerName"));

                    CustomersListFragment fragment = new CustomersListFragment();
                    ((DashboardActivity) getActivity()).replaceFragment(fragment,args,false);
                }else Toast.makeText(getContext(),"َAdd Order error",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),"َerror: "+ t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
