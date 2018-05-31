package com.delex.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.delex.ParentFragment;
import com.delex.interfaceMgr.ImageUploadedAmazon;
import com.delex.interfaceMgr.ProfileDataChangeResponseNotifier;
import com.delex.model.ProfileModel;
import com.delex.utility.AppPermissionsRunTime;
import com.delex.utility.AppTypeface;
import com.delex.utility.CircleTransform;
import com.delex.utility.Constants;
import com.delex.utility.HandlePictureEvents;
import com.delex.utility.SessionManager;
import com.delex.utility.Utility;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;


/**
 * <h1>Profile Screen</h1>
 * This class is used to provide the Profile screen, where we can show the user pictures and their information details.
 * @author 3embed
 * @since 3 Jan 2017.
 */
public class ProfileFragment extends ParentFragment implements View.OnClickListener, ProfileDataChangeResponseNotifier
{
    private View view;
    private ImageView ivProfilePic_profileFrag;
    private TextInputEditText tietName_profileFrag, tietPhoneNo_profileFrag;
    private TextInputLayout tilOldPswd_changePswdDialog;
    private TextInputEditText tietEmail_profileFrag, tietPassword_profileFrag, tietOldPswd_changePswdDialog;

    private SessionManager session;
    private Dialog dialog;
    private String  profilePicUrl = "";
    private AppPermissionsRunTime permissionsRunTime;
    private ArrayList<AppPermissionsRunTime.Permission> permissionArrayList;
    private HandlePictureEvents handlePicEvent;
    private ProfileModel profilModel;
    private ProgressBar progressBar;
    private AppTypeface appTypeface;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initializeObj();
    }

    /**
     * <h2>initializeObj</h2>
     * <p>
     *     method to initialize the variables and their values
     * </p>
     */
    private void initializeObj()
    {
        appTypeface = AppTypeface.getInstance(getActivity());
        permissionsRunTime = AppPermissionsRunTime.getInstance();
        permissionArrayList = new ArrayList<AppPermissionsRunTime.Permission>();
        session = new SessionManager(getActivity());

        handlePicEvent = new HandlePictureEvents(getActivity(),ProfileFragment.this);
        profilModel = new ProfileModel(getActivity(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if(view != null)
        {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try
        {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
            initToolBar();
            initializeViews();
        } catch (InflateException e)
        {
            Utility.printLog("onCreateView  InflateException " + e);
        }
        return view;
    }


    private void initToolBar()
    {
        ImageView ivMenuBtn_profileFrag =  view.findViewById(R.id.ivMenuBtn_profileFrag);
        ivMenuBtn_profileFrag.setOnClickListener(this);

        TextView tvToolBarTitle_profileFrag = view.findViewById(R.id.tvToolBarTitle_profileFrag);
        tvToolBarTitle_profileFrag.setTypeface(appTypeface.getPro_narMedium());
        tvToolBarTitle_profileFrag.setText(getActivity().getString(R.string.my_profile));
    }


    /**
     * <h2>initViews</h2>
     * <p>Initializing view elements</p>
     */
    private void initializeViews()
    {
        ivProfilePic_profileFrag = view.findViewById(R.id.ivProfilePic_profileFrag);
        ivProfilePic_profileFrag.setOnClickListener(this);
        ImageView ivAddIcon_profileFrag =  view.findViewById(R.id.ivAddIcon_profileFrag);
        ivAddIcon_profileFrag.setOnClickListener(this);

        tietName_profileFrag =  view.findViewById(R.id.tietName_profileFrag);
        tietName_profileFrag.setTypeface(appTypeface.getPro_News());
        ImageView ivEditName_profileFrag =  view.findViewById(R.id.ivEditName_profileFrag);
        ivEditName_profileFrag.setOnClickListener(this);

        tietPhoneNo_profileFrag =  view.findViewById(R.id.tietPhoneNo_profileFrag);
        tietPhoneNo_profileFrag.setTypeface(appTypeface.getPro_News());
        ImageView ivPhoneNo_profileFrag = view.findViewById(R.id.ivPhoneNo_profileFrag);
        ivPhoneNo_profileFrag.setOnClickListener(this);

        tietEmail_profileFrag =  view.findViewById(R.id.tietEmail_profileFrag);
        tietEmail_profileFrag.setTypeface(appTypeface.getPro_News());
        ImageView ivEmail_profileFrag =  view.findViewById(R.id.ivEmail_profileFrag);
        ivEmail_profileFrag.setOnClickListener(this);

        tietPassword_profileFrag =  view.findViewById(R.id.tietPassword_profileFrag);
        tietPassword_profileFrag.setTypeface(appTypeface.getPro_News());
        ImageView ivPassword_profileFrag =  view.findViewById(R.id.ivPassword_profileFrag);
        ivPassword_profileFrag.setOnClickListener(this);

        progressBar =  view.findViewById(R.id.pBar_profileFrag);

        TextView tvLogout = view.findViewById(R.id.tvLogout);
        tvLogout.setTypeface(appTypeface.getPro_News());
        tvLogout.setOnClickListener(this);
    }

    /**
     * <h3>onClick()</h3>
     * This is the overridden onClick() method which is used to handle the click events on the view
     * @param v Parameter on which the click event happened
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivMenuBtn_profileFrag:
                DrawerLayout mDrawerLayout =  getActivity().findViewById(R.id.drawer_layout);
                ((MainActivity) getActivity()).moveDrawer(mDrawerLayout);
                break;

            case R.id.ivEditName_profileFrag:
                validateRespectiveField(1);
                break;

            case R.id.ivPassword_profileFrag:
                validateRespectiveField(2);
                break;

            case R.id.ivEmail_profileFrag:
                validateRespectiveField(3);
                break;

            case R.id.ivPhoneNo_profileFrag:
                validateRespectiveField(4);
                break;

            case R.id.ivAddIcon_profileFrag:
            case R.id.ivProfilePic_profileFrag:
                permissionArrayList.add(AppPermissionsRunTime.Permission.CAMERA);
                permissionArrayList.add(AppPermissionsRunTime.Permission.READ_EXTERNAL_STORAGE);

                if (Build.VERSION.SDK_INT >= 23) {
                    if (permissionsRunTime.getPermission(permissionArrayList, getActivity(), true))
                    {
                        selectImage();
                    }
                }
                else {
                    selectImage();
                }
                break;

            case R.id.tvLogout:
                profilModel.logoutConfirmationAlert();
                break;
        }
    }

    /**
     * <h2>checkCurrentPassword</h2>
     * <p>
     * This method is used to open a dialog for asking the current etNewPassword and then will
     * start operating based on our requirements like, changing name, etNewPassword, phone or email.
     * </p>
     */
    private void checkCurrentPassword()
    {
        dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_password);

        tilOldPswd_changePswdDialog = dialog.findViewById(R.id.tilOldPswd_changePswdDialog);
        tietOldPswd_changePswdDialog =dialog.findViewById(R.id.tietOldPswd_changePswdDialog);
        tietOldPswd_changePswdDialog.requestFocus();
        showSoftKeyBoard(dialog);

        dialog.findViewById(R.id.btnOk_changePswdDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tietOldPswd_changePswdDialog.getText().toString().equals("")) {
                    tilOldPswd_changePswdDialog.setEnabled(true);
                    tilOldPswd_changePswdDialog.setError(getString(R.string.password_not_empty));
                }
                else {
                    tilOldPswd_changePswdDialog.setEnabled(false);
                    profilModel.checkCurrentPassAPI(tietOldPswd_changePswdDialog.getText().toString());
                }
            }
        });
        dialog.show();
    }

    /**
     * <h2>showSoftKeyBoard</h2>
     * <p>
     * This method is used for opening the soft key board.
     * </p>
     * @param dialog soft key dialog
     */
    private void showSoftKeyBoard(Dialog dialog)
    {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * <h2>validateRespectiveField</h2>
     * <p>
     * This method is used to open a dialog for asking the current etNewPassword and then
     * will start operating based on our requirements like, changing name, etNewPassword, phone or email.
     * </p>
     * @param flag, contains the flag, for checking. 1->Name, 2->Password, 3-> Email, 4-> Phone.
     */
    private void validateRespectiveField(final int flag)
    {
        Intent intent;
        switch (flag)
        {
            case 1:
                intent = new Intent(getActivity(), EditNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ent_name", tietName_profileFrag.getText().toString());
                bundle.putString("ent_password", tietPassword_profileFrag.getText().toString());
                bundle.putString("ent_email", tietEmail_profileFrag.getText().toString());
                bundle.putString("ent_mobile", tietPhoneNo_profileFrag.getText().toString());
                bundle.putString("ent_profile", session.imageUrl());

                intent.putExtras(bundle);
                startActivity(intent);
                break;

            case 2:
                checkCurrentPassword();
                break;

            case 3:
                intent = new Intent(getActivity(),EditEmailActivity.class);
                startActivity(intent);
                break;

            case 4:
                intent= new Intent(getActivity(),EditPhoneNumberActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= 23) {
            if (permissionsRunTime.getPermission(permissionArrayList,getActivity(), true))
            {
                callResume();
            }
        }
        else {
            callResume();
        }
    }

    /**
     * This method got called, once we give any permission to our required permission.
     * @param requestCode  contains request code.
     * @param permissions   contains Permission list.
     * @param grantResults  contains the grant permission result.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.REQUEST_CODE)
        {
            boolean isAllGranted = true;
            for (String permission : permissions) {
                if (permission.equals(PackageManager.PERMISSION_GRANTED)) {
                    isAllGranted = false;
                }
            }
            if ( !isAllGranted)
            {
                permissionsRunTime.getPermission(permissionArrayList, getActivity(), true);
            }
            else
            {
                callResume();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * <h2>callResume</h2>
     * <p>
     * This method is used to perform all the task, which we wants to do on our onResume() method.
     * </p>
     */
    private void callResume()
    {
        if(session.getIsProfile())
        {
            session.setIsProfile(false);
            profilModel.getProfileDetails();
        }
        else
        {
            updateUI();
        }
    }

    /**
     * <h1>selectImage</h1>
     * @see HandlePictureEvents
     * <p>
     * This mehtod is used to show the popup where we can select our images.
     * </p>
     */
    private void selectImage()
    {
        handlePicEvent.openDialog();
    }


    /**
     * This is an overrided method, got a call, when an activity opens by StartActivityForResult(), and return something back to its calling activity.
     * @param requestCode returning the request code.
     * @param resultCode returning the result code.
     * @param data contains the actual data. */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)    //result code to check is the result is ok or not
        {
            return;
        }
        switch (requestCode)
        {
            case Constants.CAMERA_PIC:
                handlePicEvent.startCropImage(handlePicEvent.newFile);
                break;

            case Constants.GALLERY_PIC:
                Log.d("GALLERY", "onActivityResult: "+data);
                if(data!=null)
                {
                    handlePicEvent.gallery(data.getData());
                }
                break;
            case Constants.CROP_IMAGE:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path != null)
                {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        handlePicEvent.uploadToAmazon(new File(path), Constants.AMAZON_PROFILE_FOLDER, new ImageUploadedAmazon() {
                            @Override
                            public void onSuccess(String image)
                            {
                                Log.d("TAGPROFILE", "onSuccess: "+image);
                                profilePicUrl = image;
                                session.setImageUrl(image);
                                Picasso.with(getActivity()).load(session.imageUrl())
                                        .resize(ivProfilePic_profileFrag.getWidth(), ivProfilePic_profileFrag.getHeight())
                                        .centerCrop().transform(new CircleTransform())
                                        .placeholder(R.drawable.default_userpic).into(ivProfilePic_profileFrag);
                                ((MainActivity)getActivity()).setHeaderView();
                                progressBar.setVisibility(View.GONE);
                                profilModel.updateProfilePic(profilePicUrl);

                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    break;
                }
        }
    }

    @Override
    public void successUiUpdater()
    {
        updateUI();
    }

    /**
     * <h2>updateUI</h2>
     * <p>
     * This method is used for updating the UI data.
     * </p>
     */
    private void updateUI()
    {
        Log.d( "updateUI123: ",session.getCOUNTRYCODE()+session.getMobileNo());
        String img_url = session.imageUrl();
        tietName_profileFrag.setText(session.username());
        tietEmail_profileFrag.setText(session.getEMail());
        tietPhoneNo_profileFrag.setText(session.getCOUNTRYCODE()+session.getMobileNo());
        tietPassword_profileFrag.setText(session.getPassword());
        Utility.printLog("value of image url: *"+img_url+"*");
        if (img_url!=null && !img_url.isEmpty() && !img_url.equals(" "))
        {
            Picasso.with(getActivity()).load(img_url)
                    .placeholder(R.drawable.default_userpic)
                    .into(ivProfilePic_profileFrag);
        }
        ((MainActivity)getActivity()).setHeaderName();
    }

    @Override
    public void sessionExpired()
    {
        Utility.sessionExpire(getActivity());
    }

    @Override
    public void OnPasswordChangeError(String error)
    {
        tilOldPswd_changePswdDialog.setEnabled(true);
        tilOldPswd_changePswdDialog.setError(error);
    }

    @Override
    public void onPasswordSuccess()
    {
        dialog.dismiss();
    }
}