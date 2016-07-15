package com.open.teachermanager.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.open.teachermanager.R;

/**
 * Created by Administrator on 2015/7/16.
 */
public class Config {
    public static final String BASE_IMAGE_CACHE = "ONION";
    public static final String BASE_GROUP_CACHE = "TXONION";
    public static final String BASE_DOWNLOAD = "zhaoping";
    public static final String IMG_IRL = "";
    public static final String INTENT_PARAMS1 = "params1";
    public static final String INTENT_PARAMS2 = "params2";
    public static final String INTENT_PARAMS3 = "params3";
    public static final String INTENT_OrderId = "orderId";
    public static final String INTENT_String = "intentstring";
    public static final String INTENT_Boolean = "intentboolean";

    public static final String SIGN_AGREEMENT_URL = "https://www.baidu.com/";
//    public static final String LOGIN_TORUIST="loginist";//目前状态游客登录




   /* public static DisplayImageOptions logo_options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.img_learningbar_laucher)
            .showImageForEmptyUri(R.drawable.img_learningbar_laucher)
            .showImageOnFail(R.drawable.img_learningbar_laucher)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
//            .displayer(new RoundedBitmapDisplayer(20))
            .build();*/

    public static DisplayImageOptions circleIconDefault = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.icon_default)
            .showImageForEmptyUri(R.mipmap.icon_default)
            .showImageOnFail(R.mipmap.icon_default)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true).build();

    public static double defaultLat = 0;//39.92;
    public static double defaultLon = 0;//116.43;
}
