package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.indicosmic.www.mypolicynow.R;


import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.Constant;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import io.grpc.ProxyDetector;

public class QuotationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView back_btn;
    ProgressDialog myDialog;
    String QuotationFor="Bike",StrPucReminder="0";
    LinearLayout LayoutCar,LayoutBike;
    ImageView iv_bike,iv_car,iv_commercial;
    TextView til_commercial_insurance,til_car_insurance,til_bike_insurance;
    DatePickerDialog registrationDatePickerDialog,prePolicyExpiryDatePickerDialog,dateOfExpiryDatePickerDialog;
    String RegistrationDate="",PusDateOfIssue="",PusDateOfExpiry="";
    private SimpleDateFormat dateFormatter,dateFormatter1,dateFormatter2,dateFormatter3;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mYear1, mMonth1, mDay1, mHour1, mMinute1;
    private int mYear2, mMonth2, mDay2, mHour2, mMinute2;

    EditText EdtRegistrationDate,EdtInvoicePrice;

    String PolicyType="Comprehensive";
    ScrollView Scroll_BelowLayout;
    TextView TV_ComprehensivePolicy,TV_TPPolicy,TV_StandaloneOd;
    RadioGroup RG_PolicyType;
    RadioButton Rb_NewPolicy,Rb_ReNewPolicy;
    ArrayList<String> manufacturingYear = new ArrayList<String>();
    SearchableSpinner Spn_RTO,Spn_Make,Spn_Model,Spn_Variant;
    Spinner Spn_ManufacturingYear,Spn_ManufacturingMonth;
    String StrPolicyType = "New",StrManufacturingYear,StrManufacturingMonth;
    Spinner Spn_PolicyHolder;
    int registration_year=0,registration_month=0;
    RadioGroup RG_ChangeInOwnership,RG_PreviousPolicy,RG_PreviousPolicyType,RG_HaveMadeClaim;
    RadioButton Rb_NoChangeInOwnership,Rb_YesChangeInOwnership,Rb_NoPreviousPolicy,Rb_YesPreviousPolicy,Rb_ComprehensivePrePolicy,Rb_ThirdPartyPrePolicy,Rb_NotMadeClaim,Rb_YesMadeClaim;
    LinearLayout SameOwnerLayout,LinearPreviousPolicyType,LinearExpiryDate,LinearHaveMadeClaim,LayoutNilDept,LinearNCB_Per;
    Spinner Spn_NCB_Percent,Spn_ODDiscount;
    EditText EdtPreviousPolicyExpiryDate;
    String StrExpiryDate = "";
    CheckBox ChkNilDept;
    String SelectedRtoId="",SelectedMakeId="",SelectedModelId="",SelectedVaraintId="",SelectedVehicleId="";
    LinearLayout LinearChangeInOwnership,LayoutODDisount,InvoiceLayout,LinearNewPolicyWanted,IndividualPolicyHolderLayout;
    RadioGroup RG_NewPolicyRequired;
    RadioButton Rb_1OD5TP,Rb_5OD5TP,Rb_3OD3TP;
    TextView til_invoice_price;

    Spinner Spn_CPASelection,Spn_ReasonOptingOutCPA;
    LinearLayout LayoutReasonOptingOutCpa;

    ArrayList<String> rtoValue = new ArrayList<String>();
    ArrayList<String> rtoDisplayValue = new ArrayList<String>();

    ArrayList<String> makeValue = new ArrayList<String>();
    ArrayList<String> makeDisplayValue = new ArrayList<String>();

    ArrayList<String> modelValue = new ArrayList<String>();
    ArrayList<String> modelDisplayValue = new ArrayList<String>();

    ArrayList<String> cpaDisplayValue = new ArrayList<String>();


    ArrayList<String> variantValue = new ArrayList<String>();
    ArrayList<String> variantDisplayValue = new ArrayList<String>();
    ArrayList<String> variantVehicleIdValue = new ArrayList<String>();

    ArrayList<String> manufacturingMonthValue = new ArrayList<String>();
    ArrayList<String> manufacturingMonthDisplayValue = new ArrayList<String>();

    ArrayList<String> ncbDisplayValue = new ArrayList<String>();

    String StrProduct="",StrPosToken="",StrAgentId="",ProductTypeId="2",StrPolicyHolder="";
    Button BtnGetQuote;
    String StrRegistrationDate="",StrRegistration_monthId="0";

    String ex_showroom_price;
    Integer min_ex_showroom_price=0,max_ex_showroom_price=0;
    public  String[] IcList;


    //Final values to be sent
    String StrRtoCode="",StrRtoLabel="",StrRtoCity="",StrRtoPincode="",StrRtoCityCode="",StrRtoCityId="",StrRtoStateCode="",
            StrRtoStateId="",StrRtoStateName="",StrRtoZone="",StrRtoZoneTypeId="", manufacturing_date="";
    String StrVehicleId = "",StrVariantCleaned= "",StrVariant= "",StrSeatingCapacity= "",StrCC= "",StrGVW= "",
            StrFuelType= "",StrFuelTypeCleaned="";
    String policy_package_type = "",policy_type_selection="";
    String is_changes_in_ownership="no",is_previous_policy="no",previous_yr_policy_type="",is_claimed="no",
            previous_policy_ncb="0",selected_od_discount="",StrInvoicePrice="",selected_od_year="",have_motor_license="no",
            have_motor_policy="no",have_pa_policy="no",other_pa_policy="no",selected_pa_year="",previous_policy_nil_dep="no",
            ownership_change="no",product_type="bike",is_cng_lpg_tp="no",commercial_idv="0",no_of_trailer="0",
            vehicledisplaytype="0",body_type_id="0",frame_type_id="";

    String StrSelectedMake="",make_cleaned="",SelectedModel="",model_cleaned="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_screen);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("pos_token") != null) {
            StrPosToken = bundle.getString("pos_token");
        } else {
            StrPosToken = "";
        }

        QuotationFor = UtilitySharedPreferences.getPrefs(getApplicationContext(), "QuotationFor");

        getPreveledgesFromToken();

    }

    private void getPreveledgesFromToken() {
        if (StrPosToken != null && !StrPosToken.equalsIgnoreCase("")) {

            String URL = RestClient.ROOT_URL2+"tokenverify";
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

                            JSONObject agent_detailObj = data_obj.getJSONObject("agent_detail");

                            StrAgentId = agent_detailObj.getString("id");
                            String StrAgentName = agent_detailObj.getString("app_fullname");
                            String StrAgentMObile = agent_detailObj.getString("mobile_no");
                            String StrAgentEmail = agent_detailObj.getString("email");


                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosId",StrAgentId);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosName",StrAgentName);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosMobile",StrAgentMObile);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosEmail",StrAgentEmail);


                            JSONObject partner_privilegeObj = data_obj.getJSONObject("partner_privilege");

                            if(QuotationFor!=null && !QuotationFor.equalsIgnoreCase("")){

                                if(QuotationFor.equalsIgnoreCase("Car")){
                                    JSONObject car_previledgeObj = partner_privilegeObj.getJSONObject("1");
                                    ProductTypeId = car_previledgeObj.getString("product_type_id");
                                    String IcList = car_previledgeObj.getString("ic_ids");
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"IcList",IcList);
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"ProductTypeId",ProductTypeId);
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"CarPreviledges",car_previledgeObj.toString());

                                }else if(QuotationFor.equalsIgnoreCase("Bike")){
                                    JSONObject bike_previledgeObj = partner_privilegeObj.getJSONObject("2");
                                    ProductTypeId = bike_previledgeObj.getString("product_type_id");
                                    String IcList = bike_previledgeObj.getString("ic_ids");
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"IcList",IcList);
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"ProductTypeId",ProductTypeId);
                                    UtilitySharedPreferences.setPrefs(getApplicationContext(),"BikePreviledges",bike_previledgeObj.toString());

                                }


                            }
                            init();

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
                        map.put("token_number", StrPosToken);
                        Log.d("Token_verify",""+map);
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
    }

    private void init() {


        myDialog = new ProgressDialog(QuotationActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        LayoutCar = (LinearLayout)findViewById(R.id.LayoutCar);
        LayoutBike = (LinearLayout)findViewById(R.id.LayoutBike);

        iv_bike = (ImageView) findViewById(R.id.iv_bike);
        iv_car = (ImageView) findViewById(R.id.iv_car);
        iv_commercial = (ImageView) findViewById(R.id.iv_commercial);

        til_car_insurance = (TextView)findViewById(R.id.til_car_insurance);
        til_bike_insurance = (TextView)findViewById(R.id.til_bike_insurance);
        til_commercial_insurance = (TextView)findViewById(R.id.til_commercial_insurance);



        if(QuotationFor!=null && !QuotationFor.equalsIgnoreCase("")){
            if(QuotationFor.equalsIgnoreCase("Car")){
                product_type="privatecar";
                LayoutBike.setBackground(getDrawable(R.drawable.form_bg_edittext_bg));
                LayoutCar.setBackground(getDrawable(R.drawable.form_bg_selected));

                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);

                iv_car.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                iv_bike.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                til_car_insurance.setTextColor(getResources().getColor(R.color.white));
                til_bike_insurance.setTextColor(getResources().getColor(R.color.black));


            }
            else if(QuotationFor.equalsIgnoreCase("Bike")){
                product_type="bike";
                LayoutBike.setBackground(getDrawable(R.drawable.form_bg_selected));
                LayoutCar.setBackground(getDrawable(R.drawable.form_bg_edittext_bg));

                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);

                iv_bike.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                iv_car.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                til_car_insurance.setTextColor(getResources().getColor(R.color.black));
                til_bike_insurance.setTextColor(getResources().getColor(R.color.white));

            }else if(QuotationFor.equalsIgnoreCase("Commercial")){
                product_type="commercial";
                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);
            }

        }




        iv_commercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Commercial";
                product_type="commercial";
                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);
            }
        });

        Scroll_BelowLayout = (ScrollView)findViewById(R.id.Scroll_BelowLayout);

        TV_ComprehensivePolicy = (TextView)findViewById(R.id.TV_ComprehensivePolicy);
        TV_TPPolicy = (TextView)findViewById(R.id.TV_TPPolicy);
        TV_StandaloneOd = (TextView)findViewById(R.id.TV_StandaloneOd);

        RG_PolicyType = (RadioGroup)findViewById(R.id.RG_PolicyType);
        Rb_NewPolicy = (RadioButton)findViewById(R.id.Rb_NewPolicy);
        Rb_ReNewPolicy = (RadioButton)findViewById(R.id.Rb_ReNewPolicy);

        TV_ComprehensivePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("Comprehensive");
                getManufacturingYear();
                setChangeInOwnserhipVisibility();
            }
        });

        TV_TPPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("ThirdParty");
                RG_PolicyType.setVisibility(View.VISIBLE);
                getManufacturingYear();
                setChangeInOwnserhipVisibility();

            }
        });

        TV_StandaloneOd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("Standalone");
                getManufacturingYear();
                setChangeInOwnserhipVisibility();
            }
        });
        setBackGroundColor("Comprehensive");
        //getManufacturingYear();
        //setChangeInOwnserhipVisibility();

        Spn_RTO = (SearchableSpinner) findViewById(R.id.Spn_RTO);
        Spn_Make = (SearchableSpinner) findViewById(R.id.Spn_Make);
        //product_type="bike";
        GetMasterFor();

        Spn_Model = (SearchableSpinner) findViewById(R.id.Spn_Model);
        Spn_Variant= (SearchableSpinner) findViewById(R.id.Spn_Variant);

        Spn_RTO.setOnItemSelectedListener(this);
        Spn_Make.setOnItemSelectedListener(this);
        Spn_Model.setOnItemSelectedListener(this);
        Spn_Variant.setOnItemSelectedListener(this);

        Spn_ManufacturingYear = (Spinner)findViewById(R.id.Spn_ManufacturingYear);
        StrPolicyType = "New";
        StrRegistrationDate = CommonMethods.DisplayCurrentDate();
        getManufacturingYear();


        til_invoice_price = (TextView)findViewById(R.id.til_invoice_price);
        til_invoice_price.setText("Invoice Price");

        Spn_ManufacturingYear.setOnItemSelectedListener(this);


        Spn_ManufacturingMonth= (Spinner)findViewById(R.id.Spn_ManufacturingMonth);
       /* ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Constant.Month);
        Spn_ManufacturingMonth.setAdapter(monthAdapter);*/
        Spn_ManufacturingMonth.setOnItemSelectedListener(this);


        EdtRegistrationDate= (EditText)findViewById(R.id.EdtRegistrationDate);
        EdtRegistrationDate.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        if(StrPolicyType.equalsIgnoreCase("New")){
            EdtRegistrationDate.setText(StrRegistrationDate);
        }else {
            EdtRegistrationDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(StrPolicyType.equalsIgnoreCase("New")){
                        EdtRegistrationDate.setText(StrRegistrationDate);
                    }else {
                        if (registration_year != 0 && registration_month != 0) {
                            setRegistrationDate(registration_year, registration_month, 1);
                        } else {
                            setRegistrationDate(mYear, mMonth, mDay);
                        }
                    }


                }
            });
        }

        EdtInvoicePrice = (EditText)findViewById(R.id.EdtInvoicePrice);

        EdtPreviousPolicyExpiryDate= (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);
        EdtPreviousPolicyExpiryDate.setInputType(InputType.TYPE_NULL);
        EdtPreviousPolicyExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");*/
               prePolicyExpiryDatePickerDialog = new DatePickerDialog(QuotationActivity.this,
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
                            StrExpiryDate = outputFormat.format(date);
                            EdtPreviousPolicyExpiryDate.setText(StrExpiryDate);
                            checkIsBreakInCase();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, mYear1, mMonth1+1, -1);

                prePolicyExpiryDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //prePolicyExpiryDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                //((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                prePolicyExpiryDatePickerDialog.show();
            }
        });




        LinearChangeInOwnership = (LinearLayout)findViewById(R.id.LinearChangeInOwnership);
        LayoutODDisount = (LinearLayout)findViewById(R.id.LayoutODDisount);
        InvoiceLayout = (LinearLayout)findViewById(R.id.InvoiceLayout);
        LinearNewPolicyWanted = (LinearLayout)findViewById(R.id.LinearNewPolicyWanted);
        RG_NewPolicyRequired= (RadioGroup)findViewById(R.id.RG_NewPolicyRequired);
        Rb_1OD5TP = (RadioButton)findViewById(R.id.Rb_1OD5TP);
        Rb_5OD5TP = (RadioButton)findViewById(R.id.Rb_5OD5TP);
        Rb_3OD3TP = (RadioButton)findViewById(R.id.Rb_3OD3TP);

        if(ProductTypeId.equalsIgnoreCase("2")){
            Rb_3OD3TP.setVisibility(View.GONE);
            Rb_5OD5TP.setVisibility(View.VISIBLE);
        }else if(QuotationFor.equalsIgnoreCase("1")){
            Rb_3OD3TP.setVisibility(View.VISIBLE);
            Rb_5OD5TP.setVisibility(View.GONE);
        }


        LayoutBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Bike";
                product_type="bike";
                LayoutBike.setBackground(getDrawable(R.drawable.form_bg_selected));
                LayoutCar.setBackground(getDrawable(R.drawable.form_bg_edittext_bg));

                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);

                iv_bike.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                iv_car.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                til_car_insurance.setTextColor(getResources().getColor(R.color.black));
                til_bike_insurance.setTextColor(getResources().getColor(R.color.white));
                Rb_3OD3TP.setVisibility(View.GONE);
                Rb_5OD5TP.setVisibility(View.VISIBLE);
                GetMasterFor();
            }
        });


        LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Car";
                product_type="privatecar";
                LayoutBike.setBackground(getDrawable(R.drawable.form_bg_edittext_bg));
                LayoutCar.setBackground(getDrawable(R.drawable.form_bg_selected));

                Glide.with(QuotationActivity.this).load(R.drawable.bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.commercial_new).into(iv_commercial);

                iv_car.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                iv_bike.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
                til_car_insurance.setTextColor(getResources().getColor(R.color.white));
                til_bike_insurance.setTextColor(getResources().getColor(R.color.black));
                Rb_3OD3TP.setVisibility(View.VISIBLE);
                Rb_5OD5TP.setVisibility(View.GONE);
                GetMasterFor();
            }
        });


        RG_ChangeInOwnership= (RadioGroup)findViewById(R.id.RG_ChangeInOwnership);
        Rb_NoChangeInOwnership = (RadioButton)findViewById(R.id.Rb_NoChangeInOwnership);
        Rb_YesChangeInOwnership = (RadioButton)findViewById(R.id.Rb_YesChangeInOwnership);

        RG_PreviousPolicy= (RadioGroup)findViewById(R.id.RG_PreviousPolicy);
        Rb_NoPreviousPolicy = (RadioButton)findViewById(R.id.Rb_NoPreviousPolicy);
        Rb_YesPreviousPolicy = (RadioButton)findViewById(R.id.Rb_YesPreviousPolicy);

        RG_PreviousPolicyType= (RadioGroup)findViewById(R.id.RG_PreviousPolicyType);
        Rb_ComprehensivePrePolicy = (RadioButton)findViewById(R.id.Rb_ComprehensivePrePolicy);
        Rb_ThirdPartyPrePolicy = (RadioButton)findViewById(R.id.Rb_ThirdPartyPrePolicy);

        RG_HaveMadeClaim= (RadioGroup)findViewById(R.id.RG_HaveMadeClaim);
        Rb_NotMadeClaim = (RadioButton)findViewById(R.id.Rb_NotMadeClaim);
        Rb_YesMadeClaim = (RadioButton)findViewById(R.id.Rb_YesMadeClaim);


        SameOwnerLayout= (LinearLayout)findViewById(R.id.SameOwnerLayout);
        LinearPreviousPolicyType= (LinearLayout)findViewById(R.id.LinearPreviousPolicyType);
        LinearExpiryDate = (LinearLayout)findViewById(R.id.LinearExpiryDate);
        LinearHaveMadeClaim= (LinearLayout)findViewById(R.id.LinearHaveMadeClaim);
        LayoutNilDept = (LinearLayout)findViewById(R.id.LayoutNilDept);
        LinearNCB_Per= (LinearLayout)findViewById(R.id.LinearNCB_Per);
        ChkNilDept = (CheckBox)findViewById(R.id.ChkNilDept);

        ChkNilDept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    previous_policy_nil_dep = "yes";
                }else {
                    previous_policy_nil_dep = "no";
                }
            }
        });

        Spn_NCB_Percent= (Spinner)findViewById(R.id.Spn_NCB_Percent);
        Spn_ODDiscount= (Spinner)findViewById(R.id.Spn_ODDiscount);

        EdtPreviousPolicyExpiryDate = (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);
        EdtPreviousPolicyExpiryDate = (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);


        RG_NewPolicyRequired.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_1OD5TP.isChecked()) {
                    Rb_1OD5TP.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_1OD5TP.setTextColor(getResources().getColor(R.color.white));
                    Rb_1OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_5OD5TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_5OD5TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_5OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    Rb_3OD3TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_3OD3TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_3OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    selected_od_year = "1";

                }
                if (Rb_5OD5TP.isChecked()){
                    Rb_5OD5TP.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_5OD5TP.setTextColor(getResources().getColor(R.color.white));
                    Rb_5OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_1OD5TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_1OD5TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_1OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    Rb_3OD3TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_3OD3TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_3OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    selected_od_year = "5";

                }

                if (Rb_3OD3TP.isChecked()){
                    Rb_3OD3TP.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_3OD3TP.setTextColor(getResources().getColor(R.color.white));
                    Rb_3OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_5OD5TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_5OD5TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_5OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    Rb_1OD5TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_1OD5TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_1OD5TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    selected_od_year = "3";

                }

            }
        });



        RG_ChangeInOwnership.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoChangeInOwnership.isChecked()) {
                    Rb_NoChangeInOwnership.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoChangeInOwnership.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesChangeInOwnership.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesChangeInOwnership.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    SameOwnerLayout.setVisibility(View.VISIBLE);
                    LinearPreviousPolicyType.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);
                    is_changes_in_ownership = "no";

                }
                if (Rb_YesChangeInOwnership.isChecked()) {
                    Rb_YesChangeInOwnership.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesChangeInOwnership.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoChangeInOwnership.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoChangeInOwnership.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    SameOwnerLayout.setVisibility(View.GONE);
                    LinearPreviousPolicyType.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);
                    is_changes_in_ownership = "yes";


                }

            }
        });


        RG_PreviousPolicy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoPreviousPolicy.isChecked()) {
                    Rb_NoPreviousPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoPreviousPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesPreviousPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesPreviousPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPreviousPolicyType.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    is_previous_policy = "no";
                    scrollDown();

                }
                if (Rb_YesPreviousPolicy.isChecked()){
                    Rb_YesPreviousPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesPreviousPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoPreviousPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoPreviousPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPreviousPolicyType.setVisibility(View.VISIBLE);
                    LayoutNilDept.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    is_previous_policy = "yes";
                    scrollDown();

                }

            }
        });

        RG_PreviousPolicyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_ComprehensivePrePolicy.isChecked()) {
                    Rb_ComprehensivePrePolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ComprehensivePrePolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_ComprehensivePrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ThirdPartyPrePolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ThirdPartyPrePolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_ThirdPartyPrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearHaveMadeClaim.setVisibility(View.VISIBLE);
                    LinearExpiryDate.setVisibility(View.VISIBLE);
                    LayoutNilDept.setVisibility(View.VISIBLE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    previous_yr_policy_type = "comprehensive";
                    scrollDown();

                }
                if (Rb_ThirdPartyPrePolicy.isChecked()){
                    Rb_ThirdPartyPrePolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ThirdPartyPrePolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_ThirdPartyPrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ComprehensivePrePolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ComprehensivePrePolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_ComprehensivePrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    LayoutNilDept.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.VISIBLE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    previous_yr_policy_type = "third_party";

                    scrollDown();

                }

            }
        });

        RG_HaveMadeClaim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NotMadeClaim.isChecked()) {
                    Rb_NotMadeClaim.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NotMadeClaim.setTextColor(getResources().getColor(R.color.white));
                    Rb_NotMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesMadeClaim.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesMadeClaim.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearNCB_Per.setVisibility(View.VISIBLE);
                    is_claimed = "no";
                    scrollDown();

                }
                if (Rb_YesMadeClaim.isChecked()){
                    Rb_YesMadeClaim.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesMadeClaim.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NotMadeClaim.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NotMadeClaim.setTextColor(getResources().getColor(R.color.black));
                    Rb_NotMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearNCB_Per.setVisibility(View.GONE);
                    is_claimed = "yes";

                    scrollDown();
                }

            }
        });



        Spn_PolicyHolder= (Spinner)findViewById(R.id.Spn_PolicyHolder);
        IndividualPolicyHolderLayout = (LinearLayout)findViewById(R.id.IndividualPolicyHolderLayout);

        Spn_PolicyHolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StrPolicyHolder = Spn_PolicyHolder.getSelectedItem().toString().trim();
                if(StrPolicyHolder!=null && StrPolicyHolder.equalsIgnoreCase("INDIVIDUAL")){
                    IndividualPolicyHolderLayout.setVisibility(View.VISIBLE);
                }else if(StrPolicyHolder!=null && StrPolicyHolder.equalsIgnoreCase("CORPORATE")){
                    IndividualPolicyHolderLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Spn_CPASelection=(Spinner)findViewById(R.id.Spn_CPASelection);
        Spn_ReasonOptingOutCPA=(Spinner)findViewById(R.id.Spn_ReasonOptingOutCPA);
        LayoutReasonOptingOutCpa=(LinearLayout)findViewById(R.id.LayoutReasonOptingOutCpa);

        Spn_CPASelection.setOnItemSelectedListener(this);
        Spn_ReasonOptingOutCPA.setOnItemSelectedListener(this);
       /* if(StrPolicyType.equalsIgnoreCase("Renew")){
            Rb_5YearPACover.setVisibility(View.GONE);
        }else {
            Rb_5YearPACover.setVisibility(View.VISIBLE);
        }

        RG_ValidLicence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NotValidLicence.isChecked()) {
                    Rb_NotValidLicence.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NotValidLicence.setTextColor(getResources().getColor(R.color.white));
                    Rb_NotValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ValidLicence.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ValidLicence.setTextColor(getResources().getColor(R.color.black));
                    Rb_ValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_motor_license = "no";
                    LinearValidMotorPolicy.setVisibility(View.GONE);
                    scrollDown();
                }
                if (Rb_ValidLicence.isChecked()){
                    Rb_ValidLicence.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ValidLicence.setTextColor(getResources().getColor(R.color.white));
                    Rb_ValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NotValidLicence.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NotValidLicence.setTextColor(getResources().getColor(R.color.black));
                    Rb_NotValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_motor_license = "yes";
                    LinearValidMotorPolicy.setVisibility(View.VISIBLE);
                    scrollDown();
                }

            }
        });

        RG_AnotherPolicy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoOtherPolicy.isChecked()) {
                    Rb_NoOtherPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoOtherPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesOtherPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesOtherPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_motor_policy = "no";
                    LinearAnotherPA_Policy.setVisibility(View.VISIBLE);
                    scrollDown();
                }
                if (Rb_YesOtherPolicy.isChecked()){
                    Rb_YesOtherPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesOtherPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoOtherPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoOtherPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_motor_policy = "yes";
                    LinearAnotherPA_Policy.setVisibility(View.GONE);
                    scrollDown();
                }

            }
        });


        RG_AnotherPA_Policy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoOtherPA_Policy.isChecked()) {
                    Rb_NoOtherPA_Policy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoOtherPA_Policy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesOtherPA_Policy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesOtherPA_Policy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_pa_policy = "no";
                    LinearPA_Cover.setVisibility(View.VISIBLE);
                    scrollDown();
                }
                if (Rb_YesOtherPA_Policy.isChecked()){
                    Rb_YesOtherPA_Policy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesOtherPA_Policy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoOtherPA_Policy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoOtherPA_Policy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    have_pa_policy = "yes";
                    LinearPA_Cover.setVisibility(View.GONE);
                    scrollDown();
                }

            }
        });


        RG_PA_Cover.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_1YearPACover.isChecked()) {
                    Rb_1YearPACover.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_1YearPACover.setTextColor(getResources().getColor(R.color.white));
                    Rb_1YearPACover.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_5YearPACover.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_5YearPACover.setTextColor(getResources().getColor(R.color.black));
                    Rb_5YearPACover.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    scrollDown();
                    selected_pa_year = "1";

                }
                if (Rb_5YearPACover.isChecked()){
                    Rb_5YearPACover.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_5YearPACover.setTextColor(getResources().getColor(R.color.white));
                    Rb_5YearPACover.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_1YearPACover.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_1YearPACover.setTextColor(getResources().getColor(R.color.black));
                    Rb_1YearPACover.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    selected_pa_year = "5";
                    scrollDown();
                }

            }
        });*/

        RG_PolicyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NewPolicy.isChecked()) {
                    Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    StrPolicyType = "New";
                    getManufacturingYear();
                    setChangeInOwnserhipVisibility();
                    /*Rb_5YearPACover.setVisibility(View.VISIBLE);
                    Rb_5YearPACover.setChecked(false);*/
                }
                if (Rb_ReNewPolicy.isChecked()){
                    Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    StrPolicyType = "Renew";
                    getManufacturingYear();
                    setChangeInOwnserhipVisibility();
                   /* Rb_5YearPACover.setVisibility(View.GONE);
                    Rb_5YearPACover.setChecked(false);
*/

                }

            }
        });
        GetNCBListApi();
        setChangeInOwnserhipVisibility();

        BtnGetQuote = (Button)findViewById(R.id.BtnGetQuote);
        BtnGetQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()){
                    API_GET_QUOTE();
                }
            }
        });

    }

    private void GetMasterFor() {

        myDialog.show();
        rtoValue = new ArrayList<>();
        rtoDisplayValue = new ArrayList<>();
        rtoValue.add("0");
        rtoDisplayValue.add("Select Rto");

        makeValue = new ArrayList<>();
        makeDisplayValue = new ArrayList<>();
        makeValue.add("0");
        makeDisplayValue.add("Select Make");

        String URL = RestClient.ROOT_URL2+"quotation/"+product_type;


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL",""+ URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);
                        JSONObject data_res = jsonresponse.getJSONObject("data");
                        JSONArray make_arry = data_res.getJSONArray("make_list");
                        JSONArray rto_array = data_res.getJSONArray("rto_list");
                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "MakeMasterArryList", make_arry.toString());
                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "RtoMasterArryList", rto_array.toString());


                        for (int k = 0; k < make_arry.length(); k++) {
                            JSONObject makeObj = make_arry.getJSONObject(k);
                            String make_id = makeObj.getString("id");
                            String make_name = makeObj.getString("make");

                            makeValue.add(make_id);
                            makeDisplayValue.add(make_name.toUpperCase());
                        }

                        ArrayAdapter<String> makeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, makeDisplayValue);
                        Spn_Make.setAdapter(makeAdapter);


                        for (int k = 0; k < rto_array.length(); k++) {
                            JSONObject rtoObj = rto_array.getJSONObject(k);
                            String rto_id = rtoObj.getString("id");
                            String rto_name = rtoObj.getString("label");

                            rtoValue.add(rto_id);
                            rtoDisplayValue.add(rto_name.toUpperCase());
                        }

                        ArrayAdapter<String> rtoAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, rtoDisplayValue);
                        Spn_RTO.setAdapter(rtoAdapter);

                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(), "Something went wrong. Please try again later.");
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("agent_id", StrAgentId);
                    //map.put("business_id","");

                    Log.d("MasterQuoteData",""+map);

                    return map;
                }
            };


            int socketTimeout = 50000; //50 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToastInfo(getApplicationContext(), "Please check Internet Connection");

        }




    }

    private void GetModelList(String selectedMakeId) {

        modelValue = new ArrayList<>();
        modelDisplayValue = new ArrayList<>();
        modelValue.add("0");
        modelDisplayValue.add("Select Model");



        String URL = RestClient.ROOT_URL2+"getmodel";
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


                        JSONArray model_arry = jsonresponse.getJSONArray("data");

                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "ModelMasterArryList", model_arry.toString());


                        for (int k = 0; k < model_arry.length(); k++) {
                            JSONObject modelObj = model_arry.getJSONObject(k);
                            String model_id = modelObj.getString("id");
                            String model_name = modelObj.getString("model");

                            modelValue.add(model_id);
                            modelDisplayValue.add(model_name.toUpperCase());
                        }

                        ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, modelDisplayValue);
                        Spn_Model.setAdapter(modelAdapter);


                    } catch (JSONException e) {
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
                    map.put("make_id", selectedMakeId);
                    map.put("product_type_id", ProductTypeId);


                    Log.d("ModelData",""+map);
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

    private void GetVariantList(String selectedMakeId,String selectedModelId) {

        variantValue = new ArrayList<>();
        variantDisplayValue = new ArrayList<>();
        variantVehicleIdValue = new ArrayList<>();
        variantValue.add("0");
        variantDisplayValue.add("Select Variant");
        variantVehicleIdValue.add("0");

        String URL = RestClient.ROOT_URL2+"getvariant";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);


                        JSONArray variant_arry = jsonresponse.getJSONArray("data");

                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "VariantMasterArryList", variant_arry.toString());


                        for (int k = 0; k < variant_arry.length(); k++) {
                            JSONObject variantObj = variant_arry.getJSONObject(k);
                            String variant_id = variantObj.getString("id");
                            String variant_name = variantObj.getString("variant");
                            String seating_capacity = variantObj.getString("seating_capacity");
                            String cc = variantObj.getString("cc");
                            String vehicle_id = variantObj.getString("vehicle_id");
                            String fuel_cleaned = variantObj.getString("fuel_cleaned");

                            variantValue.add(variant_id);
                            variantVehicleIdValue.add(vehicle_id);
                            variantDisplayValue.add(variant_name.toUpperCase() + " ("+ seating_capacity + "SEATER) ("+fuel_cleaned.toUpperCase()+") ("+cc+" CC)");
                        }

                        ArrayAdapter<String> variantAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, variantDisplayValue);
                        Spn_Variant.setAdapter(variantAdapter);


                    } catch (JSONException e) {
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
                    map.put("make_id", selectedMakeId);
                    map.put("model_id", selectedModelId);
                    map.put("product_type_id", ProductTypeId);

                    Log.d("VariantData",""+map);

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

    private void checkIsBreakInCase() {

        if(StrExpiryDate!=null && !StrExpiryDate.equalsIgnoreCase("")){

            boolean dateExpired = CommonMethods.isDateExpired(StrExpiryDate);

            Log.d("Breaking Case Flag", ""+dateExpired);
        }
    }

    private void setRegistrationDate(int Year, int Month, int Day) {

        Log.d("DispRegistrationDate", Year +" - "+ Month +" - "+ Day);


        registrationDatePickerDialog = new DatePickerDialog(QuotationActivity.this,
                android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

                RegistrationDate =year +"-"+ (monthOfYear + 1) + "-" + dayOfMonth ;
                Date date = null;
                try {
                    date = inputFormat.parse(RegistrationDate);
                    StrRegistrationDate = outputFormat.format(date);

                    EdtRegistrationDate.setText(StrRegistrationDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        }, Year, Month-1, Day);
        long timeInMilliseconds = 0;
        long currentDatetimeInMilliseconds = 0;
        String givenDateString = Year + "-"+(Month)+"-"+Day;
        //String today_date = CommonMethods.DisplayCurrentDate();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       // SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date mDate = sdf.parse(givenDateString);
             timeInMilliseconds = mDate.getTime();

            /*Date mCurrentDate = sdf1.parse(today_date);
            currentDatetimeInMilliseconds = mCurrentDate.getTime();*/
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        registrationDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if(System.currentTimeMillis() > timeInMilliseconds) {
            registrationDatePickerDialog.getDatePicker().setMinDate(timeInMilliseconds);
            registrationDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        }else {
            registrationDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            registrationDatePickerDialog.getDatePicker().setMaxDate(timeInMilliseconds);
        }
        //((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        registrationDatePickerDialog.show();
    }

    private void getManufacturingYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Log.d("Current Year", ""+year);
        manufacturingYear = new ArrayList<>();
        manufacturingYear.add("SELECT MANUFACTURING YEAR");

        if(PolicyType!=null && PolicyType.equalsIgnoreCase("Comprehensive")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = (year-1); k >= ((year-1) - 15); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                Log.d("Im here","--->In New");
                for (int m = year; m >= (year - 1); m--) {
                    Log.d("Year", "New - " + year);
                    int new_year = m;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("ThirdParty")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = (year-1); k >= ((year-1) - 15); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                Log.d("Im here","--->In New");
                for (int m = year; m >= (year - 1); m--) {
                    Log.d("Year", "New - " + year);
                    int new_year = m;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("StandaloneOD")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = (year-1); k >= ((year-1) - 15); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            }
        }




    }

    private void setChangeInOwnserhipVisibility() {
        cpaDisplayValue = new ArrayList<>();
        cpaDisplayValue.add("Select CPA Cover *");

        if(PolicyType!=null && PolicyType.equalsIgnoreCase("Comprehensive")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.VISIBLE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);

                EdtRegistrationDate.setText("");
                EdtRegistrationDate.setEnabled(true);
                EdtRegistrationDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if (registration_year != 0 && registration_month != 0) {
                                setRegistrationDate(registration_year, registration_month, 1);
                            } else {
                                setRegistrationDate(mYear, mMonth, mDay);
                            }

                    }
                });


                cpaDisplayValue.add("0 Year");
                cpaDisplayValue.add("1 Year");

            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                LinearChangeInOwnership.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
                InvoiceLayout.setVisibility(View.VISIBLE);
                LinearNewPolicyWanted.setVisibility(View.VISIBLE);
                SameOwnerLayout.setVisibility(View.GONE);
                EdtRegistrationDate.setText(StrRegistrationDate);
                EdtRegistrationDate.setEnabled(false);

                if(ProductTypeId!=null && ProductTypeId.equalsIgnoreCase("2")) {
                    cpaDisplayValue.add("0 Year");
                    cpaDisplayValue.add("1 Year");
                    cpaDisplayValue.add("5 Year");
                    Rb_1OD5TP.setVisibility(View.VISIBLE);
                    Rb_5OD5TP.setVisibility(View.VISIBLE);
                    Rb_3OD3TP.setVisibility(View.GONE);
                }else if(ProductTypeId!=null && ProductTypeId.equalsIgnoreCase("1")){
                    cpaDisplayValue.add("0 Year");
                    cpaDisplayValue.add("1 Year");
                    cpaDisplayValue.add("3 Year");
                    Rb_1OD5TP.setVisibility(View.VISIBLE);
                    Rb_5OD5TP.setVisibility(View.GONE);
                    Rb_3OD3TP.setVisibility(View.VISIBLE);
                }


            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("ThirdParty")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.GONE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.GONE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);
                EdtRegistrationDate.setText("");
                EdtRegistrationDate.setEnabled(true);
                EdtRegistrationDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if (registration_year != 0 && registration_month != 0) {
                                setRegistrationDate(registration_year, registration_month, 1);
                            } else {
                                setRegistrationDate(mYear, mMonth, mDay);
                            }

                    }
                });

                cpaDisplayValue.add("0 Year");
                cpaDisplayValue.add("1 Year");

            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                LinearChangeInOwnership.setVisibility(View.GONE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.GONE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);
                EdtRegistrationDate.setText(StrRegistrationDate);
                EdtRegistrationDate.setEnabled(false);

                if(ProductTypeId!=null && ProductTypeId.equalsIgnoreCase("2")) {
                    cpaDisplayValue.add("0 Year");
                    cpaDisplayValue.add("1 Year");
                    cpaDisplayValue.add("5 Year");
                }else if(ProductTypeId!=null && ProductTypeId.equalsIgnoreCase("1")){
                    cpaDisplayValue.add("0 Year");
                    cpaDisplayValue.add("1 Year");
                    cpaDisplayValue.add("3 Year");
                }
            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("StandaloneOD")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.VISIBLE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);
                EdtRegistrationDate.setText("");
                EdtRegistrationDate.setEnabled(true);
                EdtRegistrationDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                            if (registration_year != 0 && registration_month != 0) {
                                setRegistrationDate(registration_year, registration_month, 1);
                            } else {
                                setRegistrationDate(mYear, mMonth, mDay);
                            }



                    }
                });

                cpaDisplayValue.add("0 Year");
                cpaDisplayValue.add("1 Year");
            }
        }

        ArrayAdapter<String> cpaAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cpaDisplayValue);
        Spn_CPASelection.setAdapter(cpaAdapter);

    }

    private void setBackGroundColor(String policy_type) {
        if(policy_type!=null && policy_type.equalsIgnoreCase("Comprehensive")){
            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.black));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.white));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.black));
            PolicyType = "Comprehensive";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setChecked(true);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(false);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else if(policy_type!=null && policy_type.equalsIgnoreCase("ThirdParty")){
            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.white));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.black));
            PolicyType = "ThirdParty";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setChecked(true);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(false);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }else if(policy_type!=null && policy_type.equalsIgnoreCase("Standalone")){

            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.white));
            PolicyType = "StandaloneOD";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.GONE);
            Rb_NewPolicy.setChecked(false);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(true);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
        }
    }

    private void scrollDown() {
        Scroll_BelowLayout.post(new Runnable() {
            @Override
            public void run() {
                Scroll_BelowLayout.smoothScrollTo(Scroll_BelowLayout.getScrollY(), Scroll_BelowLayout.getScrollY()
                        + Scroll_BelowLayout.getHeight());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void getInvoicePriceRange() {
        String URL = RestClient.ROOT_URL2+"front/Quotation/getInvoicePriceRange";
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

                        ex_showroom_price = jsonresponse.getString("ex_showroom_price");
                        min_ex_showroom_price = jsonresponse.getInt("min_ex_showroom_price");
                        max_ex_showroom_price = jsonresponse.getInt("max_ex_showroom_price");

                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ExShowroomPrice",ex_showroom_price);

                        if(min_ex_showroom_price!=0 && max_ex_showroom_price!=0) {
                            til_invoice_price.setText("Invoice Price (Min: " + min_ex_showroom_price + "- Max: " + max_ex_showroom_price + ")");
                        }else {
                            til_invoice_price.setText("Invoice Price");
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
                    map.put("variant", SelectedVehicleId);
                    map.put("agent_id", StrAgentId);


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

    private void ManufacturingMonthApi(){

        manufacturingMonthValue = new ArrayList<>();
        manufacturingMonthDisplayValue = new ArrayList<>();
        manufacturingMonthValue.add("0");
        manufacturingMonthDisplayValue.add("Select Manufacturing Month");



        String URL = RestClient.ROOT_URL2+"front/Quotation/setManufacturingMonth";
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

                        JSONArray model_arry = jsonresponse.getJSONArray("data");

                        for (int k = 0; k < model_arry.length(); k++) {
                            JSONObject modelObj = model_arry.getJSONObject(k);
                            String month_id = modelObj.getString("value");
                            String month_name = modelObj.getString("name");

                            manufacturingMonthValue.add(month_id);
                            manufacturingMonthDisplayValue.add(month_name);
                        }

                        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingMonthDisplayValue);
                        Spn_ManufacturingMonth.setAdapter(monthAdapter);


                    } catch (JSONException e) {
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
                    map.put("manufacturing_year", StrManufacturingYear);
                    map.put("selected_manufacturing_month", "");
                    map.put("policy_type", StrPolicyType.toLowerCase());
                    map.put("agent_id", StrAgentId);
                    map.put("access_from", "APP");

                    Log.d("policy_param",""+map);

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

    private void GetNCBListApi(){

        ncbDisplayValue = new ArrayList<>();
        ncbDisplayValue.add("Select NCB %");



        String URL = RestClient.ROOT_URL2+"getNcb";
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

                        JSONArray model_arry = jsonresponse.getJSONArray("data");

                        for (int k = 0; k < model_arry.length(); k++) {
                            JSONObject modelObj = model_arry.getJSONObject(k);
                            String ncb_name = modelObj.getString("val");

                            ncbDisplayValue.add(ncb_name);
                        }

                        ArrayAdapter<String> ncbAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ncbDisplayValue);
                        Spn_NCB_Percent.setAdapter(ncbAdapter);


                    } catch (JSONException e) {
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
                    map.put("age", "18");
                    map.put("agent_id", StrAgentId);
                    map.put("access_from", "APP");


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

    private void API_GET_QUOTE() {
        if(myDialog!=null) {
            myDialog.show();
        }

        if(PolicyType!=null && PolicyType.equalsIgnoreCase("Comprehensive")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                policy_package_type= "comprehensive";
                policy_type_selection = "comprehensive_renewal";
                selected_od_year = "1";
                StrInvoicePrice = "";
                StrExpiryDate = EdtPreviousPolicyExpiryDate.getText().toString();
            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                policy_package_type= "comprehensive";
                policy_type_selection = "comprehensive_new";
                is_changes_in_ownership= "no";
                is_previous_policy = "no";
                previous_yr_policy_type="";
                StrExpiryDate="";
                is_claimed="no";
                previous_policy_ncb = "0";
                previous_policy_nil_dep = "no";
            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("ThirdParty")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                policy_package_type= "thirdparty";
                policy_type_selection = "thirdparty_renewal";
                selected_od_year = "1";
                StrExpiryDate = EdtPreviousPolicyExpiryDate.getText().toString();
            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                policy_package_type= "thirdparty";
                policy_type_selection = "thirdparty_new";
                is_changes_in_ownership= "no";
                is_previous_policy = "no";
                previous_yr_policy_type="";
                StrExpiryDate="";
                is_claimed="no";
                previous_policy_ncb = "0";
                previous_policy_nil_dep = "no";

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("StandaloneOD")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                policy_package_type= "standalone od";
                policy_type_selection = "standalone_renewal";
                selected_od_year = "1";

            }
        }

        if(Spn_NCB_Percent.getSelectedItemPosition()>0) {
            previous_policy_ncb = Spn_NCB_Percent.getSelectedItem().toString();
        }else {
            previous_policy_ncb = "0";
        }

        if(StrRegistration_monthId!=null && StrRegistration_monthId.length()==1){
            manufacturing_date = "01/0"+StrRegistration_monthId+"/"+StrManufacturingYear;
        }else {
            manufacturing_date = "01/"+StrRegistration_monthId+"/"+StrManufacturingYear;
        }

            String ODDiscountPer = Spn_ODDiscount.getSelectedItem().toString();
            if(ODDiscountPer.equalsIgnoreCase("MAX DISCOUNT")){
                selected_od_discount = "max";

            }else {
                selected_od_discount = ODDiscountPer.replace("%","");
            }


        StrInvoicePrice = EdtInvoicePrice.getText().toString();

        String make_arry =  UtilitySharedPreferences.getPrefs(getApplicationContext(), "MakeMasterArryList");
        String rto_array = UtilitySharedPreferences.getPrefs(getApplicationContext(), "RtoMasterArryList");
        String model_arry = UtilitySharedPreferences.getPrefs(getApplicationContext(), "ModelMasterArryList");
        String variant_arry = UtilitySharedPreferences.getPrefs(getApplicationContext(), "VariantMasterArryList");


        if(rto_array!=null){

            try {
                JSONArray jsonArray = new JSONArray(rto_array);
                for(int k =0; k<jsonArray.length();k++){
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    String id = jsonObject.getString("id");
                    if(SelectedRtoId.equalsIgnoreCase(id)){
                        StrRtoCode = jsonObject.getString("code");
                        StrRtoLabel = jsonObject.getString("label");
                        StrRtoCity = jsonObject.getString("rto_city");
                        StrRtoCityId = jsonObject.getString("city_id");
                        StrRtoStateId = jsonObject.getString("state_id");
                        StrRtoZoneTypeId = jsonObject.getString("zone_type_id");

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        if(make_arry!=null){

            try {
                JSONArray jsonArray = new JSONArray(make_arry);
                for(int k =0; k<jsonArray.length();k++){
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    String id = jsonObject.getString("id");
                    if(SelectedMakeId.equalsIgnoreCase(id)){
                        make_cleaned = jsonObject.getString("make_cleaned");
                        StrSelectedMake = jsonObject.getString("make");


                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        if(model_arry!=null){
            try {
                JSONArray jsonArray = new JSONArray(model_arry);
                for(int k =0; k<jsonArray.length();k++){
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    String id = jsonObject.getString("id");
                    if(SelectedModelId.equalsIgnoreCase(id)){
                        model_cleaned = jsonObject.getString("model_cleaned");
                        SelectedModel = jsonObject.getString("model");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        if(variant_arry!=null){
            try {
                JSONArray jsonArray = new JSONArray(variant_arry);
                for(int k =0; k<jsonArray.length();k++){
                    JSONObject jsonObject = jsonArray.getJSONObject(k);
                    String id = jsonObject.getString("id");
                    if(SelectedVaraintId.equalsIgnoreCase(id)){
                        StrVehicleId = jsonObject.getString("vehicle_id");
                        StrVariantCleaned = jsonObject.getString("variant_cleaned");
                        StrVariant = jsonObject.getString("variant");
                        StrSeatingCapacity = jsonObject.getString("seating_capacity");
                        StrCC = jsonObject.getString("cc");
                        if(jsonObject.getString("gvw")!=null &&
                                !jsonObject.getString("gvw").equalsIgnoreCase("") &&
                                !jsonObject.getString("gvw").equalsIgnoreCase("null")) {
                            StrGVW = jsonObject.getString("gvw");
                        }else {
                            StrGVW="";
                        }
                        StrFuelType = jsonObject.getString("fuel");
                        StrFuelTypeCleaned = jsonObject.getString("fuel_cleaned");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        String URL = RestClient.ROOT_URL2+"getquote";
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
                        JSONObject mpn_data = data_obj.getJSONObject("mpn_data");
                        JSONObject user_action_data = data_obj.getJSONObject("user_action_data");


                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"MpnData",mpn_data.toString());
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"UserActionData",user_action_data.toString());
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"PolicyHolder",StrPolicyHolder.toLowerCase());

                        Intent intent = new Intent(getApplicationContext(),IcListingQuoteScreen.class);
                        startActivity(intent);
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
                    map.put("vehicle_id", StrVehicleId);
                    map.put("vehicle_fuel", StrFuelType.toLowerCase());
                    map.put("policy_type", StrPolicyType.toLowerCase());
                    map.put("policy_package_type", policy_package_type);
                    map.put("policy_type_selection", policy_type_selection);
                    map.put("rto_id", SelectedRtoId);
                    map.put("make_id", SelectedMakeId);
                    map.put("model_id", SelectedModelId);
                    map.put("variant_id", SelectedVaraintId);
                    map.put("manufacturing_year", String.valueOf(registration_year));
                    map.put("manufacturing_date", manufacturing_date);
                    map.put("manufacturing_month", StrRegistration_monthId);
                    map.put("purchase_invoice_date", StrRegistrationDate);
                    map.put("policy_holder_type", StrPolicyHolder.toLowerCase());
                    map.put("is_changes_in_ownership", is_changes_in_ownership);
                    map.put("is_previous_policy", is_previous_policy);
                    map.put("previous_yr_policy_type", previous_yr_policy_type);
                    map.put("previous_policy_expiry_date", StrExpiryDate);
                    map.put("is_claimed", is_claimed);
                    map.put("previous_policy_ncb", previous_policy_ncb);
                    map.put("selected_od_discount", selected_od_discount);
                    map.put("invoice_price", StrInvoicePrice);
                    map.put("selected_od_year", selected_od_year);
                    map.put("have_motor_license", have_motor_license);
                    map.put("have_motor_policy", have_motor_policy);
                    map.put("have_pa_policy", have_pa_policy);
                    map.put("other_pa_policy", other_pa_policy);
                    map.put("selected_pa_year",selected_pa_year);
                    map.put("product_type_id", ProductTypeId);
                    map.put("previous_policy_nil_dep", previous_policy_nil_dep);
                    map.put("product_type", product_type);
                    map.put("rto_code", StrRtoCode);
                    map.put("rto_label", StrRtoLabel);
                    map.put("rto_city", StrRtoCity);
                    map.put("rto_zone_type_id", StrRtoZoneTypeId);
                    map.put("rto_state_id", StrRtoStateId);
                    map.put("rto_city_id", StrRtoCityId);
                    map.put("cc", StrCC);
                    map.put("ex_showroom_price", ex_showroom_price);
                    map.put("seating_capacity", StrSeatingCapacity);
                    map.put("gvw", StrGVW);
                    map.put("make_cleaned", make_cleaned.toLowerCase());
                    map.put("make", StrSelectedMake.toLowerCase());
                    map.put("model_cleaned", model_cleaned.toLowerCase());
                    map.put("model", SelectedModel.toLowerCase());
                    map.put("variant_cleaned", StrVariantCleaned.toLowerCase());
                    map.put("variant", StrVariant.toLowerCase());
                    map.put("fuel_cleaned", StrFuelTypeCleaned);
                    map.put("fuel", StrFuelType);
                    Log.d("QuotationData",""+map);
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

    private boolean validateFields() {

        boolean result = true;

        if(!MyValidator.isValidSearchableSpinner(Spn_RTO)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select RTO");
        }

        if(!MyValidator.isValidSearchableSpinner(Spn_Make)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Make");
        }

        if(!MyValidator.isValidSearchableSpinner(Spn_Model)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Model");
        }

        if(!MyValidator.isValidSearchableSpinner(Spn_Variant)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Variant");
        }

        if(!MyValidator.isValidSpinner(Spn_ManufacturingYear)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Manufacturing Year");
        }

        if(!MyValidator.isValidSpinner(Spn_ManufacturingMonth)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Manufacturing Month");
        }

        if(!MyValidator.isValidField(EdtRegistrationDate)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Valid Registration Date");
        }

        if(!MyValidator.isValidSpinner(Spn_PolicyHolder)){
            result = false;
            CommonMethods.DisplayToastError(getApplicationContext(),"Select Policy Holder Type");
        }

       /* if(LayoutODDisount.getVisibility()==View.VISIBLE) {
            if (!MyValidator.isValidSpinner(Spn_ODDiscount)) {
                result = false;
                CommonMethods.DisplayToastError(getApplicationContext(), "Select Policy OD Discount");
            }
        }*/



        if(StrPolicyType.equalsIgnoreCase("New")){
            if(InvoiceLayout.getVisibility()==View.VISIBLE) {



                if(EdtInvoicePrice.getText().toString()!=null && EdtInvoicePrice.getText().toString().length()>0){

                    String InvoicePrice = EdtInvoicePrice.getText().toString();
                    int invoice_price = Integer.valueOf(InvoicePrice);
                    if(min_ex_showroom_price!=0 && max_ex_showroom_price!=0){
                        if(invoice_price < min_ex_showroom_price){
                            result = false;
                            CommonMethods.DisplayToastError(getApplicationContext(),"Min: "+min_ex_showroom_price +" & Max: "+max_ex_showroom_price);
                        }

                        if(invoice_price > max_ex_showroom_price){
                            result = false;
                            CommonMethods.DisplayToastError(getApplicationContext(),"Min: "+min_ex_showroom_price +" & Max: "+max_ex_showroom_price);
                        }
                    }

                }

            }

            if(LinearNewPolicyWanted.getVisibility()==View.VISIBLE) {
                if (!MyValidator.isValidRadioGroup(RG_NewPolicyRequired)) {
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(), "Select New Policy Required");
                }
            }
        }else if (StrPolicyType.equalsIgnoreCase("Renew")){
            if(LinearChangeInOwnership.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidRadioGroup(RG_ChangeInOwnership)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select Is there a change in ownership in last one year?");
                }
            }

            if(SameOwnerLayout.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidRadioGroup(RG_PreviousPolicy)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select Do you have previous policy?");
                }
            }

            if(LinearPreviousPolicyType.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidRadioGroup(RG_PreviousPolicyType)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select previous policy type");
                }
            }

            if(LinearExpiryDate.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidField(EdtPreviousPolicyExpiryDate)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select Previous Policy Expiry Date");
                }
            }

            if(LinearHaveMadeClaim.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidRadioGroup(RG_HaveMadeClaim)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select Have you made a claim?");
                }
            }

            if(LinearNCB_Per.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidSpinner(Spn_NCB_Percent)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select NCB Percent");
                }
            }

            /*if(LayoutNilDept.getVisibility()==View.VISIBLE){
                if(!MyValidator.isValidCheckBox(ChkNilDept)){
                    result = false;
                    CommonMethods.DisplayToastError(getApplicationContext(),"Select Nil Dept Policy or Not");
                }
            }*/




        }


        if(IndividualPolicyHolderLayout.getVisibility()==View.VISIBLE){
            if(!MyValidator.isValidSpinner(Spn_CPASelection)){
                result = false;
                CommonMethods.DisplayToastError(getApplicationContext(),"Select CPA");
            }
        }

        /*if(LinearValidMotorPolicy.getVisibility()==View.VISIBLE){
            if(!MyValidator.isValidRadioGroup(RG_AnotherPolicy)){
                result = false;
                CommonMethods.DisplayToastError(getApplicationContext(),"Select Do you have another Motor Insurance Policy with 15 lakhs PA owner cover ? ");
            }

        }

        if(LinearAnotherPA_Policy.getVisibility()==View.VISIBLE){
            if(!MyValidator.isValidRadioGroup(RG_AnotherPA_Policy)){
                result = false;
                CommonMethods.DisplayToastError(getApplicationContext(),"Select Do you have PA Policy of 15 lakhs and above ? ");
            }

        }

        if(LinearPA_Cover.getVisibility()==View.VISIBLE){
            if(!MyValidator.isValidRadioGroup(RG_PA_Cover)){
                result = false;
                CommonMethods.DisplayToastError(getApplicationContext(),"Select PA COVER FOR  ");
            }

        }*/

        return result;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if(id== R.id.Spn_RTO){

            String StrRtoSelected = Spn_RTO.getSelectedItem().toString();

            int pos_rto = Spn_RTO.getSelectedItemPosition();
            SelectedRtoId = rtoValue.get(pos_rto).toString();


        }else if(id== R.id.Spn_Make){

            StrSelectedMake = Spn_Make.getSelectedItem().toString().toLowerCase();
            int pos_make = Spn_Make.getSelectedItemPosition();
            SelectedMakeId = makeValue.get(pos_make).toString();
            Log.d("StrSelectedMake",StrSelectedMake);
            Log.d("SelectedMakeId",SelectedMakeId);
            GetModelList(SelectedMakeId);


        }else if(id== R.id.Spn_Model){

            SelectedModel = Spn_Model.getSelectedItem().toString().toLowerCase();
            int pos_model = Spn_Model.getSelectedItemPosition();
            SelectedModelId = modelValue.get(pos_model).toString();

            Log.d("SelectedModel",SelectedModel);
            Log.d("SelectedModelId",SelectedModelId);
            GetVariantList(SelectedMakeId,SelectedModelId);


        }else if(id== R.id.Spn_Variant){

            String SelectedVariant = Spn_Variant.getSelectedItem().toString().toLowerCase();
            int pos_varaint = Spn_Variant.getSelectedItemPosition();
            SelectedVaraintId = variantValue.get(pos_varaint).toString();
            SelectedVehicleId = variantVehicleIdValue.get(pos_varaint).toString();
            Log.d("SelectedVariant",SelectedVariant);
            Log.d("SelectedVehicleId",SelectedVehicleId);
            getInvoicePriceRange();

        }else if (id == R.id.Spn_ManufacturingYear) {
            StrManufacturingYear = Spn_ManufacturingYear.getSelectedItem().toString().trim();
            if (Spn_ManufacturingYear.getSelectedItemPosition() > 0) {
                registration_year = Integer.valueOf(StrManufacturingYear);
                Log.d("StrManufacturingYear",StrManufacturingYear);
                Log.d("registration_year",""+registration_year);
                ManufacturingMonthApi();
                GetNCBListApi();
            }
        } else if (id == R.id.Spn_ManufacturingMonth) {
            StrManufacturingMonth = Spn_ManufacturingMonth.getSelectedItem().toString().trim();

                if (Spn_ManufacturingMonth.getSelectedItemPosition() > 0) {
                    int pos_month = Spn_ManufacturingMonth.getSelectedItemPosition();

                    if(manufacturingMonthValue.get(pos_month)!=null) {
                        StrRegistration_monthId = manufacturingMonthValue.get(pos_month).toString();
                        registration_month = Integer.valueOf(StrRegistration_monthId);
                    }
                    if(StrPolicyType.equalsIgnoreCase("Renew")){
                        if(registration_year!=0 && registration_month!=0) {
                            setRegistrationDate(registration_year, registration_month, 1);
                        }else {
                            setRegistrationDate(mYear, mMonth, mDay);
                        }
                    }else {
                        StrRegistrationDate = CommonMethods.DisplayCurrentDate();
                        EdtRegistrationDate.setText(StrRegistrationDate);

                    }


                 }
            } else if (id == R.id.Spn_CPASelection) {
                String Selected_cpa = Spn_CPASelection.getSelectedItem().toString().trim();
                if(Selected_cpa!=null && !Selected_cpa.equalsIgnoreCase("")){
                    if(Selected_cpa.equalsIgnoreCase("0 Year")){
                        selected_pa_year = "0";
                        LayoutReasonOptingOutCpa.setVisibility(View.VISIBLE);
                    }else if(Selected_cpa.equalsIgnoreCase("1 Year")){
                        selected_pa_year = "1";
                        LayoutReasonOptingOutCpa.setVisibility(View.GONE);
                    }else if(Selected_cpa.equalsIgnoreCase("5 Year")){
                        selected_pa_year = "5";
                        LayoutReasonOptingOutCpa.setVisibility(View.GONE);
                    }else if(Selected_cpa.equalsIgnoreCase("3 Year")){
                        selected_pa_year = "3";
                        LayoutReasonOptingOutCpa.setVisibility(View.GONE);
                    }
                    scrollDown();
                }
            }else if (id == R.id.Spn_ReasonOptingOutCPA) {
                String Selected_ReasonOptingOutCPA = Spn_ReasonOptingOutCPA.getSelectedItem().toString().trim();
                if (Selected_ReasonOptingOutCPA != null && !Selected_ReasonOptingOutCPA.equalsIgnoreCase("")) {
                        if (Selected_ReasonOptingOutCPA.equalsIgnoreCase("Do not have valid license")) {
                            have_motor_license = "no";
                            have_motor_policy = "no";
                            have_pa_policy = "no";
                            other_pa_policy = "no";
                            selected_pa_year = "0";
                        } else if (Selected_ReasonOptingOutCPA.equalsIgnoreCase("Have another Motor Insurance Policy with 15 lakhs PA owner cover")) {
                            have_motor_license = "yes";
                            have_motor_policy = "yes";
                            have_pa_policy = "no";
                            other_pa_policy = "no";
                            selected_pa_year = "0";
                        } else if (Selected_ReasonOptingOutCPA.equalsIgnoreCase("Have PA Policy of 15 lakhs and above")) {
                            have_motor_license = "yes";
                            have_motor_policy = "no";
                            have_pa_policy = "yes";
                            other_pa_policy = "no";
                            selected_pa_year = "0";

                        } else if (Selected_ReasonOptingOutCPA.equalsIgnoreCase("Have Other PA Policy")) {
                            have_motor_license = "yes";
                            have_motor_policy = "no";
                            have_pa_policy = "no";
                            other_pa_policy = "yes";
                            selected_pa_year = "0";
                    }
                }
            }
        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

