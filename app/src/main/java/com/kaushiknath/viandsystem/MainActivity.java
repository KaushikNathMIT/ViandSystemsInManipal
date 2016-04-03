package com.kaushiknath.viandsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.kawanfw.sql.api.client.android.AceQLDBManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Gets the AceQL server URL to connect to
    EditText serverURLView;
    //Gets the SQL query to execute
    //Tap it to execute query
    Button loginB, spButton;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    int hd =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.login) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else if (id == R.id.hodel) {
                    Toast.makeText(getApplicationContext(), "Only Hotels with home delivery option will be showed", Toast.LENGTH_LONG);
                    hd = 1;
                } else if (id == R.id.fbname) {
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("val", hd);
                    startActivity(intent);
                }

                return false;
            }
        });

        mDrawerLayout.closeDrawers();


        //Find the view from the layout XML file
        serverURLView = (EditText) findViewById(R.id.et_server_url);
        loginB = (Button) findViewById(R.id.b_login);
        spButton = (Button) findViewById(R.id.sel_proc_button);

        //Restore the previously entered values into the fields if any for those lazy folks
        restoreInputConfiguration();

        //This listener tells the database manager what kind of statements to execute
        //We will be using this listener when the execute button is clicked
        //Set what to do when the execute button is clicked
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Let the user know that the process has begun
                //outputView.setText(getString(R.string.loading));

                //If the URL has been edited, then we should reinitialize the AceQLDBManager with the new URL
                String newURL = serverURLView.getText().toString();
                String oldURL = AceQLDBManager.getServerUrl();
                if (!newURL.equals(oldURL)) {
                    //Null for any of the fields means that they wont be modified
                    //This statement will also cause the connection to be reset meaning the next query might take a little longer
                    AceQLDBManager.initialize(newURL, null, null);
                }

                //Save the query and url so that the user doesn't have to type it again next time.
                saveInputConfigurations();
            }
        });

        spButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SelectionProcess.class);
                intent.putExtra("val",hd);
                startActivity(intent);
            }
        });


    }

    private void saveInputConfigurations() {
        SharedPreferences.Editor editor = getSharedPreferences("sharedPrefs", MODE_PRIVATE).edit();
        editor.putString("url", serverURLView.getText().toString());
        editor.apply();
    }

    private void restoreInputConfiguration() {
        SharedPreferences sp = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String previousURL = sp.getString("url", null);
        if (previousURL != null)
            serverURLView.setText(previousURL);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}