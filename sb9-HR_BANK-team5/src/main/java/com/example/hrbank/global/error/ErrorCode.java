package com.example.hrbank.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  // 공통 시스템 에러 (각자 도메인 에러를 여기에 추가)
  INVALID_INPUT_VALUE(400, "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다."),

  /*
  // --- 도메인별 에러 추가 예시 (각 담당자가 아래 양식을 참고해서 추가하세요) ---

  // 2. 직원 관리 (이메일 중복, 존재하지 않는 사번 등) [cite: 63, 70]
  // EMP_NOT_FOUND(404, "직원을 찾을 수 없습니다."),
  // EMAIL_DUPLICATION(400, "이미 사용 중인 이메일입니다.")*/

  DEPT_NOT_FOUND(404, "부서를 찾을 수 없습니다."),
  DEPT_NAME_DUPLICATION(400, "잘못된 요청 또는 이미 존재하는 부서 이름입니다."),
  DEPT_DELETE_NOT_ALLOWED(400, "소속 직원이 있는 부서는 삭제할 수 없습니다."),


  BACKUP_ALREADY_IN_PROGRESS(409, "이미 진행 중인 백업 작업이 있습니다."),
  BACKUP_NOT_FOUND(404, "해당 백업 이력을 찾을 수 없습니다.");

  private final int status;
  private final String message;
}