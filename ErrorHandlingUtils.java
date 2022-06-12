package kr.datasolution.bigstation.utils.rule.common.util;

public class ErrorHandlingUtils {
    /**
     * using in catch clause
     * generate error massage
     * @param errorMsg
     * @param e
     * @param stackTraceElement Thread.currentThread().getStackTrace()[1]
     * @return error message string
     */
    public static String getErrorLog(String errorMsg, Exception e, StackTraceElement stackTraceElement){
        return "< " + errorMsg + " > " + formattedErrorStack(e, stackTraceElement);
    }

    public static String getErrorLog(Exception e, StackTraceElement stackTraceElement){
        return formattedErrorStack(e, stackTraceElement);
    }

    private static String formattedErrorStack(Exception e, StackTraceElement stackTraceElement){
        return stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName() + ": " + stackTraceElement.getLineNumber()  + " - " +  e.getClass()  + ": " +  e.getMessage();
    }

    public static String getErrorStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage() + System.lineSeparator());
        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            sb.append(String.format("%s %s.%s:%s",stackTraceElement.getFileName(), stackTraceElement.getClassName(),
                    stackTraceElement.getMethodName(), stackTraceElement.getLineNumber()) + System.lineSeparator());
        }
        return sb.toString();
    }
}
