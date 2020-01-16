package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.model.Helper;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.Constant;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.utils.CommonMethods.ucFirst;
import static com.indicosmic.www.mypolicynow.utils.CommonMethods.ucFirst;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL2;

public class IcListingQuoteScreen extends AppCompatActivity {

    ImageView iv_vehicle_type;
    TextView tv_vehicle_make_model_variant_name,tv_vehicle_id,tv_vehicle_make_id,tv_vehicle_model_id,tv_vehicle_variant_id,
            tv_cc,tv_fuel,tv_rto,tv_rto_id,tv_policy_package_type,tv_policy_start_date,tv_previous_ncb,tv_policy_type,tv_policy_end_date,
            tv_current_ncb,tv_policy_holder_type,tv_claimed_in_past_year,tv_plan_type,tv_previous_policy_type,tv_previous_policy_expiry_date,
            tv_pa_cover_tenure;
    LinearLayout LinearPA_Cover,LinearIsClaimed,LinearPreviousPolicyData,LinearEdit;

    String StrAgentId="",StrMpnData="",StrUserActionData="",StrImageUrl="",StrPlanTypeData="",selected_od_year,ProductTypeId="";
    String StrMake,StrModel,StrVariant,Str_vehicle_make_model_variant_name="",Str_vehicle_id="",Str_vehicle_make_id="",Str_vehicle_model_id="",Str_vehicle_variant_id="",
    Str_cc="",Str_fuel="",Str_rto="",Str_policy_package_type="",Str_policy_start_date="",Str_previous_ncb="",Str_policy_type="",Str_policy_end_date="",
    Str_current_ncb="",Str_policy_holder_type="",Str_claimed_in_past_year="",Str_plan_type="",Str_previous_policy_type="",Str_previous_policy_expiry_date="",
    Str_pa_cover_tenure="",Str_rto_id="",Str_have_pa_policy="",Str_product_type_id="";
    ProgressDialog myDialog;
    LinearLayout ll_parent_portfolio;
    Dialog DialogBreakUpDetail;


    String error_message,vehicle_idv,available_od_discount,total_addon_premium,gross_premium,addon_button_html_message;
    JSONObject vehicle_idv_yearObj,start_date_yearObj,end_date_yearObj,total_basic_od_yrObj,
            total_electrical_premiumObj,total_non_electrical_premiumObj,
            total_od_yr_wiseObj,total_geographical_value_odObj,year_wiseObj,total_bifuel_premiumObj;
    String total_basic_od_year_amount,total_electrical_year_amount,total_non_electrical_year_amount,total_geographical_year_amount,
            total_basic_od_premium,total_bifuel_value;

    JSONObject deductibles_year_wiseObj,antitheft_value_year_wiseObj,total_automobile_association_premium_year_wiseObj,
            ncb_valueObj,deductibles_total_year_wiseObj;
    String total_antitheft_value,total_automobile_association_value,total_ncb_value,deductibles_total_year_value;

