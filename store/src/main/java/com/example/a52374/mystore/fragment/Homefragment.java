package com.example.a52374.mystore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.a52374.mystore.R;
import com.example.a52374.mystore.adapter.DivideritemDecortion;
import com.example.a52374.mystore.adapter.Homerecycadapter;
import com.example.a52374.mystore.adapter.Homevpadapter;
import com.example.a52374.mystore.bean.Banner;
import com.example.a52374.mystore.bean.Homecampaigm;
import com.example.a52374.mystore.http.Imageloader;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/22.
 */

public class Homefragment extends  BaseFragment {

   private boolean flag;
   private ViewPager vp;
    private RadioGroup rg;
    private EditText ed;
    private Homevpadapter vpadapter;
    private ArrayList<ImageView> list=new ArrayList<>();//vp的数据源
    private Context context;
    private ArrayList<Banner> mbanner;
   // private ArrayList<Banners.Banner>  banners;
    private Imageloader imageloader;
 //   private Okhttputilss okhttputilss;
    private OkHttpHelper helper;
    private RecyclerView recyclerView;
    private ArrayList<Homecampaigm> list1;//recycleview的数据源
    private Homerecycadapter recycadapter; //适配器

    private android.os.Handler mhandler=new android.os.Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       context=getActivity();
        imageloader=Imageloader.getimageloader();
        helper=OkHttpHelper.getInstance(context);
       // okhttputilss = Okhttputilss.getOkhttputilss();
       init(view);
        initdata();
        initrecycleview();

    }
  //初始化recycleview的数据
    private void initrecycleview() {
         initrecycleviewdata();

    }

    private void initrecycleviewdata() {
        String url="http://112.124.22.238:8081/course_api/campaign/recommend";

         helper.get(url, new SpotsCallBack<ArrayList<Homecampaigm>>(getActivity()) {
             @Override
             public void onFailure(Request request, Exception e) {
                 Log.i("tmd","图片加载失败");
             }

             @Override
             public void onSuccess(Response response, ArrayList<Homecampaigm> homecampaigms) {
                Log.i("tmd",homecampaigms.size()+"");
                 recycadapter=new Homerecycadapter(homecampaigms,context);
                 recyclerView.setAdapter(recycadapter);

             }


             @Override
             public void onError(Response response, int code, Exception e) {
                 Log.i("tmd","图片加载失败111");
             }

             @Override
             public void onTokenError(Response response, int code) {

             }
         });

    }

       private void initcycadaper(){
            recycadapter=new Homerecycadapter(list1,context);
           recyclerView.setAdapter(recycadapter);
    }

    private void initadapter() {
        vpadapter=new Homevpadapter(list,context);
        vp.setAdapter(vpadapter);
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                 rg.check(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                   switch (state){
                       case ViewPager.SCROLL_STATE_DRAGGING:
                           mhandler.removeCallbacks(r);
                             flag=true;
                            break;

                       case ViewPager.SCROLL_STATE_IDLE:
                        if(true){
                          mhandler.postDelayed(r,2000);
                            flag=false;
                        }

                           break;


                   }
            }
        });
       mhandler.postDelayed(r,2000);


    }

    Runnable r=new Runnable() {
        @Override
        public void run() {
           vp.setCurrentItem(vp.getCurrentItem()+1);
            mhandler.postDelayed(r,2000);
        }
    } ;

 //实现viewpager的数据初始化
    private void initdata() {
        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 vp.setCurrentItem(checkedId);
            }
        });

        helper.get(url, new SpotsCallBack<ArrayList<Banner>>(getActivity()) {

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, ArrayList<Banner> banners) {
                for(int i=0;i<banners.size();i++){
                    ImageView iv=new ImageView(context);
                    iv.setImageResource(R.mipmap.ic_launcher);

                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    list.add(iv);
                    imageloader.loadimage(banners.get(i).getImgUrl(),iv);
                }
                for(int i=0;i<list.size();i++){
                    RadioButton rb=new RadioButton(context);
                    rb.setId(i);
                  //  rb.setButtonDrawable(R.drawable.radioselector);
                    rg.addView(rb);
                }

                initadapter();
                rg.check(0);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void init(View view) {
         vp= (ViewPager) view.findViewById(R.id.homevp);
        rg= (RadioGroup) view.findViewById(R.id.homerg);
        ed= (EditText) view.findViewById(R.id.toolbar_searchview);
       recyclerView= (RecyclerView) view.findViewById(R.id.homerecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new DivideritemDecortion());
    }


}
