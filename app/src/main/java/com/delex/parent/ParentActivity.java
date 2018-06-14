package com.delex.parent;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2018-05-31.
 */

public class ParentActivity extends AppCompatActivity{

    public static final String TAG = ParentActivity.class.getSimpleName();

    public void getActivityName(Class childActivity){
        Log.d(TAG, "getActivityName: "+(childActivity.getSimpleName()));
    }


    @Override
    protected void onResume() {
        getActivityName(getClass());
        super.onResume();
    }
}
