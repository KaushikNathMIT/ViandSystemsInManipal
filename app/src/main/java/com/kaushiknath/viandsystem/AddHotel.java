package com.kaushiknath.viandsystem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Created by Kaushik Nath on 4/11/2016.
 */
public class AddHotel extends Activity {

    EditText name, id, catg, ran;
    Button add;
    RatingBar rat;
    String nam,cat,sql;
    int idi,range;
    float rate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addhotel);

        id = (EditText) findViewById(R.id.eidh);
        name = (EditText) findViewById(R.id.name);
        ran = (EditText) findViewById(R.id.range);
        rat = (RatingBar) findViewById(R.id.rate);
        catg = (EditText) findViewById(R.id.cat);
        add = (Button) findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nam = name.getText().toString();
                idi = Integer.parseInt(id.getText().toString());
                range = Integer.parseInt(ran.getText().toString());
                rate = rat.getRating();
                cat = catg.getText().toString();
                sql = "Insert into info_table values(" + idi + ", '" + nam + "', sysdate, '" + cat + "', " + range + ", " + Float.toString(rate) + ")";
                Log.d("Updation Query", sql);
            }
        });

    }
}
