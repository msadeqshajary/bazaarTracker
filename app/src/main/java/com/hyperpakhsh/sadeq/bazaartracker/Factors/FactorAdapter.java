package com.hyperpakhsh.sadeq.bazaartracker.Factors;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyperpakhsh.sadeq.bazaartracker.Order.*;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class FactorAdapter extends RecyclerView.Adapter<FactorAdapter.Holder> {
    private ArrayList<FactorItem> items;
    private FragmentManager fragmentManager;
    private ApiInterface apiInterface;
    private DbHelper helper;
    private Context context;
    private OnChangedListener onChangedListener;
    private DecimalFormat formatter = new DecimalFormat("#,###,###");

    void setOnChangedListener(OnChangedListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }


    interface OnChangedListener{
        void OnDataChanged();
    }

    FactorAdapter(ArrayList<FactorItem> items,FragmentManager fragmentManager,Context context) {
        this.items = items;
        this.fragmentManager = fragmentManager;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        helper = new DbHelper(context);
        this.context = context;
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView row,date,customer,total;
        ImageView status,print;
        LinearLayout container;

        Holder(View itemView) {
            super(itemView);

            Typeface sansLight = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/sanslight.ttf");
            row = (TextView) itemView.findViewById(R.id.item_factors_row);
            date = (TextView) itemView.findViewById(R.id.item_factors_date);
            customer = (TextView) itemView.findViewById(R.id.item_factors_customer);
            total = (TextView) itemView.findViewById(R.id.item_factors_total);
            print = (ImageView) itemView.findViewById(R.id.item_factors_print);

            row.setTypeface(sansLight);
            date.setTypeface(sansLight);
            customer.setTypeface(sansLight);
            total.setTypeface(sansLight);

            container = (LinearLayout) itemView.findViewById(R.id.item_factors_container);

            status = (ImageView) itemView.findViewById(R.id.item_factors_status);

        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_factors,parent,false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {
        final FactorItem factor = items.get(position);
        String[] splittedDate = factor.getDate().split("-");

        holder.total.setText(factor.getTotal()+"");
        holder.customer.setText(factor.getCustomer());
        holder.date.setText(splittedDate[1]+"/"+splittedDate[2]);
        holder.row.setText((position+1)+"");

        holder.status.setImageResource((factor.getSentStatus()==0)?R.drawable.ic_checked_red:R.drawable.ic_checked);

        holder.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(factor.getSentStatus()==0){
                    final ArrayList<OrderItem> items = helper.getOrdersByFactorId(factor.getId());
                    Log.e("DESCRIPTION",factor.getDes());
                    String json = new Gson().toJson(factor);
                    apiInterface.addFactor(json).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Log.e("RESPONSE",response.body());
                            if(!response.body().contains("false")){
                                String res = response.body().replace(" ","");
                                int factorIdx = Integer.parseInt(res);
                                factor.setMysqlId(factorIdx);
                                OrderItem[] newItems = new OrderItem[items.size()];
                                for(int i =0;i<items.size();i++){
                                    OrderItem x = items.get(i);
                                    OrderItem item = new OrderItem();
                                    item.setFactorId(factorIdx);
                                    item.setQuantity(x.getQuantity());
                                    item.setDes(x.getDes());
                                    item.setQntPrice(x.getQntPrice());
                                    item.setRowNo(x.getRowNo());
                                    item.setStoreStuffRelationByLevelID(x.getStoreStuffRelationByLevelID());
                                    item.setTotal(x.getTotal());
                                    item.setProductLocalId(x.getProductLocalId());
                                    item.setMysqlId(factorIdx);
                                    newItems[i] = item;
                                }
                                String jsonArray = new Gson().toJson(newItems);
                                System.out.println(jsonArray);
                                apiInterface.addOrder(jsonArray).enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if(response.body().contains("true")){
                                            Log.e("STATUS","EVERY THING IS OKAY");
                                            helper.updateFactorStatus(factor.getId());
                                            holder.status.setImageResource(R.drawable.ic_checked);
                                            factor.setSentStatus(1);
                                        }else Toast.makeText(context,"َAdd Order error",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(context,"َerror: "+ t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }else Toast.makeText(context,"Error occurred",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("ERROR",t.getLocalizedMessage());
                        }
                    });
                }
            }
        });

        holder.container.setBackgroundColor((position%2==0)? Color.parseColor("#D4D4D4"): Color.parseColor("#EFEFEF"));

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int factorId = factor.getId();
                OrdersDialog dialog = new OrdersDialog();
                dialog.setListener(new OrdersDialog.onDismiss() {
                    @Override
                    public void onDismissListener() {

                        onChangedListener.OnDataChanged();
                    }
                });
                Bundle bundle = new Bundle();
                bundle.putInt("factorId", factorId);
                bundle.putBoolean("isSent", factor.getSentStatus() != 0);
                bundle.putString("customerName",factor.getCustomer());
                bundle.putInt("discount",factor.getDiscountAmount());
                bundle.putInt("shipping",factor.getCharge());
                dialog.setArguments(bundle);
                dialog.show(fragmentManager ,"TAG");
            }
        });

        holder.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintOrder(helper.getProductsByFactorId(factor.getId()),factor);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (items != null)?items.size():0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void PrintOrder(ArrayList<ProductItem> items, FactorItem factor){
        String html = Utils.HtmlBase;
        html = html.replace("$customer",factor.getCustomer());
        final WebView webView = new WebView(context);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("STATUS","PAGE FINISHED");
                doPrintJob(webView);
            }
        });

        int tot = 0;
        for(ProductItem product: items){
            tot += Integer.valueOf(product.getPrice()) * product.getQuantity();
        }
        html = html.replace("$order",getOrdersInTrStyle(items));
        html = html.replace("$total","  "+ formatter.format(tot));
        html = html.replace("$shipping","  "+formatter.format(factor.getCharge()));
        html = html.replace("$discount","  "+formatter.format(factor.getDiscountAmount()));
        html = html.replace("$final","  "+ formatter.format(factor.getTotal()));
        String desc = factor.getDes();
        html = html.replace("$desc","  "+ (desc.equals("")?"فروش رسمی به "+factor.getCustomer():desc));
        html = html.replace("$date",":  تاریخ "+factor.getDate());
        webView.loadDataWithBaseURL("file:///android_asset/",html,"text/HTML","UTF-8",null);
    }

    private void doPrintJob(WebView webView){
        PrintManager printManager = (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
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

                String template = Utils.orderTr;
                ProductItem item = items.get(i);
                template = template.replace("$row", (i) + "");
                template = template.replace("$product", item.getName());
                template = template.replace("$price",item.getPrice());
                template = template.replace("$quantity",item.getQuantity()+"");
                template = template.replace("$totalPrice",String.valueOf(item.getQuantity() * Integer.valueOf(item.getPrice())));
                stringBuilder.append(template);

        }

        return stringBuilder.toString();
    }
}
