package com.open.teachermanager.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.open.teachermanager.business.baseandcommon.TApplication;

/**
 * Created by Administrator on 2015/7/20.
 */
public class PreferencesHelper {
    final static String SPNAME = "ONION2015720";
    final static Gson gson = new Gson();
    private static PreferencesHelper instance = new PreferencesHelper();

    private PreferencesHelper() {
    }

    public static PreferencesHelper getInstance() {
        return instance;
    }

    //存bean
    public void saveBean(Object p) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(p.getClass().getName(), gson.toJson(p));
        edit.commit();
    }

    //取bean
    public Object getBean(Class c) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        return gson.fromJson(spf.getString(c.getName(), ""), c);
    }

    public void clearBean(Class c) {
        final SharedPreferences spf = TApplication.getInstance().getSharedPreferences(
                SPNAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = spf.edit();
        edit.putString(c.getName(), "");
        edit.commit();
    }


}
