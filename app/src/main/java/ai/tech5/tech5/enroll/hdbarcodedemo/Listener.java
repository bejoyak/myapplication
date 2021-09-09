package ai.tech5.tech5.enroll.hdbarcodedemo;

import android.content.Context;

import com.tech5.passportizer.Decompressor;
import com.tech5.passportizer.DecompressorConfig;
import com.tech5.passportizer.IOneShotProcessor;
import com.tech5.passportizer.OneShotProcessorConfig;
import com.tech5.passportizer.PassportizerFactory;

import ai.tech5.sdk.abis.face.t5face.FaceSDKFactory;
import ai.tech5.sdk.abis.face.t5face.IFaceSDKTemplateExtractor;
import ai.tech5.sdk.abis.face.t5face.IFaceSDKTemplateMatcher;
import ai.tech5.sdk.abis.face.t5face.TemplateExtractorConfig;
import ai.tech5.sdk.abis.face.t5face.TemplateMatcherConfig;
import ai.tech5.tech5.enroll.utils.Logger;

import static ai.tech5.tech5.enroll.utils.Logger.logException;


public class Listener {


    public static IFaceSDKTemplateExtractor t5TemplateCreator;
    public static IOneShotProcessor t5OneshotProcessor;

    public static IFaceSDKTemplateMatcher t5TemplateMatcher;
    public static boolean isSDKInitialized = false;
    //public static SmartCompress smartCompress;
    public static Decompressor decompressor;
    private static String TAG = "Listener";

    public static boolean initSDK(int compressionLevel, Context context) {



        try {
            if (!isSDKInitialized) {
                long t1 = System.currentTimeMillis();
                System.err.println("Before init() !!!");
                Logger.addToLog(TAG, "Before init() !!! compLevel " + compressionLevel);

                long t4 = System.currentTimeMillis();

                int version = 210;
                String matcherTableCode = "gn";

                TemplateExtractorConfig extractorConfig = new TemplateExtractorConfig();
                extractorConfig.FaceDetectorVersion = version;
                extractorConfig.FaceDetectorConfidence = 0.9f;
                extractorConfig.BuilderVersion = version;

                extractorConfig.QualityVersion = -1;

                t5TemplateCreator = FaceSDKFactory.CreateTemplateExtractor();
                t5TemplateCreator.Init(extractorConfig);

                System.out.println("template creator  loaded successfully !!! " + t5TemplateCreator);

                TemplateMatcherConfig mConfig = new TemplateMatcherConfig();
                mConfig.BuilderVersion = version;
                mConfig.MatcherFirListHint = 10;
                mConfig.MatcherTableCode = matcherTableCode;
                t5TemplateMatcher = FaceSDKFactory.CreateTemplateMatcher();
                t5TemplateMatcher.Init(mConfig);
                long t3 = System.currentTimeMillis();
                System.err.println("@@@@@@@Time Taken to load Face Matcher :: " + (t3 - t4));
                Logger.addToLog(TAG, "@@@@@@@Time Taken to load Face Matcher :: " + (t3 - t4));
                System.out.println("face matcher loaded successfully !!!");
                Logger.addToLog(TAG, "face matcher loaded successfully !!! ");

                LogUtils.debug(TAG, "compression level " + compressionLevel);


                OneShotProcessorConfig config = new OneShotProcessorConfig();
                config.computeDevice = -1;
                config.detectorConfidence = 0.9f;
                config.detectorVersion = version;
                config.compressorVersion = 100;
                config.compressionLevel = compressionLevel;
                config.builderVersion = version;


                t5OneshotProcessor = PassportizerFactory.CreateOneShotProcessor(config);

                DecompressorConfig decompressorConfig = new DecompressorConfig();

                decompressorConfig.decompressorVersion = 100;

                decompressor = new Decompressor(decompressorConfig);

                System.err.println("Oneshot processor loaded successfully !!!compressionLevel " + t5OneshotProcessor);
                Logger.addToLog(TAG, "Oneshot processor  loaded successfully !!! compressionLevel " + decompressor);
                long t2 = System.currentTimeMillis();
                System.err.println("@@@@@@@Time Taken to load Template Creator :: " + (t2 - t1));
                Logger.addToLog(TAG, "@@@@@@@Time Taken to load Template Creator :: " + (t2 - t1));


                isSDKInitialized = true;


            }
        } catch (Exception e) {
            e.printStackTrace();
            isSDKInitialized = false;
            Logger.addToLog(TAG, "isSDKInitialized:: failed");
            logException(TAG, e);
        }
        return isSDKInitialized;
    }


}
