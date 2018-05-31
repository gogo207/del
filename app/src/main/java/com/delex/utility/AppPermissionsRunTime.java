package com.delex.utility;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.delex.customer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>AppPermissionsRunTime</h1>
 * <p>
 *     Class to handle app runtime permissions
 * </p>
 *@since 24/5/16.
 */
public class AppPermissionsRunTime
{
    final public int REQUEST_CODE_PERMISSIONS = Constants.REQUEST_CODE;
    private List<String> permissionsNeeded=null;
    private List<String> permissionsList=null;
    private AlertDialog dialog_parent=null;

    public enum Permission
    {
        LOCATION, CAMERA, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, PHONE, RECORD_AUDIO, READ_CONTACT, READ_SMS, RECEIVE_SMS
    }

    /**
     * Singleton Class Object.
     */
    private static AppPermissionsRunTime app_permission = new AppPermissionsRunTime();

    /**
     * Private constructor to make this class as Singleton.
     */
    private AppPermissionsRunTime(){}

    /**
     * <h2>getInstance</h2>
     * <p>
     *     Method need to access the Object of this single tone class
     * </p>*/
    public static AppPermissionsRunTime getInstance()
    {
        return app_permission;
    }

    /**
     *
     * @param permission_list: list of required permissions
     * @param activity: calling activity reference
     * @param isFixed:
     * @return true if requested permissions are already granted
     */
    public boolean getPermission(final ArrayList<Permission> permission_list,Activity activity,boolean isFixed)
    {
        /**
         * Creating the List if not created .
         * if created then clear the list for refresh use.*/
        if(permissionsNeeded==null||permissionsList==null)
        {
            permissionsNeeded= new ArrayList<String>();
            permissionsList= new ArrayList<String>();
        }else
        {
            permissionsNeeded.clear();
            permissionsList.clear();
        }

        if(dialog_parent!=null&&dialog_parent.isShowing())
        {
            dialog_parent.dismiss();
            dialog_parent.cancel();
        }
        for(int count=0;permission_list!=null&&count<permission_list.size();count++)
        {
            switch (permission_list.get(count))
            {
                case LOCATION:
                    if (!   addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Fine Location");
                    }
                    if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION,activity))
                    {
                        permissionsNeeded.add("GPS Course Location");
                    }
                    break;
                case RECORD_AUDIO:
                    if (!   addPermission(permissionsList,Manifest.permission.RECORD_AUDIO,activity))
                    {
                        permissionsNeeded.add("Record audio");
                    }
                    break;
                case CAMERA:
                    if (!addPermission(permissionsList, Manifest.permission.CAMERA,activity))
                    {
                        permissionsNeeded.add("Camera");
                    }
                    break;
                case READ_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Write to external Storage");
                    }
                    break;
                case WRITE_EXTERNAL_STORAGE:
                    if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE,activity))
                    {
                        permissionsNeeded.add("Read to external Storage");
                    }
                    break;
                case PHONE:
                    if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE,activity))
                    {
                        permissionsNeeded.add("Read Phone State");
                    }
                    break;
                 case READ_CONTACT:
                    if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS,activity))
                    {
                        permissionsNeeded.add("Read Contact State");
                    }
                case READ_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.READ_SMS,activity))
                    {
                        permissionsNeeded.add("Read SMS State");
                    }
                case RECEIVE_SMS:
                    if (!addPermission(permissionsList, Manifest.permission.RECEIVE_SMS,activity))
                    {
                        permissionsNeeded.add("Receive SMS State");
                    }
                    break;
                default:
                    break;
            }

        }
        if (permissionsList.size() > 0&&permissionsNeeded.size() > 0)
        {
            String message = "You need to grant access to " + permissionsNeeded.get(0);
            for (int i = 1; i < permissionsNeeded.size(); i++)
            {
                message = message + ", " + permissionsNeeded.get(i);
            }
            showAlert(message,activity,isFixed);


            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     *
     * @param permissionsList: array of requested permissions
     * @param permission: array of requested permissions
     * @param activity: calling activity reference
     * @return
     */
    private boolean addPermission(List<String> permissionsList,String permission,Activity activity)
    {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            return false;
        }else
        {
            return true;
        }
    }


    /**
     * <h2>showAlert</h2>
     * <p>
     *     method to show alert for requested permission has been granted or not
     * </p>
     * @param message: to be display in permission alert
     * @param mActivity: calling activity reference
     * @param isFixed: boolean true if permission has denied permanently
     */
    private void showAlert(final String message, final Activity mActivity, final boolean isFixed)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Note.");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
                check_for_Permission(permissionsList.toArray(new String[permissionsList.size()]),mActivity);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                if (isFixed) {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    mActivity.onBackPressed();
                } else {
                    Toast.makeText(mActivity, "To Proceed fourther App need " + "\n" + message, Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }

            }
        });
        dialog_parent = alertDialog.show();
        dialog_parent.show();
    }


    /**
     * <h2>check_for_Permission</h2>
     * <p>
     *     method to check whether requested permission has granted or not
     * </p>
     * @param permissions: array of permissions to be requested
     * @param mactivity:* calling activity reference
     */
    public void check_for_Permission(String permissions[],Activity mactivity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mactivity.requestPermissions(permissions, REQUEST_CODE_PERMISSIONS);
        }
    }

}
