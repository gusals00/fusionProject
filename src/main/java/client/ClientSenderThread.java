package client;


import network.LoginAndLogoutCode;
import network.Protocol;
import network.ProtocolType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSenderThread implements Runnable{
    private OutputStream os=null;
    private Client client=null;
    public ClientSenderThread(Socket socket, Client client) throws IOException {
        os=socket.getOutputStream();
        this.client=client;
    }
    @Override
    public void run(){
        //System.out.println("클라이언트 송신쓰레드 시작");

        while (true){
            try{
                if(client.getSendProtocol()!=null){// 서버로 보낸 데이터가 있는 경우
                    //System.out.println("클라이언트가 보냄 type :"+client.getSendProtocol().getProtocolType()+"code:"+client.getSendProtocol().getProtocolCode());
                    os.write(client.getSendProtocol().getPacket());
                    os.flush();
                    if(client.getSendProtocol().getProtocolType()== ProtocolType.EXIT.getType()){//프로그램 종료
                        client.stopOutput();
                        client.setExitPgm(true);
                        break;
                    }
                    client.setSendProtocol(null);// 서버로 데이터 보냈으니 sendProtocol을 null로 변경
                }
                else{
                    System.out.print("");
                }
            }catch(Exception e){
                System.out.println("에러");
                client.setSendProtocol(responseIdAndPw(" "," "));
            }
        }

        System.out.println("송신 스레드 종료");

    }


    public Protocol responseIdAndPw(String id, String pw){// ID,PWD 응답 PROTOCOL 만들어서 리턴
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.ID_AND_PW_RES.getCode());
        int idLen = 20*3+1;// ID에 해당하는 byte 길이
        int pwLen = 20*3+1;// PWD에 해당하는 byte 길이
        id += '\0';
        pw += '\0';
        byte[] data = new byte[idLen+pwLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length); //ID를 BYTE배열로 변환
        System.arraycopy(pw.getBytes(),0,data,idLen,pw.getBytes().length); //PWD를 BYTE배열로 변환
        protocol.setDataPacket(data);// BYTE 배열로 바뀐 데이터를 PROTOCOL에 넣어줌

        return protocol; // ID,PWD 응답 PROTOCOL 리턴
    }
}
