package com.example.a52374.mystore.bean;

/**
 * Created by 52374 on 2017/2/22.
 */
 // 主页面底部fragementtabhost
public class tab {
    private int title;
    private int icon;
    private Class fragment;

    public tab(int title, int icon, Class fragment) {
        this.title = title;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }
}
