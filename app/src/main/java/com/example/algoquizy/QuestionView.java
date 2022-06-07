package com.example.algoquizy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionView extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    ImageButton breturn;
    Button bfirsta, bseconda, bthirda, bfourtha;
    TextView title, question;
    ImageView image;
    int number, index, result;
    boolean cmusic = true, csounds;
    String language, ans, img;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_question_view);
        mediaPlayer = MediaPlayer.create(QuestionView.this, R.raw.algoquizy_question);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        breturn = findViewById(R.id.returnButton);
        breturn.setOnClickListener(this::OnClick);
        image = findViewById(R.id.questionImage);
        title = findViewById(R.id.title);
        question = findViewById(R.id.question);
        bfirsta = findViewById(R.id.firstaButton);
        bseconda = findViewById(R.id.secondaButton);
        bthirda = findViewById(R.id.thirdaButton);
        bfourtha = findViewById(R.id.fourthaButton);
        Intent intent = getIntent();
        number = intent.getIntExtra("number", 0);
        index = intent.getIntExtra("index", 0);
        result = intent.getIntExtra("result", 0);
        language = intent.getStringExtra("language");
        cmusic = intent.getBooleanExtra("music", true);
        csounds = intent.getBooleanExtra("sounds", true);
        ans = "answers" + language;
        img = "image" + language;
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
                QuestionView.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject array = new JSONObject(myResponse);
                            String stitle = array.getJSONArray("quizzes").getJSONObject(number).getString("title");
                            title.setText(stitle);
                            if(array.getJSONArray("quizzes").getJSONObject(number).getInt("count") == index){
                                mediaPlayer.stop();
                                Intent i = new Intent(QuestionView.this, QuizResults.class);
                                i.putExtra("result", result);
                                i.putExtra("music", cmusic);
                                i.putExtra("sounds", csounds);
                                i.putExtra("count", index);
                                startActivity(i);
                                finish();
                            }
                            else{
                                JSONObject questions = array.getJSONArray("quizzes").getJSONObject(number).getJSONArray("questions")
                                        .getJSONObject(index);
                                JSONArray answers = questions.getJSONArray(ans);
                                question.setText(questions.getString("title"));
                                bfirsta.setText(answers.getJSONObject(0).getString("text"));
                                bseconda.setText(answers.getJSONObject(1).getString("text"));
                                bthirda.setText(answers.getJSONObject(2).getString("text"));
                                bfourtha.setText(answers.getJSONObject(3).getString("text"));
                                bfirsta.setOnClickListener(this::OnClick);
                                bseconda.setOnClickListener(this::OnClick);
                                bthirda.setOnClickListener(this::OnClick);
                                bfourtha.setOnClickListener(this::OnClick);
                                String jimg = questions.getString(img);
                                System.out.println(jimg);
                                int resID = getResources().getIdentifier(jimg, "drawable", getPackageName());
                                image.setImageDrawable(getDrawable(resID));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    private void OnClick(View view) {
                        int in;
                        if(view.getId() == bfirsta.getId()){
                            in = 0;
                        }
                        else if(view.getId() == bseconda.getId()){
                            in = 1;
                        }
                        else if(view.getId() == bthirda.getId()){
                            in = 2;
                        }
                        else{
                            in = 3;
                        }
                        JSONObject array = null;
                        try {
                            array = new JSONObject(myResponse);
                            JSONObject questions = array.getJSONArray("quizzes").getJSONObject(number).getJSONArray("questions")
                                    .getJSONObject(index);
                            boolean correct = questions.getJSONArray(ans).getJSONObject(in).getBoolean("correct");
                            mediaPlayer.stop();
                            if(correct){
                                Intent i = new Intent(QuestionView.this, GoodAnswer.class);
                                i.putExtra("number", number);
                                i.putExtra("index", index);
                                i.putExtra("result", result);
                                i.putExtra("language", language);
                                i.putExtra("music", cmusic);
                                i.putExtra("sounds", csounds);
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(QuestionView.this, BadAnswer.class);
                                i.putExtra("number", number);
                                i.putExtra("index", index);
                                i.putExtra("result", result);
                                i.putExtra("language", language);
                                i.putExtra("music", cmusic);
                                i.putExtra("sounds", csounds);
                                startActivity(i);
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    void OnClick(View view){
        if(view.getId() == breturn.getId()){
            mediaPlayer.stop();
            Intent i = new Intent(QuestionView.this, ChooseQuiz.class);
            i.putExtra("music", cmusic);
            i.putExtra("sounds", csounds);
            startActivity(i);
            finish();
        }
    }
}