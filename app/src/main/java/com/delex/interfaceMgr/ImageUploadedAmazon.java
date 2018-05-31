package com.delex.interfaceMgr;

/**
 * <h>ImageUploadedAmazon</h>
 * this is ti check if image is uploaded on the amazon or not
 * Created by ${Ali} on 8/18/2017.
 */

public interface ImageUploadedAmazon
{
    void onSuccess(String image);
    void onError();
}
