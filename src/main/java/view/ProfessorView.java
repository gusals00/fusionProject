package view;

import controller.ProfessorController;
import network.ProfessorCode;
import lombok.Getter;
import lombok.Setter;
import network.Protocol;

import java.util.Scanner;

@Getter
@Setter
public class ProfessorView {
    private ProfessorController professorController;
    private String id;
    private String name;
    private int menuNum1 = 0;
    private int menuNum2 = 0;
    private int pagingNum = 0;

    public ProfessorView(){
        professorController = new ProfessorController();
    }

    public Protocol professorResView(Protocol getProtocol){//선택한 기능에 따라 서버에게 패킷 전송,서버에게 패킷 받아서 결과 출력.
        Scanner s=new Scanner(System.in);
        String data = null;

        try{
            if(getProtocol!=null){
                if(getProtocol.getProtocolCode()== ProfessorCode.SUCCESS_CREATE_AND_UPDATE_AND_DELETE.getCode()){//성공 메세지 출력
                    System.out.println("성공");
                }
                else if(getProtocol.getProtocolCode()== ProfessorCode.FAIL_CREATE_AND_UPDATE_AND_DELETE_AND_READ.getCode()){//실패 메세지 출력.
                    System.out.println("실패");
                }
                else if(getProtocol.getProtocolCode()== ProfessorCode.READ_LECTURE_LIST_RES.getCode()){//개설 교과목 조회 목록 출력.
                    byte[] viewData=getProtocol.getPacket();
                    viewOpenLectureList(getProtocol,viewData);
                }
                else if(getProtocol.getProtocolCode()== ProfessorCode.READ_OPEN_LECTURE_ENROLLED_STUDENT_RES.getCode()){//선택한 개설 교과목의 수강 신청 학생 조회 목록 출력.
                    byte[] viewData=getProtocol.getPacket();
                    viewLectureEnrolledStudentList(getProtocol,viewData);
                }
                else if(getProtocol.getProtocolCode()== ProfessorCode.READ_SYLLABUS_LIST_RES.getCode()){//강의 계획서 (+주차정보) 출력.
                    byte[] viewData=getProtocol.getPacket();
                    viewSyllabusList(getProtocol,viewData);
                }
                else if(getProtocol.getProtocolCode()== ProfessorCode.READ_PROFESSOR_SCHEDULE_RES.getCode()){//교수 개인 시간표 출력.
                    byte[] viewData=getProtocol.getPacket();
                    viewProfessorSchedule(getProtocol,viewData);
                }
            }
        }catch(Exception e){
            System.out.println("오류가 발생했습니다. 다시 입력하십시오.");
        }

        //메뉴 출력 및 기능 선택.
        System.out.println("id : "+id);
        menu1();
        setMenuNum();//선택한 기능에 따라 필요시 메뉴2 출력.

        if(menuNum1 == 1) {//교수 개인정보 수정
            System.out.print("수정할 정보를 입력하시오. : ");
            data = s.nextLine();
            return professorController.requestProfessorUpdateInfo(id, data, menuNum2);
        }

        else if(menuNum1 == 2) {//강의 계획서 생성
            System.out.println("생성할 정보를 입력하시오.");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("3.교재 : ");
            s.nextLine();
            String bookName = s.nextLine();
            System.out.print("4.강의 목표 : ");
            String lectureGoal = s.nextLine();
            return professorController.requestSyllabusCreateInfo(id, lectureCode, seperatedNum, bookName, lectureGoal);
        }

        else if(menuNum1 == 3) {//강의 계획서 수정
            System.out.println("수정할 강의계획서의 과목코드와 분반번호를 입력하시오.  ");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("수정할 정보를 입력하시오. : ");
            s.nextLine();
            data = s.nextLine();
            return professorController.requestSyllabusUpdateInfo(lectureCode,seperatedNum,data, menuNum2);
        }

        else if(menuNum1 == 4) {//교과목 목록 조회
            return professorController.requestOpenLectureListInfo(id);
        }

        else if(menuNum1 == 5) {//강의 계획서 (+주차 정보) 조회
            System.out.println("조회할 강의 계획서의 과목코드와 분반번호를 입력하시오.  ");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();

            return professorController.requestSyllabusListInfo(lectureCode,seperatedNum);
        }

        else if(menuNum1 == 6) {//교과목 수강 신청 학생 목록 조회
            System.out.println("조회할 교과목의 과목코드와 분반번호를 입력하시오.  ");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("몇명씩 볼 것인지 입력하시오.");
            pagingNum = s.nextInt();

            return professorController.requestLectureEnrolledStudentsListInfo(lectureCode,seperatedNum);
        }

        else if(menuNum1 == 7) {//교수 교과목 시간표 조회
            return professorController.requestProfessorSchedule(id);
        }

        else if(menuNum1 == 8) {//주차별 강의 정보 추가
            System.out.println("생성할 정보를 입력하시오.");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("3.주차 : ");
            int syllabusWeek = s.nextInt();
            System.out.print("4.주차 목표 : ");
            s.nextLine();
            String syllabusSubject = s.nextLine();
            System.out.print("5.주차 내용 : ");
            String syllabusContent = s.nextLine();
            System.out.print("6.과제 : ");
            String syllabusAssignment = s.nextLine();
            System.out.print("7.평가 : ");
            String syllabusEvaluation = s.nextLine();

            return professorController.requestSyllabusWeekInfoCreateInfo(id, lectureCode, seperatedNum, syllabusWeek, syllabusSubject, syllabusContent,syllabusAssignment,syllabusEvaluation);
        }

        else if(menuNum1 == 9) {//주차별 강의 정보 수정
            System.out.println("수정할 강의계획서 주차정보의 과목코드와 분반번호,주차를 입력하시오.  ");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("3.주차 : ");
            int week = s.nextInt();
            System.out.println("수정할 정보를 입력하시오. : ");
            s.nextLine();
            data = s.nextLine();
            return professorController.requestSyllabusWeekInfoUpdateInfo(lectureCode,seperatedNum,week, data, menuNum2);
        }

        else if(menuNum1 == 10) {//주차별 강의 정보 삭제
            System.out.println("삭제할 강의계획서 주차정보의 과목코드와 분반번호,주차를 입력하시오.  ");
            System.out.print("1.과목 코드 : ");
            String lectureCode = s.nextLine();
            System.out.print("2.분반 번호 : ");
            int seperatedNum = s.nextInt();
            System.out.print("3.주차 : ");
            int week = s.nextInt();

            return professorController.requestSyllabusWeekInfoDeleteInfo(lectureCode,seperatedNum,week);
        }
        else if(menuNum1 == 11){//로그 아웃
            System.out.println("로그아웃합니다.");
            return professorController.logoutReqProtocol();
        }

      return null;
    }

