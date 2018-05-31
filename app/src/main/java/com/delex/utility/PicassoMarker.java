package com.delex.utility;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * <h>PicassoMarker</h>
 * <P>
 *     class to create google map marker using the picasso
 *     for the vehicle type image
 * </P>
 * @since 9/10/15.
 */
public class PicassoMarker implements Target {

    Marker mMarker;
    public PicassoMarker(Marker marker) {

        mMarker = marker;

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        try {

            mMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));

        }catch (Exception e){

            e.printStackTrace();

        }
    }

    /**
     * <h2>getmMarker</h2>
     * <p>
     *     method to get the created marker
     * </p>
     * @return
     */
    public Marker getmMarker() {
        return mMarker;
    }



    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
