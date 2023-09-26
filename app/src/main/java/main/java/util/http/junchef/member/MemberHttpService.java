package main.java.util.http.junchef.member;

import static main.java.util.http.constant.RequestConstant.DELETE_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.GET_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.JSON_CONTENT_TYPE;
import static main.java.util.http.constant.RequestConstant.POST_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.PUT_METHOD_NAME;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionContent;
import main.java.util.http.junchef.member.request.ChangePasswordRequestDto;
import main.java.util.http.junchef.member.request.DeleteMemberRequestDto;
import main.java.util.http.junchef.member.request.GetMemberRequestDto;
import main.java.util.http.junchef.member.request.JoinMemberRequestDto;
import main.java.util.http.junchef.member.request.LoginRequestDto;
import main.java.util.http.junchef.member.request.LogoutRequestDto;

public class MemberHttpService {
    public String getMember(GetMemberRequestDto getMemberRequestDto) {
        return requestAndResponseOfGetAndDelete(getMemberRequestDto.getUrl(), GET_METHOD_NAME);
    }

    public String joinMember(JoinMemberRequestDto joinMemberRequestDto) {
        return requestAndResponseOfPostAndPut(joinMemberRequestDto.getUrl(), POST_METHOD_NAME, joinMemberRequestDto.toString());
    }

    public String login(LoginRequestDto loginRequestDto) {
        return requestAndResponseOfPostAndPut(loginRequestDto.getUrl(), POST_METHOD_NAME, loginRequestDto.toString());
    }

    public String logout(LogoutRequestDto logoutRequestDto) {
        return requestAndResponseOfGetAndDelete(logoutRequestDto.getUrl(), GET_METHOD_NAME);
    }

    public String changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        return requestAndResponseOfPostAndPut(changePasswordRequestDto.getUrl(), PUT_METHOD_NAME, changePasswordRequestDto.toString());
    }

    public String deleteMember(DeleteMemberRequestDto deleteMemberRequestDto) {
        return requestAndResponseOfGetAndDelete(deleteMemberRequestDto.getUrl(), DELETE_METHOD_NAME);
    }

    @Nullable
    private String requestAndResponseOfGetAndDelete(String memberUrl, String methodType) {
        String response = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(memberUrl);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(methodType);

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            response = getResponseByInputStream(httpURLConnection.getInputStream());

        } catch (UnknownHostException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }

        return response;
    }

    @Nullable
    private String requestAndResponseOfPostAndPut(String url, String methodType, String bodyRequest) {
        String response = null;
        HttpURLConnection httpURLConnection;

        try{
            URL myUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) myUrl.openConnection();

            httpURLConnection.setDoOutput(true);

            // header
            httpURLConnection.setRequestMethod(methodType);
            httpURLConnection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);

            // body
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(getRequestBodyBytes(bodyRequest));

            response = getResponseByInputStream(httpURLConnection.getInputStream());

        } catch (UnknownHostException u) {
            throw new JunChefException(JunChefExceptionContent.NETWORK_ERROR.getCode(), JunChefExceptionContent.NETWORK_ERROR.getTitle(), JunChefExceptionContent.NETWORK_ERROR.getMessage());

        } catch (IOException e){
            e.printStackTrace();
        }

        return response;
    }

    private byte[] getRequestBodyBytes(String string) {
        return string.getBytes();
    }

    private String getResponseByInputStream(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String inputLine;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((inputLine = br.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeBufferedReader(br); // closeBufferedReader() 메서드를 호출하여 BufferedReader를 안전하게 닫음
        }
        return stringBuilder.toString();
    }

    private void closeBufferedReader(BufferedReader br) {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
