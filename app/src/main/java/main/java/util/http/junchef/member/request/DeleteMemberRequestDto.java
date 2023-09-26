package main.java.util.http.junchef.member.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_MEMBER_BASE_URL;

public class DeleteMemberRequestDto {
    private final Long memberId;

    public DeleteMemberRequestDto(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getUrl() {
        return JUN_CHEF_MEMBER_BASE_URL + "/" + getMemberId();
    }
}
