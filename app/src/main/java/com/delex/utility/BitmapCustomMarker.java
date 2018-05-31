package com.delex.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.delex.customer.R;

/**
 * <h1>BitmapCustomMarker</h1>
 * This class is used to plot the marker with ETA
 * @author Akbar
 */
public class BitmapCustomMarker
{
    private String sname;
    Context context;
    public Marker driverOnTheWayMarker;

    public BitmapCustomMarker(Context context, String sname)
    {
        this.context=context;
        this.sname=sname;
        Utility.printLog("time in marker "+sname);
    }
    public Bitmap createBitmap()
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(R.layout.layout_eta_marker, null);
        RelativeLayout lyBitmap= layout.findViewById(R.id.map_image);
        TextView tveta= layout.findViewById(R.id.pick_eta_on_the_way);
        tveta.setText(sname);
        layout.setDrawingCacheEnabled(true);
        layout.buildDrawingCache();
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        layout.layout(0, 0, lyBitmap.getMeasuredWidth(), lyBitmap.getMeasuredHeight());
        return layout.getDrawingCache();
    }
}
