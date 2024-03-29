package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

import androidx.annotation.NonNull;

public class JoinMemberRequestDto {
    private final String name;
    private final String email;
    private final String passwd;

    public JoinMemberRequestDto(String name, String email, String passwd) {
        this.name = name.replaceAll("\\s+", "");
        this.email = email.replaceAll("\\s+", "");
        this.passwd = passwd;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/join";
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"name\":" + "\"" + name + "\"" +
                ", \"email\":"+ "\"" + email + "\"" +
                ", \"passwd\":" + "\"" + passwd + "\"" +
                "}";
    }
}
