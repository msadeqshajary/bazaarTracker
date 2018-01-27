package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.ResultReceiver;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.hyperpakhsh.sadeq.bazaartracker.Map.MapsActivity;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.DbHelper;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.NameValueItem;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerFragment extends Fragment implements OnMapReadyCallback {

    int regionId, gradeId,locationId;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location mLocation;
    AddressResultReceiver receiver;
    boolean isGpsReceived;
    String address,jsonCustomer,shop;
    ApiInterface apiInterface;
    ProgressBar statusPb;
    Button submit;
    GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_add_customer,container,false);

        /**
         * Initialize Map
         */
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.add_customer_map);
        mapFragment.getMapAsync(this);

        /**
         * Get Location info
         */

        receiver = new AddressResultReceiver(new Handler());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getLastLocation();

        LocationSettingsRequest.Builder settingBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient settingsClient = LocationServices.getSettingsClient(getContext());
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(settingBuilder.build());

        task.addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Toast.makeText(getContext(),"Location settings are done",Toast.LENGTH_LONG).show();
            }
        });

        task.addOnFailureListener(getActivity(), new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(getActivity(),
                                0);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }

            }
        });

        /**
         * Initialize objects
         */

        Typeface sans = Typeface.createFromAsset(getContext().getAssets(),"fonts/sans.ttf");
        Typeface sansMedium = Typeface.createFromAsset(getContext().getAssets(),"fonts/sansmedium.ttf");
        Typeface sansLight = Typeface.createFromAsset(getContext().getAssets(),"fonts/sanslight.ttf");

        final DbHelper helper = new DbHelper(getContext());
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        /*
        Introduce Views
         */

        TextView title = v.findViewById(R.id.add_customer_title);
        TextView addTitle = v.findViewById(R.id.add_customer_card_title);
        final EditText shopInput = v.findViewById(R.id.add_customer_input_shop);
        final EditText nameinput = v.findViewById(R.id.add_customer_input_name);

        TextInputLayout shopTil = v.findViewById(R.id.add_customer_shop_inputlayout);
        TextInputLayout nameTil = v.findViewById(R.id.add_customer_name_inputlayout);
        TextInputLayout phoneTil = v.findViewById(R.id.add_customer_phone_inputlayout);
        final EditText phoneInput = v.findViewById(R.id.add_customer_input_phone);
        TextInputLayout addressTil = v.findViewById(R.id.add_customer_address_inputlayout);
        final EditText addressInput = v.findViewById(R.id.add_customer_input_address);


        addressTil.setTypeface(sansMedium);
        addressInput.setTypeface(sansLight);
        title.setTypeface(sans);
        addTitle.setTypeface(sansMedium);
        shopTil.setTypeface(sansMedium);
        nameTil.setTypeface(sansMedium);
        shopInput.setTypeface(sansLight);
        nameinput.setTypeface(sansLight);
        phoneTil.setTypeface(sansMedium);
        phoneInput.setTypeface(sansLight);

        //Insert Values to regions. if exist in database, don't connect to webservice again.
        final TextView regions = v.findViewById(R.id.add_customer_region);
        regions.setTypeface(sans);
        regions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.regions_dialog);
                final RecyclerView list = dialog.findViewById(R.id.regions_list);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                if(!helper.isRegionsExists()){
                    apiInterface.getRegions().enqueue(new Callback<ArrayList<NameValueItem>>() {
                        @Override
                        public void onResponse(Call<ArrayList<NameValueItem>> call, Response<ArrayList<NameValueItem>> response) {
                            NameValueItem[] items = response.body().toArray(new NameValueItem[response.body().size()]);
                            list.setAdapter(new RegionsSpinnerAdapter(items, new RegionsSpinnerAdapter.onRegionSelected() {
                                @Override
                                public void setOnRegionSelectedListener(NameValueItem item) {
                                    dialog.dismiss();
                                    regions.setText(item.getName());
                                    regionId = Integer.parseInt(item.getId());
                                }
                            }));
                            helper.insertRegions(items);
                            System.out.println("size: "+ response.body().size());
                        }

                        @Override
                        public void onFailure(Call<ArrayList<NameValueItem>> call, Throwable t) {
                            Toast.makeText(getContext(),"دریافت دیتا با مشکل مواجه شد",Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    list.setAdapter(new RegionsSpinnerAdapter(helper.getRegions(), new RegionsSpinnerAdapter.onRegionSelected() {
                        @Override
                        public void setOnRegionSelectedListener(NameValueItem item) {
                            dialog.dismiss();
                            regions.setText(item.getName());
                            regionId = Integer.parseInt(item.getId());
                        }
                    }));
                }

                dialog.show();
            }
        });

        //Insert grades and that's listener
        final TextView grade = v.findViewById(R.id.add_customer_grade);
        grade.setTypeface(sans);
        grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.regions_dialog);
                final RecyclerView list = dialog.findViewById(R.id.regions_list);
                list.setLayoutManager(new LinearLayoutManager(getContext()));
                list.setAdapter(new RegionsSpinnerAdapter(getGrades(), new RegionsSpinnerAdapter.onRegionSelected() {
                    @Override
                    public void setOnRegionSelectedListener(NameValueItem item) {
                        dialog.dismiss();
                        grade.setText(item.getName());
                        gradeId = Integer.parseInt(item.getId());
                        grade.setBackgroundResource(R.drawable.grade_background);
                    }
                }));
                dialog.show();
            }
        });

        //Submit to WebService
        submit = v.findViewById(R.id.add_customer_submit_button);
        submit.setEnabled(false);
        submit.setTypeface(sans);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusPb.setVisibility(View.VISIBLE);

                getLastLocation();
                shop = shopInput.getText().toString();
                if(shop.length()>3 && nameinput.getText().toString().length() > 1 && addressInput.getText().toString().length() > 2
                        && !grade.getText().equals("گرید") && !regions.getText().equals("منطقه"))
                    apiInterface.getLocationId(mLocation.getLatitude()+","+mLocation.getLongitude(),address).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            //if location Id inserted and it's id returned

                            String responsex = response.body().replaceAll("\\s+","");
                            locationId = Integer.parseInt(responsex);

                            CustomerItem customer = new CustomerItem();
                            customer.setName(nameinput.getText().toString());
                            customer.setAddress(addressInput.getText().toString());
                            customer.setGrade(gradeId);
                            customer.setLocationId(locationId);
                            customer.setPhone(phoneInput.getText().toString());
                            customer.setShop(shop);
                            customer.setUserId(MapsActivity.userId);
                            customer.setType("current");
                            customer.setRegionId(regionId);

                            Gson gson = new Gson();
                            jsonCustomer = gson.toJson(customer);

                            //then insert new customer
                            insertCustomer();
                        }


                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            System.out.println(t.getLocalizedMessage());
                        }
                    });

                else {
                    Toast.makeText(getContext(), "لطفا مقادیر خواسته شده را تکمیل کنید", Toast.LENGTH_LONG).show();
                    statusPb.setVisibility(View.INVISIBLE);
                }
            }
        });

        //status text and progressBar

        statusPb = v.findViewById(R.id.add_customer_send_progress);
        statusPb.setVisibility(View.INVISIBLE);

        //toolbar
        ImageView back = v.findViewById(R.id.add_customer_toolbar_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(new Intent(getContext(), MapsActivity.class));
            }
        });

        v.findViewById(R.id.add_customer_toolbar_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });



        return v;
    }

    NameValueItem[] getGrades(){
        NameValueItem item1 = new NameValueItem();
        item1.setName("A+");
        item1.setId(String.valueOf(1));

        NameValueItem item2 = new NameValueItem();
        item2.setName("A");
        item2.setId(String.valueOf(2));

        NameValueItem item3 = new NameValueItem();
        item3.setName("B+");
        item3.setId(String.valueOf(3));

        NameValueItem item4 = new NameValueItem();
        item4.setName("B");
        item4.setId(String.valueOf(4));

        NameValueItem item5 = new NameValueItem();
        item5.setName("C+");
        item5.setId(String.valueOf(5));

        NameValueItem item6 = new NameValueItem();
        item6.setName("C");
        item6.setId(String.valueOf(6));

        NameValueItem[] items = new NameValueItem[6];
        items[0] = item1;
        items[1] = item2;
        items[2] = item3;
        items[3] = item4;
        items[4] = item5;
        items[5] = item6;

        return items;
    }

    void insertCustomer(){
        apiInterface.setCustomer(jsonCustomer).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                statusPb.setVisibility(View.GONE);

                Intent intent = new Intent(getContext(),MapsActivity.class);
                intent.putExtra("lat",mLocation.getLatitude());
                intent.putExtra("lng",mLocation.getLongitude());
                intent.putExtra("userId",MapsActivity.userId);

                getActivity().finish();
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(),"Error inserting customer",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }

    void moveToLocation(Location location){
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15);
        mMap.animateCamera(update);
    }

    private class AddressResultReceiver extends ResultReceiver{

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            String result = resultData.getString("address");
            isGpsReceived=true;
            address = result;
            submit.setEnabled(true);
        }
    }

    void getLastLocation(){
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mLocation = location;
                    moveToLocation(mLocation);
                    if(!Geocoder.isPresent()){
                        Toast.makeText(getContext(),"Geocoder is not available",Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(getContext(),ReverseGeocodingService.class);
                        intent.putExtra("receiver",receiver);
                        intent.putExtra("location",location);

                        getContext().startService(intent);
                    }
                }
                else Toast.makeText(getContext(),"Location not founded",Toast.LENGTH_LONG).show();
            }
        });
    }
}
