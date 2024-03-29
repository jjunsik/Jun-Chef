package main.java.service.junchef.member;

import android.util.Log;

import java.util.concurrent.CompletableFuture;

import main.java.model.member.Member;
import main.java.util.error.junchef.JunChefException;
import main.java.util.error.junchef.JunChefExceptionParser;
import main.java.util.http.junchef.member.MemberHttpService;
import main.java.util.http.junchef.member.request.ChangePasswordRequestDto;
import main.java.util.http.junchef.member.request.DeleteMemberRequestDto;
import main.java.util.http.junchef.member.request.GetMemberRequestDto;
import main.java.util.http.junchef.member.request.JoinMemberRequestDto;
import main.java.util.http.junchef.member.request.LoginRequestDto;
import main.java.util.http.junchef.member.request.LogoutRequestDto;
import main.java.util.parser.junchef.member.MemberResponseParser;

public class MemberService {
    private final MemberHttpService memberHttpService;
    private final MemberResponseParser memberResponseParser;
    private final JunChefExceptionParser exceptionParser = new JunChefExceptionParser();
    private JunChefException junChefException;

    public MemberService(MemberHttpService memberHttpService, MemberResponseParser memberResponseParser) {
        this.memberHttpService = memberHttpService;
        this.memberResponseParser = memberResponseParser;
    }

    public CompletableFuture<Long> login (String email, String passwd) {
        return CompletableFuture.supplyAsync( () -> {
            LoginRequestDto loginRequestDto = new LoginRequestDto(email, passwd);

            try {
                String response = memberHttpService.login(loginRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "로그인 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return memberResponseParser.getMemberIdByResponse(response);

            } catch (JunChefException j) {
                Log.d("junchef", "로그인 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }

    public CompletableFuture<Long> joinMember(String name, String email, String passwd) {
        return CompletableFuture.supplyAsync(() -> {
            JoinMemberRequestDto joinMemberRequestDto = new JoinMemberRequestDto(name, email, passwd);
            Log.d("회원 가입", "joinMember: " + joinMemberRequestDto);

            try {
                String response = memberHttpService.joinMember(joinMemberRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "회원 가입 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return memberResponseParser.getMemberIdByResponse(response);

            } catch (JunChefException j) {
                Log.d("junchef", "회원 가입 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }

    public CompletableFuture<Member> getMember(Long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            GetMemberRequestDto getMemberRequestDto = new GetMemberRequestDto(memberId);

            try {
                String response = memberHttpService.getMember(getMemberRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "회원 조회 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return memberResponseParser.getMember(response);

            } catch (JunChefException j) {
                Log.d("junchef", "회원 조회 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }

    public CompletableFuture<Long> changePassword(Long memberId, String newPassword) {
        return CompletableFuture.supplyAsync(() -> {
            ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(memberId, newPassword);

            try {
                String response = memberHttpService.changePassword(changePasswordRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode() != 0) {
                    Log.d("junchef", "비밀번호 변경 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return memberResponseParser.getMemberIdByResponse(response);

            } catch (JunChefException j) {
                Log.d("junchef", "비밀번호 변경 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }

    public CompletableFuture<Void> logout() {
        return CompletableFuture.runAsync(() -> {
            LogoutRequestDto logoutRequestDto = new LogoutRequestDto();
            memberHttpService.logout(logoutRequestDto);
        });
    }

    public CompletableFuture<Long> deleteMember(Long memberId) {
        return CompletableFuture.supplyAsync(() -> {
            DeleteMemberRequestDto deleteMemberRequestDto = new DeleteMemberRequestDto(memberId);

            try {
                String response = memberHttpService.deleteMember(deleteMemberRequestDto);

                junChefException = exceptionParser.getJunChefException(response);

                if (junChefException.getCode()  != 0) {
                    Log.d("junchef", "회원 삭제 예외 받음" + junChefException.getCode() + junChefException.getTitle() + junChefException.getMessage());
                    throw new JunChefException(junChefException.getCode(), junChefException.getTitle(), junChefException.getMessage());
                }

                return memberResponseParser.getMemberIdByResponse(response);

            } catch (JunChefException j) {
                Log.d("junchef", "회원 삭제 에러" + j.getCode() + j.getTitle() + j.getMessage());
                throw j;
            }
        });
    }
}
