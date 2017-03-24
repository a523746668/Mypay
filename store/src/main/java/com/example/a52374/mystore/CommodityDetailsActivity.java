package com.example.a52374.mystore;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.example.a52374.mystore.bean.Hotitem;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;


//商品详情页面
public class CommodityDetailsActivity extends BaseActivity {

     private Button back,fenxiang;
     private WebView webView;
    private Hotitem item;
    private webappinterface webapp;

    private SpotsDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_details);
         getSupportActionBar().hide();
        Serializable serializable=getIntent().getSerializableExtra("11");
        if (serializable==null)
            finish();


        item= (Hotitem) serializable;
         init();
        initwebview();
        dialog=new SpotsDialog(this,"load~~");
        dialog.show();
    }

    private void initwebview() {
        WebSettings set=webView.getSettings();
        set.setJavaScriptEnabled(true); //设置支持javascript
        set.setBlockNetworkImage(false);//
        set.setAppCacheEnabled(true);
        webapp=new webappinterface(this);
        webView.loadUrl(Contants.API.WARES_DETAIL);
        webView.addJavascriptInterface(webapp,"appInterface");
        webView.setWebViewClient(new WC());

    }

    class WC extends WebViewClient{

        //档webview加载完毕调用方此方法,我们在此时传入商品信息，显示
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if(dialog!=null&&dialog.isShowing()) {
                dialog.dismiss();
            }
             webapp.showDetail();

        }
    }

    class webappinterface {

       Context context;
        public webappinterface(Context context){
             this.context=context;
        }


        //通过此方法调用html中的方法传入值，让webview去加载相应商品的信息
        @JavascriptInterface
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  webView.loadUrl("javascript:showDetail("+item.getId()+")");
                }
            });
        }

        //将商品加入购物车
        @JavascriptInterface
        public void buy(long id){


            Toast.makeText(context,id+"加入购物车",0).show();
        }


       //将商加入收藏
        @JavascriptInterface
        public void addFavorites(long id){

            Toast.makeText(context,id+"加入收藏",0).show();
        }

    }

    private void init() {
     back= (Button) findViewById(R.id.but_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       fenxiang= (Button) findViewById(R.id.fenxiang);
       fenxiang.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });

       webView= (WebView) findViewById(R.id.webview);
    }



}
