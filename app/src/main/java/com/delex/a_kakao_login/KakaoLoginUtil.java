package com.delex.a_kakao_login;

import android.util.Log;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

/**
 * Created by Administrator on 2018-06-27.
 */

public class KakaoLoginUtil {

    public String TAG = KakaoLoginUtil.class.getSimpleName();



    /**
     * 카카오 로그아웃시
     * 카카오 로그인 계정 로그아웃 (해당 메소드 호출 후 로그인 시도시 정보제공 동의요청 창 없이 바로 로그인됨)
     */
    public void kakaoLogout() {
        //로그아웃
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "onCompleteLogout: ");
            }
        });
    }

    /**
     * 가입 탈퇴시
     * 카카오 로그인 앱 연결 해제 (해당 메소드 호출 후 로그인 시도시 정보제공 동의요청 창 다시 뜸)
     */
    public void kakaoUnlink() {
        UserManagement.requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed: "+errorResult);
            }

            @Override
            public void onNotSignedUp() {

            }

            @Override
            public void onSuccess(Long result) {
                Log.d(TAG, "onSuccess: "+result);
            }
        });
    }

}
