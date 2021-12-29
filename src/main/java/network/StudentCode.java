package network;

public enum StudentCode {
    UPDATE_PERSONAL_INFO((byte) 0x01), ENROLMENT((byte) 0x02), CANCEL_ENROLMENT((byte) 0x03), READ_OPEN_LECTURE_LIST((byte) 0x04),RESULT_READ_OPEN_LECTURE_LIST((byte) 0x05),
    READ_SELECTED_LECTURE_SYLLABUS((byte) 0x06), RESULT_READ_SELECTED_LECTURE_SYLLABUS((byte) 0x07), READ_OWN_SCHEDULE((byte) 0x08),
    RESULT_READ_OWN_SCHEDULE((byte) 0x09), SUCCESS_CREATE_UPDATE_DELETE((byte) 0x10), FAILURE_CREATE_UPDATE_DELETE_READ((byte) 0x11);

    /*
    Code 0x01: 개인정보 및 비밀번호 수정 UPDATE_PERSONAL_INFO
    Code 0x02: 수강 신청 ENROLMENT
    Code 0x03: 수강 취소 CANCEL_ENROLMENT
    Code 0x04: 개설 교과목 목록(전학년) 조회 READ_OPEN_LECTURE_LIST
    Code 0x05: 개설 교과목 목록(전학년) 조회 결과 RESULT_READ_OPEN_LECTURE_LIST
    Code 0x06: 선택 교과목 강의 계획서 조회 READ_SELECTED_LECTURE_SYLLABUS
    Code 0x07: 선택 교과목 강의 계획서 조회 결과 RESULT_READ_SELECTED_LECTURE_SYLLABUS
    Code 0x08: 본인 시간표 조회 READ_OWN_SCHEDULE
    Code 0x09: 본인 시간표 조회 결과 RESULT_READ_OWN_SCHEDULE
    Code 0x10: 추가 ,삭제 및 수정 성공 SUCCESS_CREATE_UPDATE_DELETE
    Code 0x11: 추가 ,삭제 ,수정 및 조회 실패 FAILURE_CREATE_UPDATE_DELETE_READ
    */

    private final byte code;

    public byte getCode() {
        return code;
    }

    StudentCode(byte code) {
        this.code = code;
    }

    public static StudentCode get(int code) {
        switch (code) {
            case 0x01:
                return UPDATE_PERSONAL_INFO;
            case 0x02:
                return ENROLMENT;
            case 0x03:
                return CANCEL_ENROLMENT;
            case 0x04:
                return READ_OPEN_LECTURE_LIST;
            case 0x05:
                return RESULT_READ_OPEN_LECTURE_LIST;
            case 0x06:
                return READ_SELECTED_LECTURE_SYLLABUS;
            case 0x07:
                return RESULT_READ_SELECTED_LECTURE_SYLLABUS;
            case 0x08:
                return READ_OWN_SCHEDULE;
            case 0x09:
                return RESULT_READ_OWN_SCHEDULE;
            case 0x10:
                return SUCCESS_CREATE_UPDATE_DELETE;
            default:
                return FAILURE_CREATE_UPDATE_DELETE_READ;
        }
    }
}
