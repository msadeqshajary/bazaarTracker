package com.hyperpakhsh.sadeq.bazaartracker.Map;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.hyperpakhsh.sadeq.bazaartracker.Customers.AddCustomerFragment;
import com.hyperpakhsh.sadeq.bazaartracker.R;

import java.util.List;

public class MapsActivity extends FragmentActivity {

    public static int userId;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        userId = getIntent().getIntExtra("userId",0);

        ViewStub toolbarBelow = findViewById(R.id.main_stub);
        View toolbar = toolbarBelow.inflate();

        toolbar.findViewById(R.id.toolbar_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MapsActivity.this,SerachActivity.class);
//                intent.putExtra("userId",userId);
//                startActivity(intent);
            }
        });
        toolbar.findViewById(R.id.toolbar_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCustomerFragment customerFragment = new AddCustomerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("userId",userId);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.maps_container,customerFragment,"add");
                fragmentTransaction.commit();
            }
        });

        toolbar.findViewById(R.id.toolbar_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapFragment mapFragment = new MapFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                        .replace(R.id.maps_container,mapFragment,"map");
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }
}
