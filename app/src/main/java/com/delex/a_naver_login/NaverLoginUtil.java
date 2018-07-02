package com.delex.a_naver_login;

import android.util.Log;

import com.delex.a_sign.LoginActivity;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

/**
 * Created by Administrator on 2018-06-29.
 */

public class NaverLoginUtil {  //메소드 보고 사용

    /**
     * 카카오 로그아웃시
     * 카카오 로그인 계정 로그아웃 (해당 메소드 호출 후 로그인 시도시 정보제공 동의요청 창 없이 바로 로그인됨)
     */
    public void naverLogout() {
       //mOAuthLoginModule.logout(mContext);

    }


    /**
     * 가입 탈퇴시
     * 카카오 로그인 앱 연결 해제 (해당 메소드 호출 후 로그인 시도시 정보제공 동의요청 창 다시 뜸)
     */
    public void naverUnlink() {
//         boolean isSuccessDeleteToken = mOAuthLoginModule.logoutAndDeleteToken(LoginActivity.this);
//
//            if (!isSuccessDeleteToken) {
//                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
//                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
//                Log.d("Ddddd", "errorCode:" + mOAuthLoginModule.getLastErrorCode(LoginActivity.this));
//                Log.d("dddddd", "errorDesc:" + mOAuthLoginModule.getLastErrorDesc(LoginActivity.this));
//            }
    }
}
