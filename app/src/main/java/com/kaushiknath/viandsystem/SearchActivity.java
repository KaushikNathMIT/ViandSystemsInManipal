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

    String sql;
    int hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_search);

        hd = getIntent().getIntExtra("val", 0);

        final EditText hotser = (EditText) findViewById(R.id.hotel_search_text);


        executeB = (Button) findViewById(R.id.button2);
        //Set what to do when the execute button is clicked
        executeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                Toast.makeText(getApplicationContext(), (getString(R.string.loading)), Toast.LENGTH_LONG).show();
                StringBuffer sqlbuffer = new StringBuffer("");
                sqlbuffer.append("select name,range,h_d_radius from info_table natural join services natural join home_delivery " +
                        "where name like '%" + hotser.getText() + "%'");
                if (hd == 1) {
                    sqlbuffer.append(" and h_d_radius > 0");
                }
                sql = sqlbuffer.toString();
                Intent intent = new Intent(SearchActivity.this, T_List.class);
                intent.putExtra("sql_q", sql);
                startActivity(intent);

            }
        });
    }

}
