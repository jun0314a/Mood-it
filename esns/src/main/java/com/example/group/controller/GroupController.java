package com.example.group.controller;

import com.example.group.dto.GroupDto;
import com.example.group.dto.GroupCreateRequest;
import com.example.group.dto.GroupUpdateRequest;
import com.example.group.service.GroupService;
import com.example.group.dto.PostCreateRequest;
import com.example.group.dto.PostDto;
import com.example.group.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Group", description = "그룹 관련 기능")
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final PostService postService;

    @Operation(summary = "그룹 생성", description = "새 그룹을 생성합니다. creatorId 파라미터와 함께 요청합니다.")
    @PostMapping
    public ResponseEntity<GroupDto> createGroup(
            @RequestParam("creatorId") Long creatorId,
            @RequestBody GroupCreateRequest request) {
        request.setCreatorId(creatorId);
        return ResponseEntity.ok(groupService.createGroup(request));
    }

    @Operation(summary = "그룹 수정", description = "기존 그룹의 이름 및 설명을 변경합니다.")
    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Integer groupId,
                                                @RequestBody GroupUpdateRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, request));
    }

    @Operation(summary = "그룹 삭제", description = "ID에 해당하는 그룹을 삭제합니다.")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer groupId) {
        groupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "그룹 가입", description = "사용자를 그룹에 가입시킵니다.")
    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(@PathVariable Integer groupId,
                                          @RequestParam Long userId) {
        groupService.joinGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "그룹 탈퇴", description = "사용자를 그룹에서 탈퇴시킵니다.")
    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable Integer groupId,
                                           @RequestParam Long userId) {
        groupService.leaveGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "그룹 멤버 조회", description = "그룹의 모든 멤버 ID 목록을 조회합니다.")
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<Long>> getGroupMembers(@PathVariable Integer groupId) {
        List<Long> members = groupService.getMemberIds(groupId);
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "그룹 상세 조회", description = "ID에 해당하는 그룹의 상세 정보를 조회합니다.")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Integer groupId) {
        return ResponseEntity.ok(groupService.getGroup(groupId));
    }

    @Operation(summary = "그룹 검색", description = "제목, 태그 또는 감정으로 그룹을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<GroupDto>> searchGroups(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String emotion) {
        return ResponseEntity.ok(groupService.searchGroups(title, tag, emotion));
    }

    @Operation(summary = "그룹 내 게시글 작성", description = "지정된 그룹에 새 게시글을 작성합니다.")
    @PostMapping("/{groupId}/posts")
    public ResponseEntity<PostDto> createGroupPost(
        @PathVariable Integer groupId,
        @RequestBody PostCreateRequest req) {
        return ResponseEntity.ok(postService.createGroupPost(groupId, req));
    }

    @Operation(summary = "그룹 내 게시글 조회", description = "그룹의 모든 게시글을 조회합니다.")
    @GetMapping("/{groupId}/posts")
    public ResponseEntity<List<PostDto>> getGroupPosts(@PathVariable Integer groupId) {
        return ResponseEntity.ok(postService.getPostsByGroup(groupId));
    }

        @Operation(
      summary = "내 그룹 목록 조회",
      description = "PathVariable로 받은 userId가 가입한 그룹 리스트를 반환합니다."
    )
    @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "404", description = "해당 사용자가 없습니다.")
    })
    @GetMapping
    public ResponseEntity<List<GroupDto>> getMyGroups(@PathVariable Long userId) {
        List<GroupDto> groups = groupService.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }
}
