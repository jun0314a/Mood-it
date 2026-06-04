# Mood-it Backend

> 감정 분석 기반 영화/드라마 콘텐츠 추천 SNS, Mood-it의 백엔드 서버입니다.

`Mood-it`은 사용자의 현재 감정을 분석하여 감정에 어울리는 영화/드라마 콘텐츠를 추천하고, 감정 일기와 소셜 피드를 통해 다른 사용자와 감정을 공유할 수 있는 소셜 네트워킹 서비스입니다.

사용자는 챗봇과의 대화를 통해 오늘의 감정을 진단받고, 분석된 감정을 기반으로 맞춤형 콘텐츠를 추천받을 수 있습니다.

---

## 개발 기간

2025.03 ~ 2025.11

---

## 프로젝트 목표

`Mood-it`은 단순한 콘텐츠 추천 서비스를 넘어, 사용자의 감정 상태를 기반으로 더 개인화된 추천 경험을 제공하는 것을 목표로 합니다.

사용자는 자신의 감정을 기록하고 공유하며, 다른 사용자와 소통할 수 있습니다. 이를 통해 감정 기반 콘텐츠 추천과 소셜 네트워킹 기능을 결합한 서비스를 구현하고자 했습니다.

---

## 주요 기능

### 감정 분석 및 진단
- ChatGPT API를 활용한 사용자 대화 기반 감정 분석
- 사용자의 현재 감정을 진단하고 감정 결과 제공

### 맞춤형 콘텐츠 추천
- TMDB API를 활용한 영화/드라마 콘텐츠 정보 연동
- 분석된 감정에 어울리는 콘텐츠 추천
- 추천 콘텐츠에 대한 설명 제공

### 감정 일기 및 공유
- 사용자의 감정을 일기 형태로 기록
- 감정 기록을 다른 사용자와 공유

### 소셜 피드
- 친구 및 다른 사용자의 감정 공유 게시글 조회
- 게시글 공감 및 댓글 기능 제공

### 회원 관리
- Spring Security와 JWT 기반 회원가입 및 로그인
- 인증/인가를 통한 안전한 API 접근 제어
- Redis를 활용한 이메일 인증 코드 관리

---

## 기술 스택

### Backend

<p align="center">
  <img src="https://img.shields.io/badge/Java_21-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-4C4C4C?style=for-the-badge&logo=hibernate&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
</p>

### External API & Tools

<p align="center">
  <img src="https://img.shields.io/badge/OpenAI_API-74AA9C?style=for-the-badge&logo=openai&logoColor=white">
  <img src="https://img.shields.io/badge/TMDB_API-01B4E4?style=for-the-badge&logo=themoviedatabase&logoColor=white">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black">
  <br>
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
</p>

---

## 백엔드 주요 구현 내용

| 구분 | 내용 |
|---|---|
| 인증/인가 | Spring Security와 JWT를 활용한 로그인 및 API 접근 제어 |
| 감정 분석 | OpenAI API를 활용한 사용자 대화 기반 감정 분석 |
| 콘텐츠 추천 | TMDB API를 활용한 영화/드라마 데이터 조회 및 추천 |
| 데이터 관리 | MySQL과 JPA를 활용한 사용자, 게시글, 댓글, 콘텐츠 데이터 관리 |
| 캐싱/인증 코드 | Redis를 활용한 이메일 인증 코드 저장 및 관리 |
| API 문서화 | Swagger를 활용한 REST API 문서 자동화 |

---

## 서비스 흐름

1. 사용자가 챗봇과 대화합니다.
2. ChatGPT API를 통해 대화 내용을 기반으로 감정을 분석합니다.
3. 분석된 감정에 어울리는 영화/드라마 콘텐츠를 TMDB API에서 조회합니다.
4. 사용자에게 맞춤형 콘텐츠를 추천합니다.
5. 사용자는 감정 일기를 작성하고, 소셜 피드를 통해 다른 사용자와 감정을 공유합니다.
