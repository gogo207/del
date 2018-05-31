package com.delex.servicesMgr;

import android.util.Log;

import com.delex.utility.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * <h>MyFirebaseInstanceIDService</h>
 * This class is used as a Service which is used for handling FireBase Token Id .
 * @author 3embed
 * @since 6 Apr 2017.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService
{
    private static final String TAG="MyFireBaseInstanceID";
    private SessionManager sessionManager;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]

    @Override
    public void onTokenRefresh() {
        //Get updated InstanceId token.
        sessionManager = new SessionManager(this);
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshToken);
        Log.i(TAG,"refetoken "+refreshToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token)
    {
        // TODO: Implement this method to send token to your app server.
        sessionManager.setRegistrationId(token);
        Log.i("Regist","FCMTOKEN: "+sessionManager.getRegistrationId());
    }
}
