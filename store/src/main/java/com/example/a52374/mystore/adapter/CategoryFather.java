package com.example.a52374.mystore.adapter;

import android.content.Context;

import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.Father;

import java.util.List;

/**
 * Created by 52374 on 2017/2/25.
 */

public class CategoryFather extends BaseAdapter<Father,BaseViewHolder> {


    public CategoryFather(Context context, int layoutResId, List<Father> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Father item) {
        viewHoder.getTextView(R.id.fathertv).setText(item.getName());
    }


}
