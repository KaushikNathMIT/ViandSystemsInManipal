package com.kaushiknath.viandsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.kawanfw.sql.api.client.android.AceQLDBManager;
import org.kawanfw.sql.api.client.android.BackendConnection;
import org.kawanfw.sql.api.client.android.execute.OnGetPrepareStatement;
import org.kawanfw.sql.api.client.android.execute.query.OnGetResultSetListener;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Kaushik Nath on 19-Mar-16.
 */

public class T_List extends AppCompatActivity {
    String[] res = new String[40];
    int[] range = new int[40];
    float[] ran = new float[40];
    String[] cat = new String[40];
    String sql;
    int length;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sql = getIntent().getStringExtra("sql_q");
        Log.d("sql_query  is", sql);

        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.sr1);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                    Toast.makeText(getApplicationContext(), (getString(R.string.error_occured) + '\n' + "this" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log)), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), (getString(R.string.error_occured) + '\n' + "that" + e.getLocalizedMessage() + '\n' + getString(R.string.see_log)), Toast.LENGTH_LONG).show();
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
                            cat[i] = resultSets.getString(4);
                            Log.d("Length Here", Integer.toString(res[i].length()));
                            i++;
                        }
                        length = i;
                        Log.e("Status", "reached 2");
                        //Toast.makeText(getApplicationContext(), "Actual Length" + Integer.toString(length),Toast.LENGTH_SHORT).show();
                        //Always close the Result set when your done
                        resultSets.close();
                        setView(length);


                    } catch (SQLException e1) {
                        //Log and display any error that occurs
                        e1.printStackTrace();
                        Toast.makeText(getApplicationContext(), (e1.getLocalizedMessage()), Toast.LENGTH_LONG).show();
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

        AceQLDBManager.executeQuery(onGetPrepareStatements, onGetResultSetListener);





        //Toast.makeText(getApplicationContext(), "Setting View", Toast.LENGTH_LONG).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //final TextView textView = (TextView) findViewById(R.id.textView3);
        //selview = (RecyclerView) findViewById(R.id.recycler_selected);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        //Toast.makeText(getApplicationContext(), "Putting View", Toast.LENGTH_LONG).show();

    }

    private void setView(int length) {

        String[] dataArray = new String[length];
        int i;
        for (i = 0; i < length; i++) {
            dataArray[i] = res[i];
        }

        adapter = new RecyclerAdapter(getApplicationContext(), dataArray, range, ran, cat);
        recyclerView.setAdapter(adapter);

        //Toast.makeText(getApplicationContext(), Integer.toString(dataArray.length), Toast.LENGTH_LONG).show();
        Log.d("Length now is", Integer.toString(dataArray.length));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
