package com.example.a52374.mystore.http;

import android.content.Context;

import com.example.a52374.mystore.R;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import dmax.dialog.SpotsDialog;


public abstract class SpotsCallBack<T> extends BaseCallback<T> {



    private  SpotsDialog mDialog;

    public SpotsCallBack(Context context){


        initSpotsDialog(context);
    }


    private  void initSpotsDialog(Context context){

        mDialog = new SpotsDialog(context, R.string.sports);

    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }


   /* public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }  */


    @Override
    public void onBeforeRequest(Request request) {

        showDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }


}
