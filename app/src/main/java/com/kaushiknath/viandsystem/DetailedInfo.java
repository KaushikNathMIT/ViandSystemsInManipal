package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.query.OnGetResultSetListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kaushik Nath on 4/2/2016.
 */
public class DetailedInfo extends Activity {
    String sql;
    TextView outputView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.det_info);

        outputView = (TextView) findViewById(R.id.textView5);
        TextView sel = (TextView) findViewById(R.id.sel_hotel);
        String se = getIntent().getStringExtra("Selected");
        sel.setText(se);

        sql = "select rating from info_table " +
                "where name like '" + se + "'";
        final RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);

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
                    outputView.setText(getString(R.string.error_occured) + '\n' + "this" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log));
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
                    outputView.setText(getString(R.string.error_occured) + '\n' + "that" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log));
                    Log.e("Status", "reached 3");
                } else if (true) {
                    //Since we executed only one query, the result will show up in index 0 of the array
                    //ResultSet rs = resultSets[0];

                    int i = 0;
                    try {
                        //Build the output and display it in the TextView
                        StringBuffer stringBuffer = new StringBuffer("First 5 rows:\n");

                        while (resultSets.next()) {//While there are rows and we still haven't displayed the first 5 rows
                            //i++;
                            rating.setNumStars((int) resultSets.getFloat(1));
                            stringBuffer.append(resultSets.getString(1));
                            stringBuffer.append('\n');
                        }
                        int length = i;
                        Log.e("Status", "reached 2");
                        //Always close the Result set when your done
                        resultSets.close();
                        //Finally display the rows
                        outputView.setText(stringBuffer);

                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        outputView.setText(e1.getLocalizedMessage());
                        Log.e("Status", "reached 11");
                    }
                } else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.e("Result", "Received no result sets from query");
                    outputView.setText(getString(R.string.no_result_sets));
                }
            }
        };
        outputView.setText(getString(R.string.loading));
        AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);

    }

}
