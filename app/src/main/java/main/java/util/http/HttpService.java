package main.java.util.http;

import static main.java.util.http.constant.RequestConstant.CONNECTION_TIME_OUT_VALUE;
import static main.java.util.http.constant.RequestConstant.JSON_CONTENT_TYPE;
import static main.java.util.http.constant.RequestConstant.POST_METHOD_NAME;
import static main.java.util.http.constant.RequestConstant.READ_TIME_OUT_VALUE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import main.java.util.http.request.CommonRequest;

public class HttpService {
    public String post(CommonRequest request) throws IOException {
        String response;

        URL myUrl = new URL(request.getUrl());
        HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
        connection.setConnectTimeout(CONNECTION_TIME_OUT_VALUE);
        connection.setReadTimeout(READ_TIME_OUT_VALUE);

        connection.setDoOutput(true);

        connection.setRequestMethod(POST_METHOD_NAME);
        connection.setRequestProperty("Content-Type", JSON_CONTENT_TYPE);
        connection.setRequestProperty("Authorization", "Bearer " + request.getKey());

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(getRequestBodyBytes(request.toPostRequestString()));

        response = getResponseByInputStream(connection.getInputStream());

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
            closeBufferedReader(br);
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
