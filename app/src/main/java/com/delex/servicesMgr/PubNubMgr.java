package com.delex.servicesMgr;

import android.content.Context;
import android.util.Log;


import com.delex.chat_module.WalletDataChangedEvent;
import com.delex.chat_module.WalletStatusChangedEvent;

import com.delex.pojos.DriverPubnubPojo;
import com.delex.pojos.PubnubMasArrayPojo;
import com.delex.pojos.PubnubResponsePojoHome;
import com.delex.pojos.StartReceiptActPojo;
import com.delex.pojos.Types;
import com.delex.pojos.UnReadCount;
import com.delex.utility.Constants;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.eventsHolder.DriverDetailsEvent;

import com.delex.eventsHolder.DriversList;
import com.google.gson.Gson;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <h1>PubNubMgr</h1>
 * <p>
 * 이 클래스는 모든 pubnub 관련 데이터를 처리하는 데 사용됩니다.
 * this class is use to handle all pubnub related data
 * </p>
 *
 * @since on 07/07/17.
 */

public class PubNubMgr {
    private final String TAG = "PubNubMgr";
    private static final Object lockKey = new Object();

    //volatile : useful for concurrency control. is it needed here? (think)
    private static volatile PubNubMgr pubNubMgr;
    private static volatile PubNub pubNub;
    private PNConfiguration pnConfiguration = null;
    private static boolean hasPubNubSubscribed;

    //to control event bus
    private Gson gson;
    private static volatile PubnubResponsePojoHome pubnubResponse_old;
    private static volatile boolean isReceiptActVisible = false;
    private static String pubNubListenerChannles;
    private static String customerApiInterval;          // to ignore the config api response if it comes b4 this interval

    /**
     * <h2>getInstance</h2>
     * <p>
     * 이 메소드는,이 클래스의 단독 & thread에 대해서 안전한 인스턴스를 돌려줍니다.
     * this method will return singleton & thread safe instance of this class
     * </p>
     *
     * @return PubNubMgr single instance
     */
    public static PubNubMgr getInstance() {
        if (pubNubMgr == null) {
            synchronized (lockKey) {
                if (pubNubMgr == null) {
                    Log.d("PubNubMgr", "getInstance pubNubMgr == null ");
                    pubNubMgr = new PubNubMgr();
                }
            }
        }
        return pubNubMgr;
    }
    //=============================================================

    /**
     * <h2>PubNubMgr</h2>
     * <p>
     * private constructor
     * </p>
     */
    private PubNubMgr() {
        Log.d(TAG, "constructor()");
        gson = new Gson();
    }
    //=============================================================

    /**
     * <h2>getPubNubInstance</h2>
     * <p>
     * this method is use to get pubnub singleton instance
     * </p>
     *
     * @return PubNub: pubnub singleton instance
     */
    public PubNub getPubNubInstance(Context context) {
        Log.d(TAG, "getPubNubInstance()");
        if (pubNub == null) {
            synchronized (lockKey) {
                if (pnConfiguration == null) {
                    configPubNub(context);
                }
                pubNub = new PubNub(pnConfiguration);
                subscribe();
            }
        }
        return pubNub;
    }
    //=============================================================

    /**
     * <h2>configPubNub</h2>
     * <p>
     * pubNub의 상세를 설정하는 메소드
     * </p>
     *
     * @param context: calling activity reference to get SessionManager instance
     */
    private void configPubNub(Context context) {

        Log.d(TAG, "configPubNub()");
        SessionManager sessionMgr = new SessionManager(context);
        pubNubListenerChannles = sessionMgr.getChannel();

        if (!sessionMgr.getCustomerApiInterval().isEmpty()) {
            customerApiInterval = sessionMgr.getCustomerApiInterval();
        } else {
            customerApiInterval = "10";
        }

        pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey(sessionMgr.getPubnub_Publish_Key());
        pnConfiguration.setSubscribeKey(sessionMgr.getPubnub_Subscribe_Key());
    }
    //=============================================================

    /**
     * <h1>restartPubNub</h1>
     * <p>
     * pubnub를 다시 시작하는 방법 : pubnub가 이미 초기화 된 경우 null로 설정하고 다시 구성하십시오.
     * </p>
     *
     * @param context
     */
    public void restartPubNub(Context context) {
        Log.d(TAG, "restartPubNub()");
        if (pubNub != null) {
            synchronized (lockKey) {
                unSubscribePubNub();
            }
        }
        pubNub = getPubNubInstance(context);
    }
    //=============================================================

