package com.delex.bookingFlow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.parent.ParentActivity;
import com.delex.adapter.CancelBookingAdapter;
import com.delex.controllers.CancelBookingController;

import com.delex.pojos.CancelReasonPojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;
import com.delex.utility.LocaleHelper;
import com.delex.utility.OkHttp3Connection;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.customer.R;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * <h1>CancelBookingActivity Activity</h1>
 * This class is used to Cancel the job.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class CancelBookingActivity extends ParentActivity implements View.OnClickListener{

    private static final String TAG = "CancelBookingActivity";
    RelativeLayout back_Layout;
    Button btn_cancel_booking;
    String cancel_reason = "";
    private String  bid;
    int status = 3;
    private CancelBookingController controller;
    private RecyclerView rv_cancel_booking_reasons;
    private String amount = "";
    private AppTypeface appTypeface;
    private EditText etComment;
    private TextView tvCommentTitle;
    private CancelReasonPojo cancelReasonPojo;
    private String price="",text="";
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    /**
     * This is the onCreateHomeFrag method that is called firstly, when user came to login screen.
     * @param savedInstanceState contains an instance of Bundle.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager= new SessionManager(CancelBookingActivity.this);
        setContentView(R.layout.activity_booking_cancel);
        overridePendingTransition(R.anim.slide_in_up, R.anim.stay_still);
        appTypeface= AppTypeface.getInstance(this);
        SessionManager sessionManager = new SessionManager(CancelBookingActivity.this);
        controller = new CancelBookingController(this, sessionManager);
        progressDialog= new ProgressDialog(CancelBookingActivity.this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            bid = bundle.getString("ent_bid");
        }
        cancelReasons(bid);
        //This is used for getting the Cancellation reason.


        /*intialise the view*/
        initView();
    }


    private void cancelReasons(String bid)
    {
        if(Utility.isNetworkAvailable(CancelBookingActivity.this)) {

            String url = Constants.CANCEL_REASON+"/"+Constants.USER_TYPE+"/0/"+bid;
            Utility.printLog("value of reason:urL: "+url);
            OkHttp3Connection.doOkHttp3Connection(sessionManager.getSession(),sessionManager.getLanguageId(), url, OkHttp3Connection.Request_type.GET, new JSONObject(), new OkHttp3Connection.OkHttp3RequestCallback() {
                        @Override
                        public void onSuccess(String result) {
                            progressDialog.dismiss();
                            Utility.printLog("value of reason: "+result);
                            Gson gson = new Gson();
                            CancelReasonPojo responsePojo = gson.fromJson(result, CancelReasonPojo.class);
                            switch (responsePojo.getErrFlag())
                            {
                                case 0:
                                    price=String.valueOf(responsePojo.getData().getCancellationFee());
                                    Log.d("onClick123556: ",price);
                                    text=responsePojo.getData().getCancellationText();
                                    CancelBookingAdapter cancelBookingAdapter = new CancelBookingAdapter(CancelBookingActivity.this, responsePojo.getData(),String.valueOf(responsePojo.getData().getCancellationFee()));
                                    rv_cancel_booking_reasons.setAdapter(cancelBookingAdapter);
                                    break;

                                default:
                                    Toast.makeText(CancelBookingActivity.this, responsePojo.getErrMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            progressDialog.dismiss();
                        }
                    }

            );
        }
        else{
            Alerts alerts=new Alerts();
            alerts.showNetworkAlert(CancelBookingActivity.this);
        }
    }


    /**
     * <h2>initView</h2>
     * <p>Initializing view elements</p>
     */
    private void initView()
    {
        back_Layout = findViewById(R.id.rlBackArrow);
        rv_cancel_booking_reasons = findViewById(R.id.rv_cancel_booking_reasons);
        btn_cancel_booking = findViewById(R.id.btn_cancel_booking);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        rv_cancel_booking_reasons.setLayoutManager(linearLayoutManager);

        tvCommentTitle=(TextView)findViewById(R.id.et_comment);
        tvCommentTitle.setTypeface(appTypeface.getPro_narMedium());

        etComment=(EditText)findViewById(R.id.et_comment);
        etComment.setTypeface(appTypeface.getPro_News());

        TextView signup_title = findViewById(R.id.tvToolBarTitle);
        signup_title.setText(getString(R.string.cancel_reason));
        signup_title.setTypeface(appTypeface.getSans_semiBold());

        ProgressDialog progressDialog = new ProgressDialog(CancelBookingActivity.this);
        progressDialog.setMessage(getString(R.string.canceling));
    }

    /**
     * This method is keep on calling each time
     */
    @Override
    protected void onResume() {
        super.onResume();
        back_Layout.setOnClickListener(this);
        btn_cancel_booking.setOnClickListener(this);
    }

    /**
     * This method is providing the onClick listener.
     * @param view contains the actual view.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlBackArrow:
                onBackPressed();
                break;
            case R.id.btn_cancel_booking:
                Utility.printLog(TAG+" cancel_reason "+cancel_reason+" amount "+price);
                if (!cancel_reason.equals("") && !price.equals(""))


                    if(!etComment.getText().toString().equalsIgnoreCase(""))
                    cancel_reason=etComment.getText().toString();

                Log.d( "onClick12355: ",price);
                    controller.showAlert(bid, cancel_reason, status,price);


                break;
        }
    }

    /**
     * This method will get the reason and amount from its adapter class.
     * @param reason cancel reason.
     * @param amount amount to be charged
     */
    public void cancelReason(String reason, String amount)
    {
        cancel_reason = reason;
        this.amount = amount;
    }

    /**onBack pressed close the activity.*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_down_acvtivity);
    }
}
