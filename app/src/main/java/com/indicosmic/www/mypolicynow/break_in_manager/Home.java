package com.indicosmic.www.mypolicynow.break_in_manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.indicosmic.www.mypolicynow.MainActivity;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.break_in_manager.inprogressInspection.InprogressInspection;
import com.indicosmic.www.mypolicynow.break_in_manager.newInspection.NewInspection;
import com.indicosmic.www.mypolicynow.break_in_manager.referbackInspection.ReferbackInspection;
import com.indicosmic.www.mypolicynow.fragments.NewVehicleFragment;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.utils.GPSTracker;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.StoreCounter;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView agent_name;
    Agentinfo agent;
    String agent_id;
    boolean doubleBackToExitPressedOnce = false;

    // GPSTracker class
    GPSTracker gps;
    double latitude;
    double longitude;

    StoreCounter mStoreCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //getting agent info
        agent = SharedPrefManager.getInstance(this).getAgent();
        agent_id = SharedPrefManager.getInstance(getApplicationContext()).getAgent().getId();
        if (agent_id != null) {

            SingletonClass.getinstance().agent_id = agent_id;
        }



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        agent_name = (TextView)headerView.findViewById(R.id.agent_name);
        agent_name.setText(agent.getFull_name());



        gps = new GPSTracker(Home.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            SingletonClass.getinstance().latitude = latitude;
            SingletonClass.getinstance().longitude = longitude;


        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainfragment, new NewVehicleFragment()).commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.left_right,R.animator.right_left);
            finish();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
       // int id = item.getItemId();

        switch(item.getItemId()){
            case R.id.nav_newinspection:
                Intent newVehicleIntent = new Intent(Home.this, NewInspection.class);
                startActivity(newVehicleIntent);
                break;
            case R.id.nav_wip:
                Intent inProgressIntent = new Intent(Home.this, InprogressInspection.class);
                startActivity(inProgressIntent);
                break;
            case R.id.nav_completed:
                Intent completedIntent = new Intent(Home.this, ReferbackInspection.class);
                startActivity(completedIntent);
                break;
           /* case R.id.nav_logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
               // mStoreCounter.clearCounter();
                Intent i = new Intent(getApplicationContext(),S.class);
                startActivity(i);
                finish();
                break;
*/
            default:
        }

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
