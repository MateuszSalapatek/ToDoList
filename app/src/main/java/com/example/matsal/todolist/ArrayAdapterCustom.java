package com.example.matsal.todolist;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ArrayAdapterCustom extends ArrayAdapter {
    private String[] list;
    MainActivity ma = new MainActivity();
    ArrayList<Integer> checkedIds;


    public ArrayAdapterCustom(Context context, int textViewResourceId, ArrayList<NoteLists> objects, ArrayList<Integer> checkedIds) {
        super(context, textViewResourceId, objects);
        this.checkedIds = checkedIds;
    }

    ///////
    //to check bacggroud of checked items, when activity is refreshed
    //////
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        view.setBackgroundColor(Color.WHITE);
        for (int k = 0; k<checkedIds.size(); k++){
            if (checkedIds.get(k) == position ) {
                view.setBackgroundColor(Color.LTGRAY);
            }
        }
        return view;
    }
}
