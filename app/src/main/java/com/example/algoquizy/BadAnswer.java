package com.example.algoquizy;

//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BadAnswer extends AppCompatActivity {
    Button bforward;
    int number, index, result;
    String language;
    boolean cmusic, csounds;
    MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bad_answer);
        bforward = findViewById(R.id.forwardbadButton);
        bforward.setOnClickListener(this::OnClick);
        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        index = intent.getIntExtra("index", 0);
        result = intent.getIntExtra("result", 0);
        language = intent.getStringExtra("language");
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        mediaPlayer = MediaPlayer.create(BadAnswer.this, R.raw.bad_answer);
        index++;
        if(csounds){
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

    }
    void OnClick(View view){
        try {
            JSONObject array = new JSONObject(loadContentFromFile(BadAnswer.this, "db.json"));
            if(array.getJSONArray("quizzes").getJSONObject(number).getInt("count") == index){
                mediaPlayer.stop();
                Intent i = new Intent(BadAnswer.this, QuizResults.class);
                i.putExtra("result", result);
                i.putExtra("music", cmusic);
                i.putExtra("sounds", csounds);
                i.putExtra("count", index);
                startActivity(i);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(BadAnswer.this, QuestionView.class);
        mediaPlayer.stop();
        i.putExtra("number", number);
        i.putExtra("index", index);
        i.putExtra("result", result);
        i.putExtra("language", language);
        i.putExtra("music", cmusic);
        i.putExtra("sounds", csounds);
        startActivity(i);
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