package com.delex.interfaceMgr;

/**
 * <h2>ContactInterface</h2>
 * This interface will work only for providing callback when we are selecting any contact from our contact list.
 * and work in Add_Shipment class.
 */

public interface ContactInterface {
    public void firstProcess(String s);
    public void secondProcess(String s);
    public void thirdProcess(String nameContact, String cNumber);
    public void fourthProcess(String nameContact, String cNumber);
}
