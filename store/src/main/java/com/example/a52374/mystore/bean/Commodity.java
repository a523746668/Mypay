package com.example.a52374.mystore.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 52374 on 2017/2/26.
 */
//商品数据，购物车
public class Commodity extends DataSupport implements Serializable {

    private int count=1;//数量
    private boolean ischeck;
    private byte[] bitmap;
    private int id;
    private String name;
    private String imgUrl;
    private double price;
    private int sale;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public byte[] getBitmap() {
        return bitmap;
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }
    public void addcount(){
        count++;
    }
    public void suncount(){
        if(count>1){
            count--;
        }
    }
}
