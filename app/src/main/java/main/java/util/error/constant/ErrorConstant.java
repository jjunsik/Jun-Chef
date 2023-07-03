package main.java.util.error.constant;

import java.util.HashMap;
import java.util.Map;

import main.java.util.error.ErrorFormat;

public class ErrorConstant {

    public static final String NETWORK_ERROR_TITLE = "네트워크 연결 오류";
    public static final String NETWORK_ERROR_MESSAGE = "\n인터넷 연결 상태를 확인해 주세요.";
    public static final String SEARCH_ERROR_TITLE = "검색 오류";
    public static final String SEARCH_ERROR_MESSAGE = "\n검색어를 다시 입력해 주세요.";
    public static final String API_ERROR_TITLE = "오류";
    public static final String API_ERROR_MESSAGE = "\n잠시 후 다시 시도해 주세요.";

    private static final Map<String, String> messageToTitle = new HashMap<>();
    private static final String SEPARATOR = "---";

    static {
        messageToTitle.put(NETWORK_ERROR_MESSAGE, NETWORK_ERROR_TITLE);
        messageToTitle.put(SEARCH_ERROR_MESSAGE, SEARCH_ERROR_TITLE);
        messageToTitle.put(API_ERROR_MESSAGE, API_ERROR_TITLE);
    }

    public static String getErrorTitle(String message) {
        if (messageToTitle.containsKey(message))
            return messageToTitle.get(message);

        return "";
    }

    public static ErrorFormat getErrorFromMessage(String message) {
        String final_message = message.split(SEPARATOR)[1];
        return new ErrorFormat(getErrorTitle(final_message), final_message);
    }

    public static String getNetworkErrorMessage() {
        return "---" + NETWORK_ERROR_MESSAGE;
    }

    public static String getSearchErrorMessage() {
        return "---" + SEARCH_ERROR_MESSAGE;
    }

    public static String getAPIErrorMessage() {
        return "---" + API_ERROR_MESSAGE;
    }
}
