package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.button.MaterialButton;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView resultTv,solutionTv;
    MaterialButton buttonC,buttonBrackOpen,buttonBrackClose;
    MaterialButton buttonDivide,buttonMultiply,buttonPlus,buttonMinus,buttonEquals;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC,buttonDot;
    MaterialButton history;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference historyRef = database.getReference("calculator_history");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTv=findViewById(R.id.result_tv);
        solutionTv=findViewById(R.id.solution_tv);
        assignId(buttonC,R.id.button_c);
        assignId(buttonBrackOpen,R.id.button_open_bracket);
        assignId(buttonBrackClose,R.id.button_close_bracket);
        assignId(buttonDivide,R.id.button_divide);
        assignId(buttonMultiply,R.id.button_multiply);
        assignId(buttonPlus,R.id.button_plus);
        assignId(buttonMinus,R.id.button_minus);
        assignId(buttonEquals,R.id.button_equals);
        assignId(button0,R.id.button_0);
        assignId(button1,R.id.button_1);
        assignId(button2,R.id.button_2);
        assignId(button3,R.id.button_3);
        assignId(button4,R.id.button_4);
        assignId(button5,R.id.button_5);
        assignId(button6,R.id.button_6);
        assignId(button7,R.id.button_7);
        assignId(button8,R.id.button_8);
        assignId(button9,R.id.button_9);
        assignId(buttonAC,R.id.button_ac);
        assignId(buttonDot,R.id.button_dot);
        assignId(history,R.id.button_history);
    }


    void assignId(MaterialButton btn,int id){
        btn=findViewById(id);
        btn.setOnClickListener(this);
    }
//    CalculationHistoryItem calculationHistoryItem=new CalculationHistoryItem();
    @Override
    public void onClick(View v) {
        MaterialButton button =(MaterialButton) v;
        String buttonText = button.getText().toString();
        String dataToCalc = solutionTv.getText().toString();
        if (buttonText.equals("History")){
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
            return;
        }

        if ((buttonText.equals("+")||buttonText.equals("-")||buttonText.equals("*")||buttonText.equals("/"))&& resultTv.getText().toString().equals("")){
            Toast.makeText(MainActivity.this, "Invalid format used", Toast.LENGTH_SHORT).show();
            return;
        }
        if(buttonText.equals("AC")){
            solutionTv.setText("");
            resultTv.setText("");
            return;
        }
        if(buttonText.equals("=")) {
            solutionTv.setText(resultTv.getText());

            String finalResult = getResult(dataToCalc);

            if (!finalResult.equals("Err")) {
                CalculationHistoryItem historyItem = new CalculationHistoryItem(dataToCalc, finalResult);
                historyRef.push().setValue(historyItem);
            }
            return;
        }
        if(buttonText.equals("C")&&!solutionTv.getText().toString().equals("")){
            dataToCalc = dataToCalc.substring(0,dataToCalc.length()-1);
        }else if(!buttonText.equals("C")){
            dataToCalc = dataToCalc+buttonText;
        }
        else {return;}
        solutionTv.setText(dataToCalc);

        String finalResult = getResult(dataToCalc);

        if(!finalResult.equals("Err")){
            resultTv.setText(finalResult);
        }


    }

    String getResult(String data){
        try {
            Context context=Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable=context.initSafeStandardObjects();
            String finalResult =context.evaluateString(scriptable,data,"Javascript",1 ,null).toString();
            if (finalResult.endsWith(".0")){
                finalResult=finalResult.replace(".0","");
            }
            return finalResult;
        }catch (Exception e){
            return "Err";
        }
    }
}