package controller;

import network.LoginAndLogoutCode;
import network.ProfessorCode;
import network.ProtocolType;
import persistence.dto.*;
import service.*;
import network.Protocol;

import java.util.List;


public class ProfessorController {
    ProfessorService professorService=null;
    OpenLectureService openLectureService = null;
    SyllabusService syllabusService = null;
    LectureRoomByService lectureRoomByService = null;
    LectureRoomService lectureRoomService = null;
    LectureTimeService lectureTimeService = null;
    LectureHistoryService lectureHistoryService = null;
    StudentService studentService = null;
    LectureService lectureService = null;
    SyllabusWeekInfoService syllabusWeekInfoService = null;
    PeriodService periodService=null;

    public ProfessorController(){ }

    public ProfessorController(ProfessorService professorService,OpenLectureService openLectureService,SyllabusService syllabusService,
                               LectureRoomByService lectureRoomByService,LectureHistoryService lectureHistoryService,StudentService studentService,
                               LectureRoomService lectureRoomService,LectureTimeService lectureTimeService,LectureService lectureService,
    SyllabusWeekInfoService syllabusWeekInfoService,PeriodService periodService) {
        this.openLectureService= openLectureService;
        this.professorService = professorService;
        this.syllabusService = syllabusService;
        this.lectureRoomByService = lectureRoomByService;
        this.lectureRoomService = lectureRoomService;
        this.lectureTimeService = lectureTimeService;
        this.lectureHistoryService=lectureHistoryService;
        this.studentService=studentService;
        this.lectureService=lectureService;
        this.syllabusWeekInfoService=syllabusWeekInfoService;
        this.periodService=periodService;
    }

