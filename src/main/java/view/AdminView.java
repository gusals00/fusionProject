package view;

import controller.AdminController;
import network.AdminCode;
import network.ProtocolType;
import lombok.Getter;
import lombok.Setter;
import network.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
public class AdminView {
    AdminController adminController;
    private String id;
    private Scanner s = new Scanner(System.in);

    public AdminView(){
        adminController = new AdminController();
    }

    public Protocol adminResView(Protocol getProtocol){

        try{
            if(getProtocol!=null){
                if(getProtocol.getProtocolCode() == AdminCode.SUCCESS.getCode()){// 성공 메시지 출력
                    System.out.println("성공했습니다.");
                }
                else if(getProtocol.getProtocolCode() == AdminCode.FAIL.getCode()){// 실패 메시지 출력
                    System.out.println("실패했습니다.");
                }
                else if(getProtocol.getProtocolCode() == AdminCode.READ_LECTURE.getCode()){// 수신받은 교과목 정보 출력
                    readLectureDTOS(getProtocol);
                }
                else if(getProtocol.getProtocolCode()==AdminCode.OPEN_LECTURE_INFO_RES.getCode()){//개설 교과목 출력
                    printOpenLecture(getProtocol);
                }
                else if(getProtocol.getProtocolCode()==AdminCode.PROFESSOR_INFO_RES.getCode()){// 교수 정보 출력
                    printProfessorInfo(getProtocol);
                }
                else if(getProtocol.getProtocolCode()==AdminCode.STUDENT_INFO_RES.getCode()){// 학생 정보 출력
                    printStudentInfo(getProtocol);
                }
            }
        }catch(Exception e){
            System.out.println("오류가 발생했습니다. 다시 입력하십시오.");
        }

        while(true){ // 메뉴 출력
                System.out.println("관리자 id : " + id);
                menu();
                int select = s.nextInt();
                int select2;
                if (select == 1) {
                    System.out.println("1. 교수 계정 생성");
                    System.out.println("2. 학생 계정 생성");
                    System.out.print("입력 : ");
                    select2 = s.nextInt();
                    if (select2 == 1) {
                        return getProfessorDTO(); // 교수 계정 생성
                    } else if (select2 == 2) {
                        return getStudentDTO(); // 학생 계정 생성
                    }
                } else if (select == 2) {

                    System.out.println("1. 교과목 생성");
                    System.out.println("2. 교과목 수정");
                    System.out.println("3. 교과목 삭제");
                    System.out.println("4. 교과목 조회");
                    System.out.print("입력 : ");
                    select2 = s.nextInt();

                    if (select2 == 1) {
                        return getLectureDTO(); // 교과목 생성
                    } else if (select2 == 2) {
                        return getUpdateLectureDTO(); // 교과목 수정
                    } else if (select2 == 3) {
                        return getLectureCode(); // 교과목 삭제
                    } else if (select2 == 4) {
                        return adminController.lectureReadReqProtocol(); // 교과목 조회
                    }
                } else if (select == 3) {
                    System.out.println("1. 개설 교과목 생성");
                    System.out.println("2. 개설 교과목 수정");
                    System.out.println("3. 개설 교과목 삭제");
                    System.out.println("4. 개설 교과목 조회");

                    System.out.print("입력 : ");
                    select2 = s.nextInt();

                    if (select2 == 1) {
                        return getOpenLectureCreate(); // 개설 교과목 생성
                    } else if (select2 == 2) {
                        return getOpenLectureUpdate(); // 개설 교과목 수정
                    } else if (select2 == 3) {
                        return getDeleteInfo(); // 개설 교과목 삭제
                    } else if (select2 == 4) {
                        return getOpenLectureRead();// 개설 교과목 정보 요청
                    }
                } else if (select == 4) {
                    return getPeriod(6); // 강의 계획서 입력 기간 설정
                } else if (select == 5) {
                    System.out.println("1. 1학년 수강신청 기간 설정");
                    System.out.println("2. 2학년 수강신청 기간 설정");
                    System.out.println("3. 3학년 수강신청 기간 설정");
                    System.out.println("4. 4학년 수강신청 기간 설정");
                    System.out.println("5. 전체학년 수강신청 기간 설정");
                    System.out.print("입력 : ");
                    select2 = s.nextInt();
                    if (select2 == 1) {
                        return getPeriod(1); // 1학년 수강신청 기간 설정
                    } else if (select2 == 2) {
                        return getPeriod(2); // 2학년 수강신청 기간 설정
                    } else if (select2 == 3) {
                        return getPeriod(3); // 3학년 수강신청 기간 설정
                    } else if (select2 == 4) {
                        return getPeriod(4); // 4학년 수강신청 기간 설정
                    } else if (select2 == 5) {
                        return getPeriod(5); // 전체학년 수강신청 기간 설정
                    }
                } else if (select == 6) {// 교수/학생 정보 조회
                    System.out.println("1.교수 정보 조회");
                    System.out.println("2.학생 정보 조회");
                    System.out.print("입력 : ");
                    select2 = s.nextInt();
                    if (select2 == 1) {//교수 정보 조회
                        String professorId = "\0";
                        System.out.println("1.전체 교수 조회");
                        System.out.println("2.특정 교수 조회");
                        System.out.print("입력 : ");
                        int select3 = s.nextInt();
                        if (select3 == 1) {
                            return adminController.requestProfessorRead(professorId);
                        } else if (select3 == 2) { //교수 아이디 입력
                            System.out.print("교수 아이디 입력 : ");
                            professorId = s.next() + "\0";
                            return adminController.requestProfessorRead(professorId);
                        }

                    } else if (select2 == 2) {//학생 정보 조회
                        String studentId = "\0";
                        System.out.println("1.전체 학생 조회");
                        System.out.println("2.특정 학생 조회");

                        System.out.print("입력 : ");
                        int select3 = s.nextInt();
                        if (select3 == 1) {
                            return adminController.requestStudentRead(studentId);

                        } else if (select3 == 2) {//학생 학번 입력
                            System.out.print("학생 학번 입력 : ");
                            studentId = s.next() + "\0";
                            return adminController.requestStudentRead(studentId);

                        }
                    }

                } else if (select == 7) { // 로그아웃
                    System.out.println("로그아웃합니다.");
                    return adminController.logoutReqProtocol();
                } else {
                    System.out.println("잘못된 번호입니다. 다시 입력하십시오.");
                }
        }


    }

