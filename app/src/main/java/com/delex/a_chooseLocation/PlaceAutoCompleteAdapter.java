package com.delex.a_chooseLocation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.delex.customer.R;

import com.delex.event.PlaceClickEvent;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.skt.Tmap.TMapPOIItem;

import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.delex.utility.Constants.OUT_JSON;
import static com.delex.utility.Constants.PLACES_API_BASE;
import static com.delex.utility.Constants.TYPE_AUTOCOMPLETE;

/**
 * <h1>PlaceAutoComplete_Adapter</h1>
 * 이 클래스는 주소를 볼 수있는 PlaceAutoComplete 화면을 제공하는 데 사용됩니다.
 *
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class PlaceAutoCompleteAdapter extends RecyclerView.Adapter<PlaceAutoCompleteAdapter.PlaceViewHolder> {

//    private ArrayList<TMapPOIItem> auto_complete_pojo_list;

//    public interface PlaceAutoCompleteInterface {
//        void onPlaceClick(ArrayList<TMapPOIItem> mResultList, int position);
//    }

    private static final String TAG = PlaceAutoCompleteAdapter.class.getSimpleName();
    private Context mContext;
    //    private PlaceAutoCompleteInterface mListener;
    private ArrayList<TMapPOIItem> mResultList;
    private SessionManager sessionManager;

    private boolean isSearching = false;

    /**
     * <h2>PlaceAutoCompleteAdapter</h2>
     * This method is constructor
     *
     * @param context context of the activity from which this is called
     */
    public PlaceAutoCompleteAdapter(Context context, ArrayList<TMapPOIItem> resultList) {

        mContext = context;
        this.mResultList = resultList;
//        this.mListener = (PlaceAutoCompleteInterface) mContext;
        sessionManager = new SessionManager(context);
    }

//    @Override
//    public Filter getFilter() {  //text 필터
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                android.util.Log.d(TAG, "performFiltering: performFiltering");
//                FilterResults results = new FilterResults();
//                // 조건이 주어지지 않으면 자동 완성 쿼리를 건너 뜁니다.
//                if (constraint != null) {
//                    // Query the autocomplete API for the (constraint) search string.
//                    Utility.printLog(TAG + " : " + constraint);
//
////                    if (!isSearching) {
////                        isSearching = true;
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    autocomplete(constraint.toString());
//
////                    if (mResultList != null) {
////                        // API가 성공적으로 결과를 반환했습니다.
////                        results.values = mResultList;
////                        results.count = mResultList.size();
////
////                        android.util.Log.d(TAG, "performFiltering: " + results.values + " , " + results.count);
//////                        }
////                    }
//                }
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                android.util.Log.d(TAG, "performFiltering: publishResults");
////                if (results != null && results.count > 0) {
//                // API가 하나 이상의 결과를 반환하고 데이터를 업데이트합니다.
////                    notifyDataSetChanged();
////                }
//            }
//        };
//    }

    /**
     * <h2>autocomplete</h2>
     * 검색한 키워드에 관련된 주소리스트들 mResultList에 set
     *
     * @param searchWord 자동 완성 필터
     * @return returns the list of addresses filtered
     */
