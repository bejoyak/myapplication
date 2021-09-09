package ai.tech5.tech5.rotation;

import android.content.Context;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FacerotationPresenterImplementation implements FacerotationPresenter {

    private FacerotationView view;
    private CompositeDisposable compositeDisposable;


    private Context context;

    private int MAX_IMAGE_SIZE_WIDTH = 480;
    private int MAX_IMAGE_SIZE_HEIGHT = 600;


    @Override
    public void setView(Context context, FacerotationView view) {
        this.view = view;
        this.context = context;

        if (compositeDisposable == null || !compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }

    }

    @Override
    public void rotateImage(Context con, ImageRotateParams params) {

        view.showProgress();
        getRotatateImageObservable(con, params).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                view.hideProgress();
                view.onImageRotated(s);
            }

            @Override
            public void onError(Throwable e) {

                view.hideProgress();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void destroy() {
        view = null;
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    public Observable<String> getRotatateImageObservable(final Context context, final ImageRotateParams params) {

        return Observable.fromCallable(() -> new ImageRotator().rotateImage(context, params)


        );
    }

}
