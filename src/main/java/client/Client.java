package client;


import network.ProtocolType;
import network.Protocol;
import view.AdminView;
import view.LoginView;
import view.ProfessorView;
import view.StudentView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    final static int ServerPort = 5000;
    private Socket socket;
    private Protocol sendProtocol;//송신할 프로토콜
    private Protocol receiveProtocol;//수신할 프로토콜
    private Thread clientSenderThread=null;//송신 스레드
    private Thread clientReceiverThread = null;//수신 스레드
    private LoginView loginView;
    private ProfessorView professorView;
    private StudentView studentView;
    private AdminView adminView;
    private boolean isExitPgm;// 프로그램 종료할지 여부

    public Client(InetAddress ip) throws IOException {
        // establish the connection
        this.socket= new Socket(ip, ServerPort);// 소켓 연결
        sendProtocol=null;
        receiveProtocol=null;
        boolean isExitPgm=false;

        ClientSenderThread clientSender=new ClientSenderThread(socket,this);
        ClientReceiverThread clientReceiver=new ClientReceiverThread(socket,this);
        clientSenderThread = new Thread(clientSender);
        clientReceiverThread = new Thread(clientReceiver);
        clientSenderThread.start();//송신 스레드 시작
        clientReceiverThread.start();//수신 스레드 시작

        adminView=new AdminView();
        professorView=new ProfessorView();
        studentView=new StudentView();
        loginView=new LoginView(adminView,professorView,studentView);

        run();
    }

    public void run() throws IOException {

        //System.out.println("클라이언트 시작됨.");

        while (!isExitPgm) {

            if(receiveProtocol!=null){// 수신 프로토콜이 있는 경우 각각의 view 호출
                if(receiveProtocol.getProtocolType()==ProtocolType.LOGIN_AND_LOGOUT.getType()){//로그인 view
                    sendProtocol=loginView.loginResView(receiveProtocol);
                }
                else if(receiveProtocol.getProtocolType()==ProtocolType.PROFESSOR.getType()){//교수 view
                    sendProtocol=professorView.professorResView(receiveProtocol);
                }
                else if(receiveProtocol.getProtocolType()==ProtocolType.ADMIN.getType()){//관리자 view
                    sendProtocol=adminView.adminResView(receiveProtocol);
                }
                else if(receiveProtocol.getProtocolType()==ProtocolType.STUDENT.getType()){//학생 view
                    sendProtocol=studentView.studentResView(receiveProtocol);
                }
                else{

                }
                receiveProtocol=null;
            }
            else{//수신 프로토콜이 없는 경우
                System.out.print("");
            }
        }

        closeSocket();
        System.out.println("clientPgm을 종료합니다.");
    }

    public void stopInput() throws IOException {
        socket.shutdownInput();
    }

    public void stopOutput()throws IOException{
        socket.shutdownOutput();
    }

    public void setExitPgm(boolean isExitPgm){//프로그램 종료 여부 설정
        this.isExitPgm=isExitPgm;
    }

    public void closeSocket() throws IOException {//소켓 종료
        socket.close();
    }



    public Protocol getSendProtocol(){
        return sendProtocol;
    }

    public Protocol getReceiveProtocol(){
        return receiveProtocol;
    }

    public void setSendProtocol(Protocol sendProtocol){
        this.sendProtocol=sendProtocol;
    }

    public void setReceiveProtocol(Protocol receiveProtocol){
        this.receiveProtocol=receiveProtocol;
    }

    public static void main(String[] args) throws IOException {
        InetAddress ip = InetAddress.getByName("localhost");//접속할 서버 ip

        //InetAddress ip = InetAddress.getByName(args[0]);
        Client client=new Client(ip);
    }

}
