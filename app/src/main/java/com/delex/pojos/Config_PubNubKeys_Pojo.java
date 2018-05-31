package com.delex.pojos;

/**
 * Created by embed on 30/6/17.
 */

public class Config_PubNubKeys_Pojo {
    private String publishKey;

    private String subscribeKey;

    public String getPublishKey ()
    {
        return publishKey;
    }

    public void setPublishKey (String publishKey)
    {
        this.publishKey = publishKey;
    }

    public String getSubscribeKey ()
    {
        return subscribeKey;
    }

    public void setSubscribeKey (String subscribeKey)
    {
        this.subscribeKey = subscribeKey;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [publishKey = "+publishKey+", subscribeKey = "+subscribeKey+"]";
    }
}
