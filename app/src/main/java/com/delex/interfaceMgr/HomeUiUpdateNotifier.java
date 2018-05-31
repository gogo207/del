package com.delex.interfaceMgr;

/**
 * @since 18/08/17.
 */

public interface HomeUiUpdateNotifier
{
    void favAddressUpdater(final boolean isToSetAsFavAdrs, final String address, final String favAdrsName);
    void updateCameraPosition(final Double currentLat, final Double currentLng);
    void updateEachVehicleTypeETA();
    void OnGettingOfCurrentLoc(double latitude, double longutude);
    void NotifyIfAddressChanged();
}
