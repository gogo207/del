package com.delex.pojos;

import java.io.Serializable;

/**
 * Created by rahul on 23/3/16.
 */
/*"text" : "26 mins",
                  "value" : 1549*/
public class DurationClass implements Serializable
{
    String text;
    String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {

        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
