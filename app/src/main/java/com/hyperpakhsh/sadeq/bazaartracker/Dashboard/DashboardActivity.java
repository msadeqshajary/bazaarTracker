package com.hyperpakhsh.sadeq.bazaartracker.Dashboard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyperpakhsh.sadeq.bazaartracker.Customers.CustomersListFragment;
import com.hyperpakhsh.sadeq.bazaartracker.Factors.FactorsFragment;
import com.hyperpakhsh.sadeq.bazaartracker.Order.OrderFragment;
import com.hyperpakhsh.sadeq.bazaartracker.Products.ProductsFragment;
import com.hyperpakhsh.sadeq.bazaartracker.R;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[] bottoms = new ImageView[5];
    TextView toolbarTitle;

    /*
    home => 0
    customers => 1
    orders => 2
    products => 3
    diagrams => 4
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarrr_main);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/sans.ttf"));

        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom_container);
        bottoms[0] = (ImageView) bottom.findViewById(R.id.bottom_home);
        bottoms[1] = (ImageView) bottom.findViewById(R.id.bottom_customers);
        bottoms[2] = (ImageView) bottom.findViewById(R.id.bottom_orders);
        bottoms[3] = (ImageView) bottom.findViewById(R.id.bottom_products);
        bottoms[4] = (ImageView) bottom.findViewById(R.id.bottom_diagrams);

        for(ImageView bottomImg : bottoms){
            bottomImg.setOnClickListener(this);
        }

        bottomBarClick(1);
        toolbarTitle.setText("لیست مشتریان");

    }

    public void replaceFragment(Fragment fragment,Bundle args,boolean addToBackStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.main_container,fragment);
        if(addToBackStack) fragmentTransaction.addToBackStack(null);
        if(fragment instanceof OrderFragment){
            toolbarTitle.setText("ثبت فاکتور جدید");
        }

        fragmentTransaction.commit();
    }

    void bottomBarClick(int index){
        for(int i=0;i<bottoms.length;i++){
            if(i==index)bottoms[i].setColorFilter(Color.YELLOW);
            else bottoms[i].setColorFilter(Color.WHITE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_home:
                break;
            case R.id.bottom_customers:bottomBarClick(1);
                replaceFragment(new CustomersListFragment(),null,false);
                toolbarTitle.setText("لیست مشتریان");
                break;
            case R.id.bottom_orders:bottomBarClick(2);
                replaceFragment(new FactorsFragment(),null,false);
                toolbarTitle.setText("لیست فاکتورها");
                break;
            case R.id.bottom_products:bottomBarClick(3);
                replaceFragment(new ProductsFragment(),null,false);
                toolbarTitle.setText("لیست محصولات");
                break;
            case R.id.bottom_diagrams:
                break;

        }
    }
}
