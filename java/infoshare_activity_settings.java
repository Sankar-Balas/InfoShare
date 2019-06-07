package com.example.sankba.infoshare;

/**--------------------------------------------------------------------------------------------------------------------
 All Imports goes down here
 --------------------------------------------------------------------------------------------------------------------**/

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**--------------------------------------------------------------------------------------------------------------------
  The class helps configure user settings
  Version 1.0
  Author Sankar Balasubramanian
 --------------------------------------------------------------------------------------------------------------------**/

public class infoshare_activity_settings
        extends Fragment {

/**--------------------------------------------------------------------------------------------------------------------
 Constructor
 --------------------------------------------------------------------------------------------------------------------**/

    public infoshare_activity_settings() {}

/**--------------------------------------------------------------------------------------------------------------------
 What should happen while creating a view
 --------------------------------------------------------------------------------------------------------------------**/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.infoshare_layout_settings, container, false);
        Spinner spinner = (Spinner) v.findViewById(R.id.radius);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.distance_radius, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        return v;
    }
}