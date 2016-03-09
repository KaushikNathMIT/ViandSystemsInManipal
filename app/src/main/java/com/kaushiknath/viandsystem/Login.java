package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.DriverManager;

/**
 * Created by Kaushik Nath on 09-Mar-16.
 */
public class Login extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        TextView tv1 = (TextView) findViewById(R.id.editText);
        Button con = (Button) findViewById(R.id.button);
        final String url = "jdbc:aceql:http://192.168.55.1:9090/ServerSqlManager";
        Button cont = (Button)findViewById(R.id.button);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,MainActivity.class);
                intent.putExtra("Name1",url);
                startActivity(intent);
                finish();
            }
        });

    }
}
