package com.example.anujsharma.shuffler.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anujsharma.shuffler.R;

public class AddSongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);
        getSupportActionBar().setTitle("Add Song");
        getSupportActionBar().setElevation(0);
    }
}
