package com.example.finalyearproject117477692;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final EditText etEnterAmount = findViewById(R.id.etEnterAmount);
        final Spinner spDistanceType = findViewById(R.id.spDistanceType);
        Button btnConvert = findViewById(R.id.btnConvert);
        final TextView tvResult = findViewById(R.id.tvResult);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etEnteredDistanceType = String.valueOf(etEnterAmount.getText());
                double convertedValue, finalValue;

                if(etEnteredDistanceType.isEmpty()){
                    etEnterAmount.setError("Error");
                    Toast.makeText(SecondActivity.this, "Please enter a distance amount", Toast.LENGTH_SHORT).show();
                }else{
                    final double distanceValue = Double.parseDouble(String.valueOf(etEnterAmount.getText()));
                    switch (spDistanceType.getSelectedItemPosition()){
                        case 0:

                            Toast.makeText(SecondActivity.this, "Please select a distance type", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            convertedValue = convertToKm(distanceValue);
                            finalValue = Math.round(convertedValue * 100.0)/100.0;
                            tvResult.setText(etEnteredDistanceType + " Miles is\n" + String.valueOf(finalValue) + " Kilometres");
                            tvResult.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            convertedValue = convertToMiles(distanceValue);
                            finalValue = Math.round(convertedValue * 100.0)/100.0;
                            tvResult.setText(etEnteredDistanceType + " Kilometres is\n" + String.valueOf(finalValue) + " Miles");
                            tvResult.setVisibility(View.VISIBLE);
                            break;
                    }

                }
            }
        });
    }
    public double convertToKm(double x){
        double resultValue;
        resultValue = (x * 1.60934);
        return resultValue;
    }
    public double convertToMiles(double y){
        double resultValue;
        resultValue = (y * 0.621371);
        return resultValue;
    }

}