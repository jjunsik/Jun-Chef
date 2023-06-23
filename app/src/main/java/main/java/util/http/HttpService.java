package main.java.util.http;

import static main.java.util.http.constant.RequestConstant.GET_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.JSON_CONTENT_TYPE;
import static main.java.util.http.constant.RequestConstant.POST_METHOD_NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import main.java.util.http.request.CommonRequest;

public class HttpService {

    public String post(CommonRequest request) {
        String response = null;

        try {
            // url 입력
            URL myUrl = new URL(request.getUrl());
            HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();

            // output stream 에 값을 입력 하기 위해 설정 변경
            connection.setDoOutput(true);

//             응답 받을 데이터가 있는 경우 true
//            connection.setDoInput(true);
//             요청시 데이터를 보내야 하는 경우 true
//            connection.setDoOutput(true);

            // header 정보 입력
            connection.setRequestMethod(POST_METHOD_NAME);
            connection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);
            connection.setRequestProperty("Authorization", "Bearer " + request.getKey());

            // body 입력
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(getRequestBodyBytes(request.toPostRequestString()));

            // response code 가 200 인지를 확인
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // response
            response = getResponseByInputStream(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String get(CommonRequest request) {
        String response = null;
        HttpURLConnection conn = null;

        try {
            URL url = new URL(request.getUrl() + request.toGetRequestString());

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(GET_METHOD_NAME);

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            response = getResponseByInputStream(conn.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
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
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return stringBuilder.toString();
    }
}
