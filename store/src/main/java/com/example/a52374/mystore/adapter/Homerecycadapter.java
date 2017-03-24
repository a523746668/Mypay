package com.example.a52374.mystore.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a52374.mystore.CommoditylistActivity;
import com.example.a52374.mystore.Contants;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.Homecampaigm;
import com.example.a52374.mystore.http.Imageloader;

import java.util.ArrayList;

/**
 * Created by 52374 on 2017/2/24.
 */

public class Homerecycadapter extends RecyclerView.Adapter<Homerecycadapter.Myhoder> {
     private ArrayList<Homecampaigm> list;
    private LayoutInflater inflater;
    private Context context;
    private Imageloader imageloader;

    public Homerecycadapter(ArrayList<Homecampaigm> list, Context context) {
        this.list = list;
        this.context = context;
        imageloader=Imageloader.getimageloader();
    }

    @Override
    public Myhoder onCreateViewHolder(ViewGroup parent, int viewType) {
        inflater=LayoutInflater.from(parent.getContext());
        if(viewType==0){
           return new Myhoder(inflater.inflate(R.layout.template_home_cardview,parent,false));
       } else {
           return  new Myhoder(inflater.inflate(R.layout.template_home_cardview2,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(Myhoder holder, int position) {
         Homecampaigm homecampaigm=list.get(position);
        holder.textView.setText(homecampaigm.getTitle());
        holder.smalltwo.setImageResource(R.mipmap.ic_launcher);
        holder.big.setImageResource(R.mipmap.ic_launcher);
        holder.smallone.setImageResource(R.mipmap.ic_launcher);
        //Log.i("tmd","ddddd"+homecampaigm.getCpone()+"aaa");
        Log.i("tmd","ddddd"+homecampaigm+"aaa");
        imageloader.loadimage(homecampaigm.getCpOne().getImgUrl(),holder.big);
        imageloader.loadimage(homecampaigm.getCpTwo().getImgUrl(),holder.smallone);
        imageloader.loadimage(homecampaigm.getCpThree().getImgUrl(),holder.smalltwo);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
          if(position%2==0){
              return 0;
          } else {
              return  1;
          }
    }

    class Myhoder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView textView;
     ImageView big,smallone,smalltwo;

        public Myhoder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.text_title);
            big= (ImageView) itemView.findViewById(R.id.imgview_big);
            smallone= (ImageView) itemView.findViewById(R.id.imgview_small_top);
            smalltwo= (ImageView) itemView.findViewById(R.id.imgview_small_bottom);
            big.setOnClickListener(this);
            smallone.setOnClickListener(this);
            smalltwo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            startanim(v);
        }
       //点击图片之后开启一个动画
       private void startanim(final View view){
           ObjectAnimator animator=ObjectAnimator.ofFloat(view,"rotationX",0,360);
           animator.setDuration(1000);
          animator.addListener(new Animator.AnimatorListener() {
              @Override
              public void onAnimationStart(Animator animation) {

              }

              @Override
              public void onAnimationEnd(Animator animation) {
                Homecampaigm homecampaigm=list.get(getLayoutPosition());
                  switch (view.getId()){
                      case R.id.imgview_big:
                          Intent intent=new Intent(context, CommoditylistActivity.class);
                          intent.putExtra(Contants.COMPAINGAIN_ID,homecampaigm.getCpOne().getId());
                          context.startActivity(intent);
                          break;
                      case R.id.imgview_small_bottom:
                          Intent intent2=new Intent(context, CommoditylistActivity.class);
                          intent2.putExtra(Contants.COMPAINGAIN_ID,homecampaigm.getCpThree().getId());
                          context.startActivity(intent2);
                          break;
                      case R.id.imgview_small_top:
                          Intent intent1=new Intent(context, CommoditylistActivity.class);
                          intent1.putExtra(Contants.COMPAINGAIN_ID,homecampaigm.getCpTwo().getId());
                          context.startActivity(intent1);
                          break;
                  }
              }

              @Override
              public void onAnimationCancel(Animator animation) {

              }

              @Override
              public void onAnimationRepeat(Animator animation) {

              }
          });
           animator.start();

       }
    }
}