//    private void autocomplete(String searchWord) {
//
//        ArrayList<TMapPOIItem> auto_complete_pojo_list = new ArrayList<>();
//
//        mTMapData.autoComplete(searchWord, new TMapData.AutoCompleteListenerCallback() {
//            @Override
//            public void onAutoComplete(ArrayList<String> arrayList) {
//
//            }
//        });
//
//        mTMapData.findAllPOI(searchWord, 5, new TMapData.FindAllPOIListenerCallback() {
//            @Override
//            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {
//
//                mResultList = null;
//                EventBus.getDefault().post(new SearchAddressEvent());
//
//                for (int i = 0; i < poiItem.size(); i++) {
//                    auto_complete_pojo_list.add(poiItem.get(i));
//
//                }
//
//                if (auto_complete_pojo_list.size() > 0) {
//                    mResultList = auto_complete_pojo_list;
//
//                    if (mResultList != null && mResultList.size() > 0) {
//                        android.util.Log.d(TAG, "onFindAllPOI: " + mResultList.size());
//                        EventBus.getDefault().post(new SearchAddressEvent());
//                    }
//                }
//            }
//        });
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(Event event) {
//        if (event instanceof SearchAddressEvent) {
//
//             API가 하나 이상의 결과를 반환하고 데이터를 업데이트합니다.
//
//            notifyDataSetChanged();
//
//        }


//    }

//    private ArrayList<PlaceAutoCompletePojo> autocomplete(String input) {
//        ArrayList resultList = null;
//        ArrayList<PlaceAutoCompletePojo> auto_complete_pojo_list = new ArrayList<PlaceAutoCompletePojo>();
//        PlaceAutoCompletePojo auto_complete_pojo;
//        HttpURLConnection conn = null;
//        StringBuilder jsonResults = new StringBuilder();
//        try {
//            URL url= getUrlForGoogleAutoComplete(input);
//            conn = (HttpURLConnection) url.openConnection();
//            InputStreamReader in = new InputStreamReader(conn.getInputStream());
//            // Load the results into a StringBuilder
//            int read;
//            char[] buff = new char[1024];
//            while ((read = in.read(buff)) != -1) {
//                jsonResults.append(buff, 0, read);
//            }
//            Utility.printLog(TAG+" ,address: "+jsonResults);
//        } catch (MalformedURLException e) {
//            Log.e(TAG, "Error processing Places API URL", e);
//            return resultList;
//        } catch (IOException e) {
//            Log.e(TAG, "Error connecting to Places API", e);
//            return resultList;
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//        try {
//            // Create a JSON object hierarchy from the results
//            JSONObject jsonObj = new JSONObject(jsonResults.toString());
//            //if status is ok then set all the address in adapter else rotate the keys
//            if(jsonObj.getString("status").equals("OK"))
//            {
//                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
//                Utility.printLog(TAG+" ,address: "+jsonObj+ " -----, "+predsJsonArray);
//                // Extract the Place descriptions from the results
//                resultList = new ArrayList(predsJsonArray.length());
//                for (int i = 0; i < predsJsonArray.length(); i++) {
//                    auto_complete_pojo = new PlaceAutoCompletePojo();
//                    Utility.printLog(TAG+" response:"+predsJsonArray.getJSONObject(i).getString("description"));
//                    System.out.println("============================================================");
//                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
//                    auto_complete_pojo.setAddress(predsJsonArray.getJSONObject(i).getString("description"));
//                    auto_complete_pojo.setRef_key(predsJsonArray.getJSONObject(i).getString("reference"));
//                    auto_complete_pojo_list.add(i, auto_complete_pojo);
//                }
//            }
//        } catch (JSONException e) {
//            Utility.printLog(TAG+" ,address exception : "+e);
//            e.printStackTrace();
//        }
//        return auto_complete_pojo_list;
//    }
    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(R.layout.item_drop_address, viewGroup, false);
        return new PlaceViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder mPredictionHolder, int i) {

        TMapPOIItem searchItem = mResultList.get(i);
        String name = searchItem.getPOIName();

        mPredictionHolder.tv_drop_address.setText(name);

        //to add the click listener for the layout and notify
        mPredictionHolder.rl_drop_address_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PlaceClickEvent(i));
//                mListener.onPlaceClick(mResultList, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        android.util.Log.d(TAG, "getItemCount: ");

        return mResultList != null ? mResultList.size() : 0;

    }

    /**
     * <h2>getUrlForGoogleAutoComplete</h2>
     * This method is used to create the link for google API
     *
     * @param input input for the address filter
     * @return returns the URL for the google API
     */
    private URL getUrlForGoogleAutoComplete(String input) {
        URL url = null;
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=").append(sessionManager.getGoogleServerKey());
            sb.append("&location=").append(sessionManager.getlatitude()).append(",").
                    append(sessionManager.getlongitude()).append("&radius=500&amplanguage=en");
            sb.append("&input=").append(URLEncoder.encode(input, "utf8"));
            Utility.printLog(TAG + " urL : " + sb.toString());
            url = new URL(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * <h2>PlaceViewHolder</h2>
     * This method is used for defining the android widgets
     */
    class PlaceViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_drop_fav_icon, iv_drop_clock_icon;
        RelativeLayout rl_drop_address_layout;
        TextView tv_drop_address;

        PlaceViewHolder(View itemView) {
            super(itemView);
            iv_drop_clock_icon = itemView.findViewById(R.id.iv_drop_clock_icon);
            iv_drop_clock_icon.setImageResource(R.drawable.home_location_icon);
            rl_drop_address_layout = itemView.findViewById(R.id.rl_drop_address_layout);
            tv_drop_address = itemView.findViewById(R.id.tv_drop_address);

            iv_drop_fav_icon = itemView.findViewById(R.id.iv_drop_fav_icon);
            iv_drop_fav_icon.setVisibility(View.GONE);
        }
    }
}