    public void menu(){ // 메뉴출력
        System.out.println("-----관리자 페이지-----");
        System.out.println("1. 교수·학생 계정 생성");
        System.out.println("2. 교과목 생성/수정/삭제/조회");
        System.out.println("3. 개설 교과목 생성/수정/삭제/조회");
        System.out.println("4. 강의 계획서 입력 기간 설정");
        System.out.println("5. 학년별 수강 신청 기간 설정");
        System.out.println("6. 교수/학생 정보 조회");
        System.out.println("7. 로그아웃");
        System.out.print("입력 : ");
    }

    public Protocol getOpenLectureRead(){// 개설 교과목 정보 요청 protool 리턴
        int select=0;
        String level="\0";
        String name="\0";
        System.out.println("1.전체 개설 교과목 조회");
        System.out.println("2.학년으로 조회");
        System.out.println("3.교수이름으로 조회");
        System.out.println("4.학년,교수이름으로 조회");
        System.out.print("입력 : ");
        select = s.nextInt();

        if(select==1){//전체 조회
        }

        else if(select==2){//학년으로 조회
            System.out.print("학년 :");
            level=s.next();
        }
        else if(select==3){//교수이름으로 조회
            System.out.print("교수 이름 :");
            name=s.next();
        }
        else {//학년,교수 이름으로 조회
            System.out.print("학년 :");
            level=s.next();
            System.out.print("교수 이름 :");
            name=s.next();
        }

        return sendOpenLectureProtocol(level,name);
    }

