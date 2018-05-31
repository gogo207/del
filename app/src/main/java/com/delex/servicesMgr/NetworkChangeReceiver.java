package com.delex.servicesMgr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.delex.utility.NetworkUtil;
import com.delex.utility.Utility;

/**
 * <h>NetworkChangeReceiver</h>
 * <p>
 *     Subclass of broadcast manager to handle the network change status
 * </p>
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Utility.printLog("PK" + " onRecieve  " + intent.getAction());
        String status = NetworkUtil.getConnectivityStatusString(context);

        String[] networkStatus = status.split(",");

        Intent homeIntent = new Intent("com.dayrunner.passenger");
        homeIntent.putExtra("STATUS", networkStatus[1]);
        context.sendBroadcast(homeIntent);
        Utility.printLog("PK Network Status" + status);

        // 0 -> No Internet.
        // 1 -> Internet Connected.
    }
}
