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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoodAnswer extends AppCompatActivity {
    Button bforward;
    int number, index, result;
    boolean cmusic, csounds;
    MediaPlayer mediaPlayer;
    String language;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_good_answer);
        bforward = findViewById(R.id.forwardgoodButton);
        bforward.setOnClickListener(this::OnClick);
        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        index = intent.getIntExtra("index", 0);
        result = intent.getIntExtra("result", 0);
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        language = intent.getStringExtra("language");
        mediaPlayer = MediaPlayer.create(GoodAnswer.this, R.raw.good_answer);
        result++;
        index++;
        if(csounds){
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        }

    }
    void OnClick(View view){
        JSONObject array;
        try {
            array = new JSONObject(loadContentFromFile(GoodAnswer.this, "db.json"));
            if(array.getJSONArray("quizzes").getJSONObject(number).getInt("count") == index){
                mediaPlayer.stop();
                Intent i = new Intent(GoodAnswer.this, QuizResults.class);
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
        Intent i = new Intent(GoodAnswer.this, QuestionView.class);
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