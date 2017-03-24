package com.example.a52374.mystore.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a52374.mystore.MyApplication;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.WriteOrderActivity;
import com.example.a52374.mystore.bean.Collect;
import com.example.a52374.mystore.bean.Commodity;
import com.example.a52374.mystore.wight.Myaddsubview;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 52374 on 2017/2/26.
 */

public class Caradapter extends RecyclerView.Adapter<Caradapter.ViewHoler1>  {
    private ArrayList<Commodity> coms;
    private Context context;
    private LayoutInflater inflater;
   // private ArrayList<Commodity> list=new ArrayList<>();
   // private ArrayList<Commodity> list1=new ArrayList<>();
    private SparseArray<Commodity> array;

    public void setArray(SparseArray<Commodity> array) {
        this.array = array;
    }

    private boolean isfinish=false;
    private int count=0; //被选中的条数
    private CheckBox all;  //
    private TextView allmoney;//


    // 获得结算等按钮
     private Button but1,but2,but3;



    public Button getBut1() {
        return but1;
    }

    public void setBut1(Button but1) {
        this.but1 = but1;
        this.but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public Button getBut2() {
        return but2;
    }

    public void setBut2(Button but2) {
        this.but2 = but2;
    }

    public Button getBut3() {
        return but3;

    }

    public void setBut3(Button but3) {
        this.but3 = but3;

        if(but3!=null) {
            this.but3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isnull())
                        return;

                    ArrayList<Commodity> list = new ArrayList<>();

                    for (Commodity commodity : coms) {
                        if (commodity.ischeck()) {
                            list.add(commodity);
                        }
                    }
                    if (list.size() > 0) {
                        MyApplication.getMyApplication().setList(list);
                        Intent i = new Intent(context, WriteOrderActivity.class);
                        i.putExtra("price",Double.parseDouble(allmoney.getText().toString()));
                        context.startActivity(i);
                    }

                }
            });
        }
    }

    public void setAllmoney(TextView allmoney) {
        this.allmoney = allmoney;
    }

    public void setAll(final CheckBox all) {
        this.all = all;
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(all.isChecked());
                getanshowprecie();
            }
        });
    }

    public Caradapter(ArrayList<Commodity> coms, Context context) {
        this.coms = coms;
        this.context = context;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public ViewHoler1 onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHoler1(inflater.inflate(R.layout.cartrecycitem,parent,false));
    }

    @Override
    public void onBindViewHolder(final ViewHoler1 holder, final int position) {
     final Commodity commodity=coms.get(position);
       holder.iv.setImageBitmap(BitmapFactory.decodeByteArray(commodity.getBitmap(),0,commodity.getBitmap().length));
       holder.name.setText(commodity.getName());
        holder.price.setText(String.valueOf(commodity.getPrice()));
        holder.addsun.setValue(commodity.getCount());
        holder.addsun.setOnButtonClickListener(new Myaddsubview.OnButtonClickListener() {
            @Override
            public void onButtonAddClick(View view, int value) {
                 commodity.addcount();
                getanshowprecie();
                ContentValues values=new ContentValues();
                values.put("count",commodity.getCount());
                DataSupport.update(Commodity.class,values,commodity.getId());
            }

            @Override
            public void onButtonSubClick(View view, int value) {
               commodity.suncount();
                getanshowprecie();
                ContentValues values=new ContentValues();
                values.put("count",commodity.getCount());
                DataSupport.update(Commodity.class,values,commodity.getId());
            }
        });
        holder.box.setChecked(commodity.ischeck());
        holder.box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               commodity.setIscheck(holder.box.isChecked());
                notifyItemChanged(position);
                checklisten();
                getanshowprecie();
            }
        });

    }

    @Override
    public int getItemCount() {
        return coms.size();
    }

    class ViewHoler1 extends  RecyclerView.ViewHolder{
      Myaddsubview addsun;
      TextView name,price;
      ImageView iv;
      CheckBox box;
        public ViewHoler1(View itemView) {
            super(itemView);
           addsun= (Myaddsubview) itemView.findViewById(R.id.addsub);
            iv= (ImageView) itemView.findViewById(R.id.cartitemiv);
            box= (CheckBox) itemView.findViewById(R.id.cartitemcb);
            name= (TextView) itemView.findViewById(R.id.cartitemtv);
            price= (TextView) itemView.findViewById(R.id.cartitemtv2);

        }
    }

    public void setiffinish(boolean flag){
         isfinish=flag;
    }

    //将count初始化
    public void setCount(){
        count=0;
    }
    //获得条数
    public int getCount(){
        return  count;
    }
    //是否全选
    public boolean isall(){
        return count==coms.size();
    }
    //设置all check的状态
    public void setall(){
        if(isall()){
            all.setChecked(true);
        }else {
            all.setChecked(false);
        }
    }
       //把选中的删除
       public void delete() {

             if(!isnull()){
                 return;
             }

        for(Iterator iterator=coms.iterator(); iterator.hasNext();){
              Commodity commodity= (Commodity) iterator.next();

            if (commodity.ischeck()){
                int position=coms.indexOf(commodity);
                iterator.remove();
                array.remove(commodity.getId());
                DataSupport.delete(Commodity.class,commodity.getId());
                notifyItemRemoved(position);

            }

        }

           checklisten();
           getanshowprecie();

       }





  //被选中的总价格
    private void getanshowprecie(){
        double sum=0;
        for(int i =0;i<coms.size();i++){
            if(coms.get(i).ischeck()){
                sum=sum+coms.get(i).getCount()*coms.get(i).getPrice();
            }
        }
        allmoney.setText(String.valueOf(sum));
    }

 public void checklisten(){
        if(!isnull()){
            all.setChecked(false);

        }

        int count=0;
        for(int i =0;i<coms.size();i++){
            if(!coms.get(i).ischeck()){
                all.setChecked(false);
                break;
            }else {
                count++;

            }
             if(count==coms.size()){
                 all.setChecked(true);
              }

        }



    }

    private boolean isnull(){
        return (coms!=null&&coms.size()>0);
    }

    public void checkAll_None(boolean isChecked){
        for(int i =0;i<coms.size();i++){
            coms.get(i).setIscheck(isChecked);
           notifyItemChanged(i);
        }
        checklisten();
    }

    //结算按钮时候运行此方法
     public boolean sell(){
          if(!isnull())
              return false;

          ArrayList<Commodity> list=new ArrayList<>();

           for(Commodity commodity:coms){
                if (commodity.ischeck()){
                     list.add(commodity);
                }
           }
         if(list.size()>0){
             EventBus.getDefault().post(list);
             return  true;
         }
         return  false;

     }


    //收藏按钮点击时候运行此方法 ,将选中的商品移入收藏列表
    public void collect(){
        if(!isnull())
            return;



        for(Iterator iterator=coms.iterator(); iterator.hasNext();){
            Commodity commodity= (Commodity) iterator.next();

            if (commodity.ischeck()){
                //删除的同时加入另外一个表
                Collect collect=new Collect(commodity.getBitmap(),commodity.getName(),commodity.getPrice());
                collect.save();

                int position=coms.indexOf(commodity);
                iterator.remove();
                array.remove(commodity.getId());
                DataSupport.delete(Commodity.class,commodity.getId());
                notifyItemRemoved(position);

            }

        }

    }



}
