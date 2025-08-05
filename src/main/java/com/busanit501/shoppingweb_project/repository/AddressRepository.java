package com.busanit501.shoppingweb_project.repository;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findByMemberAndIsDefaultTrue(Member member);

    List<Address> findByMember(Member member);

    // 기본 배송지 초기화
    @Modifying
    @Transactional
    @Query("update Address a set a.isDefault = false where a.member.id = :memberId")
    int resetDefaultAddress(@Param("memberId") Long memberId);

}
