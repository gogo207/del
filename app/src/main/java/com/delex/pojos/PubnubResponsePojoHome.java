package com.delex.pojos;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Created by embed on 19/1/16.
 */
public class PubnubResponsePojoHome implements Serializable
{

    private String flag;
    private String tp;
    private String a;
    private String st;
    private ArrayList<PubnubMasArrayPojo> masArr = new ArrayList<PubnubMasArrayPojo>();
    private ArrayList<Types> types = new ArrayList<Types>();
    private ArrayList<PubnubEsPojo> es;
    public ArrayList<PubnubEsPojo> getEs() {
        return es;
    }
    public ArrayList<Types> getTypes() {
        return types;
    }
    public void setTypes(ArrayList<Types> types) {
        this.types = types;
    }
    public ArrayList<PubnubMasArrayPojo> getMasArr() {
        return masArr;
    }
    public void setMasArr(ArrayList<PubnubMasArrayPojo> masArr) {
        this.masArr = masArr;
    }


    public String getFlag ()
    {
        return flag;
    }

    public void setFlag (String flag)
    {
        this.flag = flag;
    }

    public String getTp ()
    {
        return tp;
    }

    public void setTp (String tp)
    {
        this.tp = tp;
    }

    public String getA ()
    {
        return a;
    }

    public void setA (String a)
    {
        this.a = a;
    }



    public String getSt ()
    {
        return st;
    }

    public void setSt (String st)
    {
        this.st = st;
    }
}
