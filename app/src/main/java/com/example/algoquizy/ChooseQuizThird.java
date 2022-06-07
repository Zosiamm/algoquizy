package com.example.algoquizy;

import static java.lang.Math.min;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChooseQuizThird extends AppCompatActivity {
    ImageButton breturn;
    Button quiz, bnext, bprevious;
    MediaPlayer mediaPlayer;
    boolean cmusic = true, csounds;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_choose_quiz_third);
        Intent intent = getIntent();
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        breturn = findViewById(R.id.returnButton);
        breturn.setOnClickListener(this::OnClick);
        bnext = findViewById(R.id.next);
        bnext.setOnClickListener(this::OnClick);
        bprevious = findViewById(R.id.previous);
        bprevious.setOnClickListener(this::OnClick);
        mediaPlayer = MediaPlayer.create(ChooseQuizThird.this, R.raw.algoquizy_thinking_music);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        if(!cmusic){
            mediaPlayer.stop();
        }
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
                ChooseQuizThird.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject array = new JSONObject(myResponse);
                            int count = array.getInt("count");
                            for(int i = 10; i < min(15,count); i++){
                                String idi = "q";
                                idi += String.valueOf(i);
                                int resID = getResources().getIdentifier(idi, "id", getPackageName());
                                quiz=findViewById(resID);
                                String text = array.getJSONArray("quizzes").getJSONObject(i).getString("title");
                                quiz.setText(text);
                                quiz.setOnClickListener(this::OnClick);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void OnClick(View view) {
                        String resourceName = view.getResources().getResourceName(view.getId());
                        String strnumber = "";
                        for(int i = 26; i < resourceName.length(); i++){
                            strnumber += resourceName.charAt(i);
                        }
                        int number = Integer.parseInt(strnumber);
                        int index = 0;
                        mediaPlayer.stop();
                        Intent i = new Intent(ChooseQuizThird.this, ChooseLanguage.class);
                        i.putExtra("music", cmusic);
                        i.putExtra("number", number);
                        i.putExtra("index", index);
                        i.putExtra("result", 0);
                        i.putExtra("sounds", csounds);
                        startActivity(i);
                        finish();
                    }
                });
            }
        });

    }
    void OnClick(View view){
        if(view.getId() == breturn.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(ChooseQuizThird.this, MainActivity.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
        else if(view.getId() == bnext.getId()){
            return;
        }
        else if(view.getId() == bprevious.getId()){
            mediaPlayer.stop();
            Intent it = new Intent(ChooseQuizThird.this, ChooseQuizSecond.class);
            it.putExtra("music", cmusic);
            it.putExtra("sounds", csounds);
            startActivity(it);
            finish();
        }
    }
}