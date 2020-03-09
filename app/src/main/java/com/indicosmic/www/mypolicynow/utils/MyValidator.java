package com.indicosmic.www.mypolicynow.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SIR.WilliamRamsay on 03-Dec-15.
 */
public class MyValidator {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String REQUIRED_MSG = "Field required";
    public static final String GSTINFORMAT_REGEX = "[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}";
    public static final String GSTN_CODEPOINT_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Bitmap bitmap=null;



    public static boolean isValidPhone(CharSequence phone) {
        if (TextUtils.isEmpty(phone)) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phone).matches();
        }
    }



    public static boolean isValidIFSC(EditText editText){

        String ifsc_edt = editText.getText().toString().trim();
        if (ifsc_edt != null && ifsc_edt.length()!=11) {
            editText.setError("Enter Valid IFSC Code");
            return false;
        }else if (ifsc_edt != null && ifsc_edt.length() == 11) {
            Pattern pattern = Pattern.compile("(([A-Z|a-z]{4})([0])([A-Z0-9]{6}))");

            Matcher matcher = pattern .matcher(ifsc_edt);

            if (!matcher.matches()) {
                editText.setError("Invalid IFSC code. Enter Valid IFSC code.");
                return false;

            }else {
                //editText.setError(null);
                return true;
            }


        }else {
            //editText.setError(null);
            return true;
        }

    }



    // validating email id
    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            editText.setError("Enter valid Email");
            return false;
        }else if (email.length() == 0) {
            editText.setError("Enter valid Email");
            return false;
        }
        editText.setError(null);
        return true;
    }

    // validating password
    public static boolean isValidPassword(EditText editText) {

        String pass = editText.getText().toString().trim();
        if (pass != null && pass.length()<=0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }else {
             //$specialCharacters = "-@%\\\\[\\\\}+'!/#$^?:;,\\\\(\\\"\\\\)~`.*=&\\\\{>\\\\]<_";
            Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@%!/#$^?*=&])(?=\\S+$).{6,20}$");

            Matcher matcher = pattern.matcher(pass);

            if (!matcher.matches()) {
                editText.setError("Please Enter Strong Password.");
                return false;
            }else{
                editText.setError(null);
                return  true;
            }
        }



    }

    public static boolean isValidImageString(String imageStr) {

        if (imageStr != null && (imageStr.length()!=0 || !imageStr.equalsIgnoreCase(""))) {

            return true;
        }

        return false;
    }




    public static boolean isValidField(EditText editText) {
        String txtValue = editText.getText().toString().trim();

        if (txtValue.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        editText.setError(null);
        return true;
    }


    public static boolean isValidName(EditText editText) {
        String txtValue = editText.getText().toString().trim();

        if (txtValue.length() < 2) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        editText.setError(null);
        return true;
    }

    public static boolean isValidAge(EditText editText) {
       Integer retValue=0;

        String txtValue = editText.getText().toString().trim();
        if(txtValue.length() == 0){
            editText.setError(REQUIRED_MSG);
            return  false;
        }

        if(txtValue.length() > 0 && txtValue.length()>2)
        {
            editText.setError("Age cannot be greater than 100 years");
            return  false;
        }

        if(txtValue.length() > 0 && Integer.valueOf(txtValue)==0){
            editText.setError("Age cannot be 0 year");
            return  false;
        }


        editText.setError(null);
        return true;
    }

    public static boolean isValidAdultAge(EditText editText) {
       boolean result = false;

        String txtValue = editText.getText().toString().trim();
        if(txtValue.length() == 0){
            editText.setError(REQUIRED_MSG);
            result =   false;
        }else if(txtValue.length() > 0) {

            if (txtValue != null && !txtValue.equalsIgnoreCase("") && Integer.valueOf(txtValue) < 18) {
                editText.setError("Age cannot be less than 18 years");
                result =  false;
            }else {
                result = true;
            }
        }
        return  result;
    }

    public static boolean isValidSpinner(Spinner spinner) {
        View view = spinner.getSelectedView();
        //TextView textView = (TextView) view;
        if (spinner.getSelectedItemPosition() == 0) {
            //textView.setError("None selected");
            return false;
        }
        
        return true;
    }

    public static boolean isValidSearchableSpinner(SearchableSpinner spinner) {
        View view = spinner.getSelectedView();
        //TextView textView = (TextView) view;
        if (spinner.getSelectedItemPosition() == 0) {
            //textView.setError("None selected");
            return false;
        }

        return true;
    }

    public static boolean isValidRadioGroup(RadioGroup radioGroup) {
        int val = radioGroup.getCheckedRadioButtonId();


        if (val == -1) {

            return false;
        }

        return true;
    }

    public static boolean isValidCheckBox(CheckBox checkBox) {
        boolean checked = checkBox.isChecked();


        if (!checked) {

            return false;
        }

        return true;
    }

    public static boolean isValidMobile(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length()!=10) {
            editText.setError(REQUIRED_MSG + " Enter 10 digits");
            return false;
        }else {
            Pattern pattern = Pattern.compile("(([6-9]{1})([0-9]{9}))");

            Matcher matcher = pattern .matcher(mob);

            if (!matcher.matches()) {
                editText.setError("Invalid Mobile No. Enter Valid Mobile Number");
                return false;
            }else{
                editText.setError(null);
                return  true;
            }
        }


    }

    public static boolean isValidPan(EditText editText){

        String pan = editText.getText().toString().trim();
        if (pan != null && pan.length()!=10) {
            editText.setError("Enter Valid PAN Number");
            return false;
        }else if (pan != null && pan.length()>1) {
            Pattern pattern = Pattern.compile("(([A-Z]{5})([0-9]{4})([a-zA-Z]))");

            Matcher matcher = pattern .matcher(pan);

            if (!matcher.matches()) {
                editText.setError("Invalid PAN. Enter Valid PAN Number");
                return false;

            }else {
                //      ([CPHFATBLJG])
                    String char_4 = pan.substring(3,4);
                    Log.d("Char 4",char_4);
                    Pattern pat = Pattern.compile("[CPHFATBLJG]");
                    Matcher mat = pat.matcher(char_4);
                    if(!mat.matches()){
                        editText.setError("Invalid PAN. Enter Valid PAN Number");
                        return false;

                    }else {
                        editText.setError(null);
                        return true;
                    }
            }

        }else {
            editText.setError(null);
            return true;
        }

    }

    public static boolean isValidAadhaar(EditText editText) {
        String adhaar = editText.getText().toString().trim();
        if (adhaar != null && adhaar.length() == 12) {
            editText.setError(null);
            return true;
        }
        editText.setError(REQUIRED_MSG + " Enter 12 digits");
        return false;
    }

    public static boolean isValidImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }

    public static boolean CheckDates(String StartDT, String EndDT, EditText edt_SIPEndDate)    {
        SimpleDateFormat dfDate  = new SimpleDateFormat("yyyy-MM-dd");
        boolean b = false;
        try {
            if(dfDate.parse(StartDT).before(dfDate.parse(EndDT)))
            {

                b = true;//If start date is before end date
                edt_SIPEndDate.setError("End Date Should Be Greater Than Start Date");
            }
            else if(dfDate.parse(StartDT).equals(dfDate.parse(EndDT)))
            {
                b = false;//If two dates are equal
                edt_SIPEndDate.setError("End Date Should Be Greater Than Start Date");
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }


    public static boolean isValidGSTIN(EditText editText){
        boolean result = false;
        String gst_in = editText.getText().toString().trim();
        if (gst_in != null && gst_in.length()==0) {
            editText.setError("Enter Valid GST IN Number");
            result =  false;
        }else if (gst_in != null && gst_in.length()>1) {

            try {
                result =  validGSTIN(gst_in);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            editText.setError(null);
            result =  true;
        }
        return  result;

    }

    private static boolean validGSTIN(String gstin) throws Exception {
        boolean isValidFormat = false;
        if (checkPattern(gstin, GSTINFORMAT_REGEX)) {
            isValidFormat = verifyCheckDigit(gstin);
        }
        return isValidFormat;

    }

    /**
     * Method for checkDigit verification.
     *
     * @param gstinWCheckDigit
     * @return
     * @throws Exception
     */
    private static boolean verifyCheckDigit(String gstinWCheckDigit) throws Exception {
        Boolean isCDValid = false;
        String newGstninWCheckDigit = getGSTINWithCheckDigit(
                gstinWCheckDigit.substring(0, gstinWCheckDigit.length() - 1));

        if (gstinWCheckDigit.trim().equals(newGstninWCheckDigit)) {
            isCDValid = true;
        }
        return isCDValid;
    }

    /**
     * Method to check if an input string matches the regex pattern passed
     *
     * @param inputval
     * @param regxpatrn
     * @return boolean
     */
    public static boolean checkPattern(String inputval, String regxpatrn) {
        boolean result = false;
        if ((inputval.trim()).matches(regxpatrn)) {
            result = true;
        }
        return result;
    }

    /**
     * Method to get the check digit for the gstin (without checkdigit)
     *
     * @param gstinWOCheckDigit
     * @return : GSTIN with check digit
     * @throws Exception
     */
    public static String getGSTINWithCheckDigit(String gstinWOCheckDigit) throws Exception {
        int factor = 2;
        int sum = 0;
        int checkCodePoint = 0;
        char[] cpChars;
        char[] inputChars;

        try {
            if (gstinWOCheckDigit == null) {
                throw new Exception("GSTIN supplied for checkdigit calculation is null");
            }
            cpChars = GSTN_CODEPOINT_CHARS.toCharArray();
            inputChars = gstinWOCheckDigit.trim().toUpperCase().toCharArray();

            int mod = cpChars.length;
            for (int i = inputChars.length - 1; i >= 0; i--) {
                int codePoint = -1;
                for (int j = 0; j < cpChars.length; j++) {
                    if (cpChars[j] == inputChars[i]) {
                        codePoint = j;
                    }
                }
                int digit = factor * codePoint;
                factor = (factor == 2) ? 1 : 2;
                digit = (digit / mod) + (digit % mod);
                sum += digit;
            }
            checkCodePoint = (mod - (sum % mod)) % mod;
            return gstinWOCheckDigit + cpChars[checkCodePoint];
        } finally {
            inputChars = null;
            cpChars = null;
        }
    }

    public static boolean isValidEngine_ChassisNumber(EditText edt_engineNo) {

        String engine_no = edt_engineNo.getText().toString().trim();
        if (engine_no != null && (engine_no.length() > 4 && engine_no.length() <= 22 )){
            edt_engineNo.setError(null);
            return true;
        }
        edt_engineNo.setError(REQUIRED_MSG + "This Filed should have Minimum 5 & maximum 22 digits");
        return false;

    }
}
