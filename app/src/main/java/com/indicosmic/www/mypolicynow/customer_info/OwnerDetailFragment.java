package com.indicosmic.www.mypolicynow.customer_info;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.MyValidator;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OwnerDetailFragment extends Fragment implements BlockingStep, View.OnClickListener, AdapterView.OnItemSelectedListener {


    View rootView;
    Context context;
    EditText edt_Email_Addresss,edt_Mobile_Number,edt_First_Name,edt_Middle_Name,edt_Last_Name,edt_Dob,edt_Pan_Card,edt_Aadhar_Card,Edt_GstInNumber;
    Spinner Spn_Salutation,Spn_Gender,Spn_MaritalStatus;
    private DatePickerDialog dobDatePickerDialog;
    private SimpleDateFormat dateFormatter, dateFormatter1;
    String StrPolicyHolder="",Date_of_born = "", YearOfBorn = "";
    String StrSelectedSalutation,StrSelectedGender,StrSelectedMaritalStatus,StrFirstName,StrMiddleName, StrLastName, StrEmailAddress, StrPan, StrAadharCard,StrMobileNo="",StrGstInNumber="";
    ProgressDialog myDialog;
    Dialog MessageDialog;
    StepperLayout.OnNextClickedCallback mCallback;

    public OwnerDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        rootView = inflater.inflate(R.layout.fragment_owner_details_info, container, false);

        context = getContext();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        init();


        return rootView;

    }

    private void init() {

        StrPolicyHolder = UtilitySharedPreferences.getPrefs(context,"PolicyHolder");

        edt_Email_Addresss = (EditText)rootView.findViewById(R.id.edt_Email_Addresss);
        edt_Mobile_Number = (EditText)rootView.findViewById(R.id.edt_Mobile_Number);
        edt_First_Name  = (EditText)rootView.findViewById(R.id.edt_First_Name);
        edt_Middle_Name  = (EditText)rootView.findViewById(R.id.edt_Middle_Name);
        edt_Last_Name  = (EditText)rootView.findViewById(R.id.edt_Last_Name);
        edt_Dob  = (EditText)rootView.findViewById(R.id.edt_Dob);
        edt_Pan_Card  = (EditText)rootView.findViewById(R.id.edt_Pan_Card);
        edt_Aadhar_Card  = (EditText)rootView.findViewById(R.id.edt_Aadhar_Card);
        Edt_GstInNumber = (EditText)rootView.findViewById(R.id.Edt_GstInNumber);

        Spn_Salutation= (Spinner) rootView.findViewById(R.id.Spn_Salutation);
        Spn_Gender= (Spinner) rootView.findViewById(R.id.Spn_Gender);
        Spn_MaritalStatus = (Spinner) rootView.findViewById(R.id.Spn_MaritalStatus);

        Spn_Salutation.setOnItemSelectedListener(this);
        Spn_Gender.setOnItemSelectedListener(this);
        Spn_MaritalStatus.setOnItemSelectedListener(this);

        InputMethodManager mgr1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr1.showSoftInput(edt_Dob, InputMethodManager.SHOW_FORCED);
        setDateTimeField();
        setDataToFields();

        myDialog = new ProgressDialog(context);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

    }

    private void setDataToFields() {

        String StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");

        try {
            JSONObject mpnObj = new JSONObject(StrMpnData);
            JSONObject customer_quoteObj = mpnObj.getJSONObject("customer_quote");
            if(customer_quoteObj!=null && !customer_quoteObj.toString().contains("vehicle_owner")){

                JSONObject vehicle_ownerObj = customer_quoteObj.getJSONObject("vehicle_owner");
                StrEmailAddress  = vehicle_ownerObj.getString("email");
                StrMobileNo = vehicle_ownerObj.getString("mobile_no");
                StrSelectedSalutation = vehicle_ownerObj.getString("salutaion");
                StrFirstName = vehicle_ownerObj.getString("first_name");
                StrMiddleName = vehicle_ownerObj.getString("middle_name");
                StrLastName = vehicle_ownerObj.getString("last_name");
                Date_of_born = vehicle_ownerObj.getString("dob");
                StrGstInNumber = vehicle_ownerObj.getString("individual_gst_no");
                StrPan = vehicle_ownerObj.getString("pancard");
                StrAadharCard = vehicle_ownerObj.getString("aadharcard");
                StrSelectedMaritalStatus = vehicle_ownerObj.getString("marital_status");
                StrSelectedGender=  vehicle_ownerObj.getString("gender");



                if(StrFirstName!=null && !StrFirstName.equalsIgnoreCase("") && !StrFirstName.equalsIgnoreCase("null")) {
                    edt_First_Name.setText(StrFirstName);
                    if (StrEmailAddress != null && !StrEmailAddress.equalsIgnoreCase("") && !StrEmailAddress.equalsIgnoreCase("null")) {
                        edt_Email_Addresss.setText(StrEmailAddress);
                    }

                    if (StrMobileNo != null && !StrMobileNo.equalsIgnoreCase("") && !StrMobileNo.equalsIgnoreCase("null")) {
                        edt_Mobile_Number.setText(StrMobileNo);
                    }

                    if (StrSelectedSalutation != null && !StrSelectedSalutation.equalsIgnoreCase("") && !StrSelectedSalutation.equalsIgnoreCase("null")) {
                        int i = getIndex(Spn_Salutation, StrSelectedSalutation);
                        Spn_Salutation.setSelection(i);
                    }

                    if (StrMiddleName != null && !StrMiddleName.equalsIgnoreCase("") && !StrMiddleName.equalsIgnoreCase("null")) {
                        edt_Middle_Name.setText(StrMiddleName);
                    }

                    if (StrLastName != null && !StrLastName.equalsIgnoreCase("") && !StrLastName.equalsIgnoreCase("null")) {
                        edt_Last_Name.setText(StrLastName);
                    }

                    if (Date_of_born != null && !Date_of_born.equalsIgnoreCase("") && !Date_of_born.equalsIgnoreCase("null")) {
                        edt_Dob.setText(Date_of_born);
                    }

                    if (StrSelectedGender != null && !StrSelectedGender.equalsIgnoreCase("") && !StrSelectedGender.equalsIgnoreCase("null")) {
                        int i = getIndex(Spn_Gender, StrSelectedGender);
                        Spn_Gender.setSelection(i);
                    }


                    if (StrSelectedMaritalStatus != null && !StrSelectedMaritalStatus.equalsIgnoreCase("") && !StrSelectedMaritalStatus.equalsIgnoreCase("null")) {
                        int i = getIndex(Spn_MaritalStatus, StrSelectedMaritalStatus);
                        Spn_MaritalStatus.setSelection(i);
                    }

                    if (StrPan != null && !StrPan.equalsIgnoreCase("") && !StrPan.equalsIgnoreCase("null")) {
                        edt_Pan_Card.setText(StrPan);
                    }

                    if (StrAadharCard != null && !StrAadharCard.equalsIgnoreCase("") && !StrAadharCard.equalsIgnoreCase("null")) {
                        edt_Aadhar_Card.setText(StrAadharCard);
                    }

                    if (StrGstInNumber != null && !StrGstInNumber.equalsIgnoreCase("") && !StrGstInNumber.equalsIgnoreCase("null")) {
                        Edt_GstInNumber.setText(StrGstInNumber);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private int getIndex(Spinner spinner, String searchString) {

        if (searchString == null || spinner.getCount() == 0) {

            return -1; // Not found

        } else {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                    return i; // Found!
                }
            }

            return -1; // Not found
        }
    }



    private void setDateTimeField() {

        edt_Dob.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        dobDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                Date_of_born = dateFormatter1.format(newDate.getTime());
                YearOfBorn = String.valueOf(year);
                Log.d("YearOfBornBasicDetail", "" + YearOfBorn);
                edt_Dob.setText(Date_of_born);
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dobDatePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.edt_Dob) {

            dobDatePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.Spn_Salutation:

                StrSelectedSalutation = Spn_Salutation.getSelectedItem().toString().trim();

                break;
            case R.id.Spn_Gender:

                StrSelectedGender = Spn_Gender.getSelectedItem().toString().trim();

                break;
            case R.id.Spn_MaritalStatus:

                StrSelectedMaritalStatus = Spn_MaritalStatus.getSelectedItem().toString().trim();

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        mCallback = callback;
        if (IsValidFields()) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Edt_GstInNumber.getWindowToken(), 0);

            StrEmailAddress = edt_Email_Addresss.getText().toString();
            StrMobileNo = edt_Mobile_Number.getText().toString();
            StrSelectedSalutation = Spn_Salutation.getSelectedItem().toString();
            StrFirstName = edt_First_Name.getText().toString();
            StrMiddleName = edt_Middle_Name.getText().toString();
            StrLastName = edt_Last_Name.getText().toString();
            Date_of_born = edt_Dob.getText().toString();
            StrSelectedGender = Spn_Gender.getSelectedItem().toString();
            StrPan = edt_Pan_Card.getText().toString();
            StrAadharCard = edt_Aadhar_Card.getText().toString();
            StrSelectedMaritalStatus = Spn_MaritalStatus.getSelectedItem().toString();

            if(Edt_GstInNumber.getText().toString().length()!=0) {
                StrGstInNumber = Edt_GstInNumber.getText().toString();
            }else {
                StrGstInNumber = "";
            }

            JSONObject vehicle_ownerObj = new JSONObject();

            try {
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

            } catch (JSONException e) {
                e.printStackTrace();
            }


            UtilitySharedPreferences.setPrefs(context,"vehicle_ownerObj",vehicle_ownerObj.toString());
            Log.d("OwnerDetails",""+vehicle_ownerObj.toString());
            callback.goToNextStep();
        }
    }

    private boolean IsValidFields() {
        boolean result = true;

        if (!MyValidator.isValidEmail(edt_Email_Addresss)) {
            edt_Email_Addresss.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Valid Email Id");
            result = false;
        }

        if (!MyValidator.isValidMobile(edt_Mobile_Number)) {
            edt_Mobile_Number.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Valid Mobile No");
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_Salutation)) {

            CommonMethods.DisplayToastWarning(context, "Please Select Salutation");
            result = false;
        }

        if (!MyValidator.isValidName(edt_First_Name)) {
            edt_First_Name.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter First Name");
            result = false;
        }

        if (!MyValidator.isValidName(edt_Last_Name)) {
            edt_Last_Name.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Last Name");
            result = false;
        }

        if (!MyValidator.isValidField(edt_Dob)) {
            edt_Dob.requestFocus();
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_Gender)) {
            CommonMethods.DisplayToastWarning(context, "Please Select Gender");
            result = false;
        }

        if(StrPolicyHolder!=null && !StrPolicyHolder.equalsIgnoreCase("") && !StrPolicyHolder.equalsIgnoreCase("null")){

            if(StrPolicyHolder.equalsIgnoreCase("CORPORATE")){

                if (!MyValidator.isValidPan(edt_Pan_Card)) {
                    CommonMethods.DisplayToastWarning(context, "Please Enter Valid Pan");
                    edt_Pan_Card.requestFocus();
                    result = false;
                }

                if (!MyValidator.isValidAadhaar(edt_Aadhar_Card)) {
                    CommonMethods.DisplayToastWarning(context, "Please Enter Valid Aadhaar");
                    edt_Aadhar_Card.requestFocus();
                    result = false;
                }

                if (!MyValidator.isValidPan(edt_Pan_Card)) {
                    CommonMethods.DisplayToastWarning(context, "Please Enter Valid Pan");
                    edt_Pan_Card.requestFocus();
                    result = false;
                }

                if (!MyValidator.isValidGSTIN(Edt_GstInNumber)) {
                    CommonMethods.DisplayToastWarning(context, "Please Enter Valid GSTIN  Number");
                    Edt_GstInNumber.requestFocus();
                    result = false;
                }



            }

        }





        return result;
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {

    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!IsValidFields()) {
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
