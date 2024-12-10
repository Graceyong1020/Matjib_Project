package org.grace.matjibbacked.domain;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "memberRoleList") // 연관관계 있으면 무조건 exclude로 설정 (무한루프 방지)
public class Member {

    @Id
    private String email;

    private String pw;

    private String nickname;

    private boolean social; // 소셜 로그인 여부

    @ElementCollection(fetch = FetchType.LAZY) // 컬렉션을 저장하기 위한 어노테이션
    @Builder.Default
    private List<MemberRole> memberRoleList = new ArrayList<>();

    // 회원의 권한을 추가하는 메서드
    public void addRole(MemberRole memberRole) {
        memberRoleList.add(memberRole);
    }
    // 회원의 권한을 제거하는 메서드
    public void clearRole() {
        memberRoleList.clear();
    }
    //nickname 수정
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    //pw 수정
    public void changePw(String pw) {
        this.pw = pw;
    }
    //social 수정
    public void changeSocial(boolean social) {
        this.social = social;
    }
}
