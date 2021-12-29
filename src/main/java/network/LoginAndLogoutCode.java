package network;


public enum LoginAndLogoutCode {
    FAIL((byte) 0x00),ID_AND_PW_REQ((byte) 0x01), ID_AND_PW_RES((byte) 0x02), ADMIN_SUCCESS((byte) 0x03), PROFESSOR_SUCCESS((byte) 0x04),
    STUDENT_SUCCESS((byte) 0x05),LOGOUT_REQ((byte) 0x06), LOGOUT_SUCCESS((byte) 0x07);
    /*
    Code 0x01 : ID,PWD 요청  ID_AND_PW_REQ
    Code 0x02: ID,PWD 응답 ID_AND_PW_RES
    Code 0x03: 관리자성공 ADMIN_SUCCESS
    Code 0x04: 교수성공 PROFESSOR_SUCCESS
    Code 0x05: 학생성공 STUDENT_SUCCESS
    Code 0x06: 로그아웃 요청 LOGOUT_REQ
    Code 0x07: 로그아웃 성공 LOGOUT_SUCCESS
    Code 0x00: 실패 FAIL
     */

    private final byte code;

    public int getCode() {
        return code;
    }

    LoginAndLogoutCode(byte code) {
        this.code = code;
    }

    public static LoginAndLogoutCode get(int code) {
        switch (code) {
            case 0x01:
                return ID_AND_PW_REQ;
            case 0x02:
                return ID_AND_PW_RES;
            case 0x03:
                return ADMIN_SUCCESS;
            case 0x04:
                return PROFESSOR_SUCCESS;
            case 0x05:
                return STUDENT_SUCCESS;
            case 0x06:
                return LOGOUT_REQ;
            case 0x07:
                return LOGOUT_SUCCESS;
            default:
                return FAIL;

        }
    }
}
