package com.example.a52374.mystore.wight;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a52374.mystore.R;

/**
 * Created by 52374 on 2017/2/25.
 */

public class Myaddsubview extends LinearLayout implements View.OnClickListener{
    private LayoutInflater inflater;
    private Button add,sub;
    private TextView textView;
    private OnButtonClickListener onButtonClickListener;
    private  int value=1;
    private int minValue=1;
    private int maxValue=100;//默认100

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.addbt:
                addValue();
                if (onButtonClickListener!=null){
                    onButtonClickListener.onButtonAddClick(v,this.value);
                }
                break;
            case R.id.subbt:
               subValue();
                if (onButtonClickListener!=null){
                    onButtonClickListener.onButtonSubClick(v,this.value);
                }
                break;
        }
    }

    public interface  OnButtonClickListener{

        public void onButtonAddClick(View view, int value);
        public void onButtonSubClick(View view, int value);

    }
    public Myaddsubview(Context context) {
        super(context);
        init(context);
    }

    public Myaddsubview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

      }

    private void init(Context context) {
        inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.add_sub_view,this,true);
        add= (Button) view.findViewById(R.id.addbt);
        sub= (Button) view.findViewById(R.id.subbt);
        textView= (TextView) view.findViewById(R.id.addtext);
        add.setOnClickListener(this);
        sub.setOnClickListener(this);

    }

    public Myaddsubview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

   public void setValue(int value){
         this.value=value;
         textView.setText(String.valueOf(value));
   }
   public void addValue(){
         if(value<maxValue){
          ++value;}
        textView.setText(String.valueOf(value));
   }
    public void subValue(){
       if(value>1){
        --value;}
        textView.setText(String.valueOf(value));
    }
    public void setMaxValue(int num){
        this.maxValue=num;
    }
    public void setOnButtonClickListener(OnButtonClickListener listener){
        this.onButtonClickListener=listener;
    }

}
