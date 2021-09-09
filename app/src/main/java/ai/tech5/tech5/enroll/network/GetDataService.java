package ai.tech5.tech5.enroll.network;


import java.util.List;

import ai.tech5.tech5.enroll.model.BarCodeResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface GetDataService {

//    @POST
//    Call<BarcodeResponse> uploadBarcodeData(@Url String url, @Body BarcodeRequestModel barcodeRequestModel);


//    @Multipart
//    @POST("https://idencode.tech5-sa.com/v1/enroll")
//    Call<NewBarCodeResponse> createBarCode(@Part MultipartBody.Part image, @Part MultipartBody.Part extra);

    @Multipart
    @POST()
    Call<BarCodeResponse> createBarCode(@Url String url, @Part List<MultipartBody.Part> images);

}