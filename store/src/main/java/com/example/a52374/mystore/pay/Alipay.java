package com.example.a52374.mystore.pay;

import android.app.Activity;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Handler;

/**
 * Created by 52374 on 2017/3/24.
 */

//支付宝
public class Alipay {
    // 商户PID
    public static final String PARTNER = "";
    // 商户收款账号
    public static final String SELLER = "";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    private static final int SDK_PAY_FLAG = 1;

 // private    Activity activity;
 // private Handler  handler;




  //调用支付接口 获取结果
    public String pay(Activity activity,Handler handler,String payInfo) {
        // 构造PayTask 对象
        PayTask alipay = new PayTask(activity);

        // 调用支付接口，获取支付结果
        String result = alipay.pay(payInfo, true);

        return result;
    }



  //生成完整的符合支付宝参数规范的订单信息
   public String getpayInfo(String subject, String body, String price){
         String orderInfo=getOrderInfo(subject,  body,  price);
       String sign = sign(orderInfo);
       try {
           /**
            * 仅需对sign 做URL编码
            */
           sign = URLEncoder.encode(sign, "UTF-8");
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }
     return orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
   }



  //  生成订单
  public String getOrderInfo(String subject, String body, String price) {

      // 签约合作者身份ID
      String orderInfo = "partner=" + "\"" + PARTNER + "\"";

      // 签约卖家支付宝账号
      orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

      // 商户网站唯一订单号
      orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

      // 商品名称
      orderInfo += "&subject=" + "\"" + subject + "\"";

      // 商品详情
      orderInfo += "&body=" + "\"" + body + "\"";

      // 商品金额
      orderInfo += "&total_fee=" + "\"" + price + "\"";

      // 服务器异步通知页面路径
      orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

      // 服务接口名称， 固定值
      orderInfo += "&service=\"mobile.securitypay.pay\"";

      // 支付类型， 固定值
      orderInfo += "&payment_type=\"1\"";

      // 参数编码， 固定值
      orderInfo += "&_input_charset=\"utf-8\"";

      // 设置未付款交易的超时时间
      // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
      // 取值范围：1m～15d。
      // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
      // 该参数数值不接受小数点，如1.5h，可转换为90m。
      orderInfo += "&it_b_pay=\"30m\"";

      // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
      // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

      // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
      orderInfo += "&return_url=\"m.alipay.com\"";

      return orderInfo;
  }


   //生成订单号
   private String getOutTradeNo() {
       SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
       Date date = new Date();
       String key = format.format(date);

       Random r = new Random();
       key = key + r.nextInt();
       key = key.substring(0, 15);
       return key;
   }


    //对订单进行签名
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }


    //获得SDK版本号
    public void getSDKVersion(Activity context) {
        PayTask payTask = new PayTask(context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
