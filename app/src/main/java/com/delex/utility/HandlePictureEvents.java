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
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.delex.interfaceMgr.ImageUploadedAmazon;
import com.delex.interfaceMgr.MediaInterface;
import com.delex.customer.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import eu.janmuller.android.simplecropimage.CropImage;

import static android.os.Build.VERSION_CODES.N;

/**
 * <h>HandlePictureEvents</h>
 * this class open the popup for the option to take the image
 * after it takes the the, it crops the image
 * and then upload it to amazon
 * Created by ${Ali} on 8/17/2017.
 */

public class HandlePictureEvents
{
    private Activity mcontext = null;
    private String takenNewImage;
    public File newFile;
    private Fragment fragment = null;
    private AmazonCdn amazons3 ;
    private  String amazonFileName="";

    public HandlePictureEvents(Activity mcontext , Fragment fragment)
    {
        this.fragment = fragment;
        this.mcontext = mcontext;
        initializeAmazon();
    }

    /**
     * <h2>initializeAmazon</h2>
     * <p>
     *     method to configure and initialize AmazonS3
     * </p>
     */
    private void initializeAmazon()
    {
        amazons3 = AmazonCdn.getInstance();
        AmazonCdn.configureSettings(Constants.ACCESS_KEY_ID, Constants.SECRET_KEY, Regions.US_EAST_1);
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(Constants.ACCESS_KEY_ID,
                Constants.SECRET_KEY));
        s3Client.setRegion(Region.getRegion(Regions.US_EAST_1));
    }

    /**
     * <h>openDialog</h>
     * <p>
     * this dialog have the option to choose whether to take picture
     * or open gallery or cancel the dialog
     * </p>
     */

    public void openDialog()
    {
        takenNewImage = "DayRunner"+String.valueOf(System.nanoTime())+".png";
        CreateOrClearDirectory directory = CreateOrClearDirectory.getInstance();
        newFile = directory.getAlbumStorageDir(mcontext, Constants.PARENT_FOLDER+"/Profile_Pictures",false);
        final Resources resources = mcontext.getResources();
        final CharSequence[] options = {resources.getString(R.string.TakePhoto), resources.getString(R.string.ChoosefromGallery), resources.getString(R.string.action_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(resources.getString(R.string.TakePhoto)))
                {
                    takePicFromCamera();
                }
                else if (options[item].equals(resources.getString(R.string.ChoosefromGallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    if(fragment!=null)
                        fragment.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC);
                    else
                        mcontext.startActivityForResult(photoPickerIntent, Constants.GALLERY_PIC);
                    mcontext.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);

                }
                else if (options[item].equals(resources.getString(R.string.action_cancel))){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    /**
     * <h1>takePicFromCamera</h1>
     * <p>
     * This method is got called, when user chooses to take photos from camera.
     * </p>
     */
    private void takePicFromCamera()
    {
        String state;
        try
        {
            takenNewImage = "";
            state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage"+String.valueOf(System.nanoTime())+".png";
            Uri newProfileImageUri;
            if (Environment.MEDIA_MOUNTED.equals(state))
                newFile = new File(Environment.getExternalStorageDirectory()+"/"+ Constants.PARENT_FOLDER+"/Profile_Pictures/",takenNewImage);
            else
                newFile = new File(mcontext.getFilesDir()+"/"+ Constants.PARENT_FOLDER+"/Profile_Pictures/",takenNewImage);
            if(Build.VERSION.SDK_INT>=N)

                newProfileImageUri = FileProvider.getUriForFile(mcontext, mcontext.getApplicationContext().getPackageName() + ".provider", newFile);
            else
                newProfileImageUri = Uri.fromFile(newFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, newProfileImageUri);
            intent.putExtra("return-data", true);
            if(fragment!=null)
                fragment.startActivityForResult(intent, Constants.CAMERA_PIC);
            else
                mcontext.startActivityForResult(intent, Constants.CAMERA_PIC);
        }
        catch (ActivityNotFoundException e)
        {
            Utility.printLog("profile fragment cannot take picture: " + e);
        }
    }


    /**
     * <h2>startCropImage</h2>
     * <p>
     * This method got called when cropping starts done.
     * </p>
     * @param newFile image file to be cropped
     */
    public File startCropImage(File newFile)
    {
        Utility.printLog("profile fragment CROP IMAGE CALLED: "+ this.newFile.getPath());
        Utility.printLog("profile fragment CROP IMAGE CALLEDd: "+ newFile);
        Intent intent = new Intent(mcontext,CropImage.class );
        intent.putExtra(CropImage.IMAGE_PATH, this.newFile.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 4);
        intent.putExtra(CropImage.ASPECT_Y, 4);
        if(fragment!=null)
            fragment.startActivityForResult(intent, Constants.CROP_IMAGE);
        else
            mcontext.startActivityForResult(intent, Constants.CROP_IMAGE);
        return newFile;
    }

    /**
     * <h2>gallery</h2>
     * <p>
     * This method is got called, when user chooses to take photos from camera.
     * </p>
     * @param data uri data given by gallery
     */
    public File gallery(Uri data)
    {
        try {
            String state = Environment.getExternalStorageState();
            takenNewImage = "takenNewImage" + String.valueOf(System.nanoTime()) + ".png";
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                newFile = new File(Environment.getExternalStorageDirectory() + "/" + Constants.PARENT_FOLDER+ "/Profile_Pictures/", takenNewImage);
            } else {
                newFile = new File(mcontext.getFilesDir() + "/" + Constants.PARENT_FOLDER+ "/Profile_Pictures/", takenNewImage);
            }
            InputStream inputStream = mcontext.getContentResolver().openInputStream(data);
            FileOutputStream fileOutputStream = new FileOutputStream(newFile);
            Utility.copyStream(inputStream, fileOutputStream);
            fileOutputStream.close();
            inputStream.close();
            // newProfileImageUri = Uri.fromFile(newFile);
            startCropImage(newFile);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return newFile;
    }


    /**
     *<h1>uploadToAmazon</h1>
     * <p></p>
     * This method is used to upload the image on AMAZON bucket.
     *  </p>
     * @param image image file to be uploaded
     * @param imageupload interface call back for the update of profile on the server
     */
    public void uploadToAmazon(File image, final String folderName, final ImageUploadedAmazon imageupload){

        Uri muri = Uri.fromFile(image);
        MediaInterface callbacksAmazon = new MediaInterface() {
            @Override
            public void onSuccessUpload(JSONObject message) {
                String profilePicUrl="https://s3.amazonaws.com/"+Constants.PICTURE_BUCKET+"/"+folderName + amazonFileName;
                imageupload.onSuccess(profilePicUrl);
            }
            @Override
            public void onUploadError(JSONObject message) {
                if(mcontext!=null)
                    Log.d("onUploadError: ",message.toString());
                    Toast.makeText(mcontext,"chrcking",Toast.LENGTH_LONG).show();
                imageupload.onError();
            }
            @Override
            public void onSuccessDownload(String fileName, byte[] stream, JSONObject object) {
            }
            @Override
            public void onDownloadFailure(JSONObject object) {
                Log.d("onUploadError: ",object.toString());
            }
            @Override
            public void onSuccessPreview(String fileName, byte[] stream, JSONObject object) {
            }
        };
        amazonFileName = System.currentTimeMillis()+".jpg";
        JSONObject message = stringToJsonAndPublish(mcontext.getString(R.string.app_name) +"/"+ amazonFileName, Uri.fromFile(image));
        amazons3.uploadMedia(mcontext, muri, Constants.PICTURE_BUCKET, folderName +amazonFileName, callbacksAmazon, message);
    }

    /**
     * <h1>stringToJsonAndPublish</h1>
     * <p>
     *    This method is used to convert our string into json file and then publish on amazon.
     *    </p>
     * @param fileName contains the name of file.
     * @param uri contains the uri.
     * @return the json object.
     */
    private JSONObject stringToJsonAndPublish(String fileName, Uri uri) {
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
}
