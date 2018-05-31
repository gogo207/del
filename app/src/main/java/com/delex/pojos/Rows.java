package com.delex.pojos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by rahul on 23/3/16.
 */


/*
*  "elements" : [
            {
               "distance" : {
                  "text" : "9.9 km",
                  "value" : 9887
               },
               "duration" : {
                  "text" : "26 mins",
                  "value" : 1549
               },
               "status" : "OK"
            }
         ]*/
public class Rows implements Serializable
{
    ArrayList<Elements>  elements;

    public ArrayList<Elements> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Elements> elements) {
        this.elements = elements;
    }
}
