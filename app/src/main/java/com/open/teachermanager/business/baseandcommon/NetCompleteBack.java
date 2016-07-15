package com.open.teachermanager.business.baseandcommon;

import android.widget.Toast;

import com.open.teachermanager.factory.bean.OpenResponse;
import com.open.teachermanager.utils.DialogManager;

import rx.functions.Action2;

/**
 * 返回OpenResponse
 * Created by Administrator on 2016/5/24.
 */
public abstract class NetCompleteBack<View, OpenRespones> implements Action2<View, OpenResponse> {
    @Override
    public void call(View v, OpenResponse tOpenResponse) {
        DialogManager.dismissNetLoadingView();
        switch (tOpenResponse.getCode()) {
            case 200:
                onComplete(v, tOpenResponse);
                break;
            case 800:
                Toast.makeText(TApplication.getInstance(), "程序异常:" + tOpenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callBackServerError(v, tOpenResponse);
                break;
            case 700:
                TApplication.getInstance().exit();
                Toast.makeText(TApplication.getInstance(), "您的账号在别处登陆。", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(TApplication.getInstance(), "程序异常" + tOpenResponse.getMessage(), Toast.LENGTH_SHORT).show();
                callBackServerError(v, tOpenResponse);
                break;
        }

    }

    public abstract void onComplete(View v, OpenResponse t);

    public void callBackServerError(View v, OpenResponse t) {

    }
}
