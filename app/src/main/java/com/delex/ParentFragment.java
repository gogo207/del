package com.delex;

import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by Administrator on 2018-05-31.
 */

public class ParentFragment extends Fragment {

    public static final String TAG = ParentActivity.class.getSimpleName();

    public void getFragmentName(Class childActivity){
        Log.d(TAG, "getActivityName: "+(childActivity.getSimpleName()));
    }


    @Override
    public void onStart() {
        getFragmentName(getClass());
        super.onStart();
    }
}
