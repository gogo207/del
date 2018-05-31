package com.delex.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.ParentFragment;
import com.delex.adapter.CardsListAdapter;
import com.delex.interfaceMgr.NetworkCheck;
import com.delex.interfaceMgr.OnClickOfDeleteButton;
import com.delex.interfaceMgr.PaymentFragmentResponce;
import com.delex.model.PaymentFragmentModel;
import com.delex.pojos.CardInfoPojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Payment Screen</h1>
 * This class is used to provide the Payment screen, where we can add our card details.
 * @author 3embed
 * @since 3 Jan 2017.
 * @see PaymentFragmentModel interface
 */
public class PaymentFragment extends ParentFragment implements AdapterView.OnItemClickListener,
        PaymentFragmentResponce,NetworkCheck,OnClickOfDeleteButton
{
    private ListView lv_payment_cards;
    private List<CardInfoPojo> rowItems;
    private ProgressDialog pDialog;
    private Context mcontext;
    private PaymentFragmentModel paymentFragmentModel;
    private CardsListAdapter adapter;
    private AppTypeface appTypeface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mcontext = getActivity();
        pDialog = new ProgressDialog(mcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        appTypeface = AppTypeface.getInstance(mcontext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

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
        final DrawerLayout mDrawerLayout =  getActivity().findViewById(R.id.drawer_layout);
        ImageView ivMenuBtnToolBar =  view.findViewById(R.id.ivMenuBtnToolBar);
        ivMenuBtnToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) mcontext).moveDrawer(mDrawerLayout);
            }
        });

        TextView tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setTypeface(appTypeface.getPro_narMedium());
        tvTitleToolbar.setText(getString(R.string.payments));
    }

    /**
     * <h2>initViews</h2>
     * <p>Initializing view elements</p>
     * @param view View instance.
     */
    private void initViews(View view)
    {
        paymentFragmentModel = new PaymentFragmentModel(mcontext);

        TextView tv_payment_add_card = view.findViewById(R.id.tv_payment_add_card);
        tv_payment_add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, AddCardActivity.class);
                intent.putExtra("coming_From","payment_Fragment");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
            }
        });

        lv_payment_cards = view.findViewById(R.id.lv_payment_cards);
        lv_payment_cards.setOnItemClickListener(this);
        rowItems = new ArrayList<CardInfoPojo>();
        adapter = new CardsListAdapter(mcontext, R.layout.item_card_list, rowItems,this);
        lv_payment_cards.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        paymentFragmentModel.NetworkISAvailble(this);
        Constants.isPaymentFragActive=true;
    }

    /**
     * This is an Overrided method got call when a item_country_picker is clicked
     * @param parent parent instance
     * @param view view instance
     * @param position position of item_country_picker
     * @param id id.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        deleteCardMethod(position);
    }

    /**
     * <h2>deleteCardMethod</h2>
     * This method is used to delete the card selected
     * @param position position of the card to be deleted
     */
    public void deleteCardMethod(int position)
    {
        CardInfoPojo row_details = (CardInfoPojo) lv_payment_cards.getItemAtPosition(position);
        Intent intent = new Intent(mcontext, DeleteCardActivity.class);
        intent.putExtras(paymentFragmentModel.createBundle(row_details));
        startActivityForResult(intent, 2);
    }

    @Override
    public void ResponseItem(CardInfoPojo item)
    {
        rowItems.add(item);
    }

    @Override
    public void errorResponse(String response)
    {
        pDialog.dismiss();

        Toast.makeText(mcontext, "No cards present", Toast.LENGTH_LONG).show();
    }

    @Override
    public void notifyAdapter()
    {
        pDialog.dismiss();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearRowItem()
    {
        rowItems.clear();
    }
    @Override
    public void isNetworkAvailble(boolean isavailble)
    {
        if(isavailble)
        {
            pDialog.show();
            paymentFragmentModel.cllGetAllCards(this);
        }
        else{

            Alerts alerts=new Alerts();
            alerts.showNetworkAlert(mcontext);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Constants.isPaymentFragActive=false;
    }

    @Override
    public void onClickOfDelete(int position) {
        deleteCardMethod(position);
    }
}
