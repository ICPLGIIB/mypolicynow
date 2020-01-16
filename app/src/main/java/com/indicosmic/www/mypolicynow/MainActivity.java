package com.indicosmic.www.mypolicynow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.google.android.material.navigation.NavigationView;
import com.indicosmic.www.mypolicynow.break_in_manager.Home;
import com.indicosmic.www.mypolicynow.model.Agentinfo;
import com.indicosmic.www.mypolicynow.mypolicynow_activities.MyPolicyNowDashboard;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow.utils.SingletonClass;
import com.indicosmic.www.mypolicynow.utils.StoreCounter;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String StrMobile,StrUserName,StrUserId,StrEmail,StrUniqueId;
    TextView TV_WelcomeTxt,TV_TodayDate;
    ProgressDialog myDialog;
    private static final String TAG = "MainActivity";
    CardView CardMyPolicyNow,CardRSA,CardBreakInManager;
    StoreCounter mStoreCounter;
    Agentinfo agent;
    String agent_id;
    /**
     * Bluetooth device discovery time，second。
     */
    private static final int BLUETOOTH_DISCOVERABLE_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewByIds();

    }

    private void findViewByIds() {
        StrMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserMobile");
        StrUserName = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserName");
        StrUserId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserId");
        StrEmail = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserEmail");
        StrUniqueId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UniqueUserId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        //((TextView)toolbar.getChildAt(0)).setTextSize(25);
        setSupportActionBar(toolbar);
       // Typeface type = Typeface.createFromAsset(getAssets(),"font/Roboto-Regular.ttf");




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //TextView nav_header_userId = navigationView.findViewById(R.id.nav_header_userId);
        View hView =  navigationView.getHeaderView(0);
        ImageView iv_logo = (ImageView)hView.findViewById(R.id.iv_logo);
        ImageView iv_facebook = (ImageView)navigationView.findViewById(R.id.iv_facebook);
        ImageView iv_google_plus = (ImageView)navigationView.findViewById(R.id.iv_google_plus);
        ImageView iv_twitter = (ImageView)navigationView.findViewById(R.id.iv_twitter);
        ImageView iv_youtube = (ImageView)navigationView.findViewById(R.id.iv_youtube);
        ImageView iv_linked_in = (ImageView)navigationView.findViewById(R.id.iv_linked_in);

        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.facebook_page)));
                startActivity(i);
            }
        });

        iv_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.google_plus_page)));
                startActivity(i);
            }
        });

        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.twitter_page)));
                startActivity(i);
            }
        });

        iv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.youtube_page)));
                startActivity(i);
            }
        });

        iv_linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.linked_in_page)));
                startActivity(i);
            }
        });

        /*Glide.with(this)
                .load("https://www.mypolicynow.com/assets/images/logo_spinner.gif")
                .placeholder(R.drawable.logo_spinner)
                .into(iv_logo);*/

        agent = SharedPrefManager.getInstance(this).getAgent();
        agent_id = SharedPrefManager.getInstance(getApplicationContext()).getAgent().getId();
        if (agent_id != null) {

            SingletonClass.getinstance().agent_id = agent_id;
        }
        TextView nav_header_userName = (TextView)hView.findViewById(R.id.nav_header_userName);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.nav_Email);
        if(StrUserName!=null && !StrUserName.equalsIgnoreCase("")){
            nav_header_userName.setText(StrUserName.toUpperCase());
        }
        nav_user_email.setText("Mobile: "+StrMobile);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        TV_WelcomeTxt = (TextView)findViewById(R.id.TV_WelcomeTxt);
        if(StrUserName!=null && !StrUserName.equalsIgnoreCase("")){
            TV_WelcomeTxt.setText("Welcome, "+StrUserName.toUpperCase());
        }else {
            TV_WelcomeTxt.setText("Welcome");
        }

        TV_TodayDate = (TextView)findViewById(R.id.TV_TodayDate);
        TV_TodayDate.setText(CommonMethods.DisplayCurrentDate());


        myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);


        CardMyPolicyNow = (CardView)findViewById(R.id.CardMyPolicyNow);
        CardRSA= (CardView)findViewById(R.id.CardRSA);
        CardBreakInManager= (CardView)findViewById(R.id.CardBreakInManager);

        CardMyPolicyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), MyPolicyNowDashboard.class);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
            }
        });

        CardBreakInManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fetchCounter();

            }
        });
    }


    public void fetchCounter() {
        myDialog.show();

        mStoreCounter= new StoreCounter(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_INSPECTION_COUNTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(myDialog!=null && myDialog.isShowing()){
                    myDialog.dismiss();
                }
                try {

                    Log.d("Response", ""+response);
                    JSONObject jsonresponse = new JSONObject(response);

                    String status = jsonresponse.getString("status");

                    if (status.trim().contains("TRUE")) {
                        JSONObject jsonObject = jsonresponse.getJSONObject("data");
                        String pening_counter = jsonObject.getString("Pending");
                        SingletonClass.getinstance().newInspection_count = Integer.parseInt(pening_counter);

                        String inprogress_counter = jsonObject.getString("inprocess");
                        SingletonClass.getinstance().inprogress_count = Integer.parseInt(inprogress_counter);

                        String referback_counter = jsonObject.getString("referback");
                        SingletonClass.getinstance().referback_count = Integer.parseInt(referback_counter);

                        mStoreCounter.storeCounterValue(pening_counter, inprogress_counter, referback_counter);

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                        finish();

                    }
                    /*

                        //


                        for (int i = 0; i < ins_counter_array.length(); i++) {

                            JSONObject jsonObject = ins_counter_array.getJSONObject(i);
                            String inner_status = jsonObject.getString("status");

                            if (inner_status.trim().contains("Pending")){

                                pening_counter = jsonObject.getString("counter");


                            }if (inner_status.trim().contains("inprocess")){

                                 = jsonObject.getString("counter");


                              // Toast.makeText(Splash2.this, "inprogress_counter="+inprogress_counter, Toast.LENGTH_SHORT).show();

                            }if (inner_status.trim().contains("referback")){

                                 = jsonObject.getString("counter");
                             // Toast.makeText(Splash2.this, "referback_counter="+referback_counter, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }*/


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(myDialog!=null && myDialog.isShowing()){
                    myDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("pos_id", SharedPrefManager.getInstance(getApplicationContext()).getAgent().id);
                if (SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id != null && !SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id.isEmpty()
                        && !SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id.equals("")) {
                    map.put("business_id",SharedPrefManager.getInstance(getApplicationContext()).getAgent().business_id);
                }else {
                    map.put("business_id","");
                }

                Log.d("POSTDATA", "counter_data: "+map.toString());
                return map;
            }
        };


        int socketTimeout = 50000; //30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about_us) {
            Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        }else if (id == R.id.nav_settings) {
            Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        } else if(id == R.id.nav_contact_us){
            Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();

        }else if (id == R.id.nav_logout){

            SharedPrefManager.getInstance(getApplicationContext()).logout();
            UtilitySharedPreferences.clearPref(getApplicationContext());
            Intent i = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.left_right,R.animator.right_left);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






}
