package com.example.a52374.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a52374.mystore.Utils.DESUtil;
import com.example.a52374.mystore.bean.User;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.example.a52374.mystore.msg.LoginRespMsg;
import com.example.a52374.mystore.wight.CountTimerView;
import com.example.a52374.mystore.wight.HomeToolbar;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;



public class Regist2Activity extends BaseActivity {

    private String phone;
    private String pwd;
    private String countryCode;
    private HomeToolbar toolbar;
    private SpotsDialog dialog;
    private SmsEventhandler handler;
    private String code;//验证码
    private Button but;
    private TextView tv;
    private EditText ed;
    private OkHttpHelper helper=OkHttpHelper.getInstance(this);
    private  CountTimerView count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist2);
       getSupportActionBar().hide();
           init();
           inittoolbar();
          Intent intent=getIntent();
          phone= intent.getStringExtra("phone");
          pwd=intent.getStringExtra("pwd");
          countryCode=intent.getStringExtra("code");

        String txttip="+"+countryCode+splitPhoneNum(phone);
        tv.setText(txttip);

        SMSSDK.initSDK(this,"1bb7a278d2b52","f4e8d735f3d17a5243b6687174b0e4da");
        handler=new SmsEventhandler();
        SMSSDK.registerEventHandler(handler);
         count=new CountTimerView(but);
        count.start();
        dialog = new SpotsDialog(this);
        dialog = new SpotsDialog(this,"正在校验验证码");


    }

    private void init() {
        toolbar= (HomeToolbar) findViewById(R.id.mtool);
         but= (Button) findViewById(R.id.rbut);

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("+"+countryCode,phone);
                Log.i("tmd","正在重新申请");
                dialog.setMessage("正在重新申请");
                dialog.dismiss();
                count=new CountTimerView(but,R.string.count);
                count.start();
            }
        });
        tv= (TextView) findViewById(R.id.txtTip);
        ed= (EditText) findViewById(R.id.red);
    }
    private void inittoolbar(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  finish();
            }
        });
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finishrigister();
            }
        });
    }
//提交验证码
   private void finishrigister(){
       dialog.show();
          code=ed.getText().toString().trim();
         if(!code.isEmpty()){
             SMSSDK.submitVerificationCode(countryCode,phone,code);

         }else {
             Toast.makeText(Regist2Activity.this,"验证码不能为空",0).show();
         }
   }
// 验证成功的话，提交注册信息
    private void doReg(){
        Map<String ,Object> params=new HashMap<>();
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY, pwd));
        helper.post(Contants.API.REG, params, new SpotsCallBack<LoginRespMsg<User>>(Regist2Activity.this) {
            @Override
            public void onFailure(Request request, Exception e) {
                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                Log.i("tmd","onfa");
            }

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();
                if(userLoginRespMsg.getStatus()==LoginRespMsg.STATUS_ERROR) {
                     Toast.makeText(Regist2Activity.this,"注册失败",0).show();
                     return;
                } else {
                   User user=userLoginRespMsg.getData();
                   MyApplication application=MyApplication.getMyApplication();
                    application.putuser(user,userLoginRespMsg.getToken());
                    Intent intent=new Intent(Regist2Activity.this,MainActivity.class);
                    startActivity(intent);
                }

            }


            @Override
            public void onError(Response response, int code, Exception e) {
                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();
                Log.i("tmd","error11111");
            }

            @Override
            public void onTokenError(Response response, int code) {
                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();

                Log.i("tmd","tokenerror");
            }
        });

    }


    class SmsEventhandler extends  EventHandler{

        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(dialog !=null && dialog.isShowing())
                    dialog.dismiss();
                  Log.i("tta","刚刚after");

                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                //验证成功，返回国家和手机号

//                              HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//                              String country = (String) phoneMap.get("country");
//                              String phone = (String) phoneMap.get("phone");

//                            ToastUtils.show(RegSecondActivity.this,"验证成功："+phone+",country:"+country);

                        Log.i("tta","验证成功正在提交注册");
                        doReg();
                        dialog.setMessage("正在提交注册信息");
                        dialog.show();
                    }
                } else {

                    // 根据服务器返回的网络错误，给toast提示
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;

                        JSONObject object = new JSONObject(
                                throwable.getMessage());
                        String des = object.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
//                                ToastUtils.show(RegActivity.this, des);
                            return;
                        }
                    } catch (Exception e) {
                        SMSLog.getInstance().w(e);
                    }

                }


            }
        });}
    }

    /** 分割电话号码 */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(handler);
    }
}
