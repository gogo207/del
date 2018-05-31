package com.delex.servicesMgr;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.delex.chat_module.WalletDataChangedEvent;
import com.delex.chat_module.WalletStatusChangedEvent;
import com.delex.model.DataBaseHelper;
import com.delex.customer.NotificationHandler;
import com.delex.customer.R;
import com.delex.pojos.DataBaseGetItemDetailPojo;
import com.delex.pojos.UnReadCount;
import com.delex.utility.Constants;
import com.delex.utility.Utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

/**
 * <h1>FireBaseMessageReceiver</h1>
 * This class is for handling the messages those were came from FCM server.
 * @author 3embed
 * @since 6 Apr 2017.
 */
public class FireBaseMessageReceiver extends FirebaseMessagingService{
    PendingIntent intent = null;
    NotificationManager notificationManager;
    private static final String TAG = "FireBase_Message";
    private Bundle mbundle;
    private Bundle mbundle1;
    private int count =0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Utility.printLog(TAG+"onMessageReceived "+remoteMessage.getData());
        Utility.printLog(TAG+"onMessageReceived notification "+remoteMessage.getNotification());
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {
            Log.d(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            try {
                //JSONObject jsonObject = new JSONObject();
                String str = new Gson().toJson(remoteMessage.getNotification());
                /*jsonObject.put("msg", remoteMessage.getNotification().getBody());
                jsonObject.put("st", remoteMessage.getNotification().get;
                jsonObject.put("msg", remoteMessage.getNotification().getBody()));
                jsonObject.put("msg", remoteMessage.getNotification().getBody()));
                jsonObject.put("msg", remoteMessage.getNotification().getBody()));*/

                Log.d(TAG, "Notification jsonObject: " + new Gson().toJson(str));
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

            // app is in foreground, broadcast the push message
            /*Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", remoteMessage.getNotification().getBody());
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);*/
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Data Payload: " + remoteMessage.getData().toString());
            if(remoteMessage.getNotification()==null){
                handleMessage(remoteMessage.getData(),null);
            }else{
                handleMessage(remoteMessage.getData(),remoteMessage.getNotification().getBody());
            }

        }

        //Log.d(TAG, "From:" +remoteMessage.getFrom());
        //Log.d(TAG, "data: "+remoteMessage.getData());
        //JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        //check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null)
        {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    /**
     * <h2>handleMessage</h2>
     * <p>
     *     method to parse and handle the data received from fcm
     * </p>
     * @param response: json object of the received object
     */
    private void handleMessage(Map response,String bodyNoti)
    {
        String status;
        String bid;
        String subid=null;
        String pay_load;
        String body,title;
        String unread;
        String chatId;
        String recieveId;
        String senderName;
        String message;

        pay_load = String.valueOf(response.get("msg"));
        status = (String) response.get("st");
        bid = (String) response.get("bid");
        body= (String) response.get("body");
        title= (String) response.get("title");
        unread=(String)response.get("nt");
        chatId=(String)response.get("chatId");
        recieveId=(String)response.get("senderId");
        senderName=(String)response.get("senderName");
        message=(String)response.get("message");

        Utility.printLog("FCM isApplicationSentToBackground " + getBaseContext().getPackageName());
        boolean isbkrnd = isApplicationSentToBackground(getBaseContext());
        Utility.printLog("FCM isApplicationSentToBackground " + isbkrnd);

        Log.d("asdf", "notification: inside came: " +status +" ,payload: "+ pay_load+" ,isbkrnd: "+isbkrnd+" ,status: "+status);
        if (pay_load != null && !isbkrnd && status!=null)
        {
            DataBaseHelper dataBaseHelper;
            dataBaseHelper = new DataBaseHelper(getBaseContext());
            Utility.printLog("notification: inside came intent ");



            //This status becomes 420, when booking got completed by server.
            //This status becomes 25, when booking got accepted through website.
            if (!status.equals("2") )        //(!status.equals("25") && !status.equals("2") && !status.equals("22") && !status.equals("420"))            //(!status.equals("2"))        //THis status (2), means order is already accepted, so no need to show it once again.
            {
                DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo_notfication;
                dataBase_getItem_detail_pojo_notfication = dataBaseHelper.extractFrMyOrderDetail(bid, subid);
                if(dataBase_getItem_detail_pojo_notfication!=null)
                    if (!dataBase_getItem_detail_pojo_notfication.getStatus().equals(status))
                    {
                        Utility.printLog("notification: inside came intent else");
                        dataBaseHelper.updateOrderStatus(status, bid, subid);
                        Toast.makeText(getBaseContext(), pay_load, Toast.LENGTH_SHORT).show();
                        Utility.printLog("notification: inside came intent" + bid + "sub " + subid + "status" + status);
                    }
            }
            else if(!status.isEmpty())
            {
                int statusCode = Integer.parseInt(status.trim());
                switch (statusCode)
                {
                    case 45:        //reachedSoftLimit
                    case 46:        //reachedHardLimit
                    case 47:        //cameOutOfSoftLimit
                    case 48:        //cameOutOfHardLimit
                    case 49:        //newTransaction
                    case 50:        //bothSoftAndHarLimitChanged
                    case 51:        //softLimitBoundryChanged
                    case 52:        //hardLimitBoundryChanged
                        Log.i(TAG, "message action: "+statusCode + "  isWalletFragActive: "+
                                Constants.isWalletFragActive+"  isWalletUpdateCalled: "+Constants.isWalletUpdateCalled);
                        if(Constants.isWalletFragActive && !Constants.isWalletUpdateCalled)
                        {
                            // uncomment it when get wallet data from push also
                            /*final WalletDataChangedEvent walletDataChangedEvent = new Gson().fromJson(msg, WalletDataChangedEvent.class);
                            EventBus.getDefault().post(walletDataChangedEvent);*/
                            EventBus.getDefault().post(new WalletDataChangedEvent());
                        }
                        break;

                    case 53:        //walletEnabled
                    {
                        WalletStatusChangedEvent walletStatusChangedEvent = new WalletStatusChangedEvent();
                        walletStatusChangedEvent.setWalletEnabled(true);
                        EventBus.getDefault().post(walletStatusChangedEvent);
                    }
                    break;

                    case 54:        //walletDisabled
                    {
                        WalletStatusChangedEvent walletStatusChangedEvent = new WalletStatusChangedEvent();
                        walletStatusChangedEvent.setWalletEnabled(false);
                        EventBus.getDefault().post(walletStatusChangedEvent);
                    }
                    break;

                    default:
                        break;
                }
            }
        }

        if(bodyNoti!=null){
            pay_load=bodyNoti;
            body= bodyNoti;
        }

if(unread!=null) {
    if (unread.equalsIgnoreCase("1010")) {
        status = "20";
        bid = chatId;
        subid = recieveId;
        title = senderName;
        pay_load = message;
        count++;
        UnReadCount unReadCount = new UnReadCount();
        unReadCount.setUnreadCount(String.valueOf(count));
        EventBus.getDefault().post(unReadCount);
    }
}

        sendNotification(pay_load,status,bid,subid,body,title);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.

    /**
     * <h2>sendNotification</h2>
     * <p>
     *     method to Put the message into a notification and post it.
     * </p>
     * @param msg: contains the data to be display in notification
     * @param status: status value i.e action id
     * @param bid: booking id
     * @param subid: sub id of booking
     */
    private void sendNotification(String msg, String status, String bid, String subid,String body ,String titleNotification)
    {
        Utility.printLog( "sendNotification  msg " + msg + " bid to send " + bid + "action " + status+"body="+body);
        String title = this.getString(R.string.app_name);
        long when = System.currentTimeMillis();

        if(status==null)
        {
            if(titleNotification!=null){

                title=titleNotification;
            }

            if(body!=null){
                msg=body;
            }

        }





            notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            mbundle1 = new Bundle();

            mbundle1.putString("status", status);
            mbundle1.putString("bid", bid);


            if(status!=null){
            if(status.equals("20")){


                Intent chatIntent = new Intent(this, ChatMessagesScreen.class);
                mbundle1.putString("recieverPhoto", "");


                mbundle1.putString("receiverUid", subid);
                mbundle1.putString("receiverName", title);
                mbundle1.putString("chatName", bid);

                chatIntent.putExtras(mbundle1);
                intent = PendingIntent.getActivity(this,0, chatIntent, PendingIntent.FLAG_ONE_SHOT);


                }
                else {
                Intent notificationIntent1 = new Intent(this, NotificationHandler.class);

                Utility.printLog(" bundle in notification12 " + mbundle1 + " status " + status + " bid " + bid);
                notificationIntent1.putExtras(mbundle1);
                intent = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            }
            else {
                Intent notificationIntent1 = new Intent(this, NotificationHandler.class);
                Utility.printLog(" bundle in notification12 " + mbundle1 + " status " + status + " bid " + bid);
                notificationIntent1.putExtras(mbundle1);
                intent = PendingIntent.getActivity(this, 0, notificationIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
            }



        if(status!=null)
        {
            Constants.latesBid=bid;
            Constants.latesstatus=status;
            Constants.latesSubBid=subid;
        }


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_stat_name)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_launcher))
                .setContentTitle(title).setContentText(msg)
                .setTicker(msg).setAutoCancel(true)
                        .setStyle(new NotificationCompat.InboxStyle())
                .setContentIntent(intent)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify( 0, notificationBuilder.build());
        boolean isbkrnd = isApplicationSentToBackground(this);
        Log.d("log2","2");
        if(!isbkrnd)
        {

     Log.d("log3","3");
     if(status!=null){

         if(status.equalsIgnoreCase("20")){
             Log.d("log4","4");
             doBroadcast(false);

         }else{

             doBroadcast(true);
         }
     }else {
         doBroadcast(true);
     }
        }else{
         doBroadcast(true);
     }



    }

    /**
     * <h2>isApplicationSentToBackground</h2>
     * <p>
     *     method to check that whether app is in foreground or in background
     * </p>
     * @param context: calling activiyt reference
     * @return: true if app is in background
     */
    public static boolean isApplicationSentToBackground(final Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty())
        {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName()))
            {
                return true;
            }
        }
        return false;
    }


    /**
     * <h2>doBroadcast</h2>
     * <p>
     *     method to broadcast the data
     * </p>
     */
    private void doBroadcast(boolean check)
    {


        Log.d("log1","1");

        Intent intent = new Intent("com.dayrunner.passenger.booking");
        if(check){
            intent.putExtras(mbundle1);
            sendBroadcast(intent);
        }

        }
}
