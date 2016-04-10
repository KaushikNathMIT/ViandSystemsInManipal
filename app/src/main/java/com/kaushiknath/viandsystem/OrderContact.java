package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
 * Created by Kaushik Nath on 4/8/2016.
 */
public class OrderContact extends Activity {
    String sql;
    TextView land_no;
    TextView mob_no;
    TextView email_id;
    TextView credc;
    TextView debc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont_info);

        SwipeRefreshLayout sr2 = (SwipeRefreshLayout) findViewById(R.id.sr2);

        sr2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        sql = getIntent().getStringExtra("sql_q2");
        mob_no = (TextView) findViewById(R.id.mob);
        land_no = (TextView) findViewById(R.id.ll);
        email_id = (TextView) findViewById(R.id.eid);
        credc = (TextView) findViewById(R.id.cdc);
        debc = (TextView) findViewById(R.id.debc);

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
                        StringBuffer stringBuffer = new StringBuffer("Number Of Stars:\n");

                        while (resultSets.next()) {
                            mob_no.setText(mob_no.getText() + ":" + resultSets.getLong(3));
                            land_no.setText(land_no.getText().toString() + " : " + resultSets.getLong(4));
                            email_id.setText(email_id.getText().toString() + " : " + resultSets.getString(5));
                            debc.setText(debc.getText().toString() + " : " + resultSets.getString(14));
                            credc.setText(credc.getText().toString() + " : " + resultSets.getString(13));
                            Log.d("email_id", email_id.getText().toString());
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
