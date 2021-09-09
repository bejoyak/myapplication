package ai.tech5.tech5.mvp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import ai.tech5.tech5.R;
import ai.tech5.tech5.models.Biometric;
import ai.tech5.tech5.models.EkycDemographics;
import ai.tech5.tech5.models.EnrollResponce;
import ai.tech5.tech5.models.MiddlewareRequest;
import ai.tech5.tech5.models.VerifyResponce;
import ai.tech5.tech5.network.ApiClient;
import ai.tech5.tech5.network.T5Service;
import ai.tech5.tech5.utils.PreferencesHelper;
import ai.tech5.tech5.utils.Utility;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;

//import java.util.logging.Handler;

public class OnPresenterImpl implements OnPresenter {

    private OnView view;
    private CompositeDisposable compositeDisposable;
    private T5Service service;
    private Utility utility;

    private Context context;

    private int MAX_IMAGE_SIZE_WIDTH = 480;
    private int MAX_IMAGE_SIZE_HEIGHT = 600;


//    public FingerIdentifyPresenterImpl(Context context) {
//        this.context = context;
//    }

    @Override
    public void setView(Context context, OnView view) {
        this.view = view;
        this.context = context;
        utility = new Utility();

        if (compositeDisposable == null || !compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
//
        service = new ApiClient(new PreferencesHelper(context)).getRetrofitClient().create(T5Service.class);

    }

    @Override
    public void enrolldetails(Uri capturedfaceImage, Uri Idcardfaceimage, String name, String Dateofbirth, String country, String mail, String gender, String bloodgroup) {
        view.showProgress(R.raw.load);

//        view.showProgress(R.raw.load);
        getenrollObservable(context, capturedfaceImage, Idcardfaceimage, name, Dateofbirth, country, mail, gender, bloodgroup).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<EnrollResponce>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(EnrollResponce response) {
                view.hideProgress();
                view.onenrollmentrespone(response);
            }

            @Override
            public void onError(Throwable e) {
                view.hideProgress();
                view.onenrollmentFailed(e);
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        });

    }


    private EnrollResponce enrollIdentify(Context context, Uri capturedfaceImage, Uri Idcardfaceimage, String name, String Dateofbirth, String country, String mail, String gender, String bloodgroup) throws Exception {
        EnrollResponce response = null;
        try {

            MiddlewareRequest request = new MiddlewareRequest();
            request.transactionId = UUID.randomUUID() + "-" + System.currentTimeMillis();
            request.digitalID = mail;
            request.requestType = "enroll";
            request.faceThreshold = 6;
            request.passiveLiveness = true;
            ArrayList<Biometric> face = new ArrayList<>();

            if (capturedfaceImage != null) {
                Bitmap currentBitmap = null;
                Bitmap imageToSearch = null;

                try {
                    currentBitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), capturedfaceImage);
                    System.out.println("faceimage width : "+currentBitmap.getWidth()+" Faceimage height: "+currentBitmap.getHeight());
                } catch (Exception e) {

                }

//                if (currentBitmap != null) {
//
//                    Log.d("TAG", "face captureimage dimensions before scaling  WxH :" + currentBitmap.getWidth() + "x" + currentBitmap.getHeight());
//                    //  Log.d(TAG, "image width " + currentBitmap.getWidth());
//                    imageToSearch = Utility.scaleDown(currentBitmap, MAX_IMAGE_SIZE_WIDTH, MAX_IMAGE_SIZE_HEIGHT, true);
//
//                    if (imageToSearch != null) {
//                        Log.d("TAG", "face capture image dimensions after scaling WxH :" + imageToSearch.getWidth() + "x" + imageToSearch.getHeight());
//
//                    }
//
//
//                }

                if (currentBitmap != null) {
                    byte[] image = Utility.bitmapToByetArray(currentBitmap);
                    Biometric biometrics = new Biometric();
                    biometrics.position = "F";
                    biometrics.type = "Face";
                    biometrics.image = Base64.encodeToString(image, Base64.NO_WRAP);
//            biometrics.image = "f position face image";
                    biometrics.template = null;
                    face.add(biometrics);

                }

            } else {
                Log.d("TAG", "Face capture image is null");
            }
            if (Idcardfaceimage != null) {
                Bitmap currentBitmap = null;
                Bitmap imageToSearch = null;

                try {
                    currentBitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), Idcardfaceimage);
                } catch (Exception e) {

                }

//                if (currentBitmap != null) {
//
//                    Log.d("TAG", "face id image dimensions before scaling  WxH :" + currentBitmap.getWidth() + "x" + currentBitmap.getHeight());
//                    //  Log.d(TAG, "image width " + currentBitmap.getWidth());
//                    imageToSearch = Utility.scaleDown(currentBitmap, MAX_IMAGE_SIZE_WIDTH, MAX_IMAGE_SIZE_HEIGHT, true);
//
//                    if (imageToSearch != null) {
//                        Log.d("TAG", "face id image dimensions after scaling WxH :" + imageToSearch.getWidth() + "x" + imageToSearch.getHeight());
//
//                    }
//
//
//                }

                if (currentBitmap != null) {
                    byte[] image = Utility.bitmapToByetArray(currentBitmap);
                    Biometric biometrics2 = new Biometric();
                    biometrics2.position = "E";
                    biometrics2.type = "Face";
                    biometrics2.image = Base64.encodeToString(image, Base64.NO_WRAP);
//            biometrics2.image = "A position face image";
                    biometrics2.template = null;
                    face.add(biometrics2);


                }

            } else {
                Log.d("TAG", "Face id image is null");
            }


            request.biometrics = face;
            EkycDemographics customvaluesis = new EkycDemographics();
            customvaluesis.name = name;
            customvaluesis.dateOfBirth = Dateofbirth;
            customvaluesis.gender = gender;
            customvaluesis.bloodGroup = bloodgroup;
            customvaluesis.COUNTRY = country;

            request.demographics = customvaluesis;

            File file = utility.createExternalDirectory(context, "cache");

            utility.writeToFile(new Gson().toJson(request).getBytes(), file.getAbsolutePath() + File.separator + "enrollrequest.json");


            Call<EnrollResponce> call = service.enrollbiometrics(request);

            response = call.execute().body();

              utility.writeToFile(new Gson().toJson(response).getBytes(), file.getAbsolutePath() + File.separator + "enrollresponse.json");


        } catch (
                Exception e) {
            throw e;
        }

        return response;

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

