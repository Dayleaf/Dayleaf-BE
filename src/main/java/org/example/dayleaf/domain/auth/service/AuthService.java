package org.example.dayleaf.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.dayleaf.domain.auth.dto.request.LoginRequest;
import org.example.dayleaf.domain.auth.dto.request.ProfileRequest;
import org.example.dayleaf.domain.auth.dto.request.RefreshRequest;
import org.example.dayleaf.domain.auth.dto.request.SignUpRequest;
import org.example.dayleaf.domain.auth.dto.response.TokenResponse;
import org.example.dayleaf.domain.member.entity.Member;
import org.example.dayleaf.domain.member.entity.MemberRole;
import org.example.dayleaf.domain.member.repository.MemberRepository;
import org.example.dayleaf.global.exception.CustomException;
import org.example.dayleaf.global.exception.ErrorCode;
import org.example.dayleaf.global.jwt.JwtTokenProvider;
import org.example.dayleaf.global.redis.RefreshTokenRepository;
import org.example.dayleaf.global.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse signUp(SignUpRequest request) {
        if (memberRepository.existsByLoginId(request.loginId())) {
            throw new CustomException(ErrorCode.LOGIN_ID_ALREADY_EXISTS);
        }

        Member member = Member.createAccount(
                request.loginId(),
                passwordEncoder.encode(request.password())
        );
        memberRepository.save(member);

        return issueTokens(member);
    }

    @Transactional
    public TokenResponse completeProfile(ProfileRequest request) {
        Long memberId = SecurityUtils.getCurrentMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getRole() != MemberRole.GUEST) {
            throw new CustomException(ErrorCode.PROFILE_ALREADY_COMPLETED);
        }

        if (memberRepository.existsByNickname(request.nickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        member.completeProfile(request.nickname(), request.email());

        return issueTokens(member);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        return issueTokens(member);
    }

    @Transactional(readOnly = true)
    public TokenResponse refresh(RefreshRequest request) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(request.refreshToken());

        String stored = refreshTokenRepository.find(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!stored.equals(request.refreshToken())) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return issueTokens(member);
    }

    public void logout() {
        Long memberId = SecurityUtils.getCurrentMemberId();
        refreshTokenRepository.delete(memberId);
    }

    private TokenResponse issueTokens(Member member) {
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        refreshTokenRepository.save(member.getId(), refreshToken);
        return TokenResponse.of(accessToken, refreshToken);
    }
}
