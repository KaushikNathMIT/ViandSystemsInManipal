package com.kaushiknath.viandsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.kawanfw.sql.api.client.android.AceQLDBManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Gets the AceQL server URL to connect to
    EditText serverURLView;
    //Gets the SQL query to execute
    //Tap it to execute query
    Button loginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view from the layout XML file
        serverURLView = (EditText) findViewById(R.id.et_server_url);
        loginB = (Button) findViewById(R.id.b_login);

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
                startActivity(new Intent(MainActivity.this, SelectionProcess.class));
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