    public void menu1(){//메뉴
        System.out.println("-----교수 페이지-----");
        System.out.println("1. 교수 개인정보 수정 ");
        System.out.println("2. 강의 계획서 입력 ");
        System.out.println("3. 강의 계획서 수정 ");
        System.out.println("4. 교과목 목록 조회 ");
        System.out.println("5. 교과목 강의 계획서 조회 ");
        System.out.println("6. 교과목 수강 신청 학생 목록 조회 ");
        System.out.println("7. 교과목 시간표 조회 ");
        System.out.println("8. 주차별 강의 정보 추가 ");
        System.out.println("9. 주차별 강의 정보 수정 ");
        System.out.println("10. 주차별 강의 정보 삭제 ");
        System.out.println("11. 로그아웃 ");
    }

    public void menu2(int menuNum1){//메뉴2
        if(menuNum1 == 1){
            System.out.println("1. 교수 패스워드 수정 ");
            System.out.println("2. 교수 이름 수정 ");
            System.out.println("3. 교수 주민번호 수정 ");
            System.out.println("4. 교수 이메일 수정 ");
            System.out.println("5. 교수 폰 번호 수정 ");

        }
        if(menuNum1 == 3){
            System.out.println("1. 책 이름 수정 ");
            System.out.println("2. 강의 목표 수정 ");

        }
        if(menuNum1 == 9){
            System.out.println("1. 강의 주차 주제 수정 ");
            System.out.println("2. 강의 주차 내용 수정 ");
            System.out.println("3. 강의 주차 과제 수정 ");
            System.out.println("4. 강의 주차 평가 수정 ");

        }
    }

    public void setMenuNum(){//메뉴1에서 선택한 기능 중 메뉴2 불러와야하면 불러오고 기능 잘못 선택시 오류 표시.
        Scanner s=new Scanner(System.in);
        menuNum1 = s.nextInt();
        if(menuNum1 < 1 || 11 < menuNum1){
            System.out.println("잘못된 번호입니다. 다시 입력하십시오. ");
            menu1();
            setMenuNum();
        }

        else if(menuNum1 == 1 || menuNum1 == 3 || menuNum1 == 9 ){
            menu2(menuNum1);
            menuNum2 = s.nextInt();
            if((menuNum1 == 1 && menuNum2 > 5) || (menuNum1 == 3 && menuNum2 > 2) || (menuNum1 == 9 && menuNum2 > 4)){
                System.out.println("잘못된 번호입니다. 다시 입력하십시오. ");
                menu1();
                setMenuNum();
            }
        }
    }

