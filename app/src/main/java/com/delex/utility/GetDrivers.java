package com.delex.utility;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * <h1>GetDrivers</h1>
 * <P>
 *     Class to get drivers from servers
 * </P>
 * @since 27/7/17.
 */

public class GetDrivers
{
    private String TAG = "GetDrivers";
    private Context context;
    private SessionManager sessionManager;

    public GetDrivers(Context context)
    {
        this.context = context;
        sessionManager= new SessionManager(context);
    }

    /**
     * <h2>getDrivers</h2>
     * <p>
     *     This method is used for calling get Drivers API.
     * </p>
     */
    public void getDrivers(final String sessionToken, final String url)
    {
        Log.d(TAG, "getDrivers1 url: "+url);
        //String url = Constants.GET_DRIVERS+ currentLat+"/"+ currentLng+"/"+ sessionManager.getChannel() + "/0";
        OkHttp3Connection.doOkHttp3Connection(sessionToken,sessionManager.getLanguageId(), url, OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result)
            {
                try
                {
                    Utility.printLog(TAG+ "GetDrivers result: "+result);
                    JSONObject jsnResponse = new JSONObject(result);
                    if (jsnResponse.has("statusCode"))
                    {
                        int statusCode = jsnResponse.getInt("statusCode");
                        Log.d(TAG, "publish() statusCode: " + statusCode);
                        if (statusCode == 401)
                        {
                            Utility.sessionExpire(context);
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error)
            {
            }
        });
    }

    /**
     * <h2>getDrivers</h2>
     * <P>
     *     This method is used for calling get Drivers API.
     * </P>
     * @param callback, proper callback to the Caller's Activity.
     * URL - contains the value either 1 or 0. 1-> returns vehicle and driver information , 0-> won't return anything.
     */
    public void getDrivers(final String sessionToken, final String url, final GetDriversCallback callback)
    {
        Log.d(TAG, "getDrivers2 url: "+url);
        //final String url = Constants.GET_DRIVERS + currentLat + "/" + currentLng + "/" + sessionManager.getChannel() + "/" + sessionManager.getPresenceTime() + "/1";
        OkHttp3Connection.doOkHttp3Connection(sessionToken,sessionManager.getLanguageId(), url,
                OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback()
                {
                    @Override
                    public void onSuccess(String result)
                    {
                        try
                        {
                            Utility.printLog(TAG+ "GetDrivers result: "+result);
                            JSONObject jsnResponse = new JSONObject(result);
                            if (jsnResponse.has("statusCode"))
                            {
                                int statusCode = jsnResponse.getInt("statusCode");
                                Log.d(TAG, "publish() statusCode: " + statusCode);
                                if (statusCode == 401)
                                {
                                    Utility.sessionExpire(context);
                                }
                            }
                            else
                                callback.success(result);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error)
                    {
                        callback.error(error);
                    }
                });
    }

    /**
     * <h2>GetDriversCallback</h2>
     * <P>
     *     interface to return the getDrivers api call back to calling activity or fragment
     * </P>
     */
    public interface GetDriversCallback
    {
        /**
         *Method for sucess .
         * @param success it is true on sucess and false for falure.*/
        void success(String success);
        /**
         * Method for falure.
         * @param errormsg contains the error message.*/
        void error(String errormsg);
    }
}
