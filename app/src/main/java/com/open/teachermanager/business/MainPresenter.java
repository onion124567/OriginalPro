package com.open.teachermanager.business;


import android.os.Bundle;
import android.util.Log;

import com.open.teachermanager.business.baseandcommon.BaseToastNetError;
import com.open.teachermanager.business.baseandcommon.MPresenter;
import com.open.teachermanager.business.baseandcommon.NetCompleteBack;
import com.open.teachermanager.business.baseandcommon.TApplication;
import com.open.teachermanager.factory.bean.OpenResponse;
import com.open.teachermanager.factory.bean.RegistJpushRequest;

import rx.Observable;
import rx.functions.Func0;


public class MainPresenter extends MPresenter<MainActivity> {
   // final public int REQUEST_INDEX = 1;
    final public int REQUEST_REGIST = 2;
    private RegistJpushRequest request;
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        //註冊所有的請求
      /*  restartableLatestCache(REQUEST_INDEX,
                new Func0<Observable<OpenResponse<MainInfoBean>>>() {
                    @Override
                    public Observable<OpenResponse<MainInfoBean>> call() {
                        return TApplication.getServerAPI().index(request);
                    }
                },
                new NetCallBack<MainActivity, MainInfoBean>() {
                    @Override
                    public void onComplete(MainActivity activity, MainInfoBean bean) {
                        activity.updateUI(bean);
                    }
                },
                new BaseToastNetError<MainActivity>());*/

        restartableLatestCache(REQUEST_REGIST,
                new Func0<Observable<OpenResponse>>() {
                    @Override
                    public Observable<OpenResponse> call() {
                        return TApplication.getServerAPI().pushRegist(request);
                    }
                },
                new NetCompleteBack<MainActivity, OpenResponse>() {
                    @Override
                    public void onComplete(MainActivity activity, OpenResponse bean) {
                        Log.i("onion","bean"+bean);
                    }
                },
                new BaseToastNetError<MainActivity>());

    }
   public void registJpush(){
       request=new RegistJpushRequest();
       start(REQUEST_REGIST);
   }
    /*public void getMainInfo(TeacherRequest request) {
        this.request = request;
        start(REQUEST_INDEX);
    }*/

}
