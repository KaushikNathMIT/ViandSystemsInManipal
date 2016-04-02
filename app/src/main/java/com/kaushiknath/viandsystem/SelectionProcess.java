package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.query.OnGetResultSetListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    String[] res = new String[20];
    int[] range = new int[20];
    float[] ran = new float[20];
    String sql;
    EditText rate;
    String sel_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slectionpro);
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
        rich.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rate.setText(Integer.toString(10000));
                rate.setEnabled(false);
            }
        });





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
                    Toast.makeText(getApplicationContext(), (getString(R.string.error_occured) + '\n' + "this" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log)),Toast.LENGTH_LONG);
                    return null;
                }
            }
        };
        //This listener tells the database manager what to do when we receive the result of the query execution
        //We will be using this listener when the execute button is clicked

        final OnGetResultSetListener onGetResultSetListener = new OnGetResultSetListener() {
            @Override
            public void onGetResultSet(ResultSet resultSets, SQLException e) {
                if (e != null) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), (getString(R.string.error_occured) + '\n' + "that" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log)), Toast.LENGTH_LONG);
                    Log.e("Status", "reached 3");
                } else if (true) {
                    //Since we executed only one query, the result will show up in index 0 of the array
                    //ResultSet rs = resultSets[0];

                    int i = 0;
                    try {
                        //Build the output and display it in the TextView
                        //StringBuffer stringBuffer = new StringBuffer("First 5 rows:\n");
                        while (resultSets.next()) {//While there are rows and we still haven't displayed the first 5 rows
                            //i++;
                            //stringBuffer.append(resultSets.getString(1));
                            //stringBuffer.append('\n');
                            res[i] = resultSets.getString(1);
                            range[i] = resultSets.getInt(2);
                            ran[i] = resultSets.getFloat(3);
                            Log.d("Length Here", Integer.toString(res[i].length()));
                            i++;
                        }
                        int length = i;
                        Log.e("Status", "reached 2");
                        //Always close the Result set when your done
                        resultSets.close();
                        //Finally display the rows
                        //outputView.setText(stringBuffer);

                        Intent intent = new Intent(SelectionProcess.this, T_List.class);
                        try {
                            for (i = 0; i < 5; i++)
                                Log.d("Length There", Integer.toString(res[i].length()));
                        }
                        catch(Exception e1){}
                        intent.putExtra("res", res);
                        intent.putExtra("Number", length);
                        intent.putExtra("Rates", range);
                        intent.putExtra("Ran", ran);
                        startActivity(intent);

                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        Toast.makeText(getApplicationContext(), (e1.getLocalizedMessage()), Toast.LENGTH_LONG);
                        Log.e("Status", "reached 11");
                    }
                } /*else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.e("Result", "Received no result sets from query");
                    outputView.setText(getString(R.string.no_result_sets));
                }*/
            }
        };
        executeB = (Button) findViewById(R.id.b_execute);
        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                Toast.makeText(getApplicationContext(), (getString(R.string.loading)), Toast.LENGTH_LONG);
                sql = "select name,range,h_d_radius from info_table natural join services natural join home_delivery " +
                        "where Category like '"+sel_cat+"' " +
                        "and range <=" + Integer.parseInt(rate.getText().toString());
                AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);

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
