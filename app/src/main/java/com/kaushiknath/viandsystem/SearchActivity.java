package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.query.OnGetResultSetListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kaushik Nath on 4/4/2016.
 */
public class SearchActivity extends Activity {
    Button executeB;
    //It shows the results of the query or an error if it occurs
    //TextView outputView;
    String[] res = new String[20];
    int[] range = new int[20];
    float[] ran = new float[20];
    String sql;
    StringBuffer sqlbuffer = new StringBuffer("");
    int hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_search);

        hd = getIntent().getIntExtra("val", 0);

        final EditText hotser = (EditText) findViewById(R.id.hotel_search_text);

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
                    Toast.makeText(getApplicationContext(), (getString(R.string.error_occured) + '\n' + "this" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log)), Toast.LENGTH_LONG);
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

                        Intent intent = new Intent(SearchActivity.this, T_List.class);
                        try {
                            for (i = 0; i < 5; i++)
                                Log.d("Length There", Integer.toString(res[i].length()));
                        } catch (Exception e1) {
                        }
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
                }
            }
        };
        executeB = (Button) findViewById(R.id.button2);
        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                Toast.makeText(getApplicationContext(), (getString(R.string.loading)), Toast.LENGTH_LONG);
                sqlbuffer.equals("");
                sqlbuffer.append("select name,range,h_d_radius from info_table natural join services natural join home_delivery " +
                        "where name like '%" + hotser.getText() + "%'");
                if (hd == 1) {
                    sqlbuffer.append(" and h_d_radius > 0");
                }
                sql = sqlbuffer.toString();
                AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);

            }
        });
    }

}
