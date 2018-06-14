package com.delex.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.delex.a_main.MainActivity;
import com.delex.parent.ParentFragment;
import com.delex.adapter.SupportExpandableRVA;
import com.delex.interfaceMgr.ResponseListener;
import com.delex.pojos.SupportDataPojo;
import com.delex.pojos.SupportPojo;
import com.delex.utility.Alerts;
import com.delex.utility.AppTypeface;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.delex.model.SupportModel;

import java.util.ArrayList;

/**
 * <h1>Support Screen</h1>
 * This class is used to provide the Support screen, where we can see the support information.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class SupportFragment extends ParentFragment
        implements AdapterView.OnItemClickListener, ResponseListener
{
    private ArrayList<SupportDataPojo> supportDataAl;
    private ProgressDialog progressDialog;
    private SessionManager sessionManager;
    private Alerts alerts;
    private AppTypeface appTypeface;
    //private SupportAdapter supportTypesAdapter;
    private SupportExpandableRVA supportTypesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        appTypeface = AppTypeface.getInstance(getActivity());
        sessionManager = new SessionManager(getActivity());
        alerts = new Alerts();
        progressDialog = Utility.GetProcessDialog(getActivity());
    }



    /**
     * <p>inflating the view</p>
     * @param inflater Layout Inflater which inflate the layout
     * @param container contains the viewGroup
     * @param savedInstanceState instance of the activity
     * @return the instance of View.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_support,container,false);
        initToolBar(view);
        initialize(view);
        progressDialog.show();
        return view;
    }

    /**
     *<h2>initToolBar</h2>
     * <p>
     *     method to initialize the toolbar for this fragment
     * </p>
     * @param view: fragment root view
     */
    private void initToolBar(View view)
    {
        ImageView ivMenuBtnToolBar= view.findViewById(R.id.ivMenuBtnToolBar);
        ivMenuBtnToolBar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DrawerLayout mDrawerLayout = getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity) getActivity()).moveDrawer(mDrawerLayout);
            }
        });

        TextView tvTitleToolbar = view.findViewById(R.id.tvTitleToolbar);
        tvTitleToolbar.setText(getActivity().getString(R.string.support));
        tvTitleToolbar.setTypeface(appTypeface.getPro_narMedium());
    }

    /**
     * <h2>initialize</h2>
     * <p>Initializing view elements</p>
     * @param view: fragment root view
     */
    private void initialize(View view)
    {
        RecyclerView recyclerView =  view.findViewById(R.id.rvSupportTypes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        supportDataAl = new ArrayList<SupportDataPojo>();
        supportTypesAdapter = new SupportExpandableRVA(getActivity(), supportDataAl);
        //supportTypesAdapter = new SupportAdapter(getActivity(), supportDataAl);
        //recyclerView.setOnItemClickListener(this);
        recyclerView.setAdapter(supportTypesAdapter);

        if(Utility.isNetworkAvailable(getActivity()))
        {
            SupportModel suporModel = new SupportModel();
            suporModel.callSupportService(sessionManager, this);
        }
        else
            alerts.showNetworkAlert(getActivity());
    }

    /**
     * <h2>onItemClick</h2>
     * <p>
     * This is an Overrided method got call when a item_country_picker is clicked
     * </p>
     * @param parent parent instance
     * @param view view instance
     * @param position position of item_country_picker
     * @param id id.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(!supportDataAl.get(position).getLink().equals("") && supportDataAl.get(position).getLink()!=null)
        {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("Link", supportDataAl.get(position).getLink());
            intent.putExtra("Title", supportDataAl.get(position).getName());
            startActivity(intent);
        }
    }

    @Override
    public void onError(String errormsg)
    {
        Toast.makeText(getActivity(),errormsg,Toast.LENGTH_SHORT).show();
        progressDialog.cancel();
    }

    @Override
    public void onSupportSuccess(SupportPojo support_pojo)
    {
        progressDialog.cancel();
        //supportDataAl = new SupportDataPojo[support_pojo.getData().length];
        supportDataAl.clear();
        supportDataAl.addAll(support_pojo.getData());
        supportTypesAdapter.notifyDataSetChanged();
    }

}
