package com.kaushiknath.viandsystem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Kaushik Nath on 19-Mar-16.
 */

public class T_List extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    //String[] dataArray = new String[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int length = getIntent().getIntExtra("Number",0);
        String dataArray1[] = getIntent().getStringArrayExtra("res");
        int rates[] = getIntent().getIntArrayExtra("Rates");

        String[] dataArray = new String[length];
        int i;
        for(i=0;i<length;i++) {
            dataArray[i]=dataArray1[i];
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        final TextView textView = (TextView) findViewById(R.id.textView3);
        //selview = (RecyclerView) findViewById(R.id.recycler_selected);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(dataArray, rates);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed(){
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
