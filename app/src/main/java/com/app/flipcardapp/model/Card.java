package com.app.flipcardapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class Card implements Serializable {
    private int id;
    private byte[] frontImg;
    private String backText;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getFrontImg() {
        return frontImg;
    }

    public void setFrontImg(Bitmap frontImg) {
        if (frontImg != null) {
            //bitmap to byte[]
            this.frontImg = bitmapToByte(frontImg);
        } else {
            this.frontImg = null;
        }
    }


    public void setImg(byte[] frontImg) {
            this.frontImg = frontImg;
    }



    public String getBackText() {
        return backText;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    // Convert imageData directly to bitmap
    public Bitmap getImageDataInBitmap() {
        if (this.frontImg != null) {
            //turn byte[] to bitmap
            return BitmapFactory.decodeByteArray(this.frontImg, 0, this.frontImg.length);
        }
        return null;
    }


    public byte[] bitmapToByte(Bitmap bitmap) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            //bitmap to byte[] stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] x = stream.toByteArray();
            //close stream to save memory
            stream.close();
            return x;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
