package com.open.teachermanager.Receiver;

/**
 * Created by che on 2015/6/19.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.common.view.shortcutbadger.ShortcutBadger;
import com.google.gson.Gson;
import com.open.teachermanager.R;
import com.open.teachermanager.factory.PushNotice;
import com.open.teachermanager.utils.Config;
import com.open.teachermanager.utils.PreferencesHelper;


import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    Ringtone ringtone = null;
    protected AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            /*Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_TITLE));
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));*/
            String data = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            String title=bundle.getString(JPushInterface.EXTRA_TITLE);
            try {//防止后台传非法数据
//                PushNotice pushNotice = new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), PushNotice.class);
                // TODO:接收处理透传（payload）数据
//打开自定义的Activity
                Intent updateIntent = null;
                updateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent updatePendingIntent = PendingIntent.getActivity(context, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.icon_default);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        context).setSmallIcon(R.mipmap.icon_default)
                        .setContentTitle("同学")
                        .setContentText(data);
                mBuilder.setLargeIcon(btm);
                mBuilder.setAutoCancel(true);//自己维护通知的消失
                mBuilder.setContentIntent(updatePendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                    Notification notification=;
//                    notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
//                    notification.defaults |=;
                viberateAndPlayTone(context);
                int notifyid = 1;
                mNotificationManager.notify(notifyid, mBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            String data = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            String title=bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            //添加红点
            boolean success = ShortcutBadger.applyCount(context, 1);
            Log.i("onion", "Got Payload:" + data);
            Log.i("onion", "Got EXTRA_EXTRA:" + extra);
            Log.i("onion", "Got title:" + title);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            //删掉红点
            boolean success = ShortcutBadger.removeCount(context);
            //获取值
            String data = bundle.getString(JPushInterface.EXTRA_ALERT);
            String extra=bundle.getString(JPushInterface.EXTRA_EXTRA);
            String title=bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            PushNotice notice=new Gson().fromJson(extra,PushNotice.class);
            Log.i("onion", "Got Payload:" + data);
            Log.i("onion", "Got EXTRA_EXTRA:" + extra);
            Log.i("onion", "Got title:" + title);
           /* Intent i = null;
            //打开自定义的Activity
            LoginBean bean = (LoginBean) PreferencesHelper.getInstance().getBean(LoginBean.class);
            if (bean != null) {//已经登陆了
                switch (notice.pushTarget){
                    case "PushTo->messge_list":
                        i=new Intent(context, NotifyChatActivity.class);
                        i.putExtra(Config.INTENT_PARAMS2,notice.getPusherId());
                        i.putExtra(Config.INTENT_PARAMS1, notice.getpType());
                        i.putExtra(Config.INTENT_String,notice.getPusherName());
                        break;
                    default:
                        i = new Intent(context, MainActivity.class);
                }

            } else {
                i = new Intent(context, LoginActivity.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);*/
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
/*		if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}*/
    }

    long lastNotifiyTime = 0;

    public void viberateAndPlayTone(Context context) {


        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        try {
            lastNotifiyTime = System.currentTimeMillis();

            // 判断是否处于静音模式
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            }

            long[] pattern = new long[]{0, 180, 80, 120};
            vibrator.vibrate(pattern, -1);
            //    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            if (ringtone == null) {
                Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                ringtone = RingtoneManager.getRingtone(context, notificationUri);
                if (ringtone == null) {
//                        EMLog.d(TAG, "cant find ringtone at:" + notificationUri.getPath());
                    return;
                }
            }

            if (!ringtone.isPlaying()) {
                String vendor = Build.MANUFACTURER;

                ringtone.play();
                // for samsung S3, we meet a bug that the phone will
                // continue ringtone without stop
                // so add below special handler to stop it after 3s if
                // needed
                if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                    Thread ctlThread = new Thread() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if (ringtone.isPlaying()) {
                                    ringtone.stop();
                                }
                            } catch (Exception e) {
                            }
                        }
                    };
                    ctlThread.run();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
