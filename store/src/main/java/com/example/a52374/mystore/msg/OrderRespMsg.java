package com.example.a52374.mystore.msg;


import com.example.a52374.mystore.bean.Charge;

//请求创建订单后的返回订单号和支付凭证
public class OrderRespMsg {

    private String orderNum;

    private Charge charge;


    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Charge getCharge() {
        return charge;
    }

    public void setCharge(Charge charge) {
        this.charge = charge;
    }

}