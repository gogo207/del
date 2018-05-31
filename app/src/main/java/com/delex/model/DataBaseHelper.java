package com.delex.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.delex.utility.Utility;
import com.delex.logger.Log;
import com.delex.pojos.DataBaseGetItemDetailPojo;
import com.delex.pojos.DropAddressPojo;
import com.delex.pojos.FavDropAdrsData;
import java.util.ArrayList;

/**
 * <h1>DataBaseHelper</h1>
 * This class is used for defining database and creating columns
 * @version v1.0
 */
public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";            // Logcat tag
    private static final int DATABASE_VERSION = 1;                // Database Version
    private static final String DATABASE_NAME = "shyper_DB";    // Database Name

    /**
     * --------------------------------------For adding Full Drop Address without name and phone no.----------------------------------------
     */
    private static final String DROP_ADDRESS_TABLE = "Drop_Address_Table";         // New Add Drop Address Table, created on 17/03/17
    private static final String DROP_ADDRESS_KEY_ID = "Drop_Address_KeyId";        // Common column names
    private static final String DROP_ADDRESS_AREA = "Drop_Address_Area";
    private static final String DROP_ADDRESS_LAT = "Drop_Address_Latitude";
    private static final String DROP_ADDRESS_LONG = "Drop_Address_Longitude";

    private static final String CREATE_DROP_ADDRESS_TABLE = "CREATE TABLE " + DROP_ADDRESS_TABLE + "(" + DROP_ADDRESS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DROP_ADDRESS_AREA + " VARCHAR," + DROP_ADDRESS_LAT + " VARCHAR," + DROP_ADDRESS_LONG + " VARCHAR" + ")";

    /*****************************for my orders *******************************************/
    private  static final  String MYORDER_TABLE="myOrderTable";
    private static final String MYORDER_KEY_ID="myOrderId";
    private static final String MYORDER_BID="myOrderBId";
    private static final String MYORDER_SUBID="myOrderSubId";
    private static final String MYORDER_DRIVER_NAME="myOrderDriverName";
    private static final String MYORDER_DRIVER_MAIL="myOrderDriverMail";
    private static final String MYORDER_DRIVER_PHONE="myOrderDriverPhone";
    private static final String MYORDER_APP_DT="myOrderAppDt";
    private static final String MYORDER_STATUS="myOrderStatus";
    private static final String MYORDER_DROPLAT="myOrderDropLat";
    private static final String MYORDER_DROPLONG="myOrderDropLong";
    private static final String MYORDER_PICKUPLAT="myOrderPickUpLat";
    private static final String MYORDER_PICKUPLONG="myOrderPickUpLong";
    private static final String MYORDER_ADDRESS="myOrderAddress";
    private static final String MYORDER_NOTES="myOrderNotes";
    private static final String MYORDER_QNT="myOrdeQnt";
    private static final String MYORDER_WT="myOrderWt";

    private static final String CREATE_MYORDER_TABLE="CREATE TABLE " + MYORDER_TABLE + "(" + MYORDER_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MYORDER_BID + " VARCHAR," + MYORDER_SUBID + " VARCHAR," + MYORDER_DRIVER_NAME + " VARCHAR," + MYORDER_DRIVER_MAIL + " VARCHAR," + MYORDER_DRIVER_PHONE + " VARCHAR," + MYORDER_APP_DT+"  VARCHAR,"
            + MYORDER_STATUS +" VARCHAR," + MYORDER_PICKUPLAT +" VARCHAR,"+ MYORDER_PICKUPLONG +" VARCHAR,"+ MYORDER_DROPLAT +" VARCHAR,"+ MYORDER_DROPLONG +" VARCHAR,"
            + MYORDER_NOTES +" VARCHAR,"+ MYORDER_QNT +" VARCHAR,"+ MYORDER_WT +" VARCHAR,"  + MYORDER_ADDRESS + " VARCHAR" + ")";

    //======================= FAVOURITE DROP ADDRESS TABLE =========================================
    private static final String FAV_DROP_ADRS_TABLE = "Fav_Drop_Adrs_Table";         // New Add Drop Address Table, created on 17/03/17
    private static final String FAV_DROP_ADRS_KEY_ID = "Fav_Drop_Adrs_KeyId";        // Common column names
    private static final String FAV_DROP_ADRS_NAME = "Fav_Drop_Adrs_Name";
    private static final String FAV_DROP_ADRS_AREA = "Fav_Drop_Adrs_Area";
    private static final String FAV_DROP_ADRS_LAT = "Fav_Drop_Adrs_Lat";
    private static final String FAV_DROP_ADRS_LONG = "Fav_Drop_Adrs_Long";

    private static final String CREATE_FAV_DROP_ADRS_TABLE = "CREATE TABLE IF NOT EXISTS " + FAV_DROP_ADRS_TABLE + "(" + FAV_DROP_ADRS_KEY_ID + " VARCHAR," +
            FAV_DROP_ADRS_NAME+ " VARCHAR," + FAV_DROP_ADRS_AREA + " VARCHAR," + FAV_DROP_ADRS_LAT + " VARCHAR," + FAV_DROP_ADRS_LONG + " VARCHAR" + ")";

    /**
     * <h2>DataBaseHelper</h2>
     * This is constructor of this class
     * @param mContext  context from which this class is called
     */
    public DataBaseHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Constructor to initialize DatabaseHandler
     * @param context  context from which this class is called
     * @return returns the object of this class
     */
    public static DataBaseHelper getInstance(Context context){
        return new DataBaseHelper(context);
    }

    // creating required tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Utility.printLog(TAG+"onCreate database ");
        db.execSQL(CREATE_DROP_ADDRESS_TABLE);
        db.execSQL(CREATE_MYORDER_TABLE);
        db.execSQL(CREATE_FAV_DROP_ADRS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DROP_ADDRESS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MYORDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAV_DROP_ADRS_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
        onUpgrade(db, 1, 2);
    }

    /**
     * this method is to insert all current orders in my order table
     * @param appdt appnt date to be added
     * @param status status of the booking
     * @param drivername driver name
     * @param driveremail driver email
     * @param driverphone driver phone number
     * @param addr address
     * @param bid booking id
     * @param subid sub id
     * @param wt weight
     * @param qt quantitiy
     * @param notes special notes
     * @param dropLat drop address latitude
     * @param droplong drop address longitude
     * @param pickuplat pickup address latitude
     * @param pickuplong pick up address longitude
     * @return item_country_picker id
     */
    public long insertMyOrder(String appdt, String status, String drivername,
                              String driveremail, String driverphone, String addr,
                              String bid, String subid, String wt,
                              String qt, String notes, String dropLat,
                              String droplong, String pickuplat, String pickuplong){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MYORDER_APP_DT,appdt);
        contentValues.put(MYORDER_STATUS,status);
        contentValues.put(MYORDER_DRIVER_NAME, drivername);
        contentValues.put(MYORDER_DRIVER_MAIL, driveremail);
        contentValues.put(MYORDER_DRIVER_PHONE, driverphone);
        contentValues.put(MYORDER_ADDRESS, addr);
        contentValues.put(MYORDER_BID, bid);
        contentValues.put(MYORDER_SUBID, subid);
        contentValues.put(MYORDER_WT, wt);
        contentValues.put(MYORDER_QNT, qt);
        contentValues.put(MYORDER_NOTES, notes);
        contentValues.put(MYORDER_DROPLAT, dropLat);
        contentValues.put(MYORDER_DROPLONG, droplong);
        contentValues.put(MYORDER_PICKUPLAT, pickuplat);
        contentValues.put(MYORDER_PICKUPLONG, pickuplong);

        long id = db.insert(MYORDER_TABLE, null, contentValues);
        db.close();
        return id;
    }

    /**
     * This method is used to extract the order detail by passing bid and subid
     * @author 3embed
     * @param bId booking id
     * @param subid sub booking id
     * @return order item_country_picker which match bid and subid
     */
    public DataBaseGetItemDetailPojo extractFrMyOrderDetail(String bId, String subid) {
        Utility.printLog("DBHelper,for excertion of id: " + bId);
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + MYORDER_TABLE + " WHERE " + MYORDER_BID + " = " + bId +" AND " + MYORDER_SUBID + " = " + subid;
        DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo = null;
        // 2. build query
        Cursor mCursor = db.rawQuery(selectQuery, null);
        // 3. if we got results get the first one
        if (mCursor != null && mCursor.moveToFirst() ) {
            // 4. build adrs object
            dataBase_getItem_detail_pojo = new DataBaseGetItemDetailPojo();

            dataBase_getItem_detail_pojo.setOrderId(mCursor.getInt(mCursor.getColumnIndex(String.valueOf(MYORDER_KEY_ID))));
            dataBase_getItem_detail_pojo.setStatus(mCursor.getString(mCursor.getColumnIndex(MYORDER_STATUS)));
            dataBase_getItem_detail_pojo.setAddress(mCursor.getString(mCursor.getColumnIndex(MYORDER_ADDRESS)));
            dataBase_getItem_detail_pojo.setAppdt(mCursor.getString(mCursor.getColumnIndex(MYORDER_APP_DT)));
            dataBase_getItem_detail_pojo.setBid(mCursor.getString(mCursor.getColumnIndex(MYORDER_BID)));
            dataBase_getItem_detail_pojo.setDriveremail(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_MAIL)));
            dataBase_getItem_detail_pojo.setDrivername(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_NAME)));
            dataBase_getItem_detail_pojo.setDriverphone(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_PHONE)));
            dataBase_getItem_detail_pojo.setDroplat(mCursor.getString(mCursor.getColumnIndex(MYORDER_DROPLAT)));
            dataBase_getItem_detail_pojo.setDroplong(mCursor.getString(mCursor.getColumnIndex(MYORDER_DROPLONG)));
            dataBase_getItem_detail_pojo.setPickLong(mCursor.getString(mCursor.getColumnIndex(MYORDER_PICKUPLONG)));
            dataBase_getItem_detail_pojo.setPickLt(mCursor.getString(mCursor.getColumnIndex(MYORDER_PICKUPLAT)));
            dataBase_getItem_detail_pojo.setSubid(mCursor.getString(mCursor.getColumnIndex(MYORDER_SUBID)));
            dataBase_getItem_detail_pojo.setQnt(mCursor.getString(mCursor.getColumnIndex(MYORDER_QNT)));
            dataBase_getItem_detail_pojo.setWt(mCursor.getString(mCursor.getColumnIndex(MYORDER_WT)));
            dataBase_getItem_detail_pojo.setNotes(mCursor.getString(mCursor.getColumnIndex(MYORDER_NOTES)));
            mCursor.close();
        }
        return dataBase_getItem_detail_pojo;
    }

    /**
     * This is used extract all orders by passing bid
     * @author 3embed
     * @param bId booking ID
     * @return arrylist of all orders which match bid
     */
    public ArrayList<DataBaseGetItemDetailPojo> extractAllOrders(String bId) {

        Utility.printLog("Database extractallbybusiid(): ");
        ArrayList<DataBaseGetItemDetailPojo> orders = new ArrayList<DataBaseGetItemDetailPojo>();

        // 1. build the query
        String selectQuery = "SELECT  * FROM " + MYORDER_TABLE + " WHERE " + MYORDER_BID + " = " + bId;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(selectQuery, null);

        Utility.printLog("Database extractallbybusiid(): mCursor.getCount() "+mCursor.getCount() );
        // looping through all rows, building AdrsDDPojo and adding to list
        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst()) {
            do {
                DataBaseGetItemDetailPojo dataBase_getItem_detail_pojo = new DataBaseGetItemDetailPojo();

                dataBase_getItem_detail_pojo.setOrderId(mCursor.getInt(mCursor.getColumnIndex(String.valueOf(MYORDER_KEY_ID))));
                dataBase_getItem_detail_pojo.setStatus(mCursor.getString(mCursor.getColumnIndex(MYORDER_STATUS)));
                dataBase_getItem_detail_pojo.setAddress(mCursor.getString(mCursor.getColumnIndex(MYORDER_ADDRESS)));
                dataBase_getItem_detail_pojo.setAppdt(mCursor.getString(mCursor.getColumnIndex(MYORDER_APP_DT)));
                dataBase_getItem_detail_pojo.setBid(mCursor.getString(mCursor.getColumnIndex(MYORDER_BID)));
                dataBase_getItem_detail_pojo.setDriveremail(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_MAIL)));
                dataBase_getItem_detail_pojo.setDrivername(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_NAME)));
                dataBase_getItem_detail_pojo.setDriverphone(mCursor.getString(mCursor.getColumnIndex(MYORDER_DRIVER_PHONE)));
                dataBase_getItem_detail_pojo.setDroplat(mCursor.getString(mCursor.getColumnIndex(MYORDER_DROPLAT)));
                dataBase_getItem_detail_pojo.setDroplong(mCursor.getString(mCursor.getColumnIndex(MYORDER_DROPLONG)));
                dataBase_getItem_detail_pojo.setPickLong(mCursor.getString(mCursor.getColumnIndex(MYORDER_PICKUPLONG)));
                dataBase_getItem_detail_pojo.setPickLt(mCursor.getString(mCursor.getColumnIndex(MYORDER_PICKUPLAT)));
                dataBase_getItem_detail_pojo.setSubid(mCursor.getString(mCursor.getColumnIndex(MYORDER_SUBID)));
                dataBase_getItem_detail_pojo.setQnt(mCursor.getString(mCursor.getColumnIndex(MYORDER_QNT)));
                dataBase_getItem_detail_pojo.setWt(mCursor.getString(mCursor.getColumnIndex(MYORDER_WT)));
                dataBase_getItem_detail_pojo.setNotes(mCursor.getString(mCursor.getColumnIndex(MYORDER_NOTES)));


                // adding to dataBase_getItem_detail_pojo list getBusinessDistance()
                orders.add(dataBase_getItem_detail_pojo);
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        Utility.printLog("DB extractAllFrmAdrsTable orders.size(): " + orders.size());

        db.close();
        return orders;
    }

    /**
     * this method is using for update the status of order by passing satus ,bid and subid
     * @author 3embed
     * @param status booking status
     * @param bid booking ID
     * @param subid sub booking ID
     * @return returnss updated item_country_picker
     */
    public int updateOrderStatus(String status,String bid,String subid){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(MYORDER_STATUS, status);
        Utility.printLog("updated ROW NUMBER IS in updateOrderStatus" + bid);
        return db.update(MYORDER_TABLE, contentValues, MYORDER_BID + " = ? AND " + MYORDER_SUBID + " = ?", new String[]{String.valueOf(bid),String.valueOf(subid)});    //selection args
    }

    /**
     * this method is used to delete all orders
     */
    public void deleteAllOrders(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(MYORDER_TABLE, "", null);
    }

    /**
     * Only store the address name and lat-longs thats it.
     * @param area drop address area
     * @param lat drop address latitude
     * @param log drop address longitude
     * @return item_country_picker id
     */
    public long insertDropAddressData(String area, String lat, String log) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DROP_ADDRESS_AREA, area);
        contentValues.put(DROP_ADDRESS_LAT, lat);
        contentValues.put(DROP_ADDRESS_LONG, log);
        long id = db.insert(DROP_ADDRESS_TABLE, null, contentValues);
        Utility.printLog("INSERTED ROW NUMBER IS :InsertDropData: " + id + " area: "+area+" ,lat: "+lat+" ,long: "+log);
        return id;
    }


    /**
     * extract all recipient address
     * @return list of address
     */
    public ArrayList<DropAddressPojo> extractAllDropAddressData() {
        ArrayList<DropAddressPojo> dropAddressPojo = new ArrayList<DropAddressPojo>();

        // 1. build the query
        String selectQuery = "SELECT  * FROM " + DROP_ADDRESS_TABLE;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCursor = db.rawQuery(selectQuery, null);

        // looping through all rows, building AdrsDDPojo and adding to list
        if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst()) {
            do {
                DropAddressPojo drop_address = new DropAddressPojo();
                drop_address.setAddressId(mCursor.getInt(mCursor.getColumnIndex(DROP_ADDRESS_KEY_ID)));
                drop_address.setAddress(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_AREA)));
                drop_address.setLat(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LAT)));
                drop_address.setLng(mCursor.getString(mCursor.getColumnIndex(DROP_ADDRESS_LONG)));

                Utility.printLog("DB extractFrmAdrsTable: latitude: " + drop_address.getLat() + " ,logitude: " + drop_address.getLng()+" ,address: " + drop_address.getAddress()+" ,addressid: " + drop_address.getAddressId());
                dropAddressPojo.add(drop_address);
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        Utility.printLog("DB extractAllFrmAdrsTable rcntlyVisitedAL.size(): " + dropAddressPojo.size());
        return dropAddressPojo;
    }

    /**
     * Only store newly added fav address.
     * favDropAdrsData:
     * @return item_country_picker id
     */
    public long insertFavDropAdrsData(FavDropAdrsData favDropAdrsData)
    {
        Log.d(TAG, "insertFavDropAdrssData favDropAdrsData: "+favDropAdrsData.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FAV_DROP_ADRS_KEY_ID, favDropAdrsData.get_id());
        contentValues.put(FAV_DROP_ADRS_NAME, favDropAdrsData.getName());
        contentValues.put(FAV_DROP_ADRS_AREA, favDropAdrsData.getAddress());
        contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favDropAdrsData.getLat()));

        contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favDropAdrsData.getLng()));
        long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
        Log.d(TAG, "insertFavDropAdrssData id: "+id);
        return id;
    }
    /****************************************************************************************************/

    public Integer deleteFavDropAdrs(String id)
    {
        Log.d(TAG, "deleteFavDropAdrs id: "+id);

      /*  SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FAV_DROP_ADRS_TABLE, "id = ? ", new String[] { String.valueOf(id) });*/

        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(FAV_DROP_ADRS_TABLE,FAV_DROP_ADRS_KEY_ID + "=? ",new String[]{id});
    }

    /**
     * @author 3embed
     * to delete item_booking_history item_country_picker from the address table
     */
    public void resetFavDropAdrsTable(ArrayList<FavDropAdrsData> favDropAdrsDataAL)
    {
        Log.d(TAG, "resetFavDropAdrsTable size: "+favDropAdrsDataAL.size());
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + FAV_DROP_ADRS_TABLE);
        db.execSQL(CREATE_FAV_DROP_ADRS_TABLE);
        insertAllFavDropAdrs(favDropAdrsDataAL);
    }

    /**
     * <h2>insertAllFavDropAdrs</h2>
     * This method is used for storing all fav address
     * @param favDropAdrsDataAL list of fav address
     */
    private void insertAllFavDropAdrs(ArrayList<FavDropAdrsData> favDropAdrsDataAL)
    {
        Log.d(TAG, "insertAllFavDropAdrs size: "+favDropAdrsDataAL.size());
        SQLiteDatabase db = this.getWritableDatabase();

        for(FavDropAdrsData favDropAdrsData: favDropAdrsDataAL)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FAV_DROP_ADRS_KEY_ID, favDropAdrsData.get_id());
            contentValues.put(FAV_DROP_ADRS_NAME, favDropAdrsData.getName());
            contentValues.put(FAV_DROP_ADRS_AREA, favDropAdrsData.getAddress());
            contentValues.put(FAV_DROP_ADRS_LAT, String.valueOf(favDropAdrsData.getLat()));
            contentValues.put(FAV_DROP_ADRS_LONG, String.valueOf(favDropAdrsData.getLng()));

            long id = db.insert(FAV_DROP_ADRS_TABLE, null, contentValues);
            Log.d(TAG, "insertFavDropAdrssData id: " + id);
        }
    }

    /**
     * extract all recipient address
     * @return list of address
     */
    public ArrayList<FavDropAdrsData> extractAllFavDropAdrs()
    {
        Log.d(TAG, "extractAllFavDropAdrs called() ");
        ArrayList<FavDropAdrsData> favDropAdrsDataAl = new ArrayList<FavDropAdrsData>();
        String selectQuery = "SELECT  * FROM " + FAV_DROP_ADRS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        try {


            Cursor mCursor = db.rawQuery(selectQuery, null);
            if ((mCursor != null && mCursor.getCount() > 0) && mCursor.moveToFirst()) {
                do {
                    FavDropAdrsData favDropAdrsData = new FavDropAdrsData();
                    favDropAdrsData.set_id(mCursor.getString(mCursor.getColumnIndex(FAV_DROP_ADRS_KEY_ID)));
                    favDropAdrsData.setName(mCursor.getString(mCursor.getColumnIndex(FAV_DROP_ADRS_NAME)));
                    favDropAdrsData.setAddress(mCursor.getString(mCursor.getColumnIndex(FAV_DROP_ADRS_AREA)));
                    favDropAdrsData.setLat(mCursor.getDouble(mCursor.getColumnIndex(FAV_DROP_ADRS_LAT)));
                    favDropAdrsData.setLng(mCursor.getDouble(mCursor.getColumnIndex(FAV_DROP_ADRS_LONG)));
                    favDropAdrsDataAl.add(favDropAdrsData);
                }
                while (mCursor.moveToNext());
                mCursor.close();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        Log.d(TAG, "extractAllFavDropAdrs size(): " + favDropAdrsDataAl.size());
        //db.close();
        return favDropAdrsDataAl;
    }

    /**
     *<h2>clearDb</h2>
     * This method is used to clear the database
     */
    public void clearDb()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DROP_ADDRESS_TABLE,null,null);
        db.delete(MYORDER_TABLE,null,null);
        Utility.printLog("DataBase Is CLeared" );
    }
}
