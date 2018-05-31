package com.delex.pojos;

/**
 * Created by embed on 17/5/17.
 */

public class LoginSignUpData {
    private String Name;

    private String sid;

    private String token;

    private String sub_key;

    private String chn;

    private String Pic;

    private String server_chn;

    private String presence_chn;

    private String pub_key;

    private String mobile;
    private String referralcode;
    private String pushTopic;
    private GetAllCardsPojo default_card;

    public GetAllCardsPojo getDefault_card() {
        return default_card;
    }
    public String getName() {
        return Name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferralcode() {
        return referralcode;
    }

    public void setReferralcode(String referralcode) {
        this.referralcode = referralcode;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSub_key() {
        return sub_key;
    }

    public void setSub_key(String sub_key) {
        this.sub_key = sub_key;
    }

    public String getChn() {
        return chn;
    }

    public void setChn(String chn) {
        this.chn = chn;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public String getServer_chn() {
        return server_chn;
    }

    public void setServer_chn(String server_chn) {
        this.server_chn = server_chn;
    }

    public String getPresence_chn() {
        return presence_chn;
    }

    public void setPresence_chn(String presence_chn) {
        this.presence_chn = presence_chn;
    }

    public String getPub_key() {
        return pub_key;
    }

    public void setPub_key(String pub_key) {
        this.pub_key = pub_key;
    }

    public String getPushTopic() {
        return pushTopic;
    }

    public void setPushTopic(String pushTopic) {
        this.pushTopic = pushTopic;
    }

    @Override
    public String toString() {
        return "LoginSignUpData{" +
                "Name='" + Name + '\'' +
                ", sid='" + sid + '\'' +
                ", token='" + token + '\'' +
                ", sub_key='" + sub_key + '\'' +
                ", chn='" + chn + '\'' +
                ", Pic='" + Pic + '\'' +
                ", server_chn='" + server_chn + '\'' +
                ", presence_chn='" + presence_chn + '\'' +
                ", pub_key='" + pub_key + '\'' +
                ", mobile='" + mobile + '\'' +
                ", referralcode='" + referralcode + '\'' +
                ", pushTopic='" + pushTopic + '\'' +
                '}';
    }
}