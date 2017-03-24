package com.example.a52374.mystore.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 52374 on 2017/2/24.
 */
//对recyclerview的item进行一定美化
public class DivideritemDecortion extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top=10;
        outRect.left=5;
        outRect.right=5;

    }
}
