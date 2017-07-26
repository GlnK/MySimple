package com.glen.mysimple.view.fragment.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.glen.mysdk.okhttp.listener.DisposeDataListener;
import com.glen.mysimple.R;
import com.glen.mysimple.activity.LoginActivity;
import com.glen.mysimple.activity.SettingActivity;
import com.glen.mysimple.manager.UserManager;
import com.glen.mysimple.module.update.UpdateModel;
import com.glen.mysimple.network.http.RequestCenter;
import com.glen.mysimple.service.update.UpdateService;
import com.glen.mysimple.util.Util;
import com.glen.mysimple.view.CommonDialog;
import com.glen.mysimple.view.fragment.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Gln on 2017/6/12.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener {
    /**
     * UI
     */
    private View mContentView;
    private RelativeLayout mLoginLayout;
    private CircleImageView mPhotoView;
    private TextView mLoginInfoView;
    private TextView mLoginView;
    private RelativeLayout mLoginedLayout;
    private TextView mUserNameView;
    private TextView mTickView;
    private TextView mVideoPlayerView;
    private TextView mShareView;
    private TextView mQrCodeView;
    private TextView mUpdateView;


    /**
     * 注册广播接收器对象,接受登录
     */
    private LoginBroadcastReceiver mReceiver = new LoginBroadcastReceiver();

    public MineFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        registerLoginBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegisterLoginBroadcast();
    }

    /**
     * 广播接收器
     */
    private void registerLoginBroadcast() {
        IntentFilter filter = new IntentFilter(LoginActivity.LOGIN_ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mReceiver, filter);
    }

    private void unRegisterLoginBroadcast() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_mine_layout, null, false);
        initView();
        return mContentView;
    }

    private void initView() {
        mLoginLayout = (RelativeLayout) mContentView.findViewById(R.id.login_layout);
        mLoginLayout.setOnClickListener(this);
        mLoginedLayout = (RelativeLayout) mContentView.findViewById(R.id.logined_layout);
        mLoginedLayout.setOnClickListener(this);
        mPhotoView = (CircleImageView) mContentView.findViewById(R.id.photo_view);
        mPhotoView.setOnClickListener(this);
        mLoginView = (TextView) mContentView.findViewById(R.id.login_view);
        mLoginView.setOnClickListener(this);
        mVideoPlayerView = (TextView) mContentView.findViewById(R.id.video_setting_view);
        mVideoPlayerView.setOnClickListener(this);
        mShareView = (TextView) mContentView.findViewById(R.id.share_imooc_view);
        mShareView.setOnClickListener(this);
        mQrCodeView = (TextView) mContentView.findViewById(R.id.my_qrcode_view);
        mQrCodeView.setOnClickListener(this);
        mLoginInfoView = (TextView) mContentView.findViewById(R.id.login_info_view);
        mUserNameView = (TextView) mContentView.findViewById(R.id.username_view);
        mTickView = (TextView) mContentView.findViewById(R.id.tick_view);
        mUpdateView = (TextView) mContentView.findViewById(R.id.update_view);
        mUpdateView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.video_setting_view:
                mContext.startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.update_view:
                Log.i("TAG", "FUCK  111111111111 ");
                checkVersion();
                break;
        }
    }

    private void checkVersion() {
        RequestCenter.checkVersion(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                final UpdateModel updateModel = (UpdateModel) responseObj;
                if (Util.getVersionCode(mContext) < updateModel.data.currentVersion) {
                    //??????,????
                    CommonDialog dialog = new CommonDialog(mContext,
                            getString(R.string.update_new_version),
                            getString(R.string.update_title),
                            getString(R.string.update_install),
                            getString(R.string.cancel),
                            new CommonDialog.DialogClickListener() {
                                @Override
                                public void onDialogClick() {
                                    Intent intent = new Intent(mContext, UpdateService.class);
                                    mContext.startService(intent);
                                }
                            });
                    dialog.show();
                } else {
                    Toast.makeText(mContext, "当前版本已是最新", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                Toast.makeText(mContext, "请求更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //自定义广播接收器
    private class LoginBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //更新UI
            mLoginLayout.setVisibility(View.GONE);
            mLoginedLayout.setVisibility(View.VISIBLE);
            mUserNameView.setText(UserManager.getInstance().getUser().data.name);
            mTickView.setText(UserManager.getInstance().getUser().data.tick);
        }
    }
}
