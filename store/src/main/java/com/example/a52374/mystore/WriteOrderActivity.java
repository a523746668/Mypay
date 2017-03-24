package com.example.a52374.mystore;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a52374.mystore.Utils.Sharutil;
import com.example.a52374.mystore.bean.Commodity;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.wight.HomeToolbar;
import com.example.a52374.mystore.wight.Viewgroupdemo1;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;


// 生成订单
public class WriteOrderActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";



     private HomeToolbar tool;


      private RelativeLayout rl;

    //订单的总价
      private double pricesum;

    //支付渠道
    private String payChannel=CHANNEL_ALIPAY;



    private ArrayList<Commodity> coms;

     @ViewInject(value = R.id.woiv1)
     private ImageView iv1;

    @ViewInject(value = R.id.woiv2)
    private ImageView iv2;

    @ViewInject(value = R.id.wotv4)
    private TextView count;

    @ViewInject(value = R.id.wotv6)
    private TextView allmoney;

    @ViewInject(value = R.id.wobutput)
    private Button put;

    private static OkHttpHelper helper;

    private String consignee;
    private String phone;
    private String addr;

    private CheckBox wx;
    private CheckBox alipay;

    @ViewInject(value = R.id.wotv)
    private TextView name;

    @ViewInject(value = R.id.wotv2)
    private TextView addrtext;

    @ViewInject(value = R.id.wovp1)
   private Viewgroupdemo1 vgalpay;

    @ViewInject(value = R.id.wovp2)
   private Viewgroupdemo1 vgwx;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_order);
        x.view().inject(this);
         helper=OkHttpHelper.getInstance(this);
        pricesum=getIntent().getDoubleExtra("price",0);
        allmoney.setText(String.valueOf(pricesum));
        EventBus.getDefault().register(this);

        getSupportActionBar().hide();
        inittoolbar();
        coms=MyApplication.getMyApplication().getList();
         initview();
        initrl();
        getdefaultaddress();

        //默认使用支付宝支付
        vgalpay.setche(true);
        vgwx.setche(false);

        //设置点击事件
        vgalpay.setOnClickListener(this);
        vgwx.setOnClickListener(this);

       //结算
        put.setOnClickListener(this);
    }




    //跳转到收货地址
    private void initrl() {
        rl= (RelativeLayout) findViewById(R.id.worl);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WriteOrderActivity.this,AdressActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }


    //订单商品列表，无论多少商品就显示前面两个
    private void initview() {
       int sum=0;
       if(coms==null||coms.size()<=0)
           return;

        for(int i=0;i<coms.size();i++){
            Commodity com=coms.get(i);
              if(i==0) {
                  byte[] bitmap = com.getBitmap();
                  iv1.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
              }
            if(i==1){
                byte[] bitmap = com.getBitmap();
                iv2.setImageBitmap(BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length));
            }
             sum=com.getCount()+sum;
          }

          count.setText(sum+"件商品");
    }

    private void inittoolbar() {
      tool= (HomeToolbar) findViewById(R.id.wotool);
      tool.setNavigationOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              finish();
          }
      });
    }



    @Subscribe(threadMode= ThreadMode.MAIN)
    public void receivefromcar(ArrayList<Commodity> list){


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(resultCode==200){
             //从数据库中取出最新的默认地址
              getdefaultaddress();
         }

        if(requestCode== Contants.REQUEST_CODE_PAYMENT){
            if(resultCode== Activity.RESULT_OK){
                String result = data.getExtras().getString("pay_result");
                switch (result){
                    case "success" :  //支付成功
                        break;

                    case "fail" :    //支付失败
                        break;

                    case "cancel":  //支付异常  取消支付了等
                        break;
                }


            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    private  void getdefaultaddress() {
        //从数据库中取出最新的默认地址
        consignee= Sharutil.getString(this,"consignee") ;
        phone=Sharutil.getString(this,"phone");
        addr=Sharutil.getString(this,"addr");
        if(!TextUtils.isEmpty(consignee)){
        name.setText(consignee+"("+Sharutil.replacePhoneNum(phone)+")");
        addrtext.setText(addr);}
    }


   //实现支付方式的单选
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wovp1:
             if(vgalpay.getche()){
                 vgalpay.setche(false);
                  vgwx.setche(true);
                  payChannel=CHANNEL_WECHAT;
             }  else {
                 vgalpay.setche(true);
                 vgwx.setche(false);
                 payChannel=CHANNEL_ALIPAY;
             }

                break;
            case R.id.wovp2:
               if(vgwx.getche()) {
                   vgalpay.setche(true);
                   vgwx.setche(false);
                   payChannel=CHANNEL_ALIPAY;
               } else {
                   vgalpay.setche(false);
                   vgwx.setche(true);
                   payChannel=CHANNEL_WECHAT;
               }
                break;
            case R.id.wobutput:

                break;
        }
    }




    //打开支付页面
    private void openPaymentActivity(String charge){


    }






    //订单提交成功后清除购物车数据
    private void clearcar() {
      for(Commodity commodity:coms){
            DataSupport.delete(Commodity.class,commodity.getId());
            Log.i("tmd",commodity.getName()+"被删除了");
        }
    }



}
