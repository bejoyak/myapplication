package ai.tech5.tech5.enroll.foruploadandverify.hdbarcodedemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.tech5.passportizer.OneShotResult;

import ai.tech5.tech5.R;
import ai.tech5.tech5.enroll.foruploadandverify.ResultActivity;
import ai.tech5.tech5.enroll.hdbarcodedemo.Listener;
import ai.tech5.tech5.enroll.hdbarcodedemo.Utilities;
import ai.tech5.tech5.enroll.utils.Logger;

public class MatchWithTemplateTask extends AsyncTask<String, Void, String> {

    private static final String TAG = MatchWithTemplateTask.class.getSimpleName();

    private ResultActivity parentActivity;
    private byte[] template1, capturedFaceBytes, deCompressedFaceImage;
    private ProgressDialog pdLoading;
    private float score = 0;
    private float timeTakenForTemplateCreation, timeTakenForMatching;

    boolean hasFaceTemplate = false;


    public MatchWithTemplateTask(ResultActivity parentActivity, byte[] template1, byte[] capturedFaceBytes, byte[] decompressedImage) {

        this.parentActivity = parentActivity;
        this.capturedFaceBytes = capturedFaceBytes;
        this.deCompressedFaceImage = decompressedImage;
        this.template1 = template1;

        if (template1 != null) {
            hasFaceTemplate = true;
        }

        Logger.addToLog(TAG, "MatchWithTemplateTask() called, hasFaceTemplate = " + hasFaceTemplate+" template size "+(template1==null?0:template1.length));

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pdLoading = new ProgressDialog(parentActivity);
        if (null != pdLoading) {
            pdLoading.setMessage(parentActivity.getResources().getString(R.string.please_wait));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Logger.addToLog(TAG, "Before Creating Templates");

            OneShotResult compressedImageTCResult = null;
            OneShotResult capturedImageTCResult;

            long t1 = System.currentTimeMillis();
//            ArrayList<CreateFaceTemplateResult> tmpls = Listener.t5TemplateCreator.CreateFaceTemplate(capturedFaceBytes);
//            if (tmpls != null && tmpls.size() > 0) {
//                captureImageTCResult = tmpls.get(0);
//            }

            capturedImageTCResult = Listener.t5OneshotProcessor.processImage(capturedFaceBytes);

            if(capturedImageTCResult!=null){
                Logger.addToLog(TAG, "Template created for face image "+capturedImageTCResult.template);
            }


            if (!hasFaceTemplate) {

                Logger.addToLog(TAG, "No face template, creating Template for compressed Image  ");

                byte[] decompressedImg = Utilities.upscaleImageToDouble(deCompressedFaceImage);

                // Listener.t5TemplateCreator.CreateFaceTemplate(deCompressedFaceImage);
                compressedImageTCResult = Listener.t5OneshotProcessor.processImage(decompressedImg);

                Logger.addToLog(TAG, "Template created for compressed image "+compressedImageTCResult);

            }


            long t2 = System.currentTimeMillis();
            timeTakenForTemplateCreation = t2 - t1;
            Logger.addToLog(TAG,"Template Created :: Time Taken " + timeTakenForTemplateCreation);
            t1 = System.currentTimeMillis();
            score = Listener.t5TemplateMatcher.Match(capturedImageTCResult.template, hasFaceTemplate ? template1 : compressedImageTCResult.template);
            t2 = System.currentTimeMillis();
            timeTakenForMatching = t2 - t1;
            Logger.addToLog(TAG, "Matching Done score : "+score);
        } catch (Exception e) {
            e.printStackTrace();

            Logger.logException(TAG, e);
        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (null != pdLoading) pdLoading.dismiss();
        Logger.addToLog(TAG, "Face Matching Process completed...................");
        ResultObject resultObject = new ResultObject();
        resultObject.setMatchingScore(score);
        resultObject.setTimeTakenForTemplateCreation(timeTakenForTemplateCreation);
        resultObject.setTimeTakenForMatching(timeTakenForMatching);
        parentActivity.processFinish(resultObject);

    }
}
