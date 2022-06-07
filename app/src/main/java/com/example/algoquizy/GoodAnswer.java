package com.example.algoquizy;

//import android.content.Intent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
//import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        OkHttpClient client = new OkHttpClient();
        String url = "http://10.0.2.2:3000/";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                GoodAnswer.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject array = new JSONObject(myResponse);
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
                    }
                });
            }
        });
        Intent i = new Intent(GoodAnswer.this, QuestionView.class);
        i.putExtra("number", number);
        i.putExtra("index", index);
        i.putExtra("result", result);
        i.putExtra("language", language);
        i.putExtra("music", cmusic);
        i.putExtra("sounds", csounds);
        startActivity(i);
    }
}