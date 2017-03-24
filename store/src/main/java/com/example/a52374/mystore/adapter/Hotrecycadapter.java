package com.example.a52374.mystore.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a52374.mystore.CommodityDetailsActivity;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.Hotitem;
import com.example.a52374.mystore.http.Imageloader;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/25.
 */

public class Hotrecycadapter extends RecyclerView.Adapter<Hotrecycadapter.MyViewholder> implements View.OnClickListener {
      private ArrayList<Hotitem> list;
      private LayoutInflater inflater;
       private Context context;
    private Imageloader imageloader=Imageloader.getimageloader();

    public Hotrecycadapter(ArrayList<Hotitem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(parent.getContext());

        return new MyViewholder(inflater.inflate(R.layout.item_hot_rec   ,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, final int position) {
         Hotitem item=list.get(position);
         holder.name.setText(item.getName());
         holder.price.setText(String.valueOf(item.getPrice()));
         holder.iv.setImageResource(R.mipmap.ic_launcher);
         imageloader.loadimage(item.getImgUrl(),holder.iv);
        holder.but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

    }

    class MyViewholder extends RecyclerView.ViewHolder{
        TextView name,price;
        ImageView iv;
        Button but;

         public MyViewholder(View itemView) {
             super(itemView);
             name= (TextView) itemView.findViewById(R.id.hot_item_tv);
             price= (TextView) itemView.findViewById(R.id.hot_item_tv2);
             iv= (ImageView) itemView.findViewById(R.id.hotiv);
             but= (Button) itemView.findViewById(R.id.hot_item_but);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     int postion=getLayoutPosition();
                     Intent intent=new Intent(context, CommodityDetailsActivity.class);
                     intent.putExtra("11",list.get(postion));
                    context.startActivity(intent);
                 }
             });
         }
     }

    public void refreshdata(ArrayList<Hotitem> hotitems){
        list.clear();
         list.addAll(hotitems);
        notifyItemRangeChanged(0,hotitems.size());
    }

    public int getsize(){
        return  list.size();
    }
    public void loadmore(ArrayList<Hotitem> hotitems){
         int postion=list.size();
           list.addAll(hotitems);
          notifyItemRangeChanged(postion,list.size());

    }



}
