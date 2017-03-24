/*
*User.java
*Created on 2015/11/18 下午3:44 by Ivan
*Copyright(c)2014 Guangzhou Onion Information Technology Co., Ltd.
*http://www.cniao5.com
*/
package com.example.a52374.mystore.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

//收货地址对应的bean
public class Address extends DataSupport implements Serializable,Comparable<Address>  {

    private Long id;

    private String consignee;
    private String phone;
    private String addr;
    private String zipCode;


   //0代表false   1代表true
    private int isDefault;

    public Address(){};

    public Address(String consignee, String phone, String addr, int isDefault){
        this.consignee = consignee;
        this.phone=phone;
        this.addr= addr;
       this.isDefault=isDefault;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    public Boolean getIsDefault() {
        if(isDefault==1){
            return  true;
        } else {
            return false;
        }

    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }


    @Override
    public int compareTo(Address another) {

        if(another.getIsDefault()!=null && this.getIsDefault() !=null)
            return another.getIsDefault().compareTo(this.getIsDefault());

        return -1;
    }
}
