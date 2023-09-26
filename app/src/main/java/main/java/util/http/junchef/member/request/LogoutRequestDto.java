package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

import androidx.annotation.NonNull;

public class LogoutRequestDto {
    private final Long memberId;

    public LogoutRequestDto(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/logout/" + getMemberId();
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"memberId\":" + "\"" +  memberId + "\"" +
                "}";
    }
}
