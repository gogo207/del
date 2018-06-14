package com.delex.bookingHistory;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.customer.R;

import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;
import com.delex.presenterMgr.ReceiptRatingCommentPresenter;
import com.delex.interfaceMgr.ReceiptRatingCommentInteractor;

/**
 * <h1>ReceiptRatingCommentActivity</h1>
 * <p>
 *     Activity to take input for rating reason
 * </p>
 * @since 06/09/17.
 */

public class ReceiptRatingCommentActivity extends ParentActivity
        implements ReceiptRatingCommentInteractor.ViewNotifier
{
    private TextInputLayout tilRatingReason;
    private TextInputEditText tietRatingReason;
    private AppTypeface appTypeface;
    private ReceiptRatingCommentPresenter receiptRatingCommentPresenter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_rating_comment);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
        appTypeface = AppTypeface.getInstance(this);
        receiptRatingCommentPresenter = new ReceiptRatingCommentPresenter(this, this);

        if(getIntent() == null)
            finish();
            initToolBar();
            initViews();
    }
    //====================================================================

    @Override
    protected void onResume()
    {
        super.onResume();
        Utility.hideSoftKeyBoard(tilRatingReason);
    }
    //====================================================================


    /* <h2>initToolBar</h2>
        * <p>
        *     method to initialize customer toolbar
        * </p>
        */
    private void initToolBar()
    {
        if(getActionBar() != null)
            getActionBar().hide();
        Toolbar toolBarInvoice =  findViewById(R.id.mToolBarLeftTitle);
        setSupportActionBar(toolBarInvoice);

        if(Utility.isRTL())
        {
            ImageView ivBackBtn =  findViewById(R.id.ivABarBackBtn);
            ivBackBtn.setRotation((float) 180.0);
        }

        RelativeLayout rlABarBackBtn = findViewById(R.id.rlABarBackBtn);
        rlABarBackBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                receiptRatingCommentPresenter.on_BackPressed();
            }
        });

        TextView tvAbarTitleLeft = findViewById(R.id.tvAbarTitleLeft);
        tvAbarTitleLeft.setTypeface(appTypeface.getPro_narMedium());

        if(getIntent() != null)
            tvAbarTitleLeft.setText(getIntent().getStringExtra(Constants.INTENT_TAG_SELECTED_REASON));
    }
    //====================================================================


    /**
     * <h2>initViews</h2>
     * <p>
     *     method to initialize the views of this screen
     * </p>
     */
    public void initViews()
    {
        TextView tvCommentLabel = findViewById(R.id.tvCommentLabel);
        tvCommentLabel.setTypeface(appTypeface.getPro_narMedium());

        tilRatingReason = findViewById(R.id.tilRatingReason);
        tietRatingReason = findViewById(R.id.tietRatingReason);
        tietRatingReason.setTypeface(appTypeface.getPro_News());
        tietRatingReason.clearFocus();

        Button btnSubmitRatingComment = findViewById(R.id.btnSubmitRatingComment);
        btnSubmitRatingComment.setTypeface(appTypeface.getPro_narMedium());
        btnSubmitRatingComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                receiptRatingCommentPresenter.onSubmitBtnClick(tietRatingReason.getText().toString());
            }
        });
    }
    //====================================================================


    @Override
    public void seErrorForComment(boolean isToSetError, String errorMsg)
    {
        if(isToSetError)
        {
            tilRatingReason.setErrorEnabled(true);
            tilRatingReason.setError(errorMsg);
            tietRatingReason.requestFocus();
        }
        else
        {
            tilRatingReason.setErrorEnabled(false);
        }
    }

    @Override
    public void showProgressDialog(String msg)
    {
    }

    @Override
    public void showToast(String msg, int duration)
    {
        Toast.makeText(this, msg, duration).show();
    }

    @Override
    public void showAlert(String title, String msg)
    {
    }

    @Override
    public void noInternetAlert()
    {
    }
    //====================================================================

    @Override
    public void onBackPressed()
    {
        receiptRatingCommentPresenter.on_BackPressed();
    }
    //====================================================================
}
