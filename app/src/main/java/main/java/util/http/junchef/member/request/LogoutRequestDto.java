package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

import androidx.annotation.NonNull;

public class LogoutRequestDto {

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/logout";
    }
}
