package org.grace.matjibbacked.repository;

import org.grace.matjibbacked.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths =  {"memberRoleList"}) // 연관관계를 같이 가져오기 위한 어노테이션
    @Query("select m from Member m where m.email = :email")
    Member getWithRole(@Param("email") String email); // email을 통해 회원과 권한을 가져오는 메서드

}
