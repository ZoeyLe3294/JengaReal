package com.example.jenga;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddQuote extends AppCompatActivity {
    public EditText content, timeSet;
    public CheckBox dice,timerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        content = (EditText) findViewById(R.id.quote);
        timeSet = (EditText) findViewById(R.id.time_set);
        dice = (CheckBox) findViewById(R.id.dice);

        timerBtn = (CheckBox) findViewById(R.id.timerBtn);
        timerBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    timeSet.setVisibility(View.VISIBLE);
                }else {
                    timeSet.setVisibility(View.INVISIBLE);
                    timeSet.setText("");
                }
            }
        });
    }
    public void submitAdd(View view){
        if (content.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please insert content!",Toast.LENGTH_LONG).show();
        }else {
            String seconds = timeSet.getText().toString();
            if (seconds.equals("")){
                seconds = "0";
            }
            Intent addIntent = getIntent();

            Bundle myBundle = new Bundle();
            myBundle.putString("quote", content.getText().toString());
            myBundle.putBoolean("diceChecked",dice.isChecked());
            myBundle.putString("seconds", seconds);

            addIntent.putExtra("addBundle",myBundle);
            setResult(JengaActivity.RESULT_CODE_ADD, addIntent);

            finish();
        }
    }
    public void back(View view){
        finish();
    }

}
