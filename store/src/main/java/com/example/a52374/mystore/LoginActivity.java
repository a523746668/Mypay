package com.example.a52374.mystore;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.a52374.mystore.Utils.DESUtil;
import com.example.a52374.mystore.bean.User;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.example.a52374.mystore.msg.LoginRespMsg;
import com.example.a52374.mystore.wight.ClearEditText;
import com.example.a52374.mystore.wight.HomeToolbar;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    private Button login,zhuce,foget;
    private ClearEditText user,password;
    private HomeToolbar toolbar;
    private OkHttpHelper helper=OkHttpHelper.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initview();
        login();
    }



    private void login(){
       login.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
             String username=user.getText().toString().trim();
             String pass=password.getText().toString().trim();
              if(TextUtils.isEmpty(username)|TextUtils.isEmpty(pass)){
                  Toast.makeText(LoginActivity.this,"请输入账号密码",0).show();

              }else {

                  Map<String ,Object> map=new HashMap();
                    map.put("phone",username);
                    map.put("password", DESUtil.encode(Contants.DES_KEY,pass));
                   helper.post(Contants.API.LOGIN, map, new SpotsCallBack<LoginRespMsg<User>>(LoginActivity.this) {
                       @Override
                       public void onFailure(Request request, Exception e) {

                       }

                       @Override
                       public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {

                           Log.i("tmd","user="+userLoginRespMsg.getData().getId()+"token="+userLoginRespMsg.getToken());
                           MyApplication application=MyApplication.getMyApplication();
                           if(userLoginRespMsg.getStatus()==1){
                               User user=userLoginRespMsg.getData();
                               String token=userLoginRespMsg.getToken();
                               application.putuser(user,token);


                               if(   application.getIntent()!=null){
                                   application.jumpToTargetActivity(LoginActivity.this);
                               } else {
                                   setResult(RESULT_OK);
                                   finish();
                               }

                           }  else {
                               Toast.makeText(LoginActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                           }



                       }


                       @Override
                       public void onError(Response response, int code, Exception e) {
                                 Toast.makeText(LoginActivity.this,"JSON解析有问题",Toast.LENGTH_SHORT).show();
                       }

                       @Override
                       public void onTokenError(Response response, int code) {

                       }
                   });
              }

           }
       });
    }

    private void initview() {
       login= (Button) findViewById(R.id.login_btn);
      user= (ClearEditText) findViewById(R.id.login_user);
        password= (ClearEditText) findViewById(R.id.login_password);
        zhuce= (Button) findViewById(R.id.button);
        zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegistActivity.class);
                startActivity(intent);
            }
        });
        foget= (Button) findViewById(R.id.button2);
        toolbar= (HomeToolbar) findViewById(R.id.logintool);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

}
