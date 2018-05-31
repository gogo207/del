package com.delex.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import com.delex.customer.R;
import com.delex.interfaceMgr.ImageOperationInterface;
import com.delex.interfaceMgr.MediaInterface;
import com.delex.interfaceMgr.ResultInterface;
import com.delex.interfaceMgr.SingleFileCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.os.Build.VERSION_CODES.N;

/**
 * <h>ImageOperation</h>
 * Class to handle all image operations
 * @since 17/8/17.
 */

public class ImageOperation
{
    private static final String TAG = "ImageOperation";
    Activity context;
    Resources resources;
    private String state,takenNewImage, fileType = "image", amazonFileName="";
    private int imageClick=1;
    private Uri mUri, newProfileImageUri;
    private AmazonCdn amazons3 ;
    private AmazonS3Client s3Client;
    private File newFile, profilePicsDir;
    private SessionManager sessionManager;
    private File mFileTemp;
    public File TestFile;


    public ImageOperation(Activity context)
    {
        this.context = context;
        resources = context.getResources();
        sessionManager = new SessionManager(context);
        initAmazon();
    }

    /**
     * <h2>initAmazon</h2>
     * <P>
     *    method to configure and initialize amazonS3
     * </P>
     */
    private void initAmazon()
    {
        amazons3 = AmazonCdn.getInstance();
        AmazonCdn.configureSettings(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY, Regions.US_EAST_1);
        s3Client = new AmazonS3Client( new BasicAWSCredentials(Constants.ACCESS_KEY_ID,Constants.SECRET_KEY ) );
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    /**
     * <h>doImageOperation</h>
     * <P>
     *     This mehtod is used to show the popup where we can select our images.
     * </P>
     * @param resultInterface ResultInterface: interface to return the operation call back
     */
    public void doImageOperation(ResultInterface resultInterface) {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog(100, options, resultInterface);
        }
    }



    public void doImageOperation1(ResultInterface resultInterface) {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog1(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog1(100, options, resultInterface);
        }
    }



    public void doImageOperation2(ResultInterface resultInterface) {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog2(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog2(100, options, resultInterface);
        }
    }



    public void doImageOperation3(ResultInterface resultInterface) {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog3(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog3(100, options, resultInterface);
        }
    }


    public void doImageOperation4(ResultInterface resultInterface) {
        clearOrCreateDir();
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog4(100, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog4(100, options, resultInterface);
        }
    }










