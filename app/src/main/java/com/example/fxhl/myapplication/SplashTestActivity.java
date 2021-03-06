package com.example.fxhl.myapplication;


import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.message.inapp.UmengSplashMessageActivity;

public class SplashTestActivity extends UmengSplashMessageActivity {

    @Override
    public boolean onCustomPretreatment() {
        InAppMessageManager mInAppMessageManager = InAppMessageManager.getInstance(this);
        mInAppMessageManager.setInAppMsgDebugMode(true);
        mInAppMessageManager.setMainActivityPath("com.example.fxhl.myapplication.MainActivity");
        return super.onCustomPretreatment();
    }
}
