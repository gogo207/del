/*
package com.moola.CardValidation;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.seven_moola.customer.R;
import com.moola.utility.SessionManager;
import com.moola.utility.Utility;

import org.json.JSONException;
import org.json.JSONObject;

public class WebViewActivity extends AppCompatActivity implements OnClickListener
{
	private WebView mWeb;
	private RelativeLayout progress_bar;
	private String isFromPayment="";
	private String isCardAdded="0";
	private boolean showPopup=true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);

		initialize();
		TextView titleTxt= (TextView) findViewById(R.id.title);
		String url="",title;
		mWeb=(WebView)findViewById(R.id.webView1);
		mWeb.clearCache(true);
		progress_bar=(RelativeLayout) findViewById(R.id.progress_bar);
		mWeb.getSettings().setJavaScriptEnabled(true);
		mWeb.getSettings().setDomStorageEnabled(true);
		mWeb.setWebViewClient(this.webViewMainWebClient);
		mWeb.setWebChromeClient(new WebChromeClient());
		Bundle bundle=getIntent().getExtras();

		if(bundle!=null)
		{
			url=bundle.getString("URL");
			title=bundle.getString("TITLE");

			isFromPayment=bundle.getString("PAYMENT");
			titleTxt.setText(title);
			Utility.printLog("coming for paymetnt "+isFromPayment+" url "+url);
			SessionManager sessionManager=new SessionManager(WebViewActivity.this);
			Utility.setTypefaceMuliRegular(this,titleTxt);
			if(isFromPayment==null)
			{
				mWeb.loadUrl(url);
			}
			else if(isFromPayment.equals("1"))
			{
				mWeb.loadUrl(url+"&"+"lan_id="+sessionManager.getLanguage());
			}
		}
	}
	WebViewClient webViewMainWebClient = new WebViewClient()
	{
		// Override page so it's load on my view only
		@Override
		public boolean shouldOverrideUrlLoading(WebView  view, String  url)
		{
			Utility.printLog("url for the webview "+url);
			mWeb.setVisibility(View.GONE);
			if (progress_bar.getVisibility()==View.GONE) {
				progress_bar.setVisibility(View.VISIBLE);
			}
			if(isFromPayment.equals("1"))
			{
				if(url.contains("success.php"))
				{
					isCardAdded="1";
				}
				else if(url.contains("error.php"))
				{
					isCardAdded="2";
				}
			}
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mWeb.setVisibility(View.VISIBLE);
			if (progress_bar.getVisibility()==View.VISIBLE) {
				progress_bar.setVisibility(View.GONE);
			}
			Utility.printLog("url in finish "+url);
			if(isFromPayment!=null)
			{
				if(isCardAdded.equals("1"))
				{
					if(showPopup && !WebViewActivity.this.isFinishing())
					{
						showAlert(getResources().getString(R.string.card_added),true);
					}
				}
				else if(isCardAdded.equals("2"))
				{
					if(showPopup && !WebViewActivity.this.isFinishing())
						showAlert(getResources().getString(R.string.card_not_added),false);
				}
			}
		}
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Utility.printLog("error in webviwew "+errorCode+" desc "+description);
			finish();
		}
	};

	public void showAlert(String messageToBeShown, final boolean success)
	{
		showPopup=false;
		final Dialog dialog=Utility.showPopupWithOneButton(WebViewActivity.this);
		TextView title_popup= (TextView) dialog.findViewById(R.id.title_popup);
		TextView text_for_popup= (TextView) dialog.findViewById(R.id.text_for_popup);
		TextView yes_button= (TextView) dialog.findViewById(R.id.yes_button);
		title_popup.setVisibility(View.GONE);
		yes_button.setText(getResources().getString(R.string.ok));
		dialog.setCancelable(false);

		text_for_popup.setText(messageToBeShown);
		yes_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(success)
				{
					Intent intent=new Intent();
					setResult(RESULT_OK,intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}
				else
				{
					Intent intent=new Intent();
					setResult(RESULT_CANCELED,intent);
					finish();
					overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				}

			}
		});
		dialog.show();
	}

	private void initialize()
	{
		*/
/**
		 * to set language support
		 *//*

		Utility.setLanguageSupport(this);
		*/
/**
		 * to set the status bar color
		 *//*

		Utility.setStatusBarColor(this);

		RelativeLayout RL_Webview = (RelativeLayout) findViewById(R.id.rl_signin);
		ImageButton back_btn = (ImageButton) findViewById(R.id.back_btn);
		TextView title = (TextView)findViewById(R.id.title);

		SessionManager sessionManager=new SessionManager(this);
		*/
/**
		 * to flip the arrow
		 *//*

		if(sessionManager.getLanguageCode().equals("ar"))
		{
			if (back_btn != null) {
				back_btn.setScaleX(-1);
			}
		}

		if(sessionManager.getLanguageCode().equals("en"))
			Utility.setTypefaceMuliBold(this,title);

		assert RL_Webview != null;
		RL_Webview.setOnClickListener(this);
		assert back_btn != null;
		back_btn.setOnClickListener(this);
	}
	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.back_btn:
			{
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				break;
			}
			case R.id.rl_signin:
			{
				finish();
				overridePendingTransition(R.anim.mainfadein, R.anim.slide_down_acvtivity);
				break;
			}
		}
	}


}
*/
