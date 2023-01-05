package com.example.tp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Check extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        String challenge1 = getIntent().getStringExtra("challenge1");
        String challenge2 = getIntent().getStringExtra("challenge2");
        ((TextView) findViewById(R.id.challenge1_check)).setText(challenge1);
        ((TextView) findViewById(R.id.challenge2_check)).setText(challenge2);


        ((Button) findViewById(R.id.buttonok)).setOnClickListener(v->{
            this.check();
        });


    }
    private void check(){
        Intent intent = new Intent();
        setResult(78,intent);
        intent.putExtra("result", ((EditText) findViewById(R.id.edittextcheck)).getText().toString());
        super.onBackPressed();
    }
}