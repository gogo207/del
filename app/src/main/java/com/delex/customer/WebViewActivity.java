package com.delex.customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.ParentActivity;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.Utility;

/**
 * <h>WebView Activity</h>
 * This class is used to provide the WebView screen, where we can
 * show the web view, and show our all data of html document.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class WebViewActivity extends ParentActivity
{
	private ProgressBar progressBar;
	private String title = "", url = "";

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(LocaleHelper.onAttach(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
		Intent intent=getIntent();
		if(intent!=null)
		{

			url = getIntent().getStringExtra("Link");
			title = getIntent().getStringExtra("Title");

		}
		initToolBar();
		initViews();
	}

	/**
	 * <h2>initToolBar</h2>
	 * <p>
	 *     method to initialize toolbar for this activity
	 * </p>
	 */
	private void initToolBar()
	{
		if(Utility.isRTL())
		{
			ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
			ivBackBtn.setRotation((float) 180.0);
		}

		TextView tvToolBarTitle =  findViewById(R.id.tvToolBarTitle);
		tvToolBarTitle.setTypeface(AppTypeface.getInstance(this).getPro_narMedium());

		if(title.length() > 25)
		{
			title = title.substring(0,24);
			title = title + "...";
		}

		tvToolBarTitle.setText(title);
		RelativeLayout rlBackArrow = findViewById(R.id.rlBackArrow);
		rlBackArrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}

	/**
	 * <h2>initViews</h2>
	 */
	private void initViews()
	{
		WebView webView =  findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		webView.setSaveFromParentEnabled(true);

		progressBar =  findViewById(R.id.pBar_profileFrag);
		progressBar.setVisibility(View.GONE);
		if (validateUrl(url))
		{

			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(url);


			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
				webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
	}


	/**
	 * This method is used to validate the URL.
	 * @param url URL link
	 * @return flag.
	 */
	private boolean validateUrl(String url)
	{
		return Patterns.WEB_URL.matcher(url).matches();
	}

	/**
	 * <h1>MyWebViewClient</h1>
	 * <p>
	 * This is an inner class, to load the web view data.
	 * </p>
	 */
	private class MyWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			progressBar.setVisibility(View.GONE);
			WebViewActivity.this.progressBar.setProgress(100);
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon)
		{
			progressBar.setVisibility(View.VISIBLE);
			WebViewActivity.this.progressBar.setProgress(0);
			super.onPageStarted(view, url, favicon);
		}
	}

	/**
	 * <h2>setValue</h2>
	 * <p>
	 *     method to set progress value
	 * </p>
	 * @param progress: progress value
	 */
	public void setValue(int progress)
	{
		this.progressBar.setProgress(progress);
	}

	@Override
	public void onBackPressed()
	{
		finish();
		overridePendingTransition(R.anim.side_slide_out,R.anim.side_slide_in);
	}
}
