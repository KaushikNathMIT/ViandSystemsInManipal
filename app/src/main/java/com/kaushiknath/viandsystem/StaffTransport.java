package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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
public class StaffTransport extends Activity {
    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_trans);
        String sel_name = getIntent().getStringExtra("sel_name");
        TextView sel_hot_name = (TextView) findViewById(R.id.sta_text);
        sel_hot_name.setText(sel_name);

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
                        StringBuffer stringBuffer = new StringBuffer("Number Of Stars:\n");

                        while (resultSets.next()) {
                            /*rating.setRating(resultSets.getFloat(1));
                            rating.getProgressDrawable().setColorFilter(Color.parseColor("#0064A8"), PorterDuff.Mode.SRC_ATOP);
                            rating.setEnabled(false);
                            location.setText(location.getText().toString() + " : " + resultSets.getString(2));
                            pincode.setText(pincode.getText().toString() + " : " + resultSets.getInt(3));
                            Log.d("pincode", pincode.getText().toString());*/
                            Log.d("result last",resultSets.getString(11));
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
