package com.example.yatee.trivia;
/*
    Assignment : HomeWork4
    StartActivity.java
    Yateen Kedare | Rajdeep Rao
 */
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements GetQuestionAsync.IData{
    Button next;
    String QuestionJSONFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isConnectedOnline())
            new GetQuestionAsync(this).execute("http://dev.theappsdr.com/apis/trivia_json/index.php");
        else
            Toast.makeText(getApplicationContext(),"No Internet Connection", Toast.LENGTH_LONG).show();
        next = (Button) findViewById(R.id.start_trivia);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Trivia.class);
                intent.putExtra("QUESTIONS",  QuestionJSONFile);
                startActivity(intent);
            }
        });
        next.setEnabled(false);
        Button exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean isConnectedOnline(){
        ConnectivityManager cm=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void resultValues(String result) {
        QuestionJSONFile = result;
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        findViewById(R.id.textView4).setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.trivia_start_image);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.trivia);
        next.setEnabled(true);
    }


}
