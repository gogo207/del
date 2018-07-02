package com.delex.utility;

import com.delex.customer.BuildConfig;

/**
 * <h1>Constants</h1>
 * <p>
 * Class to initialize all the required global variables constants
 * </p>
 *
 * @since 23/5/15.
 */
public class Constants {
    public static final int CAMERA_PIC1 = 666;
    public static final int GALLERY_PIC1 = 667;
    public static final int CROP_IMAGE1 = 668;

    public static final int CAMERA_PIC2 = 766;
    public static final int GALLERY_PIC2 = 767;
    public static final int CROP_IMAGE2 = 768;


    public static final int CAMERA_PIC3 = 866;
    public static final int GALLERY_PIC3 = 867;
    public static final int CROP_IMAGE3 = 868;


    public static final int CAMERA_PIC4 = 466;
    public static final int GALLERY_PIC4 = 467;
    public static final int CROP_IMAGE4 = 468;

    public static int REQUEST_CODE = 1;
    public static final String SERVICE_URL = "https://api2.delex.co.kr/";
    public static final String FACEBOOK_LINK = "https://www.facebook.com/Truckr-Uber-For-Trucks-2031946467026704/";
    //public static final String SERVICE_URL = "https://api.dayrunnerapp.com/";

    public static final String ACCESS_KEY_ID = "AKIAI73AW5GLQI7UUPVQ";
    public static final String SECRET_KEY = "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";

    static final String PICTURE_BUCKET = "truckrcust";
    public static final String AMAZON_PROFILE_FOLDER = "Customers/ProfilePicture/";
    public static final String AMAZON_SHIPMENT_FOLDER = "Customers/ShipmentImage/";
    public static final String AMAZON_PROFILE_PATH = "https://s3-us-east-1.amazonaws.com/uberfortruck/Customers/ProfilePicture/";
    public static final String AMAZON_SHIPMENT_PATH = "https://s3-us-east-1.amazonaws.com/uberfortruck/Customers/ShipmentImage/";
    public static String profile_picture_path = "";


    public static final String TERMS_LINK = "https://superadmin.uberfortruck.com/supportText/customer/en_termsAndConditions.php";
    public static final String PRIVECY_LINK = "https://superadmin.uberfortruck.com/supportText/customer/en_privacyPolicy.php";
    //public static final String TERMS_LINK="https://admin.dayrunnerapp.com/termsAndCondions.php";
    //public static final String PRIVECY_LINK="https://admin.dayrunnerapp.com/privacyPolicy.php";


    public static final String LOGOUTURL = SERVICE_URL + "app/LogOut";
    public static final String SIGNUPURL = SERVICE_URL + "slave/signup";
    public static final String SIGNIN_URL = SERVICE_URL + "slave/signin";
    public static final String LANGUAGE = SERVICE_URL + "app/lang"; //"slaveLogin";
    public static final String EMAILVALIDATION = SERVICE_URL + "slave/EmailPhoneValidate";
    public static final String PHONENOVALIDATION = SERVICE_URL + "slave/EmailPhoneValidate";
    public static final String GETVERIFICATIONCODE = SERVICE_URL + "app/signupOtp";
    public static final String FORGOTPASSWORDTOMOBILE = SERVICE_URL + "app/forgotpassword";
    public static final String VERIFYPHONE = SERVICE_URL + "app/verifyOtp";
    public static final String GOODS_TYPE = SERVICE_URL + "slave/goodsType";
    public static final String NEWLIVEBOOKING = SERVICE_URL + "slave/livebooking";
    public static final String ALL_BOOKINGS = SERVICE_URL + "slave/bookings";       //This is used for all the bookings.
    public static final String SINGLE_BOOKING = SERVICE_URL + "slave/bookingDetails";  //This is used for single booking detail.
    public static final String CANCEL_BOOKING = SERVICE_URL + "slave/cancelBooking";
    public static final String CANCEL_REASON = SERVICE_URL + "app/cancelReasons";
    public static final String FORGOTPASSTOEMAIL = SERVICE_URL + "app/forgotpassword";
    public static final String UPDATEPASS = SERVICE_URL + "app/updatePassword";
    public static final String CHECKPASS = SERVICE_URL + "slave/checkpassword";
    public static final String GETPROFILE = SERVICE_URL + "slave/profile/me";
    public static final String SUPPORT = SERVICE_URL + "app/support";
    public static final String RATE_CARD = SERVICE_URL + "app/rateCard";
    public static final String REVIEW_RATE = SERVICE_URL + "slave/rating";
    public static final String SUBBMIT_RATING = SERVICE_URL + "slave/rating";
    //public static final String GET_DRIVERS=SERVICE_URL+"slave/Drivers/";
    public static final String GET_DRIVERS = SERVICE_URL + "slave/driverstest/";
    public static final String UPDATEPROFILE = SERVICE_URL + "slave/profile/me";
    public static final String CHATNOTIFICATION = SERVICE_URL + "slave/firebase/send";


