package com.kaushiknath.viandsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.OnGetResultSetListener;
import org.kawanfw.sql.api.client.android.OnPrepareStatements;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Gets the AceQL server URL to connect to
    EditText serverURLView;
    //Gets the SQL query to execute
    EditText inputView;
    //Tap it to execute query
    Button executeB;
    //It shows the results of the query or an error if it occurs
    TextView outputView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view from the layout XML file
        inputView = (EditText) findViewById(R.id.et_input);
        serverURLView = (EditText) findViewById(R.id.et_server_url);
        executeB = (Button) findViewById(R.id.b_execute);
        outputView = (TextView) findViewById(R.id.tv_output);

        //Restore the previously entered values into the fields if any for those lazy folks
        restoreInputConfiguration();

        //This listener tells the database manager what kind of statements to execute
        //We will be using this listener when the execute button is clicked
        final OnPrepareStatements onPreparedStatementsListener = new OnPrepareStatements() {
            @Override
            public PreparedStatement[] onGetPreparedStatementListener(BackendConnection remoteConnection) {
                //Get the SQL query from the EditText view
                String sql = inputView.getText().toString();
                try {
                    //Prepare it to an executable statement
                    PreparedStatement preparedStatement = remoteConnection.prepareStatement(sql);

                    //If you want to execute more than one statement at a time,
                    //simply fill up successive array elements and return it:
                    PreparedStatement[] preparedStatements = new PreparedStatement[1];
                    preparedStatements[0] = preparedStatement;
                    return preparedStatements;
                } catch (SQLException e) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    outputView.setText(getString(R.string.error_occured) + '\n' + e.getLocalizedMessage() + '\n' + getString(R.string.see_log));
                    return null;
                }
            }
        };
        //This listener tells the database manager what to do when we receive the result of the query execution
        //We will be using this listener when the execute button is clicked
        final OnGetResultSetListener onGetResultSetListener = new OnGetResultSetListener() {
            @Override
            public void onGetResultSets(ResultSet[] resultSets, SQLException e) {
                if (e != null) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    outputView.setText(getString(R.string.error_occured) + '\n' + e.getLocalizedMessage() + '\n' + getString(R.string.see_log));
                } else if (resultSets.length > 0) {
                    //Since we executed only one query, the result will show up in index 0 of the array
                    ResultSet rs = resultSets[0];

                    int i = 0;
                    try {
                        //Build the output and display it in the TextView
                        StringBuffer stringBuffer = new StringBuffer("First 5 rows:\n");
                        while (rs.next() && i < 5) {//While there are rows and we still haven't displayed the first 5 rows
                            i++;
                            stringBuffer.append(rs.getString(1));
                            stringBuffer.append('\n');
                        }
                        //Always close the Result set when your done
                        rs.close();
                        //Finally display the rows
                        outputView.setText(stringBuffer);
                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        outputView.setText(e1.getLocalizedMessage());
                    }
                } else {
                    //This should never happen but if it does,
                    //log and display it
                    Log.e("Result", "Received no result sets from query");
                    outputView.setText(getString(R.string.no_result_sets));
                }
            }
        };

        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                outputView.setText(getString(R.string.loading));

                //If the URL has been edited, then we should reinitialize the AceQLDBManager with the new URL
                String newURL = serverURLView.getText().toString();
                String oldURL = AceQLDBManager.getServerUrl();
                if (!newURL.equals(oldURL)) {
                    //Null for any of the fields means that they wont be modified
                    //This statement will also cause the connection to be reset meaning the next query might take a little longer
                    AceQLDBManager.initialize(newURL, null, null);
                }

                //Save the query and url so that the user doesn't have to type it again next time.
                saveInputConfigurations();

                //Finally, execute the query by specifying what query (onPreparedStatementsListener),
                //And what to do once we get the result
                AceQLDBManager.executePreparedStatements(onPreparedStatementsListener, onGetResultSetListener);
            }
        });
    }

    private void saveInputConfigurations() {
        SharedPreferences.Editor editor = getSharedPreferences("sharedPrefs", MODE_PRIVATE).edit();
        editor.putString("sql", inputView.getText().toString());
        editor.putString("url", serverURLView.getText().toString());
        editor.apply();
    }

    private void restoreInputConfiguration() {
        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String previousQuery = sp.getString("sql", null);
        if (previousQuery != null)
            inputView.setText(previousQuery);
        String previousURL = sp.getString("url", null);
        if (previousURL != null)
            serverURLView.setText(previousURL);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}