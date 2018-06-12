package com.delex.interfaceMgr;

/**
 * HomeFragment UI 업데이트 알리미
 * HomeFragment와 HomeModel 에서 쓴다
 */

public interface HomeUiUpdateNotifier
{
    void favAddressUpdater(final boolean isToSetAsFavAdrs, final String address, final String favAdrsName);
    void updateCameraPosition(final Double currentLat, final Double currentLng);
    void updateEachVehicleTypeETA();
    void OnGettingOfCurrentLoc(double latitude, double longutude);
    void NotifyIfAddressChanged();
}
