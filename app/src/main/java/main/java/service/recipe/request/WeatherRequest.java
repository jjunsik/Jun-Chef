package main.java.service.recipe.request;

import main.java.util.http.request.CommonRequest;

public class WeatherRequest extends CommonRequest {

//    "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst" +
//            "?serviceKey=v01QNBuf9Ix9E%2FS2c6PhJ709%2BLBDxeNtrl9FMzYs228mlWYb%2BXvguz594REbkvupOPQLHH8F2gnXl0Odp2Lneg%3D%3D" + /*Service Key*/
//            "&pageNo=1" +/*페이지 번호*/
//            "&numOfRows=1000" + /*한 페이지 결과 수*/
//            "&dataType=JSON" + /*요청 자료 형식(XML/JSON) Default: XML*/
//            "&base_date=20230509" +
//            "&base_time=1530" +
//            "&nx=59" + /*예보 지점의 X 좌표값*/
//            "&ny=123";
    private static final String WEATHER_KEY = "v01QNBuf9Ix9E%2FS2c6PhJ709%2BLBDxeNtrl9FMzYs228mlWYb%2BXvguz594REbkvupOPQLHH8F2gnXl0Odp2Lneg%3D%3D";
    private static final String WEATHER_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
    public final int pageNo = 1;
    public final int numOfRows = 1000;
    public final String dataType = "JSON";
    public final int base_date = 20230509;
    public final int base_time = 1530;
    public final int nx = 59;
    public final int ny = 123;

    @Override
    public String toPostRequestString() {
        return '{' +
                "\"pageNo\":\"" + pageNo +
                ",\"numOfRows\":\"" + numOfRows +
                ",\"dataType\":\"" + dataType + '\"' +
                ",\"base_date\":" + base_date +
                ",\"base_time\":" + base_time +
                ",\"nx\":" + nx +
                ",\"ny\":" + ny +
                '}';
    }

    @Override
    public String toGetRequestString() {
        return "?serviceKey=" + getKey() +
                "&pageNo=" + pageNo +
                "&numOfRows=" + numOfRows +
                "&dataType=" + dataType +
                "&base_date=" + base_date +
                "&base_time=" + base_time +
                "&nx=" + nx +
                "&ny=" + ny;
    }

    @Override
    public String getKey() {
        return WEATHER_KEY;
    }

    @Override
    public String getUrl() {
        return WEATHER_URL;
    }
}
