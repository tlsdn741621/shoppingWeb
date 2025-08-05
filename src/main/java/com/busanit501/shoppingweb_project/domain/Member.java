package com.busanit501.shoppingweb_project.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    //푸쉬 테스트
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String memberId;

    private String email;
    private String password;

    @Column(name = "user_name")
    private String userName;

    private String phone;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Order> orders = new ArrayList<>();

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false)
    private boolean social;

    @Builder.Default
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public void settingRole(String role){
        this.role = role;
    }

    public boolean isProfileIncomplete(){
        if (this.social){return (phone == null || phone.isBlank() || addresses == null || addresses.isEmpty());
        }
        return false;
    }

    public boolean isKakaoUser(){
        return isSocial();
    }

    public void addOrder(Order order) {
        orders.add(order);
        order.setMember(this); // 양방향 설정
    }

}
