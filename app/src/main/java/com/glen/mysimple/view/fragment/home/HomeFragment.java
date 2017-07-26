package com.glen.mysimple.view.fragment.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.glen.mysdk.activity.AdBrowserActivity;
import com.glen.mysdk.okhttp.listener.DisposeDataListener;
import com.glen.mysimple.R;
import com.glen.mysimple.activity.PhotoViewActivity;
import com.glen.mysimple.adapter.CourseAdapter;
import com.glen.mysimple.adapter.PhotoPagerAdapter;
import com.glen.mysimple.constant.Constant;
import com.glen.mysimple.module.recommand.BaseRecommandModel;
import com.glen.mysimple.module.recommand.RecommandBodyValue;
import com.glen.mysimple.network.http.RequestCenter;
import com.glen.mysimple.util.Util;
import com.glen.mysimple.view.fragment.BaseFragment;
import com.glen.mysimple.view.home.HomeHeaderLayout;
import com.glen.mysimple.zxing.app.CaptureActivity;

/**
 * Created by Gln on 2017/6/12.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final int REQUEST_QRCODE = 0x01;
    public final String TAG = "sss";

    private View mContentView;
    private ListView mListView;
    private TextView mQRCodeView;
    private TextView mCategoryView;
    private TextView mSearchView;
    private ImageView mLoadingView;

    private CourseAdapter mAdapter;
    private BaseRecommandModel mRecommandData;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecommandData();
    }

    /**
     * 发送首页列表数据请求
     */
    private void requestRecommandData() {
        //Log.e(TAG, "开始前 " );
        RequestCenter.requestRecommandData(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                //完成真正的功能逻辑
                Log.e(TAG, "onSuccess: " + responseObj.toString());
                /**
                 * 获取到数据后更新UI
                 */
                mRecommandData = (BaseRecommandModel) responseObj;
                showSuccessView();
            }


            @Override
            public void onFailure(Object reasonObj) {
                //提示用户网络有问题
                Log.e(TAG, "onFailure: " + reasonObj.toString());
            }
        });
        // Log.e(TAG, "结束了 " );
    }

    /**
     * 请求成功执行的方法
     */
    private void showSuccessView() {
        //判断数据是否为空
        if (mRecommandData.data.list != null && mRecommandData.data.list.size() > 0) {
            Log.e("FUUUUUUUUUUUUk", "showSuccessView: " );
            mLoadingView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            //为ListView添加列表头
            mListView.addHeaderView(new HomeHeaderLayout(mContext, mRecommandData.data.head));
            //创建我们的adapter
            mAdapter = new CourseAdapter(mContext, mRecommandData.data.list);
            mListView.setAdapter(mAdapter);

            /**
             * 为ListView添加滑动事件监听
             */
            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                    mAdapter.updateAdInScrollView();
                }
            });
        } else {
            showErrorView();
        }
    }

    /**
     * 请求失败执行的方法
     */
    private void showErrorView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mContentView = inflater.inflate(R.layout.fragment_home_layout, container, false);
        initView();
        return mContentView;
    }

    private Button button;

    private void initView() {
        mQRCodeView = (TextView) mContentView.findViewById(R.id.qrcode_view);
        mQRCodeView.setOnClickListener(this);
        mCategoryView = (TextView) mContentView.findViewById(R.id.category_view);
        mCategoryView.setOnClickListener(this);
        mSearchView = (TextView) mContentView.findViewById(R.id.search_view);
        mSearchView.setOnClickListener(this);
        mListView = (ListView) mContentView.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        mLoadingView = (ImageView) mContentView.findViewById(R.id.loading_view);
        //启动Loading动画
        AnimationDrawable anim = (AnimationDrawable) mLoadingView.getDrawable();
        anim.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_view:
                if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doOpenCamera();
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
                break;
//            case R.id.category_view:
//                //与我交谈
//                Intent intent2 = new Intent(Intent.ACTION_VIEW, Util.createQQUrl("277451977"));
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent2);
//                break;
//            case R.id.search_view:
//                Intent searchIntent = new Intent(mContext, SearchActivity.class);
//                mContext.startActivity(searchIntent);
//                break;
        }
    }

    public void doOpenCamera() {
        Intent intent = new Intent(mContext, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_QRCODE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RecommandBodyValue value = (RecommandBodyValue) mAdapter.getItem(position - mListView.getHeaderViewsCount());
        if (value.type != 0) {
            Intent intent = new Intent(mContext, PhotoViewActivity.class);
            intent.putStringArrayListExtra(PhotoViewActivity.PHOTO_LIST, value.url);
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_QRCODE:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra("SCAN_RESULT");
                    if (code.contains("http") || code.contains("https")) {
                        Intent intent = new Intent(mContext, AdBrowserActivity.class);
                        intent.putExtra(AdBrowserActivity.KEY_URL, code);
                    } else {
                        Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}

