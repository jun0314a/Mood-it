package com.example.admin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "`Like`") // 백틱(`)으로 감싸서 MySQL 예약어 충돌 회피
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "User_id", nullable = false)
    private AdminUserEntity user;

    @ManyToOne
    @JoinColumn(name = "Post_id")
    private AdminPost post;

    @ManyToOne
    @JoinColumn(name = "Comment_id")
    private AdminComment comment;
}
