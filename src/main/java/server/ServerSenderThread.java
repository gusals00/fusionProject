package server;

import network.LoginAndLogoutCode;
import network.Protocol;
import network.ProtocolType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ServerSenderThread implements Runnable{
    private OutputStream os = null;
    private Socket socket=null;
    private ClientHandler clientHandler=null;
    private int portNum=0;

    public ServerSenderThread(Socket socket,ClientHandler clientHandler) throws IOException {
        os=socket.getOutputStream();
        this.clientHandler=clientHandler;
        this.socket=socket;
        portNum=socket.getPort();
    }
    @Override
    public void run(){
        System.out.println("송신용 : "+portNum);

        while (!clientHandler.getIsExitHandler()){
            if(clientHandler.getSendProtocol()!=null){
                try{
                    //System.out.println("서버에서 보냄 type : "+clientHandler.getSendProtocol().getProtocolType()+"code : "+clientHandler.getSendProtocol().getProtocolCode());
                    os.write(clientHandler.getSendProtocol().getPacket());
                    os.flush();
                    clientHandler.setSendProtocol(null);
                }catch(Exception e){
                    if(socket.isOutputShutdown())
                        break;
                    //e.printStackTrace();
                    //System.out.println("송신 에러");
                    clientHandler.setSendProtocol(loginReqProtocol());
                }
            }
            else{
                System.out.print("");
            }
        }
        System.out.println("포트번호 : "+portNum+" 서버 송신 스레드 종료");
    }
    public Protocol loginReqProtocol(){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.ID_AND_PW_REQ.getCode());

        return protocol;
    }
}
