package server;

import controller.AdminController;
import controller.LoginController;
import controller.ProfessorController;
import controller.StudentController;
import network.ProtocolType;
import persistence.PooledDataSource;
import persistence.dao.AdminDAO;
import persistence.dao.ProfessorDAO;
import persistence.dao.StudentDAO;
import service.AdminService;
import service.ProfessorService;
import service.StudentService;
import network.Protocol;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket=null;
    private LoginController loginController=null;
    private AdminController adminController=null;
    private  StudentController studentController=null;
    private ProfessorController professorController=null;
    private Protocol sendProtocol;
    private Protocol receiveProtocol;

    private boolean isExitHandler;
    ClientHandler(Socket socket, AdminController adminController, StudentController studentController,ProfessorController professorController) throws IOException {
        DataSource ds = PooledDataSource.getDataSource();
        AdminService adminService=new AdminService(new AdminDAO(ds));
        ProfessorService professorService=new ProfessorService(new ProfessorDAO(ds));
        StudentService studentService=new StudentService(new StudentDAO(ds));
        this.adminController=adminController;
        this.studentController=studentController;
        this.professorController=professorController;

        isExitHandler=false;//프로그램 종료 여부
        this.socket=socket;
        this.loginController=new LoginController(adminService,professorService,studentService);
        sendProtocol=null;//수신 프로토콜
        receiveProtocol=null;//
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

    public boolean getIsExitHandler(){return isExitHandler;}

    public void setIsExitHandler(boolean isExitHandler){this.isExitHandler=isExitHandler;}


    @Override
    public void run(){
        try {
            System.out.println("포트 번호 : "+socket.getPort()+" client handler 실행 ");
            ServerSenderThread serverSender=new ServerSenderThread(socket,this);
            ServerReceiverThread serverReceiver=new ServerReceiverThread(socket,this);
            Thread serverSenderThread = new Thread(serverSender);
            Thread serverReceiverThread = new Thread(serverReceiver);
            serverSenderThread.start();
            serverReceiverThread.start();

            sendProtocol=loginController.loginReqProtocol();//프로그램 시작하자마자 로그인 요청 보냄.

            while (!isExitHandler){
                if(receiveProtocol!=null){
                    if(receiveProtocol.getProtocolType()==ProtocolType.LOGIN_AND_LOGOUT.getType()){//로그인 관련 controller
                        sendProtocol=loginController.setSendProtocol(receiveProtocol);
                        receiveProtocol=null;
                    }
                    else if(receiveProtocol.getProtocolType()==ProtocolType.ADMIN.getType()){//관리자 관련 controller
                        sendProtocol = adminController.setSendProtocol(receiveProtocol);
                        receiveProtocol = null;
                    }
                    else if (receiveProtocol.getProtocolType() == ProtocolType.PROFESSOR.getType()) {//교수 관련 controller
                        sendProtocol = professorController.setSendProtocol(receiveProtocol);
                        receiveProtocol = null;
                    }
                    else if (receiveProtocol.getProtocolType() == ProtocolType.STUDENT.getType()) {//학생 관련 controller
                        sendProtocol = studentController.setSendProtocol(receiveProtocol);
                        receiveProtocol = null;
                    }
                    else {//EXIT
                        isExitHandler=true;
                    }
                }
                else{
                    System.out.print("");
                }
            }
            System.out.println("포트번호 : "+socket.getPort()+" client Handler가 종료되었습니다.");

            if(!socket.isInputShutdown()){
                socket.shutdownInput();
            }
            if(!socket.isOutputShutdown()){
                socket.shutdownOutput();

            }
            socket.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void shutDown() throws IOException {
        socket.close();
    }
    public void shutDownInput() throws IOException {
        socket.shutdownInput();
    }
    public void shutDownOutput() throws IOException {
        socket.shutdownOutput();

    }
}
