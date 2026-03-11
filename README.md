### 📝 HR_BANK Team Code Convention

#### 1. Git Branch Strategy
- `main`: 배포용 (건드리지 마세요)
- `develop`: 통합 개발 브랜치 (Default)
- `feature/{domain}-{task}`: 각자 작업 브랜치 (예: `feature/dept-api`)

#### 2. Git Commit Message
- `feat`: 기능 추가
- `fix`: 버그 수정
- `refactor`: 코드 구조 변경
- `chore`: 설정 변경

#### 3. Java Style
- 클래스: PascalCase (대문자로 시작)
- 메서드/변수: camelCase (소문자로 시작)
- DB 테이블 매핑: snake_case (JPA가 자동 변환하도록 설정됨)

#### 4. Rules
- 모든 API 응답은 `ResponseEntity`를 사용한다.
- 비즈니스 로직은 `Service` 계층에서만 처리한다.
- 불필요한 `system.out.println`은 금지하며 `log`를 사용한다.
