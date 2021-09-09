package ai.tech5.tech5.network;


import ai.tech5.tech5.models.EnrollResponce;
import ai.tech5.tech5.models.MiddlewareRequest;
import ai.tech5.tech5.models.VerifyResponce;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface T5Service {

    @POST("request")
    Call<EnrollResponce> enrollbiometrics(@Body MiddlewareRequest request);

    @POST("request")
    Call<VerifyResponce> virifybiometrics(@Body MiddlewareRequest request);

}