package com.delex.interfaceMgr;

import com.delex.pojos.LanguagePojo;

/**
 * Created by embed on 25/12/17.
 */

public interface LanguageApi {
     void SuccessLang(LanguagePojo result);
     void errorLang(String error);
}
