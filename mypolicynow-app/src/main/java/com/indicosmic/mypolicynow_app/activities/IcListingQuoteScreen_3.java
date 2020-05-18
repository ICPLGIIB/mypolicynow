package com.indicosmic.mypolicynow_app.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.utils.CommonMethods;
import com.indicosmic.mypolicynow_app.utils.ConnectionDetector;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.indicosmic.mypolicynow_app.utils.CommonMethods.ucFirst;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_password;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.api_user_name;
import static com.indicosmic.mypolicynow_app.webservices.RestClient.x_api_key;


public class IcListingQuoteScreen_3 extends AppCompatActivity {

    ImageView iv_vehicle_type;
    TextView tv_vehicle_make_model_variant_name,tv_vehicle_id,tv_vehicle_make_id,tv_vehicle_model_id,tv_vehicle_variant_id,
            tv_cc,tv_fuel,tv_rto,tv_rto_id,tv_policy_package_type,tv_policy_start_date,tv_previous_ncb,tv_policy_type,tv_policy_end_date,
            tv_current_ncb,tv_policy_holder_type,tv_claimed_in_past_year,tv_plan_type,tv_previous_policy_type,tv_previous_policy_expiry_date,
            tv_pa_cover_tenure;
    LinearLayout LinearPA_Cover,LinearIsClaimed,LinearPreviousPolicyData,LinearEdit;

    String StrAgentId="",StrMpnData="",StrUserActionData="",StrImageUrl="",StrPlanTypeData="",selected_od_year,ProductTypeId="",StrExShowRoomPrice="";
    String StrMake,StrModel,StrVariant,Str_vehicle_make_model_variant_name="",Str_vehicle_id="",Str_vehicle_make_id="",Str_vehicle_model_id="",Str_vehicle_variant_id="",
            Str_cc="",Str_fuel="",Str_rto="",Str_policy_package_type="",Str_policy_start_date="",Str_previous_ncb="",Str_policy_type="",Str_policy_end_date="",
            Str_current_ncb="",Str_policy_holder_type="",Str_claimed_in_past_year="",Str_plan_type="",Str_previous_policy_type="",Str_previous_policy_expiry_date="",
            Str_pa_cover_tenure="",Str_rto_id="",Str_have_pa_policy="",Str_product_type_id="",Str_vehicle_min_idv="",
            Str_vehicle_max_idv="",Str_total_vehicle_idv="";
    boolean is_breakin;
    ProgressDialog myDialog;
    LinearLayout ll_parent_portfolio;
    Dialog DialogBreakUpDetail;

    //JSONObject  selected_addonObj=null;
    String error_message,vehicle_idv,available_od_discount,total_addon_premium,gross_premium,addon_button_html_message="";
    JSONObject vehicle_idv_yearObj,start_date_yearObj,end_date_yearObj,total_basic_od_yrObj,
            total_electrical_premiumObj,total_non_electrical_premiumObj,
            total_od_yr_wiseObj,total_geographical_value_odObj,year_wiseObj,total_bifuel_premiumObj;
    String total_basic_od_year_amount,total_electrical_year_amount,total_non_electrical_year_amount,total_geographical_year_amount,
            total_basic_od_premium,total_bifuel_value,b_addOn_Premium;

    JSONObject deductibles_year_wiseObj,antitheft_value_year_wiseObj,total_automobile_association_premium_year_wiseObj,
            ncb_valueObj,deductibles_total_year_wiseObj;
    String total_antitheft_value,total_automobile_association_value,total_ncb_value,deductibles_total_year_value;

    JSONObject Addon_headerObj,Addon_valueObj,Addon_totalObj;

    CheckBox CB_Accessories,CB_Deductibles,CB_GeographicalExtension,CB_PACovers;
    public  String[] IcList;

