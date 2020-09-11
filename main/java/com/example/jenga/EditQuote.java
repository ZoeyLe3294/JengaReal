package com.example.jenga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditQuote extends AppCompatActivity {
    public EditText content,timeSet;
    public CheckBox dice, timerBtn;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        content = (EditText) findViewById(R.id.text);
        timeSet = (EditText) findViewById(R.id.editTextTime);
        dice = (CheckBox) findViewById(R.id.diceBtn);
        timerBtn = (CheckBox) findViewById(R.id.timeBtn);

        ListItem item = (ListItem) getIntent().getSerializableExtra("item");
        id = item.getId();
        content.setText(item.getContent());
        dice.setChecked(item.isDiceChecked());
        if (item.getSeconds()>0){
            timerBtn.setChecked(true);
            timeSet.setText(String.valueOf(item.getSeconds()));
            timeSet.setVisibility(View.VISIBLE);
        }else {
            timerBtn.setChecked(false);
            timeSet.setVisibility(View.INVISIBLE);
        }
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
    public void submitEdit(View view){

        Intent intent = getIntent();
        switch (view.getId()){
            case R.id.edit_btn:
                if (content.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please insert content!",Toast.LENGTH_LONG).show();
                }else {
                    String seconds = timeSet.getText().toString();
                    if (seconds.equals("")) {
                        seconds = "0";
                    }
                    Bundle myBundle = new Bundle();
                    myBundle.putString("quote", content.getText().toString());
                    myBundle.putBoolean("diceChecked", dice.isChecked());
                    myBundle.putString("seconds", seconds);

                    intent.putExtra("editBundle", myBundle);
                    setResult(JengaActivity.RESULT_CODE_EDIT, intent);
                    finish();
                }
                break;
            case R.id.delete_btn:
                setResult(JengaActivity.RESULT_CODE_DELETE, intent);
                finish();
                break;
        }
    }
}
