package com.example.group.service.impl;

import com.example.group.dto.GroupCreateRequest;
import com.example.group.dto.GroupDto;
import com.example.group.dto.GroupUpdateRequest;
import com.example.group.entity.Emotion;
import com.example.group.entity.Group;
import com.example.group.entity.GroupMember;
import com.example.group.entity.Role;
import com.example.group.repository.GroupMemberRepository;
import com.example.group.repository.GroupRepository;
import com.example.group.repository.PostRepository;
import com.example.group.service.GroupService;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepo;
    private final GroupMemberRepository memberRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    @Override
    @Transactional
    public GroupDto createGroup(GroupCreateRequest req) {
        User creator = userRepo.findById(req.getCreatorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Invalid creator ID: " + req.getCreatorId()
                ));
        Emotion emotionEnum;
        try {
            emotionEnum = Emotion.valueOf(req.getEmotion().toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid emotion value: " + req.getEmotion()
            );
        }
        Group group = Group.builder()
                .creator(creator)
                .title(req.getTitle())
                .description(req.getDescription())
                .tags(req.getTags())
                .emotion(emotionEnum)
                .build();
        groupRepo.saveAndFlush(group);
        GroupMember owner = GroupMember.builder()
                .group(group)
                .user(creator)
                .role(Role.owner)
                .joinedAt(LocalDateTime.now())
                .build();
        memberRepo.save(owner);
        return toDto(group);
    }

    @Override
    @Transactional
    public GroupDto updateGroup(Integer groupId, GroupUpdateRequest req) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        if (req.getTitle() != null) group.setTitle(req.getTitle());
        if (req.getDescription() != null) group.setDescription(req.getDescription());
        if (req.getTags() != null) group.setTags(req.getTags());
        if (req.getEmotion() != null) {
            try {
                group.setEmotion(Emotion.valueOf(req.getEmotion().toLowerCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid emotion value: " + req.getEmotion()
                );
            }
        }
        groupRepo.save(group);
        return toDto(group);
    }

    @Override
    @Transactional
    public void deleteGroup(Integer groupId) {
        // 그룹 삭제 전 연관 게시글과 멤버 먼저 삭제
        postRepo.deleteByGroupId(groupId);
        memberRepo.deleteByGroupGroupId(groupId);
        groupRepo.deleteById(groupId);
    }

    @Override
    @Transactional
    public void joinGroup(Integer groupId, Long userId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        GroupMember member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(Role.member)
                .joinedAt(LocalDateTime.now())
                .build();
        memberRepo.save(member);
    }

    @Override
    @Transactional
    public void leaveGroup(Integer groupId, Long userId) {
        memberRepo.deleteByGroupGroupIdAndUserId(groupId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getMemberIds(Integer groupId) {
        return memberRepo.findByGroupGroupId(groupId).stream()
                .map(m -> m.getUser().getId())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDto getGroup(Integer groupId) {
        Group group = groupRepo.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group not found"));
        return toDto(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDto> searchGroups(String title, String tag, String emotion) {
        List<Group> groups;
        if (title != null) {
            groups = groupRepo.findByTitleContaining(title);
        } else if (tag != null) {
            groups = groupRepo.findByTagsContaining(tag);
        } else if (emotion != null) {
            Emotion emotionEnum;
            try {
                emotionEnum = Emotion.valueOf(emotion.toLowerCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Invalid emotion value: " + emotion
                );
            }
            groups = groupRepo.findByEmotion(emotionEnum);
        } else {
            groups = groupRepo.findAll();
        }
        return groups.stream().map(this::toDto).toList();
    }

    private GroupDto toDto(Group group) {
        return GroupDto.builder()
                .groupId(group.getGroupId())
                .creatorId(group.getCreator().getId())
                .title(group.getTitle())
                .description(group.getDescription())
                .tags(group.getTags())
                .emotion(group.getEmotion().name())
                .createdAt(group.getCreatedAt())
                .build();
    }
}
