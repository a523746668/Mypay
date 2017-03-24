package com.example.a52374.mystore.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a52374.mystore.Contants;
import com.example.a52374.mystore.LoginActivity;
import com.example.a52374.mystore.MyApplication;
import com.example.a52374.mystore.R;
import com.example.a52374.mystore.bean.User;
import com.example.a52374.mystore.http.Imageloader;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 52374 on 2017/2/22.
 */

public class Minefragment extends BaseFragment implements View.OnClickListener {

      private TextView gologin;
      private ImageView ivto;
      private Imageloader loader=Imageloader.getimageloader();
      private Button loginout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.minefragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gologin= (TextView) view.findViewById(R.id.txt_username);
        ivto= (ImageView) view.findViewById(R.id.img_head);
        gologin.setOnClickListener(this);
        ivto.setOnClickListener(this);
        loginout= (Button) view.findViewById(R.id.btn_logout);
        loginout.setOnClickListener(this);
       showuser();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_username:
            case R.id.img_head:
                Intent intent = new Intent(getActivity(), LoginActivity.class);

                startActivityForResult(intent, Contants.REQUEST_CODE);
                break;
            case R.id.btn_logout:
                 MyApplication.getMyApplication().clearuser();
                showuser();
                break;
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         //不需要回传数据，只要从Application里面的去取数据
           showuser();
    }

    //
    private void showuser() {

       if(getActivity().getSharedPreferences("user.txt",MODE_PRIVATE).getString("user",null)==null){
              gologin.setText(R.string.to_login);
       }   else {
           User user=MyApplication.getMyApplication().getuser();
          gologin.setText(user.getUsername());
          //loader.loadimage(user.getLogo_url(),ivto);
           ivto.setImageResource(R.drawable.default_head);
       }

    }
}
