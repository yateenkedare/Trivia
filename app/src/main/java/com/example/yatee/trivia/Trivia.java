package com.example.yatee.trivia;
/*
    Assignment : HomeWork4
    StartActivity.java
    Yateen Kedare | Rajdeep Rao
 */
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Trivia extends AppCompatActivity {
    ArrayList<TriviaQuestion> triviaQuestionArray;
    int QuestingNumber = 0;
    ArrayAdapter adapter;
    RadioGroup rg;
    TextView Qnum,QuestionText,Timer;
    int[] answers;
    Button next;
    ImageView iv;
    private static final String FORMAT = "%02d:%02d";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);
        next = (Button) findViewById(R.id.next);
        Qnum = (TextView) findViewById(R.id.textView2);
        Timer = (TextView) findViewById(R.id.textView3);
        QuestionText = (TextView)findViewById(R.id.textView5);
        iv = (ImageView) findViewById(R.id.imageView);
        String questions =  getIntent().getStringExtra("QUESTIONS");
        parseJSON(questions);
        answers = new int[triviaQuestionArray.size()];
        Arrays.fill(answers,-1);
        rg = (RadioGroup) findViewById(R.id.RadigGroup);

        displayQuestion();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                answers[QuestingNumber] = (checkedId - QuestingNumber*100);
            }
        });
        Button prev = (Button) findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuestingNumber > 0) {
                    QuestingNumber--;
                    displayQuestion();
                    next.setText("Next");
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuestingNumber ==  triviaQuestionArray.size()-1) {
                    StartStatsActivity();
                }
                else {
                    QuestingNumber++;
                    displayQuestion();
                    if(QuestingNumber == triviaQuestionArray.size()-1) {
                        next.setText("Finish");
                    }
                }
            }
        });

        final CountDownTimer start = new CountDownTimer(121000, 1000) {

            public void onTick(long millisUntilFinished) {
                String timeLeft = String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Timer.setText(timeLeft);
            }

            public void onFinish() {
                StartStatsActivity();
            }

        }.start();
    }

    public void StartStatsActivity(){
        ArrayList<String> text = new ArrayList<>();
        ArrayList<String> wrongAnswers = new ArrayList<>();
        ArrayList<String> correctAnswers = new ArrayList<>();
        for(int i = 0; i < triviaQuestionArray.size(); i++) {
            if(answers[i] != Integer.parseInt(triviaQuestionArray.get(i).getAnswer())-1)
            {
                text.add(triviaQuestionArray.get(i).getText());
                if(answers[i] != -1)
                    wrongAnswers.add(triviaQuestionArray.get(i).getChoice().get(answers[i]));
                else
                    wrongAnswers.add("Not Answered");
                correctAnswers.add(triviaQuestionArray.get(i).getChoice().get(Integer.parseInt(triviaQuestionArray.get(i).getAnswer())-1));
            }
        }
        Intent intent = new Intent(this,StatActivity.class);
        intent.putStringArrayListExtra("TEXT",text);
        intent.putStringArrayListExtra("WRONG",wrongAnswers);
        intent.putStringArrayListExtra("CORRECT",correctAnswers);
        intent.putExtra("TOTAL",triviaQuestionArray.size());
        startActivity(intent);
        finish();
    }

    public void displayQuestion() {
        rg.removeAllViews();
        if(triviaQuestionArray.get(QuestingNumber).getImageURL()!=null)
        {
            findViewById(R.id.loadingP).setVisibility(View.VISIBLE);
            iv.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(triviaQuestionArray.get(QuestingNumber).getImageURL())
                    .into(iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            findViewById(R.id.loadingP).setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        else {
            iv.setImageDrawable(null);
            iv.setVisibility(View.GONE);
        }
        QuestionText.setText(triviaQuestionArray.get(QuestingNumber).getText());
        Qnum.setText("Q"+(QuestingNumber+1));

        for(int i = 0; i<triviaQuestionArray.get(QuestingNumber).getChoice().size(); i++)
        {
            RadioButton rdbtn = new RadioButton(this);
            rdbtn.setId(i + QuestingNumber*100);
            rdbtn.setText(triviaQuestionArray.get(QuestingNumber).getChoice().get(i));
            rg.addView(rdbtn);
        }
        if(answers[QuestingNumber] != -1) {
            Log.d("ReSelect :: ", String.valueOf((answers[QuestingNumber] + QuestingNumber * 100)));
            rg.check((answers[QuestingNumber] + QuestingNumber * 100));
        }
    }


    public Boolean parseJSON(String result){
        triviaQuestionArray =new ArrayList<TriviaQuestion>();
        try {
            JSONObject root=new JSONObject(result);
            JSONArray Questions=root.getJSONArray("questions");

            for(int i=0;i<Questions.length();i++){
                JSONObject question=Questions.getJSONObject(i);
                TriviaQuestion triviaQuestion=new TriviaQuestion();
                triviaQuestion.setId(question.getString("id"));
                triviaQuestion.setText(question.getString("text"));
                try{
                    triviaQuestion.setImageURL(question.getString("image"));
                }catch (JSONException e)
                {
                    triviaQuestion.setImageURL(null);
                }
                triviaQuestion.setChoices(question.getJSONObject("choices").getJSONArray("choice"));
                triviaQuestion.setAnswer(question.getJSONObject("choices").getString("answer"));

                triviaQuestionArray.add(triviaQuestion);
                Log.d("ArrayListNewsArticles",triviaQuestion.toString());
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}