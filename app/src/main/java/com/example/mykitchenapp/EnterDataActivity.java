package com.example.mykitchenapp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import android.app.AlertDialog;
import android.widget.DatePicker;


public class EnterDataActivity extends AppCompatActivity {

    /*EditText editTextIngredientName; */
    private Spinner spinner1;
    private Spinner spinner2;
    EditText editTextIngredientQuantity;
    EditText editTextIngredientName;
    EditText editTextIngredientUnity;
    private ArrayAdapter<CharSequence> m_adapterForSpinner;
    private ArrayAdapter<CharSequence> m_adapterForSpinner2;
    private TextView mDateDisplay;
    private TextView mDateSort;
    private Button mPickDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    RadioButton RadioButton;
    public int selezione;

    static final int DATE_DIALOG_ID = 0;
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        editTextIngredientQuantity = (EditText) findViewById(R.id.et_ingredient_quantity);
        editTextIngredientName = (EditText) findViewById(R.id.et_new_ingredient);
        spinner1 = (Spinner)findViewById(R.id.ingredient_spinner);
        spinner2 = (Spinner)findViewById(R.id.et_ingredient_unity);
        mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
        mDateSort = (TextView) findViewById(R.id.dateSort);
        mPickDate = (Button) findViewById(R.id.et_ingredient_date);


        /* dati spinner */
        m_adapterForSpinner = ArrayAdapter.createFromResource(this,
                R.array.ingredienti, android.R.layout.simple_spinner_item);;
        m_adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(m_adapterForSpinner);
        /*m_adapterForSpinner.add("gr");*/
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View SelectedItemView, int position, long id) {
                // your code here
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        m_adapterForSpinner2 = ArrayAdapter.createFromResource(this,
                R.array.misure, android.R.layout.simple_spinner_item);;
        m_adapterForSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(m_adapterForSpinner2);
        /*m_adapterForSpinner.add("gr");*/
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View SelectedItemView, int position, long id) {
                // your code here
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        /*fine dati spinner */

        //aggiunge click listener al bottone
        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //add code to view dialog
                showDialog(DATE_DIALOG_ID);
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
        updateDateSort();

    }

    //aggiorna la data in textview
    private void updateDisplay() {
        if (mDay <= 9 && mMonth <= 8)
            mDateDisplay.setText(
                    new StringBuilder()
                            .append("0").append(mDay).append("/0")
                            .append(mMonth + 1).append("/")
                            .append(mYear).append(" "));

        if (mDay <= 9 && mMonth > 8)
            mDateDisplay.setText(
                    new StringBuilder()
                            .append("0").append(mDay).append("/")
                            .append(mMonth + 1).append("/")
                            .append(mYear).append(" "));

        if (mDay > 9 && mMonth <= 8)
            mDateDisplay.setText(
                    new StringBuilder()
                            .append(mDay).append("/")
                            .append("0").append(mMonth + 1).append("/")
                            .append(mYear).append(" "));

        if(mDay>9 && mMonth >8)
            mDateDisplay.setText(
                    new StringBuilder()
                            .append(mDay).append("/")
                            .append(mMonth + 1).append("/")
                            .append(mYear).append(" "));
    }

    //aggiorna la data per l'ordinamento
    private void updateDateSort() {
        if (mDay <= 9 && mMonth <= 8)
            mDateSort.setText(
                    new StringBuilder()
                            .append(mYear)
                            .append("0").append(mMonth + 1)
                            .append("0").append(mDay));

        if (mDay <= 9 && mMonth > 8)
            mDateSort.setText(
                    new StringBuilder()
                            .append(mYear)
                            .append(mMonth + 1)
                            .append("0").append(mDay));

        if (mDay > 9 && mMonth <= 8)
            mDateSort.setText(
                    new StringBuilder()
                            .append(mYear)
                            .append("0").append(mMonth + 1)
                            .append(mDay));

        if(mDay>9 && mMonth >8)
            mDateSort.setText(
                    new StringBuilder()
                            .append(mYear)
                            .append(mMonth + 1)
                            .append(mDay));
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear,mMonth, mDay);
        }
        return null;
    }

    //il callback ricevuto quando si mette una data
    private DatePickerDialog.OnDateSetListener
            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){

            //prende la data attuale
            final Calendar c = Calendar.getInstance();
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
            updateDateSort();
        }
    };

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.ingredienti_predefiniti:
                if (checked)
                    selezione =1;
                break;
            case R.id.inserimento_manuale:
                if (checked)
                    selezione =2;
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // show menu when menu button is pressed
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case R.id.action_add:

                String ingredientQuantity = editTextIngredientQuantity.getText().toString();
                String ingredientDate = mDateDisplay.getText().toString();
                String ingredientDatesort = mDateSort.getText().toString();
                String ingredientName;
                String ingredientUnity = spinner2.getSelectedItem().toString();


                if (selezione == 1)
                {ingredientName = spinner1.getSelectedItem().toString();}
                else
                {ingredientName = editTextIngredientName.getText().toString();}


                if ( ingredientName.length() != 0 && ingredientQuantity.length() != 0) {

                    Intent newIntent = getIntent();
                    newIntent.putExtra("tag_ingredient_name", ingredientName);
                    newIntent.putExtra("tag_ingredient_quantity", ingredientQuantity);
                    newIntent.putExtra("tag_ingredient_date", ingredientDate);
                    newIntent.putExtra("tag_ingredient_datesort", ingredientDatesort);
                    newIntent.putExtra("tag_ingredient_unity", ingredientUnity);

                    this.setResult(RESULT_OK, newIntent);

                    finish();
                }
                else
                    Toast.makeText(getBaseContext(), "Inserire tutti i dati" , Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
