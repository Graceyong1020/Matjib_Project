package org.grace.matjibbacked.service;

import org.grace.matjibbacked.domain.Member;
import org.grace.matjibbacked.dto.MemberDTO;
import org.grace.matjibbacked.dto.MemberModifyDTO;

import java.util.stream.Collectors;

public interface MemberService {

    MemberDTO getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDTO memberModifyDTO);

    default MemberDTO entityToDTO(Member member) {
        MemberDTO dto = new MemberDTO(
                member.getEmail(),
                member.getPw(),
                member.getNickname(),
                member.isSocial(),
                member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
        return dto;

    }
}