    public Protocol sendOpenLectureProtocol(String level, String name){// 교수 조회 요청 protocol 생성
        Protocol protocol=new Protocol(ProtocolType.ADMIN.getType());
        protocol.setProtocolCode(AdminCode.READ_OPEN_LECTURE_INFO.getCode());
        if(level.equals("\0") && name.equals("\0")){//전체 조회

        }
        else if(!level.equals("\0") && name.equals("\0")){//학년 조회
            if(!isDigitString(level)){
                level="-1";
            }
            level+="\0";
            protocol.setDataPacket(makeDataByteArr(level,name));
        }
        else if(level.equals("\0") && !name.equals("\0")){//교수조회
            name+="\0";
            protocol.setDataPacket(makeDataByteArr(level,name));
        }
        else{//교수 및 학년 조회
            if(!isDigitString(level)){// 입력받은 학년이 숫자가 아닌 경우
                level="-1";
            }
            level+="\0";
            name+="\0";
            protocol.setDataPacket(makeDataByteArr(level,name));// protocol에 데이터 부분 저장.
        }
        return protocol;

    }

    public byte[] makeDataByteArr(String level,String name){// 수강학년과 교수 이름을 바이트 배열로 변경해서 리턴
        //학년(4+1), 교수이름으로 처리(20*3+1)
        int levelLen=5;
        int nameLen=61;

        byte[] arr = new byte[levelLen+nameLen];
        System.arraycopy(level.getBytes(),0,arr,0,level.getBytes().length);
        System.arraycopy(name.getBytes(),0,arr,levelLen,name.getBytes().length);
        return arr;
    }

    public boolean isDigitString(String str){// 해당 문자열이 양의 정수로 변환이 가능한지 여부.
        for(int i=0;i<str.length();i++){
            if(Character.isDigit(str.charAt(i))==false){
                return false;
            }
        }
        return true;
    }

    public void printProfessorInfo(Protocol protocol){//교수 정보 출력
        int idLen=20*3+1;
        int nameLen=20*3+1;
        int ssnLen=14*3+1;
        int eMailLen=30*3+1;
        int departmentLen=15*3+1;

        if(protocol.getLength()==0)//교수 정보가 하나도 없는 경우 출력 안함.
            return;

        int listCnt=protocol.getListLength();// 리스트 개수
        int listLen=protocol.getLength()/listCnt;//한개의 리스트 길이
        byte[] packet = protocol.getPacket();

        for(int i=0;i<listCnt;i++){
            byte[] data = new byte[listLen];
            System.arraycopy(packet,Protocol.LEN_HEADER+i*listLen,data,0,listLen);
            String id=new String(data,0,idLen).split("\0")[0];// 교수 id
            String name=new String(data,idLen,nameLen).split("\0")[0];//교수 이름
            String ssn=new String(data,idLen+nameLen,ssnLen).split("\0")[0];//주민등록번호
            String eMail=new String(data,idLen+nameLen+ssnLen,eMailLen).split("\0")[0];//email
            String department=new String(data,idLen+nameLen+ssnLen+eMailLen,departmentLen).split("\0")[0];//학과

            System.out.printf("ID : %s 이름 : %s 주민등록번호 : %s eMail : %s 학과 : %s \n",id,name,ssn,eMail,department);
        }

    }

