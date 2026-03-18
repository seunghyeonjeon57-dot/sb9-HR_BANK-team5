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


   EMP_NOT_FOUND(404, "직원을 찾을 수 없습니다.")
  // EMAIL_DUPLICATION(400, "이미 사용 중인 이메일입니다.")*/

  DEPT_NOT_FOUND(404, "부서를 찾을 수 없습니다."),
  DEPT_NAME_DUPLICATION(400, "잘못된 요청 또는 이미 존재하는 부서 이름입니다."),
  DEPT_DELETE_NOT_ALLOWED(400, "소속 직원이 있는 부서는 삭제할 수 없습니다."),

  EMP_NOT_FOUND(404, "직원을 찾을 수 없습니다."),
  EMAIL_DUPLICATION(400, "이미 사용 중인 이메일입니다."),
  EMPLOYEE_NUMBER_DUPLICATION(400, "이미 존재하는 사원 번호입니다."),
  INVALID_RESIGNATION_DATE(400, "퇴사일은 입사일보다 빠를 수 없습니다."),
  ALREADY_RESIGNED_EMPLOYEE(400, "이미 퇴사 처리된 사원입니다."),
  INVALID_DEPARTMENT_ID(400, "유효하지 않은 부서 ID입니다."),
  PROFILE_IMAGE_NOT_FOUND(404, "프로필 이미지 파일을 찾을 수 없습니다."),
  FILE_SIZE_EXCEEDED(400, "업로드 가능한 파일 크기를 초과했습니다."),


  BACKUP_ALREADY_IN_PROGRESS(409, "이미 진행 중인 백업 작업이 있습니다."),
  BACKUP_NOT_FOUND(404, "해당 백업 이력을 찾을 수 없습니다.");

  private final int status;
  private final String message;
}