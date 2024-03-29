package main.java.util.http.junchef.member;

import static main.java.util.http.constant.RequestConstant.DELETE_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.GET_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.JSON_CONTENT_TYPE;
import static main.java.util.http.constant.RequestConstant.POST_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.PUT_METHOD_NAME;
import static main.java.util.http.junchef.savesession.EncryptedPreferencesManager.getSessionIdFromEncryptedSharedPreferences;
import static main.java.util.http.junchef.savesession.EncryptedPreferencesManager.saveSessionIdInEncryptedSharedPreferences;

import android.content.Context;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionContent;
import main.java.util.http.junchef.member.request.ChangePasswordRequestDto;
import main.java.util.http.junchef.member.request.DeleteMemberRequestDto;
import main.java.util.http.junchef.member.request.GetMemberRequestDto;
import main.java.util.http.junchef.member.request.JoinMemberRequestDto;
import main.java.util.http.junchef.member.request.LoginRequestDto;
import main.java.util.http.junchef.member.request.LogoutRequestDto;
import main.java.util.parser.junchef.session.SessionIdParser;

public class MemberHttpService {
    private final Context context;

    public MemberHttpService(Context context) {
        this.context = context;
    }

    public String getMember(GetMemberRequestDto getMemberRequestDto) {
        return getAndDelete(getMemberRequestDto.getUrl(), GET_METHOD_NAME);
    }

    public String joinMember(JoinMemberRequestDto joinMemberRequestDto) {
        return loginAndJoin(joinMemberRequestDto.getUrl(), joinMemberRequestDto.toString());
    }

    public String login(LoginRequestDto loginRequestDto) {
        return loginAndJoin(loginRequestDto.getUrl(), loginRequestDto.toString());
    }

    public void logout(LogoutRequestDto logoutRequestDto) {
        logout(logoutRequestDto.getUrl());
    }

    public String changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        return postAndPut(changePasswordRequestDto.getUrl(), PUT_METHOD_NAME, changePasswordRequestDto.toString());
    }

    public String deleteMember(DeleteMemberRequestDto deleteMemberRequestDto) {
        return getAndDelete(deleteMemberRequestDto.getUrl(), DELETE_METHOD_NAME);
    }

    @Nullable
    private String getAndDelete(String memberUrl, String methodType) {
        String response = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(memberUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(methodType);

            String sessionId = getSessionIdFromEncryptedSharedPreferences(context);

            // 세션 ID가 있으면 요청 헤더에 추가
            if (sessionId != null && !sessionId.isEmpty()) {
                httpURLConnection.setRequestProperty("Cookie", "JSESSIONID=" + sessionId);
            }

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            response = getResponseByInputStream(httpURLConnection, httpURLConnection.getInputStream());

            // 쿠키 헤더 가져오기
            String cookiesHeader = httpURLConnection.getHeaderField("Set-Cookie");

            // 세션 ID 파싱
            String newSessionId = SessionIdParser.parseSessionId(cookiesHeader);

            assert sessionId != null;
            if (!sessionId.equals(newSessionId)) {
                saveSessionIdInEncryptedSharedPreferences(newSessionId, context);
            }


        } catch (ConnectException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            // 아이디, 비밀번호를 모두 입력하지 않고 로그인 or 회원 가입 버튼을 누르면 java.io.FileNotFoundException(IOException을 상속 받은 예외임) 발생하므로 이에 대한 예외 처리 해야 됨.
            throw new JunChefException(JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getCode(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getTitle(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getMessage());

        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

        return response;
    }

    @Nullable
    private String loginAndJoin(String url, String bodyRequest) {
        String response = null;
        HttpURLConnection httpURLConnection;

        try{
            URL myUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) myUrl.openConnection();

            httpURLConnection.setDoOutput(true);

            // header
            httpURLConnection.setRequestMethod(POST_METHOD_NAME);
            httpURLConnection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);

            // body
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(getRequestBodyBytes(bodyRequest));

            response = getResponseByInputStream(httpURLConnection, httpURLConnection.getInputStream());

        } catch (ConnectException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e){
            e.printStackTrace();
            throw new JunChefException(JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getCode(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getTitle(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getMessage());
        }

        return response;
    }

    @Nullable
    private String postAndPut(String url, String methodType, String bodyRequest) {
        String response = null;
        HttpURLConnection httpURLConnection;

        try{
            URL myUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) myUrl.openConnection();

            httpURLConnection.setDoOutput(true);

            // 세션 ID 불러오기
            String sessionId = getSessionIdFromEncryptedSharedPreferences(context);

            // header
            httpURLConnection.setRequestMethod(methodType);
            httpURLConnection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);
            httpURLConnection.addRequestProperty("Cookie", "JSESSIONID=" + sessionId); // 세션 ID를 Cookie 헤더에 추가

            // body
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(getRequestBodyBytes(bodyRequest));

            response = getResponseByInputStream(httpURLConnection, httpURLConnection.getInputStream());

            // 쿠키 헤더 가져오기
            String cookiesHeader = httpURLConnection.getHeaderField("Set-Cookie");

            // 세션 ID 파싱
            String newSessionId = SessionIdParser.parseSessionId(cookiesHeader);

            if (!sessionId.equals(newSessionId)) {
                saveSessionIdInEncryptedSharedPreferences(newSessionId, context);
            }

        } catch (ConnectException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e){
            e.printStackTrace();
            throw new JunChefException(JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getCode(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getTitle(), JunChefExceptionContent.NON_EXIST_MEMBER_ERROR.getMessage());
        }

        return response;
    }

    private void logout(String url) {
        HttpURLConnection httpURLConnection;

        try{
            URL myUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) myUrl.openConnection();

            httpURLConnection.setDoOutput(true);

            // header
            httpURLConnection.setRequestMethod(POST_METHOD_NAME);
            httpURLConnection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);

        } catch (ConnectException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private byte[] getRequestBodyBytes(String string) {
        return string.getBytes();
    }

    private String getResponseByInputStream(HttpURLConnection httpURLConnection, InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            // 쿠키 헤더 가져오기
            String cookiesHeader = httpURLConnection.getHeaderField("Set-Cookie");

            // 세션 ID 파싱
            String sessionId = SessionIdParser.parseSessionId(cookiesHeader);

            if (sessionId != null) {
                saveSessionIdInEncryptedSharedPreferences(sessionId, context);
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            closeBufferedReader(br, inputStream); // closeBufferedReader() 메서드를 호출하여 BufferedReader를 안전하게 닫음
        }

        return stringBuilder.toString();
    }

    private void closeBufferedReader(BufferedReader br, InputStream inputStream) {
        if (br != null) {
            try {
                br.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
