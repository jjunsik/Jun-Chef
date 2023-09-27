package main.java.util.http.junchef.recipe.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_RECIPE_BASE_URL;

public class GetRecipeRequestDto {
    private final Long memberId;

    private final String word;

    public GetRecipeRequestDto(Long memberId, String word) {
        this.memberId = memberId;
        this.word = word;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getWord() {
        return word;
    }

    public String getUrl() {
        return JUN_CHEF_RECIPE_BASE_URL + "/" + getMemberId() + "?search=" + getWord();
    }
}
