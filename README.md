# Mood-it: 감정 기반 콘텐츠 추천 SNS

---

### 프로젝트 소개

`Mood-it`은 사용자의 현재 **감정을 분석**하여 **맞춤형 영화, 드라마 콘텐츠를 추천**하고, 감정을 공유하며 소통할 수 있는 소셜 네트워킹 서비스입니다. 챗봇과의 대화를 통해 감정을 진단하고, 그 감정에 어울리는 콘텐츠를 추천받아 사용자의 정서적 만족감을 높이는 것을 목표로 합니다.

### 개발 기간
25.03.05 ~ 현재 진행 

### 주요 기능

* **감정 분석 및 진단**: 챗봇과의 대화를 통해 사용자의 감정을 분석하고 오늘의 감정을 진단합니다. (ChatGPT API 연동)
* **맞춤형 콘텐츠 추천**: 진단된 감정을 기반으로 사용자에게 적합한 영화/드라마 콘텐츠를 추천합니다. (TMDB API 연동)
* **감정 일기 및 공유**: 사용자의 감정을 일기 형태로 기록하고, 다른 사용자와 공유하여 소통할 수 있습니다.
* **소셜 피드**: 친구들의 감정 공유 게시글과 추천 콘텐츠를 확인하고 '공감' 또는 '댓글'로 소통합니다.
* **회원 관리**: Spring Security + JWT를 활용한 안전한 회원가입, 로그인, 인증/인가 시스템을 제공합니다.

---

### 기술 스택

**▶ Backend**

<p align="center">
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white">
  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/JPA-4C4C4C?style=for-the-badge&logo=hibernate&logoColor=white">
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
  <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/ChatGPT_API-74AA9C?style=for-the-badge&logo=openai&logoColor=white">
  <img src="https://img.shields.io/badge/TMDB_API-00B2E1?style=for-the-badge&logo=themoviedatabase&logoColor=white">
  <br>
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">
  <img src="https://img.shields.io/badge/VS_Code-007ACC?style=for-the-badge&logo=visual-studio-code&logoColor=white">
  <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white">
  <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white">
  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white">
</p>

### 기술 스택 상세 (Detailed Technologies)

**▶ 백엔드 핵심 기술 (Backend Core Technologies)**
* **Java 21 + Spring Boot (Gradle)**: MSA(Microservice Architecture) 기반의 API 서버를 견고하게 구축하기 위한 메인 개발 언어 및 프레임워크입니다.
* **Spring Security + JWT**: 안전한 사용자 인증(로그인, 회원가입) 및 인가(권한 관리)를 구현하여 RESTful API 보안을 강화했습니다.
* **Redis**: 이메일 인증 코드 저장, 캐싱 등 휘발성 데이터 및 빠른 접근이 필요한 데이터를 관리하여 시스템 성능을 최적화했습니다.

**▶ 데이터베이스 & ORM (Database & ORM)**
* **MySQL**: 사용자 정보, 게시글, 콘텐츠 메타데이터 등 모든 관계형 데이터를 저장하고 관리합니다.
* **JPA (Hibernate)**: 객체지향적인 방식으로 데이터베이스를 조작하기 위해 사용된 ORM(Object-Relational Mapping) 프레임워크로, 생산성 향상에 기여했습니다.

**▶ AI 및 외부 API 연동 (AI & External API Integration)**
* **ChatGPT API**: 사용자의 감정을 분석하고, 추천 콘텐츠에 대한 설명을 생성하는 핵심 AI 기능에 활용했습니다.
* **TMDB API**: 영화/드라마 콘텐츠 정보를 가져와 사용자에게 추천하는 기능을 구현하는 데 사용했습니다.

**▶ API 문서화 및 테스트 (API Documentation & Testing)**
* **Swagger (Springdoc OpenAPI)**: 개발된 REST API의 명세를 자동으로 생성하고 시각화하여 프론트엔드 개발자와의 협업 효율을 높였습니다.
* **Postman**: 개발된 API 엔드포인트의 정확성과 응답을 테스트하고 검증하는 데 사용했습니다.

**▶ 개발 도구 및 협업 (Development Tools & Collaboration)**
* **VS Code**: 프론트엔드 및 백엔드 개발을 위한 통합 개발 환경(IDE)으로 사용했습니다.
* **Git + GitHub**: 소스 코드의 버전 관리와 팀원 간의 협업을 위해 활용했습니다.
* **Notion**: 프로젝트 기획, 요구사항 정의, 회의록 작성, 작업 관리 등 전반적인 프로젝트 문서화 및 팀 협업 도구로 사용했습니다.

---

**▶ AI 및 외부 API**

* **ChatGPT API**: 자연어 처리 기반 감정 분석 및 추천 콘텐츠 설명 생성
* **TMDB API**: 방대한 영화/드라마 데이터베이스를 활용한 콘텐츠 정보 연동

**▶ 개발 도구 및 환경**

* **Postman**: RESTful API 테스트 및 검증
* **VS Code**: 통합 개발 환경
* **Git + GitHub**: 효과적인 버전 관리 및 협업
* **Notion**: 프로젝트 문서화 및 팀 협업
