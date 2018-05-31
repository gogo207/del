package com.delex.customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.pojos.RateCardDataPojo;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.RateCardAdapter;
import com.delex.pojos.RateCard_Pojo;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>RateCardFragment</h1>
 *  This is the RateCardFragment which show the User details
 * @since 18 May,2017
 * @author 3Embed
 */
public class RateCardFragment extends ParentFragment
{
    private AppTypeface appTypeface;
    private RateCardAdapter rateCardAdapter;
    private ProgressDialog dialogL;
    private SessionManager sessionManager;
    private ArrayList<RateCardDataPojo> rateCardAL;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        appTypeface = AppTypeface.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        rateCardAL = new ArrayList<RateCardDataPojo>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_rate_card, container, false);
        initializeToolBar(view);
        initializeViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(Utility.isNetworkAvailable(getActivity()))
        {
            dialogL.show();
            getRateCardDetails();
        }
        else
        {
            getResources().getString(R.string.nonetwork);
        }
    }

    /**
     *<h2>initializeToolBar</h2>
     * <p>
     *     method to initialize tool bar for this fragment
     * </p>
     * @param view: fragment root view
     */
    private void initializeToolBar(View view)
    {
        Toolbar toolbar = view.findViewById(R.id.tb_custom_f);
        TextView tvTitleToolbar = toolbar.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setText(getActivity().getString(R.string.rate_card));
        tvTitleToolbar.setTypeface(appTypeface.getPro_narMedium());

        ImageView ivMenuBtnToolBar = toolbar.findViewById(R.id.ivMenuBtnToolBar);
        ivMenuBtnToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout mDrawerLayout =  getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity) getActivity()).moveDrawer(mDrawerLayout);
            }
        });
    }

    /**
     * <h3>initializeView</h3>
     * <p>
     * In this method views of the fragment is initialized
     * </p>
     * @param view: fragment root view
     */
    private void initializeViews(View view)
    {
        rateCardAdapter = new RateCardAdapter(getContext());
        RecyclerView rv_RateCard =  view.findViewById(R.id.rv_RateCard);
        rv_RateCard.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_RateCard.setAdapter(rateCardAdapter);
        dialogL = Utility.GetProcessDialog(getActivity());
    }

    /**
     * <h2>getRateCardDetails</h2>
     * <p>
     * calling volley request for support add setting the adapter
     * </p>
     * */
    public void getRateCardDetails()
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorization", sessionManager.getSession());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.RATE_CARD , OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String response)
                {
                    Utility.printLog("Support response = " + response);
                    Gson gson = new Gson();
                    RateCard_Pojo rateCard_pojo = gson.fromJson(response, RateCard_Pojo.class);
                    if (rateCard_pojo.getErrFlag().equals("0") && rateCard_pojo.getErrNum().equals("200"))
                    {
                        dialogL.cancel();
                        Collections.addAll(rateCardAL, rateCard_pojo.getData());
                        rateCardAdapter.setArrayList(rateCardAL);
                        rateCardAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), rateCard_pojo.getErrMsg(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String error)
                {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                    Utility.printLog("error: " + error);
                }
            });
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
        }
        catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }
}
