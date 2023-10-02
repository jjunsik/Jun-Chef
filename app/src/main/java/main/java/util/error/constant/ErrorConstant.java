package main.java.util.error.constant;

import java.util.HashMap;
import java.util.Map;

import main.java.util.error.ErrorFormat;

public class ErrorConstant {
    public static final String CHECK_BUTTON_TEXT = "확인";

    public static final String NETWORK_ERROR_TITLE = "네트워크 연결 오류";
    public static final String NETWORK_ERROR_MESSAGE = "\n인터넷 연결 상태를 확인해 주세요.";

    public static final String API_ERROR_TITLE = "통신 오류";
    public static final String API_ERROR_MESSAGE = "\n잠시 후 다시 시도해 주세요.";

    public static final String SEARCH_WORD_ERROR_TITLE = "검색어 오류";
    public static final String SEARCH_WORD_ERROR_MESSAGE = "\n검색어를 다시 입력해 주세요.";

    public static final String NON_EXIST_RECIPE_ERROR_TITLE = "레시피 오류";
    public static final String NON_EXIST_RECIPE_ERROR_MESSAGE = "\n레시피가 없는 음식입니다.\n검색어 확인 후, 다시 검색해 주세요.";

    public static final String DUPLICATE_MEMBER_ERROR_TITLE = "중복 회원 오류";
    public static final String DUPLICATE_MEMBER_ERROR_MESSAGE = "\n중복된 이메일입니다.\n다른 이메일로 회원 가입해 주세요.";

    public static final String NON_EXIST_MEMBER_ERROR_TITLE = "회원 오류";
    public static final String NON_EXIST_MEMBER_ERROR_MESSAGE = "\n존재하지 않는 회원입니다.\n회원 가입 후 이용해 주세요.";

    public static final String NON_EXIST_MEMBER_EMAIL_ERROR_TITLE = "이메일 오류";
    public static final String NON_EXIST_MEMBER_EMAIL_ERROR_MESSAGE = "\n존재하지 않는 이메일입니다.\n이메일을 확인해 주세요.";

    public static final String MEMBER_PASSWORD_ERROR_TITLE = "비밀번호 오류";
    public static final String MEMBER_PASSWORD_ERROR_MESSAGE = "\n비밀번호가 일치하지 않습니다.\n비밀번호 확인해 주세요.";
    public static final int NUMBER_OF_SEARCHES = 2;

    private static final Map<String, String> messageToTitle = new HashMap<>();
    public static final String SEPARATOR = "---";

    static {
        messageToTitle.put(NETWORK_ERROR_MESSAGE, NETWORK_ERROR_TITLE);
        messageToTitle.put(API_ERROR_MESSAGE, API_ERROR_TITLE);
        messageToTitle.put(SEARCH_WORD_ERROR_MESSAGE, SEARCH_WORD_ERROR_TITLE);
        messageToTitle.put(NON_EXIST_RECIPE_ERROR_TITLE, NON_EXIST_RECIPE_ERROR_MESSAGE);
        messageToTitle.put(DUPLICATE_MEMBER_ERROR_TITLE, DUPLICATE_MEMBER_ERROR_MESSAGE);
        messageToTitle.put(NON_EXIST_MEMBER_ERROR_TITLE, NON_EXIST_MEMBER_ERROR_MESSAGE);
        messageToTitle.put(NON_EXIST_MEMBER_EMAIL_ERROR_TITLE, NON_EXIST_MEMBER_EMAIL_ERROR_MESSAGE);
        messageToTitle.put(MEMBER_PASSWORD_ERROR_TITLE, MEMBER_PASSWORD_ERROR_MESSAGE);
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
        return SEPARATOR + NETWORK_ERROR_MESSAGE;
    }

    public static String getAPIErrorMessage() {
        return SEPARATOR + API_ERROR_MESSAGE;
    }
    public static String getSearchWordErrorMessage() {
        return SEPARATOR + SEARCH_WORD_ERROR_MESSAGE;
    }

    public static String getNonExistRecipeErrorMessage() {
        return SEPARATOR + NON_EXIST_RECIPE_ERROR_MESSAGE;
    }

    public static String getDuplicateMemberErrorMessage() {
        return SEPARATOR + DUPLICATE_MEMBER_ERROR_MESSAGE;
    }

    public static String getNonExistMemberErrorMessage() {
        return SEPARATOR + NON_EXIST_MEMBER_ERROR_MESSAGE;
    }

    public static String getNonExistMemberEmailErrorMessage() {
        return SEPARATOR + NON_EXIST_MEMBER_EMAIL_ERROR_MESSAGE;
    }

    public static String getMemberPasswordErrorMessage() {
        return SEPARATOR + MEMBER_PASSWORD_ERROR_MESSAGE;
    }
}
