package com.hyperpakhsh.sadeq.bazaartracker.Login;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.hyperpakhsh.sadeq.bazaartracker.Dashboard.DashboardActivity;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.NameValueItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    InitItem init;
    int saleMaliID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * First get init values. if its received, then connection is OK and use that for login
         */


        final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        apiInterface.getInits().enqueue(new Callback<ArrayList<InitItem>>() {
            @Override
            public void onResponse(Call<ArrayList<InitItem>> call, Response<ArrayList<InitItem>> response) {
                init = response.body().get(0);
                Toast.makeText(getApplicationContext(),"اطلاعات اولیه دریافت شدند",Toast.LENGTH_SHORT).show();
                saleMaliID = Integer.parseInt(init.getSaleMaliID());
            }

            @Override
            public void onFailure(Call<ArrayList<InitItem>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"اشکال در دریافت اطلاعات اولیه",Toast.LENGTH_SHORT).show();
                Log.e("FAILURE",t.getLocalizedMessage());
            }
        });



        /**
         * Check if Logged In
         */

        final SharedPreferences sharedPreferences = getSharedPreferences("LOGIN",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("login",false)){
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            intent.putExtra("userId",sharedPreferences.getInt("userId",0));
            intent.putExtra("saleMaliID",sharedPreferences.getInt("saleMaliID",0));
            startActivity(intent);
        }

        /**
         * Init views
         */

        Typeface sans = Typeface.createFromAsset(getAssets(),"fonts/sans.ttf");
        Typeface sansMedium = Typeface.createFromAsset(getAssets(),"fonts/sansmedium.ttf");

        TextView title = (TextView) findViewById(R.id.login_title);
        title.setTypeface(sans);

        TextInputLayout userContainer = (TextInputLayout) findViewById(R.id.login_username_container);
        TextInputLayout passContainer = (TextInputLayout) findViewById(R.id.login_password_container);
        final EditText userInput = (EditText) findViewById(R.id.login_username);
        final EditText passInput = (EditText) findViewById(R.id.login_password);

        userContainer.setTypeface(sansMedium);
        passContainer.setTypeface(sansMedium);

        /**
         * Submit user pass
         */
        final ProgressBar pb = (ProgressBar) findViewById(R.id.login_progress);
        pb.setVisibility(View.INVISIBLE);

        final Button submit = (Button) findViewById(R.id.login_submit);
        submit.setTypeface(sansMedium);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(init != null) {
                    pb.setVisibility(View.VISIBLE);
                    userInput.setEnabled(false);
                    passInput.setEnabled(false);
                    submit.setEnabled(false);
                    apiInterface.login(userInput.getText().toString(), passInput.getText().toString()).enqueue(new Callback<ArrayList<NameValueItem>>() {
                        @Override
                        public void onResponse(Call<ArrayList<NameValueItem>> call, Response<ArrayList<NameValueItem>> response) {
                            pb.setVisibility(View.INVISIBLE);
                            NameValueItem item = response.body().get(0);
                            Toast.makeText(getApplicationContext(), "خوش آمدید " + item.getName(), Toast.LENGTH_LONG).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("login", true);
                            editor.putInt("userId", Integer.parseInt(item.getId()));
                            editor.putInt("saleMaliID",saleMaliID);
                            editor.apply();

//                        Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
//                        intent.putExtra("userId",Integer.parseInt(item.getId()));
//                        startActivity(intent);

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.putExtra("userId", Integer.parseInt(item.getId()));
                            intent.putExtra("saleMaliID",saleMaliID);
                            startActivity(intent);

                            finish();
                        }

                        @Override
                        public void onFailure(Call<ArrayList<NameValueItem>> call, Throwable t) {
                            Log.e("ERROR", t.getLocalizedMessage());
                            Toast.makeText(getApplicationContext(), "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_LONG).show();

                            pb.setVisibility(View.INVISIBLE);
                            userInput.setEnabled(true);
                            passInput.setEnabled(true);
                            submit.setEnabled(true);
                        }
                    });
                }
            }
        });
    }
}
