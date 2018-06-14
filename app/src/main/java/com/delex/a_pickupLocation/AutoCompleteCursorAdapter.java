package com.delex.a_pickupLocation;

import android.content.ContentResolver;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.delex.customer.R;
import com.skt.Tmap.TMapPOIItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018-06-13.
 */

//public class AutoCompleteCursorAdapter extends CursorAdapter {
//
////    private ArrayList<String> searchWordList;
//
//
//    public AutoCompleteCursorAdapter(Context context, Cursor c) {
//        super(context, c, false);
//
////        this.searchWordList = new ArrayList<>();
//
//
//    }
//
//    @Override
//    public int getCount() {
//        return searchWordList.size();
//    }
//
//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup parent) {
//
//        View convertView = LayoutInflater.from(context).inflate(R.layout.item_auto_complete, parent, false);
//        ViewHolder viewHolder = new ViewHolder();
//        viewHolder.text = (TextView) convertView.findViewById(R.id.search_text);
//
//        convertView.setTag(viewHolder);
//
//        return convertView;
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        Log.d("안녕", "bindView: "+cursor.getCount());
//
//        ViewHolder viewHolder = (ViewHolder) view.getTag();
////        viewHolder.text.setText(searchWordList.get());
//
//
//    }
//
//    private static class ViewHolder {
//        TextView text;
//    }
//}
