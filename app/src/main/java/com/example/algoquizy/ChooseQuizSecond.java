package com.example.algoquizy;

import static java.lang.Math.min;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseQuizSecond extends AppCompatActivity {
    ImageButton breturn;
    Button quiz, bnext, bprevious;
    MediaPlayer mediaPlayer;
    boolean cmusic = true, csounds;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choose_quiz_second);
        Intent intent = getIntent();
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        breturn = findViewById(R.id.returnButton);
        breturn.setOnClickListener(this::OnClick);
        bnext = findViewById(R.id.next);
        bnext.setOnClickListener(this::OnClick);
        bprevious = findViewById(R.id.previous);
        bprevious.setOnClickListener(this::OnClick);
        mediaPlayer = MediaPlayer.create(ChooseQuizSecond.this, R.raw.algoquizy_thinking_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(!cmusic){
            mediaPlayer.stop();
        }
        JSONObject array = null;
        try {
            array = new JSONObject(loadContentFromFile(ChooseQuizSecond.this, "db.json"));
        } catch (JSONException e) {
            System.out.println("XDDDDD");
            e.printStackTrace();
        }
        int count = 0;
        try {
            count = array.getInt("count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 5; i < min(10,count); i++){
            String idi = "q";
            idi += String.valueOf(i);
            int resID = getResources().getIdentifier(idi, "id", getPackageName());
            quiz=findViewById(resID);
            try {
                quiz.setText(array.getJSONArray("quizzes").getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            quiz.setOnClickListener(this::OnClick);
        }
    }
    void OnClick(View view){
        if(view.getId() == breturn.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(ChooseQuizSecond.this, MainActivity.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        else if(view.getId() == bnext.getId()){
            mediaPlayer.stop();
            Intent it = new Intent(ChooseQuizSecond.this, ChooseQuizThird.class);
            it.putExtra("music", cmusic);
            it.putExtra("sounds", csounds);
            startActivity(it);
            finish();
        }
        else if(view.getId() == bprevious.getId()){
            mediaPlayer.stop();
            Intent it = new Intent(ChooseQuizSecond.this, ChooseQuiz.class);
            it.putExtra("music", cmusic);
            it.putExtra("sounds", csounds);
            startActivity(it);
            finish();
        }
        else{
            String resourceName = view.getResources().getResourceName(view.getId());
            String strnumber = "";
            for(int i = 26; i < resourceName.length(); i++){
                strnumber += resourceName.charAt(i);
            }
            int number = Integer.parseInt(strnumber);
            int index = 0;
            mediaPlayer.stop();
            Intent i = new Intent(ChooseQuizSecond.this, ChooseLanguage.class);
            i.putExtra("music", cmusic);
            i.putExtra("number", number);
            i.putExtra("index", index);
            i.putExtra("result", 0);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
    }
    public static InputStream loadInputStreamFromAssetFile(Context context, String fileName){
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open(fileName);
            return is;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadContentFromFile(Context context, String path){
        String content = null;
        try {
            InputStream is = loadInputStreamFromAssetFile(context, path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }
}