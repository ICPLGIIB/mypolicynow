package com.indicosmic.www.mypolicynow.mypolicynow_activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
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
import com.iceteck.silicompressorr.videocompression.Config;
import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.mypolicynow_activities.ProposalPdfActivity;
import com.indicosmic.www.mypolicynow.utils.CommonMethods;
import com.indicosmic.www.mypolicynow.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow.utils.FilePath;
import com.indicosmic.www.mypolicynow.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow.webservices.RestClient;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static com.indicosmic.www.mypolicynow.utils.AppUtill.IMAGE_DIRECTORY_NAME;
import static com.indicosmic.www.mypolicynow.utils.CommonMethods.ucFirst;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.Basic_auth;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.api_password;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.api_user_name;
import static com.indicosmic.www.mypolicynow.webservices.RestClient.x_api_key;
import static io.fabric.sdk.android.Fabric.TAG;

public class ReviewDetailsActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    View rootView;
    ImageView iv_Ic;
    TextView tv_IC_Name,tv_policy_start_date,tv_policy_end_date,tv_IDV_CoverAmt,tv_OwnerName,tv_OwnerEmail,tv_OwnerPhone,tv_NomineeName,
            tv_NomineeAge,tv_NomineeRelation,tv_AppointeeName,tv_AppointeeAge,tv_AppointeeRelation,tv_RegistrationDate,tv_EngNo,tv_ChasNo,
            tv_Address1,tv_Address2,tv_City,tv_State,tv_Pincode;
    Button btn_Edit,btn_GenerateProposal;
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
    Dialog DialogUploadPolicy;
    Uri fileUri;
    File myFileToUpload = null;
    String PreviousPolicyFileName;
    byte[] bbytesFile;

    String file_base;
    String filePath,picturePath,b64,filename,file_extension;
    String Quote_Link="";
    EditText EdtChooseFile;
    ImageView Iv_UploadedImg;
    Button btnPreviousPolicyUpload;
    Uri outputFileUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    File imageFile = null;

    JSONObject vehicle_ownerObj;
    JSONObject nominee_detailsObj;
    JSONObject appointee_detailsObj;
    JSONObject address_detailObj;
    JSONObject vehicle_detailObj;
    JSONObject previous_policyObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_review_details_info);

        init();

    }



    private void init() {
        myDialog = new ProgressDialog(this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        setDataToView();
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void setDataToView() {
        iv_Ic = (ImageView)findViewById(R.id.iv_Ic);
        tv_IC_Name  = (TextView)findViewById(R.id.tv_IC_Name);
        tv_policy_start_date= (TextView)findViewById(R.id.tv_policy_start_date);
        tv_policy_end_date= (TextView)findViewById(R.id.tv_policy_end_date);
        tv_IDV_CoverAmt= (TextView)findViewById(R.id.tv_IDV_CoverAmt);

        tv_OwnerName= (TextView)findViewById(R.id.tv_OwnerName);
        tv_OwnerEmail= (TextView)findViewById(R.id.tv_OwnerEmail);
        tv_OwnerPhone= (TextView)findViewById(R.id.tv_OwnerPhone);

        tv_NomineeName= (TextView)findViewById(R.id.tv_NomineeName);
        tv_NomineeAge  = (TextView)findViewById(R.id.tv_NomineeAge);
        tv_NomineeRelation= (TextView)findViewById(R.id.tv_NomineeRelation);

        tv_AppointeeName= (TextView)findViewById(R.id.tv_AppointeeName);
        tv_AppointeeAge= (TextView)findViewById(R.id.tv_AppointeeAge);
        tv_AppointeeRelation= (TextView)findViewById(R.id.tv_AppointeeRelation);

        tv_RegistrationDate= (TextView)findViewById(R.id.tv_RegistrationDate);
        tv_EngNo= (TextView)findViewById(R.id.tv_EngNo);
        tv_ChasNo = (TextView)findViewById(R.id.tv_ChasNo);

        tv_Address1  = (TextView)findViewById(R.id.tv_Address1);
        tv_Address2= (TextView)findViewById(R.id.tv_Address2);
        tv_City= (TextView)findViewById(R.id.tv_City);
        tv_State= (TextView)findViewById(R.id.tv_State);
        tv_Pincode= (TextView)findViewById(R.id.tv_Pincode);

        btn_Edit = (Button)findViewById(R.id.btn_Edit);
        btn_GenerateProposal= (Button)findViewById(R.id.btn_GenerateProposal);

        LayoutAppointeeDetails = (LinearLayout)findViewById(R.id.LayoutAppointeeDetails);

        btn_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_GenerateProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCompleteClicked();
            }
        });

        Log.d("HI","Im In");
        try{
            StrMpnData = UtilitySharedPreferences.getPrefs(this,"MpnData");
            StrUserActionData = UtilitySharedPreferences.getPrefs(this,"UserActionData");
            SelectedIcId = UtilitySharedPreferences.getPrefs(this,"SelectedIcId");
            String StrOwnerDetails = UtilitySharedPreferences.getPrefs(this,"vehicle_ownerObj");
            String StrNomineeDetails = UtilitySharedPreferences.getPrefs(this,"nominee_detailsObj");
            String StrAppointeeDetails = UtilitySharedPreferences.getPrefs(this,"appointee_detailsObj");
            String StrAddressDetails = UtilitySharedPreferences.getPrefs(this,"address_detailObj");
            String StrVehicleDetails = UtilitySharedPreferences.getPrefs(this,"vehicle_detailObj");
            String StrPreviousPolicyDetails = UtilitySharedPreferences.getPrefs(this,"previous_policyObj");

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

            Glide.with(this).load(StrImageUrl).into(iv_Ic);

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
            Str_State = UtilitySharedPreferences.getPrefs(this,"AddressState");
            Str_City = UtilitySharedPreferences.getPrefs(this,"AddressCity");
         /*   Str_StateId = address_detailObj.getString("state_id");
            Str_CityId = address_detailObj.getString("city_id");
*/
            tv_Address1.setText(Str_Address1);
            tv_Address2.setText(Str_Address2);
            tv_City.setText(Str_City);
            tv_State.setText(Str_State);
            tv_Pincode.setText(Str_Pincode);

          /*  StrRtoStateCode = vehicle_detailObj.getString( "veh1");
            StrRtoCityCode = vehicle_detailObj.getString( "veh2");
            StrRtoZoneCode = vehicle_detailObj.getString( "veh3");
            StrVehicleNo = vehicle_detailObj.getString( "veh4");*/
            StrEngineNo = vehicle_detailObj.getString( "engine_no");
            StrChassisNo = vehicle_detailObj.getString( "chassis_no");
           /* StrVehicleColor = vehicle_detailObj.getString( "car_color");
            StrAgreement = vehicle_detailObj.getString( "agreement_type");
            StrBankName = vehicle_detailObj.getString( "agreement_bank");
*/
            /*StrPreviousPolicyNo = previous_policyObj.getString( "pre_policy_no");
            StrPreviousPolicyIC = previous_policyObj.getString( "pre_insurance");
*/

            if(StrEngineNo!=null && !StrEngineNo.equalsIgnoreCase("")){
                tv_EngNo.setText(StrEngineNo.toUpperCase());
            }

            if(StrChassisNo!=null && !StrChassisNo.equalsIgnoreCase("")){
                tv_ChasNo.setText(StrChassisNo.toUpperCase());
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }



    public void onCompleteClicked() {
        JSONObject customerObj = new JSONObject();

        String cityObjStr = UtilitySharedPreferences.getPrefs(this,"CustomerCityArry");
        String stateObjStr = UtilitySharedPreferences.getPrefs(this,"CustomerStateArry");
        StrMpnData =  UtilitySharedPreferences.getPrefs(this,"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(this,"UserActionData");
        String StrOwnerDetails = UtilitySharedPreferences.getPrefs(this,"vehicle_ownerObj");
        String StrNomineeDetails = UtilitySharedPreferences.getPrefs(this,"nominee_detailsObj");
        String StrAppointeeDetails = UtilitySharedPreferences.getPrefs(this,"appointee_detailsObj");
        String StrAddressDetails = UtilitySharedPreferences.getPrefs(this,"address_detailObj");
        String StrVehicleDetails = UtilitySharedPreferences.getPrefs(this,"vehicle_detailObj");
        String StrPreviousPolicyDetails = UtilitySharedPreferences.getPrefs(this,"previous_policyObj");


        try {

            vehicle_ownerObj = new JSONObject(StrOwnerDetails);
            nominee_detailsObj = new JSONObject(StrNomineeDetails);
            appointee_detailsObj = new JSONObject(StrAppointeeDetails);
            address_detailObj = new JSONObject(StrAddressDetails);
            vehicle_detailObj = new JSONObject(StrVehicleDetails);
            previous_policyObj = new JSONObject(StrPreviousPolicyDetails);


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

            JSONObject proposalObj = mpn_dataObj.getJSONObject("proposal_data");
            Quote_Link = proposalObj.getString("quote_forward_link");

            Log.d("Quote_Link",""+Quote_Link);

            //Log.d("customerObj",""+customerObj);
            UtilitySharedPreferences.setPrefs(this,"CustomerMobile",StrMobileNo);


        } catch (Exception e) {
            e.printStackTrace();
        }

        StrMpnData = mpn_dataObj.toString();

        Log.d("customerObj",""+customerObj);

        if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
            if(StrPolicyType.equalsIgnoreCase("renew")) {
                uploadPreviousPolicyDocumentPopup(true);
            }else{
                API_GET_PROPOSAL_PDF_API();
            }
        }





    }

    /*Start Upload Previous Year Policy*/
    private void uploadPreviousPolicyDocumentPopup(boolean show_skip_btn) {

        DialogUploadPolicy = new Dialog(this);
        DialogUploadPolicy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogUploadPolicy.setCanceledOnTouchOutside(false);
        DialogUploadPolicy.setCancelable(true);
        DialogUploadPolicy.setContentView(R.layout.popup_upload_previous_policy_document);
        Objects.requireNonNull(DialogUploadPolicy.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView iv_close = (ImageView) DialogUploadPolicy.findViewById(R.id.iv_close);
        Button captureImage_button = (Button) DialogUploadPolicy.findViewById(R.id.captureImage_button);

        Iv_UploadedImg = (ImageView)DialogUploadPolicy.findViewById(R.id.Iv_UploadedImg);
        btnPreviousPolicyUpload = (Button) DialogUploadPolicy.findViewById(R.id.btnUpload);
        Button btnViewProposal = (Button) DialogUploadPolicy.findViewById(R.id.btnViewProposal);

        if(show_skip_btn){
            btnViewProposal.setVisibility(View.VISIBLE);
        }else {
            btnViewProposal.setVisibility(View.GONE);
        }

        btnViewProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                API_GET_PROPOSAL_PDF_API();
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"IsPrePolicyUploaded","false");
                if(DialogUploadPolicy!=null && DialogUploadPolicy.isShowing()){
                    DialogUploadPolicy.dismiss();
                }

            }
        });

        DialogUploadPolicy.show();




        captureImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraIntent();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogUploadPolicy!=null && DialogUploadPolicy.isShowing()) {
                    DialogUploadPolicy.dismiss();
                }
            }
        });

        btnPreviousPolicyUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageFile!=null &&  !imageFile.getAbsolutePath().toString().equalsIgnoreCase("")) {
                    API_UPLOAD_PREVIOUS_YEAR_POLICY();

                }else {
                    CommonMethods.DisplayToastError(getApplicationContext(),"It seems some issue while uploading.. Please try again later.");
                }
            }
        });

    }

    private void cameraIntent(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            try {
                //imageFile = createImageFile();
                @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());

                PreviousPolicyFileName = Quote_Link+"_"+ timeStamp;
                //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AGS");
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                imageFile = File.createTempFile(
                        PreviousPolicyFileName,  // prefix
                        ".jpg",         // suffix
                        direct      // directory
                );

                mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
                Log.d("ImagePath",""+mCurrentPhotoPath);

                outputFileUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", imageFile);
                if (imageFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("take_type", 1);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                Log.i(TAG, "Exception");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try {

            if(imageFile!=null && imageFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                Iv_UploadedImg.setImageBitmap(myBitmap);
                Iv_UploadedImg.setVisibility(View.VISIBLE);
                btnPreviousPolicyUpload.setVisibility(View.VISIBLE);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void API_UPLOAD_PREVIOUS_YEAR_POLICY(){


        String UPLOAD_URL = ROOT_URL2 + "uploaddocumentproposal";
        //String path = FilePath.getPath(this, outputFileUri);
        //Log.d("path",""+path);

        Log.d("Upload_URL", UPLOAD_URL);


        //Uploading code

        myDialog.show();

        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)

                    .addFileToUpload(imageFile.getAbsolutePath(), "other_document") //
                    //Adding file
                    .addHeader("x-api-key",x_api_key)
                    .addHeader("Authorization","Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password))
                    .addParameter("document_name", "previous_policy_doc")
                    .addParameter("quote_forward_link", Quote_Link)

                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, Exception e) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");

                        }

                        @Override
                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }


                            Log.d("serverRespGetHttpCode", "" + serverResponse.getHttpCode());
                            if (serverResponse.getHttpCode() == 200) {
                                try {
                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                    Log.d("uploadResponse", "" + jsonObject);
                                    JSONObject resultObj = jsonObject.getJSONObject("result");
                                    boolean upload_status = resultObj.getBoolean("status");
                                    if (upload_status) {
                                        CommonMethods.DisplayToastSuccess(getApplicationContext(), "Document Uploaded Successfully.");
                                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "IsPrePolicyUploaded", "true");

                                        if (DialogUploadPolicy != null && DialogUploadPolicy.isShowing()) {
                                            DialogUploadPolicy.dismiss();
                                        }
                                    } else {
                                        CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                API_GET_PROPOSAL_PDF_API();
                            }
                        }

                        @Override
                        public void onCancelled(UploadInfo uploadInfo) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                        }
                    })
                    .startUpload();


        } catch (Exception exc) {
            Log.d("UploadException",""+ exc.getMessage());
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
            }
        }


    }

    /*End Upload Previous Year Policy*/

    private void API_GET_PROPOSAL_PDF_API() {

        myDialog.show();
        String URL_GET_PROPOSAL = ROOT_URL2+"generateProposal";
        try {
            Log.d("URL_GET_PROPOSAL", URL_GET_PROPOSAL);

            ConnectionDetector cd = new ConnectionDetector(this);
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


                                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"ProposalAry",dataObj.toString());
                                        Intent intent = new Intent(getApplicationContext(), ProposalPdfActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                    } else {
                                        String message = jobj.getString("message");
                                        if(message!=null && !message.equalsIgnoreCase("") && !message.equalsIgnoreCase("null")){
                                            CommonMethods.DisplayToastWarning(getApplicationContext(), message);
                                        }else{
                                            CommonMethods.DisplayToastWarning(getApplicationContext(), "Failed to generate proposal.");
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
                        CommonMethods.DisplayToastWarning(getApplicationContext(), "Something went wrong. Please try again later.");
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
                stringRequest.setRetryPolicy(policy);
                // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {
                if (myDialog != null && myDialog.isShowing()) {
                    myDialog.dismiss();
                }
                CommonMethods.DisplayToast(this, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }




    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }






}
