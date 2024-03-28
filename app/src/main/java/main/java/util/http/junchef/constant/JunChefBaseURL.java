package main.java.util.http.junchef.constant;

public class JunChefBaseURL {
    // ngrok URL 주소
    public static final String JUN_CHEF_MEMBER_BASE_URL = "https://0e7c-110-13-68-190.ngrok-free.app/jun-chef/v1/members";
    public static final String JUN_CHEF_RECIPE_BASE_URL = "https://0e7c-110-13-68-190.ngrok-free.app/jun-chef/v1/recipe";
    public static final String JUN_CHEF_HISTORY_BASE_URL = "https://0e7c-110-13-68-190.ngrok-free.app/jun-chef/v1/history";

    // AVD는 나의 컴퓨터와 격리되어 가상 라우터와 방화벽 서비스 뒤에서 실행된다.
    // 따라서 내 컴퓨터의 로컬 ip 주소인 127.0.0.1은 AVD에서 10.0.2.2로 사용해야 한다.
    public static final String JUN_CHEF_MEMBER_BASE_URL = "http://10.0.2.2:8080/jun-chef/v1/members";
    public static final String JUN_CHEF_RECIPE_BASE_URL = "http://10.0.2.2:8080/jun-chef/v1/recipe";
    public static final String JUN_CHEF_HISTORY_BASE_URL = "http://10.0.2.2:8080/jun-chef/v1/history";
}