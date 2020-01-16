package com.indicosmic.www.mypolicynow.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ind3 on 09-04-18.
 */

public class AppUtill {

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "ClaimPic";
    private static final String VIDEO_DIRECTORY_NAME = "ClaimVideo";
    static File mediaFile;

    static String text;
    static String captionString = "";
    public static String driver_license = "";
    public static String permit = "";
    public static String rcBook = "";
    public static String driver_pan = "";
    public static String driver_aadhar = "";
    public static String registration_no = "";
    public static String chassis_no = "";
    public static String odometer_reading = "";

    public static String accidentImage = "";

    public static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        //Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }


    public static File createVideoPath() {

        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), VIDEO_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(VIDEO_DIRECTORY_NAME, "Oops! Failed create " + VIDEO_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        //Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");

        return mediaFile;
    }


//    public static Bitmap addWatermark(File imgFile) {
//
//        Bitmap dest = null;
//
//        if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            Matrix mtx = new Matrix();
//            mtx.postRotate(90);
//            Bitmap resized = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), mtx, true);
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
//            captionString = sdf.format(new Date());
//            text = "Date:" + captionString + "\nLat:" + SingletonClass.getinstance().agent_lat + "\nLong:" + SingletonClass.getinstance().agent_long +
//                    "\nIMEI:" + SingletonClass.getinstance().imei+ "\nImage_Name:"+SingletonClass.getinstance().str_imageName;
//            String[] str = text.split("\n");
//
//            if (resized != null) {
//
//                try {
//                    dest = resized.copy(resized.getConfig(), true);
//                } catch (OutOfMemoryError e1) {
//                    Log.e("Exception", e1.getMessage());
//                    e1.printStackTrace();
//                } catch (Error e) {
//                    Log.e("Exception", e.getMessage());
//                    e.printStackTrace();
//                }
//
//
//                if (dest != null) {
//
//                    Canvas cs = new Canvas(dest);
//                    Typeface tf = Typeface.create("Verdana", Typeface.BOLD);
//                    Paint tPaint = new Paint();
//                    tPaint.setAntiAlias(true);
//                    tPaint.setTextSize(60);
//                    tPaint.setTextAlign(Paint.Align.LEFT);
//                    tPaint.setTypeface(tf);
//                    tPaint.setColor(Color.RED);
//                    tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//                    cs.drawBitmap(resized, 0f, 0f, null);
//                    float textHeight = tPaint.measureText("yY");
//                    int index = 0;
//                    for (String Oneline : str) {
//                        cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f,
//                                tPaint);
//                        index++;
//
//                    }
//                }
//            }
//        }
//        return dest;
//    }


   /* public static Bitmap addWatermark(File imgFile) {

        Bitmap dest = null;


        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Bitmap resized = AppUtill.rotateBitmap(myBitmap, 90);

//            if (resized != null) {
//                resized = AppUtill.rotateImage(myBitmap, 90);
//            }

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            captionString = sdf.format(new Date());
            text = "Date:" + captionString + "\nLat:" + SingletonClass.getinstance().agent_lat + "\nLong:" + SingletonClass.getinstance().agent_long +
                    "\nIMEI:" + SingletonClass.getinstance().imei_no;
            String[] str = text.split("\n");

            if (resized != null) {

                try {
                    dest = resized.copy(resized.getConfig(), true);
                } catch (OutOfMemoryError e1) {
                    Log.e("Exception", e1.getMessage());
                    e1.printStackTrace();
                } catch (Error e) {
                    Log.e("Exception", e.getMessage());
                    e.printStackTrace();
                }


                if (dest != null) {

                    Canvas cs = new Canvas(dest);
                    Typeface tf = Typeface.create("Verdana", Typeface.BOLD);
                    Paint tPaint = new Paint();
                    tPaint.setAntiAlias(true);
                    tPaint.setTextSize(60);
                    tPaint.setTextAlign(Paint.Align.LEFT);
                    tPaint.setTypeface(tf);
                    tPaint.setColor(Color.RED);
                    tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                    cs.drawBitmap(resized, 0f, 0f, null);
                    float textHeight = tPaint.measureText("yY");
                    int index = 0;
                    for (String Oneline : str) {
                        cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f,
                                tPaint);
                        index++;

                    }
                }
            }
        }

        return dest;
    }*/

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);

        return imgString;
    }


    public static void deleteFolder(){

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/ClaimPic");
       // File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);
        //   Toast.makeText(this, "dir="+dir, Toast.LENGTH_LONG).show();
        Log.d("deleteFolder: ",dir.toString());

        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
    }


    public static void strictmode() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

//    public static Bitmap saveWaterMarkImage(File imgFile) {
//
//        try {
//
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//            String imageTempName = timeStamp + ".png";
//
//            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/ClaimPic", imageTempName));
//            // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClaimPic" + "/" + imageTempName);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return ssd;
//    }

    public static void showAlertDialog(Context context, String title, String message, Boolean status) {
        android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        Drawable alert = context.getResources().getDrawable(android.R.drawable.ic_dialog_alert);
        alert.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        alertDialog.setIcon(alert);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(1);
            }
        });
        alertDialog.show();
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }




}


