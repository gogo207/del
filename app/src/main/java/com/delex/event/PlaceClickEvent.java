package com.delex.event;

import com.skt.Tmap.TMapPOIItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * AddDropLocationActivity에서 쓰임
 * 리스트뷰에 검색된 결과값중 하나 클릭했을때
 */

public class PlaceClickEvent implements Event {

    private int position;

    public PlaceClickEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
