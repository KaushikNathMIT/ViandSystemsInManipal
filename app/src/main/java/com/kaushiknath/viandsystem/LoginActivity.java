package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
public class LoginActivity extends AppCompatActivity {
    String sql;
    String[] res = new String[40];
    int length;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logina);
        setTitleColor(Color.parseColor("#ffffff"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);


        final EditText uname = (EditText) findViewById(R.id.log_uname);
        final EditText pass = (EditText) findViewById(R.id.log_pass);
        Button log_admin = (Button) findViewById(R.id.admin_login);


        sql = "select name from info_table";





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
                    Log.e("Status", "reached 3");
                } else if (true) {
                    //Since we executed only one query, the result will show up in index 0 of the array
                    //ResultSet rs = resultSets[0];

                    int i = 0;
                    try {
                        //Build the output and display it in the TextView

                        while (resultSets.next()) {//While there are rows and we still haven't displayed the first 5 rows
                            res[i] = resultSets.getString(1);
                            Log.d("result_name",res[i]);
                            i++;
                        }
                        length = i;
                        Log.e("Status", "reached 2");
                        //Always close the Result set when your done
                        resultSets.close();
                        //Finally display the rows

                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        Log.e("Status", "reached 11");
                    }
                }
            }
        };
        AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);


        log_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((uname.getText().toString().equals(getString(R.string.orig_uname))) && (pass.getText().toString().equals(getString(R.string.orig_pass)))) {

                    Intent intent = new Intent(LoginActivity.this, ManagerChoice.class);
                    intent.putExtra("hot_list", res);
                    intent.putExtra("num_hot", length);
                    for(int i=0;i<length;i++) Log.d("sdsd",res[i]);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.auth_err, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
