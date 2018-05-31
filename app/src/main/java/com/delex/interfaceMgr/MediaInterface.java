package com.delex.interfaceMgr;

import org.json.JSONObject;

public interface MediaInterface
{
	public void onSuccessUpload(JSONObject message);
	public void onUploadError(JSONObject message);
	public void onSuccessDownload(String fileName, byte[] stream, JSONObject object);
	public void onDownloadFailure(JSONObject object);
	public void onSuccessPreview(String fileName, byte[] stream, JSONObject object);
	//public void onLocationIconSuccess(ViewHolder holder,Uri uri);
}
