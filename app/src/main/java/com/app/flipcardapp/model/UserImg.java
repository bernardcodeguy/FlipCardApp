package com.app.flipcardapp.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UserImg {
    private int id;
    private byte[] Img;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImg() {
        return Img;
    }

    public void setImg(byte[] Img) {
        this.Img = Img;
    }

    public void setImgBitmap(Bitmap Img) {
        if (Img != null) {
            //bitmap to byte[]
            this.Img = bitmapToByte(Img);
        } else {
            this.Img = null;
        }
    }

    // Convert imageData directly to bitmap
    public Bitmap getImageDataInBitmap() {
        if (this.Img != null) {
            //turn byte[] to bitmap
            return BitmapFactory.decodeByteArray(this.Img, 0, this.Img.length);
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
