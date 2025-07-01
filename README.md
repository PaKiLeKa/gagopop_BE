# 🎯 GAGOPOP - 팝업 스토어 위치 기반 길찾기 서비스 (Backend)
**해커톤 프로젝트** | 2024.03.20 - 2024.04.02  

![2024-04-03_15-37-00](https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/a882a28c-b4d4-4868-a5ed-0488056957f6)

> 📎 배포 URL : https://gagopop.vercel.app/ <br/>
> ❤️ 구글 로그인을 하시면 위시리스트와 같은 서비스를 사용해보실 수 있습니다! <br/>



## 📋 **서비스 개요**
팝업 스토어 방문 시 정확한 위치를 찾기 어려운 문제를 해결하기 위한 위치 기반 서비스입니다. 일반 지도 앱과 SNS에 팝업스토어의 위치가 제대로 반영되지 않는 경우가 많아, 사용자들이 직접 주소를 검색하고 방문 루트를 계획해야 하는 번거로움을 해결하기 위해 제작하였습니다.

<img width="1031" alt="tansang" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/8c9a1dcd-8591-45c4-ae77-b3294e814020">

### 모두의 팝업 나침반 가고팝!을 이용해서 가고 싶은 팝업 정보와 경로를 찾아보세요!

## 📍 목차

