package org.grace.matjibbacked.repository;


import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Member;
import org.grace.matjibbacked.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Log4j2
@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsert() {

        for(int i = 0; i <10; i++) {

            Member member = Member.builder()
                    .email("user" + i + "@bbb.com")
                    .pw(passwordEncoder.encode("1111"))
                    .nickname("사용자" + i)
                    .build();

            member.addRole(MemberRole.USER);

            if(i>=5){
                member.addRole(MemberRole.MANAGER);
            }
            if(i>=8){
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }

    }
    @Test
    public void testRead() {
        String email = "user9@bbb.com";
        Member member = memberRepository.getWithRole(email);
    }
}
