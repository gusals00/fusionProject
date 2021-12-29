package controller;

import network.*;

import persistence.dto.*;
import service.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdminController {
    ProfessorService professorService = null;
    DepartmentService departmentService = null;
    StudentService studentService = null;
    LectureService lectureService = null;
    OpenLectureService openLectureService = null;
    PeriodService periodService=null;
    LectureRoomByService lectureRoomByService=null;
    LectureRoomService lectureRoomService=null;
    LectureTimeService lectureTimeService=null;
    public AdminController(ProfessorService professorService, DepartmentService departmentService, StudentService studentService, LectureService lectureService, OpenLectureService openLectureService,PeriodService periodService,LectureRoomByService lectureRoomByService,LectureRoomService lectureRoomService,LectureTimeService lectureTimeService){
        this.professorService = professorService;
        this.departmentService = departmentService;
        this.studentService = studentService;
        this.lectureService = lectureService;
        this.openLectureService = openLectureService;
        this.periodService=periodService;
        this.lectureRoomByService=lectureRoomByService;
        this.lectureRoomService=lectureRoomService;
        this.lectureTimeService=lectureTimeService;

    }

    public AdminController(){
    }

    public Protocol setSendProtocol(Protocol receiveProtocol){
        Protocol sendProtocol=null;
        try {

            if (receiveProtocol.getProtocolCode() == AdminCode.CREATE_PROFESSOR.getCode()) { // 교수 생성
                String professorId = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 61).split("\0")[0];
                String professorPw = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61, 61).split("\0")[0];
                String professorName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61, 61).split("\0")[0];
                String ssn = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61, 15).split("\0")[0];
                String eMail = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61 + 15, 61).split("\0")[0];
                String professorPhoneNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61 + 15 + 61, 16).split("\0")[0];
                String departmentName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61 + 15 + 61 + 16, 16).split("\0")[0];

                if (insertProfessor(professorId, professorPw, professorName, ssn, eMail, professorPhoneNumber, departmentName)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.CREATE_STUDENT.getCode()) { // 학생 생성
                String studentId = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 61).split("\0")[0];
                String studentPw = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61, 61).split("\0")[0];
                String studentName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61, 61).split("\0")[0];
                String ssn = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61, 15).split("\0")[0];
                String level = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61 + 15, 5).split("\0")[0];
                String departmentName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 61 + 61 + 61 + 15 + 5, 16).split("\0")[0];

                if (insertStudent(studentId, studentPw, studentName, ssn, level, departmentName)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.CREATE_LECTURE.getCode()) { // 교과목 생성
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 11).split("\0")[0];
                String lectureName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11, 61).split("\0")[0];
                String lectureLevel = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61, 5).split("\0")[0];
                String lectureCredit = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61 + 5, 5).split("\0")[0];

                if (insertLecture(lectureCode, lectureName, lectureLevel, lectureCredit)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.READ_LECTURE.getCode()) { // 교과목 조회 결과 보내줌
                List<LectureDTO> lectureDTOS = lectureService.readAllLectures();
                int lectureLen = 5;
                int lectureCodeLen = 11;
                int lectureNameLen = 61;
                int lectureLevelLen = 5;
                int lectureCreditLen = 5;
                int allLen = 82;
                String str = Integer.toString(lectureDTOS.size());
                str += '\0';

                byte[] data = new byte[lectureLen + (lectureCodeLen + lectureNameLen + lectureLevelLen + lectureCreditLen) * lectureDTOS.size()];
                System.arraycopy(str.getBytes(), 0, data, 0, str.getBytes().length);
                int i = 0;
                for (LectureDTO lectureDTO : lectureDTOS) {
                    String lectureCode = lectureDTO.getLectureCode() + '\0';
                    String lectureName = lectureDTO.getLectureName() + '\0';
                    String lectureLevel = Integer.toString(lectureDTO.getLectureLevel()) + '\0';
                    String lectureCredit = Integer.toString(lectureDTO.getLectureCredit()) + '\0';
                    System.arraycopy(lectureCode.getBytes(), 0, data, lectureLen + (allLen * i), lectureCode.getBytes().length);
                    System.arraycopy(lectureName.getBytes(), 0, data, lectureLen + lectureCodeLen + (allLen * i), lectureName.getBytes().length);
                    System.arraycopy(lectureLevel.getBytes(), 0, data, lectureLen + lectureCodeLen + lectureNameLen + (allLen * i), lectureLevel.getBytes().length);
                    System.arraycopy(lectureCredit.getBytes(), 0, data, lectureLen + lectureCodeLen + lectureNameLen + lectureLevelLen + (allLen * i), lectureCredit.getBytes().length);
                    i++;
                }

                Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
                protocol.setProtocolCode(AdminCode.READ_LECTURE.getCode());
                protocol.setDataPacket(data);
                sendProtocol = protocol;
            } else if (receiveProtocol.getProtocolCode() == AdminCode.UPDATE_LECTURE.getCode()) { // 교과목 변경
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 11).split("\0")[0];
                String lectureName;
                String lectureLevel;
                String lectureCredit;
                if (new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11, 61).split("\0")[0].equals(" ")) {
                    lectureName = null;
                } else {
                    lectureName = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11, 61).split("\0")[0];
                }

                if (new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61, 5).split("\0")[0].equals(" ")) {
                    lectureLevel = "0";
                } else {
                    lectureLevel = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61, 5).split("\0")[0];
                }

                if (new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61 + 5, 5).split("\0")[0].equals(" ")) {
                    lectureCredit = "0";
                } else {
                    lectureCredit = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 11 + 61 + 5, 5).split("\0")[0];
                }

                if (updateLecture(lectureCode, lectureName, Integer.parseInt(lectureLevel), Integer.parseInt(lectureCredit))) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.DELETE_LECTURE.getCode()) { // 교과목 삭제
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 11).split("\0")[0];
                System.out.println(lectureCode);
                if (deleteLecture(lectureCode)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.CREATE_OPEN_LECTURE.getCode()) { // 개설교과목 생성
                String lectureRoomLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 5).split("\0")[0];
                String lectureTimeLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5, 5).split("\0")[0];
                String timeCntLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5, 5).split("\0")[0];
                String seperatedNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5, 5).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5, 11).split("\0")[0];
                String professorId = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11, 61).split("\0")[0];
                String maxStudentNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61, 5).split("\0")[0];
                String curStudentNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5, 5).split("\0")[0];
                List<LectureRoomDTO> lectureRoomDTOS = new ArrayList<LectureRoomDTO>();
                List<LectureTimeDTO> lectureTimeDTOS = new ArrayList<LectureTimeDTO>();
                List<Integer> timeCnt = new ArrayList<Integer>();

                for (int i = 0; i < Integer.parseInt(lectureRoomLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + (i * 16), 16).split("\0")[0];
                    lectureRoomDTOS.add(new LectureRoomDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(lectureTimeLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + (i * 16), 16).split("\0")[0];
                    lectureTimeDTOS.add(new LectureTimeDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(timeCntLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + 16 * Integer.parseInt(lectureTimeLength) + (i * 6), 6).split("\0")[0];
                    timeCnt.add(Integer.parseInt(str));
                }

                if (createOpenLecture(seperatedNumber, lectureCode, professorId, maxStudentNumber, curStudentNumber, lectureRoomDTOS, lectureTimeDTOS, timeCnt)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.UPDATE_OPEN_LECTURE.getCode()) { // 개설교과목 변경
                String lectureRoomLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 5).split("\0")[0];
                String lectureTimeLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5, 5).split("\0")[0];
                String timeCntLength = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5, 5).split("\0")[0];
                String seperatedNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5, 5).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5, 11).split("\0")[0];
                String professorId = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11, 61).split("\0")[0];
                String maxStudentNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61, 5).split("\0")[0];
                String curStudentNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5, 5).split("\0")[0];
                List<LectureRoomDTO> lectureRoomDTOS = new ArrayList<LectureRoomDTO>();
                List<LectureTimeDTO> lectureTimeDTOS = new ArrayList<LectureTimeDTO>();
                List<LectureRoomDTO> changeLectureRoomDTOS = new ArrayList<LectureRoomDTO>();
                List<LectureTimeDTO> changeLectureTimeDTOS = new ArrayList<LectureTimeDTO>();
                List<Integer> timeCnt = new ArrayList<Integer>();

                for (int i = 0; i < Integer.parseInt(lectureRoomLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + (i * 16), 16).split("\0")[0];
                    lectureRoomDTOS.add(new LectureRoomDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(lectureTimeLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + (i * 16), 16).split("\0")[0];
                    lectureTimeDTOS.add(new LectureTimeDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(lectureRoomLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + 16 * Integer.parseInt(lectureTimeLength) + (i * 16), 16).split("\0")[0];
                    changeLectureRoomDTOS.add(new LectureRoomDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(lectureTimeLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + 16 * Integer.parseInt(lectureTimeLength) + 16 * Integer.parseInt(lectureRoomLength), 16).split("\0")[0];
                    changeLectureTimeDTOS.add(new LectureTimeDTO(0, str.substring(0, 1), Integer.parseInt(str.substring(1))));
                }
                for (int i = 0; i < Integer.parseInt(timeCntLength); i++) {
                    String str = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 5 + 5 + 5 + 11 + 61 + 5 + 5 + 16 * Integer.parseInt(lectureRoomLength) + 16 * Integer.parseInt(lectureTimeLength) + 16 * Integer.parseInt(lectureRoomLength) + 16 * Integer.parseInt(lectureTimeLength) + (i * 6), 6).split("\0")[0];
                    timeCnt.add(Integer.parseInt(str));
                }

                if (professorId.equals("-1")) {
                    professorId = null;
                }
                OpenLectureDTO openLectureDTO = new OpenLectureDTO(0, Integer.parseInt(seperatedNumber), lectureCode, professorId, Integer.parseInt(maxStudentNumber), Integer.parseInt(curStudentNumber), null, null);

                if (updateOpenLecture(openLectureDTO, lectureRoomDTOS, lectureTimeDTOS, timeCnt, changeLectureRoomDTOS, changeLectureTimeDTOS)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }

            } else if (receiveProtocol.getProtocolCode() == AdminCode.DELETE_OPEN_LECTURE.getCode()) { // 개설교과목 삭제
                String seperatedNumber = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 5).split("\0")[0];
                String lectureCode = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5, 11).split("\0")[0];

                if (deleteOpenLecture(seperatedNumber, lectureCode)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.UPDATE_SYLLABUS_PERIOD.getCode()) { // 기간 설정
                String id = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER, 5).split("\0")[0];
                String startTime = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5, 31).split("\0")[0];
                String endTime = new String(receiveProtocol.getPacket(), Protocol.LEN_HEADER + 5 + 31, 31).split("\0")[0];

                String starts[] = startTime.split(" ");
                String ends[] = endTime.split(" ");

                String startsDate[] = starts[0].split("-");
                String startsTime[] = starts[1].split(":");
                String endsDate[] = ends[0].split("-");
                String endsTime[] = ends[1].split(":");

                LocalDateTime start = LocalDateTime.of(Integer.parseInt(startsDate[0]), Integer.parseInt(startsDate[1]), Integer.parseInt(startsDate[2]), Integer.parseInt(startsTime[0]), Integer.parseInt(startsTime[1]), Integer.parseInt(startsTime[2]));
                LocalDateTime end = LocalDateTime.of(Integer.parseInt(endsDate[0]), Integer.parseInt(endsDate[1]), Integer.parseInt(endsDate[2]), Integer.parseInt(endsTime[0]), Integer.parseInt(endsTime[1]), Integer.parseInt(endsTime[2]));

                int intId = Integer.parseInt(id);
                if (updatePeriod(intId, start, end)) {
                    sendProtocol = successProtocol();
                } else {
                    sendProtocol = failProtocol();
                }
            } else if (receiveProtocol.getProtocolCode() == AdminCode.READ_PROFESSOR_INFO.getCode()) {// 교수 정보 조회 결과 보내줌
                sendProtocol = sendProfessorInfoRes(receiveProtocol);
            } else if (receiveProtocol.getProtocolCode() == AdminCode.READ_STUDENT_INFO.getCode()) {//학생 정보 조회 결과 보내줌
                sendProtocol = sendStudentInfoRes(receiveProtocol);
            } else if (receiveProtocol.getProtocolCode() == AdminCode.READ_OPEN_LECTURE_INFO.getCode()) {// 개설 교과목 조회 결과 보내줌
                sendProtocol = sendOpenLectureInfoRes(receiveProtocol);
            }
        }catch(Exception e){
            sendProtocol= failProtocol();
        }

        return sendProtocol;
    }

    public Protocol sendProfessorInfoRes(Protocol receiveProtocol){ // 교수 조회 정보 응답
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.PROFESSOR_INFO_RES.getCode());

        int idLen=20*3+1;
        int nameLen=20*3+1;
        int ssnLen=14*3+1;
        int eMailLen=30*3+1;
        int departmentLen=15*3+1;
        int listLen=idLen+nameLen+ssnLen+eMailLen+departmentLen;
        int listCnt=0;

        List<byte[]> dataArr = new ArrayList<byte[]>();

        if(receiveProtocol.getLength()==0){
            System.out.println("전체 교수 조회");
            List<ProfessorDTO> professorDTOS = professorService.readAllProfessors();


            for(ProfessorDTO professorDTO:professorDTOS){
                byte[] data=new byte[listLen];
                String id=professorDTO.getProfessorId()+"\0";
                String name=professorDTO.getProfessorName()+"\0";
                String ssn=professorDTO.getSsn()+ "\0";
                String eMail=professorDTO.getEMail()+"\0";
                String department=professorDTO.getDepartmentDTO().getDepartmentName()+"\0";

                System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
                System.arraycopy(name.getBytes(),0,data,idLen,name.getBytes().length);
                System.arraycopy(ssn.getBytes(),0,data,idLen+nameLen,ssn.getBytes().length);
                System.arraycopy(eMail.getBytes(),0,data,idLen+nameLen+ssnLen,eMail.getBytes().length);
                System.arraycopy(department.getBytes(),0,data,idLen+nameLen+ssnLen+eMailLen,department.getBytes().length);

                dataArr.add(data);
            }
        }
        else{
            System.out.println("특정 교수 조회");
            byte[] packet = receiveProtocol.getPacket();
            String findId = new String(packet,Protocol.LEN_HEADER,receiveProtocol.getLength()).split("\0")[0];
            ProfessorDTO professorDTOWithId=new ProfessorDTO();
            professorDTOWithId.setProfessorId(findId);
            ProfessorDTO professorDTO=new ProfessorDTO();

            professorDTO=professorService.findProfessorById(professorDTOWithId);//db에서 해당 id 가지는 교수 dto 리턴

            if (professorDTO.getProfessorId()==null){//해당하는 id를 가지는 교수 정보가 없을 경우
                return protocol;//data에 아무 정보도 넣지 않음.
            }

            byte[] data=new byte[listLen];
            String id=professorDTO.getProfessorId()+"\0";
            String name=professorDTO.getProfessorName()+"\0";
            String ssn=professorDTO.getSsn()+ "\0";
            String eMail=professorDTO.getEMail()+"\0";
            String department=professorDTO.getDepartmentDTO().getDepartmentName()+"\0";

            System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
            System.arraycopy(name.getBytes(),0,data,idLen,name.getBytes().length);
            System.arraycopy(ssn.getBytes(),0,data,idLen+nameLen,ssn.getBytes().length);
            System.arraycopy(eMail.getBytes(),0,data,idLen+nameLen+ssnLen,eMail.getBytes().length);
            System.arraycopy(department.getBytes(),0,data,idLen+nameLen+ssnLen+eMailLen,department.getBytes().length);

            dataArr.add(data);
        }

        listCnt=dataArr.size();

        byte[] totalData=new byte[listCnt*listLen];
        for(int i=0;i<listCnt;i++){
            System.arraycopy(dataArr.get(i),0,totalData,i*listLen,listLen);

        }

        protocol.setDataPacket(totalData);
        protocol.setListLength(listCnt);

        return protocol;
    }

    public Protocol sendStudentInfoRes(Protocol receiveProtocol){ // 학생 조회 정보 응답
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.STUDENT_INFO_RES.getCode());

        int idLen=20*3+1;
        int nameLen=20*3+1;
        int ssnLen=14*3+1;
        int levelLen=4+1;
        int departmentLen=15*3+1;
        int listLen=idLen+nameLen+ssnLen+levelLen+departmentLen;
        int listCnt=0;

        List<byte[]> dataArr = new ArrayList<byte[]>();

        if(receiveProtocol.getLength()==0){
            System.out.println("전체 학생 조회");
            List<StudentDTO> studentDTOS= studentService.readAllStudents();

            for(StudentDTO studentDTO:studentDTOS){
                byte[] data=new byte[listLen];
                String id=studentDTO.getStudentId()+"\0";
                String name=studentDTO.getStudentName()+"\0";
                String ssn=studentDTO.getSsn()+ "\0";
                String level=studentDTO.getStudentLevel()+"\0";
                String department=studentDTO.getDepartmentDTO().getDepartmentName()+"\0";

                System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
                System.arraycopy(name.getBytes(),0,data,idLen,name.getBytes().length);
                System.arraycopy(ssn.getBytes(),0,data,idLen+nameLen,ssn.getBytes().length);
                System.arraycopy(level.getBytes(),0,data,idLen+nameLen+ssnLen,level.getBytes().length);
                System.arraycopy(department.getBytes(),0,data,idLen+nameLen+ssnLen+levelLen,department.getBytes().length);

                dataArr.add(data);
            }
        }
        else{
            System.out.println("특정 학생 조회");
            byte[] packet = receiveProtocol.getPacket();
            String findId = new String(packet,Protocol.LEN_HEADER,receiveProtocol.getLength()).split("\0")[0];

            StudentDTO studentDTO=new StudentDTO();
            studentDTO=studentService.findStudentByStudentId(findId);//db에서 해당 id 가지는 학생 dto 리턴

            if (studentDTO.getStudentId()==null){//해당하는 id를 가지는 교수 정보가 없을 경우
                return protocol;//data에 아무 정보도 넣지 않음.
            }

            byte[] data=new byte[listLen];
            String id=studentDTO.getStudentId()+"\0";
            String name=studentDTO.getStudentName()+"\0";
            String ssn=studentDTO.getSsn()+ "\0";
            String level=studentDTO.getStudentLevel()+"\0";
            String department=studentDTO.getDepartmentDTO().getDepartmentName()+"\0";

            System.arraycopy(id.getBytes(),0,data,0,id.getBytes().length);
            System.arraycopy(name.getBytes(),0,data,idLen,name.getBytes().length);
            System.arraycopy(ssn.getBytes(),0,data,idLen+nameLen,ssn.getBytes().length);
            System.arraycopy(level.getBytes(),0,data,idLen+nameLen+ssnLen,level.getBytes().length);
            System.arraycopy(department.getBytes(),0,data,idLen+nameLen+ssnLen+levelLen,department.getBytes().length);

            dataArr.add(data);
        }

        listCnt=dataArr.size();

        byte[] totalData=new byte[listCnt*listLen];
        for(int i=0;i<listCnt;i++){
            System.arraycopy(dataArr.get(i),0,totalData,i*listLen,listLen);

        }

        protocol.setDataPacket(totalData);
        protocol.setListLength(listCnt);

        return protocol;
    }

    public Protocol sendOpenLectureInfoRes(Protocol receiveProtocol){//서버가 클라이언트에게 개설교과목 조회 결고 넘겨줌.
        int nameLen = 20*3+1;
        int levelLen=2+1;
        int codeLen=10+1;
        int seperateLen=2+1;
        int professorLen=20*3+1;
        int maxLen=3+1;
        int curLen=3+1;
        int roomAndTimeLen=60+1;
        int listLen = nameLen+levelLen+seperateLen+codeLen+professorLen+maxLen+curLen+roomAndTimeLen;
        int totalLen=0;
        List<byte[]> list=new ArrayList<byte[]>();

        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.OPEN_LECTURE_INFO_RES.getCode());

        //receiveprotocl 해석해서 db에서 찾아야 함.
        List<OpenLectureDTO> openLectureDTOS=null;


        LectureDTO lectureDTO=new LectureDTO();
        ProfessorDTO professorDTO=new ProfessorDTO();
        if(receiveProtocol.getLength()==0){//전체 교과목 조회
            openLectureDTOS= openLectureService.readAllOpenLecture();
            System.out.println("전체 교과목 조회 결과를 db에서 탐색합니다.");
        }
        else{//조건 가짐
            byte[] packet=receiveProtocol.getPacket();
            String level=new String(packet,Protocol.LEN_HEADER,5);
            String professorName = new String(packet,Protocol.LEN_HEADER+5,61);
            if(level.charAt(0)!='\0'){//level이 있는 경우
                int level_=Integer.parseInt(level.split("\0")[0]);
                lectureDTO.setLectureLevel(level_);
            }
            if(professorName.charAt(0)!='\0'){//교수 이름이 있는 경우
                professorDTO = professorService.findByName(professorName.split("\0")[0]);
                if(professorDTO.getProfessorId()==null){//올바른 교수가 아닌 경우.
                    professorDTO.setProfessorId("-1");
                }
            }

            openLectureDTOS= openLectureService.readOpenLectureByCondition(lectureDTO,professorDTO);
        }


        if(openLectureDTOS==null){//db 결과 아무것도 없는 경우
            return protocol;
        }

        for(OpenLectureDTO openLectureDTO:openLectureDTOS){
            byte[] data = new byte[listLen];

            String lectureName = openLectureDTO.getLectureDTO().getLectureName()+"\0";
            String level=String.valueOf(openLectureDTO.getLectureDTO().getLectureLevel())+"\0";
            String lectureCode = openLectureDTO.getLectureCode()+"\0";
            String seperatedNumber=String.valueOf(openLectureDTO.getSeperatedNumber())+"\0";
            String professor = openLectureDTO.getProfessorDTO().getProfessorName()+"\0";
            String maxNum = String.valueOf(openLectureDTO.getMaxStudentNumber())+"\0";
            String curNum = String.valueOf(openLectureDTO.getCurStudentNumber())+"\0";

            String roomAndTime=" ";
            List<LectureRoomByTimeDTO> lectureRoomByTimeDTOS = lectureRoomByService.selectAllLectureRoomByTime(openLectureDTO.getOpenLectureId());
            for(LectureRoomByTimeDTO lectureRoomByTimeDTO : lectureRoomByTimeDTOS){
                LectureRoomDTO lectureRoomDTO1 = lectureRoomService.selectLectureRoomDTO(lectureRoomByTimeDTO.getLectureRoomId());
                roomAndTime+=lectureRoomDTO1.getBuildingName();
                roomAndTime+=lectureRoomDTO1.getLectureRoomNumber();
                roomAndTime+="/";
                LectureTimeDTO lectureTimeDTO1 = lectureTimeService.selectLectureTimeId(lectureRoomByTimeDTO.getLectureTimeId());

                roomAndTime+=lectureTimeDTO1.getLectureDay();
                roomAndTime+=lectureTimeDTO1.getLecturePeriod();
                roomAndTime+=" ";

            }
            roomAndTime+="\0";
            System.arraycopy(lectureName.getBytes(),0,data,0,lectureName.getBytes().length);
            System.arraycopy(level.getBytes(),0,data,nameLen,level.getBytes().length);
            System.arraycopy(lectureCode.getBytes(),0,data,nameLen+levelLen,lectureCode.getBytes().length);
            System.arraycopy(seperatedNumber.getBytes(),0,data,nameLen+levelLen+codeLen,seperatedNumber.getBytes().length);
            System.arraycopy(professor.getBytes(),0,data,nameLen+levelLen+codeLen+seperateLen,professor.getBytes().length);
            System.arraycopy(professor.getBytes(),0,data,nameLen+levelLen+codeLen+seperateLen,professor.getBytes().length);
            System.arraycopy(maxNum.getBytes(),0,data,nameLen+levelLen+codeLen+seperateLen+professorLen,maxNum.getBytes().length);
            System.arraycopy(curNum.getBytes(),0,data,nameLen+levelLen+codeLen+seperateLen+professorLen+maxLen,curNum.getBytes().length);
            System.arraycopy(roomAndTime.getBytes(),0,data,nameLen+levelLen+codeLen+seperateLen+professorLen+maxLen+curLen,roomAndTime.getBytes().length);

            list.add(data);

        }
        totalLen=list.size()*listLen;
        byte[] dataPacket = new byte[totalLen];
        for(int i=0;i<list.size();i++){
            System.arraycopy(list.get(i),0,dataPacket,i*listLen,listLen);
        }

        protocol.setDataPacket(dataPacket);
        protocol.setListLength(list.size());

        return protocol;
    }





    public boolean insertProfessor(String professorId, String professorPw, String professorName, String ssn, String eMail, String professorPhoneNumber, String departmentName){ // 교수 생성
        int departmentNumber = departmentService.selectDepartmentNumber(departmentName);

        ProfessorDTO professorDTO= new ProfessorDTO(professorId, professorPw, professorName, ssn, eMail, professorPhoneNumber, departmentNumber, null);
        int result = professorService.insertProfessor(professorDTO);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insertStudent(String studentId, String studentPw, String studentName, String ssn, String level, String departmentName){ // 학생 생성
        int departmentNumber = departmentService.selectDepartmentNumber(departmentName);

        StudentDTO studentDTO = new StudentDTO(studentId, studentPw, studentName, ssn, Integer.parseInt(level), departmentNumber, null);
        int result = studentService.insertStudent(studentDTO);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }

    }

    public boolean insertLecture(String lectureCode, String lectureName, String lectureLevel, String lectureCredit){ // 교과목 생성
        LectureDTO lectureDTO = new LectureDTO(lectureCode, lectureName, Integer.parseInt(lectureLevel), Integer.parseInt(lectureCredit));
        int result = lectureService.insertLecture(lectureDTO);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteLecture(String lectureCode){ // 교과목 삭제
        int result = lectureService.deleteLecture(lectureCode);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateLecture(String lectureCode, String lectureName, int lectureLevel, int lectureCredit){ // 교과목 변경
        LectureDTO lectureDTO = new LectureDTO(lectureCode, lectureName, lectureLevel, lectureCredit);
        int result = lectureService.updateLectureByCondition(lectureDTO);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean createOpenLecture(String seperatedNumber, String lectureCode, String professorId, String maxStudentNumber, String curStudentNumber, List<LectureRoomDTO> lectureRoomDTOS, List<LectureTimeDTO> lectureTimeDTOS, List<Integer> timeCnt){ // 개설교과목 생성
        OpenLectureDTO openLectureDTO = new OpenLectureDTO(0, Integer.parseInt(seperatedNumber), lectureCode, professorId, Integer.parseInt(maxStudentNumber), Integer.parseInt(curStudentNumber), null, null);
        int result = openLectureService.insertManyOpenLecture(openLectureDTO, lectureService, lectureTimeDTOS, lectureRoomDTOS, timeCnt);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updateOpenLecture(OpenLectureDTO openLectureDTO, List<LectureRoomDTO> lectureRoomDTOS, List<LectureTimeDTO> lectureTimeDTOS, List<Integer> timeCnt, List<LectureRoomDTO> changeLectureRoomDTOS, List<LectureTimeDTO> changeLectureTimeDTOS){ // 개설교과목 변경
        int result = openLectureService.updateManyOpenLecture(openLectureDTO, lectureTimeDTOS, lectureRoomDTOS, changeLectureTimeDTOS, changeLectureRoomDTOS, timeCnt);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteOpenLecture(String seperatedNumber, String lectureCode){ // 개설교과목 삭제
        int result = openLectureService.deleteOpenLecture(Integer.parseInt(seperatedNumber), lectureCode);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean updatePeriod(int id, LocalDateTime start, LocalDateTime end){ // 기간 변경
        PeriodDTO periodDTO = new PeriodDTO(id, null, start, end);
        int result = periodService.updateAllTime(periodDTO);
        if(result == 0){
            return false;
        }
        else{
            return true;
        }
    }
    public Protocol successProtocol(){ // 성공했을때 프로토콜
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.SUCCESS.getCode());
        return protocol;
    }

    public Protocol failProtocol(){ // 실패했을때 프로토콜
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.FAIL.getCode());
        return protocol;
    }

    public Protocol logoutReqProtocol(){ // 로그아웃 요청 프로토콜
        Protocol protocol = new Protocol(ProtocolType.LOGIN_AND_LOGOUT.getType());
        protocol.setProtocolCode(LoginAndLogoutCode.LOGOUT_REQ.getCode());
        return protocol;
    }

    public Protocol lectureReadReqProtocol(){ // 교과목 조회 요청 프로토콜
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.READ_LECTURE.getCode());
        return protocol;
    }

    public Protocol requestProfessorCreate(String professorId, String professorPw, String professorName, String ssn, String eMail, String professorPhoneNumber, String departmentName){ // 교수 생성 요청
        
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.CREATE_PROFESSOR.getCode());
        int idLen = 61;
        int pwLen = 61;
        int nameLen = 61;
        int ssnLen = 15;
        int eMailLen = 61;
        int phoneNumberLen = 16;
        int departmentNameLen = 16;

        professorId += '\0'; professorPw += '\0'; professorName += '\0'; ssn += '\0'; eMail += '\0'; professorPhoneNumber += '\0'; departmentName += '\0';

        byte[] data = new byte[idLen + pwLen + nameLen + ssnLen + eMailLen + phoneNumberLen + departmentNameLen];
        System.arraycopy(professorId.getBytes(),0,data,0,professorId.getBytes().length);
        System.arraycopy(professorPw.getBytes(), 0, data, idLen, professorPw.getBytes().length);
        System.arraycopy(professorName.getBytes(), 0, data, idLen+pwLen, professorName.getBytes().length);
        System.arraycopy(ssn.getBytes(), 0, data, idLen+pwLen+nameLen, ssn.getBytes().length);
        System.arraycopy(eMail.getBytes(), 0, data, idLen+pwLen+nameLen+ssnLen, eMail.getBytes().length);
        System.arraycopy(professorPhoneNumber.getBytes(), 0, data, idLen+pwLen+nameLen+ssnLen+eMailLen, professorPhoneNumber.getBytes().length);
        System.arraycopy(departmentName.getBytes(), 0, data, idLen+pwLen+nameLen+ssnLen+eMailLen+phoneNumberLen, departmentName.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestStudentCreate(String studentId, String studentPw, String studentName, String ssn, String level, String departmentName) { // 학생 생성 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.CREATE_STUDENT.getCode());
        int idLen = 61;
        int pwLen = 61;
        int nameLen = 61;
        int ssnLen = 15;
        int levelLen = 5;
        int departmentNumberLen = 16;

        studentId += '\0'; studentPw += '\0'; studentName += '\0'; ssn += '\0'; level += '\0'; departmentName += '\0';

        byte[] data = new byte[idLen + pwLen + nameLen + ssnLen + levelLen + departmentNumberLen];
        System.arraycopy(studentId.getBytes(), 0, data, 0, studentId.getBytes().length);
        System.arraycopy(studentPw.getBytes(), 0, data, idLen, studentPw.getBytes().length);
        System.arraycopy(studentName.getBytes(), 0, data, idLen + pwLen, studentName.getBytes().length);
        System.arraycopy(ssn.getBytes(), 0, data, idLen + pwLen + nameLen, ssn.getBytes().length);
        System.arraycopy(level.getBytes(), 0, data, idLen + pwLen + nameLen + ssnLen, level.getBytes().length);
        System.arraycopy(departmentName.getBytes(), 0, data, idLen + pwLen + nameLen + ssnLen + levelLen, departmentName.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestLectureCreate(String lectureCode, String lectureName, String lectureLevel, String lectureCredit){ // 교과목 생성 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.CREATE_LECTURE.getCode());
        int lectureCodeLen = 11;
        int lectureNameLen = 61;
        int lectureLevelLen = 5;
        int lectureCreditLen = 5;

        lectureCode += '\0'; lectureName += '\0'; lectureLevel += '\0'; lectureCredit += '\0';

        byte[] data = new byte[lectureCodeLen + lectureNameLen + lectureLevelLen + lectureCreditLen];
        System.arraycopy(lectureCode.getBytes(), 0, data, 0, lectureCode.getBytes().length);
        System.arraycopy(lectureName.getBytes(), 0, data, lectureCodeLen, lectureName.getBytes().length);
        System.arraycopy(lectureLevel.getBytes(), 0, data, lectureCodeLen+lectureNameLen, lectureLevel.getBytes().length);
        System.arraycopy(lectureCredit.getBytes(), 0, data, lectureCodeLen+lectureNameLen+lectureLevelLen, lectureCredit.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;

    }

    public Protocol requestLectureDelete(String lectureCode){ // 교과목 삭제 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.DELETE_LECTURE.getCode());
        int lectureCodeLen = 11;
        lectureCode += '\0';

        byte[] data = new byte[lectureCodeLen];
        System.arraycopy(lectureCode.getBytes(), 0, data, 0, lectureCode.getBytes().length);

        protocol.setDataPacket(data);
        return protocol;
    }

    public Protocol requestLectureUpdate(String lectureCode, String lectureName, String lectureLevel, String lectureCredit){ // 교과목 변경 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.UPDATE_LECTURE.getCode());
        int lectureCodeLen = 11;
        int lectureNameLen = 61;
        int lectureLevelLen = 5;
        int lectureCreditLen = 5;

        lectureCode += '\0'; lectureName += '\0'; lectureLevel += '\0'; lectureCredit += '\0';

        byte[] data = new byte[lectureCodeLen + lectureNameLen + lectureLevelLen + lectureCreditLen];
        System.arraycopy(lectureCode.getBytes(), 0, data, 0, lectureCode.getBytes().length);
        if(!lectureName.equals("0")) {
            System.arraycopy(lectureName.getBytes(), 0, data, lectureCodeLen, lectureName.getBytes().length);
        }
        else{
            System.arraycopy(" ".getBytes(), 0, data, lectureCodeLen, 1);
        }
        if(!lectureLevel.equals("0")) {
            System.arraycopy(lectureLevel.getBytes(), 0, data, lectureCodeLen + lectureNameLen, lectureLevel.getBytes().length);
        }
        else{
            System.arraycopy(" ".getBytes(), 0, data, lectureCodeLen+lectureNameLen, 1);
        }
        if(!lectureCredit.equals("0")) {
            System.arraycopy(lectureCredit.getBytes(), 0, data, lectureCodeLen + lectureNameLen + lectureLevelLen, lectureCredit.getBytes().length);
        }
        else{
            System.arraycopy(" ".getBytes(), 0, data, lectureCodeLen+lectureNameLen+lectureLevelLen, 1);
        }

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestOpenLectureCreate(String seperatedNumber, String lectureCode, String professorId, String maxStudentNumber, String curStudentNumber, List<String> lectureRoomDTOS, List<String> lectureTimeDTOS, List<String> timeCnt, String lectureRoomLength, String lectureTimeLength, String timeCntLength){ // 개설교과목 생성 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.CREATE_OPEN_LECTURE.getCode());
        int lectureRoomLengthLen = 5;
        int lectureTimeLengthLen = 5;
        int timeCntLengthLen = 5;
        int seperatedNumberLen = 5;
        int lectureCodeLen = 11;
        int professorIdLen = 61;
        int maxStdNumLen = 5;
        int curStdNumLen = 5;
        int lectureRoomLen = lectureRoomDTOS.size()*16;
        int lectureTimeLen = lectureTimeDTOS.size()*16;
        int timeCntLen = timeCnt.size()*6;

        lectureRoomLength += '\0'; lectureTimeLength += '\0'; lectureRoomLength += '\0'; seperatedNumber += '\0'; lectureCode += '\0'; professorId += '\0'; maxStudentNumber += '\0'; curStudentNumber += '\0';

        byte[] data = new byte[lectureRoomLengthLen + lectureTimeLengthLen + timeCntLengthLen + seperatedNumberLen + lectureCodeLen + professorIdLen + maxStdNumLen + curStdNumLen + lectureRoomLen + lectureTimeLen + timeCntLen];
        System.arraycopy(lectureRoomLength.getBytes(), 0, data, 0, lectureRoomLength.getBytes().length);
        System.arraycopy(lectureTimeLength.getBytes(), 0, data, lectureRoomLengthLen, lectureTimeLength.getBytes().length);
        System.arraycopy(timeCntLength.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen, timeCntLength.getBytes().length);
        System.arraycopy(seperatedNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen, seperatedNumber.getBytes().length);
        System.arraycopy(lectureCode.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen, lectureCode.getBytes().length);
        System.arraycopy(professorId.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen, professorId.getBytes().length);
        System.arraycopy(maxStudentNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen, maxStudentNumber.getBytes().length);
        System.arraycopy(curStudentNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen, curStudentNumber.getBytes().length);
        int i=0;
        for(String str : lectureRoomDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+(16*i) ,str.getBytes().length);
            i++;
        }

        i=0;
        for(String str : lectureTimeDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+(16*i), str.getBytes().length);
            i++;
        }

        i=0;
        for(String str : timeCnt){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+lectureTimeLen+(6*i), str.getBytes().length);
            i++;
        }

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestOpenLectureDelete(String seperatedNumber, String lectureCode){ // 개설교과목 삭제 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.DELETE_OPEN_LECTURE.getCode());

        int seperatedNumberLen = 5;
        int lectureCodeLen = 11;

        seperatedNumber += '\0'; lectureCode += '\0';

        byte[] data = new byte[seperatedNumberLen + lectureCodeLen];
        System.arraycopy(seperatedNumber.getBytes(), 0, data, 0, seperatedNumber.getBytes().length);
        System.arraycopy(lectureCode.getBytes(), 0, data, seperatedNumberLen, lectureCode.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;
    }

    public Protocol requestUpdateTime(String id, String startTime, String endTime){ // 기간 변경 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.UPDATE_SYLLABUS_PERIOD.getCode());

        int idLen = 5;
        int startTimeLen = 31;
        int endTimeLen = 31;

        id+= '\0'; startTime += '\0'; endTime += '\0';

        byte[] data = new byte[idLen + startTimeLen + endTimeLen];
        System.arraycopy(id.getBytes(), 0, data, 0, id.getBytes().length);
        System.arraycopy(startTime.getBytes(), 0, data, idLen, startTime.getBytes().length);
        System.arraycopy(endTime.getBytes(), 0, data, idLen+startTimeLen, endTime.getBytes().length);

        protocol.setDataPacket(data);

        return protocol;


    }

    public Protocol requestUpdateOpenLecture(String seperatedNumber, String lectureCode, String professorId, String maxStudentNumber, String curStudentNumber, List<String> lectureRoomDTOS, List<String> lectureTimeDTOS, List<String> timeCnt, List<String> changeLectureRoomDTOS, List<String> changeLectureTimeDTOS, String lectureRoomLength, String lectureTimeLength, String timeCntLength){
        // 개설교과목 변경 요청
        Protocol protocol = new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.UPDATE_OPEN_LECTURE.getCode());
        int lectureRoomLengthLen = 5;
        int lectureTimeLengthLen = 5;
        int timeCntLengthLen = 5;
        int seperatedNumberLen = 5;
        int lectureCodeLen = 11;
        int professorIdLen = 61;
        int maxStdNumLen = 5;
        int curStdNumLen = 5;
        int lectureRoomLen = lectureRoomDTOS.size()*16;
        int lectureTimeLen = lectureTimeDTOS.size()*16;
        int changeLectureRoomLen = changeLectureRoomDTOS.size()*16;
        int changeLectureTimeLen = changeLectureTimeDTOS.size()*16;
        int timeCntLen = timeCnt.size()*6;

        lectureRoomLength += '\0'; lectureTimeLength += '\0'; lectureRoomLength += '\0'; seperatedNumber += '\0'; lectureCode += '\0'; professorId += '\0'; maxStudentNumber += '\0'; curStudentNumber += '\0';

        byte[] data = new byte[lectureRoomLengthLen + lectureTimeLengthLen + timeCntLengthLen + seperatedNumberLen + lectureCodeLen + professorIdLen + maxStdNumLen + curStdNumLen + lectureRoomLen + lectureTimeLen + changeLectureRoomLen + changeLectureTimeLen + timeCntLen];
        System.arraycopy(lectureRoomLength.getBytes(), 0, data, 0, lectureRoomLength.getBytes().length);
        System.arraycopy(lectureTimeLength.getBytes(), 0, data, lectureRoomLengthLen, lectureTimeLength.getBytes().length);
        System.arraycopy(timeCntLength.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen, timeCntLength.getBytes().length);
        System.arraycopy(seperatedNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen, seperatedNumber.getBytes().length);
        System.arraycopy(lectureCode.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen, lectureCode.getBytes().length);
        System.arraycopy(professorId.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen, professorId.getBytes().length);
        System.arraycopy(maxStudentNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen, maxStudentNumber.getBytes().length);
        System.arraycopy(curStudentNumber.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen, curStudentNumber.getBytes().length);
        int i=0;
        for(String str : lectureRoomDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+(16*i) ,str.getBytes().length);
            i++;
        }

        i=0;
        for(String str : lectureTimeDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+(16*i), str.getBytes().length);
            i++;
        }

        i=0;
        for(String str : changeLectureRoomDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+lectureTimeLen+(16*i), str.getBytes().length);
        }

        i=0;
        for(String str : changeLectureTimeDTOS){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+lectureTimeLen+changeLectureRoomLen+(16*i), str.getBytes().length);
        }

        i=0;
        for(String str : timeCnt){
            str += '\0';
            System.arraycopy(str.getBytes(), 0, data, lectureRoomLengthLen+lectureTimeLengthLen+timeCntLengthLen+seperatedNumberLen+lectureCodeLen+professorIdLen+maxStdNumLen+curStdNumLen+lectureRoomLen+lectureTimeLen+changeLectureRoomLen+changeLectureTimeLen+(6*i), str.getBytes().length);
            i++;
        }

        protocol.setDataPacket(data);

        return protocol;
    }





    public Protocol requestStudentRead(String studentId){ // 학생 조회 요청
        int studentIdLen=20*3+1;
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.READ_STUDENT_INFO.getCode());
        if(studentId.charAt(0)!='\0'){
            byte[] data=new byte[studentIdLen];
            System.arraycopy(studentId.getBytes(),0,data,0,studentId.getBytes().length);
            protocol.setDataPacket(data);
        }

        return protocol;
    }

    public Protocol requestProfessorRead(String professorId){ // 교수 조회 요청
        int professorIdLen=20*3+1;
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.READ_PROFESSOR_INFO.getCode());
        if(professorId.charAt(0)!='\0'){
            byte[] data=new byte[professorIdLen];
            System.arraycopy(professorId.getBytes(),0,data,0,professorId.getBytes().length);
            protocol.setDataPacket(data);
        }
        return protocol;
    }


}
