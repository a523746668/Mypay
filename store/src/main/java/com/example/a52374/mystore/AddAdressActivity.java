package com.example.a52374.mystore;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.example.a52374.mystore.bean.Address;
import com.example.a52374.mystore.city.model.CityModel;
import com.example.a52374.mystore.city.model.DistrictModel;
import com.example.a52374.mystore.city.model.ProvinceModel;
import com.example.a52374.mystore.city.model.XmlParserHandler;
import com.example.a52374.mystore.http.OkHttpHelper;
import com.example.a52374.mystore.wight.ClearEditText;
import com.example.a52374.mystore.wight.HomeToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


// 添加收货地址页面
public class AddAdressActivity extends AppCompatActivity {

   private HomeToolbar toolbar;

    private OkHttpHelper helper;

  private OptionsPickerView pickerView;

    private List<ProvinceModel> mProvinces;
    private ArrayList<ArrayList<String>> mCities = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<ArrayList<String>>> mDistricts = new ArrayList<ArrayList<ArrayList<String>>>();

    //姓名
     @ViewInject(value = R.id.edittxt_consignee)
    private ClearEditText ed1;


    //联系电话
    @ViewInject(value = R.id.edittxt_phone)
    private ClearEditText phone;


    //详细街道
    @ViewInject(value = R.id.edittxt_add)
    private ClearEditText madd;

    //省市区
    @ViewInject(value = R.id.txt_address)
    private TextView add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        x.view().inject(this);
        helper= OkHttpHelper.getInstance(this);
        getSupportActionBar().hide();
        inittoolbar();
        initpickviewdata();
        initpickview();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerView.show();
            }
        });
    }

    private void initpickview() {
       pickerView=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
           @Override
           public void onOptionsSelect(int options1, int options2, int options3, View v) {
               String addresss = mProvinces.get(options1).getName() +"  "
                       + mCities.get(options1).get(options2)+"  "
                       + mDistricts.get(options1).get(options2).get(options3);
              add.setText(addresss);

           }
       }).setTitleText("城市选择").setCyclic(false,false,false).build();
      pickerView.setPicker(mProvinces,mCities,mDistricts);


    }


    private void inittoolbar() {
        toolbar= (HomeToolbar) findViewById(R.id.adtool);
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 createaddress();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setResult(2);
                finish();
            }
        });
    }


      //创建地址并且向数据库保存
      private  void  createaddress(){

          String consignee = ed1.getText().toString();
          String phone = this.phone.getText().toString();
          String addr=add.getText().toString()+madd.getText().toString();

          if(!TextUtils.isEmpty(consignee)&&!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(addr))
          {
              Address address=new Address(consignee,phone,addr,0);
                address.save();
                 setResult(RESULT_OK);
                 finish();
          }
          else {
              Toast.makeText(this,R.string.wanshan,Toast.LENGTH_SHORT).show();
          }

      }

    //初始化Pickview的数据源
    private void initpickviewdata(){
        AssetManager asset = getAssets();

        try {
            InputStream is=asset.open("province_data.xml");
            SAXParserFactory factort=SAXParserFactory.newInstance();
            SAXParser parser=factort.newSAXParser();
            XmlParserHandler hand=new XmlParserHandler();
            parser.parse(is,hand);
            is.close();
            mProvinces=hand.getDataList();


        } catch (Exception e) {
            e.printStackTrace();
        }
          if(mProvinces!=null) {
                for(ProvinceModel p:mProvinces){
                   //一个省对应的城市对象
                    ArrayList<CityModel> cts= (ArrayList<CityModel>) p.getCityList();
                     //一个省所有的城市名字
                     ArrayList<String> ctsname=new ArrayList<>();

                    for(CityModel c:cts){
                  //一个城市所有的地区对象
                    List<DistrictModel> diss=c.getDistrictList();

                   //一个城市所有的地区名称
                    ArrayList<String>  dist=new ArrayList<>(diss.size());
                   //一个省的一个城市的所有地区的名字
                   ArrayList<ArrayList<String>> pd=new ArrayList<ArrayList<String>>();

                        for(DistrictModel dm: diss){
                      dist.add(dm.getName());

                    }

                pd.add(dist);
                 ctsname.add(c.getName());

                       //所有省的所有城市
                        mDistricts.add(pd);
                    }

                mCities.add(ctsname);

                }

          }

    }

}
