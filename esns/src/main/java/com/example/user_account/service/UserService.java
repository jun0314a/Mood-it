package com.example.user_account.service;

import java.io.File;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.user_account.dto.UserResponseDto;
import com.example.user_account.dto.UserSignupRequest;
import com.example.user_account.dto.UserUpdateRequest;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;

import org.springframework.security.access.AccessDeniedException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;

    // ─── 여기서 application.properties 의 프로필 업로드 경로를 주입받습니다. ───
    @Value("${file.upload-dir.profile}")
    private String profileUploadDir;
    // ────────────────────────────────────────────────────────────────────────────

    public void registerUser(UserSignupRequest dto) {
        try {
            User userEntity = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .birthdate(dto.getBirthdate())
                .phoneNumber(dto.getPhoneNumber())
                .build();

            userRepository.save(userEntity);
        } catch (Exception ex) {
            System.out.println("🔥 예외 클래스: " + ex.getClass().getName());
            System.out.println("🔥 예외 메시지: " + ex.getMessage());
            throw ex;
        }
    }

    public UserResponseDto signup(UserSignupRequest request) {
        // ✅ 이메일 인증 여부 확인
        if (!verificationCodeService.isEmailVerified(request.getEmail())) {
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
        }

        // 1) 비밀번호 암호화 처리
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        // 2) 프로필 이미지 저장 로직 추가
        String savedProfileUrl = null;
        MultipartFile profileImage = request.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                // (a) 원본 파일명에서 확장자 추출
                String originalFilename = profileImage.getOriginalFilename();
                String ext = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                // (b) UUID + 확장자로 새로운 파일명 생성
                String uuid = java.util.UUID.randomUUID().toString();
                String newFilename = uuid + ext;

                // (c) 저장 디렉터리 유무 확인 후 생성
                File dir = new File(profileUploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // (d) 실제 파일 쓰기
                File dest = new File(dir, newFilename);
                profileImage.transferTo(dest);

                // (e) 클라이언트가 접근할 URL 생성 (WebConfig에서 /profile-images/** 로 매핑)
                savedProfileUrl = "/profile-images/" + newFilename;

            } catch (Exception e) {
                throw new RuntimeException("프로필 이미지 저장 중 오류가 발생했습니다.", e);
            }
        }

        // 3) User 엔티티 생성 (profileImageUrl 필드 포함)
        User user = User.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .username(request.getUsername())
                .birthdate(request.getBirthdate())
                .phoneNumber(request.getPhoneNumber())
                .profileImageUrl(savedProfileUrl)  // null 가능
                .build();

        // 4) DB에 저장
        User savedUser = userRepository.save(user);

        // 5) 응답 DTO 반환 (profileImageUrl 포함)
        return UserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .birthdate(savedUser.getBirthdate())
                .phoneNumber(savedUser.getPhoneNumber())
                .profileImageUrl(savedUser.getProfileImageUrl())
                .build();
    }

    public void deleteUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.delete(user.get());
        } else {
            throw new IllegalArgumentException("해당 이메일의 유저가 존재하지 않습니다.");
        }
    }

    /**
     * ① 프로필 조회
     */
    @Transactional(readOnly = true)
    public UserResponseDto getUserProfile(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .birthdate(user.getBirthdate())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }

    /**
     * ② 토큰에서 추출한 이메일과 경로변수 ID의 회원이 동일한지 검증
     */
    public void verifyEmailMatchesId(String email, Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));
        if (!user.getEmail().equals(email)) {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    /**
     * ③ 프로필(이름·이미지) 수정
     */
public UserResponseDto updateUser(Long id, UserUpdateRequest request, MultipartFile profileImage) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다. id=" + id));

    System.out.println("✅ 사용자 조회 완료: " + user.getEmail());

    // 1) 이름 변경
    user.setUsername(request.getUsername());
    System.out.println("✅ 이름 변경: " + request.getUsername());

    // 2) 프로필 이미지 변경 로직 추가
    if (profileImage != null && !profileImage.isEmpty()) {
        try {
            String originalFilename = profileImage.getOriginalFilename();
            System.out.println("🖼 원본 파일명: " + originalFilename);

            String ext = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uuid = java.util.UUID.randomUUID().toString();
            String newFilename = uuid + ext;
            System.out.println("🆕 새 파일명: " + newFilename);

            File dir = new File(profileUploadDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                System.out.println("📁 저장 폴더 생성됨? " + created + " 경로: " + dir.getAbsolutePath());
            } else {
                System.out.println("📁 저장 폴더 경로 존재: " + dir.getAbsolutePath());
            }

            File dest = new File(dir, newFilename);
            System.out.println("📄 저장할 전체 경로: " + dest.getAbsolutePath());

            profileImage.transferTo(dest);

            user.setProfileImageUrl("/profile/" + newFilename);
            System.out.println("✅ 프로필 이미지 저장 완료: " + user.getProfileImageUrl());

        } catch (Exception e) {
            System.out.println("❌ 이미지 저장 중 예외 발생: " + e.getMessage());
            e.printStackTrace(); // 에러 위치까지 상세하게 출력
            throw new RuntimeException("프로필 이미지 저장 중 오류가 발생했습니다.", e);
        }
    } else {
        System.out.println("⚠ 프로필 이미지가 없음 또는 비어있음");
    }

    User updated = userRepository.save(user);
    System.out.println("✅ 사용자 정보 저장 완료");

    return UserResponseDto.builder()
            .id(updated.getId())
            .email(updated.getEmail())
            .username(updated.getUsername())
            .birthdate(updated.getBirthdate())
            .phoneNumber(updated.getPhoneNumber())
            .profileImageUrl(updated.getProfileImageUrl())
            .build();
}

}
