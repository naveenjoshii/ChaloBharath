package com.example.hp.chalobharath;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class optionActivity extends AppCompatActivity {
Button Driver,Rider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Driver = (Button)findViewById(R.id.driver);
        Rider = (Button)findViewById(R.id.rider);
        Driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(optionActivity.this,MapActivity1.class));
                finish();
            }
        });
        Rider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(optionActivity.this,mapActivity.class));
               finish();
            }
        });
    }
}
