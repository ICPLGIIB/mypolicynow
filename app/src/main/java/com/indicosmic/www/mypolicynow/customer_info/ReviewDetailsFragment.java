package com.indicosmic.www.mypolicynow.customer_info;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.mypolicynow_activities.ProposalPdfActivity;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.utils.CommonMethods.ucFirst;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL2;

public class ReviewDetailsFragment extends Fragment implements BlockingStep {


    View rootView;
    Context context;
    ImageView iv_Ic;
    TextView tv_IC_Name,tv_policy_start_date,tv_policy_end_date,tv_IDV_CoverAmt,tv_OwnerName,tv_OwnerEmail,tv_OwnerPhone,tv_NomineeName,
            tv_NomineeAge,tv_NomineeRelation,tv_AppointeeName,tv_AppointeeAge,tv_AppointeeRelation,tv_RegistrationDate,tv_EngineNo,tv_ChassisNo,
            tv_Address1,tv_Address2,tv_City,tv_State,tv_Pincode;
    LinearLayout LayoutAppointeeDetails;
    String StrAgentId="",StrMpnData="",StrUserActionData="",StrImageUrl="",SelectedIcId="";
    String StrRegistrationDate="",Date_of_born = "", StrPolicyType = "";
    String StrSelectedSalutation,StrSelectedGender,StrSelectedMaritalStatus,StrFirstName,StrMiddleName, StrLastName, StrEmailAddress, StrPan, StrAadharCard,StrMobileNo="",StrGstInNumber="";
    String Str_NomineeSalutation,Str_NomineeRelationship,Str_AppointeeSalutation,Str_AppointeeRelationship;
    String Str_NomineeFirstName,Str_NomineeMiddleName,Str_NomineeLastName,Str_NomineeAge;
    String Str_AppointeeFirstName,Str_AppointeeMiddleName,Str_AppointeeLastName,Str_AppointeeAge;
    String Str_Address1,Str_Address2,Str_Pincode,Str_State,Str_City,Str_CityId,Str_StateId;
    String StrPreviousPolicyNo,StrPreviousPolicyIC,StrRtoStateCode,StrRtoCityCode,StrRtoZoneCode,StrVehicleNo,StrEngineNo,StrChassisNo,StrVehicleColor,StrAgreement,
            StrBankName,StrBankId;
    JSONObject mpn_dataObj = new JSONObject();
    ProgressDialog myDialog;

    public ReviewDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        rootView = inflater.inflate(R.layout.fragment_review_details_info, container, false);

        context = getContext();

        init();


