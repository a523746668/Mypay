package com.example.a52374.mystore.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by 52374 on 2017/3/16.
 */


//收藏表对应类
public class Collect extends DataSupport  {
    private byte[] bitmap;
    private String name;

     private double price;

    public Collect(byte[] bitmap, String name, double price) {
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
