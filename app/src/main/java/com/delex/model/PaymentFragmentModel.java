package com.delex.model;

import android.content.Context;
import android.os.Bundle;


import com.delex.customer.R;
import com.delex.interfaceMgr.NetworkCheck;
import com.delex.interfaceMgr.PaymentFragmentResponce;
import com.delex.pojos.CardInfoPojo;
import com.delex.pojos.GetCard_pojo;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * <h>PaymentFragmentModel</h>
 * service call for the payment to get the all card
 * Created by ${Ali} on 8/19/2017.
 */

public class PaymentFragmentModel
{
    private Context mcontext;
    private SessionManager manager;
    private PaymentFragmentResponce paymentRes;
    public PaymentFragmentModel(Context mcontext)
    {
        this.mcontext = mcontext;
        manager = new SessionManager(mcontext);

    }

    public void NetworkISAvailble(NetworkCheck checkNetwork)
    {
        if(Utility.isNetworkAvailable(mcontext)){

            checkNetwork.isNetworkAvailble(true);
        }
        else{
            checkNetwork.isNetworkAvailble(false);

        }
    }

    /**
     * This method is used to call the service for knowing all the cards that we previously stored in our profiles.
     * @param presponce object of PaymentFragmentResponce
     */

    public void cllGetAllCards(PaymentFragmentResponce presponce)
    {
        paymentRes = presponce;
        JSONObject jsonObject = new JSONObject();
        OkHttp3Connection.doOkHttp3Connection(manager.getSession(),manager.getLanguageId(), Constants.GETCARD, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String jsonResponse) {
                Utility.printLog("The Response get card " + jsonResponse);
                callGetCardServiceResponse(jsonResponse);
            }
            @Override
            public void onError(String error) {
                paymentRes.errorResponse(mcontext.getResources().getString(R.string.something_went_wrong));
            }
        });
    }
    /**
     * This method is used to perform the work, that we will do after getting the response from server.
     */
    private void callGetCardServiceResponse(String Response) {

        try {
            Gson gson = new Gson();
            GetCard_pojo response = gson.fromJson(Response, GetCard_pojo.class);
            switch (response.getErrFlag())
            {
                case 0:
                    paymentRes.clearRowItem();
                    if(response.getData().length>0)
                    {
                        for (int i = 0; i < response.getData().length; i++)
                        {
                            CardInfoPojo item = new CardInfoPojo(response.getData()[i].getBrand(), response.getData()[i].getLast4(), response.getData()[i].getExp_month(),
                                    response.getData()[i].getExp_year(), response.getData()[i].getId(),response.getData()[i].getDefaultCard(),
                                    response.getData()[i].getName(), response.getData()[i].getFunding());//id

                            if (response.getData()[i].getDefaultCard())
                            {
                                manager.setLastCard(response.getData()[i].getId());
                                manager.setLastCardNumber(response.getData()[i].getLast4());
                                manager.setCardType(response.getData()[i].getFunding());
                                manager.setLastCardImage(response.getData()[i].getBrand());
                            }
                            paymentRes.ResponseItem(item);
                        }
                    }
                    else
                    {
                        manager.setLastCard("");
                        manager.setLastCardNumber("");
                        manager.setCardType("");
                        manager.setLastCardImage("");
                    }
                    paymentRes.notifyAdapter();
                    break;
                case 1:
                    paymentRes.errorResponse(response.getErrMsg());
                    Utility.sessionExpire(mcontext);
                    break;
                default:
                    paymentRes.errorResponse(mcontext.getResources().getString(R.string.something_went_wrong));
                    break;
            }

        }
        catch (Exception e) {
            paymentRes.errorResponse(e.toString());
        }
    }

    /**
     * <h2>createBundle</h2>
     * This method is used for creating the bundle to delete the card
     * @param row_details takes the object of CardInfoPojo
     * @return returns the bundle
     */
    public Bundle createBundle(CardInfoPojo row_details)
    {
        String expDate = row_details.getExp_month();
        if (expDate.length() == 1) {
            expDate = "0" + expDate;
        }
        expDate = expDate + "/" + row_details.getExp_year();
        Bundle bundle = new Bundle();
        bundle.putString("NUM", row_details.getCard_numb());
        bundle.putString("EXP", expDate);
        bundle.putString("ID", row_details.getCard_id());
        bundle.putString("NAM", row_details.getName());
        bundle.putBoolean("DFLT",row_details.getDefaultCard());
        bundle.putString("IMG", row_details.getCard_image());
        return bundle;
    }

}
