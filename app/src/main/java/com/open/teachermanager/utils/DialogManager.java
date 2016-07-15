package com.open.teachermanager.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.view.loadingview.LevelLoadingRenderer;
import com.common.view.loadingview.LoadingDrawable;
import com.open.teachermanager.R;

/**
 * 项目名称：zhaosheng1.0
 * 类描述：dialog通用类
 * 创建人：zhougl
 * 创建时间：2016/5/27 15:42
 * 修改人：zhougl
 * 修改时间：2016/5/27 15:42
 * 修改备注：
 */
public class DialogManager {
    private static Dialog netDialog;

    public static void showNormalDialog(Context context, String title, String info, String btnStr, final DialogInterface.OnClickListener listener){
        final Dialog dialog = new Dialog(context, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_normal_layout);
        TextView tv_dialog_title = (TextView) dialog.findViewById(R.id.tv_dialog_title);
        TextView tv_dialog_text = (TextView) dialog.findViewById(R.id.tv_dialog_text);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        tv_dialog_title.setText("" + title);
        tv_dialog_text.setText("" + info);
        btn_ok.setText("" + btnStr);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
            }
        });
        dialog.show();
    }

    public static void showBindingBankCardDialog(Context context,DialogInterface.OnClickListener listener){
        showNormalDialog(context,"提现","还未绑定本人银行卡,还不能进行提现哦!","去绑定银行卡",listener);
    }
    public static void showNetLoadingView(Context context){
        netDialog = new Dialog(context, R.style.CustomDialog);
        netDialog.setContentView(R.layout.dialog_net_loading_layout);
        ImageView iv_net_loading = (ImageView) netDialog.findViewById(R.id.iv_net_loading);
        LinearLayout ll_main_layout = (LinearLayout) netDialog.findViewById(R.id.ll_main_layout);
        final LoadingDrawable mLevelDrawable = new LoadingDrawable( new LevelLoadingRenderer(context));
        iv_net_loading.setImageDrawable(mLevelDrawable);
        mLevelDrawable.start();
        netDialog.show();
        netDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mLevelDrawable.stop();
            }
        });
        ll_main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netDialog.dismiss();
            }
        });
        // 设置全屏大小
        int[] screenRect = getScreenRect((Activity) context);
        netDialog.getWindow().setLayout(screenRect[0], screenRect[1]);
    }

    public static  void dismissNetLoadingView(){
        if(netDialog!=null){
            netDialog.dismiss();
        }
    }

    /**
     * 获取当前屏幕的全屏大小
     *
     * @param activity
     * @return
     */
    public static int[] getScreenRect(Activity activity) {

        WindowManager manager = activity.getWindowManager();
        int sreenW = manager.getDefaultDisplay().getWidth();
        int sreenH = manager.getDefaultDisplay().getHeight();
        return new int[]{sreenW, sreenH};
    }

}
