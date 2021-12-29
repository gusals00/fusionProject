package network;


public enum ExitCode {
    FAIL((byte) 0x00),PGM_EXIT((byte) 0x01);

    //Code 0x01 : 프로그램 종료  PGM_EXIT

    private final byte code;

    public byte getCode() {
        return code;
    }

    ExitCode(byte code) {
        this.code = code;
    }

    public static ExitCode get(int code) {
        switch (code) {
            case 0x01:
                return PGM_EXIT;
            default:
                return FAIL;
        }
    }
}
