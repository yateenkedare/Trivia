package com.example.yatee.trivia;

import android.util.Log;
/*
    Assignment : HomeWork4
    StartActivity.java
    Yateen Kedare | Rajdeep Rao
 */

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;



public class TriviaQuestion{
    private String id, text, imageURL, answer;
    private JSONArray choices;
    private ArrayList<String> choice = new ArrayList<>();
    public JSONArray getChoices() {
        return choices;
    }

    public ArrayList<String> getChoice() {
        return choice;
    }

    public void setChoices(JSONArray choices) throws JSONException {
        this.choices = choices;
        for(int i = 0; i < choices.length(); i++){
            choice.add(String.valueOf(choices.get(i)));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString(){
        return "ID:: "+id+"  text:: "+text ;
    }
}
