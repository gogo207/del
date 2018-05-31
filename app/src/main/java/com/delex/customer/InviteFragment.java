package com.delex.customer;


import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.delex.ParentFragment;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Invite Screen</h1>
 * This class is used to provide the Invite screen, where we can invite other persons, by sending message or mail.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class InviteFragment extends ParentFragment implements View.OnClickListener
{
    private Alerts alerts;
    private String shareCOde;
    private Resources resources;
    private String message = "";
    private AppTypeface appTypeface;
    private ShareDialog shareDialog;



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        resources=getResources();
        SessionManager sessionManager =  new SessionManager(getActivity());
        shareCOde=sessionManager.getCoupon();
        alerts= new Alerts();
        shareDialog=new ShareDialog(getActivity());
        String app_link = "https://play.google.com/store/apps/details?id=com.trukr.passenger";
        message = resources.getString(R.string.invite_msg_1)+"\n"+resources.getString(R.string.invite_msg_2)
                + " "+resources.getString(R.string.invite_app_name) + " "+
                getString(R.string.invite_msg_3) +" "+sessionManager.getCoupon()+ " "
                + resources.getString(R.string.invite_msg_4)+" " +resources.getString(R.string.invite_app_name)
                +"\n"+" " + getResources().getString(R.string.invite_msg_5)+" " +app_link;
    }

    /**
     * <p>inflating the view</p>
     * @param inflater inflater to inflate the view
     * @param container contains the view
     * @param savedInstanceState instance of the view
     * @return the instance of View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view=  inflater.inflate(R.layout.fragment_invite,container,false);
        initToolBar(view);
        initializeViews(view);
        return view;
    }

    /**
     * <h2>initToolBar</h2>
     * <p>
     *     method to initialize the toolbar for this fragment
     * </p>
     */
    private void initToolBar(View view)
    {
        appTypeface = AppTypeface.getInstance(getActivity());
        ImageView iv_toolbar_f = view.findViewById(R.id.ivMenuBtnToolBar);
        iv_toolbar_f.setOnClickListener(this);

        TextView tv_toolbar_f = view.findViewById(R.id.tvTitleToolbar);
        tv_toolbar_f.setTypeface(appTypeface.getPro_narMedium());
        tv_toolbar_f.setText(getActivity().getString(R.string.invite));
    }

    /**
     * <h2>initViews</h2>
     * <p>initialize view elements</p>
     * @param view instance of View.
     */
    private void initializeViews(View view)
    {
        TextView tv_invite_info = view.findViewById(R.id.tv_invite_info);
        tv_invite_info.setTypeface(appTypeface.getPro_News());

        TextView tv_invite_code = view.findViewById(R.id.tv_invite_code);
        tv_invite_code.setTypeface(appTypeface.getPro_narMedium());


        TextView tv_shareText_label = view.findViewById(R.id.tv_shareText_label);
        tv_shareText_label.setTypeface(appTypeface.getPro_News());

        TextView tv_facebook = view.findViewById(R.id.tv_facebook);
        tv_facebook.setOnClickListener(this);
        tv_facebook.setTypeface(appTypeface.getPro_News());

        TextView tv_twitter = view.findViewById(R.id.tv_twitter);
        tv_twitter.setOnClickListener(this);
        tv_twitter.setTypeface(appTypeface.getPro_News());

        TextView tv_message = view.findViewById(R.id.tv_message);
        tv_message.setOnClickListener(this);
        tv_message.setTypeface(appTypeface.getPro_News());

        TextView tv_mail = view.findViewById(R.id.tv_mail);
        tv_mail.setOnClickListener(this);
        tv_mail.setTypeface(appTypeface.getPro_News());

        TextView tv_app_name = view.findViewById(R.id.tv_app_name);
        tv_app_name.setTypeface(appTypeface.getPro_narMedium());
    }


    /**
     * <p>overriding onclick method</p>
     * @see View.OnClickListener
     * @param v the view to be clicked
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_facebook:

                facebookShare();

                break;

            case R.id.tv_twitter:
                twitterShare();
                break;

            case R.id.tv_message:
               messageShare();
                break;

            case R.id.tv_mail:
                emailShare();
                break;

            case R.id.ivMenuBtnToolBar:
                DrawerLayout mDrawerLayout =  getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
                break;

            default:
                break;
        }
    }


    /**
     * <h2>facebookShare</h2>
     * <p>
     *     method to share on facebook
     * </p>
     */
    private void facebookShare()
    {

        if (Utility.isNetworkAvailable(getActivity())) {


            if (ShareDialog.canShow(ShareLinkContent.class))
            {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle(getActivity().getResources().getString(R.string.app_name))
                        .setContentDescription("Install truckr today")
//                        .setImageUrl(Uri.parse("https://s3.amazonaws.com/yellowcarappimages/ic_launcher.png"))
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.truckr.customer"))
                        .build();

                shareDialog.show(linkContent);
            }
        }


    }



    private void facebookDataShare(){

        List<Intent> targetedShareIntents = new ArrayList<Intent>();

        Intent facebookIntent = getShareIntent("facebook", "Download Truckr","");

        if(facebookIntent != null)
            targetedShareIntents.add(facebookIntent);

        Intent chooser = Intent.createChooser(targetedShareIntents.remove(0), "Delen");

        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));

        startActivity(chooser);

    }

    private Intent getShareIntent(String type, String subject, String text)
    {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo =getContext().getPackageManager().queryIntentActivities(share, 0);
        System.out.println("resinfo: " + resInfo);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  subject);
                    share.putExtra(Intent.EXTRA_TEXT,     text);
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return null;

            return share;
        }
        return null;
    }

    /**
     * <h2>twitterShare</h2>
     * <p>
     *     method to share on twitter
     * </p>
     */
    private void twitterShare()
    {
        String Body=message;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, Body);
        boolean twitterAppFound = false;
        List<ResolveInfo> matches = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                intent.setPackage(info.activityInfo.packageName);
                twitterAppFound = true;
                break;
            }
        }
        if(twitterAppFound)
        {
            startActivity(intent);
        }
        else
        {
            if (Utility.isNetworkAvailable(getActivity()))
            {
                String url = "https://twitter.com/";        //"https://www.twitter.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            else{
                alerts.showNetworkAlert(getActivity());
            }
        }
    }

    /**
     * <h2>messageShare</h2>
     * <p>
     *     method to share through sms
     * </p>
     */
    private void messageShare()
    {
        String smsBody = message;
        Intent sms=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"));
        sms.putExtra("sms_body",smsBody);
        startActivity(sms);
    }

    /**
     * <h2>emailShare</h2>
     * <p>
     *     method to share via email
     * </p>
     */
    private void emailShare()
    {
        String Body = message;
        Intent email=new Intent(Intent.ACTION_SENDTO);
        email.putExtra(Intent.EXTRA_SUBJECT,resources.getString(R.string.registeron) + " " +resources.getString(R.string.app_name));
        email.putExtra(Intent.EXTRA_TEXT,Body);
        email.setType("text/plain");
        email.setType("message/rfc822");
        email.setData(Uri.parse("mailto:" + " "));
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

}
