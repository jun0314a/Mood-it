package com.example.story.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/story-files")
public class StoryFileController {

    @Value("${file.upload-dir.story}")
    private String uploadDir;

    @Operation(
      summary = "스토리 이미지 업로드",
      description = "Multipart 형태로 전달된 이미지를 서버에 저장하고, 접근 가능한 URL을 반환합니다."
    )
    @ApiResponses({
      @ApiResponse(
        responseCode = "200",
        description = "업로드 성공",
        content = @Content(
          mediaType = "text/plain",
          schema = @Schema(type = "string", example = "/uploads/uuid_filename.jpg")
        )
      ),
      @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    
    @PostMapping("/upload")
    
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 원본 파일명이 null 이면 예외 발생
            String original = Objects.requireNonNull(
                file.getOriginalFilename(),
                "업로드된 파일에 파일명이 없습니다."
            );
            // 이제 Null 안전하게 cleanPath 호출
            String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(original);

            Path targetPath = Paths.get(uploadDir).resolve(filename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String imageUrl = "/uploads/story/" + filename;
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(500)
                .body("파일 업로드 실패: " + e.getMessage());
        }
    }
}
