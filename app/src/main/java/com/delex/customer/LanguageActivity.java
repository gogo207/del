package com.delex.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.google.gson.Gson;
import com.delex.adapter.LanguageAdapter;
import com.delex.interfaceMgr.LanguageApi;
import com.delex.pojos.LanguagePojo;
import com.delex.utility.Constants;
import com.delex.utility.CustomLinearLayoutManager;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;

public class LanguageActivity extends ParentActivity implements LanguageApi {

    private RecyclerView rvLangList;
    private SessionManager sessionManager;
    private ProgressDialog pDialog;
    private ArrayList<String> Language_List;
    private ArrayList<String> Language_ID;
    private LanguageAdapter madapter;
    private ImageView  CancelBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        sessionManager= new SessionManager(this);
        rvLangList=(RecyclerView)findViewById(R.id.lang_list);
        CancelBtn=(ImageView)findViewById(R.id.cancelbtin);
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LanguageActivity.this,LoginActivity.class);
                finish();
                startActivity(intent);
            }
        });


        rvLangList.setLayoutManager(new CustomLinearLayoutManager(LanguageActivity.this, LinearLayoutManager.VERTICAL, false));
        pDialog = Utility.GetProcessDialog(this);
        pDialog.setMessage(getString(R.string.wait));
        pDialog.setCancelable(false);
        getlanguage(this);



    }


    public void getlanguage(final LanguageApi languageApi){

        JSONObject jsonObject= new JSONObject();

        OkHttp3Connection.doOkHttp3Connection("",sessionManager.getLanguageId(), Constants.LANGUAGE, OkHttp3Connection.Request_type.GET, jsonObject, new OkHttp3Connection.OkHttp3RequestCallback() {
            @Override
            public void onSuccess(String result) {
                pDialog.dismiss();
                Log.d("onSuccesslang:",result);
                Gson gson = new Gson();
                LanguagePojo languagePojo = gson.fromJson(result, LanguagePojo.class);
                languageApi.SuccessLang(languagePojo);
            }
            @Override
            public void onError(String error) {
                languageApi.errorLang(error);
                Toast.makeText(getApplicationContext(), getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });
    }

    @Override
    public void SuccessLang(LanguagePojo result) {
        Log.d("SuccessLang: ","entering");
        Language_List= new ArrayList<String>();
        Language_ID= new ArrayList<String>();

        for (int i=0;i<result.getData().size();i++){
            Language_List.add(i,result.getData().get(i).getLanName());
            Language_ID.add(i,result.getData().get(i).getCode());
            madapter= new LanguageAdapter(Language_List,LanguageActivity.this,Language_ID);
            rvLangList.setAdapter(madapter);
        }

    }

    @Override
    public void errorLang(String error){
    }
}
