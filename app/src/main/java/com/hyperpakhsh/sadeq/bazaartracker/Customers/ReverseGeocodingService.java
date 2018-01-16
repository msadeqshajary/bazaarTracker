package com.hyperpakhsh.sadeq.bazaartracker.Customers;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReverseGeocodingService extends IntentService{

    ResultReceiver receiver;
    String message;
    String name;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ReverseGeocodingService(String name) {
        super(name);
        this.name = name;
    }

    public ReverseGeocodingService(){
        super("sadeq");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        assert intent != null;
        Location location = intent.getParcelableExtra("location");
        receiver = intent.getParcelableExtra("receiver");

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
            message = "Error happend: "+e.getLocalizedMessage();
            deliverResultToReceiver(0,message);
        }

        if(addresses == null || addresses.size() == 0){
            message= "no address received";
            deliverResultToReceiver(0,message);
        }else{
            Address address = addresses.get(0);
            StringBuilder addressSlices = new StringBuilder();
            Log.e("ADDRESS LiNES ",address.getMaxAddressLineIndex()+" ");

            addressSlices.append(address.getAddressLine(0));


            String addressX = addressSlices.toString();

            deliverResultToReceiver(1, addressX);
        }

    }

    void deliverResultToReceiver(int resultCode, String result){
        Bundle bundle = new Bundle();
        bundle.putString("address",result);
        receiver.send(resultCode,bundle);
    }
}
