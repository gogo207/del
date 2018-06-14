package com.delex.servicesMgr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;


import com.delex.parent.ParentActivity;
import com.google.firebase.database.FirebaseDatabase;
import com.delex.chat_module.ModelClasses.SelectUserItem;
import com.delex.customer.R;


public class FireBaseChatActivity extends ParentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_main);

        /******************************************************************************************/
        //to initialize the user
        String userSid=getIntent().getStringExtra("USER_ID");
        String userName=getIntent().getStringExtra("USER_NAME");
        String userEmail=getIntent().getStringExtra("USER_EMAIL");
        String session_token=getIntent().getStringExtra("TOKEN");

        SelectUserItem item = new SelectUserItem();
        item.setUserId(userSid);
        item.setUserName(userName);
        item.setEmail(userEmail);
        item.setPushToken(session_token);

        writeNewUser(item);

        AppController.getInstance().setupPresenceSystem(item.getUserId());

        Toast.makeText(this,
                "Welcome " + item.getUserName(),
                Toast.LENGTH_LONG)
                .show();


            /*
             * Will update the push token for the user
             */
        if (AppController.getInstance().getPushToken() != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(item.getUserId()).child("pushToken").setValue(AppController.getInstance().getPushToken());
        }


        AppController.getInstance().setupPresenceSystem(item.getUserId());
        Intent i = new Intent(FireBaseChatActivity.this, Chatlist.class);
        i.putExtra("userId", item.getUserId());
        startActivity(i);
        supportFinishAfterTransition();

        /******************************************************************************************/
    }

    private void writeNewUser(SelectUserItem user) {

        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUserId()).setValue(user);
    }
}
