package com.bustrackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChoseBusLocationActivity extends AppCompatActivity {


    Spinner routeSpinner;

    Button submit;

    String routeValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_dropdown);

        routeSpinner=findViewById(R.id.route_spinner);
        submit=findViewById(R.id.submit_button);

        String[] items=new String[]{"Tiruvotriyur to Poonamallee","Tiruvotriyur to CMBT","potheri to tambaram"};

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);

        routeSpinner.setAdapter(arrayAdapter);
        routeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                routeValue=(String) parent.getItemAtPosition(position);
                Log.i("routevalue","routevalue "+routeValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChoseBusLocationActivity.this,MainActivity.class);
                intent.putExtra("route_value",routeValue);
                startActivity(intent);
            }
        });
    }
}
