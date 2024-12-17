package org.grace.matjibbacked.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.domain.Member;
import org.grace.matjibbacked.domain.MemberRole;
import org.grace.matjibbacked.dto.MemberDTO;
import org.grace.matjibbacked.dto.MemberModifyDTO;
import org.grace.matjibbacked.repository.MemberRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.LinkedHashMap;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public MemberDTO getKakaoMember(String accessToken) { //access token을 받았다는 의미는 이미 인증이 완료되었다는 의미

        //accessToken을 이용해서 카카오 서버에 사용자 정보를 요청
        String nickname = getEmailFromKakaoAccessToken(accessToken);
        //기존에 저장된 사용자인지 확인

        //DB에 저장된 사용자 정보를 가져옴
        Optional<Member> result = memberRepository.findById(nickname);

        if(result.isPresent()) { // 그렇기때문에 사용자가 DB에 존재하는지만 확인하면 됨

            MemberDTO memberDTO = entityToDTO(result.get());
            log.info("memberDTO-------------------------" + memberDTO);
            return memberDTO;
            //이미 가입된 사용자
        }
        //새로운 사용자
        Member socialMember = makeSocialMember(nickname); //Member 객체 생성

        memberRepository.save(socialMember); //DB에 저장

        MemberDTO memberDTO = entityToDTO(socialMember); //Member 객체를 MemberDTO로 변환

        return memberDTO; //MemberDTO 리턴

    }

    @Override
    public void modifyMember(MemberModifyDTO memberModifyDTO) {

        Optional<Member> result = memberRepository.findById(memberModifyDTO.getEmail());
        Member member = result.orElseThrow(); //이미 존재하는 사용자이기 때문에 null일 수 없음

        // social 회원이 아님
        member.changeNickname(memberModifyDTO.getNickname());
        member.changePw(passwordEncoder.encode(memberModifyDTO.getPw()));
        member.changeSocial(false);

        memberRepository.save(member);

    }

    private Member makeSocialMember(String email){
        //email을 이용해서 Member 객체를 생성
        //임시 비밀번호 생성
        //Member 객체에 임시 비밀번호 설정
        //Member 객체 리턴

        String tempPassword = makeTempPassword(); //임시 비밀번호 생성
        log.info("tempPassword-------------------------" + tempPassword);

        Member member = Member.builder()
                .email(email) // nickname을 email로 대체
                .pw(passwordEncoder.encode(tempPassword))
                .nickname("Social Member")
                .social(true)
                .build();

        member.addRole(MemberRole.USER);

        return member;
    }

    private String getEmailFromKakaoAccessToken(String accessToken) {
        //카카오 서버에 accessToken을 이용해서 사용자 정보를 요청

        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me"; //카카오 서버에 사용자 정보를 요청할 URL
        RestTemplate restTemplate = new RestTemplate();

        //요청 헤더 설정 -> accessToken을 이용해서 사용자 정보를 요청
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();

        ResponseEntity<LinkedHashMap> response =
                restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response----------------------------");
        log.info(response);

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody(); //카카오 서버에서 받은 응답 데이터

        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties");

        log.info("kakaoAccount-------------------------" + kakaoAccount);

        String nickname = kakaoAccount.get("nickname");

        log.info("nickname-------------------------" + nickname);

        return nickname;


    }

    private String makeTempPassword() {

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < 10; i++) { //임시 비밀번호 10자리
            buffer.append((char) ((int) (Math.random() * 55) + 65)); //임시 비밀번호 생성
        }
        return buffer.toString();
    }

}
