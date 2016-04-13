package com.kaushiknath.viandsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button spButton, sel_hot_button;
    int hd = 0;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        spButton = (Button) findViewById(R.id.sel_proc_button);
        sel_hot_button = (Button) findViewById(R.id.fbname);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.login) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else if (id == R.id.hodel) {
                    if (item.isChecked()) {
                        hd = 0;
                        item.setChecked(false);
                        item.setIcon(android.R.drawable.checkbox_off_background);
                        Toast.makeText(getApplicationContext(), R.string.hd_sel_n, Toast.LENGTH_LONG).show();
                    } else {
                        hd = 1;
                        item.setChecked(true);
                        item.setIcon(android.R.drawable.checkbox_on_background);
                        Toast.makeText(getApplicationContext(), R.string.hd_sel, Toast.LENGTH_LONG).show();
                    }
                } else if (id == R.id.change_ip) {
                    startActivity(new Intent(MainActivity.this, ServerConnect.class));
                } else if (id == R.id.dandc) {
                    startActivity(new Intent(MainActivity.this, Developer.class));
                }
                else if(id == R.id.vid){
                    startActivity(new Intent(MainActivity.this,VideoV.class));
                }

                return false;
            }
        });

        mDrawerLayout.closeDrawers();


        //Find the view from the layout XML file


        spButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectionProcess.class);
                intent.putExtra("val", hd);
                startActivity(intent);
            }
        });

        sel_hot_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("val", hd);
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}