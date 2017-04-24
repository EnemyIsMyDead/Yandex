package com.example.yandextranslater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Controller {
    static final String BASE_URL = "https://translate.yandex.net/api/v1.5/";

    public static TrAPI getApi() {
        //создаётся принимающий ответ сервера gson
        Gson gson = new GsonBuilder().setLenient().create();
        //в запрос добавляется URL
        //gson, принимающий ответ сервера, заполняется ответом
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        TrAPI api = retrofit.create(TrAPI.class);  //в запрос вставляется текст запроса
        //Создаем объект(api), при помощи которого будем выполнять запросы
        return api;
    }
}
