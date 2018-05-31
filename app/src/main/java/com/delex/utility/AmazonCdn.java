package com.delex.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import com.delex.interfaceMgr.MediaInterface;


/**
 * <h1>AmazonCdn</h1>
 * <p>
 *     Class to init and handle amazon s3 upload calls
 * </p>
 */
public class AmazonCdn{
	boolean successUpload = true;
	boolean successDelete = false;
	boolean tp;
	//FireBaseChatActivity main = new FireBaseChatActivity();
	public static AmazonS3Client s3Client;
	Bitmap bitmap,tempBitmap;
	ObjectMetadata metadata,tempMetadata;
	/*
	 	USE Regions. for the last parameter
	  	Regions.AP_NORTHEAST_1		Asia Pacific (Tokyo) Region 
		Regions.AP_SOUTHEAST_1		Asia Pacific (Singapore) Region 
		Regions.AP_SOUTHEAST_2		Asia Pacific (Sydney) Region  
		Regions.EU_WEST_1			EU (Ireland) Region    
		Regions.SA_EAST_1			South America (Sao Paulo) Region  
		Regions.US_EAST_1			US Standard   
		Regions.US_WEST_1			US West (Northern California) Region	
		Regions.US_WEST_2			US West (Oregon) Region 
	 */
	private static AmazonCdn instance = null;
	private AmazonCdn()
	{

	}

	/**
	 * <h2>getInstance</h2>
	 * <p>
	 *     method to return single instance of amazonCdn
	 * </p>
	 * @return
	 */
	public static AmazonCdn getInstance()
	{
		if(instance == null)
		{
			instance = new AmazonCdn();
		}
		return instance;
	}