    /**
     * <h2>doImageOperation</h2>
     * <p>
     *  This mehtod is used to show the popup where we can select our images.
     * </p>
     * @param resultInterface
     */
    public void doImageOperation(File fileTemp, int position, ResultInterface resultInterface) {
        mFileTemp = clearOrCreateDir(position);
//        this.mFileTemp = mFileTemp;
        newFile = mFileTemp;
        if (imageClick==2) {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel),resources.getString(R.string.Remove)};
            showDialog(position, options, resultInterface);
        }
        else {
            final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery),
                    resources.getString(R.string.action_cancel)};
            showDialog(position, options, resultInterface);
        }
    }

    /**
     * <h2>clearOrCreateDir</h2>
     * <p>
     *     method to create a directory if its not exist already else clear it
     * </p>
     * @param position: contains image index
     * @return: the created file
     */
    public File clearOrCreateDir(int position)
    {
        String filename;
        state = Environment.getExternalStorageState();
        if(position==-1)
        {
            filename=sessionManager.username()+System.currentTimeMillis()+".png";
        }
        else
        {
            filename=sessionManager.username()+"_"+position+".png";
        }

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), filename);
        }
        else
        {
            mFileTemp = new File(context.getFilesDir(),filename);
        }
        return mFileTemp;
    }

    /**
     * <h2>clearOrCreateDir</h2>
     * <p>
     *     method to create a directory if its not exist already else clear it
     * </p>
     * @return: the created file
     */
    private void clearOrCreateDir() {
        try {
            state = Environment.getExternalStorageState();
            File cropImagesDir;
            File[] cropImagesDirectory;
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                cropImagesDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            } else {
                cropImagesDir = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages");
                profilePicsDir = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/Profile_Pictures");
            }
            if (!cropImagesDir.isDirectory()) {
                cropImagesDir.mkdirs();
            } else {
                cropImagesDirectory = cropImagesDir.listFiles();
                if (cropImagesDirectory.length > 0) {
                    for (int i = 0; i < cropImagesDirectory.length; i++) {
                        cropImagesDirectory[i].delete();
                    }
                }
            }
            if (!profilePicsDir.isDirectory()) {
                profilePicsDir.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to select the dialog from where we can select our image, i.e. either camera or gallery.
     * @param position
     * @param options gives option.
     * @param resultInterface ResultInterface
     */
    private void showDialog(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( resources.getString(R.string.AddPhoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto))) {
                    if (position == 100)
                        resultInterface.errorMandatoryNotifier();
                    else
                        takePicture();
                } else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    context.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC);
                    context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else if (options[item].equals(resources.getString(R.string.action_cancel))) {
                    dialog.dismiss();
                } else if (options[item].equals(resources.getString(R.string.Remove))) {
                    resultInterface.errorInvalidNotifier();
                    imageClick = 1;
                }
            }
        });
        builder.show();
    }

    /**
     * <h2>takePicture</h2>
     * This method will call only when it is getting called by AddShipmentActivity Class.
     */
    private void takePicture()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
//                mImageCaptureUri = Uri.fromFile(mFileTemp);
                Utility.printLog("value of mfiletemp: "+mFileTemp + " ,id; "+Constants.APPLICATION_ID);
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }


    /**
     * This method is got called, when user chooses to take photos from camera.
     * @param singleCallbackWithParam
     */
    public void takePicFromCamera(SingleFileCallback singleCallbackWithParam) {
        try {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            } else {
                newFile = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            }
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to select the dialog from where we can select our image, i.e. either camera or gallery.
     * @param position
     * @param options gives option.
     * @param resultInterface ResultInterface
     */
    private void showDialog1(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( resources.getString(R.string.AddPhoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto))) {
                    if (position == 100)
                        resultInterface.errorMandatoryNotifier();
                    else
                        takePicture1();
                } else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    context.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC1);
                    context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else if (options[item].equals(resources.getString(R.string.action_cancel))) {
                    dialog.dismiss();
                } else if (options[item].equals(resources.getString(R.string.Remove))) {
                    resultInterface.errorInvalidNotifier();
                    imageClick = 1;
                }
            }
        });
        builder.show();
    }

    /**
     * <h2>takePicture</h2>
     * This method will call only when it is getting called by AddShipmentActivity Class.
     */
    private void takePicture1()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
//                mImageCaptureUri = Uri.fromFile(mFileTemp);
                Utility.printLog("value of mfiletemp: "+mFileTemp + " ,id; "+Constants.APPLICATION_ID);
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC1);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }


    /**
     * This method is got called, when user chooses to take photos from camera.
     * @param singleCallbackWithParam
     */
    public void takePicFromCamera1(SingleFileCallback singleCallbackWithParam) {
        try {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            } else {
                newFile = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            }
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC1);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to select the dialog from where we can select our image, i.e. either camera or gallery.
     * @param position
     * @param options gives option.
     * @param resultInterface ResultInterface
     */
    private void showDialog2(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( resources.getString(R.string.AddPhoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto))) {
                    if (position == 100)
                        resultInterface.errorMandatoryNotifier();
                    else
                        takePicture2();
                } else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    context.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC2);
                    context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else if (options[item].equals(resources.getString(R.string.action_cancel))) {
                    dialog.dismiss();
                } else if (options[item].equals(resources.getString(R.string.Remove))) {
                    resultInterface.errorInvalidNotifier();
                    imageClick = 1;
                }
            }
        });
        builder.show();
    }

    /**
     * <h2>takePicture</h2>
     * This method will call only when it is getting called by AddShipmentActivity Class.
     */
    private void takePicture2()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
