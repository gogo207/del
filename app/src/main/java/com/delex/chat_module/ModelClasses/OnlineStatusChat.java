package com.delex.chat_module.ModelClasses;

/**
 * Created by moda on 23/06/17.
 */

public class OnlineStatusChat {


    public int online;


    public void setOnline(int online) {
        this.online = online;
    }


    public void setUnreadMessageCount(long unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public long unreadMessageCount;


}