	/**
	 *
	 * @param accessKey: access key of  amazon account
	 * @param secretKey: secret of  amazon account
	 * @param region: region in which bucket created
	 * @return: instance of AmazonS3Client
	 */
	public static AmazonS3Client configureSettings( String accessKey, String secretKey, Regions region)
	{
		s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey,secretKey));
		s3Client.setRegion(Region.getRegion(region));
		return s3Client;
	}

	/*
	 * 
	 *  USE Region. for specifying bucket region
	 *  
	 *  Region.AP_Singapore;
		Region.AP_Sydney;
		Region.AP_Tokyo;
		Region.EU_Ireland;
		Region.SA_SaoPaulo
		Region.US_Standard
		Region.US_West
		Region.US_West_2  
	 */
	public void createBucket(final String bucketName,final com.amazonaws.services.s3.model.Region region)
	{
		if(s3Client != null)
		{
			new Thread(new Runnable()
			{
				@Override
				public void run() 
				{
					try
					{
						s3Client.createBucket(bucketName,region);
					}
					catch(AmazonServiceException exception)
					{
					}
					catch(AmazonClientException a)
					{

					}
				}
			}).start();
		}
	}

	public void presence(String channel)
	{
		//new Presence().execute(channel);
	}
	/*-------------------------------UPLOAD method for User-----------------------*/

	/**
	 * <h2>uploadMedia</h2>
	 * <p>
	 *     method to upload files
	 * </p>
	 * @param activity: calling activity reference
	 * @param uri: file path
	 * @param bucketName: bucket name in which the image to be uploaded
	 * @param fileName: file name to be stored as
	 * @param callback: interface reference to notify
	 * @param message: json object
	 * @return: S3PutObjectTask object
	 */
	public S3PutObjectTask uploadMedia(Activity activity, Uri uri, String bucketName, String fileName, MediaInterface callback, JSONObject message)
	{
		return (S3PutObjectTask) new S3PutObjectTask(activity,uri,bucketName,fileName,callback,message).execute(uri);
	}

	/*-------------------------------Download method for User-----------------------*/

	/**
	 *<h2>downloadMedia</h2>
	 * <p>
	 *     method to download files
	 * </p>
	 * @param bucketName: bucket name in which the image to be uploaded
	 * @param fileName: file name to be stored as
	 * @param callbacks: interface reference to notify
	 * @param object: json object
	 * @return: S3Download
	 */
	public S3Download downloadMedia(final String bucketName,final String fileName,MediaInterface callbacks,JSONObject object)
	{
		return (S3Download)new S3Download(bucketName,fileName,callbacks,object).execute();	
	}


	/**
	 *<h2>downloadMediaPreview</h2>
	 * <p>
	 *     method to download files previews i.e. thumbnails
	 * </p>
	 * @param bucketName: bucket name in which the image to be uploaded
	 * @param fileName: file name to be stored as
	 * @param callbacks: interface reference to notify
	 * @param object: json object
	 * @return: S3Download
	 */
	public void downloadMediaPreview(final String bucketName,final String fileName,MediaInterface callbacks,JSONObject object)
	{
		new S3Download(bucketName,fileName,callbacks,object).execute();
	}
	/*-------------------------------Method to get object metadata for users-----------------------*/


	/**
	 * <h2>getObjectMetadata</h2>
	 * <p>
	 *     method to get meta data about the file
	 * </p>
	 * @param bucketName:bucket name in which the image to be uploaded
	 * @param fileName: name of file which meta data required
	 */
	public void getObjectMetadata(final String bucketName,final String fileName)
	{
		new S3GetMetadata().execute(bucketName,fileName);
	}

	/*-------------------------------Get Url for a video to stream-----------------------*/

	/**
	 *<h2>videoUrl</h2>
	 * <p>
	 *     method to init video url
	 * </p>
	 * @param bucketName: bucket name where the video need to be uploaded
	 * @param fileName: file which has to be uploaded
	 * @return
	 */
	public String videoUrl( String bucketName,String fileName)
	{
		String url = "https://aws.amazon.com/s3/" + bucketName + "/" + fileName;
		return url;
	}

	/*-------------------------------Delete-----------------------*/

	/**
	 * <h2>deleteFile</h2>
	 * <p>
	 *    method to delete the provided file from bucket
	 * </p>
	 * @param bucketName:bucket name in which the image to be uploaded
	 * @param fileName: name of file which meta data required
	 */
	public void deleteFile(final String bucketName,final String fileName)
	{
		Utility.printLog("inside delete 1 "+bucketName+" "+fileName);

		new Thread(new Runnable()
		{
			public void run()
			{
				try 
				{
					/* If you try to delete an object that doesn't exist, Amazon S3 returns a success message 
					 * AmazonClientException: If any errors are encountered in the client while making the request or handling the response
					 * AmazonServiceException: If any errors occurred in Amazon S3 while processing the request.
					 */
					s3Client.deleteObject(bucketName, fileName);
					Utility.printLog("inside delete 2");

				}

				catch(AmazonClientException e)
				{
					throw new RuntimeException("Error deleting object from S3 bucket!", e);
				}
				catch (Exception e){
					Utility.printLog("inside delete error ");

				}
			}
		}).start();		
	}

	public String bucketListing(String bucketName)
	{
		new S3GetBucketListing().execute(bucketName);
		return "asd";
	}

	/*-------------------------------Get Object Metadata private class-----------------------*/

	/**
	 * <h2>S3GetBucketListing</h2>
	 * <p>
	 *     AsyncTask class to Get Object Metadata private class
	 * </p>
	 */
	private class S3GetBucketListing extends AsyncTask<String, Void, S3TaskResult>
	{
		protected S3TaskResult doInBackground(String... str)
		{
			try 
			{
				ObjectListing list = s3Client.listObjects(str[0]);
				do {                

					List<S3ObjectSummary> summaries = list.getObjectSummaries();
					for (S3ObjectSummary summary : summaries) 
					{
						String summaryKey = summary.getKey();               
						printLog("Bucket Content: " + summaryKey);
					}
					list = s3Client.listNextBatchOfObjects(list);
				}while (list.isTruncated());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}

	/*-------------------------------Get Object Metadata private class-----------------------*/

	/**
	 * <h2>S3GetMetadata</h2>
	 * <p>
	 *     AsyncTask class to Get Object Metadata private class-
	 * </p>
	 */
	private class S3GetMetadata extends AsyncTask<String, Void, S3TaskResult> 
	{
		protected S3TaskResult doInBackground(String... str) 
		{
			try 
			{
				metadata = s3Client.getObjectMetadata(str[0],str[1]);
				printLog("Location: " + s3Client.getBucketLocation(str[0]));;
				tempMetadata = metadata;
			}
			catch(Exception e)
			{

			}
			return null;
		}
	}
	/*-------------------------------Download private class-----------------------*/

	/**
	 * <h2>S3Download</h2>
	 * <p>
	 *     AsyncTask class to show handle amazon s3 downloads in background
	 * </p>
	 */
	public class S3Download extends AsyncTask<String, Void, S3TaskResult> 
	{
		final MediaInterface callback;
		String bucketName, fileName;
		public S3ObjectInputStream content;
		byte[] bytes;
		JSONObject object;
		boolean preview = false;
		S3Download(String bucketName,String fileName,MediaInterface callback,JSONObject object)
		{
			this.callback = callback;
			this.bucketName = bucketName;
			this.fileName = fileName;
			this.object = object;
		}
		protected S3TaskResult doInBackground(String... str)
		{		
			try 
			{
				
				content = s3Client.getObject(bucketName,fileName).getObjectContent();
				bytes = IOUtils.toByteArray(content);
				metadata = s3Client.getObjectMetadata(bucketName,fileName);
				object.put("contenttype", metadata.getContentType());
				String file[] = fileName.split("/");
				if("preview".equalsIgnoreCase(file[file.length-1]))
					preview = true;

			}
			catch(Exception e)
			{
				Log.i("","Error Came For Download and error is: "+e);
			}
			return null;
		}
		
		protected void onPostExecute(S3TaskResult result) 
		{
			Log.i("", "Post Came For Download");

			if(preview && content!=null)
				callback.onSuccessPreview(fileName,bytes,object);
			else if(content!=null)
				callback.onSuccessDownload(fileName,bytes,object);
			else
				callback.onDownloadFailure(object);
		}
	}
	/*-------------------------------UPLOAD private class-----------------------*/


	/**
	 * <h2>S3PutObjectTask</h2>
	 * <p>
	 *     AsyncTask class to show handle amazon s3 uploads in background
	 * </p>
	 */
	public class S3PutObjectTask extends AsyncTask<Uri, Void, S3TaskResult> 
	{
		Activity activity;
		String bucketName, fileName;
		Uri selectedImage;
		JSONObject object;
		private MediaInterface callback;
		
		S3PutObjectTask(Activity activity,Uri uri,String bucketName,String fileName,MediaInterface callback,JSONObject message)
		{
			this.callback = callback;
			this.activity = activity;
			this.bucketName = bucketName;
			this.fileName = fileName;
			this.object = message;
		}
		protected void onPreExecute() 
		{
			//			dialog = new ProgressDialog(activity);
			//			dialog.setMessage(activity
			//					.getString(R.string.uploading));
			//			dialog.setCancelable(false);
			//			dialog.show();
		}
		protected S3TaskResult doInBackground(Uri... uris)
		{
			if (uris == null || uris.length != 1)
			{
				return null;
			}

			// The file location of the image selected.
			selectedImage = uris[0];
			ContentResolver resolver = activity.getContentResolver();
			ObjectMetadata metadataUpload = new ObjectMetadata();
			ObjectMetadata metadataUploadPreview = new ObjectMetadata();
			ByteArrayInputStream previewStream = null;
			S3TaskResult result = new S3TaskResult();
			try 
			{
				String   i = object.getString("type");
				int j=-1;

				Log.i("", "TYPE oF Message: " + i);

				if("image".equals(i))
				{
					j=0;
				}
				if("audio".equals(i))
				{
					j=1;
				}
				if("video".equals(i))
				{
					j=2;
				}

				switch(j)
				{
				case 0:
					metadataUpload.setContentType("image/jpeg");
					previewStream = getPreviewStream(selectedImage, activity);
					metadataUploadPreview.setContentType("image/png");
					break;

				case 1:
					metadataUpload.setContentType("audio/mp3");
					break;

				case 2:
					metadataUpload.setContentType("video/mp4");
					previewStream = createVideoPreview(selectedImage);
					metadataUploadPreview.setContentType("image/png");
					break;

				default:
					Log.i("", "Coming to default AAAHHH: ");
					String fileSizeColumn[] = {OpenableColumns.SIZE};

					Cursor cursor = resolver.query(selectedImage,fileSizeColumn, null, null, null);
					cursor.moveToFirst();
					int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
					// If the size is unknown, the value stored is null.  But since an int can't be
					// null in java, the behavior is implementation-specific, which is just a fancy
					// term for "unpredictable".  So as a rule, check if it's null before assigning
					// to an int.  This will happen often:  The storage API allows for remote
					// files, whose size might not be locally known.
					String size = null;
					if (!cursor.isNull(sizeIndex))
					{
						// Technically the column stores an int, but cursor.getString will do the
						// conversion automatically.
						size = cursor.getString(sizeIndex);
					} 
					cursor.close();

					String filePath[] = fileName.split("/");
					File file = new File(Environment.getExternalStorageDirectory(), filePath[1]);
					Uri newFile = Uri.fromFile(file);
					// previewStream = getPreviewStream(newFile, activity);

					metadataUpload.setContentType(resolver.getType(selectedImage));
					if(size != null)
						metadataUpload.setContentLength(Long.parseLong(size));

					/*
					 *This is solution for OOM warning given by AmazonS3 but yet to be implemented 
					 * String streamMD5 = new String(Base64.encodeBase64(resultByte));
					 metadataUpload.setContentMD5(streamMD5);*/
				}
				
				File ter = new File(selectedImage.getPath());
				metadataUpload.setContentLength(ter.length());
				

				//fileName.replace(".", "")+"/preview"
				String testpreview = fileName.substring(fileName.indexOf("/"));
				if(testpreview.contains(".mp4"))
				{
					testpreview =	testpreview.replace(".mp4", ".png");
				}
				//Log.i("", "My File Name is:" + fileName, "My preview File Name is:" + testpreview);

				if(previewStream!=null)
				{
					PutObjectRequest porpreview = new PutObjectRequest(bucketName, "Thumbnail"+testpreview ,previewStream, metadataUploadPreview);
					porpreview.setCannedAcl(CannedAccessControlList.PublicReadWrite);
					s3Client.putObject(porpreview);
				}

				PutObjectRequest por = new PutObjectRequest(bucketName, fileName, resolver.openInputStream(selectedImage),metadataUpload);
				por.setCannedAcl(CannedAccessControlList.PublicReadWrite);
				s3Client.putObject(por);

			} 
			catch (Exception exception)
			{
				result.setErrorMessage(exception.getMessage());
			}
			return result;
		}

		protected void onPostExecute(S3TaskResult result)
		{
			Log.d("","ERROR CAME FOR JSONException"+result);
			// dialog.dismiss();
			if (result.getErrorMessage() != null)
			{
				try 
				{
					object.put("uri", selectedImage.toString());
					callback.onUploadError(object);

					Log.d("","ERROR CAME FOR UPLOAD"+result.getErrorMessage());
					successUpload = false;	
				}
				catch (JSONException e) 
				{
					Log.i("","ERROR CAME FOR JSONException"+e);
					e.printStackTrace();
				}			
			}				
			else
			{
				successUpload= true;
				callback.onSuccessUpload(object);
			}						
		}
	}
	/************************************************************/

	/**
	 * <h2>ByteArrayInputStream</h2>
	 * <p>
	 *     Uri selected image path
	 * </p>
	 * @param uri
	 * @return
	 */
	private ByteArrayInputStream createVideoPreview(Uri uri)
	{
		try 
		{			
			Bitmap thumb = ThumbnailUtils.createVideoThumbnail(uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
			Matrix matrix = new Matrix();
			Bitmap bmThumbnail = Bitmap.createBitmap(thumb, 0, 0, thumb.getWidth(), thumb.getHeight(), matrix, true);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmThumbnail.compress(CompressFormat.PNG, 200, stream);
			byte[] bitmapdata = stream.toByteArray();
			stream.close();
			return new ByteArrayInputStream(bitmapdata);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	/************************************************************/

	/**
	 * <h2>getPreviewStream</h2>
	 * <p>
	 *     method to get byte array of the selected image
	 * </p>
	 * @param selectedImage: uri of selected image
	 * @param activity: calling activity reference
	 * @return
	 */
	public ByteArrayInputStream getPreviewStream(Uri selectedImage,Activity activity)
	{
		try 
		{
			Bitmap bmp = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
			Bitmap scaled = Bitmap.createScaledBitmap(bmp, 200, 200, true);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			scaled.compress(CompressFormat.JPEG, 30 /*ignored for PNG*/, bos); 
			byte[] bitmapdata = bos.toByteArray();
			return new ByteArrayInputStream(bitmapdata);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		return null;
	}
	/************************************************************/

	public static void printLog(String... msg)
	{
		String str="";
		for(String i : msg)
		{
			str= str+"\n"+i;
		}
		if(true)
		{
			Log.i("Alpha",str);
		}
	}
	/************************************************************/

	/**
	 * <h2>S3TaskResult</h2>
	 * <p>
	 *     class to handle amazon s3 api response
	 * </p>
	 */
	private class S3TaskResult 
	{
		String errorMessage = null;
		public String getErrorMessage() 
		{
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) 
		{
			this.errorMessage = errorMessage;
		}
	}
	/************************************************************/

	/**
	 * <h2>displayAlert</h2>
	 * <p>
	 *     Display an Alert message for an error or failure.
	 * </p>
	 * @param title: alert title
	 * @param message: alert message
	 * @param activity: calling activity reference
	 */
	protected void displayAlert(String title, String message,final Activity activity) 
	{
		AlertDialog.Builder confirm = new AlertDialog.Builder(activity);
		confirm.setTitle(title);
		confirm.setMessage(message);

		confirm.setNegativeButton("OK", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.dismiss();
			}
		});
		confirm.show().show();
	}
	/************************************************************/

	/**
	 * <h2>displayErrorAlert</h2>
	 * <p>
	 *     method show an alert on failure
	 * </p>
	 * @param title: alert title
	 * @param message: alert message
	 * @param activity: calling activity reference
	 */
	protected void displayErrorAlert(String title, String message, final Activity activity)
	{
		AlertDialog.Builder confirm = new AlertDialog.Builder(activity);
		confirm.setTitle(title);
		confirm.setMessage(message);

		confirm.setNegativeButton("OK",new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which) 
			{
				//activity.finish();
			}
		});
		confirm.show().show();
	}
	/************************************************************/
}
