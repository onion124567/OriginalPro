package com.open.teachermanager.business.baseandcommon;

import android.widget.Toast;

import com.open.teachermanager.factory.bean.OpenResponse;
import com.open.teachermanager.utils.DialogManager;

import rx.functions.Action2;

/**
 * 直接返回bean
 * Created by Administrator on 2016/5/24.
 */
public abstract class NetCallBack<View, T> implements Action2<View, OpenResponse<T>> {
    @Override
    public void call(View v, OpenResponse<T> tOpenResponse) {
        DialogManager.dismissNetLoadingView();
        switch (tOpenResponse.getCode()) {
            case 200:
                callBack(v, tOpenResponse.getResult());
                break;
            case 800:
                Toast.makeText(TApplication.getInstance(), "程序异常:" + tOpenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callBackServerError(v, tOpenResponse);
                break;
            case 700:
                TApplication.getInstance().exit();
                break;
            default:
                Toast.makeText(TApplication.getInstance(), "程序异常" + tOpenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callBackServerError(v, tOpenResponse);
                break;
        }

    }

    public abstract void callBack(View v, T t);

    public void callBackServerError(View v, OpenResponse t) {

    }
}
