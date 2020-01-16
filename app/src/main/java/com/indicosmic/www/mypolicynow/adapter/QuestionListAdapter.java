package com.indicosmic.www.mypolicynow.adapter;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.indicosmic.www.mypolicynow.R;
import com.indicosmic.www.mypolicynow.break_in_manager.QuestionActivityListView;
import com.indicosmic.www.mypolicynow.model.QuestionListModel;
import com.indicosmic.www.mypolicynow.utils.JSONClass;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * Created by Zeta Apponomics 3 on 25-06-2015.
 */
@SuppressWarnings("ALL")
public class QuestionListAdapter extends BaseAdapter {

    public int counter = 0;
    int global_position;
    private List<QuestionListModel> questionListModelList;
    Context mCtc;
    private ViewHolder viewHolder;
    private int radiopos;
    ArrayList<String> glbl_imgpath = new ArrayList();

    private RadioGroup lastCheckedRadioGroup = null;

    JSONObject jsonObj;
    JSONClass jsonClass;

    ArrayList<QuestionListModel> nameArraylist;

    public QuestionListAdapter(List<QuestionListModel> questionListModelList, Context context) {
        this.questionListModelList = questionListModelList;
        this.mCtc = context;
        jsonClass = new JSONClass(mCtc);
        nameArraylist = new ArrayList<QuestionListModel>();
    }

    @Override
    public int getCount() {
        return questionListModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return questionListModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {
        return questionListModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) mCtc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.questionlist_menu, null);
        } else {
            view = convertView;
        }

        viewHolder = new ViewHolder();

        viewHolder.maincategory = (TextView) view.findViewById(R.id.maincategory);
        viewHolder.rg = (RadioGroup) view.findViewById(R.id.setAnswer);
       // viewHolder.rg.clearCheck();
        viewHolder.safe = (RadioButton) view.findViewById(R.id.safe);
        viewHolder.scarch = (RadioButton) view.findViewById(R.id.scratch);
        viewHolder.pressed = (RadioButton) view.findViewById(R.id.pressed);
        viewHolder.broken = (RadioButton) view.findViewById(R.id.broken);
        viewHolder.camera = (ImageView) view.findViewById(R.id.camera);
        viewHolder.image = (ImageView) view.findViewById(R.id.image);

        // Set data in listView
        final QuestionListModel dataSet = (QuestionListModel) questionListModelList.get(position);

        nameArraylist.addAll(questionListModelList);

        viewHolder.maincategory.setText(dataSet.getQuestion().toUpperCase());
        viewHolder.safe.setText(dataSet.getSafe());
        viewHolder.scarch.setText(dataSet.getScrach());
        viewHolder.pressed.setText(dataSet.getPressed());
        viewHolder.broken.setText(dataSet.getBroken());

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        Dialog settingsDialog = new Dialog(mCtc);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(600, 700);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        ImageView iv = new ImageView(mCtc);
        iv.setPadding(5,5,5,5);
        iv.setAdjustViewBounds(true);
        iv.setLayoutParams(lp);
        if (dataSet.isHaveImage()){
            iv.setImageBitmap(dataSet.getImage());
        }else {
            settingsDialog.dismiss();
            return;
        }

        settingsDialog.addContentView(iv,lp);
        settingsDialog.show();

            }
        });

        if (dataSet.isHaveImage()){
            viewHolder.image.setImageBitmap(dataSet.getImage());
        }else {
            Bitmap icon = BitmapFactory.decodeResource(mCtc.getResources(), R.drawable.image_preview);
            viewHolder.image.setImageBitmap(icon);
        }

        final View finalView = view;


        viewHolder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                View radioButton = radioGroup.findViewById(checkedId);
                radiopos = radioGroup.indexOfChild(radioButton);
                final QuestionListModel dataSet = (QuestionListModel) questionListModelList.get(position);
                counter++;

                 jsonClass.createJSONObjectPosition(dataSet.getId(),radiopos);

                //SingletonClass.getinstance().total_question = counter;

                switch (checkedId) {
                    case -1:
                        Log.d("TAG", "Choices cleared!");
                        break;
                    case R.id.safe:
                        dataSet.setAnswer("Safe");
                        Log.d("TAG", "Choose Safe");
                        break;
                    case R.id.scratch:
                        dataSet.setAnswer("Scratch");
                        Log.d("TAG", "Choose Scratch");
                        break;
                    case R.id.pressed:
                        dataSet.setAnswer("Press");
                        Log.d("TAG", "Choose Pressed");
                        break;
                    case R.id.broken:
                        dataSet.setAnswer("Broken");
                        Log.d("TAG", "Choose Broken");
                        break;
                    default:
                        Log.d("TAG", "Huh?");
                        break;
                }

            }

        });
        dataSet.setListItemPosition(position);

        viewHolder.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call parent method of activity to click image
                ((QuestionActivityListView) mCtc).captureImage(dataSet.getListItemPosition(), dataSet.getId() + "" + dataSet.getSafe());
                viewHolder.image.setImageBitmap(dataSet.getImage());
            }
        });


        return view;
    }


    public void setImageInItem(final int position, final Bitmap imageSrc, final String imagePath) {

        QuestionListModel dataSet = (QuestionListModel) questionListModelList.get(position);

        String encodedImageData = getEncoded64ImageStringFromBitmap(imageSrc);

        Log.d(TAG, "setImageInItem: "+glbl_imgpath);


        Log.d("que",encodedImageData);

        jsonClass.createJSONObjectImage(dataSet.getId(),encodedImageData);
        int w = imageSrc.getWidth();
        int h = imageSrc.getHeight();

        // Setting post rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(90);

        //Rotating Bitmap

      //  Bitmap rotatedBMP = Bitmap.createBitmap(resized, 0, 0, w, h, mtx, true);
        Bitmap resized = Bitmap.createScaledBitmap(imageSrc, 600, 700, true);

        dataSet.setImage(resized);
        dataSet.setHaveImage(true);
        notifyDataSetChanged();
    }



    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }

    static class ViewHolder {
        TextView maincategory;
        ImageView camera,image;
        RadioButton safe,scarch,pressed,broken;
        RadioGroup rg;
    }

    public  boolean getAnswer() {

        for (int i = 0; i < questionListModelList.size(); i++) {
        // if (global_position == getCount() - 1)

            if(questionListModelList.get(i).getAnswer().equals("Null")) {
                return false;
            }
        }
        return true;
    }

}
