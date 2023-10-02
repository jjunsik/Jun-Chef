package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

import androidx.annotation.NonNull;

public class ChangePasswordRequestDto {
    private final Long memberId;
    private final String newPasswd;

    public ChangePasswordRequestDto(Long memberId, String changePW) {
        this.memberId = memberId;
        this.newPasswd = changePW;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getNewPasswd() {
        return newPasswd;
    }

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/" + getMemberId();
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"newPasswd:\"" + "\"" + newPasswd + "\"" +
                '}';
    }
}
