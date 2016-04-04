package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Kaushik Nath on 4/4/2016.
 */
public class Staff extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staff_info);

        String sel_name = getIntent().getStringExtra("sel_name");
        TextView sel_hot_name = (TextView) findViewById(R.id.sta_text);
        sel_hot_name.setText(sel_name);
    }
}