//        File file = utility.createExternalDirectory(context, "cache");
//
//        if (demographics.face != null && !demographics.face.equals("")) {
//            byte[] faceImage = Base64.decode(demographics.face, Base64.NO_WRAP);
//            demographics.face = utility.writeToFile(faceImage, file.getAbsolutePath() + File.separator + type + "_" + nik + "_face.jpg");
//        }
//
//
    public Observable<EnrollResponce> getenrollObservable(Context context, Uri capturedfaceImage, Uri Idcardfaceimage, String name, String Dateofbirth, String country, String mail, String gender, String bloodgroup) {

        return Observable.create((ObservableEmitter<EnrollResponce> emitter) -> {
            try {

                EnrollResponce response = enrollIdentify(context, capturedfaceImage, Idcardfaceimage, name, Dateofbirth, country, mail, gender, bloodgroup);

                if (response != null) {
                    emitter.onNext(response);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("Unable to get response from server"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }

        });
    }

    @Override
    public void destroy() {

        view = null;
        context = null;

        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }

    }


    @Override
    public void authenticate(String patientid, Uri faceimage) {

        view.showProgress(R.raw.load);

//        view.showProgress(R.raw.load);
        getverifyObservable(context, patientid, faceimage).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<VerifyResponce>() {

            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(VerifyResponce response) {
                view.hideProgress();
                view.onauthenticateresponce(response);
            }

            @Override
            public void onError(Throwable e) {
                view.hideProgress();
                view.onauthenticationFailed(e);
            }

            @Override
            public void onComplete() {
                view.hideProgress();
            }
        });

    }


    private VerifyResponce verifyIdentify(Context context, String patientid, Uri faceimage) throws Exception {
        VerifyResponce response = null;
        try {

            MiddlewareRequest request = new MiddlewareRequest();
            request.transactionId = UUID.randomUUID() + "-" + System.currentTimeMillis();
            request.digitalID = patientid;
            request.requestType = "verify";
            request.faceThreshold = 6;
            request.passiveLiveness = false;
            if (faceimage != null) {
                Bitmap currentBitmap = null;
                Bitmap imageToSearch = null;

                try {
                    currentBitmap = MediaStore.Images.Media.getBitmap(context.getApplicationContext().getContentResolver(), faceimage);
                } catch (Exception e) {

                }

//                if (currentBitmap != null) {
//
//                    Log.d("TAG", "image dimensions before scaling  WxH :" + currentBitmap.getWidth() + "x" + currentBitmap.getHeight());
//                    //  Log.d(TAG, "image width " + currentBitmap.getWidth());
//                    imageToSearch = Utility.scaleDown(currentBitmap, MAX_IMAGE_SIZE_WIDTH, MAX_IMAGE_SIZE_HEIGHT, true);
//
//                    if (imageToSearch != null) {
//                        Log.d("TAG", "image dimensions after scaling WxH :" + imageToSearch.getWidth() + "x" + imageToSearch.getHeight());
//
//                    }
//
//
//                }

                if (currentBitmap != null) {
                    byte[] image = Utility.bitmapToByetArray(currentBitmap);

                    ArrayList<Biometric> face = new ArrayList<>();

                    Biometric biometrics = new Biometric();
                    biometrics.position = "F";
                    biometrics.image = Base64.encodeToString(image, Base64.NO_WRAP);
//                    biometrics.image = "f position face image";

                    face.add(biometrics);


                    request.biometrics = face;

                }

            } else {
                Log.d("TAG", "Face image is null");
            }

            ///////////////////////////////////////////////
            File file = utility.createExternalDirectory(context, "cache");

            utility.writeToFile(new Gson().toJson(request).getBytes(), file.getAbsolutePath() + File.separator + "verifyrequest.json");


            Call<VerifyResponce> call = service.virifybiometrics(request);

            response = call.execute().body();

            utility.writeToFile(new Gson().toJson(response).getBytes(), file.getAbsolutePath() + File.separator + "verifyresponse.json");

//            EkycDemographics innerResponse = new Gson().fromJson(response.demographics, EkycDemographics.class);
//
//
//            utility.writeToFile(new Gson().toJson(innerResponse).getBytes(), file.getAbsolutePath() + File.separator + "inner_response.json");

        } catch (
                Exception e) {
            throw e;
        }

        return response;

    }


    public Observable<VerifyResponce> getverifyObservable(Context context, String patientid, Uri faceimage) {

        return Observable.create((ObservableEmitter<VerifyResponce> emitter) -> {
            try {

                VerifyResponce response = verifyIdentify(context, patientid, faceimage);

                if (response != null) {
                    emitter.onNext(response);
                    emitter.onComplete();
                } else {
                    emitter.onError(new Throwable("Unable to get response from server"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                emitter.onError(e);
            }

        });
    }


}
