package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushik Nath on 4/4/2016.
 */
public class ManagerChoice extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String sel_name;
    int ida = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Toast.makeText(getApplicationContext(), R.string.log_succ, Toast.LENGTH_LONG).show();
        setContentView(R.layout.manager);
        setTitleColor(Color.parseColor("#ffffff"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_1);
        setSupportActionBar(toolbar);

        SwipeRefreshLayout srl = (SwipeRefreshLayout) findViewById(R.id.sr6);

        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        String[] hotlist = getIntent().getStringArrayExtra("hot_list");
        int length = getIntent().getIntExtra("num_hot",0);
        Log.d("len_hotlist",Integer.toString(length));

        Spinner spinner = (Spinner) findViewById(R.id.spin_name);
        spinner.setOnItemSelectedListener(this);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.animate();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerChoice.this,AddHotel.class));
            }
        });

        List<String> categories = new ArrayList<String>();
        for(int i=0;i<length;i++) {
            categories.add(hotlist[i]);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sel_name = parent.getItemAtPosition(position).toString();
        if(ida == 1) {
            Intent intent = new Intent(ManagerChoice.this, StaffTransport.class);
            intent.putExtra("sel_name", sel_name);
            startActivity(intent);
        }
        ida =1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
