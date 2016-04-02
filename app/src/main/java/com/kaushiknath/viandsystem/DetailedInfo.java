package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Kaushik Nath on 4/2/2016.
 */
public class DetailedInfo extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.det_info);
        TextView sel = (TextView) findViewById(R.id.sel_hotel);
        String se = getIntent().getStringExtra("Selected");
        sel.setText(se);
    }
}
