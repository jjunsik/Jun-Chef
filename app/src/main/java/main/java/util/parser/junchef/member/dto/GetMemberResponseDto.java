package main.java.util.parser.junchef.member.dto;

import java.util.List;

import main.java.model.history.SearchHistory;

public class GetMemberResponseDto {
    private String name;
    private String email;
    private List<SearchHistory> histories;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public List<SearchHistory> getHistories() {
        return histories;
    }
}
