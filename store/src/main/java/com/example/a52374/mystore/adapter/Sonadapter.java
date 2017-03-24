package com.example.a52374.mystore.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.Son;
import com.example.a52374.mystore.http.Imageloader;

import java.util.List;

/**
 * Created by 52374 on 2017/2/25.
 */

public class Sonadapter extends BaseAdapter<Son,BaseViewHolder> {
    private Imageloader imageloader=Imageloader.getimageloader();

    public Sonadapter(Context context, int layoutResId, List<Son> datas) {
        super(context, layoutResId, datas);
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Son item) {
            viewHoder.getTextView(R.id.sontv).setText(item.getName());
            viewHoder.getTextView(R.id.sontv2).setText(item.getPrice()+"");
        ImageView iv=viewHoder.getImageView(R.id.soniv);
        iv.setImageResource(R.mipmap.ic_launcher);
        imageloader.loadimage(item.getImgUrl(),iv);
    }
}
