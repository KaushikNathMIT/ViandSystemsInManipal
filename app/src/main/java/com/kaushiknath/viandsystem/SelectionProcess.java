package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushik Nath on 09-Mar-16.
 */
public class SelectionProcess extends Activity implements AdapterView.OnItemSelectedListener {
    //Tap it to execute query
    Button executeB;
    //It shows the results of the query or an error if it occurs
    //TextView outputView;


    EditText rate;
    String sel_cat;
    int hd;
    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slectionpro);

        hd = getIntent().getIntExtra("val", 0);

        Spinner spinner = (Spinner) findViewById(R.id.spin);
        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<String>();
        categories.add("restaurant");
        categories.add("bakery");
        categories.add("cafe");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        rate = (EditText) findViewById(R.id.rateval);

        Button rich = (Button) findViewById(R.id.button);
        rich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rate.setText(Integer.toString(10000));
                rate.setEnabled(false);
            }
        });


        executeB = (Button) findViewById(R.id.b_execute);
        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                Toast.makeText(getApplicationContext(), (getString(R.string.loading)), Toast.LENGTH_LONG).show();
                StringBuffer sqlbuffer = new StringBuffer("");
                sqlbuffer.append("select name,range,h_d_radius from info_table natural join services natural join home_delivery natural join payment_option " +
                        "where Category like '" + sel_cat + "' " +
                        "and range <=" + Integer.parseInt(rate.getText().toString()));
                if (hd == 1) {
                    sqlbuffer.append(" and h_d_radius > 0");
                }
                sql = sqlbuffer.toString();
                Intent intent = new Intent(SelectionProcess.this, T_List.class);
                intent.putExtra("sql_q", sql);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        sel_cat = parent.getItemAtPosition(position).toString();

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
