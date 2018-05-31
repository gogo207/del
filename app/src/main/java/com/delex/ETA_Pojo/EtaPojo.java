package com.delex.ETA_Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akbar on 26/11/16.
 */

public class EtaPojo implements Serializable
{
    /*    "destination_addresses":[],
    "origin_addresses":[],
    ""rows":[

    {
        "elements":[]
    }

],,
    "status":"OK"*/
    private String status;
    private ArrayList<ElementsClassForEta> rows;

    public String getStatus() {
        return status;
    }

    public ArrayList<ElementsClassForEta> getRows() {
        return rows;
    }
}
