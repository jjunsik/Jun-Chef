package main.java.util.http.junchef.history.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_HISTORY_BASE_URL;

public class GetMemberHistoriesRequestDto {
    private final Long memberId;

    public GetMemberHistoriesRequestDto(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getUrl() {
        return JUN_CHEF_HISTORY_BASE_URL + "/" + getMemberId();
    }
}
