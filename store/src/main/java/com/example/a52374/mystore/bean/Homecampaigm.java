package com.example.a52374.mystore.bean;

/**
 * Created by 52374 on 2017/2/24.
 */


//首页CardView对应显示类
public class Homecampaigm {
      private Long id;
        private String title;
     private Campaigm cpOne,cpTwo,cpThree;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Campaigm getCpOne() {
        return cpOne;
    }

    public void setCpOne(Campaigm cpOne) {
        this.cpOne = cpOne;
    }

    public Campaigm getCpTwo() {
        return cpTwo;
    }

    public void setCpTwo(Campaigm cpTwo) {
        this.cpTwo = cpTwo;
    }

    public Campaigm getCpThree() {
        return cpThree;
    }

    public void setCpThree(Campaigm cpThree) {
        this.cpThree = cpThree;
    }
}
