package org.example.dayleaf.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // ================= COMMON =================
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "COMMON_400", "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500", "서버 오류가 발생했습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL_500", "외부 API 통신 중 오류가 발생했습니다."),

    // ================= AUTH =================
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_401", "인증이 필요합니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_002", "유효하지 않거나 만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_003", "유효하지 않은 리프레시 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "잘못된 토큰입니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_005", "아이디 또는 비밀번호가 올바르지 않습니다."),
    PROFILE_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "AUTH_006", "이미 프로필이 완성된 사용자입니다."),

    // ================= MEMBER =================
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404", "사용자를 찾을 수 없습니다."),
    MEMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER_003", "이미 존재하는 사용자입니다."),
    LOGIN_ID_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER_001", "이미 사용 중인 아이디입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "MEMBER_002", "이미 사용 중인 닉네임입니다."),

    // ================= NODE =================
    NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "NODE_404", "노드를 찾을 수 없습니다."),

    // ================= SCHEDULE =================
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "SCHEDULE_404", "일정을 찾을 수 없습니다."),
    SCHEDULE_ALREADY_EXISTS(HttpStatus.CONFLICT, "SCHEDULE_409", "이미 일정이 존재하는 노드입니다."),

    // ================= REPEAT =================
    REPEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "REPEAT_404", "반복을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
