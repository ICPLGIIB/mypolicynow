package com.indicosmic.www.mypolicynow.customer_info;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.mypolicynow_activities.IcListingQuoteScreen;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow.utils.CommonMethods.ucFirst;

public class ContactDetailsFragment extends Fragment implements BlockingStep {


    View rootView;
    Context context;
    EditText Edt_Address1,Edt_Address2,Edt_AddressPincode,Edt_State,Edt_City;
    String StrAgentId,Str_Address1,Str_Address2,Str_Pincode,Str_State,Str_City,Str_StateId,Str_CityId;
    JSONObject stateObj,cityObj;
    StepperLayout.OnNextClickedCallback mCallback;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        rootView = inflater.inflate(R.layout.fragment_contact_details_info, container, false);

        context = getContext();

        init();

        return rootView;

    }

    private void init() {


        StrAgentId =   UtilitySharedPreferences.getPrefs(context,"PosId");
        Edt_Address1 = (EditText)rootView.findViewById(R.id.Edt_Address1);
        Edt_Address2 = (EditText)rootView.findViewById(R.id.Edt_Address2);
        Edt_AddressPincode = (EditText)rootView.findViewById(R.id.Edt_AddressPincode);
        Edt_State = (EditText)rootView.findViewById(R.id.Edt_State);
        Edt_City = (EditText)rootView.findViewById(R.id.Edt_City);


        Edt_AddressPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==6){
                    API_GET_CITY_STATE_BY_PINCODE(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setDataToFields();


    }

    private void API_GET_CITY_STATE_BY_PINCODE(String str_Pincode) {

        String URL_Check_Pincode = RestClient.ROOT_URL2 +"getStateCityByPinCode";

        try {
            Log.d("URL",URL_Check_Pincode);

            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Check_Pincode,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {
                                    JSONObject jobj = new JSONObject(response);

                                    boolean status = jobj.getBoolean("status");
                                    if(status){
                                        JSONObject dataObj = jobj.getJSONObject("data");
                                        stateObj = dataObj.getJSONObject("state");
                                        cityObj = dataObj.getJSONObject("city");

                                        Str_State = stateObj.getString("name");
                                        Str_City = cityObj.getString("name");

                                        Edt_City.setText(ucFirst(Str_City));
                                        Edt_State.setText(ucFirst(Str_State));
                                        Str_StateId = stateObj.getString("id");
                                        Str_CityId = cityObj.getString("id");

                                        UtilitySharedPreferences.setPrefs(context,"CustomerCityArry",cityObj.toString());
                                        UtilitySharedPreferences.setPrefs(context,"CustomerStateArry",stateObj.toString());
                                        UtilitySharedPreferences.setPrefs(context,"AddressStateId",Str_StateId);
                                        UtilitySharedPreferences.setPrefs(context,"AddressCityId",Str_CityId);
                                    }


                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("agent_id", StrAgentId);
                        map.put("pincode",str_Pincode);

                        Log.d("MapLoadIcList",""+map);
                        return map;
                    }
                };

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(context, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }


    private void setDataToFields() {

        String StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");

        try {
            JSONObject mpnObj = new JSONObject(StrMpnData);
            JSONObject customer_quoteObj = mpnObj.getJSONObject("customer_quote");
            if(customer_quoteObj!=null && !customer_quoteObj.toString().contains("address_detail")){

                JSONObject address_detailObj = customer_quoteObj.getJSONObject("address_detail");

                Str_Address1 = address_detailObj.getString("address1");
                Str_Address2 = address_detailObj.getString("address2");
                Str_Pincode = address_detailObj.getString("pincode");
                Str_StateId = address_detailObj.getString("state_id");
                stateObj = address_detailObj.getJSONObject("state");
                Str_CityId = address_detailObj.getString("city_id");
                cityObj = address_detailObj.getJSONObject("city");


                if(Str_Address1!=null && !Str_Address1.equalsIgnoreCase("") && !Str_Address1.equalsIgnoreCase("null")){
                    Edt_Address1.setText(Str_Address1);
                }

                if(Str_Address2!=null && !Str_Address2.equalsIgnoreCase("") && !Str_Address2.equalsIgnoreCase("null")){
                    Edt_Address2.setText(Str_Address2);
                }

                if(Str_Pincode!=null && !Str_Pincode.equalsIgnoreCase("") && !Str_Pincode.equalsIgnoreCase("null")){
                    Edt_AddressPincode.setText(Str_Pincode);
                }

                if(Str_State!=null && !Str_State.equalsIgnoreCase("") && !Str_State.equalsIgnoreCase("null")){
                    Edt_State.setText(Str_State);
                }

                if(Str_City!=null && !Str_City.equalsIgnoreCase("") && !Str_City.equalsIgnoreCase("null")){
                    Edt_City.setText(Str_City);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        mCallback = callback;
        if (validateFields()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Edt_City.getWindowToken(), 0);


            Str_Address1 = Edt_Address1.getText().toString();
            Str_Address2 = Edt_Address2.getText().toString();
            Str_Pincode = Edt_AddressPincode.getText().toString();
            Str_State = Edt_State.getText().toString();
            Str_City = Edt_City.getText().toString();



            JSONObject address_detailObj = new JSONObject();

            try {


                address_detailObj.put("address1",Str_Address1);
                address_detailObj.put("address2",Str_Address2);
                address_detailObj.put("pincode",Str_Pincode);
                address_detailObj.put("state_id",Str_StateId);
                address_detailObj.put("state",stateObj);
                address_detailObj.put("city_id",Str_CityId);
                address_detailObj.put("city",cityObj);


            } catch (JSONException e) {
                e.printStackTrace();
            }


            UtilitySharedPreferences.setPrefs(context,"address_detailObj",address_detailObj.toString());
            UtilitySharedPreferences.setPrefs(context,"AddressState",Str_State);
            UtilitySharedPreferences.setPrefs(context,"AddressCity",Str_City);

            Log.d("AddressDetails",""+address_detailObj.toString());


            if(mCallback!=null) {
                mCallback.goToNextStep();
            }
        }

    }

    private boolean validateFields() {
        boolean result = true;



        if (!MyValidator.isValidField(Edt_Address1)) {
            Edt_Address1.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Address 1");
            result = false;
        }

        if (!MyValidator.isValidField(Edt_Address2)) {
            Edt_Address2.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Address 2");
            result = false;
        }
        if (!MyValidator.isValidField(Edt_AddressPincode)) {
            Edt_AddressPincode.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Pincode");
            result = false;
        }

        if (!MyValidator.isValidField(Edt_State)) {
            Edt_State.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter State");
            result = false;
        }

        if (!MyValidator.isValidField(Edt_City)) {
            Edt_City.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Address City");
            result = false;
        }

        return result;
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!validateFields()) {
            return new VerificationError("");
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}