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
}