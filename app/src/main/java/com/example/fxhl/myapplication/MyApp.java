package com.example.fxhl.myapplication;

import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

/**
 * Created by huang on 2017/3/25.
 */

public class MyApp extends Application {
    private static final String TAG = "MyApp";
    private static final int MSG_CUSTOM_MSG = 1;
    private static final int MSG_TOAST = 2;

    private Context mContext;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tip;
            switch (msg.what) {
                case MSG_CUSTOM_MSG:
                    String body = (String) msg.obj;
                    Toast.makeText(mContext, body, Toast.LENGTH_LONG).show();
                    break;
                case MSG_TOAST:
                    tip = (String) msg.obj;
                    Toast.makeText(mContext, tip, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initSdk();
    }

    private void initSdk() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                Log.d(TAG, "push handler device token:" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
        mPushAgent.setMessageHandler(messageHandler);
    }

    UmengMessageHandler messageHandler = new UmengMessageHandler() {
        @Override
        public void setPrevMessage(UMessage uMessage) {
            super.setPrevMessage(uMessage);
        }

        @Override
        public void handleMessage(Context context, UMessage uMessage) {
            super.handleMessage(context, uMessage);
        }

        @Override
        public void dealWithNotificationMessage(Context context, final UMessage uMessage) {
            super.dealWithNotificationMessage(context, uMessage);
        }

        @Override
        public void dealWithCustomMessage(Context context, UMessage uMessage) {
            super.dealWithCustomMessage(context, uMessage);
            if (uMessage != null) {
                Log.d(TAG, "push handler custom msg:" + uMessage.getRaw());
                JSONObject jsonObject = uMessage.getRaw();
                if (jsonObject != null) {
                    String body = jsonObject.optString("body");
                    if (body != null) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = MSG_CUSTOM_MSG;
                        msg.obj = body;
                        mHandler.sendMessage(msg);
                    }
                }
            }
        }

        @Override
        public PendingIntent getClickPendingIntent(Context context, UMessage uMessage) {
            return super.getClickPendingIntent(context, uMessage);
        }

        @Override
        public PendingIntent getDismissPendingIntent(Context context, UMessage uMessage) {
            return super.getDismissPendingIntent(context, uMessage);
        }

        @Override
        public boolean isInNoDisturbTime(Context context) {
            return super.isInNoDisturbTime(context);
        }

        @Override
        public int getNotificationDefaults(Context context, UMessage uMessage) {
            return super.getNotificationDefaults(context, uMessage);
        }

        @Override
        public boolean startDownloadResourceService(Context context, UMessage uMessage) {
            return super.startDownloadResourceService(context, uMessage);
        }

        @Override
        public int getSmallIconId(Context context, UMessage uMessage) {
            if (uMessage != null) {
                Log.d(TAG, "push handler small icon:" + uMessage.icon);
                if (uMessage.icon != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_CUSTOM_MSG;
                    msg.obj = "small icon:" + uMessage.icon;
                    mHandler.sendMessage(msg);
                }
            }
            return super.getSmallIconId(context, uMessage);
        }

        @Override
        public Bitmap getLargeIcon(Context context, UMessage uMessage) {
            if (uMessage != null) {
                Log.d(TAG, "push handler large icon:" + uMessage.largeIcon);
                Log.d(TAG, "push handler large img:" + uMessage.img);
                String largeIcon = uMessage.largeIcon;
                if (TextUtils.isEmpty(largeIcon)) {
                    largeIcon = uMessage.img;
                }
                if (largeIcon != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_CUSTOM_MSG;
                    msg.obj = "large/img icon:" + largeIcon;
                    mHandler.sendMessage(msg);
                }
            }
            return super.getLargeIcon(context, uMessage);
        }

        @Override
        public Uri getSound(Context context, UMessage uMessage) {
            if (uMessage != null) {
                Log.d(TAG, "push handler sound :" + uMessage.sound);
                if (uMessage.sound != null) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = MSG_CUSTOM_MSG;
                    msg.obj = "sound:" + uMessage.sound;
                    mHandler.sendMessage(msg);
                }
            }
            return super.getSound(context, uMessage);
        }

        @Override
        public Notification getNotification(Context context, UMessage uMessage) {
            return super.getNotification(context, uMessage);
        }
    };

    private void makeToast(CharSequence tip) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_TOAST;
        msg.obj = tip;
        mHandler.sendMessage(msg);
    }
}
