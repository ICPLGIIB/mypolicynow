package com.indicosmic.mypolicynow_app.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by on 14/6/16.
 */
public class Constant {

    public static Dialog mProgressDialog;

    public String Plan;

    public static String[] Month = new String[] {"SELECT MONTH","January","February","March","April","May","June","July","August","September","October","November","December"};
    public static String[] Period = new String[] {"Monthly","Quarterly","Half Yearly","Yearly" };
    public static String[] SpinnerBreakPeriod= new String[] {"0", "1","2","3","4","5","6","7","8","9","10","11","12" };



    public static Typeface TextBoldFont(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/Lato_bold.ttf");
    }

    public static Typeface fontregular(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/Lato_regular.ttf");
    }

    public static Typeface FontLight(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/helvetica_neue_light.ttf");
    }



    public static Typeface LatoBold(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/Lato_bold.ttf");
    }

    public static Typeface LatoRegular(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/Lato_regular.ttf");
    }




    public static Typeface OpenSansBold(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/opensans_bold.ttf");
    }




    public static Typeface HelveticaBold(Context context) {

        return Typeface.createFromAsset(context.getAssets(),
                "font/helvetica_neu_bold.ttf");
    }

}
