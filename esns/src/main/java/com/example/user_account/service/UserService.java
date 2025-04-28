package com.example.user_account.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.user_account.dto.UserResponseDto;
import com.example.user_account.dto.UserSignupRequest;
import com.example.user_account.entity.User;
import com.example.user_account.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;

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
         if (!verificationCodeService.isEmailVerified(request.getEmail())){
            throw new IllegalArgumentException("이메일 인증이 완료되지 않았습니다.");
         }

        // 비밀번호 암호화 처리
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getEmail())
                .password(encryptedPassword)
                .username(request.getUsername())
                .birthdate(request.getBirthdate())
                .phoneNumber(request.getPhoneNumber())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponseDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .username(savedUser.getUsername())
                .birthdate(savedUser.getBirthdate())
                .phoneNumber(savedUser.getPhoneNumber())
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
}
