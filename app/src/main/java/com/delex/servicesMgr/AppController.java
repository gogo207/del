package com.delex.servicesMgr;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.delex.a_kakao_login.KakaoSDKAdapter;
import com.delex.chat_module.IsForeground;
import com.delex.customer.R;
import com.delex.logger.Dlog;
import com.delex.utility.ConnectionService;
import com.delex.utility.Utility;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import com.delex.bookingHistory.ReceiptActivity;
import com.delex.chat_module.AppStateChange.AppStateListener;
import com.delex.chat_module.AppStateChange.AppStateMonitor;
import com.delex.chat_module.AppStateChange.RxAppStateMonitor;
import com.delex.chat_module.Callbacks.DatabaseCallbackHandler;
import com.delex.interfaceMgr.OnGettingOfAppConfig;
import com.delex.pojos.StartReceiptActPojo;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;
import com.kakao.auth.KakaoSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

/**
 * base Application
 */

public class AppController extends MultiDexApplication implements Application.ActivityLifecycleCallbacks

{
    private static OnGettingOfAppConfig appConfig;
    private boolean foreground = true;
    private static AppController mInstance;
    private String pushToken = null;
    private SharedPreferences sharedPref;
    private boolean signedIn;
    private String userId, activeReceiverId = "";
    private String activeChatName = "";
    public void setActiveChatName(String activeChatName) {
        this.activeChatName = activeChatName;
    }
    private DatabaseCallbackHandler databaseHandler;
    private boolean chatScreenDestroyed = false;
    private SessionManager sessionManager;
    private Intent intent;
///////////////////카카오톡 로그인 연동///////
    private static volatile Activity currentActivity = null;
////////////////////////////////////////////////
    public static boolean DEBUG = false;  // Dlog 셋팅

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ko"));
        MultiDex.install(this);
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
//        MultiDex.install(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Locale locale = new Locale("ko");
//
//        Locale.setDefault(locale);
//
//        Configuration config = new Configuration();
//
//        config.locale = locale;
//
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        this.DEBUG = isDebuggable(this);

        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());  //카카오톡 로그인 어댑터

        EventBus.getDefault().register(this);

        IsForeground.init(this);
        IsForeground.get(this).addListener(new IsForeground.Listener() {
            @Override
            public void onBecameForeground()
            {
                if(sessionManager.isLogin())
                    Utility.callConfig(getApplicationContext(), true);
            };

            @Override
            public void onBecameBackground()
            {
                Log.d("App_is_in_fb","B");
                PubNubMgr.getInstance().unSubscribePubNub();
            }
        });



        AppStateMonitor appStateMonitor = RxAppStateMonitor.create(this);
        appStateMonitor.addListener(new AppStateListener() {
            @Override
            public void onAppDidEnterForeground() {


                foreground = true;


                if (signedIn && userId != null) {
                    updatePresenceStatus(1, userId);
                    NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancelAll();
                }

            }

            @Override
            public void onAppDidEnterBackground() {


                foreground = false;

                if (signedIn && userId != null) {
                    updatePresenceStatus(0, userId);

                }
            }
        });
        appStateMonitor.start();


        sharedPref = this.getSharedPreferences("defaultPreferences", Context.MODE_PRIVATE);

        pushToken = sharedPref.getString("pushToken", null);
        if (pushToken == null) {


            if (checkPlayServices()) {
                startService(new Intent(this, MyFirebaseInstanceIDService.class));
            }

        }


        userId = sharedPref.getString("userId", null);
        signedIn = sharedPref.getBoolean("signedIn", false);

        registerActivityLifecycleCallbacks(mInstance);
        startService(new Intent(mInstance, AppKilled.class));
        sessionManager = new SessionManager(getApplicationContext());

        if (intent == null) {
            intent = new Intent(mInstance, ConnectionService.class);
            intent.putExtra(ConnectionService.TAG_INTERVAL, 1);
            intent.putExtra(ConnectionService.TAG_URL_PING, "http://www.google.com");
            intent.putExtra(ConnectionService.TAG_ACTIVITY_NAME, this.getClass().getName());
        }
        startService(intent);
    }

    ////////////////////////카카오 로그인 연동////////////////
    public static AppController getGlobalApplicationContext() {
        return mInstance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }
    //////////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StartReceiptActPojo startReceiptActPojo)
    {
        Intent receiptIntent = new Intent(getApplicationContext(), ReceiptActivity.class);
        Log.d("MyApplication", "bid:" +startReceiptActPojo.getBookingId());
        receiptIntent.putExtra("bid", startReceiptActPojo.getBookingId());
        receiptIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(receiptIntent);
    }

    /**
     * 현재 디버그모드여부를 리턴
     *
     * @param context
     * @return
     */
    private boolean isDebuggable(Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
			// 디버그 변수는 false로 남는다
        }

        return debuggable;
    }


    @Override
    public void onTerminate() {
        PubNubMgr.getInstance().unSubscribePubNub();
        super.onTerminate();

    }




    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public boolean isForeground() {
        return foreground;
    }


    public SharedPreferences getSharedPreferences() {

        return sharedPref;
    }


    public String getPushToken() {

        return pushToken;
    }

    public void setPushToken(String pushToken) {

        this.pushToken = pushToken;
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }



    public String getUserId(){


        return userId;
    }


    public void setupPresenceSystem(String userId) {


        String str = "users/" + userId + "/";
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myConnectionsRef = database.getReference(str + "connections");
        final DatabaseReference lastOnlineRef = database.getReference(str + "lastOnline");
        final DatabaseReference connectedRef = database.getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    DatabaseReference con = myConnectionsRef.push();
                    con.setValue(Boolean.TRUE);
                    con.onDisconnect().removeValue();
                    lastOnlineRef.onDisconnect().setValue(ServerValue.TIMESTAMP);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled at .info/connected");
            }
        });


        updateUserDetails(userId);
        updatePresenceStatus(1, userId);
    }


    public void updatePresenceStatus(int foreground, String userId) {


        String str = "users/" + userId + "/";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference lastOnlineRef = database.getReference(str + "lastOnline");
        final DatabaseReference online = database.getReference(str + "online");
        if (foreground == 0) {

            lastOnlineRef.setValue(ServerValue.TIMESTAMP);
            online.setValue(Boolean.FALSE);
        } else {

            online.setValue(Boolean.TRUE);
        }
    }




    private void updateUserDetails(String userId) {

        this.userId = userId;
        this.signedIn = true;


        sharedPref.edit().putString("userId", userId).apply();
        sharedPref.edit().putBoolean("signedIn", true).apply();


    }

    public String getActiveReceiverId() {


        return activeReceiverId;
    }


    public void setActiveReceiverId(String receiverId) {


        this.activeReceiverId = receiverId;
    }

    public static void initDatabaseHandlerInstance(DatabaseCallbackHandler sktResponseHandler) {
        AppController.getInstance().databaseHandler = sktResponseHandler;


    }


    public void registerListenerForNewChats() {


        ChildEventListener chatsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot childDataSnapshot, String previousChildName) {
                /*
                 * Needed when a new chat has been initiated with any of the user
                 */

                if (AppController.getInstance().databaseHandler != null && AppController.getInstance().getActiveReceiverId().isEmpty()) {
                    try {
                        AppController.getInstance().databaseHandler.handleResponse(childDataSnapshot);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                /*
                 * To avoid the callback incase the user is on the chatmessagescreen,casue child changed will be called even for the first time as user will update the count of unread messages
                 */


                if (chatScreenDestroyed) {

                    chatScreenDestroyed = false;
                } else {
                    if (AppController.getInstance().databaseHandler != null && AppController.getInstance().getActiveReceiverId().isEmpty()) {
                        try {
                            AppController.getInstance().databaseHandler.handleResponse(dataSnapshot);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users_Chats").child(userId);


        ref.addChildEventListener(chatsListener);


        ref.onDisconnect();


    }

    public void setStatusAsOfflineForCurrentChat(String userId) {




        if (!AppController.getInstance().activeChatName.isEmpty()) {

            FirebaseDatabase.getInstance()
                    .getReference().child("Users_Chats").child(userId).child(activeChatName)
                    .child("online")
                    .setValue(0);
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {


    }

    @Override
    public void onActivityDestroyed(Activity activity) {


        if (activity.getClass().getSimpleName().equals("ChatMessagesScreen")) {
            chatScreenDestroyed = true;

        }

    }

    @Override
    public void onActivityPaused(Activity activity) {


    }

    @Override
    public void onActivityResumed(Activity activity) {


    }

    @Override
    public void onActivitySaveInstanceState(Activity activity,
                                            Bundle outState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleHelper.setLocale(this);
    }


}
