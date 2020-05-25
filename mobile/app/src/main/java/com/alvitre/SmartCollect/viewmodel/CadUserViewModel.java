package com.alvitre.SmartCollect.viewmodel;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

public class CadUserViewModel extends ViewModel {
    private Bitmap photo;
    private String photoFileName;

    public String getPhotoFileName(){return photoFileName;}
    public void setPhotoFileName(String photoFileName){this.photoFileName=photoFileName;}

    public Bitmap getPhoto(){
        return photo;
    }
    public void setPhoto( Bitmap photo){
        this.photo = photo;
    }
}
