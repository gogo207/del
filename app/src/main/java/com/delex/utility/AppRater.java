package com.delex.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.delex.customer.R;

/**
 * Created by embed on 3/4/18.
 */

public class AppRater {
    private final static String APP_TITLE = "Truckr";// App Name
    private final static String APP_PNAME = "com.truckr.customer";// Package Name

    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days
    private static int LAUNCHES_UNTIL_PROMPT = 5;//Min number of launches

    private static SessionManager sessionManager;
    public static void app_launched(Context mContext) {
            sessionManager= new SessionManager(mContext);
                    if (sessionManager.isRating()) {
            return;


        }else{





        // Increment launch counter
        long launch_count = sessionManager.getRateCount()+ 1;
        sessionManager.setRateCount(launch_count);

        // Get date of first launch
        Long date_firstLaunch = sessionManager.getRateDay();
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
           sessionManager.setRateDay(date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog(mContext);
            }
        }


        }
    }

    public static void showRateDialog(final Context mContext) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        Typeface ClanaproNarrMedium = Typeface.createFromAsset(mContext.getAssets(), "fonts/ClanPro-NarrMedium.otf");
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_rate_dialog, null);
        alertDialogBuilder.setView(view);

        final AlertDialog mDialog = alertDialogBuilder.create();
        mDialog.setCancelable(false);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView rate_header = (TextView) view.findViewById(R.id.rate_header);
        Button btnCamera = (Button) view.findViewById(R.id.camera);
        Button btnCancel = (Button) view.findViewById(R.id.cancel);
        Button btnGallery = (Button) view.findViewById(R.id.gallery);
        TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);


        rate_header.setText("Rate " + APP_TITLE);
        tvHeader.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        btnCamera.setText("Rate " + APP_TITLE);
        btnGallery.setText("Remind me later");
        btnCancel.setText("No, thanks");


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
               sessionManager.setIsRating(true);
                mDialog.dismiss();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sessionManager.setIsRating(true);
                mDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setIsRating(true);
                mDialog.dismiss();
            }
        });

        mDialog.show();

    }
}