    /**
     * <h1>subscribe</h1>
     * <p>
     * pubnub가 아직 구독하지 않은 경우 구독하는 방법
     *       * 리스너를 추가합니다.
     * </p>
     */
    private void subscribe() {
        Log.d(TAG, "subscribe() hasPubNubSubscribed: " + hasPubNubSubscribed);
        if (!hasPubNubSubscribed) {
            hasPubNubSubscribed = true;
            try {
                pubNub.subscribe()
                        .withPresence()
                        .channels(Arrays.asList(pubNubListenerChannles))
                        .execute(); // subscribe to channels.execute();
                addPubNubListener();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //=============================================================

    /**
     * <h1>addPubNubListener</h1>
     * <p>
     * 이 메소드는 pubnub에 리스너를 추가하는 것이다.
     * this method is to add listener to pubnub
     * </p>
     */
    private void addPubNubListener() {
        //Log.d(TAG, "addPubNubListener(): ");
        pubNub.addListener(subscribeCallback);
    }
    //=============================================================


    /**
     * <h1>subscribeCallback</h1>
     * <p>
     * pubnub 리스너, 즉 subscribeCallback을 초기화하는 메소드
     * </p>
     */
    private SubscribeCallback subscribeCallback = new SubscribeCallback() {
        @Override
        public void status(PubNub pubnub, PNStatus status) {
            if (status.getOperation() != null) {
                pubNubStatus(status);
            } else {
                // After a reconnection see status.getCategory()
                Log.i(TAG, "subscribeCallback status.getOperation() == null: " + status.getOperation());
            }
        }

        @Override
        public void message(PubNub pubnub, PNMessageResult message) {
            String msg = message.getMessage().toString();
            Log.i(TAG, "subscribeCallback presence = response: " + msg);
            String[] params = new String[]{message.getMessage().toString(), customerApiInterval};

            Utility.printLog(TAG + "PubNubReceivedMsgHandler params[0]: " + msg);

            //JSONObject jsnResponse;
            if (msg != null && !msg.isEmpty()) {
                try {
                    JSONObject jsnResponse = new JSONObject(msg);
                    String actionValue = jsnResponse.getString("a");

                    if (actionValue != null && !actionValue.isEmpty()) {
                        int action = Integer.parseInt(actionValue);
                        // Log.i(TAG, "PubNubReceivedMsgHandler action: " + action + "  jsnResponse: " + jsnResponse.toString());

                        switch (action) {
                            // getDrivers response
                            case 2:
                                //Log.i(TAG, "PubNubReceivedMsgHandler massArr: "+jsnResponse.getString("masArr"));
                                handleNewVehicleTypesData(msg, Integer.parseInt(params[1]));
                                long bookingId = jsnResponse.getLong("bookingId");
                                Log.d(TAG, "PubNubReceivedMsgHandler bookingId: " + bookingId);
                                if (bookingId > 0 && !isReceiptActVisible) {
                                    isReceiptActVisible = true;
                                    StartReceiptActPojo startReceiptActPojo = new StartReceiptActPojo();
                                    startReceiptActPojo.setBookingId(String.valueOf(bookingId));
                                    EventBus.getDefault().post(startReceiptActPojo);
                                }
                                break;

                            case 3:
                                Log.i(TAG, "message action ==  3 ");

                                Log.d("entering: ", "going inside");

                                driverStatusDetails(msg);
                                break;

                            case 4:
                                Log.i(TAG, "message action ==  3 ");
                                driverTrackingDetails(msg);
                                break;

                            case 155:
                                Log.i(TAG, "message action ==  155 ");
                                bookingExpired(msg);
                                break;

                            //====== handle wallet status ======
                            case 45:        //reachedSoftLimit
                            case 46:        //reachedHardLimit
                            case 47:        //cameOutOfSoftLimit
                            case 48:        //cameOutOfHardLimit
                            case 49:        //newTransaction
                            case 50:        //bothSoftAndHarLimitChanged
                            case 51:        //softLimitBoundryChanged
                            case 52:        //hardLimitBoundryChanged
                                Log.i(TAG, "message action: " + action + "  isWalletFragActive: " +
                                        Constants.isWalletFragActive + "  isWalletUpdateCalled: " + Constants.isWalletUpdateCalled);
                                if (Constants.isWalletFragActive && !Constants.isWalletUpdateCalled) {
                                    final WalletDataChangedEvent walletDataChangedEvent = new Gson().fromJson(msg, WalletDataChangedEvent.class);
                                    EventBus.getDefault().post(walletDataChangedEvent);
                                }
                                break;

                            case 53:        //walletEnabled
                            {
                                WalletStatusChangedEvent walletStatusChangedEvent = new WalletStatusChangedEvent();
                                walletStatusChangedEvent.setWalletEnabled(true);
                                if (jsnResponse.has("walletAmount")) {
                                    walletStatusChangedEvent.setWalletAmount(jsnResponse.getString("walletAmount"));
                                }
                                EventBus.getDefault().post(walletStatusChangedEvent);
                            }
                            break;

                            case 54:        //walletDisabled
                            {
                                WalletStatusChangedEvent walletStatusChangedEvent = new WalletStatusChangedEvent();
                                walletStatusChangedEvent.setWalletEnabled(false);
                                if (jsnResponse.has("walletAmount")) {
                                    walletStatusChangedEvent.setWalletAmount(jsnResponse.getString("walletAmount"));
                                }
                                EventBus.getDefault().post(walletStatusChangedEvent);
                            }
                            break;
                            //====== handle wallet status ends here ======

                            default:
                                break;
                        }
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "message JSONException: " + e);
                    e.printStackTrace();
                }
            }
            // new PubNubReceivedMsgHandler().execute(params);
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            Log.i(TAG, "subscribeCallback presence = null: " + presence);
        }
    };
    //=============================================================


    /**
     * <h1>pubNubStatus</h1>
     *
     * @param status
     */
    private void pubNubStatus(PNStatus status) {
        switch (status.getOperation()) {
            // let's combine unsubscribe and subscribe handling for ease of use
            case PNSubscribeOperation:
            case PNUnsubscribeOperation:
                // 오류를 나타낼 수 있는 카테고리가 있다
                // note: subscribe statuses never have traditional
                // errors, they just have categories to represent the
                // different issues or successes that occur as part of subscribe
                switch (status.getCategory()) {
                    case PNConnectedCategory:
                        // 이것은 구독에 대한 예상이며, 이는 오류나 문제가 전혀 없음을 의미합니다.
                        // this is expected for a subscribe, this means there is no error or issue whatsoever
                        Log.i(TAG, "pubNubStatus() PNConnectedCategory: ");
                        break;

                    case PNReconnectedCategory:
                        // this usually occurs if subscribe temporarily fails but reconnects. This means
                        // there was an error but there is no longer any issue
                        Log.i(TAG, "pubNubStatus() PNReconnectedCategory: ");
                        break;

                    case PNDisconnectedCategory:
                        // 구독 취소가 예상되는 카테고리입니다. 이것은 거기 의미합니다.
                        // 모든 항목의 구독을 취소하는 데 오류가 없었습니다.
                        // this is the expected category for an unsubscribe. This means there
                        // was no error in unsubscribing from everything
                        Log.i(TAG, "pubNubStatus() PNDisconnectedCategory: ");
                        break;


                    case PNUnexpectedDisconnectCategory:

                        EventBus.getDefault().post(new UnReadCount());
                        // 이것은 일반적으로 인터넷 연결에 문제가 있습니다. 이것은 오류이며 적절하게 처리합니다.
                        // this is usually an issue with the internet connection, this is an error, handle appropriately
                        Log.i(TAG, "pubNubStatus() PNUnexpectedDisconnectCategory: ");
                        pubNub.reconnect();
                        break;


                    case PNAccessDeniedCategory:

                        // PAM이이 클라이언트가이 구독을 허용 함을 의미합니다.
                        // 채널 및 채널 그룹 구성. 이것은 또 다른 명백한 오류입니다.
                        // this means that PAM does allow this client to subscribe to this
                        // channel and channel group configuration. This is another explicit error
                        Log.i(TAG, "pubNubStatus() PNAccessDeniedCategory: ");
                        break;


                    default:
                        //다른 오류를 명시 적으로 작성하여 더 많은 오류를 직접 지정할 수 있습니다.
                        // `PNStatusCategory`의 오류 범주,`PNTimeoutCategory` 또는`PNMalformedFilterExpressionCategory` 또는`PNDecryptionErrorCategory`
                        // More errors can be directly specified by creating explicit cases for other
                        // error categories of `PNStatusCategory` such as `PNTimeoutCategory` or `PNMalformedFilterExpressionCategory` or `PNDecryptionErrorCategory`
                        Log.i(TAG, "pubNubStatus() default: " + status.getCategory());
                        break;
                }

            case PNHeartbeatOperation:
                // heartbeat operations can in fact have errors, so it is important to check first for an error.
                // For more information on how to configure heartbeat notifications through the status
                // PNObjectEventListener callback, consult <link to the PNCONFIGURATION heartbeart config>
                if (status.isError()) {
                    // There was an error with the heartbeat operation, handle here
                } else {
                    // heartbeat operation was successful
                }
            default: {
                // Encountered unknown status type
            }
        }
    }
    //=============================================================


    //=============================================================


    /**
     * <h2>updateNewVehicleTypesData</h2>
     * <p>
     * 새로운 차량 유형 데이터 업데이트
     * 핸들을 분석하고 홈 화면에서 차량 유형을 업데이트하는 메소드
     * </p>
     *
     * @param temp
     */
    public void updateNewVehicleTypesData(PubnubResponsePojoHome temp) {
        //Log.d(TAG, "updateNewVehicleTypesData temp: "+gson.toJson(temp));
        pubnubResponse_old = new PubnubResponsePojoHome();
        pubnubResponse_old.setMasArr(temp.getMasArr());
        pubnubResponse_old.setTypes(temp.getTypes());
    }
    //=============================================================

    /**
     * <h1>handleNewVehicleTypesData</h1>
     * <p>
     * 새로운 차량 유형 데이터를 처리한다.
     * pubnub로부터받은 설정 데이터를 처리하고 분석하는 메소드
     *      뷰가 변경된 경우 뷰를 업데이트합니다.
     * </p>
     */
    private void handleNewVehicleTypesData(String configDataNew, int customerApiInterval) {
        Log.d(TAG, "handleNewVehicleTypesData() customerApiInterval: " + customerApiInterval);
        // to store time difference between last received msg and new received msg of a==2
        try {
            PubnubResponsePojoHome pubnubResponse_pojo_home_temp = new Gson().fromJson(configDataNew, PubnubResponsePojoHome.class);
            if (pubnubResponse_old == null || pubnubResponse_pojo_home_temp == null ||
                    pubnubResponse_pojo_home_temp == null || pubnubResponse_pojo_home_temp.getMasArr() == null) {
                Log.d(TAG, "handleNewVehicleTypesData()  NULL");
                pubnubResponse_old = gson.fromJson(configDataNew, PubnubResponsePojoHome.class);
                postNewVehicleTypes();
            } else {
                if (pubnubResponse_pojo_home_temp != null && pubnubResponse_old.getTypes() != null &&
                        !pubnubResponse_old.getTypes().toString().equals
                                (pubnubResponse_pojo_home_temp.getTypes().toString())) {
                    Log.d(TAG, "handleNewVehicleTypesData() VEHICLE TYPES NOT SAME");
                    pubnubResponse_old = null;
                    pubnubResponse_old = gson.fromJson(configDataNew, PubnubResponsePojoHome.class);
                    postNewVehicleTypes();
                } else {
                    Log.d(TAG, "handleNewVehicleTypesData() VEHICLE TYPES SAME");
                    if (pubnubResponse_pojo_home_temp != null && pubnubResponse_old.getMasArr() != null &&
                            !pubnubResponse_old.getMasArr().toString().equals(pubnubResponse_pojo_home_temp.getMasArr().toString())) {
                        Log.d(TAG, "handleNewVehicleTypesData() DRIVERS NOT POS SAME");
                        pubnubResponse_old = null;
                        pubnubResponse_old = gson.fromJson(configDataNew, PubnubResponsePojoHome.class);
                        postDriversMarkerPositions();
                    } else {
                        Log.d(TAG, "handleNewVehicleTypesData()  DRIVERS POS SAME");
                    }
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            Log.d(TAG, "handleNewVehicleTypesData() exc: " + exc);
        }
    }

    //=============================================================

    /**
     * 이 방법은 "BOOKING_ONTHEWAY"화면에서 지도에 드라이버를 플로팅하는 데 사용됩니다.
     *
     * @param message contains the message
     */
    private void driverStatusDetails(String message) {
        if (message != null && !message.equals("")) {
            try {
                Utility.printLog("value of message: " + message);
                DriverPubnubPojo driver_purbnub_pojo = gson.fromJson(message, DriverPubnubPojo.class);
                if (driver_purbnub_pojo != null) {
                    EventBus.getDefault().post(new DriverDetailsEvent(driver_purbnub_pojo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //=============================================================


    /**
     * This method is used for plotting driver on map in "BOOKING_ONTHEWAY" screen.
     *
     * @param message contains the message
     */
    private void driverTrackingDetails(String message) {
        if (message != null && !message.equals("")) {
            try {
                Utility.printLog("value of message: " + message);
                DriverPubnubPojo driver_purbnub_pojo = gson.fromJson(message.toString(), DriverPubnubPojo.class);
                if (driver_purbnub_pojo != null) {
                    EventBus.getDefault().post(new DriverDetailsEvent(driver_purbnub_pojo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //=============================================================


    /**
     * This method is used for plotting driver on map in "BOOKING_ONTHEWAY" screen.
     *
     * @param message contains the message
     */
    private void bookingExpired(String message) {
        if (message != null && !message.equals("")) {
            try {
                Utility.printLog("value of message: " + message);
                DriverPubnubPojo driver_purbnub_pojo = gson.fromJson(message.toString(), DriverPubnubPojo.class);
                if (driver_purbnub_pojo != null) {
                    EventBus.getDefault().post(new DriverDetailsEvent(driver_purbnub_pojo));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    //=============================================================

    /**
     *
     */
    public void postNewVehicleTypes() {
        Log.d(TAG, "postNewVehicleTypes: 탈 것 타입");
        Utility.printLog(TAG + "postNewVehicleTypes posted ");
        if (pubnubResponse_old == null) {
            EventBus.getDefault().post(new ArrayList<Types>());
        } else {
            Log.d(TAG, "postNewVehicleTypes() types: " + pubnubResponse_old.getTypes());
            EventBus.getDefault().post(pubnubResponse_old.getTypes());
        }
        postDriversMarkerPositions();
    }
    //=============================================================

    private void postDriversMarkerPositions() {
        if (pubnubResponse_old != null && pubnubResponse_old.getMasArr() != null) {
            Log.i(TAG, "pubnub drivers size if");
            DriversList driversList = new DriversList(pubnubResponse_old.getMasArr());
            EventBus.getDefault().post(driversList);
        } else {
            Log.i(TAG, "pubnub drivers size else");
            DriversList driversList = new DriversList(new ArrayList<PubnubMasArrayPojo>());
            EventBus.getDefault().post(driversList);
        }
    }
    //=============================================================

    /**
     *
     */
    public void unSubscribePubNub() {
        Log.i(TAG, "unsubscribePubnub() " + pubNubListenerChannles + " hasPubNubSubscribed: " + hasPubNubSubscribed);
        if (hasPubNubSubscribed) {
            pubNub.removeListener(subscribeCallback);

            pubNub.unsubscribe()
                    .channels(Arrays.asList(pubNubListenerChannles))
                    .execute();

            hasPubNubSubscribed = false;
            Log.i(TAG, "unsubscribePubnub() inside true hasPubNubSubscribed: " + hasPubNubSubscribed);
        }

        pnConfiguration = null;
        pubNub = null;
        pubNubMgr = null;
        Log.i(TAG, "unsubscribePubnub() " + " hasPubNubSubscribed: " + hasPubNubSubscribed);


    }
    //=============================================================


    public void stopPubNub() {
        unSubscribePubNub();
        //pnConfiguration = null;
        //pubNub = null;
        pubNubMgr = null;
    }
    //=============================================================


    public static boolean isReceiptActVisible() {
        return isReceiptActVisible;
    }

    public static void setIsReceiptActVisible(boolean isReceiptActVisible) {
        PubNubMgr.isReceiptActVisible = isReceiptActVisible;
    }
}




