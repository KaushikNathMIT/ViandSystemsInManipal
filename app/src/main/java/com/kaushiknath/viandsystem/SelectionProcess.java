package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.query.OnGetResultSetListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kaushik Nath on 09-Mar-16.
 */
public class SelectionProcess extends Activity {
    EditText inputView;
    //Tap it to execute query
    Button executeB;
    //It shows the results of the query or an error if it occurs
    TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slectionpro);
        outputView = (TextView) findViewById(R.id.tv_output);
        inputView = (EditText) findViewById(R.id.et_input);
        final OnGetPrepareStatement onGetPrepareStatements = new OnGetPrepareStatement() {
            @Override
            public PreparedStatement onGetPreparedStatement(BackendConnection remoteConnection) {
                //Get the SQL query from the EditText view
                String sql = inputView.getText().toString();
                try {
                    //Prepare it to an executable statement
                    PreparedStatement preparedStatement = remoteConnection.prepareStatement(sql);

                    //If you want to execute more than one statement at a time,
                    //simply fill up successive array elements and return it:
                    PreparedStatement[] preparedStatements = new PreparedStatement[1];
                    Log.e("Status","reached 1");
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
                        while (resultSets.next() && i < 5) {//While there are rows and we still haven't displayed the first 5 rows
                            i++;
                            stringBuffer.append(resultSets.getString(1));
                            stringBuffer.append('\n');
                        }
                        Log.e("Status","reached 2");
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
        executeB = (Button) findViewById(R.id.b_execute);
        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                outputView.setText(getString(R.string.loading));
                AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);
            }
        });
    }
}
