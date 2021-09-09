package ai.tech5.tech5.enroll.exception;

import static ai.tech5.tech5.enroll.utils.Logger.logException;

public class ExceptionUtility {

    public static void logError(String tag, String description, Exception e) {
        logException(tag, e);
    }
}
