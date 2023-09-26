package main.java.util.parser.junchef.member;

import static main.java.controller.constant.ActivityConstant.getMemberId;
import static main.java.controller.constant.ActivityConstant.setMemberId;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import main.java.model.history.SearchHistory;
import main.java.model.member.Member;
import main.java.util.parser.junchef.member.dto.GetMemberResponseDto;
import main.java.util.parser.junchef.member.dto.MemberResponseDto;

public class MemberResponseParser {
    public Member getMember(String response) {
        StringBuilder name = new StringBuilder();
        StringBuilder email = new StringBuilder();
        List<SearchHistory> histories = new ArrayList<>();

        Gson gson = new Gson();

        try {
            GetMemberResponseDto memberResponseDto = gson.fromJson(response, GetMemberResponseDto.class);

            name.append(memberResponseDto.getName());
            email.append(memberResponseDto.getEmail());
            histories.addAll(memberResponseDto.getHistories());

        } catch (JsonSyntaxException j) {
            j.printStackTrace();
        }

        return new Member(name.toString(), email.toString(), histories);
    }

    public Long getMemberIdByResponse(String response) {
        Gson gson = new Gson();

        try {
            MemberResponseDto memberResponseDto = gson.fromJson(response, MemberResponseDto.class);

            setMemberId(memberResponseDto.getMemberId());
            Log.d("회원 ID값", "getMemberIdByResponse: " + getMemberId());

        } catch (JsonSyntaxException j) {
            j.printStackTrace();
            throw j;
        }

        return getMemberId();
    }
}
