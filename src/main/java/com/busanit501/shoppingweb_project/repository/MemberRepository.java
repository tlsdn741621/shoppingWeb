package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @EntityGraph(attributePaths = {"role", "addresses"})
    Optional<Member> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);

    // 이메일로 회원 찾기 (roleSet이 아니라 role 필드이므로 수정)
    @EntityGraph(attributePaths = {"role", "addresses"})
    Optional<Member> findByEmail(String email);

    // memberId로 회원 찾기 (social 필드는 없으니 제거)
    @EntityGraph(attributePaths = {"role"})
    @Query("select m from Member m where m.memberId = :memberId")
    Optional<Member> getWithRoles(@Param("memberId") String memberId);

    // 비밀번호 업데이트 (필드명 맞춤)
    @Modifying
    @Transactional
    @Query("update Member m set m.password = :password where m.memberId = :memberId")
    void updatePassword(@Param("password") String password, @Param("memberId") String memberId);

    @Query("SELECT m FROM Member m LEFT JOIN FETCH m.addresses WHERE m.memberId = :memberId")
    Optional<Member> findByMemberIdWithAddresses(@Param("memberId") String memberId);
}
