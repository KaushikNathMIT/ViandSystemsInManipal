package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.update.OnUpdateCompleteListener;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushik Nath on 4/11/2016.
 */
public class AddHotel extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText name, id, ran;
    Spinner catg;
    Button add;
    RatingBar rat;
    String nam,cat,sql;
    int idi,range;
    float rate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhotel);

        setTitleColor(Color.parseColor("#ffffff"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        id = (EditText) findViewById(R.id.eidh);
        name = (EditText) findViewById(R.id.name);
        ran = (EditText) findViewById(R.id.range);
        rat = (RatingBar) findViewById(R.id.rate);
        catg = (Spinner) findViewById(R.id.cat);
        catg.setOnItemSelectedListener(this);
        add = (Button) findViewById(R.id.add);

        List<String> categories = new ArrayList<String>();
        categories.add("restaurant");
        categories.add("bakery");
        categories.add("cafe");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        catg.setAdapter(dataAdapter);

        final OnGetPrepareStatement onGetPrepareStatements = new OnGetPrepareStatement() {
            @Override
            public PreparedStatement onGetPreparedStatement(BackendConnection remoteConnection) {
                //Get the SQL query from the EditText view
                try {
                    //Prepare it to an executable statement
                    Log.d("Sql Query", sql);
                    PreparedStatement preparedStatement = remoteConnection.prepareStatement(sql);

                    //If you want to execute more than one statement at a time,
                    //simply fill up successive array elements and return it:
                    PreparedStatement[] preparedStatements = new PreparedStatement[1];
                    Log.e("Status", "reached 1");
                    preparedStatements[0] = preparedStatement;
                    return preparedStatement;
                } catch (SQLException e) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_occured) + '\n' + "this" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log), Toast.LENGTH_LONG).show();
                    return null;
                }
            }
        };

        final OnUpdateCompleteListener onUpdateCompleteListener = new OnUpdateCompleteListener() {
            @Override
            public void onUpdateComplete(int result, SQLException e) {
                Toast.makeText(getApplicationContext(),"Updation Complete",Toast.LENGTH_LONG).show();
            }
        };

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nam = name.getText().toString();
                idi = Integer.parseInt(id.getText().toString());
                range = Integer.parseInt(ran.getText().toString());
                rate = rat.getRating();
                sql = "Insert into info_table values(" + idi + ", '" + nam + "', sysdate, '" + cat + "', " + range + ", " + Float.toString(rate) + ")";
                Log.d("Updation Query", sql);
                AceQLDBManager.executeUpdate(onGetPrepareStatements,onUpdateCompleteListener);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cat = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
