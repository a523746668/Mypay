package com.example.a52374.mystore.http;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by 52374 on 2017/2/19.
 */

public class Okhttputilss {
     private static Okhttputilss okhttputilss =new Okhttputilss();
     private OkHttpClient  client;
     private Okhttputilss(){
          client=new OkHttpClient();
     }
     public static Okhttputilss getOkhttputilss(){
         return okhttputilss;
     }

    //封装一个简单的请求Json数据并且解析成javabean的Get请求
     public <T extends Object>void   get(final Activity activity, String url, final Class<T> tClass, final textcallback<T> ttextcallback) {
          Request request = new Request.Builder().url(url).get().build();
          client.newCall(request).enqueue(new Callback() {
               @Override
               public void onFailure(Request request, IOException e) {
                   Log.i("tmd","11111111111111") ;
               }

               @Override
               public void onResponse(Response response) throws IOException {
                   Log.i("tmd",response.body().string());
                    final T zb = new Gson().fromJson(response.body().string(), tClass);
                    activity.runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                              if (ttextcallback != null) {
                                   ttextcallback.sendtext(zb);
                              }}
                    });
               }
          });
     }

     //封装一个post方法用于传递键值对数据到服务器并且得到回传数据
     //通过同步接口实现
     public <T extends Object> T  post(Map<String,String> map,String url , textcallback<T> ttextcallback,Class<T> tClass){
         FormEncodingBuilder formEncodingBuilder=new FormEncodingBuilder();
          Set<String> keys=map.keySet();
          for(String s:keys){
               formEncodingBuilder.add(s,map.get(s));
          }
          RequestBody post=formEncodingBuilder.build();
          Request request=new Request.Builder().post(post).url(url).build();
          try {
               Response response= client.newCall(request).execute();
               T zb=new Gson().fromJson(response.body().toString(),tClass);
               return  zb;

          }
          catch (IOException e) {
               e.printStackTrace();
          }

        return null;
     }




public interface textcallback<T>{
     void sendtext(T result);}

}