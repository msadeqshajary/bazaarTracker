package com.hyperpakhsh.sadeq.bazaartracker.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hyperpakhsh.sadeq.bazaartracker.R;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiClient;
import com.hyperpakhsh.sadeq.bazaartracker.Utils.ApiInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    ApiInterface apiInterface;
    Bundle args;
    int userId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map,container,false);

        //initialize Retrofit
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        args = getArguments();

        SharedPreferences preferences = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        userId = preferences.getInt("userId",0);

        Log.e("USER IDDDD",userId+" ");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        GoogleMapOptions options = new GoogleMapOptions();
        options.camera(CameraPosition.fromLatLngZoom(new LatLng(34.6357683,50.8754933),12));

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.map).new

        SupportMapFragment mapFragment = SupportMapFragment.newInstance(options);

        mapFragment.getMapAsync(this);
        /**
         * Toolbar layout handler
         */

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        apiInterface.getLocations(userId).enqueue(new Callback<ArrayList<LocationItem>>() {
            @Override
            public void onResponse(Call<ArrayList<LocationItem>> call, Response<ArrayList<LocationItem>> response) {
                for (LocationItem item : response.body()){
                    String[] latlong = item.getLatlang().split(",");
                    double lat = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng latLng = new LatLng(lat,longitude);
                    MarkerOptions options = new MarkerOptions()
                            .position(latLng)
                            .title(item.getShop())
                            .snippet(item.getLocation());
                    mMap.addMarker(options);
                }

                if(args !=null &&args.getDouble("lat") != 0){
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(args.getDouble("lat"),args.getDouble("lng")),14);
                    mMap.moveCamera(update);
                }else{
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(34.6432245,50.880412),14);
                    mMap.moveCamera(update);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LocationItem>> call, Throwable t) {

            }
        });

    }
}
