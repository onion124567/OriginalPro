package com.open.teachermanager.business.baseandcommon;

import com.open.teachermanager.factory.bean.OpenResponse;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func0;

/**
 * 用于不同View的回调
 * Created by Administrator on 2016/5/30.
 */
@Deprecated
public class FPresenter<View> extends MPresenter<View> {
      // T 为所传的body , 暂不考虑Path及QuaryMap的情況
    //  Action1为 回调。  OpenResponse为响应数据
      protected  void addFreeRequest(int restartableId, Func0<Observable<OpenResponse>> func0, final Action1<OpenResponse> action1){
          restartableFirst(restartableId,func0,
//                new Func0<Observable<OpenResponse>>() {
//                    @Override
//                    public Observable<OpenResponse> call() {
//                        return TApplication.getServerAPI().schoolDetail(request);
//                    }
//                },
                  new NetCompleteBack<View, OpenResponse>() {
                      @Override
                      public void onComplete(View activity, OpenResponse bean) {
                          Observable.just(bean).subscribe(action1);
                      }
                  },
                  new BaseToastNetError<View>());
      }

}
