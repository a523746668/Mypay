package com.example.a52374.mystore.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by 52374 on 2017/1/2.
 */
//封装一个用于图片内存管理的工具类
public class Imageloader {
    private  static Imageloader imageloader=new Imageloader(); //单例模式
    private LruCache<String ,Bitmap> lruCache;    //图片缓存的核心类
    private HashMap<String, SoftReference<Bitmap>> map;  //与弱缓存配合使用

    private ExecutorService mThreadpoll; //线程池
    //private int Threadpoolnum=1; //线程池中线程数量 默认为1

    private Thread backgroundThread; //后台轮询线程，最开始就要初始化
    private Handler backgroundhandler; //后台线程的handler,接受消息

    private Handler uihandeler;   //用于更新UI

    private boolean isDiskcache=false;// 是否开启硬盘缓存
    private boolean  isMemorycache=true;//是否开启内存缓存

     private Semaphore mthreadpoolsemaphore;
    private Semaphore  backsemaphore=new Semaphore(0);//保证后台线程一定被初始化

    private Type mtype= Type.LIFO;  //调度策略

    private LinkedList<Runnable> mTaskqueue=new LinkedList();

    //队列的调度方式
    public enum  Type{
        FIFO,LIFO;
    }

    //初始化线程池，通过Semaphore来实现线程池中资源的互斥
    private void init(){
        mThreadpoll= Executors.newFixedThreadPool(3);
        mthreadpoolsemaphore=new Semaphore(3);
          //开启一个后台线程，当有加载图片的任务传过来是，让线程池中的线程执行任务
        backgroundThread=new Thread(){
            @Override
            public void run() {
              Looper.prepare();
                backgroundhandler=new Handler(){
                  @Override
                  public void handleMessage(Message msg) {
                      mThreadpoll.execute(gettask());
                      try {
                          mthreadpoolsemaphore.acquire();
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }


                  }
              };
                backsemaphore.release();
                Looper.loop();
            }
        };
        backgroundThread.start();
    }


