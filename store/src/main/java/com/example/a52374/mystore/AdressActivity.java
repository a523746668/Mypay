package com.example.a52374.mystore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cjj.MaterialRefreshLayout;
import com.example.a52374.mystore.Utils.Sharutil;
import com.example.a52374.mystore.adapter.AdlistrecycAdapter;
import com.example.a52374.mystore.adapter.DivideritemDecortion;
import com.example.a52374.mystore.bean.Address;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.wight.HomeToolbar;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;


//收货地址页面
public class AdressActivity extends AppCompatActivity {
    @ViewInject(value = R.id.alisttool)
    private HomeToolbar toolbar;

    @ViewInject(value = R.id.alistrefresh)
    private MaterialRefreshLayout material;

    @ViewInject(value = R.id.alistrecyc)
    private RecyclerView recyclerview;

    private AdlistrecycAdapter adapter;

    private ArrayList<Address> list=new ArrayList<>();

    private OkHttpHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);
        helper=OkHttpHelper.getInstance(this);
        x.view().inject(this);
        getSupportActionBar().hide();
        initoolbar();
        getdata();

    }

    //向数据库请求地址列表
    private  void getdata(){
       list.clear();

        list.addAll(DataSupport.findAll(Address.class));
        if(list!=null&&list.size()>0){
            Collections.sort(list);
            if(list.get(0).getIsDefault()) {
                addDefaulttoshared(list.get(0));
            }
            initrecycadapter();
        }


    }
    //初始化适配器
    private void initrecycadapter() {

        adapter=new AdlistrecycAdapter(this,R.layout.item_adlistrecyc,list);
          adapter.setLisneter(new AdlistrecycAdapter.AddressLisneter() {
              @Override
              public void setDefault(Address address,  SpotsDialog mDialog) {
                       updateAddress(address);
                      if(mDialog!=null&&mDialog.isShowing()){
                          mDialog.dismiss();
                  }
                }
          });
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(AdressActivity.this));
        recyclerview.addItemDecoration(new DivideritemDecortion());
    }

    private void initoolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list!=null&&list.size()>0){


                    setResult(200);

                } else {
                 setResult(0);
                }
                finish();
            }
        });
    toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(AdressActivity.this,AddAdressActivity.class);
           startActivityForResult(intent,1);
        }
    });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2)
            return;

        if (resultCode==RESULT_OK){
              getdata ();
        }
    }

    //修改默认地址时候调用
    private void updateAddress(Address address){

          getdata();
    }


    //每次有对收货地址有更改将最新的地址写入本地
    private void addDefaulttoshared(Address address){
        Sharutil.putstring(this,"consignee",address.getConsignee());
        Sharutil.putstring(this,"phone",address.getPhone());
        Sharutil.putstring(this,"addr",address.getAddr());
    }

}