    public void printStudentInfo(Protocol protocol){// 학생 정보 출력
        int idLen=20*3+1;
        int nameLen=20*3+1;
        int ssnLen=14*3+1;
        int levelLen=4+1;
        int departmentLen=15*3+1;

        if(protocol.getLength()==0)//학생 정보가 하나도 없는 경우
            return;

        int listCnt=protocol.getListLength();
        int listLen=protocol.getLength()/listCnt;
        byte[] packet = protocol.getPacket();

        for(int i=0;i<listCnt;i++){
            byte[] data = new byte[listLen];
            System.arraycopy(packet,Protocol.LEN_HEADER+i*listLen,data,0,listLen);
            String id=new String(data,0,idLen).split("\0")[0];// 학생 id
            String name=new String(data,idLen,nameLen).split("\0")[0];//학생 이름
            String ssn=new String(data,idLen+nameLen,ssnLen).split("\0")[0];//주민등록번호
            String level=new String(data,idLen+nameLen+ssnLen,levelLen).split("\0")[0];//학년
            String department=new String(data,idLen+nameLen+ssnLen+levelLen,departmentLen).split("\0")[0];//학과

            System.out.printf("ID : %s 이름 : %s 주민등록번호 : %s 학년 : %s 학과 : %s \n",id,name,ssn,level,department);
        }
    }


    public void printOpenLecture(Protocol protocol){// 개설 교과목 출력
        int nameLen = 20*3+1;
        int levelLen=2+1;
        int codeLen=10+1;
        int seperateLen=2+1;
        int professorLen=20*3+1;
        int maxLen=3+1;
        int curLen=3+1;
        int roomAndTimeLen=60+1;
        int listLen = nameLen+levelLen+seperateLen+codeLen+professorLen+maxLen+curLen+roomAndTimeLen;
        int listCnt=protocol.getListLength();//리스트 개수
        byte[] packet = protocol.getPacket();

        for(int i=0;i<listCnt;i++){
            byte[] data = new byte[listLen];
            System.arraycopy(packet,Protocol.LEN_HEADER+i*listLen,data,0,listLen);
            String name=new String(data,0,nameLen).split("\0")[0];// 개설 교과목 이름
            String level=new String(data,nameLen,levelLen).split("\0")[0];//학년
            String code=new String(data,nameLen+levelLen,codeLen).split("\0")[0];//코드
            String seperate=new String(data,nameLen+levelLen+codeLen,seperateLen).split("\0")[0];//분반
            String professor=new String(data,nameLen+levelLen+codeLen+seperateLen,professorLen).split("\0")[0];//교수
            String max=new String(data,nameLen+levelLen+codeLen+seperateLen+professorLen,maxLen).split("\0")[0];//최대 수강 인원
            String cur=new String(data,nameLen+levelLen+codeLen+seperateLen+professorLen+maxLen,curLen).split("\0")[0];//현재 수강인원
            String roomAndTime=new String(data,nameLen+levelLen+codeLen+seperateLen+professorLen+maxLen+curLen,roomAndTimeLen).split("\0")[0];//강의실 및 시간
            System.out.printf("과목명:%s 이수학년:%s 강의코드:%s-%s 교수:%s 최대수강인원:%s 현재수강인원:%s 강의실 및 시간:%s\n",name,level,code,seperate,professor,max,cur,roomAndTime);
        }


    }

    public Protocol getProfessorDTO(){ // 교수 생성 요청 protocol 생성
        String professorId, professorPw, professorName, ssn, eMail, professorPhoneNumber, departmentName;
        System.out.print("교수 아이디, 교수 비밀번호, 교수이름, 주민번호, 이메일, 교수폰번호, 학과이름 입력 : ");
        professorId = s.next();
        professorPw = s.next();
        professorName = s.next();
        ssn = s.next();
        eMail = s.next();
        professorPhoneNumber = s.next();
        departmentName = s.next();

        return adminController.requestProfessorCreate(professorId, professorPw, professorName, ssn, eMail, professorPhoneNumber, departmentName);
    }

    public Protocol getStudentDTO(){ // 학생 생성 요청 protocol 생성
        String studentId, studentPw, studentName, ssn, level, departmentName;
        System.out.print("학생 아이디, 학생 비밀번호, 학생이름, 주민번호, 학년, 학과이름 입력 : ");
        studentId = s.next();
        studentPw = s.next();
        studentName = s.next();
        ssn = s.next();
        level = s.next();
        departmentName = s.next();

        return adminController.requestStudentCreate(studentId, studentPw, studentName, ssn, level, departmentName);
    }

