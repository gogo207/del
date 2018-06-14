package com.delex.customer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.a_main.MainActivity;
import com.delex.parent.ParentFragment;
import com.delex.utility.AppTypeface;
import com.delex.utility.Utility;

import com.delex.utility.Constants;

/**
 * <h1>About Fragment</h1>
 * <p>
 * This Fragment is used to provide the About Fragment, where we can know about our product and services.
 * </p>
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class AboutFragment extends ParentFragment implements View.OnClickListener
{
    private AppTypeface appTypeface;


    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appTypeface = AppTypeface.getInstance(getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_about,container,false);
        initToolBar(view);
        initViews(view);
        return view;
    }


    /**
     * <h2>initToolBar</h2>
     * <p>
     *     method to init tool bar for this fragment
     * </p>
     * @param view: this fragment root view reference
     */
    private void initToolBar(View view)
    {

        ImageView ivMenuBtnToolBar =  view.findViewById(R.id.ivMenuBtnToolBar);
        ivMenuBtnToolBar.setOnClickListener(this);
        TextView tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setTypeface(appTypeface.getPro_narMedium());
        tvTitleToolbar.setText(getString(R.string.about));

    }

    /**
     * <h2>initViews</h2>
     * <p>Initializing view elements</p>
     * @param view View instance.
     */
    void initViews(View view)
    {
        RelativeLayout rlRateInGooglePlay =  view.findViewById(R.id.rlRateInGooglePlay);
        rlRateInGooglePlay.setOnClickListener(this);
        TextView tvRateInGooglePlay =  view.findViewById(R.id.tvRateInGooglePlay);
        tvRateInGooglePlay.setTypeface(appTypeface.getPro_News());
        RelativeLayout rlLikeInFacebook =  view.findViewById(R.id.rlLikeInFacebook);
        rlLikeInFacebook.setOnClickListener(this);
        TextView tvLikeInFacebook =  view.findViewById(R.id.tvLikeInFacebook);
        tvLikeInFacebook.setTypeface(appTypeface.getPro_News());
        RelativeLayout rlLegal =  view.findViewById(R.id.rlLegal);
        rlLegal.setOnClickListener(this);
        TextView tvLegal =  view.findViewById(R.id.tvLegal);
        tvLegal.setTypeface(appTypeface.getPro_News());
        TextView tvAppVersion =  view.findViewById(R.id.tvAppVersion);
        tvAppVersion.setTypeface(appTypeface.getPro_News());
        tvAppVersion.setText(getActivity().getString(R.string.version)
                +" "+ Utility.getAppVersion(getActivity()));
    }


    /**
     * <h2>onClick</h2>
     * override the on click listener methods
     * @param v view instance.
     */
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ivMenuBtnToolBar:
                DrawerLayout mDrawerLayout=  getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
                break;

            case R.id.rlRateInGooglePlay:    // open play store to rate the app
                if (Utility.isNetworkAvailable(getActivity()))
                {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(goToMarket);
                    }
                    catch (ActivityNotFoundException e)
                    {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    }
                }
                else
                {
                    Utility.showAlert(getActivity().getString(R.string.network_problem), getActivity());
                }
                break;


            case R.id.rlLikeInFacebook:       // open facebook link
                if (Utility.isNetworkAvailable(getActivity()))
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(Constants.FACEBOOK_LINK));
                    startActivity(i);
                }
                else
                {
                    Utility.showAlert(getActivity().getString(R.string.network_problem), getActivity());
                }
                break;

            case R.id.rlLegal:       // start activity for terms and condtion
                Intent intent=new Intent(getActivity(), TermsActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;
            default:
                break;
        }
    }
}
