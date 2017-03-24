package com.example.a52374.mystore;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.a52374.mystore.bean.tab;
import com.example.a52374.mystore.fragment.Cartfragment;
import com.example.a52374.mystore.fragment.Catagoryfragment;
import com.example.a52374.mystore.fragment.Homefragment;
import com.example.a52374.mystore.fragment.Hotfragment;
import com.example.a52374.mystore.fragment.Minefragment;
import com.example.a52374.mystore.wight.FragmentTabHost;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;

public class MainActivity extends  BaseActivity {
     private FragmentTabHost fragmentTabHost;
    private LayoutInflater inflater;
    private ArrayList<tab> tabs=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        fragmentTabHost= (FragmentTabHost) findViewById(R.id.tabhost);
        fragmentTabHost.setup(this,getSupportFragmentManager(),R.id.mainfl);
        inflater=LayoutInflater.from(this);
        inittabhost();


    }

    private void inittabhost() {
       tab tab_home=new tab(R.string.home, R.drawable.shome,Homefragment.class);
        tab tab_catagory=new tab(R.string.catagory, R.drawable.s_catagory,Catagoryfragment.class);
        tab tab_hot=new tab(R.string.hot, R.drawable.s_hot, Hotfragment.class);
        tab tab_cart=new tab(R.string.cart, R.drawable.s_cart,Cartfragment.class);
        tab tab_mine=new tab(R.string.mine, R.drawable.s_mine,Minefragment.class);
        tabs.add(tab_home);
        tabs.add(tab_hot);
        tabs.add(tab_catagory);
        tabs.add(tab_cart);
        tabs.add(tab_mine);
        for(tab tab1 :tabs){
            TabHost.TabSpec tabspec=fragmentTabHost.newTabSpec(getString(tab1.getTitle()));
            View view=inflater.inflate(R.layout.tab_indicator,null);
            ImageView iv= (ImageView) view.findViewById(R.id.tab_indicator_iv);
            iv.setImageResource(tab1.getIcon());
            TextView tv= (TextView) view.findViewById(R.id.tab_indicator_tv);
            tv.setText(tab1.getTitle());
            tabspec.setIndicator(view);
            fragmentTabHost.addTab(tabspec,tab1.getFragment(),null);
        }
        fragmentTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        fragmentTabHost.setCurrentTab(0);
    }
}
