package com.example.iotproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Dashbord extends AppCompatActivity {
    ImageView sound,lumiere,temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        sound =findViewById(R.id.sound);
        lumiere=findViewById(R.id.lum);
        temperature=findViewById(R.id.temp);


        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashbord.this, Sound.class);
                startActivity(i);
            }
        });
        lumiere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashbord.this, MainActivity.class);
                startActivity(i);

            }
        });
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashbord.this, Temp.class);
                startActivity(i);

            }
        });


    }
}
