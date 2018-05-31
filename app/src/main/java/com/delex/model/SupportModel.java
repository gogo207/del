package com.delex.model;

import com.delex.interfaceMgr.ResponseListener;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.pojos.SupportPojo;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * <h>SupportModel</h>
 * call the service for the support
 * Created by ${Ali} on 8/21/2017.
 */

public class SupportModel
{

    /**
     * calling volley request for support add setting the adapter
     * @param sessionManager {@see SessionManager }
     * @param resLstnr {@see ResponseListener}
     */
    public void callSupportService(SessionManager sessionManager, final ResponseListener resLstnr)
    {
        try {
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.SUPPORT
                            + "/0"+"/"+ Constants.USER_TYPE, OkHttp3Connection.Request_type.GET,
                    new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String response)
                {
                    Utility.printLog("Support response = " + response);
                    Gson gson = new Gson();
                    SupportPojo support_pojo = gson.fromJson(response, SupportPojo.class);
                    if(support_pojo.getStatusCode() != null && support_pojo.getStatusCode().equals("500"))
                    {
                        resLstnr.onError(support_pojo.getErrMsg());
                    }
                    else if (support_pojo.getErrFlag().equals("0") && support_pojo.getErrNum().equals("200"))
                    {
                        resLstnr.onSupportSuccess(support_pojo);
                    } else {

                        resLstnr.onError(support_pojo.getErrMsg());
                    }
                }
                @Override
                public void onError(String error)
                {
                    Utility.printLog("error: " + error);
                }
            });
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
