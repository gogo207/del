package com.delex.model;

import android.util.Log;

import com.delex.interfaceMgr.SingleCallbackWithParam;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.pojos.PhoneNumberValidator_pojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author  embed on 21/8/17.
 */

public class ShipmentDetail_Model {
    private SessionManager sessionManager;

    public ShipmentDetail_Model( SessionManager sessionManager)
    {

        this.sessionManager = sessionManager;

    }

    /**
     * <h2>getShipmentFare</h2>
     * 이 방법은 위치 간의 실제 운임을 알기 위해 사용되며 또한 특정 지역이 우리 지역에 존재하는지 여부를 알기 위해 사용됩니다.
     * 거리 api를 콜한다
     * @param singleCallbackWithParam
     */
    public void getShipmentFare(final SingleCallbackWithParam singleCallbackWithParam) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ent_wrk_type", sessionManager.getDeliveredId());
            jsonObject.put("pickup", sessionManager.getPickUpAdr());
            jsonObject.put("drop", sessionManager.getDropAdr());
            jsonObject.put("start_lat_long", sessionManager.getPickLt() + "," + sessionManager.getPickLg());
            jsonObject.put("end_lat_long", sessionManager.getDropLt() + "," + sessionManager.getDropLg());

            Log.d("dd", "getShipmentFare: "+jsonObject.toString());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.SHIPMENTfARE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result) {
                    singleCallbackWithParam.doFirstProcess(result);
                    Utility.printLog("inside service position shipment result" + result);
                }
                @Override
                public void onError(String error) {
                    singleCallbackWithParam.onErrorResponse(error);
                    Utility.printLog("error: "+error);
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * <h2>applyPromo</h2>
     * 이 메소드는 api를 호출하여 주어진 내 프로모션 코드가 유효한지 확인함으로써 프로모션 코드를 호출합니다.
     * 유효한 경우 쿠폰을 제시하고 할인을 제공하거나 단순히 그대로 두십시오.
     * This method calling apply promo code, by calling api to check my given promo code is valid or not.
     * If it is valid then show the coupon and provide the discount or else leave it simply.
     * @param coupon coupon code.
     * @param payment payment Type.
     * @param singleCallbackWithParam callback.
     */
    public void applyPromo(final String coupon, String payment, final SingleCallbackWithParam singleCallbackWithParam) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", coupon);
            jsonObject.put("lat", sessionManager.getPickLt());
            jsonObject.put("long", sessionManager.getPickLg());
            jsonObject.put("type", Integer.parseInt(payment));
            Utility.printLog("value of promo params: "+jsonObject);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.PROMO_CODE, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String jsonResponse) {
                    Utility.printLog("value of promo response: "+jsonResponse);
                    PhoneNumberValidator_pojo phoneNumberValidator_pojo;
                    Gson gson = new Gson();
                    phoneNumberValidator_pojo = gson.fromJson(jsonResponse, PhoneNumberValidator_pojo.class);
                    if (phoneNumberValidator_pojo.getStatusCode() == 401)
                        singleCallbackWithParam.onSessionExpire();
                    else
                        singleCallbackWithParam.doFirstProcess(phoneNumberValidator_pojo.getErrFlag()+","+phoneNumberValidator_pojo.getErrMsg());
                }

                @Override
                public void onError(String error) {
                    singleCallbackWithParam.onErrorResponse(error);
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
