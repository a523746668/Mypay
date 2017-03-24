package com.example.a52374.mystore.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.a52374.mystore.LoginActivity;
import com.example.a52374.mystore.MyApplication;
import com.example.a52374.mystore.bean.User;

/**
 * Created by 52374 on 2017/2/24.
 */

public class BaseFragment extends Fragment {

     private MyApplication application=MyApplication.getMyApplication();

    public void startActivity(Intent intent,boolean isneedlogin) {
       if(isneedlogin){
           //如果需要登录，先检查用户是否登录
           User user=application.getuser();
           if(user!=null){
               //如果用户已经登录
               super.startActivity(intent);
           }

           else {
              //如果用户没登录，保存原有意图并且跳转到登录界面
               application.setIntent(intent);
               super.startActivity(new Intent(getActivity(), LoginActivity.class));
           }


       }else{
             super.startActivity(intent);
       }
    }
}
