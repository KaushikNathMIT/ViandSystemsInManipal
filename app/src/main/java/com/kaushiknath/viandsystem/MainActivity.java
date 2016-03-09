package com.kaushiknath.viandsystem;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Driver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final String url = getIntent().getStringExtra("Name1");
        //final String url = "jdbc:aceql:http://"+ip+":9090/ServerSqlManager";
        // The login info for strong authentication on server side.
        // These are *not* the username/password of the remote JDBC Driver,
        // but are the auth info checked by remote
        // CommonsConfigurator.login(username, password) method.
        //final TextView textView2 = (TextView) findViewById((R.id.textView3));
        //final TextView textView1 = (TextView) findViewById(R.id.textView2);

        new MyTask().execute(url);

    }

    private class MyTask extends AsyncTask<String, Integer, java.sql.Connection> {

        @Override
        protected java.sql.Connection doInBackground(String... params) {

            String url = params[0];

            int flag = 0;
            final String username = "username";
            final String password = "password";
            try {
                Class.forName("org.kawanfw.sql.api.client.RemoteDriver");    // Attempts to establish a connection to the remote database:
                //textView2.setText("Class Connected");
            } catch (Exception e) {
                //return "class Not recognized";
            }
            try {
                java.sql.Connection connection = DriverManager.getConnection(url, username, password);
                if (connection != null) {
                    return connection;
                }
            } catch (Exception e) {
                //textView1.setText("Connection with remote Driver Failed \n The error is " + e + "\n The url is " + url);

            }
            //if(flag == 1) return "Connected";
            return null;
        }

        @Override
        protected void onPostExecute(final java.sql.Connection connection) {
            if (connection != null) {
                Log.d("status", "I am Connected");
                final TextView tv2 = (TextView) findViewById(R.id.textView2);
                tv2.setText("Connection Established");
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            PreparedStatement ps = connection.prepareStatement("select count(*) from info_table");
                            final ResultSet rs = ps.executeQuery();
                            while(rs.next()) {
                                TextView tv3 = (TextView) findViewById(R.id.textView3);
                                try {
                                    Log.d("Count is", String.valueOf(rs.getInt("COUNT(*)")));
                                }
                                catch(Exception e){
                                    Log.d("Error",e.toString());
                                }
                            }

                        } catch (SQLException e) {
                            //tv2.setText(e.toString());
                        }
                    }
                });
                thread1.start();
                super.onPostExecute(connection);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public java.sql.Connection connect(String url, Properties info) throws SQLException {
        return null;
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }
}