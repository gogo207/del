package com.delex.interfaceMgr;

/**
 * <h>ProfileDataChangeResponseNotifier</h>
 * this interface handles the profile responce to reflect on the User Interface
 * Created by ${Ali} on 8/19/2017.
 */

public interface ProfileDataChangeResponseNotifier
{
    /**
     * <h2>successUiUpdater</h2>
     * <p>
     *     ProfileDataChangeResponseNotifier interface method to update
     *     the views on the success response
     * </p>
     */
    void successUiUpdater();

    /**
     * <h2>sessionExpired</h2>
     * <p>
     *     ProfileDataChangeResponseNotifier interface method to notify
     *     ththat user session has been expired
     * </p>
     */
    void sessionExpired();

    /**
     * <h2>OnPasswordChangeError</h2>
     * <p>
     *     ProfileDataChangeResponseNotifier interface method to notify
     *     password change error
     * </p>
     * @param error: error message retrieved on changing password
     */
    void OnPasswordChangeError(String error);

    /**
     * <h2>onPasswordSuccess</h2>
     * <p>
     *     ProfileDataChangeResponseNotifier Interface method() to notify the success password change
     * </p>
     */
    void onPasswordSuccess();
}
