package controller;

import network.LoginAndLogoutCode;
import network.ProtocolType;
import network.StudentCode;
import persistence.dto.*;
import service.*;
import network.Protocol;

import java.util.List;

public class StudentController {
    OpenLectureService openLectureService;
    LectureRoomByService lectureRoomByService;
    SyllabusService syllabusService;
    LectureRoomService lectureRoomService;
    LectureTimeService lectureTimeService;
    SyllabusWeekInfoService syllabusWeekInfoService;
    LectureService lectureService;
    ProfessorService professorService;
    PeriodService periodService;
    StudentService studentService=null;
    LectureHistoryService lectureHistoryService=null;

    public StudentController(StudentService _studentService,ProfessorService _professorService,OpenLectureService _openLectureService,LectureHistoryService _lectureHistoryService,SyllabusService _syllabusService,LectureRoomByService _lectureRoomByService ,LectureTimeService _lectureTimeService,LectureRoomService _lectureRoomService,SyllabusWeekInfoService _syllabusWeekInfoService,LectureService _lectureService,PeriodService _periodService){
        studentService=_studentService;
        professorService=_professorService;
        openLectureService=_openLectureService;
        lectureHistoryService=_lectureHistoryService;
        syllabusService=_syllabusService;
        lectureRoomByService=_lectureRoomByService;
        lectureTimeService=_lectureTimeService;
        lectureRoomService=_lectureRoomService;
        syllabusWeekInfoService=_syllabusWeekInfoService;
        lectureService=_lectureService;
        periodService=_periodService;
    }
    public StudentController(){}



    public Protocol setSendProtocol(Protocol receiveProtocol) {
        Protocol sendProtocol = new Protocol();
        sendProtocol.setProtocolType(ProtocolType.STUDENT.getType());
        try {
            if (receiveProtocol.getProtocolCode() == StudentCode.UPDATE_PERSONAL_INFO.getCode()) {//개인정보및 비빌번호 변경
                int idLength = 20+1;
                int nameLength = 20*3+1;
                int pwLength = 20*3+1;
                String id=new String(receiveProtocol.getPacket(),Protocol.LEN_HEADER,idLength).split("\0")[0];;
                String name=new String(receiveProtocol.getPacket(),Protocol.LEN_HEADER+idLength,nameLength).split("\0")[0];;
                String pw=new String(receiveProtocol.getPacket(),Protocol.LEN_HEADER+idLength+nameLength,nameLength).split("\0")[0];;
                StudentDTO studentDTO=new StudentDTO();
                studentDTO.setStudentId(id); studentDTO.setStudentName(name); studentDTO.setStudentPw(pw);
                int result=0;
                if(name.equals(" ")){
                    result=studentService.updateStudentPw(studentDTO);
                }else{
                    result=studentService.updateStudentName(studentDTO);
                }

                if(result!=0){
                    sendProtocol=successresult(sendProtocol,"수정 성공");
                }else{
                    sendProtocol=failresult(sendProtocol,"수정 실패");
                }

            } else if (receiveProtocol.getProtocolCode() == StudentCode.ENROLMENT.getCode()) {//수강신청
                String id=new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 21).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER+21, 6).split("\0")[0];
                String lectureSeperatdeLen = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 27, 6).split("\0")[0];
                System.out.println("StudentController에서 확인할 lectureCode,lectureSeperatdeLen : " + lectureCode + ", " + lectureSeperatdeLen);

