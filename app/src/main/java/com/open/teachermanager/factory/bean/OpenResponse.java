package com.open.teachermanager.factory.bean;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONObject;

/**
 * 通用响应，解析外层数据。
 * Created by Administrator on 2016/5/19.
 */
public class OpenResponse<P> {


    /**
     * code : 200  成功  800 服务器异常
     * message : 操作成功
     * result : {"id":1,"loginname":"ceshi","phone":"123456789","token":"C4CA4238A0B923820DCC509A6F75849B","path":"","idcard":"370811199909099999","bigpath":"","teachername":"测试"}
     */

    private int code;
    private String message;
    private P result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public P getResult() {
        return result;
    }

    public void setResult(P result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "OpenResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }

    /**
     * 為了緩存
     * @param c
     * @return
     */
    public P parseBean(Class<P> c) {
        if (result == null) return null;
       LinkedTreeMap map= (LinkedTreeMap) result;
        JSONObject obj=new JSONObject(map);
        return new Gson().fromJson(obj.toString(),c);
    }
}