    //构造方法，初始化缓存核心类
    private Imageloader(){

        init();
       int maxmemory= (int) Runtime.getRuntime().maxMemory();
        int cachesize=maxmemory/8;
        map=new HashMap<>();
        lruCache=new LruCache<String,Bitmap>(cachesize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return  value.getByteCount();
            }


            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                if(evicted){
                 map.put(key,new SoftReference<Bitmap>(oldValue));
                    Log.i("tmd","从lrucache中移除了图片");
                }
            }
        };


    }
  public static Imageloader getimageloader(){
      return  imageloader;
  }


    // 根据imgurl从内存或者本地先取图片
    private    Bitmap getbitmap(String imgurl){
       Bitmap result=null;
        result=lruCache.get(imgurl);
        if(result==null){
            SoftReference<Bitmap> softReference=map.get(imgurl);
            if(softReference!=null){
                result=softReference.get();
            }

            if(result==null){
                String filename=imgurl.replaceAll("/","+");
                result= BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator
                +"zhf"+File.separator+filename);
            }
        }


        return  result;

    }


    private void addbitmap(String imgurl,Bitmap bitmap){

        //如果开启了内存缓存
        if(isMemorycache){
        lruCache.put(imgurl,bitmap);
        }
        //如果开启了硬盘缓存
        if(isDiskcache) {
            String url = imgurl.replace("/", "+");
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "zhf"
                    + File.separator;
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                FileOutputStream fos = new FileOutputStream(new File(file, url));
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

      //联网下载图片的压缩 并且返回图片
    private Bitmap samplebit(ImageView imageView,byte[] to){
             Bitmap bitmap=null;
          //需求宽高
           int  height=-1;
             int wight=-1;
            getimageviewsize(imageView,height,wight);

        //获取图片真实宽高
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeByteArray(to,0,to.length,options);
       int zheight=options.outHeight;
        int zwight=options.outWidth;

        //根据需求宽高和实际宽高计算Samplesize
        int insample=1;
       if(zheight>height||zwight>wight){
           int heightradio=Math.round(zheight*1.0f/height);
           int wightradio=Math.round(zwight*1.0f/wight);
           insample=Math.max(heightradio,wightradio);

       }
        options.inSampleSize=insample;
        options.inJustDecodeBounds=false;
        bitmap=BitmapFactory.decodeByteArray(to,0,to.length,options);
      return  bitmap;
    }
  //获得imageview的宽高，即我们的图片最终要压缩的大小
    private void getimageviewsize(ImageView imageView,int height,int widht){
       height=-1;
        widht=-1;
        DisplayMetrics displayMetrics=imageView.getContext().getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams= (ViewGroup.LayoutParams) imageView.getLayoutParams();
       widht=imageView.getWidth();
        if(widht<=0){
            widht=layoutParams.width;
        }
        if(widht<=0){
            widht=displayMetrics.widthPixels;
        }
        height=imageView.getHeight();
        if(height<=0){
            height=layoutParams.width;
        }
        if(height<=0){
            height=displayMetrics.widthPixels;
        }

    }

    //联网下载图片
    private Bitmap downloadbyurl(String url,ImageView imageView){
         Bitmap bitmap=null;
         InputStream is=null;
        ByteArrayOutputStream bos=null;
        try {
            HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            if(conn.getResponseCode()==200){
                 is=conn.getInputStream();
                bos=new ByteArrayOutputStream();
                byte[] bytes=new byte[1024];
                int num=0;
                while((num=is.read(bytes))!=-1){
                   bos.write(bytes,0,num);

                }
               byte[] to=bos.toByteArray();
             bitmap=BitmapFactory.decodeByteArray(to,0,to.length);
                return  bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                if(is!=null){
                is.close();}
                if(bos!=null){
                bos.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  bitmap;

    }



    //当内存和硬盘中没有图片，调用此方法新建一个任务
    private Runnable buildtask(final String imgurl , final ImageView imageView ){
        return  new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap=downloadbyurl(imgurl,imageView);

                //发送消息更新
                imagebean bean=new imagebean();
                bean.bit=bitmap;
                bean.imgurl=imgurl;
                bean.imageView=imageView;
                Message msg=Message.obtain();
                msg.obj=bean;
                uihandeler.sendMessage(msg);

                //下载完毕后释放许可
                mthreadpoolsemaphore.release();

                //加入内存和硬盘
                addbitmap(imgurl,bitmap);

            }
        };
    }

     //添加一个任务到任务队列
    private synchronized  void addtask(Runnable runnable){
        mTaskqueue.add(runnable);
        if(backgroundhandler==null){
            try {
                backsemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        backgroundhandler.sendEmptyMessage(0x110);
    }
    //从任务队列取出一个任务执行
    private   Runnable gettask(){
        if(mtype== Type.FIFO){
        //先进先出

           return  mTaskqueue.removeFirst();
        }else {
            //后进后出
           return  mTaskqueue.removeLast();
        }

    }


    //给图片加圆角
    private Bitmap addround(Bitmap source ){
        Bitmap bitmap1=Bitmap.createBitmap(source.getWidth(),source.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas=new Canvas(bitmap1);
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        RectF rectF=new RectF(0,0,source.getWidth(),source.getHeight());
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF,80,40,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source,0,0,paint);

        return  bitmap1;
    }

    //更新UI时传送的类
    class imagebean{
        Bitmap bit;
        String imgurl;
        ImageView imageView;
    }

    //拿到图片并且显示到控件，开放API
    public void loadimage(String imgurl, final ImageView imageView){
        imageView.setTag(imgurl);

        //初始化uihandler
        if(uihandeler==null){
            uihandeler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    imagebean bean= (imagebean) msg.obj;
                    Bitmap bit=bean.bit;
                    String url=bean.imgurl;
                    ImageView iv=bean.imageView;
                    Bitmap bitmap=addround(bit);
                    if(iv.getTag().toString().equalsIgnoreCase(url)){
                        iv.setImageBitmap(bitmap);
                    }
                }
            };
        }

        //先尝试从内存或者硬盘中获取图片
        Bitmap bitmap=getbitmap(imgurl);
        if(bitmap!=null){
            Log.i("tmd","从内存中取出了图片");
            imagebean bean=new imagebean();
            bean.bit=bitmap;
            bean.imageView=imageView;
            bean.imgurl=imgurl;
            Message message=Message.obtain();
            message.obj=bean;
            uihandeler.sendMessage(message);
        }else {
            addtask(buildtask(imgurl,imageView));
        }


    }


    //联网下载图片，得到二进制数组
    private byte[] downloadbyurltobyte(String url){

        InputStream is=null;
        ByteArrayOutputStream bos=null;
        try {
            HttpURLConnection conn= (HttpURLConnection) new URL(url).openConnection();
            conn.connect();
            if(conn.getResponseCode()==200){
                is=conn.getInputStream();
                bos=new ByteArrayOutputStream();
                byte[] bytes=new byte[1024];
                int num=0;
                while((num=is.read(bytes))!=-1){
                    bos.write(bytes,0,num);

                }
               return bos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                if(is!=null){
                    is.close();}
                if(bos!=null){
                    bos.close();}
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

return  null;
    }


    //获得图片的二进制数组
    public byte[] getbitmapbyte(String imgurl){
          byte[] bytes=new byte[1024];
          Bitmap bitmap=getbitmap(imgurl);
        if (bitmap!=null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            return  bos.toByteArray();
        } else {
            return  downloadbyurltobyte(imgurl);
        }
    }

}
