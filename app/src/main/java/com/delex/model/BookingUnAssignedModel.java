package com.delex.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.delex.customer.R;

import com.delex.ETA_Pojo.ElementsForEta;
import com.delex.ETA_Pojo.EtaPojo;
import com.delex.interfaceMgr.AssignedBookingsInterface;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.controllers.BookingUnAssignedController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <h1>BookingUnAssignedModel</h1>
 * <h4>This is a Model class for BookingUnAssigned Activity</h4>
 * This class is used for performing the task related to Database , API calling and Image uploading and
 * this class is getting called from BookingUnAssignedController class.
 * @version 1.0
 * @since 25/08/17
 * @see BookingUnAssignedController
 */
public class BookingUnAssignedModel
{
    private static final String TAG = "BookingUnAssignedModel";
    private Activity context;
    private SessionManager sessionManager;
    private Resources resources;
    private ProgressDialog progressDialog;
    private AssignedBookingsInterface assignedBookingsInterface;

    public BookingUnAssignedModel(Activity context, SessionManager sessionManager,
                                  AssignedBookingsInterface assignedBookingsInterface)
    {
        this.context = context;
        this.sessionManager = sessionManager;
        this.assignedBookingsInterface=assignedBookingsInterface;
        resources = context.getResources();
        initDialog();
    }

    /**
     * <h2>initDialog</h2>
     * <p>
     * This method is used for initialising the Progress Dialog.
     * </p>
     */
    private void initDialog()
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(resources.getString(R.string.wait));
        progressDialog.setCancelable(false);
    }

    /**
     * <h2>getDriverDetail</h2>
     * <p>
     * This method is used for calling driver detail api and on success setting driver details to particular fields.
     * </p>
     */
    public void getDriverDetail(String bid) {
        progressDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", bid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sessiontoken: ",sessionManager.getSession());
        OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.SINGLE_BOOKING, OkHttp3Connection.Request_type.POST, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()   {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                Utility.printLog("BookingUnAssigned onSuccess JSON DATA in  detail response" + result);
                if (result.contains("ï")) {
                    result = result.replace("ï", "");
                    result = result.replace("»", "");
                    result = result.replace("¿", "");
                }
                assignedBookingsInterface.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                progressDialog.dismiss();
            }
        });
    }

    /**
     * <h2>initETACall</h2>
     * This method is used to initiate the ETA call
     */
    public void initETACall(String ... params)
    {
        new getETAForDriver().execute(params);
    }

    /**
     * <h2>getETA</h2>
     * This class is used to get the ETA from google
     */
    private class getETAForDriver extends AsyncTask<String, Void, ArrayList<ElementsForEta>>
    {
        private EtaPojo etaPojo=new EtaPojo();
        @Override
        protected ArrayList<ElementsForEta> doInBackground(String... params)
        {
            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                    params[0] + "," + params[1] + "&" + "destinations=" + params[2]+ "," + params[3] +
                    "&mode=driving" + "&" + "key=" + sessionManager.getGoogleServerKey();
            Log.d(TAG, "Distance matrix getETA() url: " + url);
            try
            {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String result = response.body().string();
                if (result != null)
                {
                    Gson gson = new Gson();
                    etaPojo = gson.fromJson(result, EtaPojo.class);

                    if (!etaPojo.getStatus().equals("OK") )
                    {
                        Log.d(TAG, "Distance matrix key exceeded ");
                        //if the stored key is exceeded then rotate to next key
                        List<String> googleServerKeys=sessionManager.getGoogleServerKeys();
                        if(googleServerKeys.size()>0)
                        {
                            Log.d(TAG, "Distance matrix keys size before remove "+googleServerKeys.size());
                            googleServerKeys.remove(0);
                            Log.d(TAG, "Distance matrix keys size after remove "+googleServerKeys.size());
                            if(googleServerKeys.size()>0)
                            {
                                Log.d(TAG, "Distance matrix keys next key "+googleServerKeys.get(0));
                                //store next key in shared pref
                                sessionManager.setGoogleServerKey(googleServerKeys.get(0));
                                //if the stored key is exceeded then rotate to next and call eta API
                                initETACall();
                            }
                            //to store the google keys array by removing exceeded key from list
                            sessionManager.setGoogleServerKeys(googleServerKeys);
                        }
                    }
                    Utility.printLog(TAG+"Distance matrix getETA() duration:doInBackground " +
                            etaPojo.getRows().get(0).getElements().get(0).getDuration());
                }
            }
            catch (Exception exc)
            {
                exc.printStackTrace();
            }
            return etaPojo.getRows().get(0).getElements();
        }

        @Override
        protected void onPostExecute(ArrayList<ElementsForEta> etaElementsOfDriver)
        {
            Utility.printLog(TAG+"Distance matrix getETA() duration:onPostExecute " +
                    etaPojo.getRows().get(0).getElements().get(0).getDuration());
            if(!etaElementsOfDriver.isEmpty())
                assignedBookingsInterface.OnGettingOfETA(etaElementsOfDriver);
        }
    }
}
