package com.example.a52374.mystore.wight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.a52374.mystore.R;


/**
 * Created by 52374 on 2016/12/25.
 */

// 实现界面下的条框
public class Viewgroupdemo1 extends RelativeLayout {
     private ImageView iv1;
     private CheckBox checkBox;
     private TextView textView;
     private Bitmap source;
    private LayoutInflater inflater;


    //自定义属性
     private String content; //内容
     private int bitresouce ; //图片资源
     private boolean che;     //设置check

    public Viewgroupdemo1(Context context) {
        super(context);
        initview(context);
    }

    public Viewgroupdemo1(Context context, AttributeSet attrs) {
        super(context, attrs);

        initview(context);

        TypedArray  ta=context.obtainStyledAttributes(attrs,R.styleable.Viewgroupdemo1);
        content=ta.getString(R.styleable.Viewgroupdemo1_content);
        setcontent();
        bitresouce=ta.getResourceId(R.styleable.Viewgroupdemo1_bitsouce,0);
        source= BitmapFactory.decodeResource(getResources(),bitresouce);
        if(source!=null){
            setiv(source);
        }
        che=ta.getBoolean(R.styleable.Viewgroupdemo1_istrue,false);
        setcheckbox();

    }

    private void setcheckbox() {
         if(checkBox!=null){
             checkBox.setChecked(che);
         }

    }

    private void setcontent() {
        if(!TextUtils.isEmpty(content)&&textView!=null){
            textView.setText(content);
        }

    }

    private void initview(Context context) {
         inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.viewgroup,this,true);
        textView= (TextView) findViewById(R.id.textView);
        iv1= (ImageView) findViewById(R.id.imageView);
        checkBox= (CheckBox) findViewById(R.id.checkbox1);

    }

    //此方法用于给参数图片缩放后加圆角显示
    public void setiv(Bitmap source){
       // Bitmap source1= Bitmap.createScaledBitmap(source,50,50,true);
        Bitmap bit=Bitmap.createBitmap(source.getWidth(),source.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bit);
        Paint paint=new Paint();
        RectF rect=new RectF(0,0,source.getWidth(),source.getHeight());
        paint.setAntiAlias(true);
       canvas.drawRoundRect(rect,15,15,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);
        iv1.setImageBitmap(bit);
    }

    public Viewgroupdemo1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    //外部点击事件之后调用此方法改版check状态
    public void changecheckbox(){
        che=!che;
        checkBox.setChecked(che);
    }
    //获得checkbox的状态
     public boolean getche(){
         return  che;
     }

    public void setche(boolean flag){
         che=flag;
         checkBox.setChecked(che);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

    }
}
