package com.hyperpakhsh.sadeq.bazaartracker.Map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
        userId = MapsActivity.userId;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /**
         * Toolbar layout handler
         */

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        apiInterface.getLocations().enqueue(new Callback<ArrayList<LocationItem>>() {
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
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_shop_approve));
                    mMap.addMarker(new MarkerOptions().position(latLng).title(item.getShop()));
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
