package main.java.util.http.junchef.history.request;

import static main.java.util.http.junchef.constant.JunChefBaseURL.JUN_CHEF_HISTORY_BASE_URL;

public class DeleteHistoryRequestDto {
    private final Long historyId;

    public DeleteHistoryRequestDto(Long historyId) {
        this.historyId = historyId;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public String getUrl() {
        return JUN_CHEF_HISTORY_BASE_URL + "/" + getHistoryId();
    }
}
