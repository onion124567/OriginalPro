package com.open.teachermanager.business.baseandcommon;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.open.teachermanager.presenter.RxPresenter;
import com.open.teachermanager.presenter.delivery.Delivery;
import com.open.teachermanager.utils.ACache;
import com.open.teachermanager.utils.StrUtils;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Created by onion on 2016/5/10.
 * for cache
 * <View>  type View's className and restartableId for cache key
 * <T>  T's Gsonformate is cache value
 */
public class MPresenter<View> extends RxPresenter<View> {
    final static int unNeedCache = -1;
    private String mPresenterName = getClass().getName();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
    }

    @Override
    protected void onTakeView(View view) {
        super.onTakeView(view);

    }

    /**
     * 复写为了 联网在后台，回调在UI   添加缓存
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>
     */
    @Override
    public <T> void restartableLatestCache(final int restartableId, final Func0<Observable<T>> observableFactory, final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {


        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                //this Thread main
                return observableFactory.call()
                        .compose(MPresenter.this.<T>deliverLatestCache()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(restartableId, onNext, onError));
            }
        });

    }

    /**
     * 复写为了 联网在后台，回调在UI 添加缓存
     * @param restartableId     an id of the restartable.
     * @param observableFactory a factory that should return an Observable when the restartable should run.
     * @param onNext            a callback that will be called when received data should be delivered to view.
     * @param onError           a callback that will be called if the source observable emits onError.
     * @param <T>
     */
    @Override
    public <T> void restartableFirst(int restartableId, final Func0<Observable<T>> observableFactory,
                                     final Action2<View, T> onNext, @Nullable final Action2<View, Throwable> onError) {

        restartable(restartableId, new Func0<Subscription>() {
            @Override
            public Subscription call() {
                return observableFactory.call()
                        .compose(MPresenter.this.<T>deliverFirst()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(split(onNext, onError));
            }
        });
    }
    public <T> Action1<Delivery<View, T>> split(final int restartableId, final Action2<View, T> onNext, final @Nullable Action2<View, Throwable> onError) {
        return new Action1<Delivery<View, T>>() {
            @Override
            public void call(Delivery<View, T> delivery) {
                //this Thread Retrofit-Idl
                delivery.split(onNext, onError);

                //for cache
                if (restartableId != unNeedCache && delivery.getResponseValue() != null) {
                    ACache.get(TApplication.getInstance()).put(mPresenterName + restartableId, delivery.getResponseValue().getClass().getName());
                    ACache.get(TApplication.getInstance()).put(StrUtils.string2md5(mPresenterName + restartableId), gson.toJson(delivery.getResponseValue()));
                }
            }
        };
    }

    /**
     * 获取cache的时候
     *
     * @param restartableId
     */
    public <T> T getCache(int restartableId) {
        //get cache
        String className = ACache.get(TApplication.getInstance()).getAsString(mPresenterName + restartableId);
        if (!TextUtils.isEmpty(className)) {
            try {
                Object o = gson.fromJson(ACache.get(TApplication.getInstance()).getAsString(StrUtils.string2md5(mPresenterName + restartableId)), Class.forName(className));
                return (T) o;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
