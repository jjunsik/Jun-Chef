package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

import androidx.annotation.NonNull;

public class LoginRequestDto {
    private final String email;

    private final String passwd;

    public LoginRequestDto(String email, String passwd) {
        this.email = email;
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/login";
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"email\":" + "\"" + email + "\"" +
                ", \"passwd\":" + "\"" + passwd + "\"" +
                '}';
    }
}
