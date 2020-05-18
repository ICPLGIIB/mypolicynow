package com.indicosmic.mypolicynow_app.breakin_app_files;

import android.graphics.Bitmap;

/**
 * Created by Zeta Apponomics 3 on 25-06-2015.
 */
public class CheckPointListModel {

    private int id;
    private String question;
    private String safe;
    private String scrach;
    private String pressed;
    private String broken;
    private String camera;
    private  Bitmap image;
    private int listItemPosition;
    private String imagePath;
    boolean haveImage;
    boolean status;
    String answer;

    public CheckPointListModel() {
    }

    public CheckPointListModel(int id, String question, String safe, String scrach, String pressed, String broken) {
        this.id = id;
        this.question = question;
        this.safe = safe;
        this.scrach = scrach;
        this.pressed = pressed;
        this.broken = broken;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSafe() {
        return safe;
    }

    public void setSafe(String safe) {
        this.safe = safe;
    }

    public String getScrach() {
        return scrach;
    }

    public void setScrach(String scrach) {
        this.scrach = scrach;
    }

    public String getPressed() {
        return pressed;
    }

    public void setPressed(String pressed) {
        this.pressed = pressed;
    }

    public String getBroken() {
        return broken;
    }

    public void setBroken(String broken) {
        this.broken = broken;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImage(String imagePath) {
        this.imagePath = imagePath;
    }



    public int getListItemPosition() {
        return listItemPosition;
    }

    public void setListItemPosition(int listItemPosition) {
        this.listItemPosition = listItemPosition;
    }
}