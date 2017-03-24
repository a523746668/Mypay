package com.example.a52374.mystore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.a52374.mystore.Contants;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.adapter.BaseAdapter;
import com.example.a52374.mystore.adapter.CategoryFather;
import com.example.a52374.mystore.adapter.Homevpadapter;
import com.example.a52374.mystore.adapter.Sonadapter;
import com.example.a52374.mystore.bean.Banner;
import com.example.a52374.mystore.bean.Father;
import com.example.a52374.mystore.bean.Page;
import com.example.a52374.mystore.bean.Son;
import com.example.a52374.mystore.http.Imageloader;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/22.
 */

public class Catagoryfragment extends  BaseFragment {
     private ViewPager vp;
     private RecyclerView father,son;
    private Homevpadapter vpadapter;
    private ArrayList<ImageView> list=new ArrayList<>();//vp的数据源
     private Context context;
    private Imageloader imageloader;
    private OkHttpHelper helper;
    private ArrayList<Father> list1;
    private CategoryFather adapter;
    private ArrayList<Son> list2;
    private Sonadapter adapter2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.catagory,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
         context=getActivity();
         helper=OkHttpHelper.getInstance(context);
        imageloader=Imageloader.getimageloader();
         initview(view);
          initviewpager();
          initfaher();
        initson();
    }

    private void initson() {
    String url= "http://112.124.22.238:8081/course_api/wares/list?categoryId=1&curPage=1&pageSize=10";
     helper.get(url, new SpotsCallBack<Page<Son>>(getActivity()) {
         @Override
         public void onFailure(Request request, Exception e) {
             Log.i("tmd","下载子类数据成功fai23333");
         }

         @Override
         public void onSuccess(Response response, Page<Son> sonPage) {
             list2= (ArrayList<Son>) sonPage.getList();
             Log.i("tmd","下载子类数据成功"+list2.size());

             adapter2=new Sonadapter(context,R.layout.catagoryson,list2);
             son.setAdapter(adapter2);

         }


         @Override
         public void onError(Response response, int code, Exception e) {
             Log.i("tmd","下载子类数据成功error");
         }

         @Override
         public void onTokenError(Response response, int code) {

         }
     });
    }

    private void initfaher() {
        father.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        helper.get(Contants.API.CATEGORY_LIST, new SpotsCallBack<ArrayList<Father>>(getActivity()) {
            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, ArrayList<Father> fathers) {
                     list1=fathers;
                    adapter=new CategoryFather(context,R.layout.fatheritem,list1);
                  father.setAdapter(adapter);
                adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                          Father father=list1.get(position);
                          int id=father.getId();
                        change(id);
                    }
                });

            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
    //随着父类的点击而改变子类的数据
    private void change(int categoryId){
        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage=1"+"&pageSize=10";
       helper.get(url, new SpotsCallBack<Page<Son>>(getActivity()) {
           @Override
           public void onFailure(Request request, Exception e) {

           }

           @Override
           public void onSuccess(Response response, Page<Son> sonPage) {
                     adapter2.clear();
                     list2= (ArrayList<Son>) sonPage.getList();
                    adapter2.addData(list2);
           }


           @Override
           public void onError(Response response, int code, Exception e) {

           }

           @Override
           public void onTokenError(Response response, int code) {

           }
       });
    }

    private void initviewpager() {
        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";
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


                initvpadapter();

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
   private void initvpadapter(){
       vpadapter=new Homevpadapter(list,context);
       vp.setAdapter(vpadapter);
   }
    private void initview(View view) {
      vp= (ViewPager) view.findViewById(R.id.catagoryvp);
      father= (RecyclerView) view.findViewById(R.id.fatherrecyc);
        son= (RecyclerView) view.findViewById(R.id.sonrecyc);
        father.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        son.setLayoutManager(new GridLayoutManager(context,2));
    }
}
