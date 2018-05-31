package com.delex.ETA_Pojo;

import java.io.Serializable;

/**
 * @author Akbar on 26/11/16.
 */
public class DurationForEta implements Serializable
{
    /*    "text":"48 m",
    "value":48*/
    private String value;

    public String getText() {
        return text;
    }

    private String text;

    public String getValue() {
        return value;
    }
}
