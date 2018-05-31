package com.delex.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;

/**
 * <h1>Terms & Condition Activity</h1>
 * This class is used to provide the Terms & Condition screen, where we can check the Terms & Condition.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class TermsActivity extends Activity implements OnClickListener
{
	private AppTypeface appTypeface;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_terms_cond);
		appTypeface = AppTypeface.getInstance(this);
		initToolBar();
		initializeVariables();

	}

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(LocaleHelper.onAttach(newBase));

	}

	/**
	 * <h2>initToolBar</h2>
	 * <p>
	 *     method to initialize the tool bar for this activity
	 * </p>
	 */
	private void initToolBar()
	{
		if(Utility.isRTL())
		{
			ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
			ivBackBtn.setRotation((float) 180.0);
		}

		RelativeLayout rlBackArrow =  findViewById(R.id.rlBackArrow);
		rlBackArrow.setOnClickListener(this);

		TextView toolBarTitle =  findViewById(R.id.tvToolBarTitle);
		toolBarTitle.setTypeface(appTypeface.getPro_narMedium());
		toolBarTitle.setText(getString(R.string.legal));
	}

	/**
	 * <h2>initializeVariables</h2>
	 * <p>
	 *     here we are initialize all view element of this activity
	 * </p>
	 */
	private void initializeVariables()
	{
		RelativeLayout rlTermsConditions = findViewById(R.id.rlTermsConditions);
		rlTermsConditions.setOnClickListener(this);
		RelativeLayout rlPrivacyPolicy =  findViewById(R.id.rlPrivacyPolicy);
		rlPrivacyPolicy.setOnClickListener(this);
		TextView tvTermsConditions =  findViewById(R.id.tvTermsConditions);
		tvTermsConditions.setTypeface(appTypeface.getPro_News());
		TextView tvPrivacyPolicy =  findViewById(R.id.tvPrivacyPolicy);
		tvPrivacyPolicy.setTypeface(appTypeface.getPro_News());
	}

	/**
	 * <h2>onClick</h2>
	 * <p>override the onclick method of OnClickListener</p>
	 *  @see OnClickListener
	 */
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.rlBackArrow:
				onBackPressed();
				break;

			default:
				break;

			case R.id.rlTermsConditions:
				Intent intentTerms =new Intent(this,WebViewActivity.class);
				intentTerms.putExtra("Link", Constants.TERMS_LINK);
				intentTerms.putExtra("Title", getResources().getString(R.string.terms_and_conditions));
				startActivity(intentTerms);
				overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
				break;

			case R.id.rlPrivacyPolicy:
				Intent intentPrivacy =new Intent(this,WebViewActivity.class);
				intentPrivacy.putExtra("Link", Constants.PRIVECY_LINK);
				intentPrivacy.putExtra("Title", getResources().getString(R.string.privacy_policy));
				startActivity(intentPrivacy);
				overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
				break;

		}
	}


	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
	}
}
