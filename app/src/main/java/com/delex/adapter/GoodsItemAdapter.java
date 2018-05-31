package com.delex.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.delex.utility.AppTypeface;
import com.delex.utility.SessionManager;
import com.delex.customer.R;
import com.delex.pojos.GoodsDataPojo;
import java.util.ArrayList;

/**
 * <h1>GoodsItemAdapter</h1>
 * @author 3Embed
 * @since 20/5/17
 */
public class GoodsItemAdapter extends ArrayAdapter<GoodsDataPojo> {
    private Context context;
    private ArrayList<GoodsDataPojo> goodsDataList=new ArrayList<GoodsDataPojo>();
    private SessionManager sessionManager;
    private  TextView tv_goods_type_name;
    private RelativeLayout rlbody;
    private int k=0;
    public ImageView iv_goods_type_check;

    /**
     * <h2>GoodsItemAdapter</h2>
     * This method is constructor for this adapter
     * @param context context of the activity from where it is called
     * @param goodsDataList list of goods
     */
    public GoodsItemAdapter(Context context,ArrayList<GoodsDataPojo> goodsDataList){
        super(context, R.layout.item_goods_type,goodsDataList);
        this.context=context;
        this.goodsDataList=goodsDataList;
        sessionManager = new SessionManager(context);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.item_goods_type,parent,false);
        iv_goods_type_check = view.findViewById(R.id.iv_goods_type_check);
        tv_goods_type_name = view.findViewById(R.id.tv_goods_type_name);

        if (goodsDataList.get(position).get_id().equals(sessionManager.getLastGoodsID()))
        {
            tv_goods_type_name.setTextColor(context.getResources().getColor(R.color.grayTextColor));
            iv_goods_type_check.setVisibility(View.GONE);
        }
        else
        {
            tv_goods_type_name.setTextColor(context.getResources().getColor(R.color.grayTextColor));
            iv_goods_type_check.setVisibility(View.GONE);
        }


        tv_goods_type_name.setText(goodsDataList.get(position).getName());
        //to set the fonts
        setFonts();
        //to make the tick mark for selected item

        return view;
    }

    /**
     * <h2>setFonts</h2>
     * This method is used to set the fonts
     */
    private void setFonts()
    {
        AppTypeface appTypeface=AppTypeface.getInstance(context);
        Typeface clanproNarrowNews=appTypeface.getPro_News();
        tv_goods_type_name.setTypeface(clanproNarrowNews);
    }


}
