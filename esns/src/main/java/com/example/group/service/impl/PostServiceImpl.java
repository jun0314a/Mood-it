package com.example.group.service.impl;

import com.example.group.dto.PostCreateRequest;
import com.example.group.dto.PostDto;
import com.example.group.entity.Post;
import com.example.group.entity.PostType;
import com.example.group.repository.PostRepository;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements com.example.group.service.PostService {
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    @Override
    public PostDto createGroupPost(Integer groupId, PostCreateRequest req) {
        User user = userRepo.findById(req.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Post p = Post.builder()
                .user(user)
                .authorName(req.getAuthorName())
                .groupId(groupId)
                .title(req.getTitle())
                .content(req.getContent())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likes(0)
                .tag(req.getTag())
                .postType(PostType.group)
                .build();

        postRepo.save(p);
        return toDto(p);
    }

    @Override
    public List<PostDto> getPostsByGroup(Integer groupId) {
        List<Post> posts = postRepo.findByGroupId(groupId);
        return posts.stream().map(this::toDto).toList();
    }

    private PostDto toDto(Post p) {
        return PostDto.builder()
                .postId(p.getPostId())
                .userId(p.getUser().getId())
                .authorName(p.getAuthorName())
                .groupId(p.getGroupId())
                .title(p.getTitle())
                .content(p.getContent())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .likes(p.getLikes())
                .tag(p.getTag())
                .postType(p.getPostType())
                .build();
    }
}