    public Protocol setSendProtocol(Protocol receiveProtocol) {
        Protocol sendProtocol = null;
        try {


            if (receiveProtocol.getProtocolCode() == ProfessorCode.UPDATE_PRIVACY_AND_PASSWORD.getCode()) {//교수 개인 정보 수정
                String id = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 61).split("\0")[0];
                String pw = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61, 61).split("\0")[0];
                String name = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61, 61).split("\0")[0];
                String ssn = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 183, 15).split("\0")[0];
                String email = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 198, 91).split("\0")[0];
                String phoneNum = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 198 + 91, 16).split("\0")[0];

                System.out.println("id: " + id);
                System.out.println("pw: " + pw);
                System.out.println("name: " + name);
                System.out.println("ssn: " + ssn);
                System.out.println("email: " + email);
                System.out.println("phoneNum: " + phoneNum);

                if (!pw.equals(" ")) {// 패스워드 수정시
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(id);
                    professorDTO.setProfessorPw(pw);
                    int result = professorService.updatePw(professorDTO);

                    if (result == 1) {
                        sendProtocol = successProtocol();
                    } else {
                        sendProtocol = failProtocol();
                    }
                } else if (!name.equals(" ")) {//이름 수정시
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(id);
                    professorDTO.setProfessorName(name);
                    int result = professorService.updateName(professorDTO);

                    if (result == 1) {
                        sendProtocol = successProtocol();
                    } else {
                        sendProtocol = failProtocol();
                    }
                } else if (!ssn.equals(" ")) {//주민 번호 수정시
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(id);
                    professorDTO.setSsn(ssn);
                    int result = professorService.updateSSN(professorDTO);

                    if (result == 1) {
                        sendProtocol = successProtocol();
                    } else {
                        sendProtocol = failProtocol();
                    }
                } else if (!email.equals(" ")) {//이메일 수정시
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(id);
                    professorDTO.setEMail(email);
                    int result = professorService.updateEmail(professorDTO);

                    if (result == 1) {
                        sendProtocol = successProtocol();
                    } else {
                        sendProtocol = failProtocol();
                    }
                } else if (!phoneNum.equals(" ")) {//폰 번호 수정시
                    ProfessorDTO professorDTO = new ProfessorDTO();
                    professorDTO.setProfessorId(id);
                    professorDTO.setProfessorPhoneNumber(phoneNum);
                    int result = professorService.updatePhoneNumber(professorDTO);

                    if (result == 1) {
                        sendProtocol = successProtocol();
                    } else {
                        sendProtocol = failProtocol();
                    }
                }
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.CREATE_SYLLABUS.getCode()) {//강의계획서 생성
                String id = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 61).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 31, 6).split("\0")[0];
                String bookName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 31 + 6, 91).split("\0")[0];
                String lectureGoal = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 31 + 6 + 91, 91).split("\0")[0];

                int seperatedNum = Integer.parseInt(seperatedNumStr);

                System.out.println("id: " + id);
                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);
                System.out.println("bookName: " + bookName);
                System.out.println("lectureGoal: " + lectureGoal);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO.setOpenLectureId(openLectureId);

                if (!periodService.isAvailableRegister(6)) {// 강의 계획서 입력 기간이 아닌경우
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }

                if (syllabusService.isSyllabusExist(openLectureDTO)) {// 강의 계획서가 이미 존재할 경우
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }

                SyllabusDTO syllabusDTO = new SyllabusDTO();
                syllabusDTO.setOpenLectureId(openLectureId);
                syllabusDTO.setBookName(bookName);
                syllabusDTO.setLectureGoal(lectureGoal);
                int result = syllabusService.insertSyllabus(syllabusDTO);

                if (result == 1) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.UPDATE_SYLLABUS.getCode()) {//강의 계획서 수정
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31, 6).split("\0")[0];
                String bookName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6, 91).split("\0")[0];
                String lectureGoal = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 91, 91).split("\0")[0];

                int seperatedNum = Integer.parseInt(seperatedNumStr);

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);
                System.out.println("bookName: " + bookName);
                System.out.println("lectureGoal: " + lectureGoal);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);

                System.out.println("openLectureId: " + openLectureId);

                SyllabusDTO syllabusDTO = new SyllabusDTO();
                syllabusDTO.setOpenLectureId(openLectureId);
                if (!bookName.equals(" ")) {
                    syllabusDTO.setBookName(bookName);
                } else if (!lectureGoal.equals(" ")) {
                    syllabusDTO.setLectureGoal(lectureGoal);
                }
                int result = syllabusService.updateSyllabus(syllabusDTO);

                if (result == 1) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.READ_LECTURE_LIST_REQ.getCode()) {//개설 교과목 조회
                String id = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];

                System.out.println("id: " + id);

                ProfessorDTO professorDTO = new ProfessorDTO();
                professorDTO.setProfessorId(id);

                LectureDTO lectureDTO = new LectureDTO();

                List<OpenLectureDTO> list = openLectureService.readOpenLectureByCondition(lectureDTO, professorDTO);
                byte[] data = createProtocolPacketDataByOpenLectureList(list);//서버에서 실행한 개설 교과목 조회 명령의 정보를 byte 배열로 치환.


                sendProtocol = responseOpenLectureListInfo(data);
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.READ_SYLLABUS_LIST_REQ.getCode()) {//강의 계획서 조회
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 6).split("\0")[0];
                String lectureSeperated = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 6, 6).split("\0")[0];

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("lectureSeperated: " + lectureSeperated);

                SyllabusDTO syllabusCondition;
                List<SyllabusWeekInfoDTO> syllabusWeekInfoCondition;
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO = openLectureService.getOpenLectureByOpenLectureId(openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, Integer.parseInt(lectureSeperated)));
                syllabusCondition = syllabusService.findSyllabusByOpenLectureId(openLectureDTO);
                /////////
                if (syllabusCondition != null) {
                    syllabusWeekInfoCondition = syllabusWeekInfoService.findSyllabusWeekInfoBySyllabusId(syllabusCondition.getSyllabusId());
                    byte[] data = createProtocolPacketDataBySyllabusList(openLectureDTO, syllabusCondition, syllabusWeekInfoCondition);//서버에서 실행한 강의 계획서 (+주차 정보) 조회 명령의 정보를 byte 배열로 치환.
                    sendProtocol = responseSyllabusListInfo(data);
                } else {
                    Protocol newProtocol = new Protocol(ProtocolType.PROFESSOR.getType());
                    newProtocol.setProtocolCode(ProfessorCode.READ_SYLLABUS_LIST_RES.getCode());
                    sendProtocol = newProtocol;
                }
                /////////
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.READ_OPEN_LECTURE_ENROLLED_STUDENT_REQ.getCode()) {//개설 교과목 수강 신청한 학생 조회
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31, 6).split("\0")[0];

                int seperatedNum = Integer.parseInt(seperatedNumStr);

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);

                System.out.println("openLectureId: " + openLectureId);

                int totalPage = lectureHistoryService.getTotalPage(openLectureId, 10);
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO.setOpenLectureId(openLectureId);

                List<StudentDTO> list = lectureHistoryService.readStudentList(openLectureDTO, studentService, 1, totalPage * 10);
                byte[] data = createProtocolPacketDataByStudentList(list);//서버에서 실행한 개설 교과목의 수강 신청 학생 조회 명령의 정보를 byte 배열로 치환.

                sendProtocol = responseLectureEnrolledStudentListInfo(data);
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.READ_PROFESSOR_SCHEDULE_REQ.getCode()) {//교수 개인 시간표 조회
                String id = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];

                System.out.println("id: " + id);

                String schedule = lectureRoomByService.getProfessorSchedule(openLectureService, id);

                System.out.println("schedule: " + schedule);

                byte[] data = new byte[schedule.getBytes().length];
                System.arraycopy(schedule.getBytes(), 0, data, 0, schedule.getBytes().length);

                sendProtocol = responseProfessorSchedule(data);
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.CREATE_SYLLABUS_WEEK_INFO.getCode()) {//강의 계획서 주차정보 생성
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31, 6).split("\0")[0];
                String syllabusWeekStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6, 6).split("\0")[0];
                String syllabusSubject = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6, 61).split("\0")[0];
                String syllabusContent = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61, 151).split("\0")[0];
                String syllabusAssignment = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61 + 151, 91).split("\0")[0];
                String syllabusEvaluation = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61 + 151 + 91, 91).split("\0")[0];

                if (!periodService.isAvailableRegister(6)) {// 강의 계획서 입력 기간이 아닌경우
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }

                int seperatedNum = Integer.parseInt(seperatedNumStr);
                int syllabusWeek = Integer.parseInt(syllabusWeekStr);

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);
                System.out.println("syllabusWeek: " + syllabusWeek);
                System.out.println("syllabusSubject: " + syllabusSubject);
                System.out.println("syllabusContent: " + syllabusContent);
                System.out.println("syllabusAssignment: " + syllabusAssignment);
                System.out.println("syllabusEvaluation: " + syllabusEvaluation);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO.setOpenLectureId(openLectureId);

                SyllabusDTO syllabusDTO = syllabusService.findSyllabusByOpenLectureId(openLectureDTO);
                int syllabusId = syllabusDTO.getSyllabusId();

                if (syllabusWeekInfoService.isExistSyllabusWeekInfo(syllabusId, syllabusWeek)) {//강의 계획서 주차정보에 생성하고픈 주차정보와 이미 동일 주차 존재시
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }
                SyllabusWeekInfoDTO syllabusWeekInfoDTO = new SyllabusWeekInfoDTO();
                syllabusWeekInfoDTO.setSyllabusId(syllabusId);
                syllabusWeekInfoDTO.setSyllabusWeek(syllabusWeek);
                syllabusWeekInfoDTO.setSyllabusContent(syllabusContent);
                syllabusWeekInfoDTO.setSyllabusSubject(syllabusSubject);
                syllabusWeekInfoDTO.setSyllabusAssignment(syllabusAssignment);
                syllabusWeekInfoDTO.setSyllabusEvaluation(syllabusEvaluation);

                int result = syllabusWeekInfoService.insertSyllabusWeekInfo(syllabusWeekInfoDTO);

                if (result == 1) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.UPDATE_SYLLABUS_WEEK_INFO.getCode()) {//강의 계획서 주차 정보 수정
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31, 6).split("\0")[0];
                String syllabusWeekStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6, 6).split("\0")[0];
                String syllabusSubject = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6, 61).split("\0")[0];
                String syllabusContent = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61, 151).split("\0")[0];
                String syllabusAssignment = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61 + 151, 91).split("\0")[0];
                String syllabusEvaluation = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6 + 6 + 61 + 151 + 91, 91).split("\0")[0];

                int seperatedNum = Integer.parseInt(seperatedNumStr);
                int syllabusWeek = Integer.parseInt(syllabusWeekStr);

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);
                System.out.println("syllabusWeek: " + syllabusWeek);
                System.out.println("syllabusSubject: " + syllabusSubject);
                System.out.println("syllabusContent: " + syllabusContent);
                System.out.println("syllabusAssignment: " + syllabusAssignment);
                System.out.println("syllabusEvaluation: " + syllabusEvaluation);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO.setOpenLectureId(openLectureId);

                SyllabusDTO syllabusDTO = syllabusService.findSyllabusByOpenLectureId(openLectureDTO);
                int syllabusId = syllabusDTO.getSyllabusId();

                if (!syllabusWeekInfoService.isExistSyllabusWeekInfo(syllabusId, syllabusWeek)) {
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }

                SyllabusWeekInfoDTO syllabusWeekInfoDTO = new SyllabusWeekInfoDTO();
                syllabusWeekInfoDTO.setSyllabusId(syllabusId);
                syllabusWeekInfoDTO.setSyllabusWeek(syllabusWeek);


                if (!syllabusContent.equals(" ")) {
                    syllabusWeekInfoDTO.setSyllabusContent(syllabusContent);
                } else if (!syllabusSubject.equals(" ")) {
                    syllabusWeekInfoDTO.setSyllabusSubject(syllabusSubject);
                } else if (!syllabusAssignment.equals(" ")) {
                    syllabusWeekInfoDTO.setSyllabusAssignment(syllabusAssignment);
                } else if (!syllabusEvaluation.equals(" ")) {
                    syllabusWeekInfoDTO.setSyllabusEvaluation(syllabusEvaluation);
                }

                int result = syllabusWeekInfoService.updateSyllabusWeekInfo(syllabusWeekInfoDTO);

                if (result == 1) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == ProfessorCode.DELETE_SYLLABUS_WEEK_INFO.getCode()) {//강의 계획서 주차 정보 삭제
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 31).split("\0")[0];
                String seperatedNumStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31, 6).split("\0")[0];
                String syllabusWeekStr = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 31 + 6, 6).split("\0")[0];

                int seperatedNum = Integer.parseInt(seperatedNumStr);
                int syllabusWeek = Integer.parseInt(syllabusWeekStr);

                System.out.println("lectureCode: " + lectureCode);
                System.out.println("seperatedNum: " + seperatedNum);
                System.out.println("syllabusWeek: " + syllabusWeek);

                int openLectureId = openLectureService.getOpenLectureIdByLectureCodeAndSeperatedNumber(lectureCode, seperatedNum);
                OpenLectureDTO openLectureDTO = new OpenLectureDTO();
                openLectureDTO.setOpenLectureId(openLectureId);

                SyllabusDTO syllabusDTO = syllabusService.findSyllabusByOpenLectureId(openLectureDTO);
                int syllabusId = syllabusDTO.getSyllabusId();

                if (!syllabusWeekInfoService.isExistSyllabusWeekInfo(syllabusId, syllabusWeek)) {
                    sendProtocol = failProtocol();
                    return sendProtocol;
                }

                SyllabusWeekInfoDTO syllabusWeekInfoDTO = new SyllabusWeekInfoDTO();
                syllabusWeekInfoDTO.setSyllabusId(syllabusId);
                syllabusWeekInfoDTO.setSyllabusWeek(syllabusWeek);

                int result = syllabusWeekInfoService.deleteSyllabusWeekInfo(syllabusWeekInfoDTO);

                if (result == 1) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            }

        }catch(Exception e){
            sendProtocol = failProtocol();
        }
            return sendProtocol;
    }


    public Protocol successProtocol(){//생성,수정,삭제 성공시 보낼 프로토콜 생성
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.SUCCESS_CREATE_AND_UPDATE_AND_DELETE.getCode());
        return protocol;
    }

    public Protocol failProtocol(){//생성,수정,삭제 실패시 보낼 프로토콜 생성
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.FAIL_CREATE_AND_UPDATE_AND_DELETE_AND_READ.getCode());
        return protocol;
    }

    public Protocol requestProfessorUpdateInfo(String id, String inputData, int menuNum2){//교수 개인 정보 수정 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.UPDATE_PRIVACY_AND_PASSWORD.getCode());
        int idLen = 20*3+1;
        int pwLen = 20*3+1;
        int nameLen = 20*3+1;
        int ssnLen = 14+1;
        int emailLen = 30*3+1;
        int phoneNumLen = 15+1;

        String nullData = " \0";
        inputData += '\0';

        byte[] data = new byte[idLen+pwLen+nameLen+ssnLen+emailLen+phoneNumLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,idLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,idLen+pwLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,idLen+pwLen+nameLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,idLen+pwLen+nameLen+ssnLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,idLen+pwLen+nameLen+ssnLen+emailLen,nullData.getBytes().length);


        if(menuNum2 == 1){
            System.arraycopy(inputData.getBytes(),0,data,idLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 2){
            System.arraycopy(inputData.getBytes(),0,data,idLen+pwLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 3){
            System.arraycopy(inputData.getBytes(),0,data,idLen+pwLen+nameLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 4){
            System.arraycopy(inputData.getBytes(),0,data,idLen+pwLen+nameLen+ssnLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 5){
            System.arraycopy(inputData.getBytes(),0,data,idLen+pwLen+nameLen+ssnLen+emailLen,inputData.getBytes().length);
        }

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestProfessorSchedule(String id) {//교수 개인 시간표 조회 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_PROFESSOR_SCHEDULE_REQ.getCode());

        int idLen = 20*3+1;

        id += "\0";

        byte[] data = new byte[idLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 생성 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusCreateInfo(String id,String lectureCode,int seperatedNum,String bookName,String lectureGoal){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.CREATE_SYLLABUS.getCode());

        int idLen = 20*3+1;
        int lectureCodeLen = 10*3+1;
        int seperatedNumLen = 5+1;
        int bookNameLen = 30*3+1;
        int lectureGoalLen = 30*3+1;

        String seperatedNumStr = Integer.toString(seperatedNum);

        id += "\0";
        lectureCode += "\0";
        seperatedNumStr += "\0";
        bookName += "\0";
        lectureGoal += "\0";

        byte[] data = new byte[idLen+lectureCodeLen+seperatedNumLen+bookNameLen+lectureGoalLen];

        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
        System.arraycopy(lectureCode.getBytes(),0,data,idLen,lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(),0,data,idLen+lectureCodeLen,seperatedNumStr.getBytes().length);
        System.arraycopy(bookName.getBytes(),0,data,idLen+lectureCodeLen+seperatedNumLen,bookName.getBytes().length);
        System.arraycopy(lectureGoal.getBytes(),0,data,idLen+lectureCodeLen+seperatedNumLen+bookNameLen,lectureGoal.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 주차 정보 생성 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusWeekInfoCreateInfo(String id, String lectureCode, int seperatedNum, int syllabusWeek, String syllabusSubject, String syllabusContent,String syllabusAssignment,String syllabusEvaluation){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.CREATE_SYLLABUS_WEEK_INFO.getCode());

        int lectureCodeLen = 10*3+1;
        int seperatedNumLen = 5+1;
        int syllabusWeekLen = 5+1;
        int syllabusSubjectLen = 20*3+1;
        int syllabusContentLen = 50*3+1;
        int syllabusAssignmentLen = 30*3+1;
        int syllabusEvaluationLen = 30*3+1;

        String seperatedNumStr = Integer.toString(seperatedNum);
        String syllabusWeekStr = Integer.toString(syllabusWeek);

        lectureCode += "\0";
        seperatedNumStr += "\0";
        syllabusWeekStr += "\0";
        syllabusSubject += "\0";
        syllabusContent += "\0";
        syllabusAssignment += "\0";
        syllabusEvaluation += "\0";

        byte[] data = new byte[lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen+syllabusAssignmentLen+syllabusEvaluationLen];

        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(),0,data,lectureCodeLen,seperatedNumStr.getBytes().length);
        System.arraycopy(syllabusWeekStr.getBytes(),0,data,lectureCodeLen+seperatedNumLen,syllabusWeekStr.getBytes().length);
        System.arraycopy(syllabusSubject.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen,syllabusSubject.getBytes().length);
        System.arraycopy(syllabusContent.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen,syllabusContent.getBytes().length);
        System.arraycopy(syllabusAssignment.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen,syllabusAssignment.getBytes().length);
        System.arraycopy(syllabusEvaluation.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen+syllabusAssignmentLen,syllabusEvaluation.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 주차 정보 수정 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusWeekInfoUpdateInfo(String lectureCode, int seperatedNum, int syllabusWeek, String inputData, int menuNum2) {
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.UPDATE_SYLLABUS_WEEK_INFO.getCode());

        int lectureCodeLen = 10*3+1;
        int seperatedNumLen = 5+1;
        int syllabusWeekLen = 5+1;
        int syllabusSubjectLen = 20*3+1;
        int syllabusContentLen = 50*3+1;
        int syllabusAssignmentLen = 30*3+1;
        int syllabusEvaluationLen = 30*3+1;

        String seperatedNumStr = Integer.toString(seperatedNum);
        String syllabusWeekStr = Integer.toString(syllabusWeek);

        String nullData = " \0";
        inputData += '\0';

        byte[] data = new byte[lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen+syllabusAssignmentLen+syllabusEvaluationLen];

        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(),0,data,lectureCodeLen,seperatedNumStr.getBytes().length);
        System.arraycopy(syllabusWeekStr.getBytes(),0,data,lectureCodeLen+seperatedNumLen,syllabusWeekStr.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen+syllabusAssignmentLen,nullData.getBytes().length);

        if(menuNum2 == 1){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 2){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 3){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 4){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+syllabusWeekLen+syllabusSubjectLen+syllabusContentLen+syllabusAssignmentLen,inputData.getBytes().length);
        }

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 주차 정보 삭제 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusWeekInfoDeleteInfo(String lectureCode, int seperatedNum, int syllabusWeek) {
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.DELETE_SYLLABUS_WEEK_INFO.getCode());

        int lectureCodeLen = 10*3+1;
        int seperatedNumLen = 5+1;
        int syllabusWeekLen = 5+1;

        String seperatedNumStr = Integer.toString(seperatedNum);
        String syllabusWeekStr = Integer.toString(syllabusWeek);

        lectureCode += "\0";
        seperatedNumStr += "\0";
        syllabusWeekStr += "\0";

        byte[] data = new byte[lectureCodeLen+seperatedNumLen+syllabusWeekLen];

        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(),0,data,lectureCodeLen,seperatedNumStr.getBytes().length);
        System.arraycopy(syllabusWeekStr.getBytes(),0,data,lectureCodeLen+seperatedNumLen,syllabusWeekStr.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 수정 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusUpdateInfo(String lectureCode,int seperatedNum,String inputData,int menuNum2){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.UPDATE_SYLLABUS.getCode());
        int lectureCodeLen = 10*3+1;
        int seperatedNumLen = 5+1;
        int bookNameLen = 30*3+1;
        int lectureGoalLen = 30*3+1;

        String seperatedNumStr = Integer.toString(seperatedNum);
        lectureCode += '\0';
        seperatedNumStr += '\0';
        String nullData = " \0";
        inputData += '\0';

        byte[] data = new byte[lectureCodeLen+seperatedNumLen+bookNameLen+lectureGoalLen];

        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(),0,data,lectureCodeLen,seperatedNumStr.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen,nullData.getBytes().length);
        System.arraycopy(nullData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+bookNameLen,nullData.getBytes().length);

        if(menuNum2 == 1){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen,inputData.getBytes().length);
        }
        else if(menuNum2 == 2){
            System.arraycopy(inputData.getBytes(),0,data,lectureCodeLen+seperatedNumLen+bookNameLen,inputData.getBytes().length);
        }

        protocol.setDataPacket(data);

        return protocol;
    }

    //개설 교과목 조회 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestOpenLectureListInfo(String id){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_LECTURE_LIST_REQ.getCode());
        int idLen = 20*3+1;
        id+= '\0';
        byte[] data = new byte[idLen];
        System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //클라이언트에게 보내줄 교수 개인 시간표 조회 정보 패킷에 저장 후 리턴
    private Protocol responseProfessorSchedule(byte[] data) {
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_PROFESSOR_SCHEDULE_RES.getCode());

        protocol.setDataPacket(data);

        return protocol;
    }

    //클라이언트에게 보내줄 교수 개설 교과목 조회 정보 패킷에 저장 후 리턴
    public Protocol responseOpenLectureListInfo(byte[] data){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_LECTURE_LIST_RES.getCode());

        protocol.setDataPacket(data);

        return protocol;
    }

    //클라이언트에게 보내줄 해당 개설 교과목의 수강 신청 학생 조회 정보 패킷에 저장 후 리턴
    public Protocol responseLectureEnrolledStudentListInfo(byte[] data){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_OPEN_LECTURE_ENROLLED_STUDENT_RES.getCode());

        protocol.setDataPacket(data);

        return protocol;
    }

    //클라이언트에게 보내줄 강의 계획서 (+주차 정보) 조회 정보 패킷에 저장 후 리턴
    public Protocol responseSyllabusListInfo(byte[] data){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_SYLLABUS_LIST_RES.getCode());

        protocol.setDataPacket(data);

        return protocol;
    }

    //해당 개설 교과목의 수강 신청 학생 정보 조회 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestLectureEnrolledStudentsListInfo(String lectureCode,int seperatedNum) {
        Protocol protocol = new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_OPEN_LECTURE_ENROLLED_STUDENT_REQ.getCode());
        int lectureCodeLen = 10 * 3 + 1;
        int seperatedNumLen = 5 + 1;

        String seperatedNumStr = Integer.toString(seperatedNum);
        lectureCode += '\0';
        seperatedNumStr += '\0';

        byte[] data = new byte[lectureCodeLen + seperatedNumLen];

        System.arraycopy(lectureCode.getBytes(), 0, data, 0, lectureCode.getBytes().length);
        System.arraycopy(seperatedNumStr.getBytes(), 0, data, lectureCodeLen, seperatedNumStr.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    //강의 계획서 조회 시 필요한 정보를 서버에 보낼 패킷에 저장 후 리턴
    public Protocol requestSyllabusListInfo(String lectureCode,int lectureSeperatedNumber){
        Protocol protocol=new Protocol(ProtocolType.PROFESSOR.getType());
        protocol.setProtocolCode(ProfessorCode.READ_SYLLABUS_LIST_REQ.getCode());
        int lectureCodeLen = 5+1;
        int lectureSeperatedLen = 5+1;
        lectureCode += '\0';
        String lectureSeperated=Integer.toString(lectureSeperatedNumber)+ '\0';
        byte[] data = new byte[lectureCodeLen+lectureSeperatedLen];
        System.arraycopy(lectureCode.getBytes(),0,data,0,lectureCode.getBytes().length);
        System.arraycopy(lectureSeperated.getBytes(),0,data,lectureCodeLen,lectureSeperated.getBytes().length);
        protocol.setDataPacket(data);
        return protocol;
    }

    //서버에서 실행한 개설 교과목 조회 명령의 정보를 byte 배열로 치환.
    public byte[] createProtocolPacketDataByOpenLectureList(List<OpenLectureDTO> openLectureByCondition){
        int listenLevelLength=5+1;
        int lectureNameLength=20*3+1;
        int lectureCodeLength=20*3+1;
        int lectureSeperatdeNumberLength=5+1;
        int lectureProfessorLength=20*3+1;
        int lectureTimeAndRoomlength=20*3+20*3+1;
        int maxStudentNumberLength=5+1;
        int curStudentNumberLength=5+1;

        int countLine=0;
        for(OpenLectureDTO openLectureDTO:openLectureByCondition){
            countLine++;
        }
        String listenLevel;
        String lectureName;
        String lectureCode;
        String lectureSeperatdeNumber;
        String lectureProfessor;
        String maxStudentNumber;
        String curStudentNumber;
        String lectureTimeAndRoom;

        int oneLineLen=listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength+curStudentNumberLength;
        byte[] data = new byte[oneLineLen*countLine];
        int cont=0;
        for(OpenLectureDTO openLectureDTO:openLectureByCondition){
            listenLevel= Integer.toString(openLectureDTO.getLectureDTO().getLectureLevel())+'\0';
            lectureName=openLectureDTO.getLectureDTO().getLectureName()+'\0';
            lectureCode=openLectureDTO.getLectureDTO().getLectureCode()+'\0';
            lectureSeperatdeNumber= Integer.toString(openLectureDTO.getSeperatedNumber())+'\0';
            lectureProfessor=openLectureDTO.getProfessorDTO().getProfessorName()+'\0';
            lectureTimeAndRoom=" ";
            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByService.selectAllLectureRoomByTime(openLectureDTO.getOpenLectureId());
            for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS) {
                LectureRoomDTO lectureRoomDTO = lectureRoomService.selectLectureRoomDTO(lectureRoomByTimeDTO.getLectureRoomId());
                LectureTimeDTO lectureTimeDTO = lectureTimeService.selectLectureTimeId(lectureRoomByTimeDTO.getLectureTimeId());
                String lectureroom=lectureRoomDTO.getBuildingName()+lectureRoomDTO.getLectureRoomNumber();
                String lecturetime=lectureTimeDTO.getLectureDay()+lectureTimeDTO.getLecturePeriod();

                lectureTimeAndRoom+=lectureroom+"/"+lecturetime+",";
            }

            maxStudentNumber= Integer.toString(openLectureDTO.getMaxStudentNumber())+'\0';
            curStudentNumber= Integer.toString(openLectureDTO.getCurStudentNumber())+'\0';
            System.arraycopy(listenLevel.getBytes(),0,data,(oneLineLen*cont),listenLevel.getBytes().length);
            System.arraycopy(lectureName.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength,lectureName.getBytes().length);
            System.arraycopy(lectureCode.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength,lectureCode.getBytes().length);
            System.arraycopy(lectureSeperatdeNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength,lectureSeperatdeNumber.getBytes().length);
            System.arraycopy(lectureProfessor.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength,lectureProfessor.getBytes().length);
            System.arraycopy(lectureTimeAndRoom.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength,lectureTimeAndRoom.getBytes().length);
            System.arraycopy(maxStudentNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength,maxStudentNumber.getBytes().length);
            System.arraycopy(curStudentNumber.getBytes(),0,data,(oneLineLen*cont)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength,curStudentNumber.getBytes().length);

            cont=cont+1;
        }

        return data;
    }

    //서버에서 실행한 개설 교과목의 수강 신청 학생 조회 명령의 정보를 byte 배열로 치환.
    public byte[] createProtocolPacketDataByStudentList(List<StudentDTO> list){
        int idLen = 20*3+1;;
        int nameLen = 20*3+1;
        int levelLen = 5+1;
        int departmentNameLen = 15*3+1;

        int countLine=0;
        for(StudentDTO studentDTO:list){
            countLine++;
        }

        String id;
        String name;
        String level;
        String departmentName;

        int oneLineLen=idLen+nameLen+levelLen+departmentNameLen;
        byte[] data = new byte[oneLineLen*countLine];
        int cnt=0;

        for(StudentDTO studentDTO:list){
            id=studentDTO.getStudentId()+'\0';
            name=studentDTO.getStudentName()+'\0';
            level= Integer.toString(studentDTO.getStudentLevel())+'\0';
            departmentName=studentDTO.getDepartmentDTO().getDepartmentName()+'\0';

            System.arraycopy(id.getBytes(),0,data,(oneLineLen*cnt),id.getBytes().length);
            System.arraycopy(name.getBytes(),0,data,(oneLineLen*cnt)+idLen,name.getBytes().length);
            System.arraycopy(level.getBytes(),0,data,(oneLineLen*cnt)+idLen+nameLen,level.getBytes().length);
            System.arraycopy(departmentName.getBytes(),0,data,(oneLineLen*cnt)+idLen+nameLen+levelLen,departmentName.getBytes().length);

            cnt++;
        }
        return data;
    }

    //서버에서 실행한 강의 계획서 (+주차 정보) 조회 명령의 정보를 byte 배열로 치환.
    public byte[] createProtocolPacketDataBySyllabusList(OpenLectureDTO openLectureDTO,SyllabusDTO syllabusCondition,List<SyllabusWeekInfoDTO> SyllabusWeekInfoCondition){
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
        for(SyllabusWeekInfoDTO syllabusWeekInfoDTO:SyllabusWeekInfoCondition){
            SyllabusWeekInfoOneLineLengthcount++;
        }
        byte[] data = new byte[syllabuslength+SyllabusWeekInfoOneLineLengthcount*SyllabusWeekInfoOneLineLength];

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
        for(SyllabusWeekInfoDTO syllabusWeekInfoDTO:SyllabusWeekInfoCondition){
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

    //로그아웃 시 보낼 프로토콜 생성
    public Protocol logoutReqProtocol(){
        Protocol protocol = new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.LOGOUT_REQ.getCode());
        return protocol;
    }

}
