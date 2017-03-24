package com.example.a52374.mystore.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by 52374 on 2017/2/25.
 */

public class Hotitem extends DataSupport implements Serializable{

    /**
     * id : 1
     * categoryId : 5
     * campaignId : 1
     * name : 联想（Lenovo）拯救者14.0英寸游戏本（i7-4720HQ 4G 1T硬盘 GTX960M 2G独显 FHD IPS屏 背光键盘）黑
     * imgUrl : http://7mno4h.com2.z0.glb.qiniucdn.com/s_recommend_55c1e8f7N4b99de71.jpg
     * price : 5979.0
     * sale : 8654
     */

    private long  id;

    private String name;
    private String imgUrl;
    private double price;
    private int sale;







    public long  getId() {
        return id;
    }

    public void setId(long  id) {
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
}