//                mImageCaptureUri = Uri.fromFile(mFileTemp);
                Utility.printLog("value of mfiletemp: "+mFileTemp + " ,id; "+Constants.APPLICATION_ID);
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC2);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }


    /**
     * This method is got called, when user chooses to take photos from camera.
     * @param singleCallbackWithParam
     */
    public void takePicFromCamera2(SingleFileCallback singleCallbackWithParam) {
        try {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            } else {
                newFile = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            }
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC2);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to select the dialog from where we can select our image, i.e. either camera or gallery.
     * @param position
     * @param options gives option.
     * @param resultInterface ResultInterface
     */
    private void showDialog3(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( resources.getString(R.string.AddPhoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto))) {
                    if (position == 100)
                        resultInterface.errorMandatoryNotifier();
                    else
                        takePicture3();
                } else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    context.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC3);
                    context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else if (options[item].equals(resources.getString(R.string.action_cancel))) {
                    dialog.dismiss();
                } else if (options[item].equals(resources.getString(R.string.Remove))) {
                    resultInterface.errorInvalidNotifier();
                    imageClick = 1;
                }
            }
        });
        builder.show();
    }



    private void showDialog4(final int position, final CharSequence[] options, final ResultInterface resultInterface){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle( resources.getString(R.string.AddPhoto));
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto))) {
                    if (position == 100)
                        resultInterface.errorMandatoryNotifier();
                    else
                        takePicture4();
                } else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    context.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC4);
                    context.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                } else if (options[item].equals(resources.getString(R.string.action_cancel))) {
                    dialog.dismiss();
                } else if (options[item].equals(resources.getString(R.string.Remove))) {
                    resultInterface.errorInvalidNotifier();
                    imageClick = 1;
                }
            }
        });
        builder.show();
    }

    /**
     * <h2>takePicture</h2>
     * This method will call only when it is getting called by AddShipmentActivity Class.
     */
    private void takePicture3()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
