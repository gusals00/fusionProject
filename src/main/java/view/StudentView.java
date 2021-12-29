package view;

import network.StudentCode;
import lombok.Getter;
import lombok.Setter;
import network.Protocol;
import controller.StudentController;

import java.util.Scanner;

@Getter
@Setter
public class StudentView {
    private String id;
    private String name;
    private StudentController studentController;

    public StudentView() {
    }

    public Protocol studentResView(Protocol getProtocol) {
        studentController = new StudentController();
        try{
            if (getProtocol != null) {
                Translate(getProtocol);//들어온 프로토콜이 있을경우 프로토콜 해석함수로 보내준다.
            }
        }catch(Exception e){
            System.out.println("오류가 발생했습니다. 다시 입력하십시오.");
        }

        Scanner s = new Scanner(System.in);
        int number = 0;
        while(true) {
            System.out.println("학생 id : " + id);
            menu();//메뉴 출력
            number = s.nextInt();

            if(number==1){
                System.out.println("변경할 정보를 정해주세요");
                System.out.println("1. 이름 ,2. 비밀번호");
                int num=s.nextInt();//실행할 작업번호를 입력 받다
                switch (num){
                    case 1:
                        System.out.println("변경할 이름을 입력해주세요");
                        String name=s.next();//변경할 이름 입력받기
                        return studentController.requestUpdateName(id,name);

                    case 2:
                        System.out.println("변경할 비빌번호를 입력해주세요");
                        String PW=s.next();//변경할 비밀번호 입력받기
                        return studentController.requestUpdatePw(id,PW);
                }
            }
            else if(number==2) {
                System.out.println("1. 수강 신청 , 2. 수강 취소");
                int Tnum = s.nextInt();//실행할 작업번호를 입력 받다

                if (Tnum == 1) {
                    System.out.println("과목 코드를 입력 해주세요. ");
                    System.out.print("입력 :");
                    String lectureCode = s.next();//과목코드를 입력받는다
                    System.out.println("분반을 입력 해주세요. ");
                    System.out.print("입력 :");//분반을 입력을 받는다
                    int lectureSeperatdeNumber = s.nextInt();
                    return studentController.requestEnrolment(id, lectureCode, lectureSeperatdeNumber);
                } else if (Tnum == 2) {
                    System.out.println("과목 코드를 입력 해주세요. ");
                    System.out.print("입력 :");//과목코드를 입력받는다
                    String lectureCode = s.next();
                    System.out.println("분반을 입력 해주세요. ");
                    System.out.print("입력 :");//분반을 입력을 받는다
                    int lectureSeperatdeNumber = s.nextInt();
                    return studentController.requestCancleEnrolment(id, lectureCode, lectureSeperatdeNumber);
                }
            }
            else if(number==3){
                System.out.println("1. 전체조회 ,2. 학년으로 조회 ,3. 교수이름으로 조회,4. 학년,교수이름으로 조회");
                int num=s.nextInt();//실행할 작업번호를 입력 받다
                String level;
                String name;
                switch (num) {
                    case 1:
                        return studentController.requestLevelandPorfessrName();//전체조회
                    case 2:
                        System.out.print("조회할 학년을 입력해주세요 :");
                        level = s.next();//학년정보를 입력 받는다.
                        return studentController.requestLevel(level);//입력된 학년정보를 통해 조회한다.
                    case 3:
                        System.out.print("조회할 교수님 이름을을 입력해주세요 :");
                        name = s.next();//교수이름을 입력 받는다
                        return studentController.requestPorfessrName(name);//입력된 교수이름를 통해 조회한다.
                    case 4:
                        System.out.print("조회할 학년과 교수님 이름을을 입력해주세요 :");
                        level = s.next();//학년정보를 입력 받는다.
                        name = s.next();//교수이름을 입력 받는다
                        return studentController.requestLevelandPorfessrName(level, name);//입력된 학년정보과 교수이름을 통해 조회한다.

                }
            }

            else if(number==4){
                System.out.println("과목 코드를 입력 해주세요. ");
                System.out.print("입력 :");
                String lectureCode = s.next();//과목코드를 입력받는다
                System.out.println("분반을 입력 해주세요. ");
                System.out.print("입력 :");
                int lectureSeperatdeNumber=s.nextInt();//분반을 입력을 받는다
                if(lectureCode!=null&&lectureSeperatdeNumber!=0){
                    return studentController.requestSyllabus(lectureCode,lectureSeperatdeNumber);//과목코드와 분반을 통해 강의계획서를 출력한다.
                }
            }
            else if(number==5)
                return studentController.requestTimetable(id);//저장되어있는 ID를 사용해서 시간표를 출력한다.

            else if(number==6)
                return studentController.logoutReqProtocol();
            else
                System.out.println("잘못된 번호입니다. 다시 입력하십시오.");

        }
    }

