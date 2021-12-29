package server;

import network.LoginAndLogoutCode;
import network.Protocol;
import network.ProtocolType;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ServerReceiverThread implements Runnable{
    private InputStream is = null ;
    private ClientHandler clientHandler=null;
    private int portNum=0;
    private Socket socket=null;
    ServerReceiverThread(Socket socket,ClientHandler clientHandler) throws IOException {
        is=socket.getInputStream();
        portNum=socket.getPort();
        this.clientHandler=clientHandler;
        this.socket=socket;
    }
    @Override
    public void run(){
        System.out.print("수신용 : "+portNum);
        byte[] header=new byte[Protocol.LEN_HEADER];
        Protocol protocol=new Protocol();
        while (!clientHandler.getIsExitHandler()){
            try{
                int state = is.read(header);
                if(clientHandler.getReceiveProtocol()==null&&state>0){
                    protocol.setHeaderPacket(header);
                    if(protocol.getLength()>0){
                        byte[] data=new byte[protocol.getLength()];
                        is.read(data);
                        protocol.setDataPacket(data);
                    }
                    //System.out.println("서버에서 받음 type: "+protocol.getProtocolType()+"code:"+protocol.getProtocolCode()+"length:"+protocol.getLength());
                    clientHandler.setReceiveProtocol(protocol);
                }
                else if(state==-1){
                    clientHandler.shutDownInput();
                    clientHandler.setIsExitHandler(true);
                    break;
                }
                else{
                    clientHandler.setReceiveProtocol(protocol);
                }
            }catch(IOException e){
                //System.out.println("수신 에러");
                if(socket.isInputShutdown()){
                    clientHandler.setIsExitHandler(true);//해당 client handler 종료
                    break;
                }
                header=new byte[Protocol.LEN_HEADER];
                clientHandler.setSendProtocol(loginReqProtocol());//로그인 request protocl을 sendProtocol에 넣기
                clientHandler.setReceiveProtocol(null);
            }

        }

        System.out.println("포트번호 : "+portNum+" 서버 수신 스레드 종료");
    }

    public Protocol loginReqProtocol(){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.ID_AND_PW_REQ.getCode());
        return protocol;
    }
}



