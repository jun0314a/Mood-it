// src/main/java/com/example/Group/service/PostService.java
package com.example.group.service;

import java.util.List;

import com.example.group.dto.PostCreateRequest;
import com.example.group.dto.PostDto;

public interface PostService {
    PostDto createGroupPost(Integer groupId, PostCreateRequest req);
    List<PostDto> getPostsByGroup(Integer groupId);
}
