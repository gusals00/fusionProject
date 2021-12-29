package controller;

import network.ExitCode;
import network.LoginAndLogoutCode;
import network.ProtocolType;
import persistence.dto.AdminDTO;
import persistence.dto.ProfessorDTO;
import persistence.dto.StudentDTO;
import service.AdminService;
import service.ProfessorService;
import service.StudentService;
import network.Protocol;

public class LoginController {
    AdminService adminService=null;
    ProfessorService professorService=null;
    StudentService studentService=null;
    int cnt;
    public LoginController(AdminService adminService, ProfessorService professorService, StudentService studentService){
        this.adminService=adminService;
        this.professorService=professorService;
        this.studentService=studentService;
        cnt=0;
    }
    public LoginController(){
    }

    public Protocol setSendProtocol(Protocol receiveProtocol){
        Protocol sendProtocol=null;

        if(receiveProtocol.getProtocolCode()== LoginAndLogoutCode.ID_AND_PW_RES.getCode()){

            String id=new String(receiveProtocol.getPacket(),Protocol.LEN_HEADER,61).split("\0")[0];
            String pw=new String(receiveProtocol.getPacket(),Protocol.LEN_HEADER+61,61).split("\0")[0];
            System.out.println("loginController에서 확인할 id,pw : "+id+", "+pw);
            if(isAdminLoginOk(id,pw)){
                AdminDTO adminDTO=new AdminDTO(id,pw);
                adminDTO.setAdminId(id);
                sendProtocol =AdminSuccessProtocol(adminDTO);
            }
            else if(isProfessorLoginOk(id,pw)){
                ProfessorDTO professorDTO=new ProfessorDTO();
                professorDTO.setProfessorId(id);
                professorDTO.setProfessorPw(pw);
                sendProtocol = professorSuccessProtocol(professorDTO);
            }
            else if(isStudentLoginOk(id,pw)){
                StudentDTO studentDTO=new StudentDTO();
                studentDTO.setStudentId(id);
                studentDTO.setStudentPw(pw);
                sendProtocol =studentSuccessProtocol(studentDTO);
            }
            else{
                cnt++;
                System.out.println("틀린횟수:"+cnt);
                if(cnt>=3){
                    sendProtocol =loginFailProtocol();
                }
                else{
                    sendProtocol =loginReqProtocol();
                }
            }
        }

        else if(receiveProtocol.getProtocolCode() == LoginAndLogoutCode.LOGOUT_REQ.getCode()){//LOGOUT_REQ
            sendProtocol = loginReqProtocol();
        }
        return sendProtocol;
    }

    public boolean isAdminLoginOk(String id,String pw){

        AdminDTO adminDTO=new AdminDTO(id,pw);
        return adminService.isLoginOk(adminDTO);
    }

    public boolean isProfessorLoginOk(String id,String pw){
        ProfessorDTO professorDTO=new ProfessorDTO();
        professorDTO.setProfessorId(id);
        professorDTO.setProfessorPw(pw);
        return professorService.isLoginOk(professorDTO);
    }

    public boolean isStudentLoginOk(String id,String pw){
        StudentDTO studentDTO=new StudentDTO();
        studentDTO.setStudentId(id);
        studentDTO.setStudentPw(pw);
        return studentService.isLoginOk(studentDTO);
    }

    public Protocol AdminSuccessProtocol(AdminDTO adminDTO){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.ADMIN_SUCCESS.getCode());
        int idMaxSize=3*20+1;
        byte[] id = (adminDTO.getAdminId()+'\0').getBytes();
        byte[] dataPacket=new byte[idMaxSize];
        System.arraycopy(id,0,dataPacket,0,id.length);
        protocol.setDataPacket(dataPacket);
        return protocol;
    }

    public Protocol professorSuccessProtocol(ProfessorDTO professorDTO){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.PROFESSOR_SUCCESS.getCode());
        professorDTO=professorService.findProfessorById(professorDTO);

        int idMaxSize=3*20+1;
        int nameMaxSize=3*20+1;
        byte[] id = (professorDTO.getProfessorId()+'\0').getBytes();
        byte[] name = (professorDTO.getProfessorName()+'\0').getBytes();

        byte[] dataPacket=new byte[idMaxSize+nameMaxSize];
        System.arraycopy(id,0,dataPacket,0,id.length);
        System.arraycopy(name,0,dataPacket,idMaxSize,name.length);
        protocol.setDataPacket(dataPacket);


        return protocol;
    }

    public Protocol studentSuccessProtocol(StudentDTO studentDTO){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.STUDENT_SUCCESS.getCode());
        studentDTO=studentService.findStudentByStudentId(studentDTO.getStudentId());
        int idMaxSize=3*20+1;
        int nameMaxSize=3*20+1;
        byte[] id = (studentDTO.getStudentId()+'\0').getBytes();
        byte[] name = (studentDTO.getStudentName()+'\0').getBytes();
        byte[] dataPacket=new byte[idMaxSize+nameMaxSize];
        System.arraycopy(id,0,dataPacket,0,id.length);
        System.arraycopy(name,0,dataPacket,idMaxSize,name.length);
        protocol.setDataPacket(dataPacket);
        return protocol;
    }

    public Protocol loginFailProtocol(){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.FAIL.getCode());
        return protocol;
    }

    public Protocol loginReqProtocol(){
        Protocol protocol=new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.ID_AND_PW_REQ.getCode());
        return protocol;
    }






    // 클라이언트에서 사용


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

    public Protocol pgmExit(){
        Protocol protocol=new Protocol(ProtocolType.EXIT.getType());
        protocol.setProtocolCode(ExitCode.PGM_EXIT.getCode());
        return protocol;
    }
}
