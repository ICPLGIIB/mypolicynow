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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class NomineeDetailsFragment extends Fragment implements BlockingStep,AdapterView.OnItemSelectedListener {

    Spinner Spn_Education;

    SearchableSpinner Spn_EducationField;

    String Str_Education,Str_EducationField,Str_EducationFieldCode;
    String UserObj,ClientId,StrFirstName,StrLastName,StrEmail,StrMobile,DeviceId,StrCountryId,StrReligionId;
    ProgressDialog myDialog;
    Context context;
    StepperLayout.OnNextClickedCallback mCallback;

    ArrayList<String> relationNameList = new ArrayList<String>();

    String StrCollegeName;

    private ArrayList<String> stateArrayList;


    //    //Arraylist for occupation
    ArrayList<String> educationFieldNameList = new ArrayList<String>();
    ArrayList<String> educationFieldCodeList = new ArrayList<String>();



    Spinner Spn_NomineeSalutation,Spn_NomineeRelationship,Spn_AppointeeSalutation,Spn_AppointeeRelationship;
    EditText edt_NomineeFirstName,edt_NomineeMiddleName,edt_NomineeLastName,edt_NomineeAge,edt_AppointeeFirstName,edt_AppointeeMiddleName,
            edt_AppointeeLastName,edt_AppointeeAge;
    LinearLayout LayoutAppointeeDetails;
    String Str_NomineeSalutation,Str_NomineeRelationship,Str_AppointeeSalutation,Str_AppointeeRelationship;
    String Str_NomineeFirstName,Str_NomineeMiddleName,Str_NomineeLastName,Str_NomineeAge;
    String Str_AppointeeFirstName,Str_AppointeeMiddleName,Str_AppointeeLastName,Str_AppointeeAge;

    View rootView;



    public NomineeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_nominee_details_info, container, false);
        context = getContext();
        init();

        return rootView;
    }

    private void init() {

        Spn_NomineeSalutation = (Spinner)rootView.findViewById(R.id.Spn_NomineeSalutation);
        Spn_NomineeRelationship = (Spinner)rootView.findViewById(R.id.Spn_NomineeRelationship);
        Spn_AppointeeSalutation = (Spinner)rootView.findViewById(R.id.Spn_AppointeeSalutation);
        Spn_AppointeeRelationship = (Spinner)rootView.findViewById(R.id.Spn_AppointeeRelationship);

        fetchRelationType("Nominee","MR.");
        fetchRelationType("Appointee","MR.");


        edt_NomineeFirstName = (EditText)rootView.findViewById(R.id.edt_NomineeFirstName);
        edt_NomineeMiddleName = (EditText)rootView.findViewById(R.id.edt_NomineeMiddleName);
        edt_NomineeLastName = (EditText)rootView.findViewById(R.id.edt_NomineeLastName);
        edt_NomineeAge = (EditText)rootView.findViewById(R.id.edt_NomineeAge);


        LayoutAppointeeDetails = (LinearLayout)rootView.findViewById(R.id.LayoutAppointeeDetails);
        LayoutAppointeeDetails.setVisibility(View.GONE);

        edt_AppointeeFirstName = (EditText)rootView.findViewById(R.id.edt_AppointeeFirstName);
        edt_AppointeeMiddleName = (EditText)rootView.findViewById(R.id.edt_AppointeeMiddleName);
        edt_AppointeeLastName = (EditText)rootView.findViewById(R.id.edt_AppointeeLastName);
        edt_AppointeeAge = (EditText)rootView.findViewById(R.id.edt_AppointeeAge);


        edt_NomineeAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>=1){
                    String nomineeAge = editable.toString();
                    if(nomineeAge.matches("^[0-9]*$")) {
                        Integer nominee_age = Integer.valueOf(nomineeAge);
                        if (nominee_age < 18) {
                            LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                        } else {
                            LayoutAppointeeDetails.setVisibility(View.GONE);
                        }
                    }else {
                        edt_NomineeAge.setError("Please enter valid years");
                    }

                }else if(editable.toString().length()==0) {
                    LayoutAppointeeDetails.setVisibility(View.GONE);
                }
            }
        });


        edt_AppointeeAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().length()>=1){
                    String appointeeAge = editable.toString();
                    if(appointeeAge.matches("^[0-9]*$")) {
                        Integer appointee_age = Integer.valueOf(appointeeAge);
                        if (appointee_age < 18) {
                            edt_AppointeeAge.setError("Appointee Age cannot be less than 18 years.");
                        } else {
                            edt_AppointeeAge.setError(null);

                        }
                    }else{
                        edt_AppointeeAge.setError("Please enter valid years");
                    }

                }else if(editable.toString().length()==0) {
                    edt_AppointeeAge.setError(null);
                }
            }
        });


        Spn_NomineeSalutation.setOnItemSelectedListener(this);
        Spn_NomineeRelationship.setOnItemSelectedListener(this);
        Spn_AppointeeSalutation.setOnItemSelectedListener(this);
        Spn_AppointeeRelationship.setOnItemSelectedListener(this);

        myDialog = new ProgressDialog(getActivity());
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setMessage("Please Wait...");




        setDataToFields();
    }


    public String loadJSONFromAssetRelationType() {
        String json = null;
        try {
            InputStream is = context.getAssets().open("relations.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void fetchRelationType(String RelationFor, String Salutation) {

        JSONArray m_jArry = null;
        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetRelationType());

            if(Salutation!=null && Salutation.equalsIgnoreCase("MR.")){
                m_jArry  = obj.getJSONArray("MR_RelationList");
                relationNameList = new ArrayList<>();
            }else if(Salutation!=null && Salutation.equalsIgnoreCase("MS.")){
                m_jArry  = obj.getJSONArray("MS_RelationList");
                relationNameList = new ArrayList<>();
            }else if(Salutation!=null && Salutation.equalsIgnoreCase("MRS.")){
                m_jArry  = obj.getJSONArray("MRS_RelationList");
                relationNameList = new ArrayList<>();
            }


            if (m_jArry != null) {
                for (int i = 0; i < m_jArry.length(); i++) {

                    JSONObject jo_inside = m_jArry.getJSONObject(i);

                    String relationName = jo_inside.getString("relationName");

                    relationNameList.add(relationName);
                }
            }



            if(RelationFor!=null && RelationFor.equalsIgnoreCase("Nominee")){
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, relationNameList);
                Spn_NomineeRelationship.setAdapter(countryAdapter);
            }else if(RelationFor!=null && RelationFor.equalsIgnoreCase("Appointee")) {
                ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, relationNameList);
                Spn_AppointeeRelationship.setAdapter(countryAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private void setDataToFields() {

        String StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");

        try {
            JSONObject mpnObj = new JSONObject(StrMpnData);
            if(StrMpnData.contains("customer_quote")) {
                JSONObject customer_quoteObj = mpnObj.getJSONObject("customer_quote");
                if (customer_quoteObj != null && !customer_quoteObj.toString().contains("nominee_details")) {

                    JSONObject nominee_detailsObj = customer_quoteObj.getJSONObject("nominee_details");

                    Str_NomineeSalutation = nominee_detailsObj.getString("nominee_salutaion");
                    Str_NomineeFirstName = nominee_detailsObj.getString("nominee_first_name");
                    Str_NomineeMiddleName = nominee_detailsObj.getString("nominee_middle_name");
                    Str_NomineeLastName = nominee_detailsObj.getString("nominee_last_name");
                    Str_NomineeRelationship = nominee_detailsObj.getString("nominee_relationship");
                    Str_NomineeAge = nominee_detailsObj.getString("nominee_age");


                    if (customer_quoteObj != null && !customer_quoteObj.toString().contains("appointee_details")) {
                        LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                        JSONObject appointee_detailsObj = customer_quoteObj.getJSONObject("appointee_details");

                        Str_AppointeeSalutation = appointee_detailsObj.getString("appointee_salutaion");
                        Str_AppointeeFirstName = appointee_detailsObj.getString("appointee_first_name");
                        Str_AppointeeMiddleName = appointee_detailsObj.getString("appointee_middle_name");
                        Str_AppointeeLastName = appointee_detailsObj.getString("appointee_last_name");
                        Str_AppointeeRelationship = appointee_detailsObj.getString("appointee_relationship");
                        Str_AppointeeAge = appointee_detailsObj.getString("appointee_age");


                    }

                    if (Str_NomineeSalutation != null && !Str_NomineeSalutation.equalsIgnoreCase("") && !Str_NomineeSalutation.equalsIgnoreCase("null")) {
                        int i = getIndex(Spn_NomineeSalutation, Str_NomineeSalutation);
                        Spn_NomineeSalutation.setSelection(i);
                    }

                    if (Str_NomineeFirstName != null && !Str_NomineeFirstName.equalsIgnoreCase("") && !Str_NomineeFirstName.equalsIgnoreCase("null")) {
                        edt_NomineeFirstName.setText(Str_NomineeFirstName);
                    }

                    if (Str_NomineeMiddleName != null && !Str_NomineeMiddleName.equalsIgnoreCase("") && !Str_NomineeMiddleName.equalsIgnoreCase("null")) {
                        edt_NomineeMiddleName.setText(Str_NomineeMiddleName);
                    }

                    if (Str_NomineeLastName != null && !Str_NomineeLastName.equalsIgnoreCase("") && !Str_NomineeLastName.equalsIgnoreCase("null")) {
                        edt_NomineeLastName.setText(Str_NomineeLastName);
                    }

                    if (Str_NomineeRelationship != null && !Str_NomineeRelationship.equalsIgnoreCase("") && !Str_NomineeRelationship.equalsIgnoreCase("null")) {
                        int i = getIndex(Spn_NomineeRelationship, Str_NomineeRelationship);
                        Spn_NomineeRelationship.setSelection(i);
                    }
                    if (Str_NomineeAge != null && !Str_NomineeAge.equalsIgnoreCase("") && !Str_NomineeAge.equalsIgnoreCase("null")) {
                        edt_NomineeAge.setText(Str_NomineeAge);
                        int nominee_age = Integer.valueOf(Str_NomineeAge);
                        if (nominee_age < 18) {
                            LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                        } else {
                            LayoutAppointeeDetails.setVisibility(View.GONE);
                        }
                    }

                    if (LayoutAppointeeDetails.getVisibility() == View.VISIBLE) {

                        if (Str_AppointeeSalutation != null && !Str_AppointeeSalutation.equalsIgnoreCase("") && !Str_AppointeeSalutation.equalsIgnoreCase("null")) {
                            int i = getIndex(Spn_AppointeeSalutation, Str_AppointeeSalutation);
                            Spn_AppointeeSalutation.setSelection(i);
                        }

                        if (Str_AppointeeFirstName != null && !Str_AppointeeFirstName.equalsIgnoreCase("") && !Str_AppointeeFirstName.equalsIgnoreCase("null")) {
                            edt_AppointeeFirstName.setText(Str_AppointeeFirstName);
                        }

                        if (Str_AppointeeMiddleName != null && !Str_AppointeeMiddleName.equalsIgnoreCase("") && !Str_AppointeeMiddleName.equalsIgnoreCase("null")) {
                            edt_AppointeeMiddleName.setText(Str_AppointeeMiddleName);
                        }

                        if (Str_AppointeeLastName != null && !Str_AppointeeLastName.equalsIgnoreCase("") && !Str_AppointeeLastName.equalsIgnoreCase("null")) {
                            edt_AppointeeLastName.setText(Str_AppointeeLastName);
                        }

                        if (Str_AppointeeRelationship != null && !Str_AppointeeRelationship.equalsIgnoreCase("") && !Str_AppointeeRelationship.equalsIgnoreCase("null")) {
                            int i = getIndex(Spn_AppointeeRelationship, Str_AppointeeRelationship);
                            Spn_AppointeeRelationship.setSelection(i);
                        }
                        if (Str_AppointeeAge != null && !Str_AppointeeAge.equalsIgnoreCase("") && !Str_AppointeeAge.equalsIgnoreCase("null")) {
                            edt_AppointeeAge.setText(Str_AppointeeAge);

                        }

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

        int id = adapterView.getId();
        if (id == R.id.Spn_NomineeSalutation) {
            if(Spn_NomineeSalutation.getSelectedItemPosition()>0) {
                Str_NomineeSalutation = Spn_NomineeSalutation.getSelectedItem().toString().trim();
                fetchRelationType("Nominee", Str_NomineeSalutation);
            }else {
                Str_NomineeSalutation = "";
            }
        } else if (id == R.id.Spn_NomineeRelationship) {
            Str_NomineeRelationship = Spn_NomineeRelationship.getSelectedItem().toString().toLowerCase().trim();
        } else if (id == R.id.Spn_AppointeeSalutation) {
            if(Spn_AppointeeSalutation.getSelectedItemPosition()>0) {
                Str_AppointeeSalutation = Spn_AppointeeSalutation.getSelectedItem().toString().trim();
                fetchRelationType("Appointee", Str_AppointeeSalutation);
            }else {
                Str_AppointeeRelationship = "";
            }
        } else if (id == R.id.Spn_AppointeeRelationship) {
            Str_AppointeeRelationship = Spn_AppointeeRelationship.getSelectedItem().toString().toLowerCase().trim();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!validateFields()) {
            return new VerificationError("");
        }

        return null;
    }

    private boolean validateFields() {
        boolean result = true;

        if (!MyValidator.isValidSpinner(Spn_NomineeSalutation)) {
            CommonMethods.DisplayToastWarning(getContext(),"Please Select Nominee Salutation");
            result = false;
        }

        if (!MyValidator.isValidName(edt_NomineeFirstName)) {
            edt_NomineeFirstName.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter First Name");
            result = false;
        }

        if (!MyValidator.isValidName(edt_NomineeLastName)) {
            edt_NomineeLastName.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Last Name");
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_NomineeRelationship)) {
            CommonMethods.DisplayToastWarning(getContext(),"Please Select Nominee Relationship");
            result = false;
        }

        if (!MyValidator.isValidField(edt_NomineeAge)) {
            edt_NomineeAge.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Nominee Age");
            result = false;
        }


        if(LayoutAppointeeDetails!=null && LayoutAppointeeDetails.getVisibility()==View.VISIBLE){

            if (!MyValidator.isValidSpinner(Spn_AppointeeSalutation)) {
                CommonMethods.DisplayToastWarning(getContext(),"Please Select Appointee Salutation");
                result = false;
            }

            if (!MyValidator.isValidName(edt_AppointeeFirstName)) {
                edt_AppointeeFirstName.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter First Name");
                result = false;
            }

            if (!MyValidator.isValidName(edt_AppointeeLastName)) {
                edt_AppointeeLastName.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter Last Name");
                result = false;
            }

            if (!MyValidator.isValidSpinner(Spn_AppointeeRelationship)) {
                CommonMethods.DisplayToastWarning(getContext(),"Please Select Appointee Relationship");
                result = false;
            }

            if (!MyValidator.isValidAdultAge(edt_AppointeeAge)) {
                edt_AppointeeAge.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter Valid Appointee Age");
                result = false;
            }


        }






        return result;
    }
        //Block Steps Until Operation gets Finished

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {

        mCallback = onNextClickedCallback;

        if(validateFields()) {

            InputMethodManager imm = (InputMethodManager) Objects.requireNonNull(getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edt_NomineeAge.getWindowToken(), 0);
            }

            Str_NomineeSalutation = Spn_NomineeSalutation.getSelectedItem().toString();
            Str_NomineeFirstName = edt_NomineeFirstName.getText().toString();
            Str_NomineeMiddleName = edt_NomineeMiddleName.getText().toString();
            Str_NomineeLastName = edt_NomineeLastName.getText().toString();
            Str_NomineeRelationship = Spn_NomineeRelationship.getSelectedItem().toString();
            Str_NomineeAge = edt_NomineeAge.getText().toString();

            if (LayoutAppointeeDetails != null && LayoutAppointeeDetails.getVisibility() == View.VISIBLE) {
                Str_AppointeeSalutation = Spn_AppointeeSalutation.getSelectedItem().toString();
                Str_AppointeeFirstName = edt_AppointeeFirstName.getText().toString();
                Str_AppointeeMiddleName = edt_AppointeeMiddleName.getText().toString();
                Str_AppointeeLastName = edt_AppointeeLastName.getText().toString();
                Str_AppointeeRelationship = Spn_AppointeeRelationship.getSelectedItem().toString();
                Str_AppointeeAge = edt_AppointeeAge.getText().toString();
            } else {
                Str_AppointeeSalutation = "";
                Str_AppointeeFirstName = "";
                Str_AppointeeMiddleName = "";
                Str_AppointeeLastName = "";
                Str_AppointeeRelationship = "";
                Str_AppointeeAge = "";
            }

            JSONObject nominee_detailsObj = new JSONObject();
            JSONObject appointee_detailsObj = new JSONObject();

            try {
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


            } catch (JSONException e) {
                e.printStackTrace();
            }


            UtilitySharedPreferences.setPrefs(context,"nominee_detailsObj",nominee_detailsObj.toString());
            UtilitySharedPreferences.setPrefs(context,"appointee_detailsObj",appointee_detailsObj.toString());
            Log.d("NomineeDetail",""+nominee_detailsObj.toString());
            Log.d("AppointeeDetail",""+appointee_detailsObj.toString());


            if(mCallback!=null) {
                mCallback.goToNextStep();
            }
        }

    }




    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {



    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {

        onBackClickedCallback.goToPrevStep();
    }



    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }

}
