package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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
 * Created by Kaushik Nath on 4/2/2016.
 */
public class DetailedInfo extends AppCompatActivity {
    String sql, se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.det_info);


        setTitle("Location and cuisine Details");
        setTitleColor(Color.parseColor("#ffffff"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);


        TextView sel = (TextView) findViewById(R.id.sel_hotel);
        se = getIntent().getStringExtra("Selected");
        sel.setText(se);

        sql = "select rating,loc_name,pincode,cuisine from info_table natural join loc_table natural join whereabouts natural join type " +
                "where name = '" + se + "'";
        final RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
        final TextView location = (TextView) findViewById(R.id.loca);
        final TextView pincode = (TextView) findViewById(R.id.pc);
        final Button order = (Button) findViewById(R.id.order_button);
        final TextView cuisine = (TextView) findViewById(R.id.cuisine);

        SwipeRefreshLayout sr4 = (SwipeRefreshLayout) findViewById(R.id.sr4);

        sr4.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
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
                        StringBuffer stringBuffer = new StringBuffer("Number Of Stars:\n");

                        while (resultSets.next()) {
                            rating.setRating(resultSets.getFloat(1));
                            rating.getProgressDrawable().setColorFilter(Color.parseColor("#0064A8"), PorterDuff.Mode.SRC_ATOP);
                            rating.setEnabled(false);
                            location.setText(location.getText().toString() + " : " + resultSets.getString(2));
                            pincode.setText(pincode.getText().toString() + " : " + resultSets.getInt(3));
                            cuisine.setText(cuisine.getText().toString() + ":" + resultSets.getString(4));
                            Log.d("pincode", pincode.getText().toString());
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

        order.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedInfo.this, OrderContact.class);
                String sql_q2 = "select * from contact natural join info_table natural join services natural join payment_option where name like '" + se + "'";
                intent.putExtra("sql_q2", sql_q2);
                startActivity(intent);
            }
        });

    }

}
