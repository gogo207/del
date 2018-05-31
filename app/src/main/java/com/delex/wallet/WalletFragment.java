package com.delex.wallet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.bookingFlow.ChangeCardActivity;
import com.delex.customer.R;
import com.delex.chat_module.WalletDataChangedEvent;
import com.delex.interfaceMgr.WalletFragInteractor;
import com.delex.customer.MainActivity;
import com.delex.pojos.WalletDataPojo;
import com.delex.presenterMgr.WalletFragPresenter;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.Utility;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * <h1>WalletFragment</h1>
 * <p>
 *     Class to load Wallet Fragment and show wallet details
 * </p>
 * @since 25/09/17.
 */

public class WalletFragment extends ParentFragment implements View.OnClickListener, WalletFragInteractor.WalletFragViewNotifier
{
    private TextView tvWalletBalance, tvSoftLimitValue, tvHardLimitValue;
    private TextView tvCardNo,tvcardadd, tvCurrencySymbol;
    private EditText etPayAmountValue;
    private Alerts alerts;
    private ProgressDialog pDialog;
    private AppTypeface appTypeface;
    private WalletFragPresenter walletFragPresenter;
    private TextView tv_wallet_text;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appTypeface = AppTypeface.getInstance(getActivity());
    }


    //====================================================================

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        initToolBar(view);
        initViews(view);
        initPayViews(view);
        initSoftHardLimitDescriptionsView(view);
        return view;

    }
    //====================================================================

    /** <h2>initToolBar</h2>
     * <p>
     *     method to initialize customer toolbar
     * </p>
      */
    private void initToolBar(View view)
    {
        ImageView ivMenuBtnToolBar =  view.findViewById(R.id.ivMenuBtnToolBar);
        ivMenuBtnToolBar.setOnClickListener(this);
        TextView tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setTypeface(appTypeface.getPro_narMedium());
        tvTitleToolbar.setText(getString(R.string.recentTransactions));
    }
    //====================================================================

    /**
     *<h2>initViews</h2>
     * <P>
     *  custom method to load top views of the screen
     * </P>
     * @param view: root view of the fragment
     */
    private void initViews(View view)
    {
        TextView tvCurCreditLabel = view.findViewById(R.id.tvCurCreditLabel);
        tvCurCreditLabel.setTypeface(appTypeface.getPro_News());
        tvWalletBalance = view.findViewById(R.id.tvWalletBalance);
        tvWalletBalance.setTypeface(appTypeface.getPro_News());


        tv_wallet_text=view.findViewById(R.id.tv_wallet_text);
        tv_wallet_text.setTypeface(appTypeface.getPro_News());


        TextView tvSoftLimitLabel = view.findViewById(R.id.tvSoftLimitLabel);
        tvSoftLimitLabel.setTypeface(appTypeface.getPro_News());
        tvSoftLimitValue = view.findViewById(R.id.tvSoftLimitValue);
        tvSoftLimitValue.setTypeface(appTypeface.getPro_News());
        TextView tvHardLimitLabel = view.findViewById(R.id.tvHardLimitLabel);
        tvHardLimitLabel.setTypeface(appTypeface.getPro_News());
        tvHardLimitValue = view.findViewById(R.id.tvHardLimitValue);
        tvHardLimitValue.setTypeface(appTypeface.getPro_News());
        Button btnRecentTransactions = view.findViewById(R.id.btnRecentTransactions);
        btnRecentTransactions.setTypeface(appTypeface.getPro_News());
        btnRecentTransactions.setOnClickListener(this);


    }
    //====================================================================

    /**
     *<h2>initPayViews</h2>
     * <P>
     *  custom method to load payment views of the screen
     * </P>
     * @param view: root view of the fragment
     */
    private void initPayViews(View view)
    {
        TextView tvPayUsingCardLabel = view.findViewById(R.id.tvPayUsingCardLabel);
        tvPayUsingCardLabel.setTypeface(appTypeface.getPro_narMedium());

        tvCardNo = view.findViewById(R.id.tvCardNo);
        tvcardadd=view.findViewById(R.id.tvCardNoadd);
        tvcardadd.setTypeface(appTypeface.getPro_News());
        tvcardadd.setOnClickListener(this);
        tvCardNo.setTypeface(appTypeface.getPro_News());
        tvCardNo.setOnClickListener(this);
        TextView tvPayAmountLabel = view.findViewById(R.id.tvPayAmountLabel);
        tvPayAmountLabel.setTypeface(appTypeface.getPro_narMedium());
        tvCurrencySymbol = view.findViewById(R.id.tvCurrencySymbol);
        tvCurrencySymbol.setTypeface(appTypeface.getPro_News());
        TextView tvHardLimitLabel = view.findViewById(R.id.tvHardLimitLabel);
        tvHardLimitLabel.setTypeface(appTypeface.getPro_News());
        tvHardLimitValue = view.findViewById(R.id.tvHardLimitValue);
        tvHardLimitValue.setTypeface(appTypeface.getPro_News());
        etPayAmountValue = view.findViewById(R.id.etPayAmountValue);
        etPayAmountValue.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                walletFragPresenter.setRechargeAmount(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }
    //====================================================================

    /**
     *<h2>initSoftHardLimitDescriptionsView</h2>
     * <P>
     *  custom method to init soft and hard limit description views of the screen
     * </P>
     * @param view: root view of the fragment
     */
    private void initSoftHardLimitDescriptionsView(View view)
    {

        TextView tvSoftLimitMsgLabel = view.findViewById(R.id.tvSoftLimitMsgLabel);
        tvSoftLimitMsgLabel.setTypeface(appTypeface.getPro_News());
        TextView tvSoftLimitMsg = view.findViewById(R.id.tvSoftLimitMsg);
        tvSoftLimitMsg.setTypeface(appTypeface.getPro_News());
        tvSoftLimitMsg.setText(getString(R.string.softLimitMsg));
        TextView tvHardLimitMsgLabel = view.findViewById(R.id.tvHardLimitMsgLabel);
        tvHardLimitMsgLabel.setTypeface(appTypeface.getPro_News());
        TextView tvHardLimitMsg = view.findViewById(R.id.tvHardLimitMsg);
        tvHardLimitMsg.setTypeface(appTypeface.getPro_News());
        tvHardLimitMsg.setText(getString(R.string.hardLimitMsg));
        Button btnConfirmAndPay = view.findViewById(R.id.btnConfirmAndPay);
        btnConfirmAndPay.setTypeface(appTypeface.getPro_News());
        btnConfirmAndPay.setOnClickListener(this);

    }
    //====================================================================

    @Override
    public void onResume()
    {
        super.onResume();
        if(alerts == null)
            alerts = new Alerts();


        if(walletFragPresenter == null)

            walletFragPresenter = new WalletFragPresenter(getActivity(), WalletFragment.this);
            walletFragPresenter.initGetOrderDetails();
            Constants.isWalletFragActive = true;
            EventBus.getDefault().register(this);
    }
    //====================================================================

    /**
     * <p>
     *     to handle events from pubNub or push
     *     that wallet setting or data has modified
     * </p>
     * @param walletDataChangedEvent: retrieved wallet date from pubNub
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WalletDataChangedEvent walletDataChangedEvent)
    {
        walletFragPresenter.initGetOrderDetails();
    }
    //====================================================================


    @Override
    public void onClick(View view)
    {
        Utility.hideSoftKeyBoard(tvWalletBalance);
        switch (view.getId())
        {
            case R.id.ivMenuBtnToolBar:
                DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity)getActivity()).moveDrawer(mDrawerLayout);
                break;

            case R.id.btnRecentTransactions:
                Intent intent = new Intent(getActivity(), WalletTransActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                break;

            case R.id.tvCardNo:
                Intent cardsIntent = new Intent(getActivity(), ChangeCardActivity.class);
                startActivityForResult(cardsIntent, 1);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;

            case R.id.tvCardNoadd:
                Intent cardsIntent1 = new Intent(getActivity(), ChangeCardActivity.class);
                startActivityForResult(cardsIntent1, 1);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
                break;
            case R.id.btnConfirmAndPay:
                showRechargeConfirmationAlert(walletFragPresenter.getCurrencySymbol()
                        +" "+walletFragPresenter.getRechargeValue());
                break;
        }
    }
    //====================================================================


    private void hideProgressDialog()
    {
        if(pDialog != null && pDialog.isShowing())
        {
            pDialog.dismiss();
        }
    }
    //====================================================================


    @Override
    public void showProgressDialog(String msg)
    {
        if(pDialog == null)
        {
            pDialog = new ProgressDialog(getActivity());
        }

        if(!pDialog.isShowing())
        {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(getString(R.string.wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }
    //====================================================================

    @Override
    public void showToast(String msg, int duration)
    {
        hideProgressDialog();
        Toast.makeText(getActivity(), msg, duration).show();
    }
    //====================================================================

    @Override
    public void showAlert(String title, String msg)
    {
        hideProgressDialog();
        Toast.makeText(getActivity(), "SHOW ALERT", Toast.LENGTH_SHORT).show();
    }
    //====================================================================

    @Override
    public void noInternetAlert()
    {
        alerts.showNetworkAlert(getActivity());
    }
    //====================================================================

    /**
     *<h2>walletDetailsApiSuccessViewNotifier</h2>
     * <p>
     *     method to update fields data on the success response of api
     * </p>
     * @param walletDataPojo: Response retrieved from the api call
     * @param currencySymbol: app currency symbol
     */
    @Override
    public void walletDetailsApiSuccessViewNotifier(WalletDataPojo walletDataPojo, String currencySymbol)
    {
        hideProgressDialog();
        etPayAmountValue.setText("");
        if(walletDataPojo.isReachedHardLimit())
        {
            tvWalletBalance.setTextColor(getResources().getColor(R.color.red_light));
        }
        else if(walletDataPojo.isReachedSoftLimit())
        {
            tvWalletBalance.setTextColor(getResources().getColor(R.color.yellow_light));
        }
        else
        {
            tvWalletBalance.setTextColor(getResources().getColor(R.color.black));
        }

        if(!walletDataPojo.getWalletAmount().equalsIgnoreCase("")){

            if(Double.parseDouble(walletDataPojo.getWalletAmount())>0.0){
                tv_wallet_text.setText("*Truckr owe you by"+currencySymbol+" "+Double.parseDouble(walletDataPojo.getWalletAmount()));
            }else if(Double.parseDouble(walletDataPojo.getWalletAmount())<0.0){
                double d= Double.parseDouble(walletDataPojo.getWalletAmount());
                d=d-2*d;
                tv_wallet_text.setText("*You owe Truckr by"+currencySymbol+" "+d);
            }else{

                tv_wallet_text.setText(" ");

            }

        }

        tvWalletBalance.setText(currencySymbol+"-"+walletDataPojo.getWalletAmount());
        tvSoftLimitValue.setText(currencySymbol+"-"+ Utility.getFormattedPrice(String.valueOf(walletDataPojo.getSoftLimit())));
        tvHardLimitValue.setText(currencySymbol+"-"+ Utility.getFormattedPrice(String.valueOf(walletDataPojo.getHardLimit())));
        if(walletFragPresenter.getLastCardNo().equalsIgnoreCase(""))
        {
            tvCardNo.setVisibility(View.GONE);
            tvcardadd.setVisibility(View.VISIBLE);
        }
        else
        {
            tvCardNo.setVisibility(View.VISIBLE);
            tvcardadd.setVisibility(View.GONE);
        }

        tvCardNo.setText("card ending by"+walletFragPresenter.getLastCardNo());
        tvCurrencySymbol.setText(currencySymbol);
    }
    //====================================================================


    /**
     *<h2>walletDetailsApiErrorViewNotifier</h2>
     * <p>
     *     method to notify api error
     * </p>
     */
    @Override
    public void walletDetailsApiErrorViewNotifier(String error)
    {
        showToast(error, Toast.LENGTH_SHORT);
    }
    //====================================================================


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //====================================================================

    @Override
    public void onPause()
    {
        super.onPause();
        Constants.isWalletFragActive = false;
        EventBus.getDefault().unregister(this);
    }
    //====================================================================

    /**
     * <h2>showRechargeConfirmationAlert</h2>
     * <p>
     *     method to show an alert dialog to take user
     *     confirmation to proceed to recharge
     * </p>
     */
    private void showRechargeConfirmationAlert(String amount)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(getActivity().getString(R.string.confirmPayment));
        alertDialog.setCancelable(false);
        alertDialog.setMessage(getActivity().getString(R.string.paymentMsg1)
        + " "+ getActivity().getString(R.string.app_name) +" "+getActivity().getString(R.string.paymentMsg2)+" "+amount);

        alertDialog.setPositiveButton(getActivity().getString(
                R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                walletFragPresenter.initRechargeWalletApi();
                dialog.dismiss();
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton(getActivity().getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    //====================================================================
}
