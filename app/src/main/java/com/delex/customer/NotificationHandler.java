package com.delex.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.delex.ParentActivity;
import com.delex.bookingHistory.BookingAssignedActivity;
import com.delex.bookingHistory.ReceiptActivity;
import com.delex.model.DataBaseHelper;
import com.delex.utility.Utility;
import com.delex.utility.Constants;

/**
 * <h1>NotificationHandler Activity</h1>
 * This class is used to provide the NotificationHandler screen, where we can get all the Notifications and can divide based on their
 * status.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class NotificationHandler extends ParentActivity
{
    String status = "",bid = "",subid ="",recieveId="",userId="",SenderName="";
    DataBaseHelper dataBaseHelper;
    //DataBase_getItem_Detail_pojo dataBase_getItem_detail_pojo;

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        dataBaseHelper = new DataBaseHelper(NotificationHandler.this);
        if(bundle!=null)
        {
            status = bundle.getString("status");
            bid = bundle.getString("bid");
            subid = bundle.getString("Subid");
            recieveId=bundle.getString("receiverUid");
                    userId=bundle.getString("chatName");
                            SenderName=bundle.getString("receiverName");


        }

        if(status == null || status.equals(""))
        {
            Intent intent=new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(intent);
        }

        if(bid==null)
        {
            status = Constants.latesstatus;
            bid = Constants.latesBid;
            subid = Constants.latesSubBid;
            Utility.printLog(" bundle null" + bundle);

        }


        Utility.printLog(" bundle " + bundle + " status " + status + " bid " + bid);

        if(subid==null) {
            subid = "1";
        }

        if(status.equals("2") || status.equals("6") || status.equals("7")||
                status.equals("8")||  status.equals("9")|| status.equals("16"))
        {
            Intent notificationIntent = new Intent(this, BookingAssignedActivity.class);
            notificationIntent.putExtra("status", status);
            notificationIntent.putExtra("bid", bid);
            notificationIntent.putExtra("Subid", subid);
            notificationIntent.putExtra("comingfrom", "notalert");
            notificationIntent.putExtra("status", status);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(notificationIntent);

        }else if(status.equals("4")){
            Intent notificationIntent = new Intent(this, MainActivity.class);
            Constants.bookingFlag = true;
            Constants.cacelFlag=true;
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(notificationIntent);
        }
        else if(status.equals("10"))
        {
            Constants.bookingFlag = true;
            Intent notificationIntent = new Intent(this, ReceiptActivity.class);
            notificationIntent.putExtra("bid", bid);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(notificationIntent);

            /*Intent notificationIntent = new Intent(this, FireBaseChatActivity.class);
            Constants.bookingFlag = true;
            Constants.cacelFlag=true;
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(notificationIntent);*/

        }
        else if(status.equals("11"))
        {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            this.finish();
            startActivity(notificationIntent);
        }
//        else if(status.equals("20")){
//
//            Intent notificationIntent1 = new Intent(this, ChatMessagesScreen.class);
//            Bundle bundle1 = new Bundle();
//            bundle1.putString("receiverUid", recieveId);
//            bundle1.putString("receiverName",SenderName);
//            bundle1.putString("chatName", userId);
//            bundle1.putString("recieverPhoto", "");
//            notificationIntent1.putExtras(bundle1);
//            this.finish();
//            startActivity(notificationIntent1);
//        }
    }
}
