
package com.example.a52374.mystore.msg;



//登录请求后返回的数据对应的Bean
public class LoginRespMsg<T> extends BaseRespMsg {


    private String token;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