    public static final String BOOKINGDETAIL = SERVICE_URL + "getAppointmentDetails";
    public static final String DRIVER_DETAIL = SERVICE_URL + "getMasterDetails";
    public static final String SHIPMENTfARE = SERVICE_URL + "thirdParty/fareEstimate";     //"thirdParty/google";
    public static final String PROMOCODE = SERVICE_URL + "checkCoupon";
    public static final String PARENT_FOLDER = "Ebba";
    public static final String ADDCARD = SERVICE_URL + "paymentGateway/card/me";      //+"addCard";
    public static final String GETCARD = SERVICE_URL + "paymentGateway/cards/me";      //+"getCards";              //"getSlaveAppointmentDetails";
    public static final String REMOVE_CARD = SERVICE_URL + "paymentGateway/card/me";
    public static final String DEFAULT_CARD = SERVICE_URL + "paymentGateway/card/default/me";
    public static final String PROMO_CODE = SERVICE_URL + "app/validatereferralcode";


    public static final String CONFIG = SERVICE_URL + "slave/app/config/1";
    public static final String GETAPPOINTMENTDETAILS = SERVICE_URL + "getAppointmentDetails";
    public static final String CHECKREGION = "LatLongisInzone";
    public static final String GETALLREGION = "http://159.203.118.135:8088/api/allzones";
    public static final String CHECKZONEONE = "http://159.203.118.135:8088/api/zoneone";
    public static final String CHECKZONETWO = "http://159.203.118.135:8088/api/zonetwo";
    public static final String CHECKZONEBOTH = SERVICE_URL + "app/Zone";
    public static final String CHECKAPPT_TYPE = "http://159.203.118.135/smart/services.php/getWorkplaces";

    //======================= Add/ Delete/ Get Favourite Address =====================
    public static final String GET_FAV_ADDRESSES = SERVICE_URL + "slave/address/me";
    public static final String ADD_FAV_ADDRESS = SERVICE_URL + "slave/address/me";
    public static final String DELETE_FAV_ADDRESS = SERVICE_URL + "slave/address/";

    public static boolean switchFlag = false;
    public static boolean cardFlag = false;
    public static boolean bookingFlag = false;
    public static boolean profileFlag = false;
    public static boolean pubnubflag = false;
    public static boolean cacelFlag = false;
    public static int DROP_ID = 150;
    public static int PICK_ID = 151;
    public static int COMPANY_ADDR_ID = 152;

    /**
     * if bookingalertFlag true then accepted alert will show
     */
    public static boolean bookingalertFlag = false;

    /**
     * this flag to if true then notification pop up will not come
     * and if false popup will show
     */
    public static boolean orderFlag = false;

    /**
     * if will true when driver cancel booking
     * and if it is true then when notification come controller will change
     */
    public static boolean canclebooking = false;


    /**
     * if will true when driver on the way
     * and if it is true then when notification come controller will change
     */
    public static boolean chnagebooking = false;
    public static String currentbookingPage = "";

    /**
     * flag for oreder toast
     */
    public static boolean showToast = false;
    public static String latesBid = "";
    public static String latesstatus = "";
    public static String latesSubBid = "";
    public static String promoCode = "";

    public static final String currency = "INR";
    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String DEVICE_MODEL = android.os.Build.MODEL;
    public static final String DEVICE_MAKER = android.os.Build.MANUFACTURER;
    public static final int DEVICE_TYPE = 2; //1 for ios 2 for android
    public static final int USER_TYPE = 1; //1 for customer 2 for driver
    public static final int MOBILE_EMAIL_TYPE = 1; //1 for mobile 2 for email
    public static final int GOODS_TYPE_INTENT = 735; //1 for customer 2 for driver
    public static final int CAMERA_PIC = 101, GALLERY_PIC = 102, CROP_IMAGE = 103;
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public final static int REQUEST_CODE_GALLERY = 0x1;
    public final static int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public final static int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final int RC_SIGN_IN = 7;

    // =============== By PS ==============

    public static final int REQUEST_CODE_RATING_COMMENT = 111;
    public static final String INTENT_TAG_SELECTED_REASON = "SELECTED_REASON_TAG";
    public static final String APPLICATION_ID = "com.delex.customer.provider";

    public static final String WALLET_DETAILS = SERVICE_URL + "slave/paymentsettings";
    public static final String RECHARGE_WALLET = SERVICE_URL + "slave/rechargeWallet";
    public static final String WALLET_TRANSACTIONS = SERVICE_URL + "wallet/transction/";

    public static boolean isWalletFragActive = false, isWalletUpdateCalled = false;
    public static boolean isPaymentFragActive = false, isToUpdateAlertVisible = false;
    /*Google places API*/
    public static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    public static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    public static final String OUT_JSON = "/json";
    /*Drop Address screen*/
    public static final int RECENT_TYPE_LIST = 1, FAV_TYPE_LIST = 2, SEARCH_TYPE_LIST = 3;
    /*ImagesListAdapter*/
    public static final int ITEM_ROW = 0;
    public static final int FOOTER_ROW = 1;
    /*Booking History fragments*/
    public static final int PAGE_COUNT = 3;
    /*InvoiceACtivity */
    public static final int VIEW_TYPE_LIST = 0, VIEW_TYPE_GRID = 1;
    /*AddShipmentActivity*/
    public static final int PICK_CONTACT = 105;
    /*AddCardActivity */
    public static final int REQUEST_CODE_PERMISSION_MULTIPLE = 127;

}
