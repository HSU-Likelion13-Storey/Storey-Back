# 🏪 Storey - 소상공인 마스코트 브랜딩 서비스

>AI 기반 인터뷰를 통해 소상공인의 가게 스토리를 발굴하고, 전용 마스코트 캐릭터를 생성하여 브랜딩을 지원하는 서비스입니다.

<img width="1920" height="1080" alt="Image" src="https://github.com/user-attachments/assets/a5d48a01-3eaa-4388-bd0f-1c75af0ba4c3" />



## 📌 프로젝트 소개
**Storey**는 소상공인들이 자신만의 브랜드 스토리를 발굴하고, **AI를 활용해 전용 마스코트 캐릭터**를 생성할 수 있는 혁신적인 브랜딩 서비스입니다.

---
### 🚀 핵심 가치
- **스토리 발굴**: AI 인터뷰로 가게만의 특별한 이야기 추출
- **개성 있는 브랜딩**: 가게 특성을 반영한 고유 마스코트 캐릭터 생성
- **고객 참여**: QR 코드 스캔을 통한 캐릭터 수집 게임
- **손쉬운 관리**: 직관적 UI로 누구나 쉽게 브랜딩 가능
---

## ✨ 핵심 기능
### 🎤 AI 인터뷰 시스템
- **맞춤형 질문 생성**: 업종·분위기에 맞춘 첫 질문 제공
- **대화형 인터뷰**: 답변 분석 후 다음 질문 동적 생성
- **스토리 요약**: 인터뷰 내용을 핵심 문장으로 압축


### 🎨 마스코트 캐릭터 생성
- **AI 이미지 생성**: OpenAI DALL·E 3 기반 고품질 캐릭터
- **자동 네이밍**: 스토리에 어울리는 이름 추천
- **캐릭터 설정**: 성격·말버릇·태그라인 자동 생성
- **재생성 가능**: 마음에 들지 않으면 즉시 재생성


### 🗺️ 인터랙티브 지도
- **가게 위치 표시**: 카카오맵 API 연동
- **QR 스캔 해금**: 방문 고객만 캐릭터 획득 가능
- **캐릭터 수집 게임**: 포켓몬GO 스타일 수집 요소
- **이벤트 정보**: 진행 중인 이벤트 표시


## 🛠 기술 스택

| 구분 | 기술 |
|------|------|
| **언어** | <img src="https://img.shields.io/badge/Java 21-007396?style=flat-square&logo=java&logoColor=white" /> |
| **프레임워크** | <img src="https://img.shields.io/badge/Spring%20Boot 3.5.4-6DB33F?style=flat-square&logo=springboot&logoColor=white" /> |
| **보안** | <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white" /> |
| **빌드 도구** | <img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white" /> |
| **데이터베이스** | <img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=flat-square&logo=mysql&logoColor=white" /> |
| **ORM** | <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=flat-square&logo=springboot&logoColor=white" /> |
| **AI API** | <img src="https://img.shields.io/badge/OpenAI GPT--4o-412991?style=flat-square&logo=openai&logoColor=white" /> <img src="https://img.shields.io/badge/DALL--E 3-412991?style=flat-square&logo=openai&logoColor=white" /> |
| **좌표 변환 API** | <img src="https://img.shields.io/badge/Kakao-FFCD00?style=flat-square&logo=kakao&logoColor=black" /> |
| **결제 API** | <img src="https://img.shields.io/badge/Toss Payments-0064FF?style=flat-square&logo=tosspayments&logoColor=white" /> |
| **클라우드 스토리지** | <img src="https://img.shields.io/badge/AWS S3-569A31?style=flat-square&logo=amazons3&logoColor=white" /> |
| **컨테이너** | <img src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white" /> |
| **CI/CD** | <img src="https://img.shields.io/badge/GitHub Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white" /> |



## 🦴 Commit Convention
### 커밋 메시지 규칙
커밋 메시지는 아래 형식으로 작성합니다:
```
<타입>:<내용>

ex) feat: 로그인 기능 추가
```
<br>

| 타입         | 설명                     |
| ---------- | ---------------------- |
| `feat`     | 새로운 기능 추가              |
| `fix`      | 버그 수정                  |
| `docs`     | 문서 수정 (README 등)       |
| `style`    | 코드 포맷팅, 세미콜론 누락 등      |
| `refactor` | 코드 리팩토링 (기능 변화 없음)     |
| `test`     | 테스트 코드 추가 또는 수정        |
| `chore`    | 기타 변경사항 (빌드 설정, 패키지 등) |

## 🌙 Git Flow 브랜치 전략
### 주요 브랜치
| 브랜치 이름      | 용도                                     |
| ----------- | -------------------------------------- |
| `main`      | 배포(Release)가 이루어지는 안정적인 코드             |
| `develop`   | 다음 릴리스를 준비하는 개발 브랜치                    |

---
### 브랜치 네이밍
브랜치 네이밍은 아래 형식으로 작성합니다:
```
<타입>/<이슈번호>

ex)feat/#23
```
| 타입         | 설명                |
| ---------- | ----------------- |
| `feat`  | 새로운 기능 작업         |
| `fix`      | 버그 수정 작업          |
| `hotfix`   | 급한 수정 작업 (배포 후 등) |
| `refactor` | 코드 리팩토링           |
| `docs`     | 문서 작업             |
| `chore`    | 기타 작업 (설정, 패키지 등) |

### ☸️ Git 브랜치 및 개발 프로세스

1. **기능 개발 시작**
   - `develop` 브랜치에서 새로운 `feature` 브랜치를 생성하여 개발을 시작합니다.

2. **기능 개발 및 커밋**
   - `feature` 브랜치에서 기능을 완성하고 커밋을 진행합니다.

3. **코드 리뷰 및 병합**
   - 개발 완료 후, `feature` 브랜치에서 `develop` 브랜치로 PR(Pull Request)을 생성하여 코드 리뷰를 받습니다.
   - 리뷰가 완료되면 `develop` 브랜치에 병합합니다.

4. **테스트**
   - `develop` 브랜치에서 배포 전 최종 기능들이 안정적으로 동작하는지 테스트합니다.

5. **배포**
   - 테스트가 완료되면 `develop` 브랜치를 `main` 브랜치에 병합하여 최종 배포를 진행합니다.







