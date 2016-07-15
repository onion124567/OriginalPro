package com.open.teachermanager.business.baseandcommon;

import com.open.teachermanager.utils.DialogManager;

import rx.functions.Action2;

/**
 * 如果网络异常不需要其他操作，只Toast可以
 * Created by Administrator on 2016/5/24.
 */
public class BaseToastNetError<View> implements Action2<View, Throwable> {

    @Override
    public void call(View v, Throwable throwable) {
        DialogManager.dismissNetLoadingView();
       /* if (throwable instanceof RetrofitError) {
            RetrofitError error= (RetrofitError) throwable;
            switch (error.getKind()){
                case HTTP:
                    Toast.makeText(TApplication.getInstance(), "服务器异常，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case NETWORK:
                    Toast.makeText(TApplication.getInstance(), "网络连接错误，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case CONVERSION:
                    break;
                case UNEXPECTED:
                    break;

            }

        } else {
            Toast.makeText(TApplication.getInstance(),  throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }
}
