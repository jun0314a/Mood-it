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

    // 🚨 여기를 수정: 스토리 이미지 저장 경로를 정확히 주입받습니다.
    @Value("${file.upload-dir.story}") // <-- upload.path 대신 file.upload-dir.story 사용
    private String storyUploadDir; // 변수 이름도 storyUploadDir로 변경하는 것이 좋습니다.

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

            // 🚨 여기를 수정: 파일을 storyUploadDir에 저장하도록 변경
            Path targetPath = Paths.get(storyUploadDir).resolve(filename);
            
            // 저장 디렉토리가 존재하지 않으면 생성
            File dir = new File(storyUploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 🚨 여기를 수정: URL에 /story/ 서브 디렉토리를 포함시킵니다.
            String imageUrl = "/uploads/story/" + filename; // <-- /uploads/ 뒤에 story/ 추가
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity
                .status(500)
                .body("파일 업로드 실패: " + e.getMessage());
        }
    }
}