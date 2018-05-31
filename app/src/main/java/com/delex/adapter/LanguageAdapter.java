package com.delex.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.delex.customer.R;
import com.delex.customer.SplashActivity;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.SessionManager;

import java.util.ArrayList;

/**
 * Created by embed on 3/1/18.
 */

public class LanguageAdapter extends RecyclerView.Adapter {

    private ArrayList<String>listLang;
    private ArrayList<String>listLangId;
    private Activity mactivity;
    private SessionManager sessionManager;
    private Resources resources;
    private AppTypeface appTypeface;

    public LanguageAdapter(ArrayList<String> listLang, Activity mactivty,ArrayList<String> listlangId)
    {
        this.mactivity=mactivty;
        this.listLang= listLang;
        this.listLangId=listlangId;
        sessionManager= new SessionManager(mactivty);
        appTypeface = AppTypeface.getInstance(mactivty);
        resources = mactivty.getResources();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_language, parent, false);
        viewHolder = new LangViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final LangViewHolder vh2 = (LangViewHolder)holder;
        vh2.tvLang.setText(listLang.get(position));
        vh2.tvLang.setTypeface(appTypeface.getPro_News());
        if(sessionManager.getLanguageId().equalsIgnoreCase(listLangId.get(position))){
            vh2.tvLang.setTextColor(Color.parseColor("#e40d00"));
        }else {
            vh2.tvLang.setTextColor(Color.parseColor("#ff444444"));

        }


        vh2.bodyClick.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                vh2.tvLang.setTextColor(Color.parseColor("#e40d00"));
                setLocale(listLangId.get(position),mactivity);
                sessionManager.setLanguageId(listLangId.get(position)+"");
                Intent intent = new Intent(mactivity, SplashActivity.class);
                intent.putExtra("text_select",listLang.get(position));
                mactivity.startActivity(intent);
                mactivity.finish();
               // mactivity.finishAfterTransition();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLangId.size();
    }


    class LangViewHolder extends RecyclerView.ViewHolder {

        private TextView tvLang;
        private RelativeLayout bodyClick;
        public LangViewHolder(View itemView) {
            super(itemView);
            tvLang=(TextView)itemView.findViewById(R.id.tv_lang_txt);
            bodyClick=(RelativeLayout) itemView.findViewById(R.id.body_language);
        }
    }



    private void setLocale(String langCode ,Context context1) {

        Log.d("setLocale: ",langCode);
        LocaleHelper.setLocale(context1, langCode);

    }
}
