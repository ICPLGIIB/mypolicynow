package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.indicosmic.www.mypolicynow.AboutUsActivity;
import com.indicosmic.www.mypolicynow.ContactUsActivity;
import com.indicosmic.www.mypolicynow.MainActivity;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.SettingsActivity;
import com.indicosmic.www.mypolicynow.SplashActivity;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.Constant;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.utils.CommonMethods.md5;

public class MyPolicyNowDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String StrMobile,StrUserName,StrUserId,StrEmail,StrUniqueId,POS_TOKEN="";
    TextView TV_WelcomeTxt,TV_TodayDate;
    ProgressDialog myDialog;
    private static final String TAG = "MainActivity";
    LinearLayout LayoutCar,LayoutBike,LayoutCommercial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_policy_now);
        init();
    }

    private void init() {
        StrMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserMobile");
        StrUserName = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserName");
        StrUserId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserId");
        StrEmail = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserEmail");
        StrUniqueId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UniqueUserId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



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

        Glide.with(this)
                .load("https://www.mypolicynow.com/assets/images/logo_spinner.gif")
                .placeholder(R.drawable.logo_spinner)
                .into(iv_logo);

        TextView nav_header_userName = (TextView)hView.findViewById(R.id.nav_header_userName);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.nav_Email);
        if(StrUserName!=null && !StrUserName.equalsIgnoreCase("")){
            nav_header_userName.setText(StrUserName.toUpperCase());
        }
        //nav_user_email.setText("Email: "+StrEmail);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);





        myDialog = new ProgressDialog(MyPolicyNowDashboard.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        getPasswordForMPN();

        LayoutCar = (LinearLayout)findViewById(R.id.LayoutCar);
        LayoutBike = (LinearLayout)findViewById(R.id.LayoutBike);
        LayoutCommercial = (LinearLayout)findViewById(R.id.LayoutCommercial);


        LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Car");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }
            }
        });

        LayoutBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }
            }
        });


        LayoutCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Commercial");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }
            }
        });


    }


        private void getPasswordForMPN() {

                myDialog.show();

                String URL = RestClient.ROOT_URL2+"getpassword";
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                boolean isInternetPresent = cd.isConnectingToInternet();
                if (isInternetPresent) {
                    Log.d("URL", "" + URL);
                    StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            try {

                                Log.d("Response", "" + response);
                                JSONObject jsonresponse = new JSONObject(response);

                                JSONObject data_obj = jsonresponse.getJSONObject("data");

                                 POS_TOKEN  = data_obj.getString("token");



                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("mobile_number", StrMobile);
                            map.put("email_id", StrEmail);
                            if(StrMobile!=null && !StrMobile.equalsIgnoreCase("")){
                                map.put("access_key", md5(StrMobile));
                            }else if(StrEmail!=null && !StrEmail.equalsIgnoreCase("")){
                                map.put("access_key", md5(StrEmail));
                            }
                            Log.d("GetPasswordToken",""+map);
                            return map;
                        }
                    };


                    int socketTimeout = 50000; //30 seconds - change to what you want
                    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    request.setRetryPolicy(policy);
                    // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(request);
                }else {
                    CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
                }




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


            UtilitySharedPreferences.clearPref(getApplicationContext());
            Intent i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.left_right,R.animator.right_left);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
