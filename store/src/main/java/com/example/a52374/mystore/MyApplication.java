package com.example.a52374.mystore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.a52374.mystore.Utils.JSONUtil;
import com.example.a52374.mystore.Utils.Sharutil;
import com.example.a52374.mystore.bean.Commodity;
import com.example.a52374.mystore.bean.User;

import org.litepal.LitePalApplication;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;

public class MyApplication extends LitePalApplication{

    private SharedPreferences sharedPreferences;
    private User user=null;
    private  static MyApplication myApplication;
   private String Token=null;

    private ArrayList<Commodity> list;

    public ArrayList<Commodity> getList() {
        return list;
    }

    public void setList(ArrayList<Commodity> list) {
        this.list = list;
    }

    public static MyApplication getMyApplication(){

       return myApplication;
    }
    @Override
    public void onCreate() {
        super.onCreate();
       myApplication=this;
         if (myApplication==null){
             Log.i("tmd","nulll");
         }
        //初始化数据库
        SQLiteDatabase db = Connector.getDatabase();
      sharedPreferences=getSharedPreferences(Contants.user,MODE_PRIVATE);

        inituser();
    }



    //每次启动程序，先从数据库中取得用户数据
    private void inituser(){

       if (!TextUtils.isEmpty(Sharutil.getString(this,"user"))){
            user=JSONUtil.fromJson(Sharutil.getString(this,"user"),User.class);
       }
    }

    public User getuser(){
        return user;
    }

   //把user信息写入数据库
   public void putuser(User user,String token){
       //转换成json数据
       this.user=user;
       String userjson= JSONUtil.toJSON(user);
       Log.i("tmd","user="+userjson+"token="+token);
       Sharutil.putUserandtoken(this,userjson,token);

   }


    //清除数据库中的用户数据
    public void clearuser(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user",null);
        editor.putString("token",null);
        editor.commit();
    }

    public String getToken() {

       return Sharutil.getString(this,"token");
    }
    private Intent intent=null;

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    //跳转到原本的意图,并且清空
    public void jumpToTargetActivity(Context context){

        context.startActivity(intent);
        this.intent =null;

    }

}
