package com.example.group.service;

import java.util.List;

import com.example.group.dto.GroupCreateRequest;
import com.example.group.dto.GroupDto;
import com.example.group.dto.GroupUpdateRequest;

/**
 * 그룹 관련 비즈니스 로직 정의
 */
public interface GroupService {
    GroupDto createGroup(GroupCreateRequest request);
    GroupDto updateGroup(Integer groupId, GroupUpdateRequest request);
    void deleteGroup(Integer groupId);
    void joinGroup(Integer groupId, Long userId);
    void leaveGroup(Integer groupId, Long userId);
    List<Long> getMemberIds(Integer groupId);
    List<GroupDto> searchGroups(String title, String tag, String emotion);
    GroupDto getGroup(Integer groupId);

    // 🔽 새로 추가
    /**
     * 사용자가 가입한 그룹 리스트 조회
     * @param userId 조회할 사용자 ID
     * @return 가입된 그룹의 DTO 리스트
     */
    List<GroupDto> getGroupsByUser(Long userId);
}