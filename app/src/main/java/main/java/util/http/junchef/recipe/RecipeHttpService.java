package main.java.util.http.junchef.recipe;

import static main.java.util.http.constant.RequestConstant.GET_METHOD_NAME;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionContent;
import main.java.util.http.junchef.recipe.request.GetRecipeRequestDto;

public class RecipeHttpService {
    public String getRecipeAndAddHistory(GetRecipeRequestDto getRecipeRequestDto) {
        String response = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(getRecipeRequestDto.getUrl());

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(GET_METHOD_NAME);

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
