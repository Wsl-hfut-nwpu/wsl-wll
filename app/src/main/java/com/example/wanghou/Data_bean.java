package com.example.wanghou;

import android.graphics.Bitmap;

public class Data_bean {
    public String data;
    public String bitmap_data;
    public Bitmap []  bitmaps;
    public Bitmap [] get(int much){
         bitmaps=new Bitmap [much];
         return bitmaps;
    }
}