    public Protocol getLectureDTO(){ // 교과목 생성 요청 protocol 생성
        String lectureCode, lectureName, lectureLevel, lectureCredit;
        System.out.print("교과목코드, 교과목이름, 학년, 학점 입력 : ");
        lectureCode = s.next();
        lectureName = s.next();
        lectureLevel = s.next();
        lectureCredit = s.next();

        return adminController.requestLectureCreate(lectureCode, lectureName, lectureLevel, lectureCredit);
    }

    public Protocol getLectureCode(){ // 교과목 삭제 요청 protocol 생성
        String lectureCode;
        System.out.print("교과목코드 입력 : ");
        lectureCode = s.next();

        return adminController.requestLectureDelete(lectureCode);
    }

    public Protocol getUpdateLectureDTO(){ // 교과목 변경 요청 protocol 생성
        String lectureCode, lectureName, lectureLevel, lectureCredit;
        System.out.print("교과목코드와 수정할 교과목 이름, 학년, 학점 입력(수정안할 데이터에는 0 입력) : ");
        lectureCode = s.next();
        lectureName = s.next();
        lectureLevel = s.next();
        lectureCredit = s.next();

        return adminController.requestLectureUpdate(lectureCode, lectureName, lectureLevel, lectureCredit);
    }

    public Protocol getOpenLectureCreate(){ /// 개설교과목 생성 요청 protocol 생성
        String seperatedNumber, lectureCode, professorId, maxStudentNumber, curStudentNumber;
        List<String> timeCnt = new ArrayList<String>(); // 강의실별 강의시간 갯수 저장
        List<String> lectureRoomDTOS = new ArrayList<String>(); // 강의실 저장
        List<String> lectureTimeDTOS = new ArrayList<String>(); // 강의시간 저장

        System.out.print("분반, 교과목코드, 교수아이디, 최대수강학생, 현재수강학생 입력 : ");
        seperatedNumber = s.next();
        lectureCode = s.next();
        professorId = s.next();
        maxStudentNumber = s.next();
        curStudentNumber = s.next();

        int i=0;
        while(true) {
            System.out.print("강의실 입력(ex D110, 더 추가할게 없을시 0 입력) : ");
            String room = s.next();
            if(room.equals("0")) break;

            lectureRoomDTOS.add(room);
            while(true) {
                System.out.print("해당 강의실에 대한 강의시간을 입력(ex 월1, 다 입력시 0 입력) : ");
                String time = s.next();
                if(time.equals("0")) break;

                lectureTimeDTOS.add(time);
                i++;
            }
            timeCnt.add(Integer.toString(i));
        }

        return adminController.requestOpenLectureCreate(seperatedNumber, lectureCode, professorId, maxStudentNumber, curStudentNumber, lectureRoomDTOS, lectureTimeDTOS, timeCnt, Integer.toString(lectureRoomDTOS.size()), Integer.toString(lectureTimeDTOS.size()), Integer.toString(timeCnt.size()));
    }

    public Protocol getDeleteInfo(){ // 개설교과목 삭제 요청 protocol 생성
        String seperatedNumber;
        String lectureCode;

        System.out.print("삭제할 분반, 교과목코드 입력 : ");
        seperatedNumber = s.next();
        lectureCode = s.next();

        return adminController.requestOpenLectureDelete(seperatedNumber, lectureCode);
    }

    public Protocol getPeriod(int id){ // 기간 변경 요청 protocol 생성
        s.nextLine();
        String startTime, endTime;
        System.out.print("시작시간 입력 (ex) 2021-11-23 09:34:05) : ");
        startTime = s.nextLine();
        System.out.print("종료시간 입력 (ex) 2021-11-24 18:00:00) : ");
        endTime = s.nextLine();

        return adminController.requestUpdateTime(Integer.toString(id), startTime, endTime);
    }

