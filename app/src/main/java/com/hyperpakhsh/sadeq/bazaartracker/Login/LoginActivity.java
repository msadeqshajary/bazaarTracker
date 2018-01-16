package com.hyperpakhsh.sadeq.bazaartracker.Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.hyperpakhsh.sadeq.bazaartracker.Map.MapsActivity;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.NameValueItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Init views
         */

        Typeface sans = Typeface.createFromAsset(getAssets(),"fonts/sans.ttf");
        Typeface sansMedium = Typeface.createFromAsset(getAssets(),"fonts/sansmedium.ttf");

        TextView title = findViewById(R.id.login_title);
        title.setTypeface(sans);

        TextInputLayout userContainer = findViewById(R.id.login_username_container);
        TextInputLayout passContainer = findViewById(R.id.login_password_container);
        final EditText userInput = findViewById(R.id.login_username);
        final EditText passInput = findViewById(R.id.login_password);

        userContainer.setTypeface(sansMedium);
        passContainer.setTypeface(sansMedium);

        /**
         * Submit user pass
         */
        final ProgressBar pb = findViewById(R.id.login_progress);
        pb.setVisibility(View.INVISIBLE);

        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Button submit = findViewById(R.id.login_submit);
        submit.setTypeface(sansMedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                userInput.setEnabled(false);
                passInput.setEnabled(false);
                submit.setEnabled(false);
                apiInterface.login(userInput.getText().toString(),passInput.getText().toString()).enqueue(new Callback<ArrayList<NameValueItem>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NameValueItem>> call, Response<ArrayList<NameValueItem>> response) {
                        pb.setVisibility(View.INVISIBLE);
                        NameValueItem item = response.body().get(0);
                        Toast.makeText(getApplicationContext(),"خوش آمدید "+item.getName(),Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
                        intent.putExtra("userId",Integer.parseInt(item.getId()));
                        startActivity(intent);

                        finish();

                        userInput.setEnabled(true);
                        passInput.setEnabled(true);
                        submit.setEnabled(true);

                        userInput.setText("");
                        passInput.setText("");
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NameValueItem>> call, Throwable t) {
                        Log.e("ERROR",t.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(),"نام کاربری یا رمز عبور اشتباه است",Toast.LENGTH_LONG).show();

                        pb.setVisibility(View.INVISIBLE);
                        userInput.setEnabled(true);
                        passInput.setEnabled(true);
                        submit.setEnabled(true);

                    }
                });
            }
        });
    }
}
