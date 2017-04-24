package com.example.yandextranslater;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.String.valueOf;

public class Main extends AppCompatActivity implements View.OnClickListener {
    private static TrAPI Api;

    Button btnTranslate;
    EditText etQuestion, etAnswer;
    String textForTranslate, TranslatedText;
    List<String> translatedTextList;
    //URL url;
    Handler handler;
    //JSONObject jsonObject = new JSONObject();
    //List<TrModel> answer; неправльно
    TrModel answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        btnTranslate = (Button)findViewById(R.id.btnTranslate);
        btnTranslate.setOnClickListener(this);
        etQuestion = (EditText) findViewById(R.id.etQuestion);
        etAnswer = (EditText) findViewById(R.id.etAnswer);

        handler = new Handler(){
            public void handleMessage(android.os.Message msg){
                Log.d("answer from handler = ", TranslatedText);
                etAnswer.setText(TranslatedText);
            }};
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnTranslate:
                textForTranslate = etQuestion.getText().toString();
                if (textForTranslate.equals("")) break;
                Thread t = new Thread(new Runnable() {
                    public void run() {
                try {


                    Log.d("вопрос = ", textForTranslate);
                    String textFT = URLEncoder.encode(textForTranslate, "UTF-8");   //текст, для перевода в url формате
                    String keyApi = "trnsl.1.1.20170416T092142Z.1e1d195761e91864.77b8d05120647e0e35ce9222cba25c063966408b";
                    //textFT = "text="+textFT;
                    String lang = "en-ru";                                  //откуда и куда переводим
                    Api = Controller.getApi();
                    Log.d("прогресс = ", "ща отправим");
                    //Api.sendForTranslate(lang,keyApi,textFT).enqueue(new Callback<List<TrModel>>() {
                    Api.sendForTranslate(lang,keyApi,textFT).enqueue(new Callback<TrModel>() {
                        @Override   //при удачном выполнении
                        //public void onResponse(Call<List<TrModel>> call, Response<List<TrModel>> response) {  неправильно
                        public void onResponse(Call<TrModel> call, Response<TrModel> response) {
                            Log.d("response success = ", valueOf(response.isSuccessful()));
                            Log.d("прогресс = ", "отправили успешно");
                            Log.d("response = ", response.toString());

                            //.body() -- только тело запроса
                            if(response.body()!=null){
                            //ОПЕРАЦИИ С ОТВЕТОМ
                            answer = response.body();  //ответ в виде gson      TrModel answer;
                            translatedTextList=answer.getText();
                            //TranslatedText=translatedTextList.get(0);
                                Log.d("answer from list = ", translatedTextList.get(0));
                                TranslatedText = translatedTextList.get(0);

                                Log.d("answer from field = ", TranslatedText);
                            handler.sendEmptyMessage(1);
                            }
                            else Log.d("response body = ","null");
                        }

                        @Override   //при неудачном выполнении
                        //public void onFailure(Call<List<TrModel>> call, Throwable t) {
                        public void onFailure(Call<TrModel> call, Throwable t) {
                            t.printStackTrace();
                            Log.d("прогресс = ", "отправили безуспешно");
                            Toast.makeText(Main.this, "An error occurred during networking", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException /*| JSONException*/|NullPointerException e) {e.printStackTrace();}  //отлов исключений


                //etAnswer.setText(TranslatedText); Из другого потока запрещено юзать View
                }});
                t.start();
                break;
        }

    }
}
