package com.hyperpakhsh.sadeq.bazaartracker.Customers;

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


import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DialogAddCustomer extends DialogFragment {

    CustomerAddListener listener;

    interface CustomerAddListener{
        void onAddProduct(CustomerListItem item);
    }

    public void setListener(CustomerAddListener listener) {
        this.listener = listener;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_add_customer,container,false);


        TextView title = (TextView) v.findViewById(R.id.dialog_add_customer_title);
        TextView nameTv = (TextView) v.findViewById(R.id.dialog_add_customer_name_tv);
        TextView priceTv = (TextView) v.findViewById(R.id.dialog_add_customer_price_tv);

        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");
        title.setTypeface(sansMedium);
        nameTv.setTypeface(sansMedium);
        priceTv.setTypeface(sansMedium);

        final EditText customer = (EditText) v.findViewById(R.id.dialog_add_customer_name);
        final EditText phone = (EditText) v.findViewById(R.id.dialog_add_customer_price);
        customer.setTypeface(sansMedium);
        phone.setTypeface(sansMedium);

        final ProgressBar pb = (ProgressBar) v.findViewById(R.id.dialog_add_customer_progress);
        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Button submit = (Button) v.findViewById(R.id.dialog_add_customer_submit);
        submit.setTypeface(sansMedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                final CustomerListItem item = new CustomerListItem();
                item.setName(customer.getText().toString());
                item.setPhone(customer.getText().toString());

                apiInterface.addCustomer(item.getName(),item.getPhone()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().contains("true")) {
                            Toast.makeText(getContext(), "مشتری جدید با موفقیت افزوده شد", Toast.LENGTH_LONG).show();
                            listener.onAddProduct(item);
                            dismiss();
                        }else{
                            Toast.makeText(getContext(), "اشکال در افزودن مشتری جدید", Toast.LENGTH_LONG).show();
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
}
