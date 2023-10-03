package main.java.util.http.junchef.history.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_HISTORY_BASE_URL;

public class GetHistoryIdRequestDto {
    private final Long memberId;
    private final String recipeName;

    public GetHistoryIdRequestDto(Long memberId, String recipeName) {
        this.memberId = memberId;
        this.recipeName = recipeName;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getUrl() {
        return JUN_CHEF_HISTORY_BASE_URL + "/" + getMemberId() + "/" + getRecipeName();
    }
}