    LinearLayout LayoutIDV,LayoutExtendIdv;
    TextView BreakInTxt,Tv_ExShowroom,TV_MinIdv,TV_MaxIdv;
    ImageView iv_idv,iv_edit_idv;
    EditText EdtIdvValue;
    String StrIdvNewValue="";
    LinearLayout LayoutAccessories_Covers,LayoutExtendAccessories_Covers;
    JSONObject addon_apiObject;
    ImageView iv_accessories_covers;
    CheckBox CB_AddONs;
    Dialog DialogAccessiores,DialogDeductibles,DialogGeographicalExtension,DialogPACovers,DialogAddOns;
    String electrical_val="",non_electrical_val="",cng_value="";
    String aa_membership_name="",aa_membership_no="",aa_expirty_date="";
    DatePickerDialog AA_ExpiryDatePickerDialog;
    private int mYear1, mMonth1, mDay1, mHour1, mMinute1;
    JSONArray AddOnArray = new JSONArray();
    boolean is_antitheft = false,is_automobile_association = false;
    String geographical="";
    String pa_unnamed_persons_no="",pa_unnamed_persons_value="",ll_paid_driver_value="",ll_paid_driver_name="";
    String addons_data="";
    JSONObject selected_addonsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ic_listing_quotation_screen);


        init();
    }

    public void init(){

        myDialog = new ProgressDialog(IcListingQuoteScreen_3.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("SELECT QUOTE");


        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserActionData");
        String ic_list =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"IcList");
        ProductTypeId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ProductTypeId");
        StrExShowRoomPrice = UtilitySharedPreferences.getPrefs(getApplicationContext(),"ExShowroomPrice");

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
        BreakInTxt =  (TextView)findViewById(R.id.BreakInTxt);
        Tv_ExShowroom = (TextView)findViewById(R.id.Tv_ExShowroom);

        if(StrExShowRoomPrice!=null && !StrExShowRoomPrice.equalsIgnoreCase("")){
            Tv_ExShowroom.setText("Ex Showroom : \u20B9 "+StrExShowRoomPrice);
        }
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

            Glide.with(IcListingQuoteScreen_3.this).load(StrImageUrl).into(iv_vehicle_type);



            selected_od_year = user_action_dataObj.getString("selected_od_year");
            if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("") && Str_product_type_id.equalsIgnoreCase("1")) {
                if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("Comprehensive")) {
                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("New")) {
                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 3 year TP";
                        }else if(selected_od_year.equalsIgnoreCase("3")){
                            Str_plan_type = "3 year OD And 3 year TP";
                        }

                    }
                    else if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {
                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 1 year TP";
                        }

                    }
                }
                if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("ThirdParty")) {

                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("New")) {

                        if(selected_od_year.equalsIgnoreCase("0")){
                            Str_plan_type = "0 year OD And 3 year TP";
                        }


                    }
                    else if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {
                        if(selected_od_year.equalsIgnoreCase("0")){
                            Str_plan_type = "0 year OD And 1 year TP";
                        }
                    }


                } else if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("standalone od")) {
                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {

                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 1 year TP";
                        }


                    }
                }
            }
            else  if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("") && Str_product_type_id.equalsIgnoreCase("2")) {
                if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("Comprehensive")) {
                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("New")) {

                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 5 year TP";
                        }else if(selected_od_year.equalsIgnoreCase("5")){
                            Str_plan_type = "5 year OD And 5 year TP";
                        }

                    }
                    else if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {
                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 1 year TP";
                        }

                    }
                }
                if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("ThirdParty")) {

                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("New")) {

                        if(selected_od_year.equalsIgnoreCase("0")){
                            Str_plan_type = "0 year OD And 5 year TP";
                        }


                    }
                    else if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {
                        if(selected_od_year.equalsIgnoreCase("0")){
                            Str_plan_type = "0 year OD And 1 year TP";
                        }

                    }


                } else if (Str_policy_package_type != null && Str_policy_package_type.equalsIgnoreCase("standalone od")) {
                    if (Str_policy_type != null && Str_policy_type.equalsIgnoreCase("Renew")) {

                        if(selected_od_year.equalsIgnoreCase("1")){
                            Str_plan_type = "1 year OD And 1 year TP";
                        }

                    }
                }
            }





            JSONObject mpn_dataObj = new JSONObject(StrMpnData);
            JSONObject policy_start_date_arr = mpn_dataObj.getJSONObject("policy_start_date_arr");
            Str_policy_start_date = policy_start_date_arr.getString("date");

            JSONObject policy_end_date_arr = mpn_dataObj.getJSONObject("policy_end_date_arr");
            Str_policy_end_date = policy_end_date_arr.getString("date");

            Str_current_ncb = mpn_dataObj.getString("current_ncb");

            is_breakin = mpn_dataObj.getBoolean("is_quote_breakin");
            Str_vehicle_min_idv = mpn_dataObj.getString("vehicle_min_idv");
            Str_vehicle_max_idv = mpn_dataObj.getString("vehicle_max_idv");
            Str_total_vehicle_idv = mpn_dataObj.getString("total_vehicle_idv");

            if(Str_product_type_id.equalsIgnoreCase("2")){

                BreakInTxt.setVisibility(View.GONE);
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"isBreakIn",""+false);
            }else if(Str_product_type_id.equalsIgnoreCase("1")){
                if(is_breakin){
                    BreakInTxt.setVisibility(View.VISIBLE);
                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"isBreakIn",""+is_breakin);
                }else {
                    BreakInTxt.setVisibility(View.GONE);
                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"isBreakIn",""+is_breakin);
                }
            }

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




        // Accessories & Covers
        LayoutAccessories_Covers = (LinearLayout)findViewById(R.id.LayoutAccessories_Covers);
        LayoutExtendAccessories_Covers = (LinearLayout)findViewById(R.id.LayoutExtendAccessories_Covers);
        iv_accessories_covers = (ImageView)findViewById(R.id.iv_accessories_covers);
        LayoutAccessories_Covers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LayoutExtendAccessories_Covers.getVisibility()==View.GONE){
                    LayoutExtendAccessories_Covers.setVisibility(View.VISIBLE);
                    iv_accessories_covers.setImageDrawable(getDrawable(R.drawable.ic_arrow_drop_up));

                }else {
                    LayoutExtendAccessories_Covers.setVisibility(View.GONE);
                    iv_accessories_covers.setImageDrawable(getDrawable(R.drawable.ic_arrow_dropdown));
                }
            }
        });

        //IDV Layout
        LayoutIDV= (LinearLayout)findViewById(R.id.LayoutIDV);
        LayoutExtendIdv = (LinearLayout)findViewById(R.id.LayoutExtendIdv);
        iv_idv = (ImageView)findViewById(R.id.iv_idv);

        LayoutIDV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LayoutExtendIdv.getVisibility()==View.GONE){
                    LayoutExtendIdv.setVisibility(View.VISIBLE);
                    iv_idv.setImageDrawable(getDrawable(R.drawable.ic_arrow_drop_up));

                }else {
                    LayoutExtendIdv.setVisibility(View.GONE);
                    iv_idv.setImageDrawable(getDrawable(R.drawable.ic_arrow_dropdown));
                }
            }
        });


        TV_MinIdv= (TextView) findViewById(R.id.TV_MinIdv);
        TV_MaxIdv= (TextView)findViewById(R.id.TV_MaxIdv);
        iv_edit_idv= (ImageView) findViewById(R.id.iv_edit_idv);


        EdtIdvValue= (EditText)findViewById(R.id.EdtIdvValue);
        EdtIdvValue.setEnabled(false);

        if(Str_vehicle_min_idv!=null && !Str_vehicle_min_idv.equalsIgnoreCase("")) {
            TV_MinIdv.setText("Min: " + Str_vehicle_min_idv);
        }

        if(Str_vehicle_max_idv!=null && !Str_vehicle_max_idv.equalsIgnoreCase("")) {
            TV_MaxIdv.setText("Max: " + Str_vehicle_max_idv);
        }

        if(Str_total_vehicle_idv!=null && !Str_total_vehicle_idv.equalsIgnoreCase("")) {
            EdtIdvValue.setText(Str_total_vehicle_idv);
        }

        iv_edit_idv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Str_policy_type.equalsIgnoreCase("renew")) {
                    EdtIdvValue.setEnabled(true);
                    EdtIdvValue.requestFocus();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Cannot Edit IDV for New Vehicle");
                    EdtIdvValue.setEnabled(false);
                }

            }
        });

        EdtIdvValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean result = false;
                if(EdtIdvValue.getText().toString().length()>0){
                    StrIdvNewValue = EdtIdvValue.getText().toString();
                    double new_idv_value = Double.parseDouble(StrIdvNewValue);
                    double min_idv_value = Double.parseDouble(Str_vehicle_min_idv);
                    double max_idv_value = Double.parseDouble(Str_vehicle_max_idv);
                    if(new_idv_value < min_idv_value || new_idv_value > max_idv_value){
                        EdtIdvValue.setError("Min: "+Str_vehicle_min_idv +"& Max: "+Str_vehicle_max_idv);
                        //CommonMethods.DisplayToastInfo(getApplicationContext(),"New IDV cannot less than "+Str_vehicle_min_idv +" \n& cannot be more than "+Str_vehicle_max_idv);
                        result = false;
                    }else {
                        StrIdvNewValue = EdtIdvValue.getText().toString();
                        API_UPDATE_QUOTE("change_idv","");
                        result = true;
                    }
                }
                return result;

            }
        });

        //Accessories & Covers


        CB_Accessories = (CheckBox)findViewById(R.id.CB_Accessories);
        CB_Deductibles = (CheckBox)findViewById(R.id.CB_Deductibles);
        CB_GeographicalExtension = (CheckBox)findViewById(R.id.CB_GeographicalExtension);
        CB_PACovers = (CheckBox)findViewById(R.id.CB_PACovers);


        setValuesToAccesories();

        if(Str_policy_type!=null && Str_policy_type.equalsIgnoreCase("new")){
            CB_GeographicalExtension.setVisibility(View.GONE);
            CB_GeographicalExtension.setChecked(false);
        }else if (Str_policy_type!=null && Str_policy_type.equalsIgnoreCase("renew")) {
            CB_GeographicalExtension.setVisibility(View.VISIBLE);
            CB_GeographicalExtension.setChecked(false);
        }


        CB_Accessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup_Accessories();
            }
        });

        CB_Deductibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup_Deductibles();
            }
        });

        CB_GeographicalExtension.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup_GeographicalExtension();
            }
        });

        CB_PACovers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowPopup_PACovers();
            }
        });


        if(ll_parent_portfolio!=null && ll_parent_portfolio.getChildCount()>0){
            ll_parent_portfolio.removeAllViews();
        }
        //IcList = new String[] {"3","6"};
        for(int k=0; k< IcList.length; k++){
            String ic_id = IcList[k];
            LoadQuotation(ic_id);
        }

    }

    private void setValuesToAccesories() {
        if(StrMpnData!=null && !StrMpnData.equalsIgnoreCase("")){

            try{
                JSONObject mpn_dataObj = new JSONObject(StrMpnData);

                if(StrMpnData.contains("accessories")) {
                    Object accessories = mpn_dataObj.get("accessories");

                    if (accessories instanceof JSONArray) {
                        // It's an array
                        JSONArray interventionJsonArray = (JSONArray) accessories;
                        if (interventionJsonArray.length() == 0) {
                            CB_Accessories.setChecked(false);
                        }
                    } else if (accessories instanceof JSONObject) {
                        // It's an object
                        JSONObject interventionObject = (JSONObject) accessories;
                        CB_Accessories.setChecked(true);
                        electrical_val = interventionObject.getString("elec_value");
                        non_electrical_val = interventionObject.getString("non_elec_value");

                    }
                }else {

                    CB_Accessories.setChecked(false);
                    electrical_val = "";
                    non_electrical_val = "";
                }

                if(StrMpnData.contains("geographical_extention")) {
                    JSONObject geographical_extention_Obj = mpn_dataObj.getJSONObject("geographical_extention");
                    String country_selected = geographical_extention_Obj.getString("country_selected");
                    if (country_selected != null && !country_selected.equalsIgnoreCase("")) {
                        CB_GeographicalExtension.setChecked(true);
                        geographical = country_selected;
                    } else {
                        CB_GeographicalExtension.setChecked(false);
                        geographical = "";
                    }
                }else {
                    CB_GeographicalExtension.setChecked(false);
                    geographical = "";
                }

                if(StrMpnData.contains("deductibles")){
                    JSONObject deductibles_Obj = mpn_dataObj.getJSONObject("deductibles");
                    is_antitheft = deductibles_Obj.getBoolean("anti_theft");

                    if(StrMpnData.contains("automobile_association")) {
                        is_automobile_association = deductibles_Obj.getBoolean("automobile_association");
                    }else {
                        is_automobile_association = false;
                    }
                }else {
                    is_antitheft = false;
                    is_automobile_association = false;
                }

                if(is_antitheft || is_automobile_association){
                    CB_Deductibles.setChecked(true);
                }else {
                    CB_Deductibles.setChecked(false);
                }

                //Condition for car aa_member_name, to set here
                if(StrMpnData.contains("pa_covers")) {
                    Object pa_covers = mpn_dataObj.get("pa_covers");

                    if (pa_covers instanceof JSONArray) {
                        // It's an array
                        JSONArray pa_coversJsonArray = (JSONArray) pa_covers;
                        if (pa_coversJsonArray.length() == 0) {
                            CB_PACovers.setChecked(false);
                        }
                    } else if (pa_covers instanceof JSONObject) {
                        // It's an object
                        JSONObject pa_coversObject = (JSONObject) pa_covers;
                        CB_Accessories.setChecked(true);
                        pa_unnamed_persons_no = pa_coversObject.getString("pa_unnamed_persons_no");
                        pa_unnamed_persons_value = pa_coversObject.getString("pa_unnamed_persons_value");

                        if (pa_unnamed_persons_no != null && !pa_unnamed_persons_no.equalsIgnoreCase("")) {
                            pa_unnamed_persons_no = "on";
                        }
                    }
                }else {
                    CB_PACovers.setChecked(false);
                    pa_unnamed_persons_no = "";
                    pa_unnamed_persons_value = "";
                }

                /*if(StrMpnData!=null && StrMpnData.contains("addons")) {
                    selected_addonsObj = mpn_dataObj.getJSONObject("addons");
                }*/





            }catch (Exception e){
                e.printStackTrace();
            }

        }



    }

    private void ShowPopup_Accessories() {

        DialogAccessiores = new Dialog(IcListingQuoteScreen_3.this);
        DialogAccessiores.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAccessiores.setCanceledOnTouchOutside(false);
        DialogAccessiores.setCancelable(false);
        DialogAccessiores.setContentView(R.layout.layout_dailog_accessiores);
        DialogAccessiores.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogAccessiores.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogAccessiores.findViewById(R.id.iv_close);


        EditText EdtElectrical = (EditText)DialogAccessiores.findViewById(R.id.EdtElectrical);
        EditText EdtNonElectrical = (EditText)DialogAccessiores.findViewById(R.id.EdtNonElectrical);

        LinearLayout Cng_LpgLayout = (LinearLayout)DialogAccessiores.findViewById(R.id.Cng_LpgLayout);
        EditText EdtCNG_LPG = (EditText)DialogAccessiores.findViewById(R.id.EdtCNG_LPG);

        Button Btn_submit = (Button)DialogAccessiores.findViewById(R.id.Btn_submit);
        Button Btn_reset = (Button)DialogAccessiores.findViewById(R.id.Btn_reset);

        if(electrical_val!=null && !electrical_val.equalsIgnoreCase("") && !electrical_val.equalsIgnoreCase("null")){
            EdtElectrical.setText(electrical_val);
        }

        if(non_electrical_val!=null && !non_electrical_val.equalsIgnoreCase("") && !non_electrical_val.equalsIgnoreCase("null")){
            EdtNonElectrical.setText(non_electrical_val);
        }
        if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
            if (Str_product_type_id.equalsIgnoreCase("1")){
                Cng_LpgLayout.setVisibility(View.VISIBLE);

                if(cng_value!=null && !cng_value.equalsIgnoreCase("") && !cng_value.equalsIgnoreCase("null")){
                    EdtCNG_LPG.setText(cng_value);
                }else {
                    EdtCNG_LPG.setText("");
                }

            }else if(Str_product_type_id.equalsIgnoreCase("2")){
                Cng_LpgLayout.setVisibility(View.GONE);

            }
        }

        DialogAccessiores.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogAccessiores!=null && DialogAccessiores.isShowing()) {
                    DialogAccessiores.dismiss();
                }

                setValuesToAccesories();

            }
        });

        Btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EdtElectrical.setText("");
                EdtNonElectrical.setText("");
                EdtCNG_LPG.setText("");
                electrical_val = "";
                non_electrical_val = "";
                cng_value = "";
                Cng_LpgLayout.setVisibility(View.GONE);

                API_UPDATE_QUOTE("accessories","");

            }
        });


        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                electrical_val = EdtElectrical.getText().toString();
                non_electrical_val = EdtNonElectrical.getText().toString();
                cng_value = EdtCNG_LPG.getText().toString();

                if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
                    if (Str_product_type_id.equalsIgnoreCase("1")){
                        if(electrical_val!=null && non_electrical_val!=null && cng_value!=null){

                            if(electrical_val.equalsIgnoreCase("") && non_electrical_val.equalsIgnoreCase("") && cng_value.equalsIgnoreCase("")){
                                CommonMethods.DisplayToastError(getApplicationContext(),"Please Enter Valid Value.");
                            }else {

                                API_UPDATE_QUOTE("accessories","");

                            }


                        }

                    }else if(Str_product_type_id.equalsIgnoreCase("2")){
                        if(electrical_val!=null && non_electrical_val!=null){

                            if(electrical_val.equalsIgnoreCase("") && non_electrical_val.equalsIgnoreCase("")){
                                CommonMethods.DisplayToastError(getApplicationContext(),"Please Enter Valid Value.");
                            }else {

                                API_UPDATE_QUOTE("accessories","");

                            }

                        }
                    }
                }

            }
        });


    }

    private void ShowPopup_Deductibles() {

        DialogDeductibles = new Dialog(IcListingQuoteScreen_3.this);
        DialogDeductibles.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogDeductibles.setCanceledOnTouchOutside(true);
        DialogDeductibles.setCancelable(true);
        DialogDeductibles.setContentView(R.layout.layout_dailog_deductibles);
        DialogDeductibles.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogDeductibles.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogDeductibles.findViewById(R.id.iv_close);


        CheckBox CB_Antitheft = (CheckBox)DialogDeductibles.findViewById(R.id.CB_Antitheft);
        CheckBox CB_AutomobileAssociation = (CheckBox)DialogDeductibles.findViewById(R.id.CB_AutomobileAssociation);
        LinearLayout LayoutAA_MemberDetails = (LinearLayout)DialogDeductibles.findViewById(R.id.LayoutAA_MemberDetails);
        EditText EdtAA_MembershipName = (EditText)DialogDeductibles.findViewById(R.id.EdtAA_MembershipName);
        EditText EdtAA_MembershipNo = (EditText)DialogDeductibles.findViewById(R.id.EdtAA_MembershipNo);
        EditText EdtAA_ExpiryDate = (EditText)DialogDeductibles.findViewById(R.id.EdtAA_ExpiryDate);

        EdtAA_ExpiryDate.setInputType(InputType.TYPE_NULL);
        Calendar newCalendar = Calendar.getInstance();
        //newCalendar.add(Calendar.YEAR, -10);
        EdtAA_ExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AA_ExpiryDatePickerDialog = new DatePickerDialog(IcListingQuoteScreen_3.this,
                        android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear1 = year;
                        mMonth1 = monthOfYear;
                        mDay1 = dayOfMonth;


                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                        String ExpiryDate =year +"-"+ (monthOfYear + 1) + "-" + dayOfMonth ;
                        Date date = null;
                        try {
                            date = inputFormat.parse(ExpiryDate);
                            aa_expirty_date = outputFormat.format(date);
                            EdtAA_ExpiryDate.setText(aa_expirty_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, mYear1, mMonth1+1, -1);

                AA_ExpiryDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                AA_ExpiryDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
                AA_ExpiryDatePickerDialog.show();
            }
        });


        Button Btn_submit = (Button)DialogDeductibles.findViewById(R.id.Btn_submit);
        Button Btn_reset = (Button)DialogDeductibles.findViewById(R.id.Btn_reset);

        if(is_antitheft){
            CB_Antitheft.setChecked(is_antitheft);
        }



        if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
            if (Str_product_type_id.equalsIgnoreCase("1")){
                CB_AutomobileAssociation.setVisibility(View.VISIBLE);
                if(is_automobile_association){
                    CB_AutomobileAssociation.setChecked(is_automobile_association);
                    LayoutAA_MemberDetails.setVisibility(View.VISIBLE);

                }else {
                    CB_AutomobileAssociation.setChecked(is_automobile_association);
                }
            }else if(Str_product_type_id.equalsIgnoreCase("2")){

                CB_AutomobileAssociation.setVisibility(View.GONE);
            }
        }



        if(CB_AutomobileAssociation.getVisibility()==View.VISIBLE){

            CB_AutomobileAssociation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    if(compoundButton.isChecked()){
                        LayoutAA_MemberDetails.setVisibility(View.VISIBLE);

                        if(aa_membership_name!=null && !aa_membership_name.equalsIgnoreCase("") && !aa_membership_name.equalsIgnoreCase("null")){
                            EdtAA_MembershipName.setText(aa_membership_name);
                        }else {
                            EdtAA_MembershipName.setText("");

                        }

                        if(aa_membership_no!=null && !aa_membership_no.equalsIgnoreCase("") && !aa_membership_no.equalsIgnoreCase("null")){
                            EdtAA_MembershipNo.setText(aa_membership_no);
                        }else {
                            EdtAA_MembershipNo.setText("");

                        }

                        if(aa_expirty_date!=null && !aa_expirty_date.equalsIgnoreCase("") && !aa_expirty_date.equalsIgnoreCase("null")){
                            EdtAA_ExpiryDate.setText(aa_expirty_date);
                        }else {
                            EdtAA_ExpiryDate.setText("");
                        }
                    }else {
                        LayoutAA_MemberDetails.setVisibility(View.GONE);


                        EdtAA_MembershipName.setText("");
                        EdtAA_MembershipNo.setText("");
                        EdtAA_ExpiryDate.setText("");
                    }

                }
            });

        }


        Btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CB_Antitheft.setChecked(false);
                CB_AutomobileAssociation.setChecked(false);
                LayoutAA_MemberDetails.setVisibility(View.GONE);
                EdtAA_MembershipName.setText("");
                EdtAA_MembershipNo.setText("");
                EdtAA_ExpiryDate.setText("");
                aa_membership_name = "";
                aa_membership_no = "";
                aa_expirty_date = "";
                is_antitheft = false;
                is_automobile_association = false;
                API_UPDATE_QUOTE("deductibles","");
                if(CB_Deductibles.isChecked()){
                    CB_Deductibles.setChecked(false);
                }

            }
        });




        DialogDeductibles.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogDeductibles!=null && DialogDeductibles.isShowing()) {
                    DialogDeductibles.dismiss();
                }
                setValuesToAccesories();

            }
        });

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_antitheft = CB_Antitheft.isChecked();


                if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
                    if (Str_product_type_id.equalsIgnoreCase("1")){

                        is_automobile_association = CB_AutomobileAssociation.isChecked();

                        if(is_automobile_association && LayoutAA_MemberDetails.getVisibility()==View.VISIBLE){

                            aa_membership_name = EdtAA_MembershipName.getText().toString();
                            aa_membership_no = EdtAA_MembershipNo.getText().toString();
                            aa_expirty_date = EdtAA_ExpiryDate.getText().toString();


                        }else {

                            aa_membership_name = "";
                            aa_membership_no = "";
                            aa_expirty_date = "";


                        }

                        if(!is_antitheft && !is_automobile_association){
                            CommonMethods.DisplayToastError(getApplicationContext(),"Please Check Atleast One Field.");
                        }else {
                            API_UPDATE_QUOTE("deductibles","");
                        }



                    }else if(Str_product_type_id.equalsIgnoreCase("2")){
                        if(!is_antitheft){
                            CommonMethods.DisplayToastError(getApplicationContext(),"Please Check Antitheft.");
                        }else {
                            API_UPDATE_QUOTE("deductibles","");
                        }
                    }
                }


            }
        });


    }

    private void ShowPopup_GeographicalExtension() {

        DialogGeographicalExtension = new Dialog(IcListingQuoteScreen_3.this);
        DialogGeographicalExtension.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogGeographicalExtension.setCanceledOnTouchOutside(true);
        DialogGeographicalExtension.setCancelable(true);
        DialogGeographicalExtension.setContentView(R.layout.layout_dailog_geographical_extension);
        DialogGeographicalExtension.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogGeographicalExtension.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogGeographicalExtension.findViewById(R.id.iv_close);

        Spinner Spn_GeographicalExtension = (Spinner)DialogGeographicalExtension.findViewById(R.id.Spn_GeographicalExtension);

        Button Btn_submit = (Button)DialogGeographicalExtension.findViewById(R.id.Btn_submit);
        Button Btn_reset = (Button)DialogGeographicalExtension.findViewById(R.id.Btn_reset);

        if(geographical!=null && !geographical.equalsIgnoreCase("") && !geographical.equalsIgnoreCase("null")){

            int index = Integer.valueOf(geographical)-1;
            Spn_GeographicalExtension.setSelection(index);
        }

        DialogGeographicalExtension.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogGeographicalExtension!=null && DialogGeographicalExtension.isShowing()) {
                    DialogGeographicalExtension.dismiss();
                }
                setValuesToAccesories();

            }
        });

        Btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spn_GeographicalExtension.setSelection(0);
                geographical = "";
                API_UPDATE_QUOTE("geographical_extention","");
                if(CB_GeographicalExtension.isChecked()){
                    CB_GeographicalExtension.setChecked(false);
                }
            }
        });

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Spn_GeographicalExtension.getSelectedItemPosition()==0){
                    geographical = "";
                    CommonMethods.DisplayToastError(getApplicationContext(),"Please Select Atleast One Extension.");
                }else {
                    geographical = Spn_GeographicalExtension.getSelectedItem().toString().trim().toLowerCase();
                    geographical = geographical.replace(" ","");
                    int value_selected = Spn_GeographicalExtension.getSelectedItemPosition();
                    geographical = String.valueOf(value_selected+1);
                    API_UPDATE_QUOTE("geographical_extention","");

                }
            }
        });


    }

    private void ShowPopup_PACovers() {

        DialogPACovers = new Dialog(IcListingQuoteScreen_3.this);
        DialogPACovers.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogPACovers.setCanceledOnTouchOutside(true);
        DialogPACovers.setCancelable(true);
        DialogPACovers.setContentView(R.layout.layout_dailog_pa_covers);
        DialogPACovers.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogPACovers.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogPACovers.findViewById(R.id.iv_close);

        CheckBox CB_PA_UnnamedPerson = (CheckBox)DialogPACovers.findViewById(R.id.CB_PA_UnnamedPerson);
        LinearLayout LayoutPA_SumAssured = (LinearLayout)DialogPACovers.findViewById(R.id.LayoutPA_SumAssured);
        Spinner Spn_Pa_SumAssured = (Spinner)DialogPACovers.findViewById(R.id.Spn_Pa_SumAssured);


        CheckBox CB_LLPaidDriver = (CheckBox)DialogPACovers.findViewById(R.id.CB_LLPaidDriver);
        LinearLayout LayoutLLPaidDriver = (LinearLayout)DialogPACovers.findViewById(R.id.LayoutLLPaidDriver);

        EditText EdtLLPaidDriverName = (EditText)DialogPACovers.findViewById(R.id.EdtLLPaidDriverName);

        Button Btn_submit = (Button)DialogPACovers.findViewById(R.id.Btn_submit);
        Button Btn_reset = (Button)DialogPACovers.findViewById(R.id.Btn_reset);




        if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
            if (Str_product_type_id.equalsIgnoreCase("1")){

                CB_PA_UnnamedPerson.setText("PA Unnamed Persons (5)");
                CB_LLPaidDriver.setVisibility(View.VISIBLE);
                LayoutLLPaidDriver.setVisibility(View.GONE);
                if(ll_paid_driver_value!=null && !ll_paid_driver_value.equalsIgnoreCase("") && !ll_paid_driver_value.equalsIgnoreCase("null")){
                    if(ll_paid_driver_value.equalsIgnoreCase("on")){
                        CB_LLPaidDriver.setChecked(true);
                        LayoutLLPaidDriver.setVisibility(View.VISIBLE);
                        if(ll_paid_driver_name!=null && !ll_paid_driver_name.equalsIgnoreCase("") && !ll_paid_driver_name.equalsIgnoreCase("null")){
                            EdtLLPaidDriverName.setText(ll_paid_driver_name);
                        }else {
                            EdtLLPaidDriverName.setText("");
                        }
                    }else {
                        CB_LLPaidDriver.setChecked(false);
                    }
                }

            }else if(Str_product_type_id.equalsIgnoreCase("2")){
                CB_PA_UnnamedPerson.setText("PA Unnamed Persons (2)");
                CB_LLPaidDriver.setVisibility(View.GONE);
                LayoutLLPaidDriver.setVisibility(View.GONE);
                ll_paid_driver_value="";
                ll_paid_driver_name = "";
                EdtLLPaidDriverName.setText("");
            }
        }

        if(pa_unnamed_persons_no!=null && !pa_unnamed_persons_no.equalsIgnoreCase("") && !pa_unnamed_persons_no.equalsIgnoreCase("null")){
            if(pa_unnamed_persons_no.equalsIgnoreCase("on")){
                CB_PA_UnnamedPerson.setChecked(true);
                LayoutPA_SumAssured.setVisibility(View.VISIBLE);
                if(pa_unnamed_persons_value!=null && !pa_unnamed_persons_value.equalsIgnoreCase("") && !pa_unnamed_persons_value.equalsIgnoreCase("null")){
                    int i = getIndex(Spn_Pa_SumAssured,pa_unnamed_persons_value);
                    Spn_Pa_SumAssured.setSelection(i);
                }
            }else {
                CB_PA_UnnamedPerson.setChecked(false);
            }
        }



        CB_PA_UnnamedPerson.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    LayoutPA_SumAssured.setVisibility(View.VISIBLE);
                }else {
                    LayoutPA_SumAssured.setVisibility(View.GONE);
                }
            }
        });


        CB_LLPaidDriver.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    LayoutLLPaidDriver.setVisibility(View.VISIBLE);
                    EdtLLPaidDriverName.setText("");

                }else {
                    LayoutLLPaidDriver.setVisibility(View.GONE);
                    EdtLLPaidDriverName.setText("");
                }
            }
        });


        Btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pa_unnamed_persons_no = "";
                pa_unnamed_persons_value = "";
                ll_paid_driver_value = "";
                ll_paid_driver_name = "";
                API_UPDATE_QUOTE("pa_covers","");
                if(CB_PACovers.isChecked()){
                    CB_PACovers.setChecked(false);
                }
            }
        });


        DialogPACovers.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogPACovers!=null && DialogPACovers.isShowing()) {
                    DialogPACovers.dismiss();
                }
                setValuesToAccesories();

            }
        });

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cb_pa_unnamedPersonChecked = CB_PA_UnnamedPerson.isChecked();


                if(Str_product_type_id!=null && !Str_product_type_id.equalsIgnoreCase("")) {
                    if (Str_product_type_id.equalsIgnoreCase("1")){
                        boolean cb_ll_paidDriverChecked = CB_LLPaidDriver.isChecked();

                        if(cb_pa_unnamedPersonChecked){
                            pa_unnamed_persons_no = "on";
                            if(Spn_Pa_SumAssured.getSelectedItemPosition()==0){
                                pa_unnamed_persons_value = "";
                                CommonMethods.DisplayToastError(getApplicationContext(),"Please Select PA Unnamed Sum Insured");
                            }else {
                                pa_unnamed_persons_value = Spn_Pa_SumAssured.getSelectedItem().toString().trim();
                                API_UPDATE_QUOTE("pa_covers","");
                            }
                        }else {
                            pa_unnamed_persons_no = "";
                            pa_unnamed_persons_value = "";
                            CommonMethods.DisplayToastError(getApplicationContext(),"Please Select PA Unnamed Person No");
                        }


                        if(cb_ll_paidDriverChecked){
                            ll_paid_driver_value = "on";
                            if(EdtLLPaidDriverName.getText().toString().length()==0){
                                ll_paid_driver_name = "";
                                API_UPDATE_QUOTE("pa_covers","");
                            }else {
                                ll_paid_driver_name = EdtLLPaidDriverName.getText().toString().trim();
                                API_UPDATE_QUOTE("pa_covers","");
                            }
                        }else {
                            ll_paid_driver_value = "";
                            ll_paid_driver_name = "";
                            API_UPDATE_QUOTE("pa_covers","");
                        }


                    }else if(Str_product_type_id.equalsIgnoreCase("2")){
                        if(cb_pa_unnamedPersonChecked){
                            pa_unnamed_persons_no = "on";
                            if(Spn_Pa_SumAssured.getSelectedItemPosition()==0){
                                pa_unnamed_persons_value = "";
                                CommonMethods.DisplayToastError(getApplicationContext(),"Please Select PA Unnamed Sum Insured");
                            }else {
                                pa_unnamed_persons_value = Spn_Pa_SumAssured.getSelectedItem().toString().trim();
                                API_UPDATE_QUOTE("pa_covers","");
                            }
                        }else {
                            pa_unnamed_persons_no = "";
                            pa_unnamed_persons_value = "";
                            CommonMethods.DisplayToastError(getApplicationContext(),"Please Select PA Unnamed Person No");
                        }
                    }
                }
            }
        });


    }

    private void ShowPopUpAddOns(String strAddon_apiObject,String strSelectedAddOn_Obj, String ic_name,String ic_id) {

        Log.d("selected_addonsObj",""+strSelectedAddOn_Obj);

        DialogAddOns = new Dialog(IcListingQuoteScreen_3.this);
        DialogAddOns.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddOns.setCanceledOnTouchOutside(true);
        DialogAddOns.setCancelable(true);
        DialogAddOns.setContentView(R.layout.layout_dailog_add_ons);
        DialogAddOns.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogAddOns.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogAddOns.findViewById(R.id.iv_close);

        TextView tv_ic_id = (TextView)DialogAddOns.findViewById(R.id.tv_ic_id) ;
        TextView tv_ic_name = (TextView)DialogAddOns.findViewById(R.id.tv_ic_name) ;

        tv_ic_name.setText(ic_name);
        tv_ic_id.setText(ic_id);

        Button Btn_submit = (Button)DialogAddOns.findViewById(R.id.Btn_submit);
        Button Btn_reset = (Button)DialogAddOns.findViewById(R.id.Btn_reset);


        LinearLayout ll_parent_addons = (LinearLayout)DialogAddOns.findViewById(R.id.ll_parent_addons);

        if(strAddon_apiObject!=null && !strAddon_apiObject.equalsIgnoreCase("")) {
            try {
                addon_apiObject = new JSONObject(strAddon_apiObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (addon_apiObject != null) {
                Log.d("addon_apiObject", "" + addon_apiObject.toString());

                Iterator keys = addon_apiObject.keys();
                int k = 0;
                while (keys.hasNext()) {

                    // Loop to get the dynamic key
                    String currentDynamicKey = (String) keys.next();

                    Log.d("currentDynamicKey", "" + currentDynamicKey);
                    // Get the value of the dynamic key
                    try {
                        JSONObject addObj = addon_apiObject.getJSONObject(String.valueOf(currentDynamicKey));
                        String addon_id = addObj.getString("addon_id");
                        String amount = addObj.getString("amount");
                        String name = addObj.getString("name");
                        String addon_description = addObj.getString("addon_description");

                        CB_AddONs = new CheckBox(getApplicationContext());
                        CB_AddONs.setText(addon_description);
                        CB_AddONs.setTextColor(getResources().getColor(R.color.black));
                        CB_AddONs.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
                        CB_AddONs.setHint(addObj.toString());
                        CB_AddONs.setId(k);

                        try {
                            selected_addonsObj = new JSONObject(strSelectedAddOn_Obj);
                            Log.d("Selected AddoNs", "" + selected_addonsObj.toString());

                            Iterator keys1 = selected_addonsObj.keys();
                            int m1 = 0;
                            while (keys1.hasNext()) {

                                // Loop to get the dynamic key
                                String currentDynamicKey1 = (String) keys1.next();

                                Log.d("currentDynamicKey1", "" + currentDynamicKey1);
                                // Get the value of the dynamic key

                                if(currentDynamicKey1.equalsIgnoreCase("addons_sum") ||
                                        currentDynamicKey1.equalsIgnoreCase("addons_sum_1year") ||
                                        currentDynamicKey1.equalsIgnoreCase("addons_sum_2year") ||
                                        currentDynamicKey1.equalsIgnoreCase("addons_sum_3year") ||
                                        currentDynamicKey1.equalsIgnoreCase("addon_premium")) {

                                    m1++;
                                }else {
                                    if(String.valueOf(selected_addonsObj).contains("addon_name")) {
                                        JSONObject strselected_addonsObj = selected_addonsObj.getJSONObject(currentDynamicKey1);
                                        String selected_addon_id = strselected_addonsObj.getString("addon_id");

                                        String Cb_HintTxt = CB_AddONs.getHint().toString();

                                        JSONObject hintObj = new JSONObject(Cb_HintTxt);
                                        Log.d("hintObj", "" + hintObj);
                                        if (hintObj.getString("addon_id").equalsIgnoreCase(selected_addon_id)) {
                                            CB_AddONs.setChecked(true);
                                        }
                                        m1++;
                                    }
                                }
                            }
                               /* //if (selected_addon_id.equalsIgnoreCase(addon_id)) {
                                    Log.d("CB_AddONs.getText()", "" + CB_AddONs.getText().toString());
                                    if (CB_AddONs.getText().toString().equalsIgnoreCase(selected_addon_description)) {
                                        CB_AddONs.setChecked(true);
                                    }

                                //}*/




                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        ll_parent_addons.addView(CB_AddONs);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    k++;
                }


            }

        }
       /* if(CB_AddONs!=null){
            CB_AddONs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(compoundButton.isChecked()){
                        CommonMethods.DisplayToastInfo(getApplicationContext(),CB_AddONs.getHint().toString());
                    }
                }
            });
        }*/




        Btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addons_data = "";
                AddOnArray = new JSONArray();
                API_UPDATE_QUOTE("addons",tv_ic_id.getText().toString());
            }
        });


        DialogAddOns.show();
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DialogAddOns!=null && DialogAddOns.isShowing()) {
                    DialogAddOns.dismiss();
                }

                setValuesToAccesories();
            }
        });

        Btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ll_parent_addons.getChildCount()>0){
                    for(int i =0; i< ll_parent_addons.getChildCount();i++){
                        View view1 = ll_parent_addons.getChildAt(i);
                        CheckBox Chk_AddOns = (CheckBox)view1.findViewById(i);

                        if(Chk_AddOns.isChecked()) {
                            String AddOnData = Chk_AddOns.getHint().toString();
                            try {
                                JSONObject addObj = new JSONObject(AddOnData);

                                selected_addonsObj = addObj;
                                AddOnArray.put(selected_addonsObj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        Log.d("selectedAddons",""+selected_addonsObj);

                    }
                    API_UPDATE_QUOTE("addons",tv_ic_id.getText().toString());

                }
            }
        });



    }

    private int getIndex(Spinner spinner, String searchString) {

        if (searchString == null || spinner.getCount() == 0) {

            return -1; // Not found

        } else {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                    return i; // Found!
                }else if(spinner.getItemAtPosition(i).toString().replace(" ","").equalsIgnoreCase(searchString)){
                    return i; // Found!
                }
            }

            return -1; // Not found
        }
    }

    private void API_UPDATE_QUOTE(String Type,String Ic_Id) {

       /* if(Type!=null && Type.equalsIgnoreCase("accessories")){

        }else if(Type!=null && Type.equalsIgnoreCase("deductibles")){

        }else if(Type!=null && Type.equalsIgnoreCase("geographical_extention")){

        }else if(Type!=null && Type.equalsIgnoreCase("pa_covers")){

        }else*/
        if(Type!=null && Type.equalsIgnoreCase("addons")){
            if(AddOnArray!=null && AddOnArray.length()>0) {
                try {
                    JSONObject mpnObj = new JSONObject(StrMpnData);
                    JSONObject dep_obj = new JSONObject();
                    dep_obj.put(Ic_Id, 1);
                    Log.d("dep_obj",dep_obj.toString());
                    JSONArray addonArray = new JSONArray();
                    JSONArray addons_obj = new JSONArray();
                    JSONObject list_obj = new JSONObject();
                    for (int k = 0; k < AddOnArray.length(); k++) {

                        JSONObject selected_addonObj = AddOnArray.getJSONObject(k);
                        String addon_id = selected_addonObj.getString("addon_id");


                        //addons_obj = new JSONObject();

                        list_obj = new JSONObject();
                        list_obj.put(addon_id,selected_addonObj);

                        addonArray.put(list_obj);

                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(Ic_Id, addonArray);

                    addons_obj.put(jsonObject);
                    Log.d("addons_obj",addons_obj.toString());

                    //Log.d("addons",""+addonArray);
                    mpnObj.put("dep",dep_obj);
                    mpnObj.put("addons",addons_obj);

                    StrMpnData = mpnObj.toString();

                } catch(JSONException e){
                    e.printStackTrace();
                }

                addons_data = AddOnArray.toString();
            }else {
                addons_data = "";
                selected_addonsObj= new JSONObject();
            }
            Log.d("AddonData",addons_data);

        }/*else if(Type!=null && Type.equalsIgnoreCase("change_idv")){

        }*/

        myDialog.show();
        String URL = ROOT_URL2+"update_quote";
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

                        StrMpnData = mpn_dataObj.toString();
                        StrUserActionData = user_action_dataObj.toString();


                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"MpnData",StrMpnData);
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserActionData",StrUserActionData);
                        setValuesToAccesories();
                        if(Type!=null && Type.equalsIgnoreCase("accessories")){
                            if(DialogAccessiores!=null && DialogAccessiores.isShowing()) {
                                DialogAccessiores.dismiss();
                            }
                        }else if(Type!=null && Type.equalsIgnoreCase("deductibles")){
                            if(DialogDeductibles!=null && DialogDeductibles.isShowing()) {
                                DialogDeductibles.dismiss();
                            }
                        }else if(Type!=null && Type.equalsIgnoreCase("geographical_extention")){
                            if(DialogGeographicalExtension!=null && DialogGeographicalExtension.isShowing()) {
                                DialogGeographicalExtension.dismiss();
                            }
                        }else if(Type!=null && Type.equalsIgnoreCase("pa_covers")){
                            if(DialogPACovers!=null && DialogPACovers.isShowing()) {
                                DialogPACovers.dismiss();
                            }
                        }else if(Type!=null && Type.equalsIgnoreCase("addons")){
                            if(DialogAddOns!=null && DialogAddOns.isShowing()) {
                                DialogAddOns.dismiss();
                            }
                        }else if(Type!=null && Type.equalsIgnoreCase("change_idv")){
                            EdtIdvValue.setEnabled(false);
                        }

                        if(ll_parent_portfolio!=null && ll_parent_portfolio.getChildCount()>0){
                            ll_parent_portfolio.removeAllViews();
                        }
                        //IcList = new String[] {"3","6"};
                        for(int k=0; k< IcList.length; k++){
                            String ic_id = IcList[k];
                            LoadQuotation(ic_id);
                        }

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
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("agent_id", StrAgentId);
                    params.put("call_attr",Type);
                    if(Type!=null && Type.equalsIgnoreCase("accessories")){
                        if(Str_product_type_id.equalsIgnoreCase("1")) {
                            params.put("electrical_val",electrical_val);
                            params.put("non_electrical_val",non_electrical_val);
                            params.put("cng_value",cng_value);
                        }else if(Str_product_type_id.equalsIgnoreCase("2")){
                            params.put("electrical_val",electrical_val);
                            params.put("non_electrical_val",non_electrical_val);
                        }
                    }else if(Type!=null && Type.equalsIgnoreCase("deductibles")){
                        if(Str_product_type_id.equalsIgnoreCase("1")) {
                            params.put("is_antitheft",String.valueOf(is_antitheft));
                            params.put("is_automobile_association",String.valueOf(is_automobile_association));
                            params.put("aa_membership_no",aa_membership_no);
                            params.put("aa_membership_name",aa_membership_name);
                            params.put("aa_expirty_date",aa_expirty_date);
                        }else if(Str_product_type_id.equalsIgnoreCase("2")){
                            params.put("is_antitheft",String.valueOf(is_antitheft));
                        }
                    }else if(Type!=null && Type.equalsIgnoreCase("geographical_extention")){
                        params.put("geographical",geographical);
                    }else if(Type!=null && Type.equalsIgnoreCase("pa_covers")){
                        if(Str_product_type_id.equalsIgnoreCase("1")) {
                            params.put("pa_unnamed_persons_no", pa_unnamed_persons_no);
                            params.put("pa_unnamed_persons_value", pa_unnamed_persons_value);
                            params.put("ll_paid_driver_value", ll_paid_driver_value);
                            params.put("ll_paid_driver_name", ll_paid_driver_name);
                        }else if(Str_product_type_id.equalsIgnoreCase("2")){
                            params.put("pa_unnamed_persons_no", pa_unnamed_persons_no);
                            params.put("pa_unnamed_persons_value", pa_unnamed_persons_value);
                        }
                    }else if(Type!=null && Type.equalsIgnoreCase("addons")){
                        params.put("ic_id",Ic_Id);
                        params.put("addons_data",addons_data);
                    }else if(Type!=null && Type.equalsIgnoreCase("change_idv")){
                        params.put("new_idv",StrIdvNewValue);
                    }
                    params.put("mpn_data",StrMpnData);
                    params.put("user_action_data",StrUserActionData);
                    Log.d("BuyPolicyData",""+params);
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
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



    private void LoadQuotation(String ic_id) {

        String URL = ROOT_URL2+"loadquotation";
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



                        if(error!=null && !error.equalsIgnoreCase("") && error.equalsIgnoreCase("1")){
                            error_message = quote_html.getString("error_message");
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View rowView = inflater.inflate(R.layout.scheme_list_row1, null);
                            rowView.setPadding(10,10,10,10);
                            ImageView iv_IC_Img = (ImageView) rowView.findViewById(R.id.iv_IC_Img);

                            Glide.with(IcListingQuoteScreen_3.this)
                                    .load(logo_url)
                                    .into(iv_IC_Img);

                            TextView row_ic_id = (TextView)rowView.findViewById(R.id.row_ic_id);
                            row_ic_id.setText(ic_id);

                            TextView row_ic_name= (TextView)rowView.findViewById(R.id.row_ic_name);
                            if(error_message!=null && !error_message.equalsIgnoreCase("")) {
                                row_ic_name.setText(error_message.toUpperCase());
                                row_ic_name.setVisibility(View.VISIBLE);
                            }else {
                                row_ic_name.setText(ic_id_name);
                                row_ic_name.setVisibility(View.GONE);
                            }

                            ll_parent_portfolio.addView(rowView);

                        }else {

                            vehicle_idv = quote_html.getString("vehicle_idv");
                            available_od_discount = quote_html.getString("maxdisc");
                            total_addon_premium = quote_html.getString("total_addon_premium");
                            gross_premium = quote_html.getString("gross_premium");
                            Object selected_addon = quote_html.get("selected_addon");



                            Object addon_api = dataObj.get("addon_api");
                            if (addon_api instanceof JSONObject) {
                                // It's an array
                                addon_apiObject = (JSONObject)addon_api;

                                addon_button_html_message = "ADDONS";
                            }else {
                                addon_apiObject = null;
                                addon_button_html_message = "NO ADDONS";
                            }


                            //idv _ breakUp data

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View rowView = inflater.inflate(R.layout.scheme_list_row1, null);
                            rowView.setPadding(10,10,10,10);
                            ImageView iv_IC_Img = (ImageView) rowView.findViewById(R.id.iv_IC_Img);

                            Glide.with(IcListingQuoteScreen_3.this)
                                    .load(logo_url)
                                    .into(iv_IC_Img);

                            TextView row_ic_id = (TextView)rowView.findViewById(R.id.row_ic_id);
                            row_ic_id.setText(ic_id);

                            TextView row_ic_name= (TextView)rowView.findViewById(R.id.row_ic_name);
                            row_ic_name.setText(ic_id_name);

                            TextView row_net_premium_amt = (TextView)rowView.findViewById(R.id.row_net_premium_amt);
                            TextView net_premium_amt = (TextView)rowView.findViewById(R.id.net_premium_amt);


                            TextView row_addon_txt = (TextView)rowView.findViewById(R.id.row_addon_txt);
                            if(addon_apiObject!=null &&  !addon_apiObject.toString().equalsIgnoreCase("")) {
                                row_addon_txt.setText(addon_apiObject.toString());
                            }else {
                                row_addon_txt.setText("");
                            }

                            TextView row_selected_addon_txt = (TextView)rowView.findViewById(R.id.row_selected_addon_txt);

                            Button btn_add_on = (Button)rowView.findViewById(R.id.btn_add_on);
                            Button btn_breakup = (Button)rowView.findViewById(R.id.btn_breakup);
                            Button btn_buy_policy = (Button)rowView.findViewById(R.id.btn_buy_policy);

                            LinearLayout Linear_row_Premium = (LinearLayout)rowView.findViewById(R.id.Linear_row_Premium);
                            LinearLayout row_linear_idv = (LinearLayout)rowView.findViewById(R.id.row_linear_idv);
                            LinearLayout row_linear_buy_policy = (LinearLayout)rowView.findViewById(R.id.row_linear_buy_policy);
                            LinearLayout LayoutSelectedAddon = (LinearLayout)rowView.findViewById(R.id.LayoutSelectedAddon);



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
                                net_premium_amt.setText(gross_premium);
                                TextView tv_idv_in_rupees = (TextView) rowView.findViewById(R.id.tv_idv_in_rupees);
                                tv_idv_in_rupees.setText("\u20B9 "+vehicle_idv);

                                TextView row_addon_premium_amt = (TextView)rowView.findViewById(R.id.row_addon_premium_amt);
                                row_addon_premium_amt.setText("\u20B9 "+total_addon_premium + " (excl. tax)");
                                if (selected_addon instanceof JSONObject) {
                                    // It's an array
                                    selected_addonsObj = (JSONObject)selected_addon;
                                    row_selected_addon_txt.setText(selected_addonsObj.toString());
                                    LayoutSelectedAddon.setVisibility(View.VISIBLE);
                                    if(selected_addonsObj!=null) {
                                        Iterator keys = selected_addonsObj.keys();
                                        int k = 0;
                                        while (keys.hasNext()) {

                                            // Loop to get the dynamic key
                                            String currentDynamicKey = (String) keys.next();

                                            Log.d("currentDynamicKey", "" + currentDynamicKey);
                                            // Get the value of the dynamic key
                                            try {
                                                if(currentDynamicKey.equalsIgnoreCase("addons_sum") ||
                                                        currentDynamicKey.equalsIgnoreCase("addons_sum_1year") ||
                                                        currentDynamicKey.equalsIgnoreCase("addons_sum_2year") ||
                                                        currentDynamicKey.equalsIgnoreCase("addons_sum_3year") ||
                                                        currentDynamicKey.equalsIgnoreCase("addon_premium")) {

                                                    k++;

                                                }else {
                                                    if(String.valueOf(selected_addonsObj).contains("addon_name")) {

                                                        JSONObject strselected_addonsObj = selected_addonsObj.getJSONObject(currentDynamicKey);
                                                        String addon_id = strselected_addonsObj.getString("addon_id");
                                                        String name = strselected_addonsObj.getString("addon_name");
                                                        LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                        final View rowView1 = inflater1.inflate(R.layout.scheme_selected_addon_list, null);
                                                        rowView1.setPadding(10, 10, 10, 10);

                                                        TextView tv_selected_add_on_name = (TextView) rowView1.findViewById(R.id.tv_selected_add_on_name);
                                                        tv_selected_add_on_name.setText(name);


                                                        LayoutSelectedAddon.addView(rowView1);
                                                    }else {
                                                        LayoutSelectedAddon.setVisibility(View.GONE);
                                                    }
                                                    k++;
                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }else {
                                    LayoutSelectedAddon.setVisibility(View.GONE);
                                    selected_addonsObj = null;
                                    row_selected_addon_txt.setText("");

                                }


                                btn_add_on.setText(addon_button_html_message);
                                btn_add_on.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (btn_add_on.getText().toString()!= null && !btn_add_on.getText().toString().equalsIgnoreCase("null") && !addon_button_html_message.equalsIgnoreCase("")) {
                                            if (btn_add_on.getText().toString().equalsIgnoreCase("No Addons")) {
                                                CommonMethods.DisplayToastInfo(getApplicationContext(), "No AddOns Available");
                                            }else if (btn_add_on.getText().toString().equalsIgnoreCase("ADDONS")) {
                                                ShowPopUpAddOns(row_addon_txt.getText().toString(),row_selected_addon_txt.getText().toString(),row_ic_name.getText().toString(),row_ic_id.getText().toString());
                                            }
                                        }
                                    }
                                });

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
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"total_premium_payable",net_premium_amt.getText().toString());
                                    API_BUY_POLICY(row_ic_id.getText().toString());
                                }
                            });


                            ll_parent_portfolio.addView(rowView);
                        }

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

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
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
        String URL = ROOT_URL2+"buypolicy";
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

                        Intent i = new Intent(getApplicationContext(), CustomerPage_4.class);
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

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //headers.put("Content-Type", "application/x-www-form-urlencoded");
                    //headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                    return headers;
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


        DialogBreakUpDetail = new Dialog(IcListingQuoteScreen_3.this);
        DialogBreakUpDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogBreakUpDetail.setCanceledOnTouchOutside(true);
        DialogBreakUpDetail.setCancelable(true);
        DialogBreakUpDetail.setContentView(R.layout.layout_scheme_detail_break_up);
        Objects.requireNonNull(DialogBreakUpDetail.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)DialogBreakUpDetail.findViewById(R.id.title) ;
        ImageView iv_close = (ImageView)DialogBreakUpDetail.findViewById(R.id.iv_close);

        CardView CardVehicleDetails = (CardView) DialogBreakUpDetail.findViewById(R.id.CardVehicleDetails);
        CardView CardTPBreakup = (CardView) DialogBreakUpDetail.findViewById(R.id.CardTPBreakup);
        CardView CardODBreakup = (CardView) DialogBreakUpDetail.findViewById(R.id.CardODBreakup);
        CardView CardAddOnsBreakup = (CardView)DialogBreakUpDetail.findViewById(R.id.CardAddOnsBreakup);
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


        //ADDONS
        WebView Wb_AddOns_BreakUp = (WebView)DialogBreakUpDetail.findViewById(R.id.Wb_AddOns_BreakUp);
        LinearLayout LayoutAddOns_Renew = (LinearLayout)DialogBreakUpDetail.findViewById(R.id.LayoutAddOns_Renew);
        LinearLayout LayoutDepCoverAddons = (LinearLayout)DialogBreakUpDetail.findViewById(R.id.LayoutDepCoverAddons);

        final TextView til_addon_header_name = (TextView) DialogBreakUpDetail.findViewById(R.id.til_addon_header_name);
        final TextView tv_dep_cover_addon_value = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_dep_cover_addon_value);
        final TextView tv_total_net_addons_premium = (TextView) DialogBreakUpDetail.findViewById(R.id.tv_total_net_addons_premium);




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


                Glide.with(IcListingQuoteScreen_3.this)
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
                    b_addOn_Premium = breakup_html.getString("b_add_on_Premium");
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

                        if(selected_od_year!=null && !selected_od_year.equalsIgnoreCase("") && (selected_od_year.equalsIgnoreCase("5")|| selected_od_year.equalsIgnoreCase("3"))) {
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
                        if(selected_od_year!=null && !selected_od_year.equalsIgnoreCase("") && (selected_od_year.equalsIgnoreCase("5")|| selected_od_year.equalsIgnoreCase("3"))) {
                            LayoutDeductibles_Renew.setVisibility(View.GONE);
                            Wb_Deductibles.setVisibility(View.VISIBLE);
                            deductibles_year_wiseObj = antitheft.getJSONObject("deductibles_year_wise");

                            antitheft_value_year_wiseObj = antitheft.getJSONObject("antitheft_value_year_wise");
                            total_antitheft_value = antitheft.getString("total_antitheft_value_year_wise");

                            //total_automobile_association_premium_year_wiseObj = antitheft.getJSONObject("automobile_association_premium_year_wise");
                            //total_automobile_association_value = antitheft.getString("total_automobile_association_premium_year_wise");

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

                    //Add Ons

                    Object addon = breakup_html.get("addon");
                    if (addon instanceof String) {
                        // It's an object
                        String strAddOns = (String)addon;
                        if(strAddOns!=null && !strAddOns.equalsIgnoreCase("")){
                            if(strAddOns.equalsIgnoreCase("No Addon Selected")){
                                CardAddOnsBreakup.setVisibility(View.GONE);
                            }
                        }

                    }if (addon instanceof JSONObject) {
                        // It's an object
                        JSONObject addonObj = (JSONObject)addon;
                        if(addonObj!=null){

                            if(Str_policy_type.equalsIgnoreCase("renew")) {
                                LayoutAddOns_Renew.setVisibility(View.VISIBLE);
                                Wb_AddOns_BreakUp.setVisibility(View.GONE);
                                String addon_name = addonObj.getString("addon_name");
                                String addon_premium = addonObj.getString("addon_premium");
                                String net_addons_premium = quote_html.getString("total_addon_premium");
                                til_addon_header_name.setText(addon_name);
                                tv_dep_cover_addon_value.setText(addon_premium);
                                tv_total_net_addons_premium.setText(net_addons_premium);

                            }else {
                                if(selected_od_year!=null && !selected_od_year.equalsIgnoreCase("") && (selected_od_year.equalsIgnoreCase("5")|| selected_od_year.equalsIgnoreCase("3"))) {
                                    LayoutAddOns_Renew.setVisibility(View.GONE);
                                    Wb_AddOns_BreakUp.setVisibility(View.VISIBLE);
                                    Addon_headerObj = addonObj.getJSONObject("Addon_header");
                                    Addon_valueObj = addonObj.getJSONObject("Addon_value");
                                    Addon_totalObj = addonObj.getJSONObject("addon_total");

                                    Wb_AddOns_BreakUp.addJavascriptInterface(new WebAppInterfaceAddOns(), "Android");
                                    Wb_AddOns_BreakUp.getSettings().setJavaScriptEnabled(true);
                                    Wb_AddOns_BreakUp.loadUrl("file:///android_asset/www/web_add_ons.html");
                                }else {
                                    LayoutAddOns_Renew.setVisibility(View.VISIBLE);
                                    Wb_AddOns_BreakUp.setVisibility(View.GONE);
                                    String addon_name = addonObj.getString("addon_name");
                                    String addon_premium = addonObj.getString("addon_premium");
                                    String net_addons_premium = quote_html.getString("total_addon_premium");
                                    til_addon_header_name.setText(addon_name);
                                    tv_dep_cover_addon_value.setText(addon_premium);
                                    tv_total_net_addons_premium.setText(net_addons_premium);

                                }
                            }



                            CardAddOnsBreakup.setVisibility(View.VISIBLE);

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

        /*@JavascriptInterface
        public String get_total_automobile_association_premium_year_wiseObj() {
            Log.d("automobile_year_wiseObj", "" + total_automobile_association_premium_year_wiseObj);
            return total_automobile_association_premium_year_wiseObj.toString();
        }*/


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

        /* @JavascriptInterface
         public String get_total_automobile_association_value() {
             Log.d("total_automobile_value", "" + total_automobile_association_value);
             return total_automobile_association_value;
         }
 */
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

    private class WebAppInterfaceAddOns{

        @JavascriptInterface
        public String get_addon_headerObj() {
            Log.d("Addon_headerObj", "" + Addon_headerObj);
            return Addon_headerObj.toString();
        }



        @JavascriptInterface
        public String get_addon_valueObj() {
            Log.d("Addon_valueObj", "" + Addon_valueObj);
            return Addon_valueObj.toString();
        }

        @JavascriptInterface
        public String get_addon_totalObj() {
            Log.d("Addon_totalObj", "" + Addon_totalObj);
            return Addon_totalObj.toString();
        }

        @JavascriptInterface
        public String get_net_add_on_premium() {
            Log.d("b_addOn_Premium", "" + b_addOn_Premium);
            return b_addOn_Premium;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