        return rootView;

    }

    private void init() {

        iv_Ic = (ImageView)rootView.findViewById(R.id.iv_Ic);
        tv_IC_Name  = (TextView)rootView.findViewById(R.id.tv_IC_Name);
        tv_policy_start_date= (TextView)rootView.findViewById(R.id.tv_policy_start_date);
        tv_policy_end_date= (TextView)rootView.findViewById(R.id.tv_policy_end_date);
        tv_IDV_CoverAmt= (TextView)rootView.findViewById(R.id.tv_IDV_CoverAmt);

        tv_OwnerName= (TextView)rootView.findViewById(R.id.tv_OwnerName);
        tv_OwnerEmail= (TextView)rootView.findViewById(R.id.tv_OwnerEmail);
        tv_OwnerPhone= (TextView)rootView.findViewById(R.id.tv_OwnerPhone);

        tv_NomineeName= (TextView)rootView.findViewById(R.id.tv_NomineeName);
        tv_NomineeAge  = (TextView)rootView.findViewById(R.id.tv_NomineeAge);
        tv_NomineeRelation= (TextView)rootView.findViewById(R.id.tv_NomineeRelation);

        tv_AppointeeName= (TextView)rootView.findViewById(R.id.tv_AppointeeName);
        tv_AppointeeAge= (TextView)rootView.findViewById(R.id.tv_AppointeeAge);
        tv_AppointeeRelation= (TextView)rootView.findViewById(R.id.tv_AppointeeRelation);

        tv_RegistrationDate= (TextView)rootView.findViewById(R.id.tv_RegistrationDate);
        tv_EngineNo= (TextView)rootView.findViewById(R.id.tv_EngineNo);
        tv_ChassisNo = (TextView)rootView.findViewById(R.id.tv_ChassisNo);

        tv_Address1  = (TextView)rootView.findViewById(R.id.tv_Address1);
        tv_Address2= (TextView)rootView.findViewById(R.id.tv_Address2);
        tv_City= (TextView)rootView.findViewById(R.id.tv_City);
        tv_State= (TextView)rootView.findViewById(R.id.tv_State);
        tv_Pincode= (TextView)rootView.findViewById(R.id.tv_Pincode);

         LayoutAppointeeDetails = (LinearLayout)rootView.findViewById(R.id.LayoutAppointeeDetails);
            myDialog = new ProgressDialog(context);
            myDialog.setMessage("Please wait...");
            myDialog.setCancelable(true);
            myDialog.setCanceledOnTouchOutside(true);
         setDataToView();
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataToView();
    }

    private void setDataToView() {

        JSONObject vehicle_ownerObj = new JSONObject();
        JSONObject nominee_detailsObj = new JSONObject();
        JSONObject appointee_detailsObj = new JSONObject();
        JSONObject address_detailObj = new JSONObject();
        JSONObject vehicle_detailObj = new JSONObject();
        JSONObject previous_policyObj = new JSONObject();

        try{
            StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");
            StrUserActionData = UtilitySharedPreferences.getPrefs(context,"UserActionData");
            SelectedIcId = UtilitySharedPreferences.getPrefs(context,"SelectedIcId");
            String StrOwnerDetails = UtilitySharedPreferences.getPrefs(context,"vehicle_ownerObj");
            String StrNomineeDetails = UtilitySharedPreferences.getPrefs(context,"nominee_detailsObj");
            String StrAppointeeDetails = UtilitySharedPreferences.getPrefs(context,"appointee_detailsObj");
            String StrAddressDetails = UtilitySharedPreferences.getPrefs(context,"address_detailObj");
            String StrVehicleDetails = UtilitySharedPreferences.getPrefs(context,"vehicle_detailObj");
            String StrPreviousPolicyDetails = UtilitySharedPreferences.getPrefs(context,"previous_policyObj");

            JSONObject mpnObj = new JSONObject(StrMpnData);
            JSONObject icQuoteObj = mpnObj.getJSONObject("ic_quote");
            JSONObject icObj = icQuoteObj.getJSONObject("ic");
            String ic_id = icObj.getString("id");
            String ic_name = icObj.getString("code");
            String ic_logo = icObj.getString("logo");
            String total_vehicle_idv = mpnObj.getString("total_vehicle_idv");

            JSONObject policy_start_date_arr = mpnObj.getJSONObject("policy_start_date_arr");
            String str_start_date = policy_start_date_arr.getString("date");

            JSONObject policy_end_date_arr = mpnObj.getJSONObject("policy_end_date_arr");
            String str_end_date = policy_end_date_arr.getString("date");


            StrImageUrl = RestClient.ROOT_IC_IMAGE_URL+ic_logo;

            Glide.with(getActivity()).load(StrImageUrl).into(iv_Ic);

            tv_IC_Name.setText(ic_name.toUpperCase());
            tv_IDV_CoverAmt.setText("\u20B9 "+total_vehicle_idv);
            tv_policy_start_date.setText(str_start_date);
            tv_policy_end_date.setText(str_end_date);

            JSONObject user_action_dataObj = new JSONObject(StrUserActionData);
            StrRegistrationDate = user_action_dataObj.getString("purchase_invoice_date");
            StrPolicyType = user_action_dataObj.getString("policy_type");

            tv_RegistrationDate.setText(StrRegistrationDate);

            vehicle_ownerObj = new JSONObject(StrOwnerDetails);
            nominee_detailsObj = new JSONObject(StrNomineeDetails);
            appointee_detailsObj = new JSONObject(StrAppointeeDetails);
            address_detailObj = new JSONObject(StrAddressDetails);
            vehicle_detailObj = new JSONObject(StrVehicleDetails);
            previous_policyObj = new JSONObject(StrPreviousPolicyDetails);

            Log.d("OwnerDetails",""+vehicle_ownerObj.toString());
            Log.d("NomineeDetails",""+nominee_detailsObj.toString());
            Log.d("appointee_details",""+appointee_detailsObj.toString());
            Log.d("address_detail",""+address_detailObj.toString());
            Log.d("VehicleDetails",""+vehicle_detailObj.toString());
            Log.d("PreviousPolicyDetails",""+previous_policyObj.toString());


            StrEmailAddress = vehicle_ownerObj.getString("email");
            StrMobileNo = vehicle_ownerObj.getString("mobile_no");
            StrSelectedSalutation = vehicle_ownerObj.getString("salutaion");
            StrFirstName = vehicle_ownerObj.getString("first_name");
            StrMiddleName = vehicle_ownerObj.getString("middle_name");
            StrLastName = vehicle_ownerObj.getString("last_name");
            Date_of_born =  vehicle_ownerObj.getString("dob");
            StrGstInNumber = vehicle_ownerObj.getString("individual_gst_no");
            StrPan = vehicle_ownerObj.getString("pancard");
            StrAadharCard = vehicle_ownerObj.getString("aadharcard");
            StrSelectedMaritalStatus =  vehicle_ownerObj.getString("marital_status");
            StrSelectedGender = vehicle_ownerObj.getString("gender");

            String OwnerFullName = StrSelectedSalutation + " " + StrFirstName + " "+StrMiddleName +" " +StrLastName;
            tv_OwnerName.setText(OwnerFullName.toUpperCase());
            tv_OwnerEmail.setText(StrEmailAddress);
            tv_OwnerPhone.setText(StrMobileNo);


            Str_NomineeSalutation = nominee_detailsObj.getString("nominee_salutaion");
            Str_NomineeFirstName = nominee_detailsObj.getString("nominee_first_name");
            Str_NomineeMiddleName = nominee_detailsObj.getString("nominee_middle_name");
            Str_NomineeLastName = nominee_detailsObj.getString("nominee_last_name");
            Str_NomineeRelationship = nominee_detailsObj.getString("nominee_relationship");
            Str_NomineeAge = nominee_detailsObj.getString("nominee_age");
            Str_AppointeeSalutation = appointee_detailsObj.getString("appointee_salutaion");
            Str_AppointeeFirstName = appointee_detailsObj.getString("appointee_first_name");
            Str_AppointeeMiddleName = appointee_detailsObj.getString("appointee_middle_name");
            Str_AppointeeLastName = appointee_detailsObj.getString("appointee_last_name");
            Str_AppointeeRelationship = appointee_detailsObj.getString("appointee_relationship");
            Str_AppointeeAge = appointee_detailsObj.getString("appointee_age");

            String NomineeFullName = Str_NomineeSalutation + " " + Str_NomineeFirstName + " "+Str_NomineeMiddleName +" " +Str_NomineeLastName;
            tv_NomineeName.setText(NomineeFullName.toUpperCase());
            tv_NomineeRelation.setText(ucFirst(Str_NomineeRelationship));

            if(Str_NomineeAge!=null && !Str_NomineeAge.equalsIgnoreCase("")){
                tv_NomineeAge.setText(Str_NomineeAge);
                int nominee_age = Integer.valueOf(Str_NomineeAge);
                if(nominee_age < 18){
                    LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                    String AppointeeFullName = Str_AppointeeSalutation + " " + Str_AppointeeFirstName + " "+Str_AppointeeMiddleName +" " +Str_AppointeeLastName;
                    tv_AppointeeName.setText(AppointeeFullName.toUpperCase());
                    tv_AppointeeAge.setText(Str_AppointeeAge);
                    tv_AppointeeRelation.setText(ucFirst(Str_AppointeeRelationship));
                }else {
                    LayoutAppointeeDetails.setVisibility(View.GONE);
                }
            }


            Str_Address1=  address_detailObj.getString("address1");
            Str_Address2 = address_detailObj.getString("address2");
            Str_Pincode = address_detailObj.getString("pincode");
            Str_State = UtilitySharedPreferences.getPrefs(context,"AddressState");
            Str_City = UtilitySharedPreferences.getPrefs(context,"AddressCity");
            Str_StateId = address_detailObj.getString("state_id");
            Str_CityId = address_detailObj.getString("city_id");

            tv_Address1.setText(Str_Address1);
            tv_Address2.setText(Str_Address2);
            tv_City.setText(Str_City);
            tv_State.setText(Str_State);
            tv_Pincode.setText(Str_Pincode);

            StrRtoStateCode = vehicle_detailObj.getString( "veh1");
            StrRtoCityCode = vehicle_detailObj.getString( "veh2");
            StrRtoZoneCode = vehicle_detailObj.getString( "veh3");
            StrVehicleNo = vehicle_detailObj.getString( "veh4");
            StrEngineNo = vehicle_detailObj.getString( "engine_no");
            StrChassisNo = vehicle_detailObj.getString( "chassis_no");
            StrVehicleColor = vehicle_detailObj.getString( "car_color");
            StrAgreement = vehicle_detailObj.getString( "agreement_type");
            StrBankName = vehicle_detailObj.getString( "agreement_bank");

            StrPreviousPolicyNo = previous_policyObj.getString( "pre_policy_no");
            StrPreviousPolicyIC = previous_policyObj.getString( "pre_insurance");


            if(StrEngineNo!=null && !StrEngineNo.equalsIgnoreCase("")){
                tv_EngineNo.setText(StrEngineNo.toUpperCase());
            }

            if(StrChassisNo!=null && !StrChassisNo.equalsIgnoreCase("")){
                tv_ChassisNo.setText(StrChassisNo.toUpperCase());
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        JSONObject customerObj = new JSONObject();

        JSONObject vehicle_ownerObj = new JSONObject();
        JSONObject nominee_detailsObj = new JSONObject();
        JSONObject appointee_detailsObj = new JSONObject();
        JSONObject address_detailObj = new JSONObject();
        JSONObject vehicle_detailObj = new JSONObject();
        JSONObject previous_policyObj = new JSONObject();

        String cityObjStr = UtilitySharedPreferences.getPrefs(context,"CustomerCityArry");
        String stateObjStr = UtilitySharedPreferences.getPrefs(context,"CustomerStateArry");
        StrMpnData =  UtilitySharedPreferences.getPrefs(context,"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(context,"UserActionData");
        String StrOwnerDetails = UtilitySharedPreferences.getPrefs(context,"vehicle_ownerObj");
        String StrNomineeDetails = UtilitySharedPreferences.getPrefs(context,"nominee_detailsObj");
        String StrAppointeeDetails = UtilitySharedPreferences.getPrefs(context,"appointee_detailsObj");
        String StrAddressDetails = UtilitySharedPreferences.getPrefs(context,"address_detailObj");
        String StrVehicleDetails = UtilitySharedPreferences.getPrefs(context,"vehicle_detailObj");
        String StrPreviousPolicyDetails = UtilitySharedPreferences.getPrefs(context,"previous_policyObj");


        try {

            vehicle_ownerObj = new JSONObject(StrOwnerDetails);
            nominee_detailsObj = new JSONObject(StrNomineeDetails);
            appointee_detailsObj = new JSONObject(StrAppointeeDetails);
            address_detailObj = new JSONObject(StrAddressDetails);
            vehicle_detailObj = new JSONObject(StrVehicleDetails);
            previous_policyObj = new JSONObject(StrPreviousPolicyDetails);

            /*
            vehicle_ownerObj.put("email",StrEmailAddress);
            vehicle_ownerObj.put("mobile_no",StrMobileNo);
            vehicle_ownerObj.put("salutaion",StrSelectedSalutation);
            vehicle_ownerObj.put("first_name",StrFirstName);
            vehicle_ownerObj.put("middle_name",StrMiddleName);
            vehicle_ownerObj.put("last_name",StrLastName);
            vehicle_ownerObj.put("dob",Date_of_born);
            vehicle_ownerObj.put("individual_gst_no",StrGstInNumber);
            vehicle_ownerObj.put("pancard",StrPan.toUpperCase());
            vehicle_ownerObj.put("aadharcard",StrAadharCard);
            vehicle_ownerObj.put("marital_status",StrSelectedMaritalStatus.toLowerCase());
            vehicle_ownerObj.put("gender",StrSelectedGender.toLowerCase());

            nominee_detailsObj.put("nominee_salutaion",Str_NomineeSalutation);
            nominee_detailsObj.put("nominee_first_name",Str_NomineeFirstName);
            nominee_detailsObj.put("nominee_middle_name",Str_NomineeMiddleName);
            nominee_detailsObj.put("nominee_last_name",Str_NomineeLastName);
            nominee_detailsObj.put("nominee_relationship",Str_NomineeRelationship.toLowerCase());
            nominee_detailsObj.put("nominee_age",Str_NomineeAge);

            appointee_detailsObj.put("appointee_salutaion",Str_AppointeeSalutation);
            appointee_detailsObj.put("appointee_first_name",Str_AppointeeFirstName);
            appointee_detailsObj.put("appointee_middle_name",Str_AppointeeMiddleName);
            appointee_detailsObj.put("appointee_last_name",Str_AppointeeLastName);
            appointee_detailsObj.put("appointee_relationship",Str_AppointeeRelationship.toLowerCase());
            appointee_detailsObj.put("appointee_age",Str_AppointeeAge);

            JSONObject cityObj = new JSONObject(cityObjStr);
            JSONObject stateObj = new JSONObject(stateObjStr);

            address_detailObj.put("address1",Str_Address1);
            address_detailObj.put("address2",Str_Address2);
            address_detailObj.put("pincode",Str_Pincode);
            address_detailObj.put("state_id",Str_StateId);
            address_detailObj.put("state",stateObj);
            address_detailObj.put("city_id",Str_CityId);
            address_detailObj.put("city",cityObj);




            previous_policyObj.put("pre_policy_no",StrPreviousPolicyNo);
            previous_policyObj.put("pre_insurance",StrPreviousPolicyIC);


            vehicle_detailObj.put("veh1",StrRtoStateCode);
            vehicle_detailObj.put("veh2",StrRtoCityCode);
            vehicle_detailObj.put("veh3",StrRtoZoneCode);
            vehicle_detailObj.put("veh4", StrVehicleNo);
            vehicle_detailObj.put("engine_no", StrEngineNo.toUpperCase());
            vehicle_detailObj.put("chassis_no", StrChassisNo.toUpperCase());
            vehicle_detailObj.put("car_color", StrVehicleColor.toUpperCase());
            vehicle_detailObj.put("reg_date", StrRegistrationDate);
            vehicle_detailObj.put("agreement_type", StrAgreement.toLowerCase());
            vehicle_detailObj.put("agreement_bank", StrBankId);
*/
            customerObj.put("vehicle_owner",vehicle_ownerObj);
            customerObj.put("nominee_details",nominee_detailsObj);

            if(Str_NomineeAge!=null && !Str_NomineeAge.equalsIgnoreCase("")){
                tv_NomineeAge.setText(Str_NomineeAge);
                int nominee_age = Integer.valueOf(Str_NomineeAge);
                if(nominee_age < 18){
                    customerObj.put("appointee_details",appointee_detailsObj);
                }
            }

            customerObj.put("address_detail",address_detailObj);
            customerObj.put("vehicle_detail",vehicle_detailObj);


            if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
                if(StrPolicyType.equalsIgnoreCase("renew")) {
                    customerObj.put("previous_policy_details", previous_policyObj);
                }
            }

            mpn_dataObj = new JSONObject(StrMpnData);
            mpn_dataObj.put("customer_quote",customerObj);
            //Log.d("customerObj",""+customerObj);
           UtilitySharedPreferences.setPrefs(context,"CustomerMobile",StrMobileNo);


        } catch (Exception e) {
            e.printStackTrace();
        }

        StrMpnData = mpn_dataObj.toString();

        Log.d("customerObj",""+customerObj);
        API_GET_PROPOSAL_PDF_API();

    }

    private void API_GET_PROPOSAL_PDF_API() {

        String URL_GET_PROPOSAL = ROOT_URL2+"generateProposal";
        try {
            Log.d("URL_GET_PROPOSAL", URL_GET_PROPOSAL);

            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PROPOSAL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (myDialog != null && myDialog.isShowing()) {
                                    myDialog.dismiss();
                                }
                                try {
                                    Log.d("mainResponse", response);

                                    JSONObject jobj = new JSONObject(response);
                                    boolean status = jobj.getBoolean("status");

                                    if (status) {

                                        JSONObject dataObj = jobj.getJSONObject("data");
                                        String proposal_no = dataObj.getString("proposal_no");
                                        String proposal_otp = dataObj.getString("otp");
                                        String policy_no = dataObj.getString("policy_no");
                                        String quote_link = dataObj.getString("quote_link");
                                        String quote_url = dataObj.getString("url");


                                        UtilitySharedPreferences.setPrefs(context,"ProposalAry",dataObj.toString());


                                        Intent intent = new Intent(getContext(), ProposalPdfActivity.class);
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                    } else {
                                        String message = jobj.getString("message");
                                        if(message!=null && !message.equalsIgnoreCase("") && !message.equalsIgnoreCase("null")){
                                            CommonMethods.DisplayToastWarning(context, message);
                                        }else{
                                            CommonMethods.DisplayToastWarning(context, "Failed to generate proposal.");
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    if (myDialog != null && myDialog.isShowing()) {
                                        myDialog.dismiss();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (myDialog != null && myDialog.isShowing()) {
                            myDialog.dismiss();
                        }
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();
                        CommonMethods.DisplayToastWarning(context, "Something went wrong. Please try again later.");
                    }
                }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("agent_id", StrAgentId);
                        params.put("mpn_data",StrMpnData);
                        params.put("user_action_data",StrUserActionData);
                        params.put("ic_id",SelectedIcId);
                        Log.d("BuyPolicyData",""+params.toString());

                        return params;
                    }


                };

                int socketTimeout = 50000; //30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(context, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);

            } else {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(context, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }




    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }


}
