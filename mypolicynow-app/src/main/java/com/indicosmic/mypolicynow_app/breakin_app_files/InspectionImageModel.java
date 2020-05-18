package com.indicosmic.mypolicynow_app.breakin_app_files;

import android.graphics.Bitmap;

/**
 * Created by Ind3 on 16-01-18.
 */

public class InspectionImageModel {

    private String id;
    private String question;
    private String is_mand;

    public String getIs_mand() {
        return is_mand;
    }

    public void setIs_mand(String is_mand) {
        this.is_mand = is_mand;
    }

    private String camera;
    private Bitmap image;
    private int listItemPosition;
    private String imagePath;
    boolean haveImage;
    boolean status;
    private String txtodometer;
    private String setImage;

    public InspectionImageModel() {
    }

    public InspectionImageModel(String id, String question, String camera, Bitmap image, int listItemPosition, String imagePath, boolean haveImage, boolean status) {
        this.id = id;
        this.question = question;
        this.camera = camera;
        this.image = image;
        this.listItemPosition = listItemPosition;
        this.imagePath = imagePath;
        this.haveImage = haveImage;
        this.status = status;
    }

    public String getSetImage() {
        return setImage;
    }

    public void setSetImage(String setImage) {
        this.setImage = setImage;
    }

    public String getTxtodometer() {
        return txtodometer;
    }

    public void setTxtodometer(String txtodometer) {
        this.txtodometer = txtodometer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getListItemPosition() {
        return listItemPosition;
    }

    public void setListItemPosition(int listItemPosition) {
        this.listItemPosition = listItemPosition;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isHaveImage() {
        return haveImage;
    }

    public void setHaveImage(boolean haveImage) {
        this.haveImage = haveImage;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
