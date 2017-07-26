package com.glen.mysdk.okhttp;

//import com.youdu.module.AdInstance;
//import com.youdu.okhttp.listener.DisposeDataHandle;
//import com.youdu.okhttp.listener.DisposeDataListener;
//import com.youdu.okhttp.request.CommonRequest;

import com.glen.mysdk.module.AdInstance;
import com.glen.mysdk.okhttp.listener.DisposeDataHandle;
import com.glen.mysdk.okhttp.listener.DisposeDataListener;
import com.glen.mysdk.okhttp.request.CommonRequest;

/**
 * Created by renzhiqiang on 16/10/27.
 *
 * @function sdk请求发送中心
 */
public class RequestCenter {

    /**
     * 发送广告请求
     */
    public static void sendImageAdRequest(String url, DisposeDataListener listener) {

        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, null),
                new DisposeDataHandle(listener, AdInstance.class));
    }
}