    public  String[] IcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_listing_quotation_screen);


        init();
    }

    public void init(){

        myDialog = new ProgressDialog(IcListingQuoteScreen.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserActionData");
        String ic_list =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"IcList");
        ProductTypeId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ProductTypeId");

        IcList = ic_list.split("\\s*,\\s*");


        iv_vehicle_type = (ImageView)findViewById(R.id.iv_vehicle_type);
        //

        tv_vehicle_make_model_variant_name = (TextView)findViewById(R.id.tv_vehicle_make_model_variant_name);
        tv_vehicle_id = (TextView)findViewById(R.id.tv_vehicle_id);
        tv_vehicle_make_id = (TextView)findViewById(R.id.tv_vehicle_make_id);
        tv_vehicle_model_id = (TextView)findViewById(R.id.tv_vehicle_model_id);
        tv_vehicle_variant_id = (TextView)findViewById(R.id.tv_vehicle_variant_id);

        tv_cc = (TextView)findViewById(R.id.tv_cc);
        tv_fuel = (TextView)findViewById(R.id.tv_fuel);
        tv_rto = (TextView)findViewById(R.id.tv_rto);
        tv_rto_id = (TextView)findViewById(R.id.tv_rto_id);

        tv_policy_package_type = (TextView)findViewById(R.id.tv_policy_package_type);
        tv_policy_start_date = (TextView)findViewById(R.id.tv_policy_start_date);
        tv_previous_ncb = (TextView)findViewById(R.id.tv_previous_ncb);
        tv_policy_type = (TextView)findViewById(R.id.tv_policy_type);
        tv_policy_end_date = (TextView)findViewById(R.id.tv_policy_end_date);
        tv_current_ncb = (TextView)findViewById(R.id.tv_current_ncb);
        tv_policy_holder_type = (TextView)findViewById(R.id.tv_policy_holder_type);
        tv_claimed_in_past_year = (TextView)findViewById(R.id.tv_claimed_in_past_year);

        tv_plan_type = (TextView)findViewById(R.id.tv_plan_type);
        tv_previous_policy_type = (TextView)findViewById(R.id.tv_previous_policy_type);
        tv_previous_policy_expiry_date = (TextView)findViewById(R.id.tv_previous_policy_expiry_date);
        tv_pa_cover_tenure = (TextView)findViewById(R.id.tv_pa_cover_tenure);

        LinearPA_Cover = (LinearLayout)findViewById(R.id.LinearPA_Cover);
        LinearIsClaimed = (LinearLayout)findViewById(R.id.LinearIsClaimed);
        LinearPreviousPolicyData= (LinearLayout)findViewById(R.id.LinearPreviousPolicyData);
        LinearEdit = (LinearLayout)findViewById(R.id.LinearEdit);
        LinearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {
            JSONObject user_action_dataObj = new JSONObject(StrUserActionData);
            StrMake = user_action_dataObj.getString("make");
            Str_vehicle_make_id = user_action_dataObj.getString("make_id");
            StrModel = user_action_dataObj.getString("model");
            Str_vehicle_model_id = user_action_dataObj.getString("model_id");
            StrVariant = user_action_dataObj.getString("variant");
            Str_vehicle_id = user_action_dataObj.getString("vehicle_id");
            Str_vehicle_variant_id = user_action_dataObj.getString("variant_id");
            Str_fuel = user_action_dataObj.getString("fuel");
            Str_rto = user_action_dataObj.getString("rto_label");
            Str_rto_id = user_action_dataObj.getString("rto_id");
            Str_cc = user_action_dataObj.getString("cc");
            Str_have_pa_policy = user_action_dataObj.getString("have_pa_policy");
            Str_product_type_id = user_action_dataObj.getString("product_type_id");
            Str_policy_package_type = user_action_dataObj.getString("policy_package_type");
            Str_policy_type = user_action_dataObj.getString("policy_type");
            Str_policy_holder_type = user_action_dataObj.getString("policy_holder_type");

            if(Str_policy_holder_type.equalsIgnoreCase("individual")){
                Str_pa_cover_tenure = user_action_dataObj.getString("selected_pa_year");
            }

            if(Str_policy_type.equalsIgnoreCase("renew")) {
                Str_claimed_in_past_year = user_action_dataObj.getString("is_claimed");
                Str_previous_policy_type = user_action_dataObj.getString("previous_yr_policy_type");
                Str_previous_policy_expiry_date = user_action_dataObj.getString("previous_policy_expiry_date");
                Str_previous_ncb = user_action_dataObj.getString("previous_policy_ncb");

            }else {
                Str_previous_ncb = "0";

            }


            if(Str_product_type_id.equalsIgnoreCase("2")){
                StrImageUrl = ROOT_URL2+"assets/images/car/bike.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("1")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/hondacity-img.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("3")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/taxi.jpg";
            } else if ((Str_product_type_id.equalsIgnoreCase("4")) || Str_product_type_id.equalsIgnoreCase("5")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/truck.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("6")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/bus.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("7") || Str_product_type_id.equalsIgnoreCase("8") ||
                    Str_product_type_id.equalsIgnoreCase("9")|| Str_product_type_id.equalsIgnoreCase("10")||
                    Str_product_type_id.equalsIgnoreCase("11") || Str_product_type_id.equalsIgnoreCase("12") ||
                    Str_product_type_id.equalsIgnoreCase("13")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/auto.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("15")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/miscd.jpg";
            } else if (Str_product_type_id.equalsIgnoreCase("16") || Str_product_type_id.equalsIgnoreCase("17")) {
                StrImageUrl = ROOT_URL2+"assets/images/car/trailer.jpg";
            }

            Glide.with(IcListingQuoteScreen.this).load(StrImageUrl).into(iv_vehicle_type);

            if(Str_product_type_id.equalsIgnoreCase("2")){
                if(Str_policy_type.equalsIgnoreCase("new")){
                    selected_od_year = user_action_dataObj.getString("selected_od_year");
                    if(selected_od_year.equalsIgnoreCase("1")){
                        Str_plan_type = "1 year OD And 5 year TP";
                    }else if(selected_od_year.equalsIgnoreCase("5")){
                        Str_plan_type = "5 year OD And 5 year TP";
                    }

                }else if(Str_policy_type.equalsIgnoreCase("renew")){
                    selected_od_year = user_action_dataObj.getString("selected_od_year");
                    if(selected_od_year.equalsIgnoreCase("1")){
                        Str_plan_type = "1 year OD And 1 year TP";
                    }
                }
            }else if(Str_product_type_id.equalsIgnoreCase("1")){
                if(Str_policy_type.equalsIgnoreCase("new")){
                    selected_od_year = user_action_dataObj.getString("selected_od_year");
                    if(selected_od_year.equalsIgnoreCase("1")){
                        Str_plan_type = "1 year OD And 3 year TP";
                    }else if(selected_od_year.equalsIgnoreCase("3")){
                        Str_plan_type = "3 year OD And 3 year TP";
                    }

                }else if(Str_policy_type.equalsIgnoreCase("renew")){
                    String selected_od_year = user_action_dataObj.getString("selected_od_year");
                    if(selected_od_year.equalsIgnoreCase("1")){
                        Str_plan_type = "1 year OD And 1 year TP";
                    }
                }
            }





            JSONObject mpn_dataObj = new JSONObject(StrMpnData);
            JSONObject policy_start_date_arr = mpn_dataObj.getJSONObject("policy_start_date_arr");
            Str_policy_start_date = policy_start_date_arr.getString("date");

            JSONObject policy_end_date_arr = mpn_dataObj.getJSONObject("policy_end_date_arr");
            Str_policy_end_date = policy_end_date_arr.getString("date");

            Str_current_ncb = mpn_dataObj.getString("current_ncb");




        } catch (JSONException e) {
            e.printStackTrace();
        }

        tv_vehicle_make_model_variant_name.setText(StrMake.toUpperCase()+" "+StrModel.toUpperCase()+ " "+StrVariant.toUpperCase());
        tv_vehicle_id.setText(Str_vehicle_id);
        tv_vehicle_make_id.setText(Str_vehicle_make_id);
        tv_vehicle_model_id.setText(Str_vehicle_model_id);
        tv_vehicle_variant_id.setText(Str_vehicle_variant_id);
        tv_cc.setText("("+Str_cc.toUpperCase()+" CC)");
        tv_fuel.setText(Str_fuel.toUpperCase());
        tv_rto.setText(Str_rto.toUpperCase());
        tv_rto_id.setText(Str_rto_id);
        tv_policy_package_type.setText(ucFirst(Str_policy_package_type));
        tv_policy_type.setText(ucFirst(Str_policy_type));

        tv_policy_start_date.setText(Str_policy_start_date);
        tv_policy_end_date.setText(Str_policy_end_date);

        tv_previous_ncb.setText("("+Str_previous_ncb +" %)");
        tv_current_ncb.setText("("+Str_current_ncb +" %)");

        tv_policy_holder_type.setText(ucFirst(Str_policy_holder_type));
        tv_plan_type.setText(ucFirst(Str_plan_type));
        if(Str_pa_cover_tenure!=null && !Str_pa_cover_tenure.equalsIgnoreCase("")){
            tv_pa_cover_tenure.setText(ucFirst(Str_pa_cover_tenure +" Year") );
            LinearPA_Cover.setVisibility(View.VISIBLE);
        }else {
            LinearPA_Cover.setVisibility(View.GONE);
        }


        if(Str_policy_type.equalsIgnoreCase("renew")) {
            LinearIsClaimed.setVisibility(View.VISIBLE);
            LinearPreviousPolicyData.setVisibility(View.VISIBLE);
            tv_claimed_in_past_year.setText(ucFirst(Str_claimed_in_past_year));
            tv_previous_policy_type.setText(ucFirst(Str_previous_policy_type));
            tv_previous_policy_expiry_date.setText(Str_previous_policy_expiry_date);

        }else {
            LinearIsClaimed.setVisibility(View.INVISIBLE);
            LinearPreviousPolicyData.setVisibility(View.GONE);
        }
        ll_parent_portfolio = (LinearLayout)findViewById(R.id.ll_parent_portfolio);
        Log.d("IcList",""+IcList.toString());
        IcList = new String[] {"3","6"};
        for(int k=0; k< IcList.length; k++){
            String ic_id = IcList[k];
            LoadQuotation(ic_id);
        }


    }

    private void LoadQuotation(String ic_id) {

        String URL = RestClient.ROOT_URL2+"loadquotation";
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
                        JSONObject responseObj = new JSONObject(response);
                        JSONObject dataObj = responseObj.getJSONObject("data");

                        //Quote Html
                        JSONObject quote_html = dataObj.getJSONObject("quote_api");
                        String logo_url = quote_html.getString("logo_url");
                        String ic_id_name = quote_html.getString("ic_id_name");

                        String error = quote_html.getString("error");

                       //boolean is_addon = quote_html.getBoolean("is_addon");



                        if(error.equalsIgnoreCase("1")){
                            error_message = quote_html.getString("error_message");
                        }else {

                            vehicle_idv = quote_html.getString("vehicle_idv");
                            available_od_discount = quote_html.getString("maxdisc");
                            total_addon_premium = quote_html.getString("total_addon_premium");
                            gross_premium = quote_html.getString("gross_premium");
                            addon_button_html_message = quote_html.getString("addon_button_html_message");
                        }
                        //idv _ breakUp data

                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View rowView = inflater.inflate(R.layout.scheme_list_row1, null);
                        rowView.setPadding(10,10,10,10);
                        ImageView iv_IC_Img = (ImageView) rowView.findViewById(R.id.iv_IC_Img);

                        Glide.with(IcListingQuoteScreen.this)
                                .load(logo_url)
                                .into(iv_IC_Img);

                        TextView row_ic_id = (TextView)rowView.findViewById(R.id.row_ic_id);
                        row_ic_id.setText(ic_id);



                        TextView row_net_premium_amt = (TextView)rowView.findViewById(R.id.row_net_premium_amt);

                        Button btn_add_on = (Button)rowView.findViewById(R.id.btn_add_on);
                        Button btn_breakup = (Button)rowView.findViewById(R.id.btn_breakup);
                        Button btn_buy_policy = (Button)rowView.findViewById(R.id.btn_buy_policy);

                        LinearLayout Linear_row_Premium = (LinearLayout)rowView.findViewById(R.id.Linear_row_Premium);
                        LinearLayout row_linear_idv = (LinearLayout)rowView.findViewById(R.id.row_linear_idv);
                        LinearLayout row_linear_buy_policy = (LinearLayout)rowView.findViewById(R.id.row_linear_buy_policy);
                        if(error.equalsIgnoreCase("1"))
                        {
                            btn_add_on.setText(error_message);
                            Linear_row_Premium.setVisibility(View.GONE);
                            btn_breakup.setVisibility(View.GONE);
                            row_linear_idv.setVisibility(View.GONE);
                            row_linear_buy_policy.setVisibility(View.GONE);
                        }else {
                            btn_breakup.setVisibility(View.VISIBLE);
                            row_linear_buy_policy.setVisibility(View.VISIBLE);
                            row_linear_idv.setVisibility(View.VISIBLE);
                            Linear_row_Premium.setVisibility(View.VISIBLE);

                            row_net_premium_amt.setText("\u20B9 "+gross_premium);

                            TextView tv_idv_in_rupees = (TextView) rowView.findViewById(R.id.tv_idv_in_rupees);
                            tv_idv_in_rupees.setText("\u20B9 "+vehicle_idv);



                            TextView row_addon_premium_amt = (TextView)rowView.findViewById(R.id.row_addon_premium_amt);
                            row_addon_premium_amt.setText("\u20B9 "+total_addon_premium + " (excl. tax)");


                            btn_add_on.setText(addon_button_html_message);
                           /* btn_add_on.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    CommonMethods.DisplayToastInfo(getApplicationContext(), "No AddOns Available");
                                }
                            });*/

                        }

                        btn_breakup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ShowPopUpForBreakupDetails(dataObj.toString());

                            }
                        });

                        btn_buy_policy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                API_BUY_POLICY(row_ic_id.getText().toString());

                            }
                        });


                        ll_parent_portfolio.addView(rowView);


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
                    map.put("agent_id", StrAgentId);
                    map.put("mpn_data",StrMpnData);
                    map.put("user_action_data",StrUserActionData);
                    map.put("ic_id",ic_id);
                    Log.d("MapLoadIcList",""+map);
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

    private void API_BUY_POLICY(String ic_id) {

        myDialog.show();
        String URL = RestClient.ROOT_URL2+"buypolicy";
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
                        JSONObject responseObj = new JSONObject(response);
                        JSONObject dataObj = responseObj.getJSONObject("data");
                        JSONObject mpn_dataObj = dataObj.getJSONObject("mpn_data");
                        JSONObject user_action_dataObj = dataObj.getJSONObject("user_action_data");

                       /* JSONObject customerObj = new JSONObject();
                        mpn_dataObj.put("customer_quote",customerObj);
*/

                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"MpnData",mpn_dataObj.toString());
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserActionData",user_action_dataObj.toString());
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"SelectedIcId",ic_id);

                        Intent i = new Intent(getApplicationContext(),CustomerPage.class);
                        startActivity(i);
                        overridePendingTransition(R.animator.move_left,R.animator.move_right);

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
                    map.put("agent_id", StrAgentId);
                    map.put("mpn_data",StrMpnData);
                    map.put("user_action_data",StrUserActionData);
                    map.put("ic_id",ic_id);
                    Log.d("BuyPolicyData",""+map);
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


    private void ShowPopUpForBreakupDetails(String response) {


        DialogBreakUpDetail = new Dialog(IcListingQuoteScreen.this);
        DialogBreakUpDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogBreakUpDetail.setCanceledOnTouchOutside(true);
        DialogBreakUpDetail.setCancelable(true);
        DialogBreakUpDetail.setContentView(R.layout.layout_scheme_detail_break_up);
        DialogBreakUpDetail.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogBreakUpDetail.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogBreakUpDetail.findViewById(R.id.iv_close);

        CardView CardVehicleDetails = (CardView) DialogBreakUpDetail.findViewById(R.id.CardVehicleDetails);
        CardView CardTPBreakup = (CardView) DialogBreakUpDetail.findViewById(R.id.CardTPBreakup);
        CardView CardODBreakup = (CardView) DialogBreakUpDetail.findViewById(R.id.CardODBreakup);

        CardView CardDeductibles = (CardView) DialogBreakUpDetail.findViewById(R.id.CardDeductibles);
        CardView CardPremiumSummary = (CardView) DialogBreakUpDetail.findViewById(R.id.CardPremiumSummary);


        ImageView iv_Ic_Img = (ImageView)DialogBreakUpDetail.findViewById(R.id.iv_Ic_Img);
        final TextView row_make_model_variant = (TextView) DialogBreakUpDetail.findViewById(R.id.row_make_model_variant);
        final TextView tv_vehicle_age = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_vehicle_age);
        final TextView tv_cc = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_cc);
        final TextView tv_idv = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_idv);
        final TextView tv_seating_capacity = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_seating_capacity);
        final TextView tv_fuel = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_fuel);
        final TextView tv_year = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_year);
        final TextView tv_rto = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_rto);
        final TextView tv_zone = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_zone);

        LinearLayout LayoutOd_Renew = (LinearLayout)DialogBreakUpDetail.findViewById(R.id.LayoutOd_Renew);
        WebView Wb_OD_BreakUp = (WebView)DialogBreakUpDetail.findViewById(R.id.Wb_OD_BreakUp);
        final TextView tv_basic_od_odbp = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_basic_od_odbp);
        final TextView tv_electrical_accessory_premium = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_electrical_accessory_premium);
        final TextView tv_non_electric_accessory = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_non_electric_accessory);
        final TextView tv_total_geographical_od = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_geographical_od);
        final TextView tv_total_net_od_premium = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_net_od_premium);


        final TextView tv_basic_tp_liability = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_basic_tp_liability);
        final TextView tv_compulsory_pa = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_compulsory_pa);
        final TextView tv_pa_to_other_person = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_pa_to_other_person);
        final TextView tv_total_geographical_tp = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_geographical_tp);
        final TextView tv_total_tp_premium = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_tp_premium);


        final TextView tv_total_idv = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_idv);
        final TextView tv_od_premmium_a = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_od_premmium_a);
        final TextView tv_add_on_premium_b = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_add_on_premium_b);
        final TextView tv_total_deductibles_c = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_deductibles_c);
        final TextView tv_net_od_premium_d = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_net_od_premium_d);
        final TextView tv_net_od_premium_gst_e = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_net_od_premium_gst_e);
        final TextView tv_net_od_premium_gst_f = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_net_od_premium_gst_f);

        final TextView til_g_label = (TextView) DialogBreakUpDetail.findViewById(R.id.til_g_label);
        final TextView tv_basic_tp_g = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_basic_tp_g);


        final TextView til_h_label = (TextView) DialogBreakUpDetail.findViewById(R.id.til_h_label);
        final TextView tv_basic_tp_18_h = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_basic_tp_18_h);

        final TextView tv_tp_other_i = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_tp_other_i);
        final TextView tv_tp_other_18_j = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_tp_other_18_j);
        final TextView tv_total_tp_k = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_tp_k);
        final TextView tv_total_premium_l = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_premium_l);
        final TextView tv_total_premium_with_gst_18_m = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_premium_with_gst_18_m);
        final TextView tv_total_total_premium_payable_n = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_total_premium_payable_n);

        LinearLayout LayoutDeductibles_Renew = (LinearLayout)DialogBreakUpDetail.findViewById(R.id.LayoutDeductibles_Renew);
        WebView Wb_Deductibles = (WebView)DialogBreakUpDetail.findViewById(R.id.Wb_Deductibles);
        final TextView tv_deduct_anti_theft = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_deduct_anti_theft);
        final TextView tv_automobile_deductible = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_automobile_deductible);

        final TextView til_ncb_percentage = (TextView) DialogBreakUpDetail.findViewById(R.id.til_ncb_percentage);
        final TextView tv_ncb_deductibles = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_ncb_deductibles);
        final TextView tv_total_discount_deductibles = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_discount_deductibles);



        DialogBreakUpDetail.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogBreakUpDetail!=null && DialogBreakUpDetail.isShowing()) {
                    DialogBreakUpDetail.dismiss();
                }

            }
        });

        Log.d("Response Of Detail",response);

        try {

            if (response != null && !response.equalsIgnoreCase("")) {
                //JSONObject responseObj =
                JSONObject dataObj = new JSONObject(response);

                //Quote Html
                JSONObject quote_html = dataObj.getJSONObject("quote_api");
                String logo_url = quote_html.getString("logo_url");
                String ic_id_name = quote_html.getString("ic_id_name");
                String error = quote_html.getString("error");


                Glide.with(IcListingQuoteScreen.this)
                        .load(logo_url)
                        .into(iv_Ic_Img);

                //boolean is_addon = quote_html.getBoolean("is_addon");

                JSONObject breakup_html = dataObj.getJSONObject("breakup_api");



                if(error.equalsIgnoreCase("1")){
                    error_message = quote_html.getString("error_message");
                }else {



                    CardVehicleDetails.setVisibility(View.VISIBLE);
                    CardODBreakup.setVisibility(View.VISIBLE);
                    CardTPBreakup.setVisibility(View.VISIBLE);
                    CardDeductibles.setVisibility(View.VISIBLE);
                    CardPremiumSummary.setVisibility(View.VISIBLE);

                    // Vehicle Details
                    JSONObject vehicle_detailsObj = breakup_html.getJSONObject("vehicle_details");
                    String make_model_variant = vehicle_detailsObj.getString("make_model_variant");
                    String idv = vehicle_detailsObj.getString("total_vehicle_idv");
                    String vehicle_age_year = vehicle_detailsObj.getString("vehicle_age_year");
                    String cc = vehicle_detailsObj.getString("cc");
                    String seating_capacity = vehicle_detailsObj.getString("seating_capacity");
                    String fuel = vehicle_detailsObj.getString("fuel");
                    String manufacturing_year = vehicle_detailsObj.getString("manufacturing_year");
                    String rto_city = vehicle_detailsObj.getString("rto_city");
                    String rto_zone_name = vehicle_detailsObj.getString("rto_zone_name");

                    row_make_model_variant.setText(make_model_variant.toUpperCase());
                    tv_idv.setText("\u20B9 " + idv);
                    tv_vehicle_age.setText(vehicle_age_year);
                    tv_cc.setText(cc +" CC");
                    tv_seating_capacity.setText(seating_capacity);
                    tv_fuel.setText(fuel);
                    tv_year.setText(manufacturing_year);
                    tv_rto.setText(rto_city);
                    tv_zone.setText(rto_zone_name);

                    //Third Party
                    String basic_third_party_liability = breakup_html.getString("basic_third_party_liability");
                    String Compulsary_pa_to_owner_driver = breakup_html.getString("Compulsary_pa_to_owner_driver");
                    String pa_cover_to_persons_other_than_owner_driver = breakup_html.getString("pa_cover_to_persons_other_than_owner_driver");
                    String total_geographical_extension_tp = breakup_html.getString("total_geographical_extension_tp");
                    String total_tp_premium = breakup_html.getString("total_tp_premium");



                    tv_basic_tp_liability.setText(basic_third_party_liability);
                    tv_compulsory_pa.setText(Compulsary_pa_to_owner_driver);
                    tv_pa_to_other_person.setText(pa_cover_to_persons_other_than_owner_driver);
                    tv_total_geographical_tp.setText(total_geographical_extension_tp);
                    tv_total_tp_premium.setText(total_tp_premium);



                    // Premium Summary
                    String total_idv = breakup_html.getString("total_idv");
                    String a_od_premium = breakup_html.getString("a_od_premium");
                    String b_addOn_Premium = breakup_html.getString("b_add_on_Premium");
                    String c_total_Deductibles = breakup_html.getString("c_total_deductibles");
                    String netOd_premium_a_b_c = breakup_html.getString("net_od_premium_a_b_c");
                    String e_net_od_premium_gst = breakup_html.getString("e_net_od_premium_gst");
                    String f_net_od_Premium_with_gst = breakup_html.getString("f_net_od_premium_with_gst");
                    String basic_cngailer_label = breakup_html.getString("basic_cng_trailer_label");
                    String basic_cngailer_tp = breakup_html.getString("basic_cng_trailer_tp");

                    String basic_cngailer_tp_gst_label = breakup_html.getString("basic_cng_trailer_tp_gst_label");
                    String basic_cngailer_tp_gst = breakup_html.getString("basic_cng_trailer_tp_gst");

                    String i_tp_other = breakup_html.getString("i_tp_other");
                    String j_tp_Other_gst = breakup_html.getString("j_tp_Other_gst");
                    String k_total_tp_premium = breakup_html.getString("k_total_tp_premium");
                    String l_total_tp_premium_with_gst = breakup_html.getString("l_total_tp_premium_with_gst");
                    String m_total_tp_premium_with_gst = breakup_html.getString("m_total_tp_premium_with_gst");
                    String kerala_cess = breakup_html.getString("kerala_cess");
                    String total_premium_payable = breakup_html.getString("total_premium_payable");

                    tv_total_idv.setText(total_idv);
                    tv_od_premmium_a.setText(a_od_premium);
                    tv_add_on_premium_b.setText(b_addOn_Premium);
                    tv_total_deductibles_c.setText(c_total_Deductibles);
                    tv_net_od_premium_d.setText(netOd_premium_a_b_c);
                    tv_net_od_premium_gst_e.setText(e_net_od_premium_gst);
                    tv_net_od_premium_gst_f.setText(f_net_od_Premium_with_gst);

                    til_g_label.setText("(G) "+basic_cngailer_label);
                    tv_basic_tp_g.setText(basic_cngailer_tp);

                    til_h_label.setText("(H) "+basic_cngailer_tp_gst_label);
                    tv_basic_tp_18_h.setText(basic_cngailer_tp_gst);

                    tv_tp_other_i.setText(i_tp_other);
                    tv_tp_other_18_j.setText(j_tp_Other_gst);
                    tv_total_tp_k.setText(k_total_tp_premium);
                    tv_total_premium_l.setText(l_total_tp_premium_with_gst);
                    tv_total_premium_with_gst_18_m.setText(m_total_tp_premium_with_gst);
                    tv_total_total_premium_payable_n.setText(total_premium_payable);

                    // OD BreakUp
                    JSONObject basic_od_htmlObj = breakup_html.getJSONObject("basic_od");
                    if(Str_policy_type.equalsIgnoreCase("renew")) {
                        LayoutOd_Renew.setVisibility(View.VISIBLE);
                        Wb_OD_BreakUp.setVisibility(View.GONE);
                        String basic_od = basic_od_htmlObj.getString("basic_od");
                        String electrical_premium = basic_od_htmlObj.getString("electrical_premium");
                        String non_electrical_premium = basic_od_htmlObj.getString("non_electrical_premium");
                        String geographical_value_od = basic_od_htmlObj.getString("geographical_value_od");
                        String net_od = basic_od_htmlObj.getString("net_od");

                        tv_basic_od_odbp.setText(basic_od);
                        tv_electrical_accessory_premium.setText(electrical_premium);
                        tv_non_electric_accessory.setText(non_electrical_premium);
                        tv_total_geographical_od.setText(geographical_value_od);
                        tv_total_net_od_premium.setText(net_od);


                    }else {

                        if(selected_od_year!=null && !selected_od_year.equalsIgnoreCase("") && selected_od_year.equalsIgnoreCase("5")) {
                            LayoutOd_Renew.setVisibility(View.GONE);
                            Wb_OD_BreakUp.setVisibility(View.VISIBLE);
                            vehicle_idv_yearObj = basic_od_htmlObj.getJSONObject("vehicle_idv_year");
                            year_wiseObj = basic_od_htmlObj.getJSONObject("year_wise");

                            start_date_yearObj = basic_od_htmlObj.getJSONObject("start_date_year");
                            end_date_yearObj = basic_od_htmlObj.getJSONObject("end_date_year");

                            total_basic_od_yrObj = basic_od_htmlObj.getJSONObject("total_basic_od_yr");
                            total_basic_od_year_amount = basic_od_htmlObj.getString("total_basic_od_year_amount");

                            total_electrical_premiumObj = basic_od_htmlObj.getJSONObject("total_electrical_premium");
                            total_electrical_year_amount = basic_od_htmlObj.getString("total_electrical_year_amount");

                            total_non_electrical_premiumObj = basic_od_htmlObj.getJSONObject("total_non_electrical_premium");
                            total_non_electrical_year_amount = basic_od_htmlObj.getString("total_non_electrical_year_amount");

                            total_geographical_value_odObj = basic_od_htmlObj.getJSONObject("total_geographical_value_od");
                            total_geographical_year_amount = basic_od_htmlObj.getString("total_geographical_year_amount");

                            total_bifuel_premiumObj = basic_od_htmlObj.getJSONObject("bifuel_premium");
                            total_bifuel_value = basic_od_htmlObj.getString("total_bifuel_value");


                            total_od_yr_wiseObj = basic_od_htmlObj.getJSONObject("total_od_yr_wise");
                            total_basic_od_premium = basic_od_htmlObj.getString("total_basic_od_premium");

                            Wb_OD_BreakUp.addJavascriptInterface(new WebAppInterfaceOdBreakup(), "Android");
                            Wb_OD_BreakUp.getSettings().setJavaScriptEnabled(true);
                            Wb_OD_BreakUp.loadUrl("file:///android_asset/www/web_od_breakup.html");
                        }else {

                            LayoutOd_Renew.setVisibility(View.VISIBLE);
                            Wb_OD_BreakUp.setVisibility(View.GONE);
                            String basic_od = basic_od_htmlObj.getString("basic_od");
                            String electrical_premium = basic_od_htmlObj.getString("electrical_premium");
                            String non_electrical_premium = basic_od_htmlObj.getString("non_electrical_premium");
                            String geographical_value_od = basic_od_htmlObj.getString("geographical_value_od");
                            String net_od = basic_od_htmlObj.getString("net_od");

                            tv_basic_od_odbp.setText(basic_od);
                            tv_electrical_accessory_premium.setText(electrical_premium);
                            tv_non_electric_accessory.setText(non_electrical_premium);
                            tv_total_geographical_od.setText(geographical_value_od);
                            tv_total_net_od_premium.setText(net_od);

                        }
                    }


                    // Deductibles
                    JSONObject antitheft = breakup_html.getJSONObject("antitheft");
                    if(Str_policy_type.equalsIgnoreCase("renew")) {
                        LayoutDeductibles_Renew.setVisibility(View.VISIBLE);
                        Wb_Deductibles.setVisibility(View.GONE);
                        String total_antitheft_value = antitheft.getString("antitheft_value");
                        String total_automobile_association_value = antitheft.getString("automobile_association");
                        String ncbpercentage = antitheft.getString("ncbpercentage");
                        String total_ncb_value = antitheft.getString("ncb_value");
                        String deductibles_total_year_value = antitheft.getString("total_discount");

                        tv_deduct_anti_theft.setText(total_antitheft_value);
                        tv_automobile_deductible.setText(total_automobile_association_value);
                        til_ncb_percentage.setText("No Claim Bonus ("+ncbpercentage+")%");
                        tv_ncb_deductibles.setText(total_ncb_value);
                        tv_total_discount_deductibles.setText(deductibles_total_year_value);


                    }else {
                        if(selected_od_year!=null && !selected_od_year.equalsIgnoreCase("") && selected_od_year.equalsIgnoreCase("5")) {
                            LayoutDeductibles_Renew.setVisibility(View.GONE);
                            Wb_Deductibles.setVisibility(View.VISIBLE);
                            deductibles_year_wiseObj = antitheft.getJSONObject("deductibles_year_wise");

                            antitheft_value_year_wiseObj = antitheft.getJSONObject("antitheft_value_year_wise");
                            total_antitheft_value = antitheft.getString("total_antitheft_value_year_wise");

                            total_automobile_association_premium_year_wiseObj = antitheft.getJSONObject("automobile_association_premium_year_wise");
                            total_automobile_association_value = antitheft.getString("total_automobile_association_premium_year_wise");

                            ncb_valueObj = antitheft.getJSONObject("ncb_value");
                            total_ncb_value = antitheft.getString("total_ncb_value");

                            deductibles_total_year_wiseObj = antitheft.getJSONObject("deductibles_total_year_wise");
                            deductibles_total_year_value = antitheft.getString("deductibles_total_year_value");


                            Wb_Deductibles.addJavascriptInterface(new WebAppInterfaceDeductibles(), "Android");
                            Wb_Deductibles.getSettings().setJavaScriptEnabled(true);
                            Wb_Deductibles.loadUrl("file:///android_asset/www/web_deductibles.html");
                        }else {
                            LayoutDeductibles_Renew.setVisibility(View.VISIBLE);
                            Wb_Deductibles.setVisibility(View.GONE);
                            String total_antitheft_value = antitheft.getString("antitheft_value");
                            String total_automobile_association_value = antitheft.getString("automobile_association");
                            String ncbpercentage = antitheft.getString("ncbpercentage");
                            String total_ncb_value = antitheft.getString("ncb_value");
                            String deductibles_total_year_value = antitheft.getString("total_discount");

                            tv_deduct_anti_theft.setText(total_antitheft_value);
                            tv_automobile_deductible.setText(total_automobile_association_value);
                            til_ncb_percentage.setText("No Claim Bonus ("+ncbpercentage+")%");
                            tv_ncb_deductibles.setText(total_ncb_value);
                            tv_total_discount_deductibles.setText(deductibles_total_year_value);

                        }
                    }



                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private class WebAppInterfaceOdBreakup{

        @JavascriptInterface
        public String get_vehicle_idv_yearObj() {
            Log.d("vehicle_idv_yearObj", "" + vehicle_idv_yearObj);
            return vehicle_idv_yearObj.toString();
        }

        @JavascriptInterface
        public String get_start_date_yearObj() {
            Log.d("start_date_yearObj", "" + start_date_yearObj);
            return start_date_yearObj.toString();
        }

        @JavascriptInterface
        public String get_end_date_yearObj() {
            Log.d("end_date_yearObj", "" + end_date_yearObj);
            return end_date_yearObj.toString();
        }


        @JavascriptInterface
        public String get_total_basic_od_yrObj() {
            Log.d("total_basic_od_yrObj", "" + total_basic_od_yrObj);
            return total_basic_od_yrObj.toString();
        }

        @JavascriptInterface
        public String get_total_electrical_premiumObj() {
            Log.d("total_elect_premObj", "" + total_electrical_premiumObj);
            return total_electrical_premiumObj.toString();
        }


        @JavascriptInterface
        public String get_total_non_electrical_premiumObj() {
            Log.d("total_non_elect_premObj", "" + total_non_electrical_premiumObj);
            return total_non_electrical_premiumObj.toString();
        }



        @JavascriptInterface
        public String get_total_od_yr_wiseObj() {
            Log.d("total_od_yr_wiseObj", "" + total_od_yr_wiseObj);
            return total_od_yr_wiseObj.toString();
        }

        @JavascriptInterface
        public String get_year_wiseObj() {
            Log.d("year_wiseObj", "" + year_wiseObj);
            return year_wiseObj.toString();
        }

        @JavascriptInterface
        public String get_total_geographical_value_odObj() {
            Log.d("total_geo_value_odObj", "" + total_geographical_value_odObj);
            return total_geographical_value_odObj.toString();
        }

        @JavascriptInterface
        public String get_total_basic_od_year_amount() {
            Log.d("total_basic_od", "" + total_basic_od_year_amount);
            return total_basic_od_year_amount;
        }

        @JavascriptInterface
        public String get_total_electrical_year_amount() {
            Log.d("total_elect_year_amount", "" + total_electrical_year_amount);
            return total_electrical_year_amount;
        }

        @JavascriptInterface
        public String get_total_non_electrical_year_amount() {
            Log.d("total_non_electri", "" + total_non_electrical_year_amount);
            return total_non_electrical_year_amount;
        }

        @JavascriptInterface
        public String get_total_geographical_year_amount() {
            Log.d("total_geog_year_amount", "" + total_geographical_year_amount);
            return total_geographical_year_amount;
        }


        @JavascriptInterface
        public String get_total_basic_od_premium() {
            Log.d("total_basic_od_amount", "" + total_basic_od_premium);
            return total_basic_od_premium;
        }

    }

    private class WebAppInterfaceDeductibles{


        @JavascriptInterface
        public String get_deductibles_year_wiseObj() {
            Log.d("deduct_year_wiseObj", "" + deductibles_year_wiseObj);
            return deductibles_year_wiseObj.toString();
        }



        @JavascriptInterface
        public String get_antitheft_value_year_wiseObj() {
            Log.d("antithe_year_wiseObj", "" + antitheft_value_year_wiseObj);
            return antitheft_value_year_wiseObj.toString();
        }

        @JavascriptInterface
        public String get_total_automobile_association_premium_year_wiseObj() {
            Log.d("automobile_year_wiseObj", "" + total_automobile_association_premium_year_wiseObj);
            return total_automobile_association_premium_year_wiseObj.toString();
        }


        @JavascriptInterface
        public String get_ncb_valueObj() {
            Log.d("ncb_valueObj", "" + ncb_valueObj);
            return ncb_valueObj.toString();
        }

        @JavascriptInterface
        public String get_deductibles_total_year_wiseObj() {
            Log.d("deduct_year_wiseObj", "" + deductibles_total_year_wiseObj);
            return deductibles_total_year_wiseObj.toString();
        }


        @JavascriptInterface
        public String get_total_antitheft_value() {
            Log.d("total_antitheft_value", "" + total_antitheft_value);
            return total_antitheft_value;
        }

        @JavascriptInterface
        public String get_total_automobile_association_value() {
            Log.d("total_automobile_value", "" + total_automobile_association_value);
            return total_automobile_association_value;
        }

        @JavascriptInterface
        public String get_total_ncb_value() {
            Log.d("total_ncb_value", "" + total_ncb_value);
            return total_ncb_value;
        }

        @JavascriptInterface
        public String get_deductibles_total_year_value() {
            Log.d("deduct_total_year_value", "" + deductibles_total_year_value);
            return deductibles_total_year_value;
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
    }
}
