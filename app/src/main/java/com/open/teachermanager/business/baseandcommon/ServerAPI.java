package com.open.teachermanager.business.baseandcommon;


import com.open.teachermanager.factory.bean.RegistJpushRequest;
import com.open.teachermanager.factory.bean.OpenResponse;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

public interface ServerAPI {

    //    public static final String ENDPOINT = "http://10.96.8.104:8080/recruit";
//    public static final String ENDPOINT = "http://10.96.8.76:8081";
    public static final String ENDPOINT = "http://10.96.8.100:8080/recruit/";

    //汇报极光id
    @POST("push/regist.mv")
    Observable<OpenResponse> pushRegist(@Body RegistJpushRequest request);
}
