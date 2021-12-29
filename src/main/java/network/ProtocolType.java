package network;


public enum ProtocolType {
    EXIT((byte) 0x00), LOGIN_AND_LOGOUT((byte) 0x01), STUDENT((byte) 0x02), PROFESSOR((byte) 0x03),ADMIN((byte) 0x04),
    UNDEFINED((byte) 0x99);

    /*
    0x00: 종료  EXIT
    0x01: 로그인, 로그아웃 LOGIN_AND_LOGOUT
    0x02: 학생 STUDENT
    0x03: 교수 PROFESSOR
    0x04: 관리자 ADMIN
    0x99: UNDEFINED UNDEFINED
    */

    private final byte type;

    public int getType() {
        return type;
    }

    ProtocolType(byte type) {
        this.type = type;
    }

    public static ProtocolType get(int type) {
        switch (type) {
            case 0x00:
                return EXIT;
            case 0x01:
                return LOGIN_AND_LOGOUT;
            case 0x02:
                return STUDENT;
            case 0x03:
                return PROFESSOR;
            case 0x04:
                return ADMIN;
            default:
                return UNDEFINED;

        }
    }
}
