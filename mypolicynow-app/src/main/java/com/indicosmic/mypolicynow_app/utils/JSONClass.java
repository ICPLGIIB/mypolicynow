package com.indicosmic.mypolicynow_app.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Ind3 on 10-01-18.
 */

public class JSONClass {
    static Context ctx;
    public JSONObject jsonObject;
    public static JSONArray jrray;
    public static JSONObject final_object;



    ArrayList <String> data;




    public JSONClass(Context context) {

        this.ctx = context;
        jrray = new JSONArray();

        data = new ArrayList<String>();

    }

    public static JSONObject getFinal_object(){
        return final_object;
    }

    public static JSONObject getJrray(){
        return  final_object;
    }

// on submit click
        /*public static void createJSONArray(){

            jrray.put(SingletonClass.getinstance().jso);
            jrray.put(SingletonClass.getinstance().jsoImage);

           JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("agent_id", SingletonClass.getinstance().agent_id);
                jsonObject.put("customer_id", SingletonClass.getinstance().proposal_list_id);
                jsonObject.put("inpection_id",SingletonClass.getinstance().inspection_id);
                jsonObject.put("ic_id",SingletonClass.getinstance().ic_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jrray.put(jsonObject);
            Log.d("aa",jsonObject.toString());

        }*/

    public void createJSONObjectPosition(int question_id,int position,Context context){

        try {
            jsonObject = new JSONObject();
            jsonObject.put("question_id", question_id);
            jsonObject.put("answer_id",position+1);
            jsonObject.put("pos_id",UtilitySharedPreferences.getPrefs(context,"PosId"));
            jsonObject.put("proposal_list_id",UtilitySharedPreferences.getPrefs(context,"proposal_list_id"));
            jsonObject.put("ic_id",UtilitySharedPreferences.getPrefs(context,"IC_Id"));
            jrray.put(jsonObject);
            Log.d( "mera ",jsonObject.toString());
            Log.d("mera_array", String.valueOf(jrray));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void createJSONObjectImage(int question_id,String imagePath,Context context){

        try {
            jsonObject = new JSONObject();
            jsonObject.put("question_id", question_id);
            jsonObject.put("image", imagePath);
            jsonObject.put("pos_id",UtilitySharedPreferences.getPrefs(context,"PosId"));
            jsonObject.put("proposal_list_id",UtilitySharedPreferences.getPrefs(context,"proposal_list_id"));
            jsonObject.put("ic_id",UtilitySharedPreferences.getPrefs(context,"IC_Id"));
            jrray.put(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

     //   Toast.makeText(ctx, ""+jsonObject, Toast.LENGTH_SHORT).show();
        Log.d("JSON", String.valueOf(jrray));

    }
 }

