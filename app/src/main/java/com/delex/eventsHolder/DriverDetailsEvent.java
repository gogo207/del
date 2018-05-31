package com.delex.eventsHolder;


import com.delex.pojos.DriverPubnubPojo;

/**
 * Created by embed on 24/7/17.
 */

public class DriverDetailsEvent {
    private DriverPubnubPojo driver_pubnub_pojo;

    public DriverDetailsEvent(DriverPubnubPojo pubnub_pojo)
    {
        driver_pubnub_pojo = pubnub_pojo;
    }

    public DriverPubnubPojo getDriver_pubnub_pojo() {
        return driver_pubnub_pojo;
    }
}
