package com.delex.bookingFlow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.pojos.GoodsDataPojo;
import com.delex.pojos.GoodsTypePojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.adapter.GoodsItemAdapter;
import com.google.gson.Gson;
import com.delex.customer.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * <h>GoodsTypeActivity</h>
 * <p>
 *     Class to
 * </p>
 */
public class GoodsTypeActivity extends ParentActivity
        implements AdapterView.OnItemClickListener
{
    private ListView listView;
    private ArrayList<GoodsDataPojo> list;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    private GoodsItemAdapter adapter;
    private AppTypeface appTypeface;
    private TextView tvSubmitButton;
    private EditText et_other_types;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_type);
        appTypeface = AppTypeface.getInstance(this);
        initToolBar();
        initVariables();
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     *     initializing the respective tool bar
     *     for this screen
     * </p>
     */
    private void initToolBar()
    {
        Toolbar toolbar =  findViewById(R.id.tb_custom_a);
        setSupportActionBar(toolbar);
        TextView tv_toolbar_title = findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setTypeface(appTypeface.getPro_narMedium());
        tv_toolbar_title.setText(R.string.goods_type);
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if(Utility.isRTL())
            {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_right_white_24dp);
            }
            else
            {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            }
        }
    }

    /**
     * <h2>initVariables</h2>
     * <p>
     *     method to initialize the variables and other
     *     required fields
     * </p>
     */
    private void initVariables()
    {
        sessionManager = new SessionManager(GoodsTypeActivity.this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getResources().getString(R.string.wait));
        listView = findViewById(R.id.lv_goodsType);
        et_other_types=(EditText)findViewById(R.id.et_other_types);
        tvSubmitButton=(TextView)findViewById(R.id.tv_submit_goodstype);
        tvSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GoodsTypeActivity.this, ShipmentDetailsActivity.class);

                if(sessionManager.getGoodsName().equalsIgnoreCase("others")){
                    sessionManager.setGoodsName(et_other_types.getText().toString());
                    sessionManager.setLastGoodsID("");
                    intent.putExtra("goods_title", sessionManager.getGoodsName());
                    intent.putExtra("goods_id", sessionManager.getLastGoodsID());
                }else {
                    intent.putExtra("goods_title", sessionManager.getGoodsName());
                    intent.putExtra("goods_id", sessionManager.getLastGoodsID());
                }

                setResult(Constants.GOODS_TYPE_INTENT, intent);
                finish();
            }
        });


        list = new ArrayList<GoodsDataPojo>();
        adapter=new GoodsItemAdapter(this,list);

        if(Utility.isNetworkAvailable(GoodsTypeActivity.this))
        {
            progressDialog.show();
            getGoodsTypeApi();
        }
        else
        {
            new Alerts().showNetworkAlert(GoodsTypeActivity.this);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * <h2>getGoodsTypeApi</h2>
     * <p>
     * This method is used to call a service for getting all the available goods.
     * </p>
     */
    private void getGoodsTypeApi()
    {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authorization", sessionManager.getSession());
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), Constants.GOODS_TYPE, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
                @Override
                public void onSuccess(String result)
                {
                    progressDialog.dismiss();
                    Gson gson = new Gson();
                    GoodsTypePojo goods_type_pojo = gson.fromJson(result, GoodsTypePojo.class);
                    Utility.printLog("value of result: " + result + ", token: " + sessionManager.getSession());
                    if (goods_type_pojo.getStatusCode() != null && goods_type_pojo.getStatusCode() == 401)
                    {
                        Toast.makeText(GoodsTypeActivity.this, getString(R.string.force_logout_msg), Toast.LENGTH_LONG).show();
                        Utility.sessionExpire(GoodsTypeActivity.this);
                    }
                    else
                    {


                        for (int pos = 0; pos < goods_type_pojo.getData().length; pos++)
                        {
                            list.add(pos, goods_type_pojo.getData()[pos]);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onError(String error) {
                    progressDialog.dismiss();
                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        sessionManager.setLastGoodsID(list.get(position).get_id());
        sessionManager.setGoodsName(list.get(position).getName());
        ImageView tv = (ImageView) view.findViewById(R.id.iv_goods_type_check);

        tv.setVisibility(View.VISIBLE);

        if(list.get(position).getName().equalsIgnoreCase("others")){

        }else{
            Intent intent = new Intent(GoodsTypeActivity.this, ShipmentDetailsActivity.class);
            intent.putExtra("goods_title", sessionManager.getGoodsName());
            intent.putExtra("goods_id", sessionManager.getLastGoodsID());
            setResult(Constants.GOODS_TYPE_INTENT, intent);
            finish();
        }





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        onBackPressed();
        return true;
    }
}
