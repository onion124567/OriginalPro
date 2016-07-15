package com.open.teachermanager.business.baseandcommon;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.view.ScrollViewWithListener;
import com.common.view.shortcutbadger.ShortcutBadger;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.open.teachermanager.R;
import com.open.teachermanager.factory.PresenterFactory;
import com.open.teachermanager.factory.ReflectionPresenterFactory;
import com.open.teachermanager.presenter.Presenter;
import com.open.teachermanager.utils.Config;
import com.open.teachermanager.utils.ImageLoaderConfig;
import com.open.teachermanager.utils.ImageUtils;
import com.common.view.PresenterLifecycleDelegate;
import com.common.view.ViewWithPresenter;
import com.open.teachermanager.utils.StrUtils;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import rx.Observable;
import rx.functions.Action1;


/**
 * This class is an example of how an activity could controls it's presenter.
 * You can inherit from this class or copy/paste this class's code to
 * create your own view implementation.
 * <p/>
 * copy from com.common.view NucleusActivity   @2016.5.9 by onion
 *
 * @param <P> a type of presenter to return with {@link #getPresenter}.
 */
public abstract class BaseActivity<P extends Presenter> extends FragmentActivity implements ViewWithPresenter<P> {

    private static final String PRESENTER_STATE_KEY = "presenter_state";

    private PresenterLifecycleDelegate<P> presenterDelegate =
            new PresenterLifecycleDelegate<>(ReflectionPresenterFactory.<P>fromViewClass(getClass()));

    /**
     * Returns a current presenter factory.
     */
    public PresenterFactory<P> getPresenterFactory() {
        return presenterDelegate.getPresenterFactory();
    }

    /**
     * Sets a presenter factory.
     * Call this method before onCreate/onFinishInflate to override default {@link ReflectionPresenterFactory} presenter factory.
     * Use this method for presenter dependency injection.
     */
    @Override
    public void setPresenterFactory(PresenterFactory<P> presenterFactory) {
        presenterDelegate.setPresenterFactory(presenterFactory);
    }

