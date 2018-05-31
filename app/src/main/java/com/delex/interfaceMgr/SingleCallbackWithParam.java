package com.delex.interfaceMgr;

/**
 * @author  embed on 21/8/17.
 * <h1>SingleCallbackWithParam</h1>
 */
public interface SingleCallbackWithParam {
    /**
     * <h1>errorMandatoryNotifier</h1>
     * This method is used to trigger when the success response comes
     */
    public void doFirstProcess(String msg);
    /**
     * <h1>onErrorResponse</h1>
     * This method is used to trigger when the error response comes
     */
    public void onErrorResponse(String error);
    /**
     * <h1>onSessionExpire</h1>
     * This method is used to trigger when the session expires
     */
    public void onSessionExpire();
}
