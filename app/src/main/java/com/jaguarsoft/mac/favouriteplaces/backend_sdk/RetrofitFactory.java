package com.jaguarsoft.mac.favouriteplaces.backend_sdk;

import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RetrofitFactory {

    <S> S create(Class<S> serviceClass) {
        return build(defaultOkHttpClientBuilder()).create(serviceClass);
    }

    Retrofit build(OkHttpClient.Builder okHttpClientBuilder) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("http://10.0.2.2:8080");
        builder.client(okHttpClientBuilder.build());
        builder.addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()));
        return builder.build();
    }

    OkHttpClient.Builder defaultOkHttpClientBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(5, SECONDS);
        builder.writeTimeout(5, SECONDS);
        builder.connectTimeout(5, SECONDS);
        return builder;
    }

}
