package com.example.a52374.mystore.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.a52374.mystore.R;
import com.example.a52374.mystore.adapter.Caradapter;
import com.example.a52374.mystore.adapter.DivideritemDecortion;
import com.example.a52374.mystore.bean.Commodity;
import com.example.a52374.mystore.bean.Hotitem;
import com.example.a52374.mystore.http.Imageloader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/22.
 */
//购物车

public class Cartfragment extends  BaseFragment  implements View.OnClickListener {
    private Button but_complie;
    private Button but_finish;
    private Button but_delete;
    private Button but_guanzhu;
    private RecyclerView recyclerView;
    private CheckBox all;
    private Button settle;
    private TextView allmoneny;
    private TextView tv2;
    private TextView tv3;
    private ArrayList<Hotitem> list=new ArrayList<>();//接收Hot页面传来的数据
    private SparseArray<Commodity>  carts=new SparseArray();
    private ArrayList<Commodity> coms=new ArrayList<>();
   private Imageloader imageloader=Imageloader.getimageloader();
    private Caradapter caradapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.cartfragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        init(view);
        getdatafromsqlite();
        initadaper();


    }

    private void initadaper() {
       caradapter=new Caradapter(coms,getActivity());
        caradapter.setAll(all);
        caradapter.setAllmoney(tv2);
        caradapter.setArray(carts);
        caradapter.setBut1(but_delete);
        caradapter.setBut2(but_guanzhu);
        caradapter.setBut3(settle);
        recyclerView.setAdapter(caradapter);
      recyclerView.addItemDecoration(new DivideritemDecortion());

    }

    private void init(View view) {
        allmoneny= (TextView) view.findViewById(R.id.carttv1);
        tv2= (TextView) view.findViewById(R.id.carttv2);
        tv3= (TextView) view.findViewById(R.id.carttv3);

        but_complie= (Button) view.findViewById(R.id.cartbut1);
        but_finish= (Button) view.findViewById(R.id.cartbut2);
       but_delete= (Button) view.findViewById(R.id.but_delete);
        but_guanzhu= (Button) view.findViewById(R.id.but_guanzhu);
        recyclerView= (RecyclerView) view.findViewById(R.id.cartcyc);
        all= (CheckBox) view.findViewById(R.id.cartcheck);
        settle= (Button) view.findViewById(R.id.but_sum);
       // allmoneny= (TextView) view.findViewById(R.id.carttv2);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
         but_complie.setOnClickListener(this);
        but_finish.setOnClickListener(this);
        settle.setOnClickListener(this);
        but_delete.setOnClickListener(this);
        but_guanzhu.setOnClickListener(this);

    }





    //接收从Hot页面传过来的数据
     @Subscribe(threadMode = ThreadMode.MAIN)
     public void recevihot(Hotitem hotitem){
        if((carts.get((int) hotitem.getId())!=null)){
            carts.get((int) hotitem.getId()).addcount();
            ContentValues values=new ContentValues();
            values.put("count",carts.get((int) hotitem.getId()).getCount());
            DataSupport.update(Commodity.class,values,hotitem.getId());

        } else {
            byte[] bytes=imageloader.getbitmapbyte(hotitem.getImgUrl());
           Commodity commodity= new Commodity();
            commodity.setName(hotitem.getName());
            commodity.setId((int) hotitem.getId());
            commodity.setPrice(hotitem.getPrice());
            commodity.setImgUrl(hotitem.getImgUrl());
            commodity.setSale(hotitem.getSale());
            commodity.setBitmap(bytes);
           commodity.save();
            coms.add(commodity);
            carts.put((int) hotitem.getId(),commodity);
          }
         caradapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    public void getdatafromsqlite() {

      coms.addAll (DataSupport.findAll(Commodity.class));
      if(coms.size()>0){
        for(Commodity c:coms){
           carts.put(c.getId(),c);
            Log.i("tmd","从数据库中取出了数据");
        }}
        //coms.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cartbut1:
               //编辑按钮
                allmoneny.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
               but_complie.setVisibility(View.GONE);
                but_complie.setClickable(false);
               settle.setVisibility(View.GONE);
               caradapter.checkAll_None(false);

                but_finish.setVisibility(View.VISIBLE);
                but_finish.setClickable(true);
                but_delete.setVisibility(View.VISIBLE);
                but_guanzhu.setVisibility(View.VISIBLE);

                break;
            case R.id.cartbut2:
             //完成按钮
                allmoneny.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
                but_complie.setVisibility(View.VISIBLE);
                but_complie.setClickable(true);
                settle.setVisibility(View.VISIBLE);

                but_finish.setVisibility(View.GONE);
                but_finish.setClickable(false);
                but_delete.setVisibility(View.GONE);
                but_guanzhu.setVisibility(View.GONE);
                caradapter.setiffinish(false);
               // caradapter.setCount();
                //caradapter.clearlist1();
                break;
            case R.id.but_sum:
                //结算按钮

             /*   if(caradapter.sell()){
                Intent sum=new Intent(getActivity(), WriteOrderActivity.class);
                startActivity(sum);}   */
                break;
            case R.id.but_delete:
             //删除按钮

                 caradapter.delete();

                break;
            case R.id.but_guanzhu:
                //关注按钮
               caradapter.collect();
                break;


        }

    }
}
