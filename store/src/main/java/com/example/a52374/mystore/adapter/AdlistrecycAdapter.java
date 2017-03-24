package com.example.a52374.mystore.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.Address;

import org.litepal.crud.DataSupport;

import java.util.List;

import dmax.dialog.SpotsDialog;

/**
 * Created by 52374 on 2017/3/18.
 */


//AdressActivity里面的recyclerview的适配器
public class AdlistrecycAdapter extends BaseAdapter<Address,BaseViewHolder> {

    private  AddressLisneter lisneter;

    public AddressLisneter getLisneter() {
        return lisneter;
    }

    public void setLisneter(AddressLisneter lisneter) {
        this.lisneter = lisneter;
    }

    public AdlistrecycAdapter(Context context, int layoutResId, List<Address> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Address item) {
        Log.i("tmd","--------"+item.getId()+"-----"+item.getIsDefault());
        viewHoder.getTextView(R.id.txt_name).setText(item.getConsignee());
        viewHoder.getTextView(R.id.txt_phone).setText(replacePhoneNum(item.getPhone()));
        viewHoder.getTextView(R.id.txt_address).setText(item.getAddr());
        final CheckBox checkBox = viewHoder.getCheckBox(R.id.cb_is_defualt);

        final boolean isDefault = item.getIsDefault();
        checkBox.setChecked(isDefault);
        viewHoder.getTextView(R.id.txt_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.delete(Address.class,item.getId());
                datas.remove(item);
                notifyItemRemoved(viewHoder.getLayoutPosition());
            }
        });


        if(isDefault){
            checkBox.setText(R.string.defaulutadd);
        } else {
              checkBox.setText(R.string.setdefaulut);

              checkBox.setClickable(true);

             checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                 @Override
                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){

                           item.setIsDefault(1);
                            SpotsDialog mDialog=new SpotsDialog(buttonView.getContext(),R.string.sports);
                           //首先将原本的TRUE改为false
                            ContentValues contentValues =new ContentValues();
                            contentValues.put("isDefault",0);
                            DataSupport.updateAll(Address.class,contentValues,"isDefault = ?","1");

                         //然后再将默认地址修改到数据库

                            ContentValues contentValues1=new ContentValues();
                            contentValues1.put("isDefault",true);
                            DataSupport.update(Address.class,contentValues1,item.getId());

                            lisneter.setDefault(item,mDialog);
                        }
                 }
           });
        }


    }

    public String replacePhoneNum(String phone){

        return phone.substring(0,phone.length()-(phone.substring(3)).length())+"****"+phone.substring(7);
    }


   //
    public interface AddressLisneter{


        public void setDefault(Address address   , SpotsDialog mDialog);

    }
}
