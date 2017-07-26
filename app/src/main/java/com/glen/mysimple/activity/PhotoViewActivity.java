package com.glen.mysimple.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.glen.mysdk.adutil.Utils;
import com.glen.mysimple.R;
import com.glen.mysimple.activity.base.BaseActivity;
import com.glen.mysimple.adapter.PhotoPagerAdapter;
import com.glen.mysimple.util.Util;

import java.util.ArrayList;

/**
 * Created by Gln on 2017/7/8.
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener {
    /**
     * UI
     */
    private ViewPager mPager;
    private TextView mIndictorView;
    private ImageView mShareView;

    /**
     * data
     */
    private ArrayList<String> mPhotoLists;
    private int mLength;
    private PhotoPagerAdapter mAdapter;
    private int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view_layout);
        initData();
        initView();
    }

    public static final String PHOTO_LIST = "photo_list";

    /**
     * 初始化要显示的图片地址列表
     */
    private void initData() {
        Intent intent = getIntent();
        mPhotoLists = intent.getStringArrayListExtra(PHOTO_LIST);
        mLength = mPhotoLists.size();
    }

    private void initView() {
        mIndictorView = (TextView) findViewById(R.id.indictor_view);
        mShareView = (ImageView) findViewById(R.id.share_view);
        mShareView.setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.photo_pager);
        mPager.setPageMargin(Utils.dip2px(this, 30));

        mAdapter = new PhotoPagerAdapter(this, mPhotoLists, false);
        mPager.setAdapter(mAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mIndictorView.setText(String.valueOf((position + 1))
                        .concat("/")
                        .concat(String.valueOf(mLength)));
                currentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        Util.hideSoftInputMethod(this, mIndictorView);
    }

    @Override
    public void onClick(View view) {

    }
}
