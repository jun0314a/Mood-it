package com.example.admin.service;

import com.example.admin.domain.AdminPost;
import com.example.admin.dto.PostRequest;
import com.example.admin.dto.PostResponse;
import com.example.admin.repository.AdminPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final AdminPostRepository postRepository;

    @Transactional
    public void likePost(Long id) {
        AdminPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikeCount(post.getLikeCount() + 1);
    }

    @Transactional
    public Long savePost(PostRequest postRequest) throws IOException {
        if (postRequest.getUserId() == null) {
            throw new IllegalArgumentException("UserId는 반드시 포함되어야 합니다.");
        }

        AdminPost post = new AdminPost();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(postRequest.getAuthor());
        post.setUserId(postRequest.getUserId());
        post.setBoardId(1L);

        if (postRequest.getImage() != null && !postRequest.getImage().isEmpty()) {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + postRequest.getImage().getOriginalFilename();
            File uploadFile = new File(uploadDir + fileName);
            uploadFile.getParentFile().mkdirs();
            postRequest.getImage().transferTo(uploadFile);

            post.setImageUrl("/" + uploadDir + fileName);
        }

        AdminPost savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    public PostResponse findPostById(Long id) {
        AdminPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor());
    }

    @Transactional
    public Long updatePost(Long id, PostRequest postRequest) {
        AdminPost post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(postRequest.getAuthor());

        if (postRequest.getUserId() != null) {
            post.setUserId(postRequest.getUserId());
        }

        return postRepository.save(post).getId();
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<PostResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor()))
                .collect(Collectors.toList());
    }
}
