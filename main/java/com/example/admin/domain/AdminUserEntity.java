package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_id")
    private Long id;

    @Column(name = "Username", nullable = false, length = 50)
    private String username;

    @Column(name = "Email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "Password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "Role", length = 20)
    private String role;

    @Column(name = "Points", nullable = false)
    private int points;

    @Column(name = "Membership_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;

    @Column(name = "Phone_number", unique = true, length = 15)
    private String phoneNumber;

    @Column(name = "Birthdate")
    private Date birthdate;

    @Column(name = "Created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public enum MembershipLevel {
        basic, premium
    }
}
