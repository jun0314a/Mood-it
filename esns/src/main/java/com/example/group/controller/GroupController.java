package com.example.group.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.group.dto.GroupCreateRequest;
import com.example.group.dto.GroupDto;
import com.example.group.dto.GroupUpdateRequest;
import com.example.group.dto.PostCreateRequest;
import com.example.group.dto.PostDto;
import com.example.group.service.GroupService;
import com.example.group.service.PostService;
import com.example.user_account.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final PostService postService;  // PostService 필드 추가
    private final UserRepository userRepository; // 추가

        private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"))
                    .getId();
        }
        throw new RuntimeException("Unauthenticated access");
    }

    /** 그룹 생성 */
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupCreateRequest request) {
        Long creatorId = getCurrentUserId();
        request.setCreatorId(creatorId);
        return ResponseEntity.ok(groupService.createGroup(request));
    }

    /** 그룹 수정 */
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Integer groupId,
                                                @RequestBody GroupUpdateRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, request));
    }

    /** 그룹 삭제 */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    /** 그룹 가입 */
    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(@PathVariable Integer groupId) {
        Long userId = getCurrentUserId();
        groupService.joinGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    /** 그룹 탈퇴 */
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable Integer groupId) {
        Long userId = getCurrentUserId();
        groupService.leaveGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    /** 그룹 멤버 조회 */
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<Long>> getGroupMembers(@PathVariable Integer groupId) {
        List<Long> members = groupService.getMemberIds(groupId);
        return ResponseEntity.ok(members);
    }

    /** 그룹 상세 조회 */
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupService.getGroup(groupId));
    }

    /** 그룹 검색 (제목, 태그, 감정) */
    @GetMapping("/search")
    public ResponseEntity<List<GroupDto>> searchGroups(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String emotion) {
        return ResponseEntity.ok(groupService.searchGroups(title, tag, emotion));
    }

    /** 그룹 내 게시글 작성 */
    @PostMapping("/{groupId}/posts")
    public ResponseEntity<PostDto> createGroupPost(
            @PathVariable Integer groupId,
            @RequestBody PostCreateRequest req) {
        Long userId = getCurrentUserId();
        req.setAuthorId(userId);
        return ResponseEntity.ok(postService.createGroupPost(groupId, req));
    }

    /** 그룹 내 게시글 조회 */
    @GetMapping("/{groupId}/posts")
    public ResponseEntity<List<PostDto>> getGroupPosts(@PathVariable Integer groupId) {
        return ResponseEntity.ok(postService.getPostsByGroup(groupId));
    }
}