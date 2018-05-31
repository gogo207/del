package com.delex.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 2/12/15.
 */
public class PubnubEsPojo implements Serializable {


    private ArrayList<String> em;
    private String tid;
    public ArrayList<String> getEm() {
        return em;
    }

    public void setEm(ArrayList<String> em) {
        this.em = em;
    }



    public String getTid ()
    {
        return tid;
    }

    public void setTid (String tid)
    {
        this.tid = tid;
    }
}
