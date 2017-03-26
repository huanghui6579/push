package com.example.fxhl.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.message.PushAgent;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.inapp.IUmengInAppMsgCloseCallback;
import com.umeng.message.inapp.InAppMessageManager;
import com.umeng.message.tag.TagManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Context mContext;

    private static final int MSG_TOAST = 1;

    TextView textView;
    EditText editText;
    Button btnAddTag;
    Button btnRemoveTag;
    PushAgent mPushAgent;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String tip;
            switch (msg.what) {
                case MSG_TOAST:
                    tip = (String) msg.obj;
                    Toast.makeText(mContext, tip, Toast.LENGTH_LONG).show();
                break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();

        initView();
        String deviceToken = mPushAgent.getRegistrationId();
        textView.setText(deviceToken);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.tv_result);
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", textView.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        editText = (EditText) findViewById(R.id.et_tag);
        btnAddTag = (Button) findViewById(R.id.btn_add_tag);
        btnRemoveTag = (Button) findViewById(R.id.btn_remove_tag);

        btnAddTag.setOnClickListener(this);
        btnRemoveTag.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.btn_show_card_message:
                InAppMessageManager.getInstance(this).showCardMessage(this, "main", new IUmengInAppMsgCloseCallback() {
                    @Override
                    public void onColse() {
                        Log.i(TAG, "card message close");
                    }
                });
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        String tag;
        switch (v.getId()) {
            case R.id.btn_add_tag:
                tag = editText.getText().toString();
                addTag(tag);
                break;
            case R.id.btn_remove_tag:
                tag = editText.getText().toString();
                removeTag(tag);
                break;
        }
    }

    private void addTag(String tag) {
        mPushAgent.getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                makeToast("tag 添加成功：" + result);
            }
        }, tag);
    }

    private void removeTag(String tag) {
        mPushAgent.getTagManager().delete(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                makeToast("tag 删除成功：" + result);
            }
        }, tag);
    }

    private void makeToast(CharSequence tip) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_TOAST;
        msg.obj = tip;
        mHandler.sendMessage(msg);
    }
}
