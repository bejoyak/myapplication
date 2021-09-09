package ai.tech5.tech5.enroll.network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private Retrofit retrofitRx = null;

//    public ApiClient(PreferencesHelper preferencesHelper) {
//
//        baseUrl = preferencesHelper.getBaseURL();
//    }


    public Retrofit getRetrofitClient() {


        if (retrofitRx == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);


            client.connectTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES);


            retrofitRx = new Retrofit.Builder()
                    .baseUrl("https://cdn-api.co-vin.in/api/")
                    .client(client.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitRx;
    }


}