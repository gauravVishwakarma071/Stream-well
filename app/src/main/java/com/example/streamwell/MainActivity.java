package com.example.streamwell;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button goliveBtn;
    TextInputEditText liveIDInput, nameIdInnput;

    String liveID,name,userID;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("name_pref",MODE_PRIVATE);

        goliveBtn = findViewById(R.id.go_live_btn);
        liveIDInput = findViewById(R.id.live_id_input);
        nameIdInnput = findViewById(R.id.name_input);

        nameIdInnput.setText(sharedPreferences.getString("name",""));

        liveIDInput.addTextChangedListener(new TextWatcher() { // adding text feature at button to display.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                liveID = liveIDInput.getText().toString();
                if(liveID.length()==0){
                    goliveBtn.setText("Start new live");
                }else{
                    goliveBtn.setText("Join a live");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        goliveBtn.setOnClickListener(view -> {
            name = nameIdInnput.getText().toString();

            if(name.isEmpty()){
                nameIdInnput.setError("Name is required");
                nameIdInnput.requestFocus();
                return;
            }

            liveID = liveIDInput.getText().toString();

            if(liveID.length()>0 && liveID.length()!=5){
                liveIDInput.setError("Invalid LIVE ID");
                liveIDInput.requestFocus();
                return;
            }
            startMeeting();

        });
    }

    void startMeeting(){
        Log.i("LOG","Start Meeting");
        sharedPreferences.edit().putString("name",name).apply();

        boolean isHost = true;
        if(liveID.length()==5)
            isHost = false;
        else 
            liveID = generateliveID();
        userID = UUID.randomUUID().toString(); // generate random user Id

        Intent intent = new Intent(MainActivity.this,LiveActivity.class);
        intent.putExtra("user_id",userID);
        intent.putExtra("name",name);
        intent.putExtra("live_id",liveID);
        intent.putExtra("host",isHost);
        startActivity(intent);
    }

     String generateliveID() { // to create random Id
        StringBuilder id = new StringBuilder();
        while (id.length()!=5){
            int random = new Random().nextInt(10);
            id.append(random);
        }
        return id.toString();
    }
}