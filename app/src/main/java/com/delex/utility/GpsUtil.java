package com.delex.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.skt.Tmap.TMapPoint;

/**
 * Created by Administrator on 2018-06-04.
 */

public class GpsUtil {

    public GpsUtil() {
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

//            mCurrentPoint = new TMapPoint(location.getLatitude(), location.getLongitude());
//
//            if (location != null) {
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                if (isFirstLocation) {  //처음에 위치 찾았을때만 centerPoint로 위치시키기
//                    mTmapview.setCenterPoint(longitude, latitude);
//                    isFirstLocation = false;
//                }
//                mTmapview.setLocationPoint(longitude, latitude);
//
//            }

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setGps() {
//        final LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, mLocationListener);
    }
}
