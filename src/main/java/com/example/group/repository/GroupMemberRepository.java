package com.example.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.group.entity.GroupMember;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Integer> {
    
    /** 그룹 ID로 멤버 목록 조회 */
    List<GroupMember> findByGroupGroupId(Integer groupId);

    /** 그룹 탈퇴 시 사용 */
    void deleteByGroupGroupIdAndUserId(Integer groupId, Long userId);

    void deleteByGroupGroupId(Integer groupId);

    // 가입 정보 조회용 메서드 추가
    List<GroupMember> findByUserId(Long userId);
}