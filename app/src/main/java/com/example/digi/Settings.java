package com.example.digi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ImageButton home;
        EditText settingOne, settingTwo, settingThree, settingFour;
        Button saveButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        home = findViewById(R.id.home_button);
        settingOne = findViewById(R.id.setting1);
        settingTwo = findViewById(R.id.setting2);
        settingThree = findViewById(R.id.setting3);
        settingFour = findViewById(R.id.setting4);
        saveButton = findViewById(R.id.save_settings);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(Settings.this, MainActivity.class);
                startActivity(goHome);
            }
        });
    }
}