package network;

public enum AdminCode {
    CREATE_ADMIN((byte) 0x01), CREATE_PROFESSOR((byte) 0x02), CREATE_STUDENT((byte) 0x03), CREATE_LECTURE((byte) 0x04), UPDATE_LECTURE((byte) 0x05), DELETE_LECTURE((byte) 0x06),
    UPDATE_SYLLABUS_PERIOD((byte) 0x07), UPDATE_ENROLMENT_PERIOD_BY_LEVEL((byte) 0x08), READ_PROFESSOR_INFO((byte) 0x09), PROFESSOR_INFO_RES((byte) 0x10), READ_STUDENT_INFO((byte) 0x11),
    STUDENT_INFO_RES((byte) 0x12), READ_OPEN_LECTURE_INFO((byte) 0x13), OPEN_LECTURE_INFO_RES((byte) 0x14), CREATE_OPEN_LECTURE((byte) 0x15), UPDATE_OPEN_LECTURE((byte) 0x16), DELETE_OPEN_LECTURE((byte) 0x17),
    READ_LECTURE((byte) 0x18), SUCCESS((byte) 0x19), FAIL((byte) 0x20);

    /*
    Code 0x01: 관리자 계정 생성 CREATE_ADMIN
    Code 0x02: 교수 계정 생성 CREATE_PROFESSOR
    Code 0x03: 학생 계정 생성 CREATE_STUDENT
    Code 0x04: 교과목 생성 CREATE_LECTURE
    Code 0x05: 교과목 수정 UPDATE_LECTURE
    Code 0x06: 교과목 삭제 DELETE_LECTURE
    Code 0x07: 강의 계획서 입력 기간 설정 UPDATE_SYLLABUS_PERIOD
    Code 0x08: 학년별 수강 신청 기간 설정 UPDATE_ENROLMENT_PERIOD_BY_LEVEL
    Code 0x09: 교수 정보 조회 READ_PROFESSOR_INFO
    Code 0x10: 교수 정보 조회 응답 PROFESSOR_INFO_RES
    Code 0x11: 학생 정보 조회 READ_STUDENT_INFO
    Code 0x12: 학생 정보 조회 응답 STUDENT_INFO_RES
    Code 0x13: 개설교과목 정보 조회 READ_OPEN_LECTURE_INFO
    Code 0x14: 개설교과목 정보 조회 응답 OPEN_LECTURE_INFO_RES
    Code 0x15: 개설 교과목 추가 CREATE_OPEN_LECTURE
    Code 0x16: 개설 교과목 삭제 DELETE_OPEN_LECTURE
    Code 0x17: 추가 ,삭제 및 수정 성공 SUCCESS
    Code 0x18: 추가 ,삭제 ,수정 및 조회 실패 FAIL
    */

    private final byte code;

    public byte getCode() {
        return code;
    }

    AdminCode(byte code) {
        this.code = code;
    }

    public static AdminCode get(int code) {
        switch (code) {
            case 0x01:
                return CREATE_ADMIN;
            case 0x02:
                return CREATE_PROFESSOR;
            case 0x03:
                return CREATE_STUDENT;
            case 0x04:
                return CREATE_LECTURE;
            case 0x05:
                return UPDATE_LECTURE;
            case 0x06:
                return DELETE_LECTURE;
            case 0x07:
                return UPDATE_SYLLABUS_PERIOD;
            case 0x08:
                return UPDATE_ENROLMENT_PERIOD_BY_LEVEL;
            case 0x09:
                return READ_PROFESSOR_INFO;
            case 0x10:
                return PROFESSOR_INFO_RES;
            case 0x11:
                return READ_STUDENT_INFO;
            case 0x12:
                return STUDENT_INFO_RES;
            case 0x13:
                return READ_OPEN_LECTURE_INFO;
            case 0x14:
                return OPEN_LECTURE_INFO_RES;
            case 0x15:
                return CREATE_OPEN_LECTURE;
            case 0x16:
                return UPDATE_OPEN_LECTURE;
            case 0x17:
                return DELETE_OPEN_LECTURE;
            case 0x18:
                return READ_LECTURE;
            case 0x19:
                return SUCCESS;
            case 0x20:
                return FAIL;
            default:
                return FAIL;

        }

    }
}
