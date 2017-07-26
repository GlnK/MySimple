package com.glen.mysimple.view.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

/**
 * Created by Gln on 2017/6/12.
 */
public class BaseFragment extends Fragment {
    protected Activity mContext;





    public void requestPermission(int code, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
    }

    public boolean hasPermission(String... permissions) {
        for (String permisson : permissions) {
            if (ContextCompat.checkSelfPermission(getActivity(), permisson) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }
}
