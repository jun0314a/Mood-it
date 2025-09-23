// src/main/java/com/example/admin/service/PostService.java

package com.example.admin.service;

import com.example.admin.domain.AdminPost;
import com.example.admin.domain.AdminUserEntity;
import com.example.admin.dto.PostRequest;
import com.example.admin.dto.PostResponse;
import com.example.admin.mapper.PostMapper;
import com.example.admin.repository.AdminPostRepository;
import com.example.admin.repository.AdminUserRepository;
import com.example.auth.service.CustomUserDetails;
import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final AdminPostRepository postRepository;
    private final AdminUserRepository userRepository;
    
    @Value("${file.upload-dir.story}")
    private String uploadDir;

    @Transactional
    public void likePost(Long id) {
        AdminPost post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        post.setLikes(post.getLikes() + 1);
    }
    
    @Transactional
    public Long savePost(PostRequest postRequest) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getUserId();
        AdminUserEntity author = userRepository.findById(currentUserId)
            .orElseThrow(() -> new ResourceNotFoundException("작성자를 찾을 수 없습니다."));

        String imageUrl = null;
        if (postRequest.getImage() != null && !postRequest.getImage().isEmpty()) {
            imageUrl = uploadImage(postRequest.getImage());
        }

        AdminPost post = AdminPost.builder()
            .title(postRequest.getTitle())
            .content(postRequest.getContent())
            .user(author)
            .imageUrl(imageUrl)
            .build();
        
        AdminPost savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional
    public Long updatePost(Long id, PostRequest postRequest) throws IOException {
        AdminPost post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getUserId();

        if (!post.getUser().getId().equals(currentUserId)) {
            throw new BadRequestException("해당 게시글을 수정할 권한이 없습니다.");
        }
        
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());

        if (postRequest.getImage() != null && !postRequest.getImage().isEmpty()) {
            String newImageUrl = uploadImage(postRequest.getImage());
            post.setImageUrl(newImageUrl);
        }

        return post.getId();
    }

    @Transactional
    public void deletePost(Long id) {
        AdminPost post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long currentUserId = userDetails.getUserId();

        if (!post.getUser().getId().equals(currentUserId)) {
            throw new BadRequestException("해당 게시글을 삭제할 권한이 없습니다.");
        }
        postRepository.delete(post);
    }
    
    @Transactional(readOnly = true)
    public PostResponse findPostById(Long id) {
        AdminPost post = postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        return PostMapper.toPostResponse(post);
    }
    
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        List<AdminPost> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return posts.stream()
            .map(PostMapper::toPostResponse)
            .collect(Collectors.toList());
    }

    private String uploadImage(MultipartFile imageFile) throws IOException {
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
            throw new IOException("업로드 디렉토리를 생성할 수 없습니다.");
        }
        File uploadFile = new File(uploadDirFile, fileName);
        imageFile.transferTo(uploadFile);
        return "/uploads/story/" + fileName;
    }
}