package com.delex.chat_module;

/**
 * Created by PrashantSingh on 21/07/17.
 */

public class HasGotVehicleTypesEvent
{
    private boolean hasGotVehicleTypes = false;

    public HasGotVehicleTypesEvent(boolean hasVehicleTypesFetched)
    {
        this.hasGotVehicleTypes = hasVehicleTypesFetched;
    }

    public boolean getHasGotVehicleTypes() {
        return hasGotVehicleTypes;
    }
}