package com.indicosmic.mypolicynow_app.breakin_app_files;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.indicosmic.mypolicynow_app.R;
import com.indicosmic.mypolicynow_app.utils.UtilitySharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by Ind3 on 15-01-18.
 */

public class InspectionImageAdapter extends BaseAdapter{


    private List<InspectionImageModel> dataList;
    Context mCtc;
    private ViewHolder viewHolder;
    int image_counter;

    // JSONObject jsonObj;
    ArrayList<String> imageArrayList;
    public static JSONArray jrray2;

    ArrayList<Integer> poslist;

    public InspectionImageAdapter(List<InspectionImageModel> getData, Context context) {
        dataList = getData;
        mCtc = context;
        imageArrayList = new ArrayList<String>();
        jrray2 = new JSONArray();
        poslist = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return dataList.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) mCtc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.next_question_menu, null);
        } else {
            view = convertView;
        }


        viewHolder = new ViewHolder();
        viewHolder.maincategory = (TextView) view.findViewById(R.id.maincategory);
        viewHolder.tv_str_is_mandate = (TextView) view.findViewById(R.id.tv_str_is_mandate);
        viewHolder.camera = (ImageView) view.findViewById(R.id.camera);
        viewHolder.image = (ImageView) view.findViewById(R.id.image);
        viewHolder.txtodometer = (EditText) view.findViewById(R.id.txtodometer);

        // Set data in listView
        final InspectionImageModel dataSet = (InspectionImageModel) dataList.get(position);

        if(dataSet.getIs_mand()!=null && !dataSet.getIs_mand().equalsIgnoreCase("")){
            if(dataSet.getIs_mand().equalsIgnoreCase("0")){
                viewHolder.maincategory.setText(dataSet.getQuestion().toUpperCase());
            }else if(dataSet.getIs_mand().equalsIgnoreCase("1")){
                viewHolder.maincategory.setText(dataSet.getQuestion().toUpperCase()+" *");

            }
        }

        viewHolder.image.setImageDrawable(null);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog settingsDialog = new Dialog(mCtc);
                settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(600, 700);
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                ImageView iv = new ImageView(mCtc);
                iv.setPadding(5, 5, 5, 5);
                iv.setAdjustViewBounds(true);
                iv.setLayoutParams(lp);
                if (dataSet.isHaveImage()) {
                    iv.setImageBitmap(dataSet.getImage());
                } else {

                    settingsDialog.dismiss();
                    return;
                }
                //use in your case iv.setImageBitmap(convertToBitmap(data.getImage()));
                settingsDialog.addContentView(iv, lp);
                settingsDialog.show();

            }
        });


        if (dataSet.isHaveImage()) {
            viewHolder.image.setImageBitmap(dataSet.getImage());
        } else {
            Bitmap icon = BitmapFactory.decodeResource(mCtc.getResources(), R.drawable.image_preview);
            viewHolder.image.setImageBitmap(icon);
        }


        dataSet.setListItemPosition(position);

        viewHolder.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call parent method of activity to click image
                ((ActivityInspectionImages) mCtc).captureImage(dataSet.getListItemPosition(), dataSet.getId() + "" + dataSet.getQuestion());
                viewHolder.image.setImageBitmap(dataSet.getImage());

                image_counter++;
                //SingletonClass.getinstance().nextimage_counter = image_counter;
                poslist.add(position);
            }
        });

        return view;
    }

    public void setImageInItem(final int position, Bitmap imageSrc, final String imagePath) {


        InspectionImageModel dataSet = (InspectionImageModel) dataList.get(position);

        String encodedImageData = getEncoded64ImageStringFromBitmap(imageSrc);
        dataSet.setImagePath(imagePath);

        Log.d("encode",encodedImageData);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("question_id", position);            //encodedImageData);
            jsonObject.put("break_in_case_id", UtilitySharedPreferences.getPrefs(mCtc,"BreakInCaseId"));
            jsonObject.put("ic_id", UtilitySharedPreferences.getPrefs(mCtc,"IC_Id"));
            jsonObject.put("proposal_list_id", UtilitySharedPreferences.getPrefs(mCtc,"proposal_list_id"));
            jsonObject.put("pos_id", UtilitySharedPreferences.getPrefs(mCtc,"PosId"));
            jsonObject.put("image", encodedImageData);//encodedImageData
            jrray2.put(jsonObject);
            imageArrayList.add(String.valueOf(jsonObject));

            Log.d("imagejson", jrray2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(90);

        // Rotating Bitmap
        //  Bitmap rotatedBMP = Bitmap.createBitmap(imageSrc, 0, 0, w, h, mtx, true);
        Bitmap resized = Bitmap.createScaledBitmap(imageSrc, 600, 700, true);

        dataSet.setImage(resized);
        dataSet.setHaveImage(true);
        notifyDataSetChanged();
    }



    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }



    static class ViewHolder {
        TextView maincategory,tv_str_is_mandate;
        ImageView camera, image;
        EditText txtodometer;
    }

    public boolean CheckImageExist() {
        int last_three_image = dataList.size() - 5;
        Log.d(TAG, "CheckImageExist: " + last_three_image);
        int j = 0;
        for (int i = 0; i < dataList.size(); i++) {

            if (dataList.get(i).getImagePath().contains("Null") && last_three_image > j) {

                return false;
            } else {
                j++;
            }
        }
        return true;

    }

}