    public void menu() {//메뉴 출력을 위한 코드
        System.out.println("-----학생 페이지-----");
        System.out.println("1. 개인정보 및 비밀번호 수정");
        System.out.println("2. 수강 신청/취소");
        System.out.println("3. 개설 교과목 목록 조회");
        System.out.println("4. 선택 교과목 강의 계획서 조회");
        System.out.println("5. 본인 시간표 조회");
        System.out.println("6. 로그아웃");
        System.out.print("입력 : ");
    }


    void Translate(Protocol getProtocol){//전송받은 프로토콜을 해석하기위한 장소이다.
        byte[] data=getProtocol.getPacket(); //프로토콜의 데이터 정보를 미리 따로 저정한다.
        if(getProtocol.getProtocolCode()==StudentCode.RESULT_READ_OPEN_LECTURE_LIST.getCode()){//코드가 개설 교과목 목록(전학년) 조회 결과가 들어온 경우.
            TranslateCaseRESULT_READ_OPEN_LECTURE_LIST(getProtocol,data);//들어온 프로토콜과 데이터를 해석하여 출력한다.
        }else if(getProtocol.getProtocolCode()==StudentCode.RESULT_READ_SELECTED_LECTURE_SYLLABUS.getCode()){//코드가 선택 교과목 강의 계획서 조회 결과가 들어온 경우.
            TranslateCaseRESULT_READ_SELECTED_LECTURE_SYLLABUS(getProtocol,data);//들어온 프로토콜과 데이터를 해석하여 출력한다.
        }else if(getProtocol.getProtocolCode()==StudentCode.SUCCESS_CREATE_UPDATE_DELETE.getCode()){//코드가 시간표를 입력을 받을 경우가 들어온다.
            TranslateCaseSUCCESS_CREATE_UPDATE_DELETE(getProtocol,data);//들어온 프로토콜과 데이터를 해석하여 출력한다.
        }else if(getProtocol.getProtocolCode()==StudentCode.FAILURE_CREATE_UPDATE_DELETE_READ.getCode()){//성공한 경우
            TranslateCaseFAILURE_CREATE_UPDATE_DELETE_READ(getProtocol,data);//들어온 프로토콜과 데이터를 해석하여 출력한다.
        }else if(getProtocol.getProtocolCode()==StudentCode.RESULT_READ_OWN_SCHEDULE.getCode()){//실패한 경우
            TranslateCaseRESULT_READ_OWN_SCHEDULE(getProtocol,data);//들어온 프로토콜과 데이터를 해석하여 출력한다.
        }
    }

