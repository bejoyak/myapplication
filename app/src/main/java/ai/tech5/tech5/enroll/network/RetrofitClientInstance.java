package ai.tech5.tech5.enroll.network;


import java.util.concurrent.TimeUnit;

import ai.tech5.tech5.enroll.preferences.AppSharedPreference;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClientInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {


//            ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                    .tlsVersions(TlsVersion.TLS_1_2)
//                    .cipherSuites(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
//                            CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
//                            CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
//                    .build();


           // .connectionSpecs(Collections.singletonList(spec));



            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS).build();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);


            retrofit = new Retrofit.Builder()
                    .baseUrl(AppSharedPreference.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }
}
