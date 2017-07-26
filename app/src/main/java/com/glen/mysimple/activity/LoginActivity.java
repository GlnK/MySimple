package com.glen.mysimple.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.glen.mysdk.okhttp.listener.DisposeDataListener;
import com.glen.mysimple.R;
import com.glen.mysimple.activity.base.BaseActivity;
import com.glen.mysimple.manager.DialogManager;
import com.glen.mysimple.manager.UserManager;
import com.glen.mysimple.module.PushMessage;
import com.glen.mysimple.module.user.User;
import com.glen.mysimple.network.http.RequestCenter;
import com.glen.mysimple.network.mina.MinaService;

/**
 * Created by Gln on 2017/7/7.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUserNameView;
    private EditText mPasswordView;
    private TextView mLoginView;

    /**
     * data
     */
    private PushMessage mPushMessage; // 推送过来的消息
    private boolean fromPush; // 是否从推送到此页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        initView();
    }

    private void initView() {
        mUserNameView = (EditText) findViewById(R.id.associate_email_input);
        mPasswordView = (EditText) findViewById(R.id.login_input_password);
        mLoginView = (TextView) findViewById(R.id.login_button);
        mLoginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                login();
                break;
        }
    }


    /**
     * 用户信息存入数据库，以使让用户一打开应用就是一个登陆过的状态
     */
    private void insertUserInfoIntoDB() {

    }

    private void login() {
        String userName = mUserNameView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            return;
        }

        if (TextUtils.isEmpty(password)) {
            return;
        }

        RequestCenter.login(userName, password, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                DialogManager.getInstnce().dismissProgressDialog();

                /**
                 * 这部分可以封装起来，封装为到一个登陆流程类中
                 */
                User user = (User) responseObj;
                UserManager.getInstance().setUser(user);//保存当前用户单例对象
                connectToSever();
                sendLoginBroadcast();
                /**
                 * 还应该将用户信息存入数据库，这样可以保证用户打开应用后总是登陆状态
                 * 只有用户手动退出登陆时候，将用户数据从数据库中删除。
                 */
                insertUserInfoIntoDB();
//
//                if (fromPush) {
//                  Intent intent = new Intent(LoginActivity.this, PushMessageActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.putExtra("pushMessage", mPushMessage);
//                    startActivity(intent);
//                }
                finish();//销毁当前登陆页面


            }

            @Override
            public void onFailure(Object reasonObj) {
                DialogManager.getInstnce().dismissProgressDialog();
            }
        });

    }

    //启动长连接
    private void connectToSever() {
        startService(new Intent(LoginActivity.this, MinaService.class));
    }

    //自定义登陆广播Action
    public static final String LOGIN_ACTION = "com.imooc.action.LOGIN_ACTION";

    //向整个应用发送登陆广播事件
    private void sendLoginBroadcast() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(LOGIN_ACTION));
    }


}
