package com.example.yatee.trivia;
/*
    Assignment : HomeWork4
    StartActivity.java
    Yateen Kedare | Rajdeep Rao
 */
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class StatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        ArrayList<String> questions = getIntent().getStringArrayListExtra("TEXT");  //questions text
        ArrayList<String> wrongAnswers = getIntent().getStringArrayListExtra("WRONG");  //User answers Text
        ArrayList<String> correctAnswers = getIntent().getStringArrayListExtra("CORRECT"); //Correct answer Text
        int total = getIntent().getIntExtra("TOTAL", 0);

        float performance = (1 - (float) questions.size()/total)*100;
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar3);
        pb.setMax(100);
        pb.setProgress((int)performance);

        TextView tvPerformance= (TextView) findViewById(R.id.textView9);
        tvPerformance.setText((int)performance+"%");
        LinearLayout ll = (LinearLayout) findViewById(R.id.lladd);


        for(int i = 0; i < questions.size();i++){
            TextView questionTv = new TextView(this);
            TextView wrongTv= new TextView(this);
            TextView correctTV = new TextView(this);

            questionTv.setText(questions.get(i));
            wrongTv.setText(wrongAnswers.get(i));
            correctTV.setText(correctAnswers.get(i));

            wrongTv.setBackgroundColor(Color.RED);

            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0, 0, 0, 50); // llp.setMargins(left, top, right, bottom);
            correctTV.setLayoutParams(llp);
            ll.addView(questionTv);
            ll.addView(wrongTv);
            ll.addView(correctTV);
        }
        Log.d("Total Question:: ", String.valueOf(total));
        Log.d("Wrong Answers:: ", String.valueOf(questions.size()));
        Log.d("Performance:: ", String.valueOf(performance));

        final Button finish = (Button) findViewById(R.id.button);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
