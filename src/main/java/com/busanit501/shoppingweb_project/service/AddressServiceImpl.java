package com.busanit501.shoppingweb_project.service;

import com.busanit501.shoppingweb_project.domain.Address;
import com.busanit501.shoppingweb_project.domain.Member;
import com.busanit501.shoppingweb_project.dto.AddressDTO;
import com.busanit501.shoppingweb_project.repository.AddressRepository;
import com.busanit501.shoppingweb_project.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<Address> getAddressesByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        return addressRepository.findByMember(member);
    }

    @Override
    @Transactional
    public Address addAddress(AddressDTO dto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));

        log.info("기본배송지 여부 (DTO): {}", dto.isDefault());

        Address address = Address.builder()
                .zipcode(dto.getZipcode())
                .addressLine(dto.getAddressLine())
                .addressId(dto.getAddressId())
                .createdAt(LocalDateTime.now())
                .isDefault(dto.isDefault())  // dto에 기본배송지 여부가 있으면 반영
                .member(member)
                .build();

        // 만약 기본배송지라면 기존 기본배송지 초기화
        if (dto.isDefault()) {
            log.info("기존 기본배송지 초기화 시작");
            addressRepository.resetDefaultAddress(member.getId());
            addressRepository.flush();
            log.info("기존 기본배송지 초기화 완료");
            address.setIsDefault(true);
        } else {
            address.setIsDefault(false);
        }

        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long addressId, AddressDTO dto, Long authenticatedMemberId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException("배송지를 찾을 수 없습니다."));
        if (!address.getMember().getId().equals(authenticatedMemberId)) {
            throw new IllegalArgumentException("본인 주소가 아닙니다.");
        }

        address.setZipcode(dto.getZipcode());
        address.setAddressLine(dto.getAddressLine());
        address.setAddressId(dto.getAddressId());

        log.info("updateAddress() 호출됨 - DTO 내용: {}", dto);
        // 기본배송지 변경 시 처리
        if (Boolean.TRUE.equals(dto.isDefault())) {
            log.info("기본배송지 초기화 시작");
            int updatedCount = addressRepository.resetDefaultAddress(address.getMember().getId());
            log.info("기본배송지 초기화 쿼리 실행 결과: {}건 수정됨", updatedCount);
            addressRepository.flush();
            log.info("기본배송지 초기화 완료");
            address.setIsDefault(true);
            log.info("현재 주소 기본배송지로 설정: {}", address.getId());
        } else {
            address.setIsDefault(false);
            log.info("기본배송지 체크 안함, 현재 주소 기본배송지 false로 설정: {}", address.getId());
        }
        Address saved = addressRepository.save(address);
        log.info("주소 업데이트 완료: {}", saved);

        return saved;
    }

    @Override
    public void setDefaultAddress(Long memberId, Long addressId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("회원 정보를 찾을 수 없습니다."));
        addressRepository.resetDefaultAddress(member.getId());
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new NoSuchElementException("배송지를 찾을 수 없습니다."));
        address.setIsDefault(true);
        addressRepository.save(address);
    }

    @Override
    public AddressDTO getAddressById(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 배송지를 찾을 수 없습니다."));

        return AddressDTO.builder()
                .id(address.getId())
                .zipcode(address.getZipcode())
                .addressId(address.getAddressId())
                .addressLine(address.getAddressLine())
                .isDefault(address.isDefault())
                .build();
    }
}