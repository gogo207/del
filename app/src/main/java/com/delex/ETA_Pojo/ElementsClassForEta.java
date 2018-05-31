package com.delex.ETA_Pojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Akbar on 26/11/16.
 */
public class ElementsClassForEta implements Serializable
{
    /*    "elements":[
        {
            "distance":{
                "text":"48 m",
                "value":48
            },
            "duration":{
                "text":"1 min",
                "value":25
            },
            "status":"OK"
        }
    ]

}*/
    private ArrayList<ElementsForEta> elements;

    public ArrayList<ElementsForEta> getElements() {
        return elements;
    }
}
