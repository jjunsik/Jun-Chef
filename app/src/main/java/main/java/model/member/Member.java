package main.java.model.member;

import java.util.List;

import main.java.model.history.SearchHistory;

public class Member {
    private final String name;
    private final String email;
    private String password;
    private final List<SearchHistory> histories;

    public Member(String name, String email, List<SearchHistory> histories) {
        this.name = name;
        this.email = email;
        this.histories = histories;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SearchHistory> getHistories() {
        return histories;
    }
}
