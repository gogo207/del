package com.delex.utility;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by embed on 12/3/18.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    @Override
    public boolean supportsPredictiveItemAnimations()
    {
        return false;
    }
    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout)
    {
        super(context, orientation, reverseLayout);
    }
}
