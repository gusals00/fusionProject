package client;

import network.LoginAndLogoutCode;
import network.ProtocolType;
import network.Protocol;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientReceiverThread implements Runnable{
    private InputStream is = null ;
    private Client client=null;
    private Socket socket=null;
    public ClientReceiverThread(Socket socket,Client client)throws IOException {
        is=socket.getInputStream();
        this.socket=socket;
        this.client=client;
    }

    @Override
    public void run(){
        Protocol protocol=new Protocol();
        byte[] header=new byte[Protocol.LEN_HEADER];

        //System.out.println("클라이언트 수신 스레드 시작");
        while (true){
            try{
                if(client.getReceiveProtocol()==null&&is.read(header)!=0){// 서버가 보낸 정보가 있을 경우
                    protocol.setHeaderPacket(header);
                    if(protocol.getLength()>0){// 데이터가 있는 경우
                        byte[] data=new byte[protocol.getLength()];
                        is.read(data);
                        protocol.setDataPacket(data);
                    }
                    client.setReceiveProtocol(protocol);//client의 receiveProtocol 변수를 변경
                    System.out.println();
                    if(protocol.getProtocolType()== ProtocolType.LOGIN_AND_LOGOUT.getType() && protocol.getProtocolCode()== LoginAndLogoutCode.FAIL.getCode()){//프로그램 종료.
                        client.stopInput();
                        break;
                    }
                }
                else{
                    System.out.print("");
                }
            }catch(Exception e){
                System.out.println("죄송합니다. 데이터 전송이 에러 중");
                Protocol loginProtocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());// 로그인 request 메시지를 receiveProtocol에 넣기
                loginProtocol.setProtocolCode(LoginAndLogoutCode.ID_AND_PW_REQ.getCode());
                client.setReceiveProtocol(loginProtocol);
            }

            header=new byte[Protocol.LEN_HEADER];
        }


        System.out.println("수신 쓰레드 종료");

    }

    public void test(){

    }
}
