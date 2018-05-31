package com.delex.utility;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

/**
 * <h1>CustomOnItemSelectedListener</h1>
 * <P>
 *     method to handle onItemClick listener of recycler view
 * </P>
 */
public class CustomOnItemSelectedListener implements OnItemSelectedListener
{

    /**
     *
     * @param parent: adapter reference
     * @param view: root view reference
     * @param pos: item position of the recycler view
     * @param id: id of the clicked view
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {

    }

}