                if(!openLectureService.isExistopenLectureId(lectureCode,Integer.parseInt(lectureSeperatdeLen))){//입력이 잘못된경우
                    sendProtocol=failresult(sendProtocol,"과목코드 또는 분반이 올바르지 않습니다");
                }
                else{
                    OpenLectureDTO openLectureDTO=openLectureService.getOpenLectureByOpenLectureId(openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode,Integer.parseInt(lectureSeperatdeLen)));
                    StudentDTO studentDTO = studentService.findStudentByStudentId(id);
                    String result=lectureHistoryService.registerOpenLecture(openLectureDTO,studentDTO,openLectureService,lectureService,periodService);
                    if(result.length()==0) {
                        sendProtocol=successresult(sendProtocol,"수강 신청 성공");
                    }else{
                        sendProtocol=failresult(sendProtocol,result);
                    }
                }

            } else if (receiveProtocol.getProtocolCode() == StudentCode.CANCEL_ENROLMENT.getCode()) {//수강취소
                String id=new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 21).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER+21, 6).split("\0")[0];
                String lectureSeperatdeLen = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 27, 6).split("\0")[0];
                System.out.println("StudentController에서 확인할 lectureCode,lectureSeperatdeLen : " + lectureCode + ", " + lectureSeperatdeLen);
                OpenLectureDTO openLectureDTO=openLectureService.getOpenLectureByOpenLectureId(openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode,Integer.parseInt(lectureSeperatdeLen)));
                StudentDTO studentDTO = studentService.findStudentByStudentId(id);

                if(!(periodService.isAvailableRegister(studentDTO.getStudentLevel()) || periodService.isAvailableRegister(5))){// 수강신청 기간이 아닌경우에 수강 취소하는 경우
                    sendProtocol=failresult(sendProtocol,"수강 취소 실패");
                    System.out.println("수강 취소 실패");
                    return sendProtocol;
                }

                int result=lectureHistoryService.deleteLectureHistory(openLectureDTO,studentDTO,openLectureService);
                if(result!=0) {
                    sendProtocol=successresult(sendProtocol,"수강 취소 성공");
                    System.out.println("수강 취소 성공");
                }else{
                    sendProtocol=failresult(sendProtocol,"수강 취소 실패");
                    System.out.println("수강 취소 실패");
                }

                //lectureHistoryService.registerOpenLecture()
            } else if (receiveProtocol.getProtocolCode() == StudentCode.READ_OPEN_LECTURE_LIST.getCode()) {//개설교과목 조회
                sendProtocol.setProtocolCode(StudentCode.RESULT_READ_OPEN_LECTURE_LIST.getCode());
                List<OpenLectureDTO> openLectureByCondition;
                if (receiveProtocol.getLength() == 0) {//전체조회
                    openLectureByCondition = openLectureService.readAllOpenLecture();
                } else if (receiveProtocol.getLength() == 6) {//학년에 따른 조회
                    String LV = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 6).split("\0")[0];
                    LectureDTO lectureDTO = new LectureDTO();
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    lectureDTO.setLectureLevel(Integer.parseInt(LV));
                    openLectureByCondition = openLectureService.readOpenLectureByCondition(lectureDTO, professorDTO);
                } else if (receiveProtocol.getLength() == 61) {//교수이름에 따른 조회
                    String PN = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 61).split("\0")[0];
                    LectureDTO lectureDTO = new LectureDTO();
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorName(PN);
                    openLectureByCondition = openLectureService.readOpenLectureByCondition(lectureDTO, professorService.findByName(PN));
                } else {//학년정보와 교수이름을 통한 조회
                    String LV = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 6).split("\0")[0];
                    String PN = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 6, 61).split("\0")[0];
                    System.out.println("StudentController에서 확인할 level,prefesser : " + LV + ", " + PN);

                    LectureDTO lectureDTO = new LectureDTO();
                    lectureDTO.setLectureLevel(Integer.parseInt(LV));
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(PN);
                    openLectureByCondition = openLectureService.readOpenLectureByCondition(lectureDTO, professorService.findByName(PN));
                }
                sendProtocol.setDataPacket(createProtocolPacketDataOPEN_LECTURE_LIST(openLectureByCondition));

            } else if (receiveProtocol.getProtocolCode() == StudentCode.READ_SELECTED_LECTURE_SYLLABUS.getCode()) {//강의 계획서 조회
                sendProtocol.setProtocolCode(StudentCode.RESULT_READ_SELECTED_LECTURE_SYLLABUS.getCode());
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 6).split("\0")[0];
                String lectureSeperatdeLen = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 6, 6).split("\0")[0];
                System.out.println("StudentController에서 확인할 lectureCode,lectureSeperatdeLen : " + lectureCode + ", " + lectureSeperatdeLen);

                SyllabusDTO syllabusCondition;
                List<SyllabusWeekInfoDTO> SyllabusWeekInfoCondition;
                OpenLectureDTO openLectureDTO;
                openLectureDTO = openLectureService.getOpenLectureByOpenLectureId(openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, Integer.parseInt(lectureSeperatdeLen)));
                syllabusCondition = syllabusService.findSyllabusByOpenLectureId(openLectureDTO);
                SyllabusWeekInfoCondition = syllabusWeekInfoService.findSyllabusWeekInfoBySyllabusId(syllabusCondition.getSyllabusId());
                sendProtocol.setDataPacket(createProtocolPacketDataSELECTED_LECTURE_SYLLABUS(openLectureDTO, syllabusCondition, SyllabusWeekInfoCondition));
            } else if (receiveProtocol.getProtocolCode() == StudentCode.READ_OWN_SCHEDULE.getCode()) {//본인 시간표 조회
                sendProtocol.setProtocolCode(StudentCode.RESULT_READ_OWN_SCHEDULE.getCode());
                String id=new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 21).split("\0")[0];
                sendProtocol.setDataPacket(createProtocolPacketDataTimetable(id));
            }
        }catch(NullPointerException e){//자료조회에 실패한경우
            sendProtocol=failresult(sendProtocol,"찾으려는 자료가 없습니다.");

        }catch(Exception e){
            sendProtocol=failresult(sendProtocol,e.toString());
        }

    return sendProtocol;
    }

    public Protocol requestLevelandPorfessrName(String level, String professorName){//개설교과목을 학년과 교수이름을 통해서 조회할떄 사용할 프로토콜을 생성하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_OPEN_LECTURE_LIST.getCode());
        int LVLen = 5+1;
        int PNLen = 20*3+1;
        level += '\0';
        professorName += '\0';
        byte[] data = new byte[LVLen+PNLen];
        System.arraycopy(level.getBytes(),0,data,0,level.getBytes().length);
        System.arraycopy(professorName.getBytes(),0,data,LVLen,professorName.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestLevel(String level){//개설교과목을 학년 통해서 조회할떄 사용할 프로토콜을 생성하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_OPEN_LECTURE_LIST.getCode());
        int LVLen = 5+1;
        level += '\0';
        byte[] data = new byte[LVLen];
        System.arraycopy(level.getBytes(),0,data,0,level.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestPorfessrName(String professorName){//개설교과목을 교수이름을 통해서 조회할떄 사용할 프로토콜을 생성하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_OPEN_LECTURE_LIST.getCode());
        int PNLen = 20*3+1;
        professorName += '\0';
        byte[] data = new byte[PNLen];
        System.arraycopy(professorName.getBytes(),0,data,0,professorName.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestLevelandPorfessrName(){//개설교과목 조회할떄 전체조회할떄 사용할 프로토콜을 생성하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_OPEN_LECTURE_LIST.getCode());
        return protocol;
    }
    public Protocol requestSyllabus(String lectureCode,int lectureSeperatdeNumber){//학생이 강의 게획서를 요청하는 프로토콜을 생성하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_SELECTED_LECTURE_SYLLABUS.getCode());
        int lectureCodeLen = 5+1;
        int lectureSeperatdeLen = 5+1;
        lectureCode += '\0';
        String lectureSeperatde=Integer.toString(lectureSeperatdeNumber)+ '\0';
        byte[] data = new byte[lectureCodeLen+lectureSeperatdeLen];
        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(lectureSeperatde.getBytes(),0,data,lectureCodeLen,lectureSeperatde.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestUpdateName(String id,String name){//학생의 이름을 수정하기위한 프로토콜을 생성하고 수정할 data를 프로토콜에 저장하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.UPDATE_PERSONAL_INFO.getCode());
        int idLength = 20+1;
        int nameLength = 20*3+1;
        int pwLength = 20*3+1;
        id+='\0';
        name+='\0';
        String pw=" "+'\0';
        byte[] data = new byte[idLength+nameLength+pwLength];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(name.getBytes(),0,data,idLength,name.getBytes().length);
        System.arraycopy(pw.getBytes(),0,data,idLength+nameLength,pw.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;

    }
    public Protocol requestUpdatePw(String id,String pw){//학생의 비밀번호를 수정하기위한 프로토콜을 생성하고 수정할 data를 프로토콜에 저장하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.UPDATE_PERSONAL_INFO.getCode());
        int idLength = 20+1;
        int nameLength = 20*3+1;
        int pwLength = 20*3+1;

        id+='\0';
        String name=" "+'\0';;
        pw+='\0';
        byte[] data = new byte[idLength+nameLength+pwLength];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(name.getBytes(),0,data,idLength,name.getBytes().length);
        System.arraycopy(pw.getBytes(),0,data,idLength+nameLength,pw.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestEnrolment(String id,String lectureCode,int lectureSeperatdeNumber){//수강 싱청을 요청하기위한 프로토콜을 생성하고 필료로하는 data를 프로토콜에 저장하는 합수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.ENROLMENT.getCode());
        int idLen=20+1;
        int lectureCodeLen = 5+1;
        int lectureSeperatdeLen = 5+1;
        id+='\0';
        lectureCode += '\0';
        String lectureSeperatde=Integer.toString(lectureSeperatdeNumber)+ '\0';
        byte[] data = new byte[idLen+lectureCodeLen+lectureSeperatdeLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(lectureCode.getBytes(),0,data,idLen,lectureCode.getBytes().length);
        System.arraycopy(lectureSeperatde.getBytes(),0,data,idLen+lectureCodeLen,lectureSeperatde.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestCancleEnrolment(String id,String lectureCode,int lectureSeperatdeNumber){//수강취소를 요청하기위한 프로토콜을 생성하고 필요로하는 data를 프로토콜에 저장하는 함수이다.
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.CANCEL_ENROLMENT.getCode());
        int idLen=20+1;
        int lectureCodeLen = 5+1;
        int lectureSeperatdeLen = 5+1;
        id+='\0';
        lectureCode += '\0';
        String lectureSeperatde=Integer.toString(lectureSeperatdeNumber)+ '\0';
        byte[] data = new byte[idLen+lectureCodeLen+lectureSeperatdeLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(lectureCode.getBytes(),0,data,idLen,lectureCode.getBytes().length);
        System.arraycopy(lectureSeperatde.getBytes(),0,data,idLen+lectureCodeLen,lectureSeperatde.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol requestTimetable(String id){//시간표를 요청하는 프로토콜을 생성하고 필요로하는 data를 입력하는 함수이다..
        Protocol protocol=new Protocol(ProtocolType.STUDENT.getType());
        protocol.setProtocolCode(StudentCode.READ_OWN_SCHEDULE.getCode());
        int idLen=20+1;
        id+='\0';
        byte[] data = new byte[idLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }

    public Protocol failresult(Protocol protocol,String str){//실패한경우 실패사유를 data에 프로토콜에 저장하는 함수이다.
        protocol.setProtocolCode(StudentCode.FAILURE_CREATE_UPDATE_DELETE_READ.getCode());
        String fail="실패사유 : "+str+'\0';
        int failLength=35*3+1;
        byte[] data=new byte[failLength];
        System.arraycopy(fail.getBytes(),0,data,0,fail.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }
    public Protocol successresult(Protocol protocol,String str){//성공한경우 성공한것을 알려주기 위한 함수이다.
        protocol.setProtocolCode(StudentCode.SUCCESS_CREATE_UPDATE_DELETE.getCode());
        String success=str+'\0';
        int successLength=20*3+1;
        byte[] data=new byte[successLength];
        System.arraycopy(success.getBytes(),0,data,0,success.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }

    public byte[] createProtocolPacketDataOPEN_LECTURE_LIST(List<OpenLectureDTO> openLectureByCondition){
        int listenLevelLength=5+1;
        int lectureNameLength=20*3+1;
        int lectureCodeLength=20*3+1;
        int lectureSeperatdeNumberLength=5+1;
        int lectureProfessorLength=20*3+1;
        int lectureTimeAndRoomlength=20*3+20*3+1;
        int maxStudentNumberLength=5+1;
        int curStudentNumberLength=5+1;
        int CanapplySLength=10*3+1;

        int countline=0;
        for(OpenLectureDTO openLectureDTO:openLectureByCondition){//개설교과목이 몇가지인가를 알기위한 반복문이다.
            countline++;
        }
        String listenLevel;
        String lectureName;
        String lectureCode;
        String lectureSeperatdeNumber;
        String lectureProfessor;
        String maxStudentNumber;
        String curStudentNumber;
        String lectureTimeAndRoom;
        String CanapplyS;
        int oneLineLen=listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength+curStudentNumberLength+CanapplySLength;
        byte[] data = new byte[oneLineLen*countline];
        int cont=0;
        for(OpenLectureDTO openLectureDTO:openLectureByCondition){//개설교과목 정보를 data에 저장하는 for문이다.
            listenLevel= Integer.toString(openLectureDTO.getLectureDTO().getLectureLevel())+'\0';
            lectureName=openLectureDTO.getLectureDTO().getLectureName()+'\0';
            lectureCode=openLectureDTO.getLectureDTO().getLectureCode()+'\0';
            lectureSeperatdeNumber= Integer.toString(openLectureDTO.getSeperatedNumber())+'\0';
            lectureProfessor=openLectureDTO.getProfessorDTO().getProfessorName()+'\0';
            lectureTimeAndRoom=" ";
            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByService.selectAllLectureRoomByTime(openLectureDTO.getOpenLectureId());
            for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS) {//강의기간과 강의실을 하나의 문자열로 만들기위한 for문이다.
                LectureRoomDTO lectureRoomDTO = lectureRoomService.selectLectureRoomDTO(lectureRoomByTimeDTO.getLectureRoomId());
                LectureTimeDTO lectureTimeDTO = lectureTimeService.selectLectureTimeId(lectureRoomByTimeDTO.getLectureTimeId());
                String lectureroom=lectureRoomDTO.getBuildingName()+lectureRoomDTO.getLectureRoomNumber();
                String lecturetime=lectureTimeDTO.getLectureDay()+lectureTimeDTO.getLecturePeriod();

                lectureTimeAndRoom+=lectureroom+"/"+lecturetime+",";
            }

            maxStudentNumber= Integer.toString(openLectureDTO.getMaxStudentNumber())+'\0';
            curStudentNumber= Integer.toString(openLectureDTO.getCurStudentNumber())+'\0';
            boolean isAvalableLectureRegister = periodService.isAvailableRegister(openLectureDTO.getLectureDTO().getLectureLevel());
            if(!isAvalableLectureRegister){
                isAvalableLectureRegister = periodService.isAvailableRegister(5);
            }
            if(isAvalableLectureRegister)//수강신청이 가능한지 알기위한 조건문이다.
                CanapplyS="수강 신청 가능";
            else
                CanapplyS="수강 신청 불가능";
            System.arraycopy(listenLevel.getBytes(),0,data,(oneLineLen*cont),listenLevel.getBytes().length);
            System.arraycopy(lectureName.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength,lectureName.getBytes().length);
            System.arraycopy(lectureCode.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength,lectureCode.getBytes().length);
            System.arraycopy(lectureSeperatdeNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength,lectureSeperatdeNumber.getBytes().length);
            System.arraycopy(lectureProfessor.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength,lectureProfessor.getBytes().length);
            System.arraycopy(lectureTimeAndRoom.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength,lectureTimeAndRoom.getBytes().length);
            System.arraycopy(maxStudentNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength,maxStudentNumber.getBytes().length);
            System.arraycopy(curStudentNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength,curStudentNumber.getBytes().length);
            System.arraycopy(CanapplyS.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength+curStudentNumberLength,CanapplyS.getBytes().length);

            cont=cont+1;
        }
        return data;
    }

    /*
    강의 게획서를 페킷에 저장할 data를 byte로 치환하는 함수이다.
     */
    public byte[] createProtocolPacketDataSELECTED_LECTURE_SYLLABUS(OpenLectureDTO openLectureDTO,SyllabusDTO syllabusCondition,List<SyllabusWeekInfoDTO> SyllabusWeekInfoCondition){
        int lectureCodelength=20*3+1;
        int lectureSeperatdeNumberlength=5+1;
        int lectureNamelength=20*3+1;
        int porfessorNamelength=20*3+1;
        int lectureCreditlength=5+1;
        int lectureTimeCountlength=5+1;
        int booknamelength=30*3+1;
        int lectureGoallength=30*3+1;

        int weeklength=5+1;
        int subjectlength=20*3+1;
        int contentlength=50*3+1;
        int assigmentlength=30*3+1;
        int evaluationlength=30*3+1;

        String lectureCode;
        String lectureSeperatdeNumber;
        String lectureName;
        String porfessorName;
        String lectureCredit;
        String lectureTimeCount;
        String bookname;
        String lectureGoal;

        String week;
        String subject;
        String content;
        String assigment;
        String evaluation;

        int syllabuslength=lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+ lectureCreditlength+lectureTimeCountlength+booknamelength+lectureGoallength;
        int SyllabusWeekInfoOneLineLength=weeklength+subjectlength+contentlength+assigmentlength+evaluationlength;

        int SyllabusWeekInfoOneLineLengthcount=0;
        for(SyllabusWeekInfoDTO syllabusWeekInfoDTO:SyllabusWeekInfoCondition){//추차별 정보가 몇주차 까지 있는지 확인하는 코드이다.
            SyllabusWeekInfoOneLineLengthcount++;
        }
        byte[] data = new byte[syllabuslength+SyllabusWeekInfoOneLineLengthcount*SyllabusWeekInfoOneLineLength];

        //강의계획서의 내용을 data에 저장하는 코드이다,
        lectureCode=openLectureDTO.getLectureCode();
        lectureSeperatdeNumber=Integer.toString(openLectureDTO.getSeperatedNumber());
        lectureName=lectureService.findLectureByLectureCode(openLectureDTO.getLectureCode()).getLectureName();
        ProfessorDTO temp=new ProfessorDTO();
        temp.setProfessorId(openLectureDTO.getProfessorId());
        porfessorName=professorService.findProfessorById(temp).getProfessorName();
        lectureCredit=Integer.toString(lectureService.findLectureByLectureCode(openLectureDTO.getLectureCode()).getLectureCredit());
        int lectureTimecnt=0;
        List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByService.selectAllLectureRoomByTime(openLectureDTO.getOpenLectureId());
        for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS) {
            lectureTimecnt++;
        }
        lectureTimeCount=Integer.toString(lectureTimecnt);
        bookname=syllabusCondition.getBookName();
        lectureGoal=syllabusCondition.getLectureGoal();

        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(lectureSeperatdeNumber.getBytes(),0,data,lectureCodelength,lectureSeperatdeNumber.getBytes().length);
        System.arraycopy(lectureName.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength,lectureName.getBytes().length);
        System.arraycopy(porfessorName.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength,porfessorName.getBytes().length);
        System.arraycopy(lectureCredit.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength,lectureCredit.getBytes().length);
        System.arraycopy(lectureTimeCount.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+lectureCreditlength,lectureTimeCount.getBytes().length);
        System.arraycopy(bookname.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+ lectureCreditlength+lectureTimeCountlength,bookname.getBytes().length);
        System.arraycopy(lectureGoal.getBytes(),0,data,lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+ lectureCreditlength+lectureTimeCountlength+booknamelength,lectureGoal.getBytes().length);

        int cont=0;
        for(SyllabusWeekInfoDTO syllabusWeekInfoDTO:SyllabusWeekInfoCondition){//주차별 정보를 확인 하고 data에 저장하는 반복문이다.
            week=Integer.toString(syllabusWeekInfoDTO.getSyllabusWeek());
            subject=syllabusWeekInfoDTO.getSyllabusSubject();
            content=syllabusWeekInfoDTO.getSyllabusContent();
            assigment=syllabusWeekInfoDTO.getSyllabusAssignment();
            evaluation=syllabusWeekInfoDTO.getSyllabusEvaluation();

            System.arraycopy(week.getBytes(),0,data,syllabuslength+(SyllabusWeekInfoOneLineLength*cont),week.getBytes().length);
            System.arraycopy(subject.getBytes(),0,data,syllabuslength+(SyllabusWeekInfoOneLineLength*cont)+weeklength,subject.getBytes().length);
            System.arraycopy(content.getBytes(),0,data,syllabuslength+(SyllabusWeekInfoOneLineLength*cont)+weeklength+subjectlength,content.getBytes().length);
            System.arraycopy(assigment.getBytes(),0,data,syllabuslength+(SyllabusWeekInfoOneLineLength*cont)+weeklength+subjectlength+contentlength,assigment.getBytes().length);
            System.arraycopy(evaluation.getBytes(),0,data,syllabuslength+(SyllabusWeekInfoOneLineLength*cont)+weeklength+subjectlength+contentlength+assigmentlength,evaluation.getBytes().length);
        cont++;
        }

        return data;
    }

    public byte[] createProtocolPacketDataTimetable(String id){//시간표를 프로토콜의 데이터에 저장하기위한 함수이다.
        String result=lectureRoomByService.getStudentSchedule(openLectureService,lectureHistoryService,id)+'\0';
        int resultLen=result.getBytes().length;
        System.out.println(resultLen);
        byte[] data = new byte[resultLen];
        System.arraycopy(result.getBytes(),0,data,0,result.getBytes().length);
        return data;
    }

    public Protocol logoutReqProtocol(){//로그아웃을 위한 프로토콜이다.
        Protocol protocol = new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.LOGOUT_REQ.getCode());
        return protocol;
    }
}
