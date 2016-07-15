package com.open.teachermanager.business.baseandcommon;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;

import com.activeandroid.ActiveAndroid;
import com.facebook.drawee.backends.pipeline.Fresco;


import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TApplication extends Application {

    private static ServerAPI serverAPI;
    private static TApplication instance;
    private Location location;
    private ArrayList<Activity> mList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPI.ENDPOINT)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(genericClient())
                .build();

        serverAPI = retrofit.create(ServerAPI.class);
        ActiveAndroid.initialize(this);
        instance = this;
        Fresco.initialize(this);
    }

    public OkHttpClient genericClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                /*MediaType mediaType = request.body().contentType();
                try {
                    Field field = mediaType.getClass().getDeclaredField("mediaType");
                    field.setAccessible(true);
                    field.set(mediaType, "application/json");
//                    field.set("latait","0.0");
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }*/
                return chain.proceed(request.newBuilder().addHeader("latait","0.0").addHeader("time","xxxooos").build());
            }
        }).addInterceptor(logging);
        return builder.build();
    }

    public static ServerAPI getServerAPI() {
        return serverAPI;
    }

    public static TApplication getInstance() {
        return instance;
    }

    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return mList.remove(activity);
    }

    public boolean isActivityIn(Activity acitivity) {
        return mList.contains(acitivity);
    }

    public void exit() { // 遍历List，退出每一个Activity
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            mList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        PreferencesHelper.getInstance().clearBean(LoginBean.class);
//		finally {//2.5.3版本以前退出是直接推出软件的，后来进入登录注册页，此段代码注释
//			System.exit(0);
//			MobclickAgent.onKillProcess(this);
//		}
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
        instance = null;
    }


    public String getVersion() {
        PackageManager manager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (packageInfo != null) {
//            return packageInfo.versionCode;
            return packageInfo.versionName;
        }
        return "1.0";
    }

}
