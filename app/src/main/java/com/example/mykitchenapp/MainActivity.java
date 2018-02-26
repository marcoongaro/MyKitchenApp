package com.example.mykitchenapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private CustomCursorAdapter customAdapter;
    private IngredientDatabaseHelper databaseHelper;
    private static final int ENTER_DATA_REQUEST_CODE = 1;
    private ListView listView;

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new IngredientDatabaseHelper(this);

        listView = (ListView) findViewById(R.id.list_data);
        listView.setEmptyView(findViewById(R.id.empty_list_item));

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "clicked on item: " + position);
                final long row = id;
                //Toast.makeText(getBaseContext(), "Clicked " + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText edit = (EditText) dialogView.findViewById(R.id.edit1);

                dialogBuilder.setTitle("Modifica quantità");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setIcon(R.drawable.ic_write);
                dialogBuilder.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String updatedquantity = edit.getText().toString();
                        if(updatedquantity.length() != 0) {
                            int updateOK = databaseHelper.updateData(row, updatedquantity);
                            Log.d(TAG, "updated: " + updateOK);
                            customAdapter.notifyDataSetChanged();
                            listView.setAdapter(customAdapter);
                            listView.invalidateViews();
                            customAdapter.changeCursor(databaseHelper.getAllData());
                        }
                        else
                            Toast.makeText(getBaseContext(), "Quantità non inserita" , Toast.LENGTH_SHORT).show();
                    }
                });
                dialogBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

        listView.setOnItemLongClickListener((new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d(TAG, "long clicked on item: " + position);
                final long row = id;
                //Toast.makeText(getBaseContext(), "Long Clicked " + position, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.drawable.ic_action_delete);
                builder.setTitle("Eliminare ingrediente?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                boolean deleteOK = databaseHelper.deleteData(row);
                                    customAdapter.notifyDataSetChanged();
                                    listView.setAdapter(customAdapter);
                                    listView.invalidateViews();
                                    customAdapter.changeCursor(databaseHelper.getAllData());
                                if(deleteOK)
                                    Toast.makeText(getBaseContext(), "Eliminato", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getBaseContext(), "Fallito " + position, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        }));

        // Database query can be a time consuming task ..
        // so its safe to call database query in another thread

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new CustomCursorAdapter(MainActivity.this, databaseHelper.getAllData());
                listView.setAdapter(customAdapter);
            }
        });
    }

    public void onClickEnterData(View btnAdd) {

        startActivityForResult(new Intent(this, EnterDataActivity.class), ENTER_DATA_REQUEST_CODE);

    }

    public void updateView() {
        customAdapter.notifyDataSetChanged();
        listView.setAdapter(customAdapter);
        listView.invalidateViews();
        customAdapter.changeCursor(databaseHelper.getAllData());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ENTER_DATA_REQUEST_CODE && resultCode == RESULT_OK) {

            databaseHelper.insertData(data.getExtras().getString("tag_ingredient_name"),data.getExtras().getString("tag_ingredient_quantity"),data.getExtras().getString("tag_ingredient_date"),data.getExtras().getString("tag_ingredient_datesort"),data.getExtras().getString("tag_ingredient_unity"));
            customAdapter.changeCursor(databaseHelper.getAllData());
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vuoi uscire dall'App?")
                .setIcon(R.drawable.ic_exit)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_search:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.search_dialog, null);
                dialogBuilder.setView(dialogView);

                final EditText editsearch = (EditText) dialogView.findViewById(R.id.editsearch);

                dialogBuilder.setTitle("Trova nella dispensa");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setIcon(R.drawable.ic_action_search);
                dialogBuilder.setPositiveButton("Cerca", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String search = editsearch.getText().toString();
                        if(search.length() != 0) {
                            customAdapter.changeCursor(databaseHelper.getSearchData(search));
                            customAdapter.notifyDataSetChanged();
                            listView.setAdapter(customAdapter);
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();

                return true;

            case R.id.action_update:
                updateView();
                Toast.makeText(getBaseContext(), "Lista aggiornata", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_sort:
                customAdapter.changeCursor(databaseHelper.getorderedData());
                customAdapter.notifyDataSetChanged();
                listView.setAdapter(customAdapter);
                Toast.makeText(getBaseContext(), "Lista ordinata per scadenza", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


