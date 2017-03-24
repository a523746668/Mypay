package com.example.a52374.mystore.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.a52374.mystore.Contants;

import java.util.Map;

/**
 * Created by 52374 on 2017/3/3.
 */

public class Sharutil {


    public static String getString(Context context,String key){
        SharedPreferences sharedPreferences=context.getSharedPreferences(Contants.user,Context.MODE_PRIVATE);
         return sharedPreferences.getString(key,null);
       }
    public static  void putUserandtoken(Context context, String user,String token){
        SharedPreferences sharedPreferences=context.getSharedPreferences(Contants.user,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user",user);
        editor.putString("token",token);
        Log.i("tmd"," 修改成功");
        editor.commit();

    }

     public static void putstring (Context context,String key,String value){
         SharedPreferences sharedPreferences=context.getSharedPreferences(Contants.user,Context.MODE_PRIVATE);
         SharedPreferences.Editor editor=sharedPreferences.edit();
         editor.putString(key,value);

         editor.commit();
          Log.i("tmd",key+"------"+sharedPreferences.getString(key,"null"));
     }

    public static void putstring(Context context,Map<String,String> map){
        SharedPreferences sharedPreferences=context.getSharedPreferences(Contants.user,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        for(String key:map.keySet()){
            editor.putString(key,map.get(key));
          }
        editor.commit();

    }


    public static String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


}
