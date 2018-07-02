package com.delex.a_naver_login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.delex.a_main.MainActivity;
import com.delex.a_sign.LoginActivity;
import com.delex.controllers.LoginController;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2018-06-29.
 */

public class NaverHandler extends OAuthLoginHandler {

    private Context mContext;
    //    private LoginActivity activity;
    private OAuthLogin mOAuthLoginModule;
    public static final String TAG = NaverHandler.class.getSimpleName();
    private LoginController loginController;


    public NaverHandler(Context mContext, OAuthLogin mOAuthLoginModule, LoginController loginController) {
        this.mContext = mContext;
        this.mOAuthLoginModule = mOAuthLoginModule;
//        this.activity = activity;
        this.loginController = loginController;
    }

    @Override
    public void run(boolean success) {
        if (success) {
            final String accessToken = mOAuthLoginModule.getAccessToken(mContext);
            Log.d("dddd", "run: "+accessToken);
            Log.d(TAG, "run: AAAAN1zfobdEwyx1fu2iv+FdOwkLFliN5ws6hc0xRUH8iVUdlqCqqrKuoHWbRDdT1M8I3g5Y/g55Ogm/NwguiaR1zkE=");
            // 얘가 있어야 유저 정보를 가져올 수 있습니다.
            ProfileTask task = new ProfileTask();
            // 이 클래스가 유저정보를 가져오는 업무를 담당합니다.
            task.execute(accessToken);
        } else {
            String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
            String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
            Toast.makeText(mContext, "errorCode:" + errorCode
                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
        }
    }

    class ProfileTask extends AsyncTask<String, Void, String> {
        String result;

        @Override
        protected String doInBackground(String... strings) {
            String token = strings[0];// 네이버 로그인 접근 토큰;
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                result = response.toString();
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            //result 값은 JSONObject 형태로 넘어옵니다.
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(result);
                if (object.getString("resultcode").equals("00")) {
                    JSONObject jsonObject = new JSONObject(object.getString("response"));
                    Log.d(TAG, "onPostExecute: " + jsonObject.toString());

                    loginController.naverLogin(jsonObject);

//                    SharedPreferences.Editor editor = activity.userData.edit();
//                    editor.putString("email", jsonObject.getString("email"));
//                    editor.putString("name", jsonObject.getString("name"));
//                    editor.putString("nickname", jsonObject.getString("nickname"));
//                    editor.putString("profile", jsonObject.getString("profile_image"));
//                    editor.apply();
//                    Intent intent = new Intent(activity, MainActivity.class);
//                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}