    /*
    TranslateCaseRESULT_READ_OPEN_LECTURE_LIST 개설 교과목을 해석한다.
    위에 length들은 들어오는 데이터의 길이를 미리 알기 때문에 길이를 정해 준다.
    이후 for 문에 안에서 데이터들을 한줄실 읽을수 있게 위에 String부분은 한줄의 데이터를 필여에 의하여 짜으로 아래 print 에서 내용을 출력한다.
     */
    void TranslateCaseRESULT_READ_OPEN_LECTURE_LIST(Protocol getProtocol,byte[] data){
        int listenLevelLength=5+1;
        int lectureNameLength=20*3+1;
        int lectureCodeLength=20*3+1;
        int lectureSeperatdeNumberLength=5+1;
        int lectureProfessorLength=20*3+1;
        int lectureTimeAndRoomlength=20*3+20*3+1;
        int maxStudentNumberLength=5+1;
        int curStudentNumberLength=5+1;
        int CanapplySLength=10*3+1;

        int oneLineLen=listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength+curStudentNumberLength+CanapplySLength;

        for(int i=0;i<getProtocol.getPacket().length/oneLineLen;i++){
            String listenLevel=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i),listenLevelLength).split("\0")[0];
            String lectureName=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength,lectureNameLength).split("\0")[0];;
            String lectureCode=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength,lectureCodeLength).split("\0")[0];;
            String lectureSeperatdeNumber=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength,lectureSeperatdeNumberLength).split("\0")[0];;
            String lectureProfessor=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength,lectureProfessorLength).split("\0")[0];;
            String lectureTimeAndRoom=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength,lectureTimeAndRoomlength).split("\0")[0];;
            String maxStudentNumber=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength,maxStudentNumberLength).split("\0")[0];;
            String curStudentNumber=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength,curStudentNumberLength).split("\0")[0];
            String registerAvailable=new String(data,getProtocol.LEN_HEADER+(oneLineLen*i)+listenLevelLength+lectureNameLength+lectureCodeLength+lectureSeperatdeNumberLength+lectureProfessorLength+lectureTimeAndRoomlength+maxStudentNumberLength+curStudentNumberLength,CanapplySLength).split("\0")[0];


            System.out.print("NO."+(i+1));
            System.out.print(" 이수학년 : "+listenLevel);
            System.out.print(" 과목이름 : "+lectureName);
            System.out.print(" 과목코드-분반 : "+lectureCode+"-"+lectureSeperatdeNumber);
            System.out.print(" 담당 교수 : "+lectureProfessor);
            System.out.print(" 최대 수강 인원 : "+maxStudentNumber);
            System.out.print(" 현재 수강 인원 : "+curStudentNumber);
            System.out.print(" 강의실/강의시간 : "+lectureTimeAndRoom);
            System.out.println(" 수강신청 가능여부: "+registerAvailable);


        }
    }
    /*
     TranslateCaseRESULT_READ_OPEN_LECTURE_LIST 개설 교과목을 해석한다.
     위에 length들은 들어오는 데이터의 길이를 미리 알기 때문에 길이를 정해 준다.
     이후 for 문에 안에서 데이터들을 한줄실 읽을수 있게 위에 String부분은 한줄의 데이터를 필여에 의하여 짜으로 아래 print 에서 내용을 출력한다.
      */
    void TranslateCaseRESULT_READ_SELECTED_LECTURE_SYLLABUS(Protocol getProtocol,byte[] data){
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

        int syllabuslength=lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+ lectureCreditlength+lectureTimeCountlength+booknamelength+lectureGoallength;
        int SyllabusWeekInfoOneLineLength=weeklength+subjectlength+contentlength+assigmentlength+evaluationlength;

        String lectureCode=new String(data,getProtocol.LEN_HEADER,lectureCodelength).split("\0")[0];
        String lectureSeperatdeNumber=new String(data,getProtocol.LEN_HEADER+lectureCodelength,lectureSeperatdeNumberlength).split("\0")[0];
        String lectureName=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength,lectureNamelength).split("\0")[0];
        String porfessorName=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength,porfessorNamelength).split("\0")[0];
        String lectureCredit=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength,lectureCreditlength).split("\0")[0];
        String lectureTimeCount=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+lectureCreditlength,lectureTimeCountlength).split("\0")[0];
        String bookname=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+lectureCreditlength+lectureTimeCountlength,booknamelength).split("\0")[0];
        String lectureGoal=new String(data,getProtocol.LEN_HEADER+lectureCodelength+lectureSeperatdeNumberlength+lectureNamelength+porfessorNamelength+lectureCreditlength+lectureTimeCountlength+booknamelength,lectureGoallength).split("\0")[0];
        System.out.print("과목코드-분반 :"+lectureCode+"-"+lectureSeperatdeNumber);
        System.out.print(" 과목명 :"+lectureName);
        System.out.print(" 교수명 :"+porfessorName);
        System.out.print(" 학점및 강의시간 :"+lectureCredit+"/"+lectureTimeCount);
        System.out.print(" 교재명 :"+bookname);
        System.out.println(" 교육목표 :"+lectureGoal);

        for(int i=0;(getProtocol.getPacket().length-syllabuslength)/SyllabusWeekInfoOneLineLength>i;i++){
            String week=new String(data,getProtocol.LEN_HEADER+syllabuslength+(SyllabusWeekInfoOneLineLength*i),weeklength).split("\0")[0];
            String subject=new String(data,getProtocol.LEN_HEADER+syllabuslength+(SyllabusWeekInfoOneLineLength*i)+weeklength,subjectlength).split("\0")[0];
            String content=new String(data,getProtocol.LEN_HEADER+syllabuslength+(SyllabusWeekInfoOneLineLength*i)+weeklength+subjectlength,contentlength).split("\0")[0];
            String assigment=new String(data,getProtocol.LEN_HEADER+syllabuslength+(SyllabusWeekInfoOneLineLength*i)+weeklength+subjectlength+contentlength,assigmentlength).split("\0")[0];
            String evaluation=new String(data,getProtocol.LEN_HEADER+syllabuslength+(SyllabusWeekInfoOneLineLength*i)+weeklength+subjectlength+contentlength+assigmentlength,evaluationlength).split("\0")[0];

            System.out.print(week+"주차");
            System.out.print(" 주제 :"+subject);
            System.out.print(" 내용 :"+content);
            System.out.print(" 과제 :"+assigment);
            System.out.println(" 평가 :"+evaluation);
        }


    }

    void TranslateCaseSUCCESS_CREATE_UPDATE_DELETE(Protocol getProtocol,byte[] data){
        int successLength=20*3+1;
        String success=new String(data,getProtocol.LEN_HEADER,successLength).split("\0")[0];
        System.out.println(success);
    }
    void TranslateCaseFAILURE_CREATE_UPDATE_DELETE_READ(Protocol getProtocol,byte[] data){
        int failLength=30*3+1;
        String fail=new String(data,getProtocol.LEN_HEADER,failLength).split("\0")[0];
        System.out.println(fail);
    }
    void TranslateCaseRESULT_READ_OWN_SCHEDULE(Protocol getProtocol,byte[] data){
        int resultLen=data.length-getProtocol.LEN_HEADER;
        String result=new String(data,getProtocol.LEN_HEADER,resultLen).split("\0")[0];
        System.out.println(result);
    }
}
