package com.indicosmic.mypolicynow_app.webservices;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Ind3 on 15-12-17.
 */

public class Common {


    //public static String BASE_URL = "http://103.87.174.12/uat/mypolicynow.com/Api/";
    //public static String BASE_URL = "https://www.mypolicynow.com/Api/";
    public static String BASE_URL = "http://uat.mypolicynow.com/Api/";
    /*http://203.112.144.126/mypolicynow.com/
    http://123.108.50.50/mypolicynow.com/
    https://www.mypolicynow.com/prelive/
    https://www.mypolicynow.com/
    http://103.87.174.12/uat/mypolicynow.com/
*/
    public static String LOGIN_URL = BASE_URL+"login_pos";
    public static String URL_CHECK_MOBILE_NO = BASE_URL+"mobile_otp";
    public static String URL_CHECK_EMAIL_ID = BASE_URL+"email_otp";
    public static String URL_CHAGED_DEVICE = BASE_URL+"check_otp";

    public static String URL_NEWINSPECTION = BASE_URL+"fetch_breaking_pending_data";
    public static String URL_INPROGRESS_INSPECTION = BASE_URL+"fetch_breaking_progress_data";
    public static String URL_REFERBACK_INSPECTION = BASE_URL+"fetch_breaking_referback_data";

    public static String URL_NEWINSPECTION_DETAILS = BASE_URL+"fetch_breaking_pending_pos_data_details";

    public static String URL_QUESTION_LIST = BASE_URL+"fetch_breaking_question";
    public static String URL_SUBMIT_QUESTION = BASE_URL+"submit_inspection_report";

    public static String URL_NEXT_QUESTION_LIST = BASE_URL+"fetch_inspection_question";
    public static String URL_NEXT_QUESTION_SUBMIT = BASE_URL+"submit_inspection_images";

    public static String URL_INSPECTION_COUNTER = BASE_URL+"proposal_counter";

    public static String URL_SUBMIT_ODOMETER_READING = BASE_URL+"odomete_insert_data";

    public static String URL_CHECKIMEI = BASE_URL+"check_imei_no";

    public static String URL_UPLOAD_VIDEO = BASE_URL+"updateBreakInVideo";


    public static String DateDisplayedFormat(String DateIn_YMD){

        String finalDate = null;

        try {
            finalDate = new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(DateIn_YMD));
        } catch (ParseException e) {
            Log.d("Exception", e.toString());
        }

        return finalDate;
    }


//    public static String LOGIN_URL = "http://203.112.144.126/mypolicynow.com/Api/login_pos";
//    public static String URL_CHECK_MOBILE_NO = "http://203.112.144.126/mypolicynow.com/Api/mobile_otp";
//    public static String URL_CHECK_EMAIL_ID = "http://203.112.144.126/mypolicynow.com/Api/email_otp";
//    public static String URL_CHAGED_DEVICE = "http://203.112.144.126/mypolicynow.com/Api/check_otp";
//
//    public static String URL_NEWINSPECTION= "http://203.112.144.126/mypolicynow.com/Api/fetch_breaking_pending_data";
//    public static String URL_INPROGRESS_INSPECTION = "http://203.112.144.126/mypolicynow.com/Api/fetch_breaking_progress_data";
//    public static String URL_REFERBACK_INSPECTION = "http://203.112.144.126/mypolicynow.com/Api/fetch_breaking_referback_data";
//
//    public static String URL_NEWINSPECTION_DETAILS = "http://203.112.144.126/mypolicynow.com/Api/fetch_breaking_pos_data_details";
//
//    public static String URL_QUESTION_LIST = "http://203.112.144.126/mypolicynow.com/Api/fetch_breaking_question";
//    public static String URL_SUBMIT_QUESTION = "http://203.112.144.126/mypolicynow.com/Api/submit_inspection_report";
//
//    public static String URL_NEXT_QUESTION_LIST = "http://203.112.144.126/mypolicynow.com/Api/fetch_inspection_question";
//    public static String URL_NEXT_QUESTION_SUBMIT = "http://203.112.144.126/mypolicynow.com/Api/submit_inspection_images";
//
//    public static String URL_INSPECTION_COUNTER = "http://203.112.144.126/mypolicynow.com/Api/proposal_counter";
//
//    public static String URL_SUBMIT_ODOMETER_READING = "http://203.112.144.126/mypolicynow.com/Api/odomete_insert_data";
//
//    public static String URL_CHECKIMEI = "http://203.112.144.126/mypolicynow.com/Api/check_imei_no";





}


