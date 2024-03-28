package main.java.util.parser.junchef.session;

import android.util.Log;

public class SessionIdParser {
    // Set-Cookie 헤더에서 JSESSIONID 쿠키의 값을 파싱하여 반환하는 메서드
    // cookiesHeader 인자는 HttpURLConnection으로부터 얻은 Set-Cookie 헤더의 값이다.
    public static String parseSessionId(String cookiesHeader) {
        Log.d("JunChef", "Response에서 세션 ID 파싱");
        if (cookiesHeader == null) {
            return null;
        }

        String[] cookies = cookiesHeader.split(";\\s*");

        for (String cookie : cookies) {
            if (cookie.startsWith("JSESSIONID=")) {
                return cookie.split("=")[1];
            }
        }

        return null;
    }
}
