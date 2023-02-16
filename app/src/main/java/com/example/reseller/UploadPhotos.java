package com.example.reseller;

public class UploadPhotos {

    private String mNAme;
    private String mImageUri;

    public UploadPhotos(){

    }

    public UploadPhotos(String name, String ImageUri) {
        if (name.trim().equals("")){
            name = "No Name";
        }
        this.mNAme = mNAme;
        this.mImageUri = mImageUri;
    }

    public String getmNAme() {
        return mNAme;
    }

    public void setmNAme(String mNAme) {
        this.mNAme = mNAme;
    }

    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}
