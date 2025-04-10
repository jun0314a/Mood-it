package com.example.user_account.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(
    name = "tbl_users",
    uniqueConstraints = {
        @UniqueConstraint(name = "unique_email", columnNames = "email"),
        @UniqueConstraint(name = "unique_username", columnNames = "username"),
        @UniqueConstraint(name = "unique_phone", columnNames = "phone_number")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Email(message = "올바른 이메일 형식을 입력해주세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "이름은 필수 항목입니다.")
    private String username;

    @Column(nullable = false)
    private String birthdate;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;
}