    public Protocol getOpenLectureUpdate(){ // 개설교과목 변경 요청 protocol 생성
        String seperatedNumber, lectureCode, professorId, maxStudentNumber, curStudentNumber;
        List<String> timeCnt = new ArrayList<String>(); // 강의실별 강의시간 갯수 저장
        List<String> lectureRoomDTOS = new ArrayList<String>(); // 수정할 강의실 저장
        List<String> lectureTimeDTOS = new ArrayList<String>(); // 수정할 강의시간 저장
        List<String> changeLectureRoomDTOS = new ArrayList<String>(); // 수정된 강의실 저장
        List<String> changeLectureTimeDTOS = new ArrayList<String>(); // 수정된 강의시간 저장

        System.out.print("분반, 교과목코드와 수정할 교수아이디, 최대학생, 현재학생 입력(수정안할 데이터는 -1 입력) : ");
        seperatedNumber = s.next();
        lectureCode = s.next();
        professorId = s.next();
        maxStudentNumber = s.next();
        curStudentNumber = s.next();

        int i=0;
        while(true){
            System.out.print("수정할 강의실 입력(ex D110, 더 수정할 강의실이 없을시 0 입력) : ");
            String room = s.next();
            if(room.equals("0")) break;
            System.out.print("해당 강의실을 수정하려면 새로운 강의실, 강의시간만 수정하려면 0 입력 : ");
            String changeRoom = s.next();

            lectureRoomDTOS.add(room);
            if(changeRoom.equals("0")){
                changeLectureRoomDTOS.add(room);
            }
            else{
                changeLectureRoomDTOS.add(changeRoom);
            }

            while(true){
                System.out.print("해당 강의실에 대해 수정할 강의시간과 새로운 강의시간들을 입력(ex) 월1 화1, 더 수정할 강의시간이 없을시 0 입력) : ");
                String time = s.next();
                if(time.equals("0")) break;
                String changeTime = s.next();

                lectureTimeDTOS.add(time);
                changeLectureTimeDTOS.add(changeTime);
                i++;
            }
            timeCnt.add(Integer.toString(i));
        }

        return adminController.requestUpdateOpenLecture(seperatedNumber, lectureCode, professorId, maxStudentNumber, curStudentNumber, lectureRoomDTOS, lectureTimeDTOS, timeCnt, changeLectureRoomDTOS, changeLectureTimeDTOS, Integer.toString(lectureRoomDTOS.size()), Integer.toString(lectureTimeDTOS.size()), Integer.toString(timeCnt.size()));
    }

    public void readLectureDTOS(Protocol getProtocol){ // 교과목 전체 출력
        byte[] data = getProtocol.getPacket();
        int lectureCodeLen = 11;
        int lectureNameLen = 61;
        int lectureLevelLen = 5;
        int lectureCreditLen = 5;
        int allLen = 82;

        String length = new String(getProtocol.getPacket(), Protocol.LEN_HEADER, 5).split("\0")[0];
        for(int i=0; i<Integer.parseInt(length); i++){
            System.out.print("과목코드 " + new String(getProtocol.getPacket(), Protocol.LEN_HEADER+5+(i*allLen), 11).split("\0")[0]);
            System.out.print(", 과목명 " + new String(getProtocol.getPacket(), Protocol.LEN_HEADER+5+11+(i*allLen), 61).split("\0")[0]);
            System.out.print(", 이수학년 " + new String(getProtocol.getPacket(), Protocol.LEN_HEADER+5+11+61+(i*allLen), 5).split("\0")[0]);
            System.out.print(", 학점 " + new String(getProtocol.getPacket(), Protocol.LEN_HEADER+5+11+61+5+(i*allLen), 5).split("\0")[0]);
            System.out.println();
        }
    }
}
