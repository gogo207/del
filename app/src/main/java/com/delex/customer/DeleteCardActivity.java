package com.delex.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentActivity;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.google.gson.Gson;
import com.delex.utility.Constants;
import com.delex.pojos.AddCardResponse;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * <h1>DeleteCardActivity</h1>
 * <p>
 *     Activity to delete cards
 * </p>
 */
public class DeleteCardActivity extends ParentActivity
{
	private ProgressDialog pDialog;
	private String cardId;
	private Resources resources;
	private SessionManager session;
	private AppTypeface appTypeface;

	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(LocaleHelper.onAttach(newBase));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_delete_card);
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);

		resources=this.getResources();
		session=new SessionManager(this);
		pDialog=new ProgressDialog(this);
		pDialog.setMessage(resources.getString(R.string.wait));
		appTypeface = AppTypeface.getInstance(this);

		initToolBar();
		initializeViews();
	}

	/**
	 * <h2>initToolBar</h2>
	 * <p>
	 *     custom method to init tool bar
	 * </p>
	 */
	private void initToolBar()
	{
		TextView toolBarTitle = findViewById(R.id.tvToolBarTitle);
		toolBarTitle.setTypeface(appTypeface.getPro_narMedium());
		toolBarTitle.setText(resources.getString(R.string.Card_Info));

		if(Utility.isRTL())
		{
			ImageView ivBackBtn =  findViewById(R.id.ivBackArrow);
			ivBackBtn.setRotation((float) 180.0);
		}

		RelativeLayout rlBack = findViewById(R.id.rlBackArrow);
		rlBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}

	/**
	 * <h2>initViews</h2>
	 * <p>
	 *     Initialize all view elements
	 * </p>
	 */
	private void initializeViews()
	{
		Bundle bundle=getIntent().getExtras();
		boolean isDefaultCard = bundle.getBoolean("DFLT");
		String cardImage = bundle.getString("IMG");
		cardId = bundle.getString("ID");

		TextView tvCardNumberDeleteCard = findViewById(R.id.tvCardNumberDeleteCard);
		tvCardNumberDeleteCard.setTypeface(appTypeface.getPro_News());
		tvCardNumberDeleteCard.setText(bundle.getString("NUM"));

		TextView tvCardNameDeleteCard = findViewById(R.id.tvCardNameDeleteCard);
		tvCardNameDeleteCard.setTypeface(appTypeface.getPro_News());
		tvCardNameDeleteCard.setText(bundle.getString("NAM"));

		TextView tvExpiryLabelDeleteCard = findViewById(R.id.tvExpiryLabelDeleteCard);
		tvExpiryLabelDeleteCard.setTypeface(appTypeface.getPro_News());

		TextView tvExpiryDateDeleteCard = findViewById(R.id.tvExpiryDateDeleteCard);
		tvCardNumberDeleteCard.setTypeface(appTypeface.getPro_News());
		tvExpiryDateDeleteCard.setText(bundle.getString("EXP"));

		ImageView ivCardDelete = findViewById(R.id.ivCardDelete);
		if(cardImage!=null)
		{
			ivCardDelete.setImageResource(Utility.checkCardType(cardImage));
		}

		if(!isDefaultCard)
		{
			CardView cvSetAsDefaultCard =  findViewById(R.id.cvSetAsDefaultCard);
			cvSetAsDefaultCard.setVisibility(View.VISIBLE);
		}

		Button btnDeleteCard = findViewById(R.id.btnDeleteCard);
		btnDeleteCard.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(Utility.isNetworkAvailable(DeleteCardActivity.this))
				{
					deleteCards(cardId);
				}
				else
				{
					Alerts alerts=new Alerts();
					alerts.showNetworkAlert(DeleteCardActivity.this);
				}
			}
		});

		CheckBox cbDeleteCard = findViewById(R.id.cbDeleteCard);
		cbDeleteCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				try
				{
					setAsDefaultCard();
				}
				catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * <h2>deleteCards</h2>
	 * <p>
	 * method to make an api call to deleteCards
	 * </p>
	 */
	public void deleteCards(String cardId)
	{
		//creating request with parameter
		pDialog.show();

		JSONObject jsonObj = new JSONObject();
		try
		{
			jsonObj.put("userId", session.getSid());
			jsonObj.put("token_name",cardId);
			Utility.printLog("delete card Json Object is =" + jsonObj);

		} catch (JSONException e)
		{

			Utility.printLog("ERROR" + e);
			e.printStackTrace();
		}

		OkHttp3Connection.doOkHttp3Connection(session.getSession(),session.getLanguageId(), Constants.REMOVE_CARD,
				OkHttp3Connection.Request_type.DELETE, jsonObj, new OkHttp3Connection.OkHttp3RequestCallback()
				{
					@Override
					public void onSuccess(String jsonResponse)
					{
						Utility.printLog("The Response delete card " + jsonResponse);
						if (jsonResponse.contains("ï")) {
							jsonResponse = jsonResponse.replace("ï", "");
							jsonResponse = jsonResponse.replace("»", "");
							jsonResponse = jsonResponse.replace("¿", "");
						}
						deleteCardsResponseHandler(jsonResponse);
					}

					@Override
					public void onError(String error)
					{
						hideProgressBar();
						Toast.makeText(DeleteCardActivity.this,
								resources.getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
					}
				});
	}

	/**
	 * <h2>deleteCardsResponseHandler</h2>
	 * <p>
	 * method to handle and parse deleteCards() api response
	 * </p>
	 * @param Response: retrieved response from deleteCards() api
	 */
	public void deleteCardsResponseHandler(String Response)
	{
		try
		{
			Gson gson = new Gson();
			AddCardResponse response = gson.fromJson(Response, AddCardResponse.class);

			Utility.printLog("error no delete card" + response.getErrNum());

			if (response.getErrNum().equals("0") && response.getErrFlag().equals("0"))
			{
				Toast.makeText(DeleteCardActivity.this,resources.getString(R.string.card_removed), Toast.LENGTH_SHORT).show();
				Intent intent=new Intent();
				setResult(RESULT_OK,intent);
				finish();
			}
			else if(response.getErrFlag().equals("1") && response.getErrNum().equals("96"))
			{
				Toast.makeText(DeleteCardActivity.this, response.getErrMsg(), Toast.LENGTH_LONG).show();
				Utility.sessionExpire(DeleteCardActivity.this);
			}
			else if(response.getErrFlag().equals("1") && response.getErrNum().equals("94"))
			{
				Toast.makeText(DeleteCardActivity.this, response.getErrMsg(), Toast.LENGTH_LONG).show();
				Utility.sessionExpire(DeleteCardActivity.this);
			}
			else
			{
				Toast.makeText(this, response.getErrMsg(), Toast.LENGTH_LONG).show();
			}
			hideProgressBar();
		}
		catch (Exception e)
		{
			hideProgressBar();
			e.printStackTrace();
			Utility.printLog("" + e);
		}
	}

	/**
	 * <h2>setAsDefaultCard</h2>
	 * <p>
	 *     to make an api call to get default card
	 * </p>
	 * @throws JSONException: if any exceptions occur while creating json
	 */
	private void setAsDefaultCard() throws JSONException
	{
		pDialog.show();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("cardId", cardId);
		OkHttp3Connection.doOkHttp3Connection(session.getSession(),session.getLanguageId(), Constants.SERVICE_URL + "paymentGateway/card/default/me", OkHttp3Connection.Request_type.POST,
				jsonObject, new OkHttp3Connection.OkHttp3RequestCallback()
				{
					@Override
					public void onSuccess(String result)
					{
						hideProgressBar();
						Log.i("TAG","CARDRESPONCE "+result);
						//String text = "In Use - card used for payment";
					}

					@Override
					public void onError(String error)
					{
						hideProgressBar();
					}
				});

	}

	/**
	 * <h2>hideProgressBar</h2>
	 * <p>
	 *     custom method to hide progress bar
	 *     if its visible
	 * </p>
	 */
	private void hideProgressBar()
	{
		if (pDialog != null)
		{
			pDialog.cancel();
			pDialog.dismiss();
		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		finish();
		overridePendingTransition(R.anim.side_slide_out, R.anim.side_slide_in);
	}
}