1. [팀 소개](#teamsogae)
2. [기술 및 개발 환경](#gisul)
3. [주요 기능](#juyogineung)
4. [기술적 문제 해결](#problem-solving)
5. [서비스 결과](#pagegineung)
6. [프로젝트 구조](#structure)

## <span id="teamsogae">PAKILIKA를 소개합니다.</span>

저희는 기획자, 디자이너, 백엔드 개발자, 프론트엔드 개발자 각 1명, 총 4명으로 구성된 '**PAKILIKA**' 입니다.<br/> 팀원들의 이름 중 성(Park, Kim, Lee, Kang)에서 앞의 두 글자를 따서 만든 팀명입니다.<br/>

|                                                               **Pa 박유진**                                                               |                                                               **Ki 김도영**                                                               |                                                               **Le 이주선**                                                               |                                                               **Ka 강애란**                                                               |
| :---------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------: | :---------------------------------------------------------------------------------------------------------------------------------------: |
| <img width="180" alt="pa_profile_img" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/5a790aef-d9cf-494e-9f20-8628d4752b99"> | <img width="180" alt="ki_profile_img" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/9615bf3c-de28-45af-a6db-98394ba826b9"> | <img width="180" alt="le_profile_img" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/54f7e811-084f-4842-aa4f-58bf13f76b2c"> | <img width="180" alt="ka_profile_img" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/a7327d5a-e65a-4a39-83c7-97c93c9faa62"> |
|                                                                 BE 개발자                                                                 |                                                                 FE 개발자                                                                 |                                                                 PM / 기획                                                                 |                                                              UI/UX 디자이너                                                               |

## <span id="gisul">⚙️ 기술 및 개발 환경</span>

### [ 프로젝트 기간 ]
2024.03.20 - 2024.04.02

### [ 프로젝트 목표 ]
- 정해진 기간 내에 구현할 수 있도록 기획
- 다른 포지션과의 소통 및 협업 경험 축적
- 정해진 기간 내에 요구사항 구현 및 배포
- 부가 기능 업데이트

### [ Backend 기술 스택 ]
<img src="https://img.shields.io/badge/Java 17-ED8B00?style=flat-square&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Boot 3.2.4-6DB33F?style=flat-square&logo=spring&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=spring&logoColor=white"/> 
<img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white"/> <img src="https://img.shields.io/badge/OAuth 2.0-4285F4?style=flat-square&logo=google&logoColor=white"/> <img src="https://img.shields.io/badge/MariaDB-003545?style=flat-square&logo=mariadb&logoColor=white"/> <img src="https://img.shields.io/badge/NCloud-03C75A?style=flat-square&logo=naver&logoColor=white"/>

### [ 개발 환경 ]
<img src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=Git&logoColor=white"/> <img src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=GitHub&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-4A154B?style=flat-square&logo=slack&logoColor=white"/>

## <span id='juyogineung'>✨ 주요 기능</span>

![mvp](https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/aa70e93e-368c-44d9-9728-271d75de9893)

### Backend 핵심 기능

- 🔐 **사용자 인증 시스템**
  - OAuth 2.0 Google 소셜 로그인
  - JWT 토큰 기반 인증/인가
  - Spring Security 보안 구현

- 🗺️ **팝업 스토어 관리**
  - 팝업 스토어 CRUD API
  - 위치 기반 검색 및 필터링
  - 카테고리별 데이터 관리

- 🧭 **최적 경로 시스템**
  - 다중 팝업 스토어 방문을 위한 최적 방문 순서 알고리즘 (자체 구현)
  - Tmap Open API 기반 도보 소요 시간 계산

- 👤 **개인화 서비스**
  - 위시리스트 관리 API
  - 사용자별 방문 기록 추적
  - 투두리스트 기능

## <span id="problem-solving">🔧 기술적 문제 해결</span>

**- 5일 내 최적 경로 알고리즘 구현으로 해커톤 완주**

**문제 상황:**
* **5일이라는 짧은 해커톤 기간** 내 MVP 구현 필요

**해결 과정:**
* 기간 내에 구현 가능한 **간단한 최단거리 계산 알고리즘 구현**
* 사용자가 선택한 여러 팝업스토어를 **효율적으로 방문할 수 있는 경로 제공** 시스템 개발

**추가 기능 구현:**
* 개인화된 팝업스토어 탐방 경험을 위한 **사용자별 방문 기록 추적 기능**
* 투두리스트 관리 기능 구현
* **Spring Security + JWT + OAuth 2.0 API를 활용한 인증 시스템** 구축

**결과:**
* 사용자별 데이터 안전 관리 시스템 완성
* 해커톤 기간 내 MVP 성공적 구현

---

**- 프론트엔드와의 협업을 통한 API 설계 및 문서화 경험**

**문제 상황:**
* 프론트엔드 개발자와 협업 시 **네이밍 컨벤션 사전 정의 부족**
* camelCase와 snake_case가 혼재되면서 **개발 생산성 저하** 발생

**배운점:**
* **팀원과의 소통 중요성, API 설계와 문서화의 중요성** 체감
* 사전 컨벤션 정의가 프로젝트 효율성에 미치는 영향 학습

## <span id="pagegineung">💻️ 서비스 결과</span>

### 핵심 기능 구현 결과


### 1) 홈

|                                                        로그인                                                         |                                                      지도 기능                                                       |                                                      팝업 검색                                                       |                                                      경로 안내                                                       |
| :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
| <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/05fcb60c-fbba-4537-83cf-26fc341502d5">  | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/35e86168-b234-4aad-a661-f11864387952"> | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/b11f8cfa-e822-4e14-9209-dba4a263a90c"> | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/4064fa16-43b0-43b6-a9c8-2790de88553a"> |

### 3) 팝업 정보

|                                                       팝업 리스트                                                        |                                                      상세 정보                                                       |
| :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
| <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/9c04f05a-0f97-4b0d-b122-46db85571dc3"> | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/ec8af30c-cac8-469d-9019-a067f44b7e61"> |

### 4) 위시 리스트

|                                                     위시 리스트                                                      |                                                     추가 및 삭제                                                     |
| :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
| <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/a806a5e4-4bb9-445d-8d46-532ee77ba89f"> | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/244b0190-eb2d-4844-b4aa-553640d6deaa"> |

### 5) 프로필

|                                                     닉네임 수정                                                      |                                                       로그아웃                                                       |
| :------------------------------------------------------------------------------------------------------------------: | :------------------------------------------------------------------------------------------------------------------: |
| <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/8f6fecbe-818c-4365-99d3-12a8723516af">  | <img width="230" src="https://github.com/PaKiLeKa/gagopop-FE/assets/112444362/c5e7c53e-141b-4278-87da-c590aafebac3"> |

<br/>

## <span id="structure">📁 프로젝트 구조</span>

```
src/main/java/pakirika/gagopop/
├── domain/           # 도메인 엔티티
│   ├── user/         # 사용자 관리
│   ├── popup/        # 팝업스토어 관리
│   ├── route/        # 경로 관리
│   └── wishlist/     # 위시리스트 관리
├── repository/       # 데이터 접근 계층
├── service/          # 비즈니스 로직
│   ├── auth/         # 인증 서비스
│   ├── route/        # 경로 계산 서비스
│   └── popup/        # 팝업스토어 서비스
├── controller/       # REST API 컨트롤러
├── security/         # 보안 설정
├── config/           # 설정 클래스
└── GagopopApplication.java
```

## 🎯 **프로젝트 성과**
- **5일 해커톤 완주**: 제한된 시간 내 백엔드 시스템 완성
- **팀 협업 경험**: 다양한 직군과의 협업을 통한 서비스 구현
- **실무형 인증 시스템**: OAuth 2.0과 JWT를 활용한 보안 시스템 구축
- **알고리즘 적용**: 최적 경로 계산 알고리즘을 실제 서비스에 적용

## 📚 **개선 사항**
- 스탬프 페이지 업데이트
- 투고 리스트 업데이트
- 경로 찾기 및 지도 최적화

## 🛠️ **실행 방법**
```bash
# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun
```

**개발환경**: Java 17, MariaDB, Spring Boot 3.2.4

## 🔗 **관련 링크**
- **Frontend Repository**: https://github.com/PaKiLeKa/gagopop-FE
- **배포 URL**: https://gagopop.vercel.app/
