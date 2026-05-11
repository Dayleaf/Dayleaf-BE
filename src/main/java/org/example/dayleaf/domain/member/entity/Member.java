package org.example.dayleaf.domain.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dayleaf.global.base.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 16)
    private String nickname;

    @Column(length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    public static Member createAccount(String loginId, String encodedPassword) {
        Member member = new Member();
        member.loginId = loginId;
        member.password = encodedPassword;
        member.role = MemberRole.GUEST;
        return member;
    }

    public void completeProfile(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
        this.role = MemberRole.USER;
    }
}
