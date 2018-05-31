package com.delex.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by embed on 2/12/15.
 */
public class PubnubMasArrayPojo implements Serializable {

    private String tid;
    private ArrayList<PubnubMasPojo> mas;

    public ArrayList<PubnubMasPojo> getMas() {
        return mas;
    }

    public void setMas(ArrayList<PubnubMasPojo> mas) {
        this.mas = mas;
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
