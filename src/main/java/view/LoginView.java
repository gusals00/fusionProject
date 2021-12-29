package view;

import controller.LoginController;
import network.LoginAndLogoutCode;
import network.Protocol;

import java.util.Scanner;

public class LoginView {
    private LoginController loginController;
    private ProfessorView professorView;
    private StudentView studentView;
    private AdminView adminView;

    public LoginView(AdminView adminView,ProfessorView professorView,StudentView studentView){
        loginController=new LoginController();
        this.adminView=adminView;
        this.professorView=professorView;
        this.studentView=studentView;
    }

    public void menu(){
        System.out.println("-------로그인 페이지-------");
    }

    public Protocol loginResView(Protocol getProtocol){
        if(getProtocol.getProtocolCode()== LoginAndLogoutCode.ID_AND_PW_REQ.getCode()) //ID,PWD 요청 메시지 수신시
            return getIdAndPw();// ID.PWD 응답 메시지 송신
        else if(getProtocol.getProtocolCode()==LoginAndLogoutCode.ADMIN_SUCCESS.getCode()){// 관리자 로그인 성공 메시지 수신시
            System.out.println("관리자 로그인");
            byte[] data=getProtocol.getPacket();
            String id=new String(data,Protocol.LEN_HEADER,61).split("\0")[0];
            adminView.setId(id);
            return adminView.adminResView(null);// 관리자 페이지로 이동
        }
        else if(getProtocol.getProtocolCode()==LoginAndLogoutCode.PROFESSOR_SUCCESS.getCode()){// 교수 로그인 성공 메시지 수신시
            System.out.println("교수 로그인");

            byte[] data=getProtocol.getPacket();
            String id=new String(data,Protocol.LEN_HEADER,61).split("\0")[0];
            String name =new String(data,Protocol.LEN_HEADER+61,61).split("\0")[0];
            professorView.setId(id);
            return professorView.professorResView(null);// 교수 페이지로 이동
        }
        else if(getProtocol.getProtocolCode()==LoginAndLogoutCode.STUDENT_SUCCESS.getCode()){// 학생 로그인 성공 메시지 수신시
            System.out.println("학생 로그인");

            byte[] data=getProtocol.getPacket();

            String id=new String(data,Protocol.LEN_HEADER,61).split("\0")[0];
            String name =new String(data,Protocol.LEN_HEADER+61,61).split("\0")[0];
            studentView.setId(id);
            return studentView.studentResView(null);// 학생 페이지로 이동
        }
        else {//3회 이상 아이디,비밀번호 틀려서 FAIL 수신
            System.out.println("실패");
            return pgmExit();//프로그램 종료 메시지 송신
        }
    }

    public Protocol getIdAndPw(){// ID,PW 사용자로부터 입력받고 PROTOCOL 만들어서 리턴
        String id=null;
        String pw=null;
        Scanner s=new Scanner(System.in);
        System.out.print("아이디 : ");
        id=s.nextLine();

        System.out.print("비밀번호 : ");
        pw=s.nextLine();

        return loginController.responseIdAndPw(id,pw);// ID,PWD 응답 PROTOCOL 리턴

    }
    public Protocol pgmExit(){// 프로그램 종료 PROTOCOL 리턴
        return  loginController.pgmExit();
    }

}
