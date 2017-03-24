package com.example.a52374.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a52374.mystore.wight.HomeToolbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegistActivity extends BaseActivity {
    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";
   private SmsEventhandler handler;

    private TextView tv1,tv2;
    private EditText ed1,ed2;
    private HomeToolbar toolbar;

    private String phone,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
         getSupportActionBar().hide();
        SMSSDK.initSDK(this,"1bb7a278d2b52","f4e8d735f3d17a5243b6687174b0e4da");
        handler=new SmsEventhandler();
        SMSSDK.registerEventHandler(handler);
         init();
        inittoolbar();



        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {

            tv2.setText("+"+country[1]);

            tv1.setText(country[0]);

        }
    }

    private void inittoolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getcode();
           }
       });

    }

  //获取验证码
  private void getcode(){
      phone=ed1.getText().toString().trim();
      password=ed2.getText().toString().trim();
      String code=tv2.getText().toString().trim();
      checkPhoneNum(phone,code);
      //获取验证码，在注册的接收对象里返回值
     SMSSDK.getVerificationCode(code,phone);

  }

    //检查手机号码的格式
    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码",0).show();
            return;
        }

        if (code == "86") {
            if(phone.length() != 11) {
                Toast.makeText(this,"手机号码长度不对",0).show();
                return;
            }

        }

        String rule = "^1(3|5|7|8|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);

        if (!m.matches()) {
            Toast.makeText(this,"您输入的手机号码格式不正确",0).show();
            return;
        }

    }




    private void init() {
    tv1= (TextView) findViewById(R.id.txtCountry);
    tv2= (TextView) findViewById(R.id.textView2);
    ed1= (EditText) findViewById(R.id.editText);
    ed2= (EditText) findViewById(R.id.editText2);
        toolbar= (HomeToolbar) findViewById(R.id.rtool);

    }



    //注册一个回调接口
    class SmsEventhandler extends EventHandler{

       //afterEvent在操作结束时被触发，
       // 同样具备event和data参数，但是data是事件操作结果，
       // 其具体取值根据参数result而定。result是操作结果，
       // 为SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.RESULT_ERROR表示操作失败。
        @Override
        public void afterEvent(final int event, final int result,
                               final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                          //返回支持发送验证码的国家列表
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);

                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 请求验证码后，跳转到验证码填写页面
                            Log.i("tta", "拿到验证码了 ");
                            afterVerificationCodeRequested((Boolean) data);

                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

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
                                Toast.makeText(RegistActivity.this,des,0).show();
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }

                    }


                }
            });

        }
    }

    /** 请求验证码后，跳转到验证码填写页面 */
    private void afterVerificationCodeRequested(boolean smart){
            String code=tv2.getText().toString().trim();
           phone = ed1.getText().toString().trim().replaceAll("\\s*", "");
        if (code.startsWith("+")) {
            code = code.substring(1);
        }

        Log.i("tta",code+"----"+phone);
        Intent intent=new Intent(RegistActivity.this,Regist2Activity.class);
        intent.putExtra("phone",phone);
        intent.putExtra("pwd",password);
        intent.putExtra("code",code);
        startActivity(intent);


    }



    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }

            Log.d("tmd","code="+code + "rule="+rule);


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       SMSSDK.unregisterEventHandler(handler);
    }
}
