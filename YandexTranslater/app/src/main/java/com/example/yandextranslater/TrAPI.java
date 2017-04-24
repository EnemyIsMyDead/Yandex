package com.example.yandextranslater;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TrAPI {

        //текст запроса
        //изменяемая часть адреса(lang)
    @FormUrlEncoded
    @POST("tr.json/translate")
    @Headers({"Content-Type: application/x-www-form-urlencoded",
              "Accept:*/*"})
    Call<TrModel> sendForTranslate(@Query("lang") String lang, @Query("key") String key, @Field(value = "text",encoded = true) String textFT);

}