    /**
     * Returns a current attached presenter.
     * This method is guaranteed to return a non-null value between
     * onResume/onPause and onAttachedToWindow/onDetachedFromWindow calls
     * if the presenter factory returns a non-null value.
     *
     * @return a currently attached presenter or null.
     */
    public P getPresenter() {
        return presenterDelegate.getPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TApplication.getInstance().addActivity(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfig.initImageLoader(this, Config.BASE_IMAGE_CACHE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    View view = getWindow().getDecorView();
                    ImageUtils.WindowHeight = view.getHeight();
                    ImageUtils.WindowWidth = view.getWidth();
                }
            }, 1000);
        }
        if (savedInstanceState != null)
            presenterDelegate.onRestoreInstanceState(savedInstanceState.getBundle(PRESENTER_STATE_KEY));

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //5.0以后展示沉浸状态栏，避免页面被状态栏覆盖做一下操作
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(PRESENTER_STATE_KEY, presenterDelegate.onSaveInstanceState());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //删掉红点
        boolean success = ShortcutBadger.removeCount(this);
        presenterDelegate.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenterDelegate.onPause(isFinishing());
    }


    /**
     * 设置当前ActionBar 的标题
     *
     * @param str
     */
    protected ImageView iv_back, iv_right;
    protected TextView tv_title, tv_right;

    protected void initTitle(String title, String right) {
        findTitle();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText(title);
        if (!TextUtils.isEmpty(right)) {
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setText(right);
            iv_right.setVisibility(View.GONE);
        }
    }

    protected void initTitle(int titleid, String right) {
        String str = getResources().getString(titleid);
        initTitle(str, right);
    }

    protected void setTitleRightPic(int resId, View.OnClickListener listener) {
        iv_right.setVisibility(View.VISIBLE);
        iv_right.setImageResource(resId);
        iv_right.setOnClickListener(listener);
    }

    private void findTitle() {
        /*iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_right = (ImageView) findViewById(R.id.iv_right);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);*/
    }

    public void showToast(int id) {
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }


    /**
     * 積分動畫 通過Subscriber返回
     *
     * @param scoer
     * @param sub
     */
    protected void scoerChangeAnimation(final double scoer, final Action1<Double> sub) {
        double begin = scoer / 3;
        final double i = scoer / 3 * 2 / 20;
        for (double d = 0; d < 20; d++) {
            double v = begin + i * d;
        }
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Observable.just((double) msg.what).subscribe(sub);
                if (msg.what + i < scoer)
                    sendEmptyMessageDelayed((int) (msg.what + i), 30);
                else {
                    Observable.just(scoer).subscribe(sub);
                }
            }
        };
        handler.sendEmptyMessage((int) begin);
    }

    /**
     * 通過listview的headview計算距離，講應該漸變的色值 及headview是否顯示
     * 通過Subscriber返回
     *
     * @param listView
     * @param sub
     */
    public AbsListView.OnScrollListener initListviewHeadAndTopColor(final View headView, ListView listView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {//headview顯示
                    if (!isHeadViewVisable) {
                        isHeadViewVisable = true;
                    }
                    //考慮漸變
                    //1計算比率
                    double privet = (headView.getHeight() + headView.getY()) * 1D / headView.getHeight();
                    //r 色值
                    double r = 240 + (225 - 240) * privet;
                    double g = 84 + (77 - 84) * privet;
                    double b = 80 + (46 - 80) * privet;
                    int color = Color.rgb((int) r, (int) g, (int) b);

                    Observable.just(color).subscribe(colorSub);
                } else if (firstVisibleItem == 1) {
                    if (isHeadViewVisable) {
                        isHeadViewVisable = false;
                    }
                }
                Observable.just(isHeadViewVisable).subscribe(neeVisable);
            }
        };
        listView.setOnScrollListener(scrollListener);
        return scrollListener;
    }

    /**
     * 通過listview的headview計算距離，講應該漸變的色值 及headview是否顯示
     * 通過Subscriber返回
     */
    public void initScrollHeadAndTopColor(final View headView, final View title, final ScrollViewWithListener scrollView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        scrollView.setOnScrollListener(new ScrollViewWithListener.OnScrollListener() {
                                           @Override
                                           public void onScroll(int scrollY) {
                                               if (headView.getHeight() - title.getHeight() >= scrollY) {//headview顯示
                                                   if (!isHeadViewVisable) {
                                                       isHeadViewVisable = true;

                                                   }
                                                   //考慮漸變
                                                   //1計算比率
                                                   double privet = scrollY * 1D / (headView.getHeight() - title.getHeight());
                                                   //r 色值
                                                   double r = 225 + (240 - 225) * privet;
                                                   double g = 77 + (84 - 77) * privet;
                                                   double b = 46 + (80 - 46) * privet;
                                                   int color = Color.rgb((int) r, (int) g, (int) b);
                                                   Observable.just(color).subscribe(colorSub);
                                               } else {
                                                   if (isHeadViewVisable) {
                                                       isHeadViewVisable = false;
                                                   }
                                               }
                                               Observable.just(isHeadViewVisable).subscribe(neeVisable);

                                           }
                                       }
        );

    }

    public void initScrollHeadAndTopAlpha(final View headView, final View title, final ScrollViewWithListener scrollView, final Action1<Integer> colorSub, final Action1<Boolean> neeVisable) {
        scrollView.setOnScrollListener(new ScrollViewWithListener.OnScrollListener() {
                                           @Override
                                           public void onScroll(int scrollY) {
                                               if (headView.getHeight() - title.getHeight() >= scrollY) {//headview顯示
                                                   if (!isHeadViewVisable) {
                                                       isHeadViewVisable = true;

                                                   }
                                                   //考慮漸變
                                                   //1計算比率
                                                   float color = scrollY * 1f / (headView.getHeight() - title.getHeight()) * 255;
                                                   Observable.just((int) color).subscribe(colorSub);
                                               } else {
                                                   if (isHeadViewVisable) {
                                                       isHeadViewVisable = false;
                                                   }
                                               }
                                               Observable.just(isHeadViewVisable).subscribe(neeVisable);

                                           }
                                       }
        );
    }

    //    private int titile_alpha_down=255;
//    private int titile_alpha_up=255;
//    private int titile_red_down=255;
//    private int titile_red_up=255;
//    private int titile_green_down=255;
//    private int titile_green_up=255;
//    private int titile_blue_down=255;
//    private int titile_blue_up=255;
    boolean isHeadViewVisable = true;
    int main_red = 0xffe14d2e;//225,77,46
    int main_light_red = 0xfff05450;//240,84,80


    /**
     * 下拉刷新模塊
     */
    protected PtrClassicFrameLayout mPtrFrame;

    /**
     * for loadmore
     * @param action1
     * @param contentView
     */
    protected void initPtrFrameLayout(final Action1<String> action1,final View contentView) {
//        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_layout);
//        StoreHouseHeader header = new StoreHouseHeader(this);
//        header.setPadding(0, 40, 0, 40);
//        header.initWithString("ZHAOSHENG");
        // header
        final MaterialHeader header = new MaterialHeader(this);
        int[] colors = getResources().getIntArray(R.array.loading_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, StrUtils.dip2px(this, 15), 0, StrUtils.dip2px(this, 10));
        header.setPtrFrameLayout(mPtrFrame);
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, contentView==null?content:contentView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                action1.call("用戶下拉了");
            /*    mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 1500);*/
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
    }

    protected void initPtrFrameLayout(final Action1<String> action1) {
        initPtrFrameLayout(action1, null);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        TApplication.getInstance().removeActivity(this);
    }



}