//                mImageCaptureUri = Uri.fromFile(mFileTemp);
                Utility.printLog("value of mfiletemp: "+mFileTemp + " ,id; "+Constants.APPLICATION_ID);
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC3);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }

    private void takePicture4()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            Uri mImageCaptureUri = null;
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state))
            {
//                mImageCaptureUri = Uri.fromFile(mFileTemp);
                Utility.printLog("value of mfiletemp: "+mFileTemp + " ,id; "+Constants.APPLICATION_ID);
                if(Build.VERSION.SDK_INT>=N)
                    mImageCaptureUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,mFileTemp);
                else
                    mImageCaptureUri = Uri.fromFile(mFileTemp);
            } else
            {
                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC4);
        } catch (ActivityNotFoundException e) {

            Log.d("error", "cannot take picture", e);
        }
    }


    /**
     * This method is got called, when user chooses to take photos from camera.
     * @param singleCallbackWithParam
     */
    public void takePicFromCamera3(SingleFileCallback singleCallbackWithParam) {
        try {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            } else {
                newFile = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            }
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC3);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void takePicFromCamera4(SingleFileCallback singleCallbackWithParam) {
        try {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            } else {
                newFile = new File(context.getFilesDir() + "/" + Constants.PARENT_FOLDER + "/Media/Images/CropImages/", takenNewImage);
//                newProfileImageUri = Uri.fromFile(newFile);
            }
            if(Build.VERSION.SDK_INT>=N)
                newProfileImageUri = FileProvider.getUriForFile(context, Constants.APPLICATION_ID,newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            singleCallbackWithParam.callback(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            context.startActivityForResult(intent, Constants.CAMERA_PIC4);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }















    /**
     * <h2>uploadToAmazon</h2>
     * This method is used to upload the image on AMAZON bucket and get the callback that it is uploaded or not.
     * @param image contains the file image.
     * @param imageOperationInterface callback.
     */
    public void uploadToAmazon(File image, final String folderName, final ImageOperationInterface imageOperationInterface)
    {
        mUri = Uri.fromFile(image);
        MediaInterface callbacksAmazon = new MediaInterface()
        {
            @Override
            public void onSuccessUpload(JSONObject message)
            {
                String loadurl="https://s3.amazonaws.com/"+Constants.PICTURE_BUCKET+"/"+folderName + amazonFileName;

                imageOperationInterface.onSuccess(loadurl);
                Utility.printLog(TAG+"onSuccessUpload ");
            }
            @Override
            public void onUploadError(JSONObject message)
            {
                imageOperationInterface.onFailure();
                Utility.printLog(TAG+"onUploadError "+message);
            }
            @Override
            public void onSuccessDownload(String fileName, byte[] stream, JSONObject object)
            {
            }
            @Override
            public void onDownloadFailure(JSONObject object)
            {
            }
            @Override
            public void onSuccessPreview(String fileName, byte[] stream, JSONObject object) {
            }
        };
        amazonFileName = System.currentTimeMillis()+".jpg";
//        amazonFileName = sessionManager.getCustomerEmail()+".jpg";
        Utility.printLog("pppppp image upload in amazon " + amazonFileName);
        JSONObject message = stringToJsonAndPublish(context.getString(R.string.app_name) +"/"+ amazonFileName, Uri.fromFile(image));
        amazons3.uploadMedia(context, mUri, Constants.PICTURE_BUCKET, folderName + amazonFileName, callbacksAmazon, message);
    }

    /**
     * This method is used to convert our string into json file and then publish on amazon.
     * @param fileName contains the name of file.
     * @param uri contains the uri.
     * @return the json object.
     */
    public JSONObject stringToJsonAndPublish(String fileName, Uri uri) {
        JSONObject message = new JSONObject();
        try {
            message.put("type", "image");
            message.put("filename", fileName);
            message.put("uri", uri.toString());
            message.put("uploaded", "inprocess");
            message.put("confirm", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param file Contains the File context.
     */
    public File startCropImage(File file) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        context.startActivityForResult(intent, Constants.CROP_IMAGE);
        return file;
    }


    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param file Contains the File context.
     */
    public File startCropImage1(File file) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        context.startActivityForResult(intent, Constants.CROP_IMAGE1);
        return file;
    }


    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param file Contains the File context.
     */
    public File startCropImage2(File file) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        context.startActivityForResult(intent, Constants.CROP_IMAGE2);
        return file;
    }

    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param file Contains the File context.
     */
    public File startCropImage3(File file) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        context.startActivityForResult(intent, Constants.CROP_IMAGE3);
        return file;
    }


    public File startCropImage4(File file) {
        if (file == null || file.equals(""))
            file = newFile;
        Intent intent = new Intent(context, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, file.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        context.startActivityForResult(intent, Constants.CROP_IMAGE4);
        return file;
    }


    /**
     * <h2>imageGallery</h2>
     * <p>
     *     method to read the image strean and save to gallery in the given file
     * </p>
     * @param data
     * @param mFileTemp
     */
    public void imageGallery(Intent data, File mFileTemp)
    {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
            Log.d("", "inputStream" + inputStream);
            Log.d("", "fileOutputStream" + fileOutputStream);
            copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();

            startCropImage(mFileTemp);

        } catch (Exception e) {

            Log.d("", "Error while creating temp file", e);
        }
    }

    /**
     * <h2>copyStream</h2>
     * <p>
     *     method to copy image byte streams from inputReadBuffer to OutputReadBuffer
     * </p>
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copyStream(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1)
        {
            output.write(buffer, 0, bytesRead);
        }
    }
}
