package com.example.a52374.mystore;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.a52374.mystore.adapter.BaseAdapter;
import com.example.a52374.mystore.adapter.BaseViewHolder;
import com.example.a52374.mystore.bean.Hotitem;
import com.example.a52374.mystore.bean.Page;
import com.example.a52374.mystore.http.Imageloader;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

public class CommoditylistActivity extends  BaseActivity {

    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;


    private long  campaigmid;

    private TabLayout tab;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
   private int orderBy=0;
    private int curPage=1;
    private int pageSize=10;
    private Page<Hotitem> page;
    private ArrayList<Hotitem> list;
    private OkHttpHelper helper;
    private Imageloader loader=Imageloader.getimageloader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoditylist);
        getSupportActionBar().hide();
        campaigmid = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);
         helper=OkHttpHelper.getInstance(this);
        init();
        inittab();
        initrecyc();

    }

    private void init() {
        tab = (TabLayout) findViewById(R.id.listtab);
       // toolbar = (Toolbar) findViewById(R.id.listtool);
        recyclerView = (RecyclerView) findViewById(R.id.listrecyc);
        /*toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/
    }


    private void initrecyc() {
      String url=Contants.API.WARES_CAMPAIN_LIST+"?curPage="+curPage+"&pageSize="+pageSize+"&campaignId="+campaigmid+"&orderBy="+orderBy;
        Log.i("list",url);
         recyclerView.setLayoutManager(new GridLayoutManager(this,2));
         helper.get(url, new SpotsCallBack<Page<Hotitem>>(this) {
             @Override
             public void onFailure(Request request, Exception e) {

             }

             @Override
             public void onSuccess(Response response, Page<Hotitem> hotitemPage) {
                 list= (ArrayList<Hotitem>) hotitemPage.getList();
                 Log.i("tmd","list页面"+list.size());

                 recyclerView.setAdapter(new BaseAdapter<Hotitem,BaseViewHolder>(CommoditylistActivity.this,R.layout.item_hot_recyc,list) {


                     @Override
                     protected void convert(BaseViewHolder viewHoder, Hotitem item) {
                         viewHoder.getTextView(R.id.hot_item_tv).setText(item.getName());
                         viewHoder.getTextView(R.id.hot_item_tv2).setText(item.getPrice()+"");
                         ImageView iv=viewHoder.getImageView(R.id.hotiv);
                         iv.setImageResource(R.mipmap.ic_launcher);
                         loader.loadimage(item.getImgUrl(),iv);
                     }});}


             @Override
             public void onError(Response response, int code, Exception e) {

             }

             @Override
             public void onTokenError(Response response, int code) {

             }
         });


    }

    private void inittab() {
        tab.addTab(tab.newTab().setText(R.string.defaulttab).setTag(TAG_DEFAULT));
        tab.addTab(tab.newTab().setText(R.string.buynumber).setTag(TAG_SALE));

        tab.addTab(tab.newTab().setText(R.string.pricetab).setTag(TAG_PRICE));
        tab.setTabGravity(TabLayout.GRAVITY_FILL);//3个标签平分
        tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorPrimary));
        tab.setTabTextColors(R.color.colorPrimary, R.color.defaultTextColor);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                orderBy = (int) tab.getTag();
                pageSize=10;
                curPage=1;
                initrecyc();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }






}
