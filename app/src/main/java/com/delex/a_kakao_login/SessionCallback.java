package com.delex.a_kakao_login;

import android.content.Context;
import android.util.Log;

import com.delex.controllers.LoginController;
import com.delex.model.LoginModel;
import com.delex.utility.SessionManager;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

/**
 * Created by Administrator on 2018-06-27.
 */

public class SessionCallback implements ISessionCallback {
    public static final String TAG = SessionCallback.class.getSimpleName();
    private LoginController loginController;


    public SessionCallback(LoginController loginController) {
        this.loginController = loginController;
    }

    //로그인에 성공한 상태
    // 세션 정보가 있는 상태
    @Override
    public void onSessionOpened() {
        Log.d("onActivityResult", "onSessionOpened: 로그인 성공");
//        Log.d(TAG, "토큰값"+ Session.getCurrentSession().getAccessToken());



        getKakaoUserInfo();  //유저 정보 가져오는 메소드

    }

    //로그인에 실패한상태
    // 세션 정보가 없는 상태
    @Override
    public void onSessionOpenFailed(KakaoException exception) {

        Log.d(TAG, "onCreate: "+ Session.getCurrentSession().getAccessToken());
        Log.d(TAG, "onActivityResult: onSessionOpenFailed" + exception);
    }

    protected void redirectSignupActivity() {
        //로그인이 성공적으로 끝났을때 넘어가는 INTENT 설정

    }

    /**
     * 카카오 로그인 완료시 유저정보 가져오는 메소드
     */

    public void getKakaoUserInfo() {

        // 사용자정보 요청 결과에 대한 Callback
        UserManagement.requestMe(new MeResponseCallback() {

            // 세션 오픈 실패. 세션이 삭제된 경우,
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
            }

            @Override
            public void onNotSignedUp() {
                Log.e("SessionCallback :: ", "onNotSignedUp");
            }

            // 사용자정보 요청에 성공한 경우,

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e("SessionCallback :: ", "onSuccess");

                String nickname = userProfile.getNickname();
                String profileImagePath = userProfile.getProfileImagePath();
                String thumnailPath = userProfile.getThumbnailImagePath();
                String UUID = userProfile.getUUID();
                long id = userProfile.getId(); //password에 저장

                loginController.kakaoLogin(userProfile);

            }

            // 사용자 정보 요청 실패

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e("SessionCallback :: ", "onFailure : " + errorResult.getErrorMessage());
            }
        });
    }


}
