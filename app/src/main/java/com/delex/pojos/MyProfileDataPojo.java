package com.delex.pojos;

/**
 * Created by embed on 25/5/17.
 */

public class MyProfileDataPojo {
    private String Name;

    private String phone;

    private String email;

    private String countryCode;



    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getPhone ()
    {
        return phone;
    }

    public void setPhone (String phone)
    {
        this.phone = phone;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", phone = "+phone+", countryCode = "+countryCode+", email = "+email+"]";
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}

