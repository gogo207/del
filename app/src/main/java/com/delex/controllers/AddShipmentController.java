package com.delex.controllers;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.delex.logger.Log;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.model.AddShipmentModel;
import com.delex.pojos.ShipmentDetailSharePojo;
import com.delex.interfaceMgr.ContactInterface;

import org.json.JSONArray;

/**
 * <h2>AddShipmentController</h2>
 * This is the controller of Add shipment.
 * @since 24-Aug-2017
 */

public class AddShipmentController {
    private Activity context;
    private AddShipmentModel model;

    public AddShipmentController(Activity context, SessionManager sessionManager)
    {
        this.context = context;
        model = new AddShipmentModel(context, sessionManager);
    }

    /**
     * This is the method that will pass our control to the model class where we are calling the API.
     * @param name receiver name.
     * @param phone receiver phone.
     * @param notes extra notes.
     * @param isImageAvailable: if any pic added
     * @param imageJsonArray image array.
     * @param sharePojo data.
     */
    public void requestBooking(String name, String phone, String notes, boolean isImageAvailable, JSONArray imageJsonArray, ShipmentDetailSharePojo sharePojo
    ,String countryCode,String length,String width,String hieght,String dimenunit) {
        model.liveBooking(name, phone, notes, isImageAvailable, imageJsonArray, sharePojo,countryCode,length,width,hieght,dimenunit);
    }

    /**
     * This method will call when we choose a contact from the contact list and then from the onActivityResult() method , we are calling this
     * method, to do further work.
     * @param data: bundle of data passed
     * @param countryCode: selected country isd code
     * @param contactInterface: reference of interface ContactInterface
     */
    public void selectContact(Intent data, String countryCode, ContactInterface contactInterface)
    {
        Uri contactData = data.getData();
        Cursor c = context.managedQuery(contactData, null, null, null, null);
        if (c.moveToFirst()) {
            String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            if (hasPhone.equalsIgnoreCase("1"))
            {
                Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                phones.moveToFirst();
                String cNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String nameContact = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                Utility.printLog("value of contact: "+cNumber+ " ,name: "+nameContact);
                /*if (nameContact.equals(cNumber))
                {
                    contactInterface.firstProcess("");
                }
                else
                {
                    contactInterface.secondProcess(nameContact);
                }*/
                if (cNumber.contains(countryCode)) {
                    Utility.printLog("value of phone: 4:if: "+cNumber + " ,name: "+nameContact);
                    cNumber = cNumber.replace(countryCode, "");
                    contactInterface.thirdProcess(nameContact, cNumber);
                }
                else
                {
                    contactInterface.fourthProcess(nameContact, cNumber);
                    Utility.printLog("value of phone: 4:else: "+cNumber + " ,name: "+nameContact);
                }
                Utility.printLog("value of phone: 4:done: "+cNumber + " ,name: "+nameContact);
            }
        }
    }

    public void setNoOfHelpers(String noOfHelpers)
    {
        Log.d("AddShipmentController", "setNoOfHelpers noOfHelpers: "+noOfHelpers);
        model.setNoOfHelpers(noOfHelpers);
    }
}
