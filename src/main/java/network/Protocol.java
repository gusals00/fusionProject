package network;

import lombok.Getter;
@Getter
public class Protocol {
    public static final int LEN_PROTOCOL_TYPE=1; //프로토콜 타입 길이
    public static final int LEN_PROTOCOL_CODE=1; //프로토콜 코드 길이
    public static final int LEN_LENGTH=2;//데이터 총 길이
    public static final int LEN_FRAG=1;//메시지 분할 여부 길이
    public static final int LEN_IS_LAST=1;//분할된 메시지의 마지막 여부 길이
    public static final int LEN_SEQ_NUMBER=1;//전송 중 파일 분할 순서번호 길이
    public static final int LEN_LIST_LENGTH=1;//리스트 개수
    public static final int LEN_HEADER=LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG+LEN_IS_LAST+LEN_SEQ_NUMBER+LEN_LIST_LENGTH;//헤더 길이
    private byte[] packet;// header+data

    private int protocolType;
    private int protocolCode;
    private int length;
    private boolean frag;
    private boolean isLast;
    private int seqNumber;
    private int listLength;

    public Protocol(int protocolType){
        setProtocolType(protocolType);
    }

    public Protocol(){
        this(-1);

    }
    public void setProtocolType(int protocolType){ //프로토콜 타입 설정
        this.protocolType=protocolType;
        packet = new byte[LEN_HEADER];
        packet[0]=(byte)protocolType;
        frag=false;
        length=0;
    }
    public void setProtocolCode(int protocolCode){// 프롵토콜 코드 설정
        this.protocolCode=protocolCode;
        packet[LEN_PROTOCOL_TYPE]=(byte)protocolCode;
    }
    public void setLength(int newLength){//프로토콜 데이터 길이 설정
        length = newLength;
        byte[] newPacket = new byte[LEN_HEADER+length];
        System.arraycopy(shortToBytes((short)newLength),0,packet,LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE,LEN_LENGTH);
        System.arraycopy(packet,0,newPacket,0,LEN_HEADER);
        packet=newPacket;
    }

    public void setFrag(boolean newFrag)
    {//분할 여부 설정
        frag=newFrag;

        if(newFrag){
            packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH]=(byte)1;
        }
        else{
            packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH]=(byte)0;
        }
    }

    public void setIsLast(boolean newIsLast){//분할된 메시지 마지막 여부 설정
        isLast=newIsLast;

        if(newIsLast){//마지막인 경우
            packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG]=(byte)1;
        }
        else{//마지막 아닌 경우
            packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG]=(byte)0;
        }
    }

    public void setSeqNumber(int newSeqNumber){//전송 중 파일 분할 순서 번호 설정
        seqNumber=newSeqNumber;
        packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG+LEN_IS_LAST]=(byte)newSeqNumber;

    }
    public void setListLength(int listLength){//리스트 개수 설정
        this.listLength=listLength;
        packet[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG+LEN_IS_LAST+LEN_SEQ_NUMBER]=(byte)listLength;
    }



    public void setHeaderPacket(byte[] headerPacket){//바이트 배열 받아서 헤더 설정
        int newProtocolType = (int)headerPacket[0];//타입 설정
        int newProtocolCode = (int)headerPacket[LEN_PROTOCOL_CODE];

        byte[] newLengthArr = new byte[LEN_LENGTH];//길이 설정
        System.arraycopy(headerPacket,LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE,newLengthArr,0,LEN_LENGTH);
        int newLength =bytesToShort(newLengthArr);


        int newFrag = (int)headerPacket[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH];//분할여부 설정
        int newIsLast = (int)headerPacket[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG];//마지막 메시지인지 설정
        int newSeqNumber =(int)headerPacket[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG+LEN_IS_LAST];//순서번호 설정
        int newlistLength=(int)headerPacket[LEN_PROTOCOL_TYPE+LEN_PROTOCOL_CODE+LEN_LENGTH+LEN_FRAG+LEN_IS_LAST+LEN_SEQ_NUMBER];//리스트 개수 설정


        setProtocolType(newProtocolType);
        setProtocolCode(newProtocolCode);
        setLength(newLength);

        if(newFrag==1)//분할됨
            setFrag(true);
        else//분할안됨
            setFrag(false);

        if(newIsLast==1)//마지막 번호
            setIsLast(true);
        else//마지막 번호 아님
            setIsLast(false);
        setSeqNumber(newSeqNumber);
        setListLength(newlistLength);

    }

    public void setDataPacket(byte[] arr){//데이터 설정
        setLength(arr.length);
        System.arraycopy(arr,0,packet,LEN_HEADER,length);
    }

    public byte[] getPacket(){//바이트 배열로 이루어진 패킷 리턴

        return packet;
    }

    public byte[] shortToBytes(short val){
        byte[] array = new byte[2];
        array[0] = (byte)((val >> 8) & 0xff);
        array[1] = (byte)(val & 0xff);
        return array;
    }

    public short bytesToShort(byte[] value) {
        short newValue = 0;
        byte[] temp = value;
        newValue |= (((short) temp[0]) << 8) & 0xFF00;
        newValue |= (((short) temp[1])) & 0xFF;

        return newValue;
    }
}


