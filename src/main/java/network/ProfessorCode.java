package network;

public enum ProfessorCode {
    UPDATE_PRIVACY_AND_PASSWORD((byte) 0x01), CREATE_SYLLABUS((byte) 0x02) ,UPDATE_SYLLABUS((byte) 0x03),
    READ_LECTURE_LIST_REQ((byte) 0x04), READ_LECTURE_LIST_RES((byte) 0x05) ,
    READ_SYLLABUS_LIST_REQ((byte) 0x06), READ_SYLLABUS_LIST_RES((byte) 0x07) ,
    READ_OPEN_LECTURE_ENROLLED_STUDENT_REQ((byte) 0x08) , READ_OPEN_LECTURE_ENROLLED_STUDENT_RES((byte) 0x09),
    READ_PROFESSOR_SCHEDULE_REQ((byte)0x10),  READ_PROFESSOR_SCHEDULE_RES((byte)0x11),
    CREATE_SYLLABUS_WEEK_INFO((byte)0x12), UPDATE_SYLLABUS_WEEK_INFO((byte)0x13), DELETE_SYLLABUS_WEEK_INFO((byte)0x14),
    SUCCESS_CREATE_AND_UPDATE_AND_DELETE((byte)0x15),FAIL_CREATE_AND_UPDATE_AND_DELETE_AND_READ((byte)0x16);

    /*
    Code 0x01: 개인정보 및 비밀번호 수정 UPDATE_PRIVACY_AND_PASSWORD
    Code 0x02: 강의 계획서 입력 CREATE_SYLLABUS
    Code 0x03: 강의 계획서 수정 UPDATE_SYLLABUS
    Code 0x04: 교과목 목록 조회 READ_LECTURE_LEST_REQ
    Code 0x05: 교과목 목록 조회 응답 READ_LECTURE_LEST_RES
    Code 0x06: 교과목 강의 계획서 조회 READ_SYLLABUS_LEST_REQ
    Code 0x07: 교과목 강의 계획서 조회 응답 READ_SYLLABUS_LEST_RES
    Code 0x08: 교과목 수강 신청 학생 목록 조회 READ_OPEN_LECTURE_ENROLLED_STUDENT_REQ
    Code 0x09: 교과목 수강 신청 학생 목록 조회 응답 READ_OPEN_LECTURE_ENROLLED_STUDENT_RES
    Code 0x10: 교과목 시간표 조회 READ_OPEN_LECTURE_TIMETABLE_REQ
    Code 0x11: 교과목 시간표 조회 응답 READ_OPEN_LECTURE_TIMETABLE_RES
    Code 0x12: 강의 시간 수정 UPDATE_OPEN_LECTURE_TIME
    Code 0x13: 강의실 수정 UPDATE_OPEN_LECTURE_ROOM
    Code 0x14: 주차 별 강의정보 추가 CREATE_SYLLABUS_WEES_INFO
    Code 0x15: 주차 별 강의정보 수정 UPDATE_SYLLABUS_WEES_INFO
    Code 0x16: 주차 별 강의정보 삭제 DELETE_SYLLABUS_WEES_INFO
    Code 0x17: 추가 ,삭제 및 수정 성공  SUCCESS_CREATE_AND_UPDATE_AND_DELETE
    Code 0x18: 추가 ,삭제 ,수정 및 조회 실패  FAIL_CREATE_AND_UPDATE_AND_DELETE_AND_READ
    */

    private final byte code;

    ProfessorCode(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
    public static ProfessorCode get(int code) {
        switch (code) {
            case 0x01:
                return UPDATE_PRIVACY_AND_PASSWORD;
            case 0x02:
                return CREATE_SYLLABUS;
            case 0x03:
                return UPDATE_SYLLABUS;
            case 0x04:
                return READ_LECTURE_LIST_REQ;
            case 0x05:
                return READ_LECTURE_LIST_RES;
            case 0x06:
                return READ_SYLLABUS_LIST_REQ;
            case 0x07:
                return READ_SYLLABUS_LIST_RES;
            case 0x08:
                return READ_OPEN_LECTURE_ENROLLED_STUDENT_REQ;
            case 0x09:
                return READ_OPEN_LECTURE_ENROLLED_STUDENT_RES;
            case 0x10:
                return READ_PROFESSOR_SCHEDULE_REQ;
            case 0x11:
                return READ_PROFESSOR_SCHEDULE_RES;
            case 0x12:
                return CREATE_SYLLABUS_WEEK_INFO;
            case 0x13:
                return UPDATE_SYLLABUS_WEEK_INFO;
            case 0x14:
                return DELETE_SYLLABUS_WEEK_INFO;
            case 0x15:
                return SUCCESS_CREATE_AND_UPDATE_AND_DELETE;
            default:
                return FAIL_CREATE_AND_UPDATE_AND_DELETE_AND_READ;

        }


    }
}
