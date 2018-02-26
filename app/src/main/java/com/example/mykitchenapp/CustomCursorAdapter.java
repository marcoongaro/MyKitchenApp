package com.example.mykitchenapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomCursorAdapter extends CursorAdapter {

    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.single_row_item, parent, false);
        return retView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

        TextView textViewIngredientName = (TextView) view.findViewById(R.id.tv_ingredient_name);
        textViewIngredientName.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));

        TextView textViewIngredientQuantity = (TextView) view.findViewById(R.id.tv_ingredient_quantity);
        textViewIngredientQuantity.setText(new StringBuilder().append(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)))).append(" ").append(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)))));

        TextView textViewIngredientDate = (TextView) view.findViewById(R.id.tv_ingredient_date);
        textViewIngredientDate.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));
    }
}