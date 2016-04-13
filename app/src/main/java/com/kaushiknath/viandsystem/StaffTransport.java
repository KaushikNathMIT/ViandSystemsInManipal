package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
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
public class StaffTransport extends AppCompatActivity {
    String sql;
    TextView mgr, wai, che, ste, vans, bikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_trans);
        String sel_name = getIntent().getStringExtra("sel_name");
        TextView sel_hot_name = (TextView) findViewById(R.id.sta_text);
        sel_hot_name.setText(sel_name);

        setTitleColor(Color.parseColor("#ffffff"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        SwipeRefreshLayout sr3 = (SwipeRefreshLayout) findViewById(R.id.sr3);

        sr3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        mgr = (TextView) findViewById(R.id.mgr);
        wai = (TextView) findViewById(R.id.wai);
        che = (TextView) findViewById(R.id.che);
        ste = (TextView) findViewById(R.id.ste);
        vans = (TextView) findViewById(R.id.vans);
        bikes = (TextView) findViewById(R.id.bikes);

        sql = "select * from info_table natural join services natural join transport natural join staff where name like '" + sel_name + "'";


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
        //This listener tells the database manager what to do when we receive the result of the query execution
        //We will be using this listener when the execute button is clicked

        final OnGetResultSetListener onGetResultSetListener = new OnGetResultSetListener() {
            @Override
            public void onGetResultSet(ResultSet resultSets, SQLException e) {
                if (e != null) {
                    //Log and display any error that occurs
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_occured) + '\n' + "that" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log), Toast.LENGTH_LONG).show();
                    Log.e("Status", "reached 3");
                } else if (true) {

                    int i = 0;
                    try {
                        //Build the output and display it in the TextView

                        while (resultSets.next()) {
                            mgr.setText(mgr.getText().toString() + " : " + resultSets.getString(11));
                            wai.setText(wai.getText().toString() + " : " + resultSets.getInt(12));
                            che.setText(che.getText().toString() + " : " + resultSets.getInt(13));
                            ste.setText(ste.getText().toString() + " : " + resultSets.getInt(14));
                            vans.setText(vans.getText().toString() + " : " + resultSets.getInt(9));
                            bikes.setText(bikes.getText().toString() + " : " + resultSets.getInt(10));
                        }
                        int length = i;
                        Log.e("Status", "reached 2");
                        resultSets.close();

                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        Toast.makeText(getApplicationContext(), e1.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Status", "reached 11");
                    }
                }
            }
        };
        Toast.makeText(getApplicationContext(), getString(R.string.loading), Toast.LENGTH_LONG).show();
        AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);
    }
}