    void viewOpenLectureList(Protocol getProtocol,byte[] data) {//개설 교과목 조회시 서버에서 받은 패킷의 정보 꺼내 출력.
        int listenLevelLength = 5 + 1;
        int lectureNameLength = 20 * 3 + 1;
        int lectureCodeLength = 20 * 3 + 1;
        int lectureSeperatdeNumberLength = 5 + 1;
        int lectureProfessorLength = 20 * 3 + 1;
        int lectureTimeAndRoomlength = 20 * 3 + 20 * 3 + 1;
        int maxStudentNumberLength = 5 + 1;
        int curStudentNumberLength = 5 + 1;
        int oneLineLen = listenLevelLength + lectureNameLength + lectureCodeLength + lectureSeperatdeNumberLength + lectureProfessorLength + lectureTimeAndRoomlength + maxStudentNumberLength + curStudentNumberLength;

        for (int i = 0; i < getProtocol.getPacket().length / oneLineLen; i++) {
            String listenLevel = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i), listenLevelLength).split("\0")[0];
            String lectureName = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength, lectureNameLength).split("\0")[0];
            ;
            String lectureCode = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength, lectureCodeLength).split("\0")[0];
            ;
            String lectureSeperatdeNumber = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength + lectureCodeLength, lectureSeperatdeNumberLength).split("\0")[0];
            ;
            String lectureProfessor = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength + lectureCodeLength + lectureSeperatdeNumberLength, lectureProfessorLength).split("\0")[0];
            ;
            String lectureTimeAndRoom = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength + lectureCodeLength + lectureSeperatdeNumberLength + lectureProfessorLength, lectureTimeAndRoomlength).split("\0")[0];
            ;
            String maxStudentNumber = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength + lectureCodeLength + lectureSeperatdeNumberLength + lectureProfessorLength + lectureTimeAndRoomlength, maxStudentNumberLength).split("\0")[0];
            ;
            String curStudentNumber = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + listenLevelLength + lectureNameLength + lectureCodeLength + lectureSeperatdeNumberLength + lectureProfessorLength + lectureTimeAndRoomlength + maxStudentNumberLength, curStudentNumberLength).split("\0")[0];

            System.out.print("NO." + i);
            System.out.print(" 이수학년 : " + listenLevel);
            System.out.print(" 과목이름 : " + lectureName);
            System.out.print(" 과목코드-분반 : " + lectureCode + "-" + lectureSeperatdeNumber);
            System.out.print(" 담당 교수 : " + lectureProfessor);
            System.out.print(" 강의실/강의시간 : " + lectureTimeAndRoom);
            System.out.print(" 최대 수강 인원 : " + maxStudentNumber);
            System.out.println(" 현재 수강 인원 : " + curStudentNumber);
        }
    }

    void viewLectureEnrolledStudentList(Protocol getProtocol,byte[] data) {//개설 교과목의 수강 신청 학생 조회시 서버에서 받은 패킷의 정보 꺼내 출력.
        Scanner s=new Scanner(System.in);
        int cnt = 0;
        int idLen = 20*3+1;;
        int nameLen = 20*3+1;
        int levelLen = 5+1;
        int departmentNameLen = 15*3+1;
        String str = " ";

        int oneLineLen=idLen+nameLen+levelLen+departmentNameLen;


        for (int i = 0; i < getProtocol.getPacket().length / oneLineLen; i++) {
            String id = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i), idLen).split("\0")[0];
            String name = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + idLen, nameLen).split("\0")[0];
            String level = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + idLen+nameLen, levelLen).split("\0")[0];
            String departmentName = new String(data, getProtocol.LEN_HEADER + (oneLineLen * i) + idLen+nameLen+levelLen, departmentNameLen).split("\0")[0];

            System.out.print(" 이수학번 : " + id);
            System.out.print(" 학생이름 : " + name);
            System.out.print(" 학년 : " + level);
            System.out.println(" 학과 : " + departmentName);

            ++cnt;

            if(pagingNum == cnt){
                while(true) {
                    System.out.print("다음 페이지를 보시겠습니까? (Y/N)");
                    str = s.nextLine();
                    if(str.equals("Y") || str.equals("N")){break;}
                    else{System.out.println("잘못 입력하셨습니다. 다시 입력하십시오.");}
                }
                pagingNum = pagingNum*2;
            }

            if(str.equals("N")){break;}
        }
    }

    void viewSyllabusList(Protocol getProtocol,byte[] data) {//강의 계획서 (+주차 정보) 조회시 서버에서 받은 패킷의 정보 꺼내 출력.
        if(getProtocol.getLength()==0) {
            return;
        }
        int lectureCodelength = 20 * 3 + 1;
        int lectureSeperatdeNumberlength = 5 + 1;
        int lectureNamelength = 20 * 3 + 1;
        int porfessorNamelength = 20 * 3 + 1;
        int lectureCreditlength = 5 + 1;
        int lectureTimeCountlength = 5 + 1;
        int booknamelength = 30 * 3 + 1;
        int lectureGoallength = 30 * 3 + 1;

        int weeklength = 5 + 1;
        int subjectlength = 20 * 3 + 1;
        int contentlength = 50 * 3 + 1;
        int assigmentlength = 30 * 3 + 1;
        int evaluationlength = 30 * 3 + 1;

        int syllabuslength = lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength + porfessorNamelength + lectureCreditlength + lectureTimeCountlength + booknamelength + lectureGoallength;
        int SyllabusWeekInfoOneLineLength = weeklength + subjectlength + contentlength + assigmentlength + evaluationlength;

        String lectureCode = new String(data, getProtocol.LEN_HEADER, lectureCodelength).split("\0")[0];
        String lectureSeperatdeNumber = new String(data, getProtocol.LEN_HEADER + lectureCodelength, lectureSeperatdeNumberlength).split("\0")[0];
        String lectureName = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength, lectureNamelength).split("\0")[0];
        String porfessorName = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength, porfessorNamelength).split("\0")[0];
        String lectureCredit = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength + porfessorNamelength, lectureCreditlength).split("\0")[0];
        String lectureTimeCount = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength + porfessorNamelength + lectureCreditlength, lectureTimeCountlength).split("\0")[0];
        String bookname = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength + porfessorNamelength + lectureCreditlength + lectureTimeCountlength, booknamelength).split("\0")[0];
        String lectureGoal = new String(data, getProtocol.LEN_HEADER + lectureCodelength + lectureSeperatdeNumberlength + lectureNamelength + porfessorNamelength + lectureCreditlength + lectureTimeCountlength + booknamelength, lectureGoallength).split("\0")[0];
        System.out.print("과목코드-분반 :" + lectureCode + "-" + lectureSeperatdeNumber);
        System.out.print(" 과목명 :" + lectureName);
        System.out.print(" 교수명 :" + porfessorName);
        System.out.print(" 학점및 강의시간 :" + lectureCredit + "/" + lectureTimeCount);
        System.out.print(" 교재명 :" + bookname);
        System.out.println(" 교육목표 :" + lectureGoal);

        for (int i = 0; (getProtocol.getPacket().length - syllabuslength) / SyllabusWeekInfoOneLineLength > i; i++) {
            String week = new String(data, getProtocol.LEN_HEADER + syllabuslength + (SyllabusWeekInfoOneLineLength * i), weeklength).split("\0")[0];
            String subject = new String(data, getProtocol.LEN_HEADER + syllabuslength + (SyllabusWeekInfoOneLineLength * i) + weeklength, subjectlength).split("\0")[0];
            String content = new String(data, getProtocol.LEN_HEADER + syllabuslength + (SyllabusWeekInfoOneLineLength * i) + weeklength + subjectlength, contentlength).split("\0")[0];
            String assigment = new String(data, getProtocol.LEN_HEADER + syllabuslength + (SyllabusWeekInfoOneLineLength * i) + weeklength + subjectlength + contentlength, assigmentlength).split("\0")[0];
            String evaluation = new String(data, getProtocol.LEN_HEADER + syllabuslength + (SyllabusWeekInfoOneLineLength * i) + weeklength + subjectlength + contentlength + assigmentlength, evaluationlength).split("\0")[0];

            System.out.print(week + "주차");
            System.out.print(" 주제 :" + subject);
            System.out.print(" 내용 :" + content);
            System.out.print(" 과제 :" + assigment);
            System.out.println(" 평가 :" + evaluation);
        }
    }

    private void viewProfessorSchedule(Protocol getProtocol, byte[] viewData) {//교수 개인 시간표 조회시 서버에서 받은 패킷의 정보 꺼내 출력.
        String schedule = new String(viewData, getProtocol.LEN_HEADER, (viewData.length-getProtocol.LEN_HEADER));

        System.out.println(schedule);
    }
}
