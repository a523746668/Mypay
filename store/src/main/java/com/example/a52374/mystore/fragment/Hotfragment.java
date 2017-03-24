package com.example.a52374.mystore.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.a52374.mystore.Contants;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.adapter.DivideritemDecortion;
import com.example.a52374.mystore.adapter.Hotrecycadapter;
import com.example.a52374.mystore.bean.Homehotitem;
import com.example.a52374.mystore.bean.Hotitem;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.http.SpotsCallBack;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/22.
 */

public class Hotfragment extends  BaseFragment {
  private Context context;
    private RecyclerView recyclerView;
    private MaterialRefreshLayout refreshLayout;
     private int pageSize=10;
     private  int curPage=1;
     private int totalPage=3;
  private OkHttpHelper helper;
     private ArrayList<Hotitem> list;
    private Hotrecycadapter myadapter;
    private int state=0;
    private final  int state_nomal=0;
    private final int state_refresh=1;
    private final int state_loadmore=2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hotfragment,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
       context=getActivity();
        helper=OkHttpHelper.getInstance(context);
      initview(view);
      getdata();
    }

  private void initview(View view) {
      recyclerView= (RecyclerView) view.findViewById(R.id.hotrecyc);
      refreshLayout= (MaterialRefreshLayout) view.findViewById(R.id.hotrefresh);
     recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
     recyclerView.addItemDecoration(new DivideritemDecortion());
      refreshLayout.setLoadMore(true);

      refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
          @Override
          public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                   curPage=1;
                   state=state_refresh;
                   getdata();
          }

          @Override
          public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                 if(curPage<totalPage){
                     ++curPage;
                     state=state_loadmore;
                    getdata();
                 } else {
                     Toast.makeText(context,"已经是最后一页了",0).show();
                     refreshLayout.finishRefreshLoadMore();
                 }
          }
      });


  }

  public void getdata() {
     String url= Contants.API.WARES_HOT+"?"+"curPage="+curPage+"&&pageSize="+pageSize;
    helper.get(url, new SpotsCallBack<Homehotitem>(getActivity()) {
      @Override
      public void onFailure(Request request, Exception e) {

      }

      @Override
      public void onSuccess(Response response, Homehotitem homehotitem) {
             list=  homehotitem.getList();
             showdata();


      }


      @Override
      public void onError(Response response, int code, Exception e) {

      }

      @Override
      public void onTokenError(Response response, int code) {

      }
    });
  }

   private void showdata(){
          switch (state){
              case state_nomal:
                  myadapter=new Hotrecycadapter(list,context);
                  recyclerView.setAdapter(myadapter);
                  break;
              case state_refresh:
                  myadapter.refreshdata(list);
                  refreshLayout.finishRefresh();
                  recyclerView.scrollToPosition(0);
                  state=state_nomal;
                  break;
              case state_loadmore:
                  int position=myadapter.getsize();
                  myadapter.loadmore(list);
                  refreshLayout.finishRefreshLoadMore();
                  recyclerView.scrollToPosition(position);
                  state=state_nomal;
                  break;
          }



   }
}
